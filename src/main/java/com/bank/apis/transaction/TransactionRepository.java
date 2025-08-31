package com.bank.apis.transaction;

import com.bank.apis.account.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
  List<Transaction> findByAccount(Account account);
}
