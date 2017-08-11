package org.ahstu.mi.dynamic;

import com.bozhong.common.util.StringUtil;
import org.ahstu.mi.common.*;
import org.ahstu.mi.consumer.MiConsumerMeta;
import org.ahstu.mi.consumer.manager.MiPullProvider;
import org.ahstu.mi.consumer.manager.MiServiceStore;
import org.ahstu.mi.module.ServiceMeta;
import org.ahstu.mi.rpc.netty.client.NettyChannelHandlerStore;

import java.lang.reflect.Method;

/**
 * Created by xiezg@317hu.com on 2017/6/8 0008.
 */
public class MiDynamicCallClient {
    private final MiConsumerMeta meta;
    private final MiDynamicDTO miDynamicDTO;

    public MiDynamicCallClient(String serviceName, String group, String version, String methodName, String param) {
        this.meta = new MiConsumerMeta();
        this.meta.setInterfaceName(serviceName);
        this.meta.setGroup(group);
        this.meta.setVersion(version);
        MiPullProvider.pull(this.meta);
        this.miDynamicDTO = new MiDynamicDTO();
        this.miDynamicDTO.setServiceName(serviceName);
        this.miDynamicDTO.setGroup(group);
        this.miDynamicDTO.setVersion(version);
        this.miDynamicDTO.setMethod(methodName);
        this.miDynamicDTO.setParam(param);
    }

    public MiResult fetchServiceMethods() {
        return rmiCall("listInterfaceMethod");
    }

    public MiResult fetchResult() {
       return rmiCall("dynamicCallMethod");
    }

    private MiResult rmiCall(String methodName) {
        MiSendDTO sendDTO;
        String requestId = MiUtil.getRequestId();
        MiResult result = null;
        int index = 1;
        boolean callSuccess = false;
        ServiceMeta serviceMeta = null;
        while (!callSuccess && index <= 3) {
            try {
                ++index;
                serviceMeta = MiServiceStore.getServiceMeta(this.meta);
                if (serviceMeta == null) {
                    throw new MiException(MiError.NOT_FIND_SERVICE);
                }

                sendDTO = createInsistSendDTO(requestId,
                        MiDynamicCallService.class.getMethod(methodName, MiDynamicDTO.class),
                        new Object[]{this.miDynamicDTO}, serviceMeta.getIp(), serviceMeta.getPort());
                result = RemoteExcutorFactory.getRpcExcutor().remoteCall(sendDTO);
                callSuccess = true;
            } catch (MiException ie) {

                MiLogger.record(StringUtil.format("MiConsumerHandler.excute error ! " +
                                "requestId:%s,serviceName:%s,group:%s,version:%s errorCode:%s",
                        requestId,
                        this.meta.getInterfaceName(),
                        this.meta.getGroup(),
                        this.meta.getVersion(),
                        ie.getErrorCode()), ie);

                if (MiError.NOT_FIND_SERVICE.getErrorCode().equals(ie.getErrorCode())) {
                    //服务不存在异常
                    throw new MiException(MiError.NOT_FIND_SERVICE, ie);
                } else if (MiError.CLIENT_TIME_OUT.getErrorCode().equals(ie.getErrorCode())) {
                    //抛出异常
                    throw new MiException(MiError.CLIENT_TIME_OUT, ie);

                } else if (MiError.CLOSED_SELECTOR_EXCEPTION.getErrorCode().equals(ie.getErrorCode())) {
                    //删除服务的本地地址 找到服务删除这个IP
                    MiServiceStore.delAllServiceByServiceMeta(serviceMeta);
                    NettyChannelHandlerStore.remove(MiUtil.ipAndPortCreateKey(serviceMeta));
                    if (index > 3) {
                        throw new MiException(MiError.CLOSED_SELECTOR_EXCEPTION, ie);
                    }

                } else if (MiError.CONNECTION_INTERRUPT.getErrorCode().equals(ie.getErrorCode())) {
                    //删除服务的本地地址 找到服务删除这个IP
                    NettyChannelHandlerStore.remove(MiUtil.ipAndPortCreateKey(serviceMeta));
                    if (index > 3) {
                        throw new MiException(MiError.CONNECTION_INTERRUPT, ie);
                    }
                }
            } catch (Throwable e) {

                MiLogger.record(StringUtil.format("MiConsumerHandler.excute error ! " +
                                "requestId:%s,serviceName:%s,group:%s,version:%s errorCode:%s",
                        requestId,
                        this.meta.getInterfaceName(),
                        this.meta.getGroup(),
                        this.meta.getVersion(),
                        e.getMessage()), e);

                throw new MiException(e.getMessage(), e);

            } finally {
                if (index > 3) {
                    callSuccess = true;
                }
            }
        }

        return result;
    }

    private MiSendDTO createInsistSendDTO(String requestId, Method method, Object[] args, String ip, int port) {
        return new MiSendDTO(
                requestId,
                MiDynamicCallService.class.getName(),
                method.getName(),
                args,
                method.getParameterTypes(),
                ip,
                port,
                MiDynamicCallConstants.GROUP,
                3000,
                MiUtil.geLocalIp(),
                MiDynamicCallConstants.VERSION
        );
    }

}
