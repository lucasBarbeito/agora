package com.agora.agora.controller;

import com.agora.agora.model.User;
import com.agora.agora.model.form.UserForm;
import com.agora.agora.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;

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
}
