package org.ahstu.mi.common;

import java.io.Serializable;

/**
 * Created by renyueliang on 17/5/15.
 */
public class MiResult<T>   implements Serializable {
    private static final long serialVersionUID = 1652432944581878306L;

    private String requestId;

    private String errorCode;

    private boolean success;

    private T module;

    private String message;

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public T getModule() {
        return module;
    }

    public void setModule(T module) {
        this.module = module;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
