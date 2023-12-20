package com.bank.payments.api.thirdparty.repository;

import com.bank.payments.api.thirdparty.model.CreditCard;
import com.bank.payments.api.thirdparty.exception.BankRepositoryException;
import com.bank.payments.api.thirdparty.exception.ErrorMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class InMemoryCreditCardRepositoryTest {

    private static final String CARD_NUMBER = "1111222233334444";
    private static final String CARD_NUMBER2 = "1111222233335555";

    private InMemoryCreditCardRepository repository;

    @BeforeEach
    void setUp() throws BankRepositoryException {
        repository = new InMemoryCreditCardRepository();
        repository.init();
        repository.save(new CreditCard(CARD_NUMBER, 123, new BigDecimal(500)));
    }


    @Test
    public void givenNullCardAndSavingItShouldThrowException() {
        Exception exception = assertThrows(BankRepositoryException.class, () -> {
            repository.save( null);
        });

        assertNotNull(exception);
        assertEquals(ErrorMessage.CARD_IS_NULL, exception.getMessage());
    }

    @Test
    public void givenExitingCardNumberAndSavingItShouldThrowException() {
        Exception exception = assertThrows(BankRepositoryException.class, () -> {
            repository.save(new CreditCard(CARD_NUMBER, 123, new BigDecimal(500)));
        });

        assertNotNull(exception);
        assertEquals(ErrorMessage.CARD_ALREADY_EXISTS, exception.getMessage());
    }

    @Test
    public void givenNonexistentCardNumberToFindItShouldThrowException() {
        Exception exception = assertThrows(BankRepositoryException.class, () -> {
            repository.find(CARD_NUMBER2);
        });

        assertNotNull(exception);
        assertEquals(ErrorMessage.CARD_DOES_NOT_EXISTS, exception.getMessage());
    }

    @Test
    public void givenExistentCardNumberToFindItShouldReturnTheCorrectOne()
        throws BankRepositoryException {
        final CreditCard creditCard = repository.find(CARD_NUMBER);

        assertNotNull(creditCard);
        assertEquals(CARD_NUMBER, creditCard.number());
        assertEquals(123, creditCard.cvv());
        assertEquals(new BigDecimal(500), creditCard.balance());
    }

    @Test
    public void givenExistentCardNumberAndRemovingItExistsShouldReturnFalse()
        throws BankRepositoryException {
        repository.remove(CARD_NUMBER);
        final boolean exists = repository.exists(CARD_NUMBER);

        assertFalse(exists);
    }

    @Test
    public void givenNonexistentCardNumberAndUpdatingItShouldThrowException() {
        Exception exception = assertThrows(BankRepositoryException.class, () -> {
            repository.update(new CreditCard(CARD_NUMBER2, 123, new BigDecimal(600)));
        });

        assertNotNull(exception);
        assertEquals(ErrorMessage.CARD_DOES_NOT_EXISTS, exception.getMessage());
    }

    @Test
    public void givenNullCardAndUpdatingItShouldThrowException() {
        Exception exception = assertThrows(BankRepositoryException.class, () -> {
            repository.update( null);
        });

        assertNotNull(exception);
        assertEquals(ErrorMessage.CARD_IS_NULL, exception.getMessage());
    }

    @Test
    public void givenCorrectCardNumberAndUpdatingItShouldBeUpdated()
        throws BankRepositoryException {
        final CreditCard creditCard =
                repository.update(new CreditCard(CARD_NUMBER, 123, new BigDecimal(700)));

        assertNotNull(creditCard);
        assertEquals(CARD_NUMBER, creditCard.number());
        assertEquals(123, creditCard.cvv());
        assertEquals(new BigDecimal(700), creditCard.balance());
    }

    @Test
    public void givenInvalidCardNumberAndLookingForItShouldThrowException() {
        Exception exception = assertThrows(BankRepositoryException.class, () -> {
            repository.exists(null);
        });

        assertNotNull(exception);
        assertEquals(ErrorMessage.INVALID_CARD_NUMBER, exception.getMessage());
    }


}