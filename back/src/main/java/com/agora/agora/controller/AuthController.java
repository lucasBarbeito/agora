package com.agora.agora.controller;

import com.agora.agora.model.form.LoginForm;
import com.agora.agora.security.jwt.JWTConfigurer;
import com.agora.agora.security.jwt.JWTToken;
import com.agora.agora.security.jwt.TokenProvider;
import com.agora.agora.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final TokenProvider tokenProvider;
    private final AuthenticationProvider authenticationProvider;
    private final AuthService authService;

    @Autowired
    public AuthController(TokenProvider tokenProvider, AuthenticationProvider authenticationProvider, AuthService authService) {
        this.tokenProvider = tokenProvider;
        this.authenticationProvider = authenticationProvider;
        this.authService = authService;
    }

    @PostMapping()
    public ResponseEntity authenticate(@Valid @RequestBody LoginForm loginForm) {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginForm.getEmail(), loginForm.getPassword());

        Authentication authentication = this.authenticationProvider.authenticate(authenticationToken);

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = tokenProvider.createToken(authentication);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(JWTConfigurer.AUTHORIZATION_HEADER, "Bearer " + jwt);
        return new ResponseEntity<>(new JWTToken(jwt), httpHeaders, HttpStatus.OK);
    }

    @DeleteMapping()
    public ResponseEntity logout(@RequestHeader (name="Authorization") String token) {
        int id = authService.logout(token.substring(7));
        return ResponseEntity.ok(id);
    }

}
