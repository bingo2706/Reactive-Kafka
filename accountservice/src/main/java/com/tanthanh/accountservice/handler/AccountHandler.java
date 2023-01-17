package com.tanthanh.accountservice.handler;

import com.tanthanh.accountservice.dto.AccountDTO;
import com.tanthanh.accountservice.service.iml.IAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class AccountHandler {

    @Autowired
    IAccountService service;

    public Mono<ServerResponse> GetAllAccount(ServerRequest request){
        Flux<AccountDTO> list = service.getAllAccount();
        return ServerResponse.ok().body(list,AccountDTO.class);
    }
}
