package com.tanthanh.accountservice.dto;

import com.tanthanh.accountservice.data.Account;
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



    public static AccountDTO entityToModel(Account account){
        return AccountDTO.builder()
                .email(account.getEmail())
                .currency(account.getCurrency())
                .balance(account.getBalance())
                .reserved(account.getReserved())
                .id(account.getId())
                .build();
    }
}
