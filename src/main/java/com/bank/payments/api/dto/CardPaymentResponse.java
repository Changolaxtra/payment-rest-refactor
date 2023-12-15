package com.bank.payments.api.dto;

import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

import java.math.BigDecimal;

@Data
@Builder
@Jacksonized
public class CardPaymentResponse {

  private boolean successful;
  private String message;
  private BigDecimal balance;
}
