package com.bank.apis.user;

import com.bank.apis.security.JwtService;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
  @Autowired private UserService users;
  @Autowired private JwtService jwt;
  @Autowired private AuthenticationManager authManager;

  record RegisterReq(@Email String email, @NotBlank String password, String name, String phone) {}
  record LoginReq(@Email String email, @NotBlank String password) {}
  record TokenRes(String token) {}

  @PostMapping("/register")
  public ResponseEntity<?> register(@RequestBody RegisterReq r){
    users.register(r.email(), r.password(), r.name(), r.phone());
    return ResponseEntity.ok(Map.of("message","registered"));
  }

  @PostMapping("/login")
  public ResponseEntity<TokenRes> login(@RequestBody LoginReq r){
    Authentication auth = authManager.authenticate(new UsernamePasswordAuthenticationToken(r.email(), r.password()));
    SecurityContextHolder.getContext().setAuthentication(auth);
    String token = jwt.generateToken(r.email(), new HashMap<>(), 1000L*60*60*24);
    return ResponseEntity.ok(new TokenRes(token));
  }
}
