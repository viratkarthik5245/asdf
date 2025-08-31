package com.bank.apis.card;

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
@RequestMapping("/api/cards")
public class CardController {
  @Autowired private CreditCardRepository cards;
  @Autowired private CardTransactionRepository txns;
  @Autowired private UserService users;

  @GetMapping public java.util.List<CreditCard> my(@AuthenticationPrincipal UserDetails p){
    User me = users.byEmail(p.getUsername()); return cards.findByOwner(me);
  }
  @GetMapping("/{id}") public ResponseEntity<?> card(@PathVariable Long id, @AuthenticationPrincipal UserDetails p){
    CreditCard c = cards.findById(id).orElseThrow(); User me = users.byEmail(p.getUsername());
    if (!c.getOwner().getId().equals(me.getId())) return ResponseEntity.status(403).build(); return ResponseEntity.ok(c);
  }
  @GetMapping("/{id}/transactions") public ResponseEntity<?> transactions(@PathVariable Long id, @AuthenticationPrincipal UserDetails p){
    CreditCard c = cards.findById(id).orElseThrow(); User me = users.byEmail(p.getUsername());
    if (!c.getOwner().getId().equals(me.getId())) return ResponseEntity.status(403).build(); return ResponseEntity.ok(txns.findByCard(c));
  }
  static class Create { public String cardNumberMasked; public String expiry; public Double limitAmount; }
  @PostMapping public ResponseEntity<?> add(@RequestBody Create req, @AuthenticationPrincipal UserDetails p){
    User me = users.byEmail(p.getUsername()); CreditCard c = CreditCard.builder().cardNumberMasked(req.cardNumberMasked).expiry(req.expiry).limitAmount(req.limitAmount).owner(me).build(); return ResponseEntity.ok(cards.save(c));
  }
  @PostMapping("/{id}/pay") public ResponseEntity<?> pay(@PathVariable Long id, @RequestBody Map<String, Double> body, @AuthenticationPrincipal UserDetails p){
    CreditCard c = cards.findById(id).orElseThrow(); User me = users.byEmail(p.getUsername());
    if (!c.getOwner().getId().equals(me.getId())) return ResponseEntity.status(403).build(); double amt = body.getOrDefault("amount", 0.0); if (amt<=0) return ResponseEntity.badRequest().body(Map.of("error","Invalid amount"));
    c.setOutstanding(Math.max(0.0, c.getOutstanding()-amt)); cards.save(c); return ResponseEntity.ok(Map.of("status","PAID","remainingOutstanding", c.getOutstanding()));
  }
}
