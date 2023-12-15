package com.bank.payments.api.dto;

import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Data
@Builder
@Jacksonized
public class CreateCardResponse {

  private String number;
  private String message;
  private boolean successful;
}
