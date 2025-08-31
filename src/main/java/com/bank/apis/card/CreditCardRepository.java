package com.bank.apis.card;

import com.bank.apis.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CreditCardRepository extends JpaRepository<CreditCard, Long> {
  List<CreditCard> findByOwner(com.bank.apis.user.User owner);
}
