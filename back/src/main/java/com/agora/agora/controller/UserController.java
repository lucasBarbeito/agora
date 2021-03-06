package com.agora.agora.controller;

import com.agora.agora.model.User;
import com.agora.agora.model.dto.*;
import com.agora.agora.model.form.EditUserForm;
import com.agora.agora.model.form.UserForm;
import com.agora.agora.model.form.UserVerificationForm;
import com.agora.agora.service.StudyGroupService;
import com.agora.agora.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/user")
public class UserController {

    private UserService userService;
    @Autowired
    private StudyGroupService studyGroupService;
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
        final List<StudyGroupDTO> userGroups = optional.get().getStudyGroups().stream().map(groups -> new StudyGroupDTO(groups.getStudyGroup().getId(), groups.getStudyGroup().getName(), groups.getStudyGroup().getDescription(), groups.getStudyGroup().getCreator().getId(), groups.getStudyGroup().getCreationDate(), groups.getStudyGroup().getLabels().stream().map(label -> new LabelDTO(label.getLabel().getId(), label.getLabel().getName())).collect(Collectors.toList()))).collect(Collectors.toList());
        final Optional<FullUserDTO> userDTO = optional.map((user) -> new FullUserDTO(user.getId(), user.getName(), user.getSurname(), user.getEmail(), userGroups, user.getContactLinks()));
        return userDTO.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity getUserById(@PathVariable("id") int id) {
        final Optional<User> optional = userService.findById(id);
        final List<StudyGroupDTO> userGroups = optional.get().getStudyGroups().stream().map(groups -> new StudyGroupDTO(groups.getStudyGroup().getId(), groups.getStudyGroup().getName(), groups.getStudyGroup().getDescription(), groups.getStudyGroup().getCreator().getId(), groups.getStudyGroup().getCreationDate(), groups.getStudyGroup().getLabels().stream().map(label -> new LabelDTO(label.getLabel().getId(), label.getLabel().getName())).collect(Collectors.toList()))).collect(Collectors.toList());
        final Optional<FullUserDTO> userDTO = optional.map((user) -> new FullUserDTO(user.getId(), user.getName(), user.getSurname(), user.getEmail(), userGroups, user.getContactLinks()));
        return userDTO.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public List<FullUserDTO> getUsers(@RequestParam Optional<String> name){
        List<User> users = userService.findUsers(name);
        List<FullUserDTO> responseUsers = new ArrayList<>();
        for (User user : users) {
            final List<StudyGroupDTO> userGroups = user.getStudyGroups().stream().map(groups -> new StudyGroupDTO(groups.getStudyGroup().getId(), groups.getStudyGroup().getName(), groups.getStudyGroup().getDescription(), groups.getStudyGroup().getCreator().getId(), groups.getStudyGroup().getCreationDate(), groups.getStudyGroup().getLabels().stream().map(label -> new LabelDTO(label.getLabel().getId(), label.getLabel().getName())).collect(Collectors.toList()))).collect(Collectors.toList());
            responseUsers.add(new FullUserDTO(user.getId(), user.getName(), user.getSurname(), user.getEmail(), userGroups, user.getContactLinks()));
        }
        return responseUsers;
    }

    @GetMapping(value = "/notification/me")
    public List<NotificationDTO> getCurrentUserNotifications(){
        return userService.getCurrentUserNotifications();
    }

    @PostMapping(value = "/me")
    public ResponseEntity editUser(@Valid @RequestBody EditUserForm editUserForm){
        int id = userService.editUser(editUserForm);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/me")
    public ResponseEntity deleteUser(){
        int userId = userService.findCurrentUser().orElseThrow(()-> new NoSuchElementException("User does not exist.")).getId();
        studyGroupService.moveOwnership(userId);
        userService.deleteUser(userId);
        return ResponseEntity.ok().build();
    }
}
