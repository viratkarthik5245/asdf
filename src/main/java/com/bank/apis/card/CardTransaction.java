package com.bank.apis.card;

import jakarta.persistence.*;
import lombok.*;
import java.time.OffsetDateTime;

@Entity @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class CardTransaction {
  @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
  @ManyToOne private CreditCard card;
  private Double amount;
  private String merchant;
  private String status = "SUCCESS";
  private OffsetDateTime createdAt = OffsetDateTime.now();
}
