package com.tanthanh.paymentcommon.common;

import org.springframework.http.HttpStatus;


public class CommonException extends RuntimeException{

    private String code;
    private String message;
    private HttpStatus status;
    public CommonException(String code,String message,HttpStatus status) {

        super(message);
        this.code = code;
        this.message = message;
        this.status = status;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }
}
