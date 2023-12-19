package com.bank.payments.api.thirdparty.repository;

import com.bank.payments.api.thirdparty.exception.BankRepositoryException;

public interface BankRepository<T, K> {

  T save(T entity) throws BankRepositoryException;

  T find(K key) throws BankRepositoryException;

  T update(T entity) throws BankRepositoryException;
  void remove(K key) throws BankRepositoryException;

  boolean exists(K key) throws BankRepositoryException;
}
