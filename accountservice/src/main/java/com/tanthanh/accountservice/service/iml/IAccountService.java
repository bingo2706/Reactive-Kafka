package com.tanthanh.accountservice.service.iml;

import com.tanthanh.accountservice.data.Account;
import com.tanthanh.accountservice.dto.AccountDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface IAccountService {
     Flux<AccountDTO> getAllAccount();
     Mono<AccountDTO> checkBalance(long id);
     Mono<Boolean> reserved(double reserved,long id);
     Mono<AccountDTO> subtract(double amount, long id) ;
}
