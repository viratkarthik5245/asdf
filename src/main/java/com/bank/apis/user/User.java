package com.bank.apis.user;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@Table(name="users")
public class User {
  @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
  @Column(unique=true, nullable=false) private String email;
  @Column(nullable=false) private String passwordHash;
  private String name;
  private String phone;
  private String address;
  private LocalDate dateOfBirth;
  private String roles; // e.g. ROLE_USER,ROLE_ADMIN
}
