package com.bank.payments.api.controller;

import com.bank.payments.api.dto.CardPaymentRequest;
import com.bank.payments.api.dto.CardPaymentResponse;
import com.bank.payments.api.model.CreditCard;
import com.bank.payments.api.thirdparty.repository.CreditCardRepository;
import org.springframework.beans.factory.annotation.Autowired;
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

  @Autowired
  private CreditCardRepository creditCardRepository;

  @ResponseBody
  @PostMapping(path = "/process",
      produces = MediaType.APPLICATION_JSON_VALUE,
      consumes = MediaType.APPLICATION_JSON_VALUE)
  public CardPaymentResponse process(@RequestBody final CardPaymentRequest request) {
    // Verify if card exists.
    if (creditCardRepository.exists(request.getNumber())) {
      CreditCard creditCard = creditCardRepository.find(request.getNumber());

      // Verifying CVV
      if (!creditCard.cvv().equals(request.getCvv())) {
        return CardPaymentResponse.builder().message("error").successful(false).build();
      }

      // Verifying if enough balance.
      if (request.getAmount().compareTo(creditCard.balance()) > 0) {
        return CardPaymentResponse.builder().message("error").successful(false).build();
      }

      // Verifying if payment amount is negative
      if (request.getAmount().signum() == -1) {
        return CardPaymentResponse.builder().message("error").successful(false).build();
      }

      // Updating Card with new amount.
      CreditCard update = creditCardRepository.update(
          new CreditCard(creditCard.number(), creditCard.cvv(),
              creditCard.balance().add(request.getAmount().negate())));

      // Returning good response.
      return CardPaymentResponse.builder().successful(true).balance(update.balance()).build();

    } else {
      // If Card does not exist.
      return CardPaymentResponse.builder().message("error").successful(false).build();
    }
  }
}
