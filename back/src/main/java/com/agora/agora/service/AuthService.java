package com.agora.agora.service;

import com.agora.agora.model.JwtBlacklist;
import com.agora.agora.model.User;
import com.agora.agora.repository.JwtBlacklistRepository;
import com.agora.agora.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.Optional;
import com.agora.agora.exceptions.ForbiddenElementException;

@Service
public class AuthService implements AuthenticationProvider {

    private UserRepository userRepository;
    private JwtBlacklistRepository blacklistRepository;

    @Autowired
    public AuthService(UserRepository userRepository, JwtBlacklistRepository blacklistRepository) {
        this.blacklistRepository = blacklistRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Authentication authenticate(Authentication authentication)
            throws AuthenticationException {

        String email = authentication.getName();
        String password = authentication.getCredentials().toString();

        return userRepository.findUserByEmail(email).map(user -> {
            if(BCrypt.checkpw(password, user.getPassword())) {
                if (user.isVerified()) {
                    ArrayList<GrantedAuthority> authorities = new ArrayList<>();
                    authorities.add(new SimpleGrantedAuthority(user.getType().toString()));
                    return new UsernamePasswordAuthenticationToken(email, password, authorities);
                }else {
                    throw new ForbiddenElementException("Email confirmation needed");
                }
            } else {
                throw new BadCredentialsException("Invalid credentials");
            }
        }).orElseThrow(() -> new BadCredentialsException("Invalid credentials"));
    }

    public boolean authenticate(String email, String password) {
        Optional<User> userOptional = userRepository.findUserByEmail(email);
        if(userOptional.isPresent()) {
            User user = userOptional.get();
            return BCrypt.checkpw(password, user.getPassword());
        }
        return false;
    }

    public int logout(String token){
        JwtBlacklist blacklist = new JwtBlacklist(token);
        blacklistRepository.save(blacklist);
        return blacklist.getId();
    }

    public boolean checkUsername(String username) {
        return userRepository.findUserByEmail(username).isPresent();
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(
                UsernamePasswordAuthenticationToken.class);
    }
}
