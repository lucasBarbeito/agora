package com.agora.agora.controller;

import com.agora.agora.model.User;
import com.agora.agora.model.dto.FullUserDTO;
import com.agora.agora.model.form.UserForm;
import com.agora.agora.model.form.UserVerificationForm;
import com.agora.agora.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.util.Optional;


@RestController
@RequestMapping("/user")
public class UserController {

    private UserService userService;
    @Autowired
    public UserController(UserService userService){
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity createUser(@Valid @RequestBody UserForm user, UriComponentsBuilder b){
        int id = userService.save(user);
        UriComponents components = b.path("/user/{id}").buildAndExpand(id);
        return ResponseEntity.created(components.toUri()).build();
    }

    @PostMapping(value = "/verify_user")
    public ResponseEntity verifyUser(@Valid @RequestBody UserVerificationForm userVerificationToken) {
        String name = userService.verifyUser(userVerificationToken.getUserVerificationToken());
        return ResponseEntity.ok(name);
    }

    @GetMapping(value = "/me")
    public ResponseEntity getCurrentUser() {
        final Optional<User> optional = userService.findCurrentUser();
        final Optional<FullUserDTO> userDTO = optional.map((user) -> new FullUserDTO(user.getId(), user.getName(), user.getSurname(), user.getEmail()));
        return userDTO.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }
}
