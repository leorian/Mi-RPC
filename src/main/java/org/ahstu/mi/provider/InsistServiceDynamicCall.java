package org.ahstu.mi.provider;

import com.bozhong.common.util.StringUtil;
import org.ahstu.mi.common.*;
import org.ahstu.mi.dynamic.InsistDynamicCallService;
import org.ahstu.mi.dynamic.InsistDynamicCallServiceImpl;
import org.apache.commons.beanutils.MethodUtils;

import java.lang.reflect.InvocationTargetException;

/**
 * Created by renyueliang on 17/5/18.
 */
public class InsistServiceDynamicCall {
    private static final InsistDynamicCallService IPM = new InsistDynamicCallServiceImpl();

    public static MiResult call(MiSendDTO sendDTO) {
        ThreadServerLocalUtil.set(MiConstants.REMOTE_CLIENT_IP, sendDTO.getClientIp());
        ThreadServerLocalUtil.set(MiConstants.REQUEST_ID, sendDTO.getRequestId());
        MiResult insistResult = new MiResult();
        InsistProviderMeta providerMeta =
                InsistProviderStore.get(InsistUtil.serviceGroupVersionCreateKey(sendDTO.getInterfaceName(), sendDTO.getGroup(), sendDTO.getVersion()));
        if (sendDTO.getInterfaceName().equals(InsistDynamicCallService.class.getName())) {
            providerMeta = new InsistProviderMeta();
            providerMeta.setRef(IPM);
        }

        if (providerMeta == null) {
            insistResult.setErrorCode(MiError.NOT_FIND_SERVICE_PROVIDER.getErrorCode());
            insistResult.setSuccess(false);
            recordLog(sendDTO, null, MiError.NOT_FIND_SERVICE_PROVIDER.getErrorCode());
            return insistResult;
        }

        try {
            Object target = MethodUtils.invokeExactMethod(providerMeta.getRef(),
                    sendDTO.getMethod(),
                    sendDTO.getArgs(),
                    sendDTO.getArgsType());

            insistResult.setModule(target);
            insistResult.setSuccess(true);

        } catch (NoSuchMethodException nsme) {
            insistResult.setSuccess(false);
            insistResult.setErrorCode(MiError.NOT_FIND_METHOD_EXCEPTION.getErrorCode());
            recordLog(sendDTO, nsme, MiError.NOT_FIND_METHOD_EXCEPTION.getErrorCode());
        } catch (IllegalAccessException iae) {
            insistResult.setSuccess(false);
            insistResult.setErrorCode(MiError.ILLEGAL_ACCESS_EXCEPTION.getErrorCode());
            recordLog(sendDTO, iae, MiError.NOT_FIND_METHOD_EXCEPTION.getErrorCode());
        } catch (InvocationTargetException ite) {
            insistResult.setSuccess(false);
            insistResult.setErrorCode(MiError.INVOCATION_TARGET_EXCEPTION.getErrorCode());
            recordLog(sendDTO, ite, MiError.INVOCATION_TARGET_EXCEPTION.getErrorCode());
        } catch (Throwable e) {
            insistResult.setSuccess(false);
            insistResult.setErrorCode(e.getMessage());
            recordLog(sendDTO, e, e.getMessage());
        }
        return insistResult;
    }

    private static void recordLog(MiSendDTO sendDTO, Throwable e, String errorCode) {
        if (e == null) {
            MiLogger.record(
                    StringUtil.format("InsistServiceDynamicCall.call error ! serviceName:%s,group:%s,version:%s,method:%s,clientIp:%s,requestId:%s errorCode:NOT_FIND_SERVICE_PROVIDER" + errorCode,
                            sendDTO.getInterfaceName(),
                            sendDTO.getGroup(),
                            sendDTO.getVersion(),
                            sendDTO.getMethod(),
                            sendDTO.getClientIp(),
                            sendDTO.getRequestId())
            );
        } else {
            MiLogger.record(
                    StringUtil.format("InsistServiceDynamicCall.call error ! serviceName:%s,group:%s,version:%s,method:%s,clientIp:%s,requestId:%s errorCode:NOT_FIND_SERVICE_PROVIDER" + errorCode,
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
