package com.bank.apis.account;

import com.bank.apis.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {
  List<Account> findByOwner(User owner);
  Optional<Account> findByAccountNumber(String accountNumber);
}
