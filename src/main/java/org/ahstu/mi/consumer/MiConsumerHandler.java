package org.ahstu.mi.consumer;

import org.ahstu.mi.common.StringUtil;
import org.ahstu.mi.common.*;
import org.ahstu.mi.consumer.manager.MiPullProvider;
import org.ahstu.mi.consumer.manager.MiServiceStore;
import org.ahstu.mi.module.ServiceMeta;
import org.ahstu.mi.rpc.netty.client.NettyChannelHandlerStore;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * Created by renyueliang on 17/5/15.
 */
public class MiConsumerHandler implements InvocationHandler {

    private MiConsumerMeta meta;
    private ServiceMeta serviceMeta;

    public MiConsumerHandler(MiConsumerMeta meta ){
        this.meta=meta;
    }


    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

//        if(!method.getDeclaringClass().getName().equals(meta.getInterfaceName()) || !meta.isThisClassMethod(method.getName())){
//            return null;
//        }

        String methodName = method.getName();
        Class<?>[] parameterTypes = method.getParameterTypes();
        if (method.getDeclaringClass() == Object.class) {
            return method.invoke(meta, args);
        }
        if ("toString".equals(methodName) && parameterTypes.length == 0) {
            return meta.toString();
        }
        if ("hashCode".equals(methodName) && parameterTypes.length == 0) {
            return meta.hashCode();
        }
        if ("equals".equals(methodName) && parameterTypes.length == 1) {
            return meta.equals(args[0]);
        }

        MiSendDTO sendDTO ;
        String requestId = MiUtil.getRequestId();
        MiResult result = null;
        int index = 1;
        boolean callSuccess=false;
        while(!callSuccess && index<=3) {
            try {
                ++index;
                this.serviceMeta = MiServiceStore.getServiceMeta(meta);
                if (this.serviceMeta == null) {
                    throw new MiException(MiError.NOT_FIND_SERVICE);
                }
                sendDTO = createMiSendDTO(requestId, method, args);
                result = RemoteExcutorFactory.getRpcExcutor().remoteCall(sendDTO);
                callSuccess=true;
            } catch (MiException ie) {

                MiLogger.record(StringUtil.format("MiConsumerHandler.excute error ! requestId:%s,serviceName:%s,group:%s,version:%s errorCode:%s",
                        requestId,
                        this.meta.getInterfaceName(),
                        this.meta.getGroup(),
                        this.meta.getVersion(),
                        ie.getErrorCode()), ie);

                if (MiError.NOT_FIND_SERVICE.getErrorCode().equals(ie.getErrorCode())) {
                    //防止重复 启动一个线程一个服务一个线程
                    MiPullProvider.pull(meta);
                    //服务不存在异常
                    throw new MiException(MiError.NOT_FIND_SERVICE, ie);
                } else if (MiError.CLIENT_TIME_OUT.getErrorCode().equals(ie.getErrorCode())) {
                    //抛出异常
                    throw new MiException(MiError.CLIENT_TIME_OUT, ie);

                } else if (MiError.CLOSED_SELECTOR_EXCEPTION.getErrorCode().equals(ie.getErrorCode())) {
                    //删除服务的本地地址 找到服务删除这个IP
                    MiServiceStore.delAllServiceByServiceMeta(this.serviceMeta);
                    NettyChannelHandlerStore.remove(MiUtil.ipAndPortCreateKey(this.serviceMeta));
                    if(index>3){
                        throw new MiException(MiError.CLOSED_SELECTOR_EXCEPTION, ie);
                    }

                } else if (MiError.CONNECTION_INTERRUPT.getErrorCode().equals(ie.getErrorCode())) {
                    //删除服务的本地地址 找到服务删除这个IP
                    NettyChannelHandlerStore.remove(MiUtil.ipAndPortCreateKey(this.serviceMeta));
                    if(index>3){
                        throw new MiException(MiError.CONNECTION_INTERRUPT, ie);
                    }
                }
            } catch (Throwable e) {

                MiLogger.record(StringUtil.format("MiConsumerHandler.excute error ! requestId:%s,serviceName:%s,group:%s,version:%s errorCode:%s",
                        requestId,
                        this.meta.getInterfaceName(),
                        this.meta.getGroup(),
                        this.meta.getVersion(),
                        e.getMessage()), e);

                throw new MiException(e.getMessage(), e);

            }finally {
                if(index>3){
                    callSuccess=true;
                }
            }
        }

        if(result==null){
           return null;
        }
        return result.getModule();
    }


    private MiSendDTO createMiSendDTO(String requestId, Method method, Object[] args){
        return new MiSendDTO(
                requestId,
                serviceMeta.getInterfaceName(),
                method.getName(),
                args,
                method.getParameterTypes(),
                serviceMeta.getIp(),
                serviceMeta.getPort(),
                this.meta.getGroup(),
                this.meta.getClientTimeout(),
                MiUtil.geLocalIp(),
                this.meta.getVersion()
        );
    }
}
