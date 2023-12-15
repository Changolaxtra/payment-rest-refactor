package com.bank.payments.api.dto;

import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

import java.math.BigDecimal;

@Data
@Builder
@Jacksonized
public class CreateCardRequest {

  private String number;
  private Integer cvv;
  private BigDecimal balance;
}
