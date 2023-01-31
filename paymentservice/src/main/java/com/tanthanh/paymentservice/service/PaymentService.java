package com.tanthanh.paymentservice.service;

import com.google.gson.Gson;
import com.tanthanh.commonservice.common.CommonException;
import com.tanthanh.paymentservice.Exception.ErrorMessage;
import com.tanthanh.paymentservice.Exception.PaymentException;
import com.tanthanh.paymentservice.dto.AccountDTO;
import com.tanthanh.paymentservice.dto.PaymentDTO;
import com.tanthanh.paymentservice.event.EventProducer;
import com.tanthanh.paymentservice.repository.PaymentRepository;
import com.tanthanh.paymentservice.utils.Constant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.kafka.sender.KafkaSender;

import java.util.Objects;

@Service
@Slf4j
public class PaymentService {
    Gson gson = new Gson();
    @Autowired
    private KafkaSender<String, String> sender;
    @Autowired
    PaymentRepository paymentRepository;
    @Autowired
    WebClient webClientAccount;
    @Autowired
    EventProducer eventProducer;


    public Mono<PaymentDTO> payment(PaymentDTO paymentDTO){
        return webClientAccount.get()
              .uri(uriBuilder -> uriBuilder.path("/checkBalance/{id}")
                      .build(paymentDTO.getAccountId()))

                .exchangeToMono(response ->
                        !response.statusCode().isError() ?
                                response.bodyToMono(AccountDTO.class) :
                                response.bodyToMono(ErrorMessage.class)
                                        .flatMap(errorMessage ->
                                                Mono.error(new PaymentException(errorMessage.getMessage()))))
                .flatMap(accountDTO -> {

                    if(paymentDTO.getAmount() <= accountDTO.getBalance()){
                        paymentDTO.setStatus(Constant.STATUS_PAYMENT_CREATING);
                    }else{
                        paymentDTO.setStatus(Constant.STATUS_PAYMENT_REJECTED);
                        createNewPayment(paymentDTO).subscribe();
                        throw new CommonException("P01", "Balance not enough", HttpStatus.BAD_REQUEST);

                    }
                     return createNewPayment(paymentDTO);
                });
    }

    public Flux<PaymentDTO> getAllPayment(int id){
        return paymentRepository.findByAccountId(id)
                .map(PaymentDTO::entityToDto)
                .switchIfEmpty(Mono.error(new CommonException("P02", "Account don't have payment", HttpStatus.NOT_FOUND)));

    }
    public Mono<PaymentDTO> createNewPayment(PaymentDTO paymentDTO){
        return Mono.just(paymentDTO)
                .map(PaymentDTO::dtoToEntity)
                .flatMap(payment -> paymentRepository.save(payment))
                .map(PaymentDTO::entityToDto)
                .doOnError(throwable -> log.error(throwable.getMessage()))
                .doOnSuccess(paymentDTO1 -> {
                    if(Objects.equals(paymentDTO1.getStatus(), Constant.STATUS_PAYMENT_CREATING))
                        eventProducer.sendPaymentRequest(Constant.PAYMENT_REQUEST_TOPIC,gson.toJson(paymentDTO1));
                });
    }
    public Mono<PaymentDTO> updateStatusPayment(PaymentDTO paymentDTO){
        return paymentRepository.findById((int)paymentDTO.getId())
                .switchIfEmpty(Mono.error(new CommonException("P03", "Payment not found", HttpStatus.NOT_FOUND)))
                .flatMap(payment -> {
                    payment.setStatus(paymentDTO.getStatus());
                    return paymentRepository.save(payment);
                })
                .map(PaymentDTO::entityToDto)
                .onErrorMap(ex -> new PaymentException(ex.getMessage()))
                .doOnError(throwable -> log.error(throwable.getMessage()));
    }


}
