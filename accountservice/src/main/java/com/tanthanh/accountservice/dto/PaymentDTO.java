package com.tanthanh.accountservice.dto;

import lombok.*;
import org.springframework.data.annotation.Id;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class PaymentDTO {
    @Id
    private long id;
    private int accountId;
    private double amount;
    private String status;



}
