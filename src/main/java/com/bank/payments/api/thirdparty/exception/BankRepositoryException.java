package com.bank.payments.api.thirdparty.exception;

public class BankRepositoryException extends RuntimeException {

  public BankRepositoryException(final String message) {
    super(message);
  }
}
