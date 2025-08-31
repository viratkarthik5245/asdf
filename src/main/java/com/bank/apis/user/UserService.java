package com.bank.apis.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Arrays;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {
  @Autowired private UserRepository repo;
  @Autowired private PasswordEncoder encoder;

  public User register(String email, String rawPassword, String name, String phone){
    if (repo.existsByEmail(email)) throw new RuntimeException("Email in use");
    User u = User.builder().email(email).passwordHash(encoder.encode(rawPassword)).name(name).phone(phone).roles("ROLE_USER").build();
    return repo.save(u);
  }
  public User update(Long id, String name, String phone, String address){
    User u = repo.findById(id).orElseThrow();
    u.setName(name); u.setPhone(phone); u.setAddress(address);
    return repo.save(u);
  }
  public User byEmail(String email){ return repo.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("Not found")); }
  @Override public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    User u = byEmail(username);
    var auth = Arrays.stream(u.getRoles().split(",")).map(String::trim).filter(s->!s.isBlank()).map(SimpleGrantedAuthority::new).collect(Collectors.toList());
    return new org.springframework.security.core.userdetails.User(u.getEmail(), u.getPasswordHash(), auth);
  }
}
