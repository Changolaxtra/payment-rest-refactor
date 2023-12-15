package com.bank.payments.api.controller;

import com.bank.payments.api.dto.CardPaymentRequest;
import com.bank.payments.api.dto.CardPaymentResponse;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
@RequestMapping("/card/payment")
public class CardPaymentController {

  @ResponseBody
  @PostMapping(path = "/process",
      produces = MediaType.APPLICATION_JSON_VALUE,
      consumes = MediaType.APPLICATION_JSON_VALUE)
  public CardPaymentResponse process(@RequestBody final CardPaymentRequest request) {
    //TODO Add the implementation to make a payment.
    return CardPaymentResponse.builder().balance(BigDecimal.ONE).successful(true).build();
  }
}
