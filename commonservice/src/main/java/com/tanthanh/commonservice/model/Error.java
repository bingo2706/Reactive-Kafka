package com.tanthanh.commonservice.model;

import org.springframework.http.HttpStatus;

public interface Error {
    String code = "";
    String message = "";
    HttpStatus status = null;

}
