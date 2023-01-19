package com.tanthanh.paymentprocessingservice.dto;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class PaymentDTO {
    private long id;
    private int accountId;
    private double amount;
    private String status;

}
