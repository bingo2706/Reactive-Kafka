package com.tanthanh.commonservice.model;

import lombok.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;


@ToString
public enum CommonError implements Error{
    INTERNAL("9999", "Unknown internal server error", HttpStatus.INTERNAL_SERVER_ERROR);

     CommonError(String code, String message, HttpStatus status) {
    }
}
