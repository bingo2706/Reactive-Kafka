package com.tanthanh.paymentservice.dto;

import com.tanthanh.paymentservice.data.Payment;
import lombok.*;
import org.springframework.data.annotation.Id;

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


    public static PaymentDTO entityToDto(Payment payment){
        return  PaymentDTO.builder()
                .id(payment.getId())
                .accountId(payment.getAccountId())
                .amount(payment.getAmount())
                .status(payment.getStatus())
                .build();
    }
    public static Payment dtoToEntity(PaymentDTO paymentDTO){
        return  Payment.builder()
                .accountId(paymentDTO.getAccountId())
                .amount(paymentDTO.getAmount())
                .status(paymentDTO.getStatus())
                .build();
    }
}
