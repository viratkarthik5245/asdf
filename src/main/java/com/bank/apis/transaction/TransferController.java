package com.bank.apis.transaction;

import com.bank.apis.account.Account;
import com.bank.apis.account.AccountRepository;
import com.bank.apis.user.User;
import com.bank.apis.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class TransferController {
  @Autowired private AccountRepository accounts;
  @Autowired private TransactionRepository txns;
  @Autowired private UserService users;

  @GetMapping("/transactions")
  public ResponseEntity<?> transactions(@RequestParam(required=false) Long accountId, @AuthenticationPrincipal UserDetails p){
    User me = users.byEmail(p.getUsername());
    if (accountId == null) {
      List<Account> my = accounts.findByOwner(me);
      return ResponseEntity.ok(my.stream().flatMap(a -> txns.findByAccount(a).stream()).toList());
    } else {
      Account a = accounts.findById(accountId).orElseThrow();
      if (!a.getOwner().getId().equals(me.getId())) return ResponseEntity.status(403).build();
      return ResponseEntity.ok(txns.findByAccount(a));
    }
  }

  static class TransferReq { public String fromAccountNumber; public String toAccountNumber; public Double amount; public String remarks; }

  @PostMapping("/transfers")
  @Transactional
  public ResponseEntity<?> transfer(@RequestBody TransferReq req, @AuthenticationPrincipal UserDetails p){
    if (req.amount == null || req.amount <= 0) return ResponseEntity.badRequest().body(Map.of("error","Invalid amount"));
    User me = users.byEmail(p.getUsername());
    Account from = accounts.findByAccountNumber(req.fromAccountNumber).orElseThrow();
    Account to = accounts.findByAccountNumber(req.toAccountNumber).orElseThrow();
    if (!from.getOwner().getId().equals(me.getId())) return ResponseEntity.status(403).build();
    if (from.getBalance() < req.amount) return ResponseEntity.badRequest().body(Map.of("error","Insufficient funds"));
    String ref = UUID.randomUUID().toString();
    from.setBalance(from.getBalance() - req.amount); to.setBalance(to.getBalance() + req.amount);
    accounts.save(from); accounts.save(to);
    txns.save(Transaction.builder().account(from).type("DEBIT").amount(req.amount).description("Transfer to "+to.getAccountNumber()+" : "+(req.remarks==null?"":req.remarks)).referenceId(ref).build());
    txns.save(Transaction.builder().account(to).type("CREDIT").amount(req.amount).description("Transfer from "+from.getAccountNumber()+" : "+(req.remarks==null?"":req.remarks)).referenceId(ref).build());
    return ResponseEntity.ok(Map.of("status","SUCCESS","referenceId",ref));
  }
}
