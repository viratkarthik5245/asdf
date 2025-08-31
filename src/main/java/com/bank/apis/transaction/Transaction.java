package com.bank.apis.transaction;

import com.bank.apis.account.Account;
import jakarta.persistence.*;
import lombok.*;
import java.time.OffsetDateTime;

@Entity @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Transaction {
  @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
  @ManyToOne private Account account;
  private String type;
  private Double amount;
  private String description;
  private OffsetDateTime createdAt = OffsetDateTime.now();
  private String status = "SUCCESS";
  private String referenceId;
}
