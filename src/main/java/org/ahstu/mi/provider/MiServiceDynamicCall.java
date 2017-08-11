package org.ahstu.mi.provider;

import com.bozhong.common.util.StringUtil;
import org.ahstu.mi.common.*;
import org.ahstu.mi.dynamic.MiDynamicCallService;
import org.ahstu.mi.dynamic.MiDynamicCallServiceImpl;
import org.apache.commons.beanutils.MethodUtils;

import java.lang.reflect.InvocationTargetException;

/**
 * Created by renyueliang on 17/5/18.
 */
public class MiServiceDynamicCall {
    private static final MiDynamicCallService IPM = new MiDynamicCallServiceImpl();

    public static MiResult call(MiSendDTO sendDTO) {
        ThreadServerLocalUtil.set(MiConstants.REMOTE_CLIENT_IP, sendDTO.getClientIp());
        ThreadServerLocalUtil.set(MiConstants.REQUEST_ID, sendDTO.getRequestId());
        MiResult miResult = new MiResult();
        MiProviderMeta providerMeta =
                MiProviderStore.get(MiUtil.serviceGroupVersionCreateKey(sendDTO.getInterfaceName(), sendDTO.getGroup(), sendDTO.getVersion()));
        if (sendDTO.getInterfaceName().equals(MiDynamicCallService.class.getName())) {
            providerMeta = new MiProviderMeta();
            providerMeta.setRef(IPM);
        }

        if (providerMeta == null) {
            miResult.setErrorCode(MiError.NOT_FIND_SERVICE_PROVIDER.getErrorCode());
            miResult.setSuccess(false);
            recordLog(sendDTO, null, MiError.NOT_FIND_SERVICE_PROVIDER.getErrorCode());
            return miResult;
        }

        try {
            Object target = MethodUtils.invokeExactMethod(providerMeta.getRef(),
                    sendDTO.getMethod(),
                    sendDTO.getArgs(),
                    sendDTO.getArgsType());

            miResult.setModule(target);
            miResult.setSuccess(true);

        } catch (NoSuchMethodException nsme) {
            miResult.setSuccess(false);
            miResult.setErrorCode(MiError.NOT_FIND_METHOD_EXCEPTION.getErrorCode());
            recordLog(sendDTO, nsme, MiError.NOT_FIND_METHOD_EXCEPTION.getErrorCode());
        } catch (IllegalAccessException iae) {
            miResult.setSuccess(false);
            miResult.setErrorCode(MiError.ILLEGAL_ACCESS_EXCEPTION.getErrorCode());
            recordLog(sendDTO, iae, MiError.NOT_FIND_METHOD_EXCEPTION.getErrorCode());
        } catch (InvocationTargetException ite) {
            miResult.setSuccess(false);
            miResult.setErrorCode(MiError.INVOCATION_TARGET_EXCEPTION.getErrorCode());
            recordLog(sendDTO, ite, MiError.INVOCATION_TARGET_EXCEPTION.getErrorCode());
        } catch (Throwable e) {
            miResult.setSuccess(false);
            miResult.setErrorCode(e.getMessage());
            recordLog(sendDTO, e, e.getMessage());
        }
        return miResult;
    }

    private static void recordLog(MiSendDTO sendDTO, Throwable e, String errorCode) {
        if (e == null) {
            MiLogger.record(
                    StringUtil.format("MiServiceDynamicCall.call error ! serviceName:%s,group:%s,version:%s,method:%s,clientIp:%s,requestId:%s errorCode:NOT_FIND_SERVICE_PROVIDER" + errorCode,
                            sendDTO.getInterfaceName(),
                            sendDTO.getGroup(),
                            sendDTO.getVersion(),
                            sendDTO.getMethod(),
                            sendDTO.getClientIp(),
                            sendDTO.getRequestId())
            );
        } else {
            MiLogger.record(
                    StringUtil.format("MiServiceDynamicCall.call error ! serviceName:%s,group:%s,version:%s,method:%s,clientIp:%s,requestId:%s errorCode:NOT_FIND_SERVICE_PROVIDER" + errorCode,
                            sendDTO.getInterfaceName(),
                            sendDTO.getGroup(),
                            sendDTO.getVersion(),
                            sendDTO.getMethod(),
                            sendDTO.getClientIp(),
                            sendDTO.getRequestId()),
                    e
            );
        }


    }
}
