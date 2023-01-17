package com.tanthanh.accountservice.service.iml;

import com.tanthanh.accountservice.data.Account;
import com.tanthanh.accountservice.dto.AccountDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface IAccountService {
     Flux<AccountDTO> getAllAccount();
     Mono<Boolean> checkBalance(double balance,String email);
     Mono<AccountDTO> reserved(double reserved,String email);
     Mono<AccountDTO> subtract(AccountDTO model) throws Exception;
}
