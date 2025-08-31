package com.bank.apis;

import com.bank.apis.account.Account;
import com.bank.apis.account.AccountRepository;
import com.bank.apis.card.CreditCard;
import com.bank.apis.card.CreditCardRepository;
import com.bank.apis.user.User;
import com.bank.apis.user.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataLoader {
  @Bean CommandLineRunner init(UserRepository users, PasswordEncoder enc, AccountRepository accounts, CreditCardRepository cards){
    return args -> {
      if (users.findByEmail("admin@bank.local").isEmpty()) {
        users.save(User.builder().email("admin@bank.local").passwordHash(enc.encode("Admin@123")).name("Admin").roles("ROLE_ADMIN,ROLE_USER").build());
      }
      if (users.findByEmail("user@bank.local").isEmpty()) {
        User u = users.save(User.builder().email("user@bank.local").passwordHash(enc.encode("User@123")).name("Sample User").roles("ROLE_USER").build());
        accounts.save(Account.builder().accountNumber("100001234567").accountType("SAVINGS").balance(5000.0).owner(u).build());
        accounts.save(Account.builder().accountNumber("200001234567").accountType("CURRENT").balance(8000.0).owner(u).build());
        cards.save(CreditCard.builder().cardNumberMasked("**** **** **** 4242").expiry("12/29").limitAmount(100000.0).outstanding(2500.0).owner(u).build());
      }
    };
  }
}
