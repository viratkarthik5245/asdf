package com.bank.apis.account;

import com.bank.apis.user.User;
import jakarta.persistence.*;
import lombok.*;

@Entity @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Account {
  @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
  @Column(unique=true, nullable=false) private String accountNumber;
  private String accountType;
  private Double balance = 0.0;
  private String status = "ACTIVE";
  @ManyToOne(fetch=FetchType.LAZY) private User owner;
}
