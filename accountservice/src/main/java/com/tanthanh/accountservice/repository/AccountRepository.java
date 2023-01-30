package com.tanthanh.accountservice.repository;

import com.tanthanh.accountservice.data.Account;
import com.tanthanh.accountservice.dto.AccountDTO;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface AccountRepository extends ReactiveCrudRepository<Account,Long> {
    Mono<Account> findByEmail(long id,double reserved);

    @Query("CALL reservedAccount(:id,:amount,@result);\n" +
            "SELECT @result")
    Flux<Integer> reserved(long id, double amount);
    @Query("CALL subtractAccount(:id,:amountReversed,:amountBalance,@result);\n" +
            "SELECT @result")
    Flux<Integer> subtract(long id, double amountReversed,double amountBalance);

    @Query("CALL rollbackReserved(:id,:amount,@result);\n" +
            "SELECT @result")
    Flux<Integer> rollbackReserved	(long id, double amount);
}
