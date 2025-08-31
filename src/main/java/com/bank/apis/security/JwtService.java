package com.bank.apis.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.security.Key;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {
  @Value("${jwt.secret}") private String secret;
  private Key getKey() {
    byte[] keyBytes = Decoders.BASE64.decode(java.util.Base64.getEncoder().encodeToString(secret.getBytes()));
    return Keys.hmacShaKeyFor(keyBytes);
  }
  public String extractUsername(String token){ return extractClaim(token, Claims::getSubject); }
  public <T> T extractClaim(String token, Function<Claims,T> f){
    final Claims c = Jwts.parserBuilder().setSigningKey(getKey()).build().parseClaimsJws(token).getBody();
    return f.apply(c);
  }
  public String generateToken(String username, Map<String,Object> extra, long expMs){
    return Jwts.builder().setClaims(extra).setSubject(username).setIssuedAt(new Date())
      .setExpiration(new Date(System.currentTimeMillis()+expMs))
      .signWith(getKey(), SignatureAlgorithm.HS256).compact();
  }
  public boolean isTokenValid(String token, String username){
    return username.equals(extractUsername(token)) && extractClaim(token, Claims::getExpiration).after(new Date());
  }
}
