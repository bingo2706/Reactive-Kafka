package com.tanthanh.accountservice.service.iml;

import com.tanthanh.accountservice.data.Account;
import com.tanthanh.accountservice.dto.AccountDTO;
import io.r2dbc.spi.Parameter;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface IAccountService {
     Flux<AccountDTO> getAllAccount();
     Mono<AccountDTO> checkBalance(long id);
     Mono<Integer> reserved(double reserved,long id);
     Mono<Integer> subtract(double amount, long id) ;
     Mono<Integer> rollBackReserved(double reserved,long id);
}
