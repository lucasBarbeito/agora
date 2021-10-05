package com.agora.agora.security.jwt;

import com.agora.agora.repository.JwtBlacklistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * @author Manuel Pedrozo
 */
public class JWTConfigurer extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

    public static final String AUTHORIZATION_HEADER = "Authorization";

    private TokenProvider tokenProvider;
    public JwtBlacklistRepository blacklistRepository;

    public JWTConfigurer(TokenProvider tokenProvider, JwtBlacklistRepository blacklistRepository) {
        this.tokenProvider = tokenProvider;
        this.blacklistRepository = blacklistRepository;
    }

    @Override
    public void configure(HttpSecurity http) {
        JWTFilter customFilter = new JWTFilter(tokenProvider, blacklistRepository);
        http.addFilterBefore(customFilter, UsernamePasswordAuthenticationFilter.class);
    }
}
