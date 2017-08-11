package org.ahstu.mi.common;

/**
 * Created by renyueliang on 17/5/15.
 */
public class MiException extends RuntimeException {

    private String errorCode;
    private String errorMsg;


    public MiException(String errorCode){
        super(errorCode);
        this.errorCode=errorCode;
    }

    public MiException(String errorCode, String errorMsg){
        super(errorCode);
        this.errorCode=errorCode;
        this.errorMsg=errorMsg;
    }

    public MiException(String errorCode, Throwable e){
        super(errorCode,e);
        this.errorCode=errorCode;
    }


    public MiException(String errorCode, String errorMsg, Throwable e){
        super(errorCode,e);
        this.errorCode=errorCode;
        this.errorMsg=errorMsg;
    }


    public MiException(MiError error){
        super(error.getErrorCode());
        this.errorCode=error.getErrorCode();
        this.errorMsg=error.getErrorMsg();
    }

    public MiException(MiError error, Throwable e){
        super(error.getErrorCode(),e);
        this.errorCode=error.getErrorCode();
        this.errorMsg=error.getErrorMsg();
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
