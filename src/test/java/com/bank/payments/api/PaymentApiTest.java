package com.bank.payments.api;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.bank.payments.api.base.BaseJsonApiTest;
import com.bank.payments.api.dto.CardPaymentRequest;
import com.bank.payments.api.dto.CardPaymentResponse;
import com.bank.payments.api.thirdparty.model.CreditCard;
import com.bank.payments.api.thirdparty.exception.BankRepositoryException;
import com.bank.payments.api.thirdparty.repository.CreditCardRepository;
import java.math.BigDecimal;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class PaymentApiTest extends BaseJsonApiTest {

  private static final String PROCESS_PAYMENT_ENDPOINT = "/card/payment/process";

  private static final String CARD_NUMBER = "1111222233334444";
  private static final String INVALID_CARD_NUMBER = "0000222233334444";
  private static final int CVV = 123;
  private static final BigDecimal BALANCE = new BigDecimal(1000);
  private static final int WRONG_CVV = 456;

  @Autowired
  private CreditCardRepository creditCardRepository;

  @BeforeEach
  @Override
  public void setUp() throws BankRepositoryException {
    super.setUp();
    setupCreditCard();
  }

  @AfterEach
  public void afterEach() throws BankRepositoryException {
    creditCardRepository.remove(CARD_NUMBER);
  }

  @Test
  public void givenWrongCvvForPaymentShouldBeUnsuccessful() throws Exception {
    final BigDecimal amount = new BigDecimal(100);
    final CardPaymentResponse response = makePayment(CARD_NUMBER, WRONG_CVV, amount);

    assertFalse(response.isSuccessful());
  }

  @Test
  public void givenCorrectCardForPaymentShouldBeSuccessfulWithCorrectBalance() throws Exception {
    final BigDecimal amount = new BigDecimal(700);
    final CardPaymentResponse response = makePayment(CARD_NUMBER, CVV, amount);
    assertTrue(response.isSuccessful());

    assertEquals(new BigDecimal(300), response.getBalance());
  }

  @Test
  public void givenGreaterAmountThanBalanceForPaymentShouldBeUnsuccessful() throws Exception {
    final BigDecimal amount = new BigDecimal(1500);
    final CardPaymentResponse response = makePayment(CARD_NUMBER, CVV,
        amount);

    assertFalse(response.isSuccessful());
  }

  @Test
  public void givenWrongCardForPaymentShouldBeUnsuccessful() throws Exception {
    final CardPaymentResponse response = makePayment(INVALID_CARD_NUMBER, CVV, BigDecimal.ONE);
    assertFalse(response.isSuccessful());
  }

  @Test
  public void givenNegativeAmountForPaymentShouldBeUnsuccessful() throws Exception {
    final BigDecimal amount = new BigDecimal(10).negate();
    final CardPaymentResponse response = makePayment(CARD_NUMBER, CVV,
        amount);
    assertFalse(response.isSuccessful());
  }

  @Test
  public void givenNullAmountForPaymentShouldBeUnsuccessful() throws Exception {
    final CardPaymentResponse response = makePayment(CARD_NUMBER, CVV, null);
    assertFalse(response.isSuccessful());
  }

  private void setupCreditCard() throws BankRepositoryException {
    final CreditCard creditCard = createCardWith1000Balance();
    assertNotNull(creditCard);
  }

  private CreditCard createCardWith1000Balance() throws BankRepositoryException {
    return creditCardRepository
        .save(new CreditCard(PaymentApiTest.CARD_NUMBER,
            PaymentApiTest.CVV,
            PaymentApiTest.BALANCE));
  }

  private CardPaymentResponse makePayment(final String cardNumber,
      final Integer cvv,
      final BigDecimal amount) throws Exception {

    final CardPaymentRequest cardPaymentRequest =
        CardPaymentRequest.builder()
            .number(cardNumber)
            .cvv(cvv)
            .amount(amount)
            .build();

    final String jsonRequest = super.mapToJson(cardPaymentRequest);
    return makeApiCall(jsonRequest, PROCESS_PAYMENT_ENDPOINT, CardPaymentResponse.class);
  }

}
