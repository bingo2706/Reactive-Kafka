package com.tanthanh.commonservice.common;
import lombok.Data;
import org.springframework.http.HttpStatus;
@Data
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
}
