package com.bank.payments.api.thirdparty.repository;

public interface BankRepository<T, K> {

  T save(T entity);

  T find(K key);

  T update(T entity);
  void remove(K key);

  boolean exists(K key);
}
