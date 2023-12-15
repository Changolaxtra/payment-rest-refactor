package com.bank.payments.api.thirdparty.repository;

import com.bank.payments.api.model.CreditCard;
import com.bank.payments.api.thirdparty.exception.BankRepositoryException;
import com.bank.payments.api.thirdparty.exception.ErrorMessage;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Slf4j
@Repository
public class CreditCardRepository implements BankRepository<CreditCard, String> {

  private Map<String, CreditCard> numberToCreditCard;

  @PostConstruct
  public void init() {
    numberToCreditCard = new HashMap<>();
  }

  @Override
  public CreditCard save(final CreditCard creditCard) {
    log.info("Saving Card: {}", creditCard);
    validateCardForSave(creditCard);
    numberToCreditCard.put(creditCard.number(),
        new CreditCard(creditCard.number(),
            creditCard.cvv(),
            creditCard.balance()));
    return numberToCreditCard.get(creditCard.number());
  }

  @Override
  public CreditCard find(final String cardNumber) {
    log.info("Searching Card: {}", cardNumber);
    validateExistence(cardNumber);
    return numberToCreditCard.get(cardNumber);
  }

  @Override
  public CreditCard update(final CreditCard updatedCard) {
    log.info("Updating Card: {}", updatedCard);
    validateCardForUpdate(updatedCard);
    numberToCreditCard.put(updatedCard.number(),
        new CreditCard(updatedCard.number(),
            updatedCard.cvv(),
            updatedCard.balance()));

    return numberToCreditCard.get(updatedCard.number());
  }

  @Override
  public void remove(String cardNumber) {
    log.info("Removing Card: {}", cardNumber);
    validateExistence(cardNumber);
    numberToCreditCard.remove(cardNumber);
  }

  @Override
  public boolean exists(final String cardNumber) {
    log.info("Checking if {} card exists.", cardNumber);
    if (StringUtils.isEmpty(cardNumber)) {
      throw new BankRepositoryException(ErrorMessage.INVALID_CARD_NUMBER);
    }
    return numberToCreditCard.containsKey(cardNumber);
  }

  private void validateCardForSave(final CreditCard creditCard) {
    validateNullCard(creditCard);
    validateAvailability(creditCard);
  }

  private void validateAvailability(CreditCard creditCard) {
    if (exists(creditCard.number())) {
      throw new BankRepositoryException(ErrorMessage.CARD_ALREADY_EXISTS);
    }
  }

  private void validateCardForUpdate(final CreditCard updatedCard) {
    validateNullCard(updatedCard);
    validateExistence(updatedCard.number());
  }

  private static void validateNullCard(CreditCard creditCard) {
    if (Objects.isNull(creditCard)) {
      throw new BankRepositoryException(ErrorMessage.CARD_IS_NULL);
    }
  }

  private void validateExistence(String cardNumber) {
    if (isNotPresent(cardNumber)) {
      throw new BankRepositoryException(ErrorMessage.CARD_DOES_NOT_EXISTS);
    }
  }

  private boolean isNotPresent(final String cardNumber) {
    return !exists(cardNumber);
  }
}
