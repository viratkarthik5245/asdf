package com.bank.apis.account;

import com.bank.apis.user.User;
import com.bank.apis.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {
  @Autowired private AccountRepository repo;
  @Autowired private UserService users;

  @GetMapping public List<Account> myAccounts(@AuthenticationPrincipal UserDetails p){
    User me = users.byEmail(p.getUsername()); return repo.findByOwner(me);
  }
  @GetMapping("/{id}") public ResponseEntity<Account> get(@PathVariable Long id, @AuthenticationPrincipal UserDetails p){
    Account a = repo.findById(id).orElseThrow(); User me = users.byEmail(p.getUsername());
    if (!a.getOwner().getId().equals(me.getId())) return ResponseEntity.status(403).build();
    return ResponseEntity.ok(a);
  }
  static class Create { public String accountNumber; public String accountType; public Double initialBalance; }
  @PostMapping public ResponseEntity<Account> create(@RequestBody Create req, @AuthenticationPrincipal UserDetails p){
    User me = users.byEmail(p.getUsername());
    Account a = Account.builder().accountNumber(req.accountNumber).accountType(req.accountType).balance(req.initialBalance==null?0.0:req.initialBalance).owner(me).build();
    return ResponseEntity.ok(repo.save(a));
  }
  @GetMapping("/{id}/statements")
  public ResponseEntity<?> statements(@PathVariable Long id){ return ResponseEntity.ok(Map.of("message","Use /api/transactions?accountId="+id)); }
}
