package com.bank.apis.card;

import com.bank.apis.user.User;
import jakarta.persistence.*;
import lombok.*;

@Entity @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class CreditCard {
  @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
  @Column(unique=true, nullable=false) private String cardNumberMasked;
  private String expiry;
  private Double limitAmount;
  private Double outstanding = 0.0;
  private String status = "ACTIVE";
  @ManyToOne(fetch=FetchType.LAZY) private User owner;
}
