package com.tanthanh.paymentcommon.model;

import lombok.*;
import org.springframework.http.HttpStatus;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ErrorMessage {
    private String code;
    private String message;
    private HttpStatus status;


}
