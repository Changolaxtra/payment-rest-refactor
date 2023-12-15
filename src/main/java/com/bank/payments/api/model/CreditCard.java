package com.bank.payments.api.model;

import java.math.BigDecimal;

public record CreditCard(String number, Integer cvv, BigDecimal balance) {

}
