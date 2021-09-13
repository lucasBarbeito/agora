package com.agora.agora.controller;

import com.agora.agora.model.User;
import com.agora.agora.model.dto.FullUserDTO;
import com.agora.agora.model.dto.StudyGroupDTO;
import com.agora.agora.model.form.UserForm;
import com.agora.agora.model.form.UserVerificationForm;
import com.agora.agora.service.EmailService;
import com.agora.agora.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.util.Optional;
import java.util.UUID;


@RestController
@RequestMapping("/user")
public class UserController {

    private UserService userService;
    private EmailService emailService;
    @Autowired
    public UserController(UserService userService, EmailService emailService){
        this.userService = userService;
        this.emailService = emailService;
    }

    @PostMapping
    public ResponseEntity createUser(@Valid @RequestBody UserForm user, UriComponentsBuilder b){
        int id = userService.save(user);
        UriComponents components = b.path("/user/{id}").buildAndExpand(id);
        Optional<User> userOptional = userService.findById(id);
        if (userOptional.isPresent()) {
            User createdUser = userOptional.get();
            createdUser.setUserVerificationToken(UUID.randomUUID().toString());
            userService.justSave(createdUser);
            // TODO: get client host dynamically
            String url = "http://localhost:3000/user/verify-user/" + createdUser.getUserVerificationToken();
            String body = "Verifica tu usuario: \n" + url;
            emailService.sendSimpleMessage(user.getEmail(), "Verificar usuario", body);
            return ResponseEntity.created(components.toUri()).build();
        } else {
            return ResponseEntity.notFound().build();
        }

    }
    @PostMapping(value = "/verify_user")
    public ResponseEntity resetPassword(@Valid @RequestBody UserVerificationForm userVerificationToken) {
        Optional<User> userOptional = userService.findByUserVerificationToken(userVerificationToken.getUserVerificationToken());
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setVerified(true);
            user.setUserVerificationToken(null);
            userService.justSave(user);
            return ResponseEntity.ok(user.getName());
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping(value = "/me")
    public ResponseEntity getCurrentUser() {
        final Optional<User> optional = userService.findCurrentUser();
        final Optional<FullUserDTO> userDTO = optional.map((user) -> new FullUserDTO(user.getId(), user.getName(), user.getSurname(), user.getEmail()));
        return userDTO.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }
}
