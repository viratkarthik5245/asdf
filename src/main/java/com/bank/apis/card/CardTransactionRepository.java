package com.bank.apis.card;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CardTransactionRepository extends JpaRepository<CardTransaction, Long> {
  java.util.List<CardTransaction> findByCard(CreditCard card);
}
