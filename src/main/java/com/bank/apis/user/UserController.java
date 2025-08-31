package com.bank.apis.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {
  @Autowired private UserService users;

  @GetMapping("/me")
  public ResponseEntity<?> me(@AuthenticationPrincipal UserDetails principal){
    User u = users.byEmail(principal.getUsername());
    return ResponseEntity.ok(new Me(u.getId(), u.getEmail(), u.getName(), u.getPhone(), u.getAddress(), u.getRoles()));
  }
  record Me(Long id, String email, String name, String phone, String address, String roles) {}

  static class Update { public String name; public String phone; public String address; }
  @PutMapping("/{id}")
  public ResponseEntity<?> update(@PathVariable Long id, @RequestBody Update p, @AuthenticationPrincipal UserDetails principal){
    User current = users.byEmail(principal.getUsername());
    if (!current.getId().equals(id) && (current.getRoles()==null || !current.getRoles().contains("ROLE_ADMIN"))) return ResponseEntity.status(403).build();
    return ResponseEntity.ok(users.update(id, p.name, p.phone, p.address));
  }
}
