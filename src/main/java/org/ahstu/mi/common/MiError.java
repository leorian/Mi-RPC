package org.ahstu.mi.common;

/**
 * Created by renyueliang on 17/5/15.
 */
public enum MiError {

    NOT_FIND_SERVICE("NOT_FIND_SERVICE","找不到服务"),
    CLIENT_TIME_OUT("CLIENT_TIME_OUT","客户端超时"),
    MI_ADDLOCK_ISNULL("MI_ADDLOCK_ISNULL","加锁异常，锁不存在或者ID是空"),
    MI_ADDLOCK_EXIST("MI_ADDLOCK_EXIST","加锁异常，锁ID重复"),
    MI_ILLEGAL_ARGUMENT("MI_ILLEGAL_ARGUMENT","INSIST参数异常"),
    MI_ZK_HOST_ISNULL("MI_ZK_HOST_ISNULL","注册地址是空"),
    MI_IPPORTKEY_ISNULL("MI_IPPORTKEY_ISNULL","IP和PORT是空"),
    CONNECTION_TIMIEOUT("CONNECTION_TIMIEOUT","连接服务端超时"),
    CLOSED_SELECTOR_EXCEPTION("CLOSED_SELECTOR_EXCEPTION","nettySelector关闭"),
    CONNECTION_INTERRUPT("CONNECTION_INTERRUPT","连接中断"),
    NOT_FIND_SERVICE_PROVIDER("NOT_FIND_SERVICE_PROVIDER","服务端找不到该服务"),
    NOT_FIND_METHOD_EXCEPTION("NOT_FIND_METHOD_EXCEPTION","找不到方法的异常"),
    ILLEGAL_ACCESS_EXCEPTION("ILLEGAL_ACCESS_EXCEPTION","反射参数异常"),
    INVOCATION_TARGET_EXCEPTION("INVOCATION_TARGET_EXCEPTION","服务端调用异常"),
    PORT_ALREADY_IN_USE_EXCEPTION("PORT_ALREADY_IN_USE_EXCEPTION","端口被占用"),
    CLIENT_META_REGISTER_EXCEPTION("CLIENT_META_REGISTER_EXCEPTION","客户端注册失败"),
    SERVICE_META_REGISTER_EXCEPTION("SERVICE_META_REGISTER_EXCEPTION","服务端注册失败"),
    MI_RESULT_IS_NULL("MI_RESULT_IS_NULL","返回insist-result为空")
    ;




    ;

    private String errorCode;
    private String errorMsg;

    private MiError(String errorCode, String errorMsg){
        this.errorCode=errorCode;
        this.errorMsg=errorMsg;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }
}
