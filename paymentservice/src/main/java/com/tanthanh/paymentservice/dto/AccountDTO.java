package com.tanthanh.paymentservice.dto;


import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class AccountDTO {
    private long id;
    private String email;
    private String currency;
    private double balance;
    private double reserved;
    private boolean isCheckBalance;
}
