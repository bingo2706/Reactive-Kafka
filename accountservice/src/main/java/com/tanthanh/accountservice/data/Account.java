package com.tanthanh.accountservice.data;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table
public class Account {
    @Id
    private long id;
    private String email;
    private String currency;
    private double balance;
    private double reserved;
}
