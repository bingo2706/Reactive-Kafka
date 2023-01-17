package com.tanthanh.accountservice.repository;

import com.tanthanh.accountservice.data.Account;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface AccountRepository extends ReactiveCrudRepository<Account,Long> {
    Mono<Account> findByEmail(String email);
}
