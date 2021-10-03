package com.agora.agora.controller;

import com.agora.agora.model.Post;
import com.agora.agora.model.StudyGroup;
import com.agora.agora.model.StudyGroupUser;
import com.agora.agora.model.dto.*;
import com.agora.agora.model.form.EditStudyGroupForm;
import com.agora.agora.model.form.PostForm;
import com.agora.agora.model.form.StudyGroupForm;
import com.agora.agora.service.StudyGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.Optional;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/studyGroup")
public class StudyGroupController {

    private StudyGroupService groupService;

    @Autowired
    public StudyGroupController(StudyGroupService groupService) {
        this.groupService = groupService;
    }

    @PostMapping
    public StudyGroupIdDTO createStudyGroup(@Valid @RequestBody StudyGroupForm studyGroup){
        int id = groupService.create(studyGroup);
        return new StudyGroupIdDTO(id);
    }

    @GetMapping
    public List<StudyGroupDTO> getAllStudyGroups(@RequestParam Optional<String> text) {
        return groupService.findStudyGroups(text);
    }

    @GetMapping(value = "/me")
    public List<StudyGroupDTO> getCurrentUserGroups(){
        final List<StudyGroup> groups = groupService.findCurrentUserGroups();
        List<StudyGroupDTO> groupDTOs = groups.stream().map(group -> new StudyGroupDTO(group.getId(), group.getName(), group.getDescription(), group.getCreator().getId(), group.getCreationDate())).collect(Collectors.toList());
        return groupDTOs;
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity getStudyGroupInfoById(@PathVariable("id") int id){
        final Optional<StudyGroup> studyGroupOptional = groupService.findStudyGroupById(id);
        final List<StudyGroupUser> studyGroupUsers = groupService.findUsersInStudyGroup(id);
        final List<UserContactDTO> userContactDTOs = studyGroupUsers.stream().map((studyGroupUser) -> new UserContactDTO(studyGroupUser.getUser().getId(), studyGroupUser.getUser().getName(), studyGroupUser.getUser().getEmail())).collect(Collectors.toList());
        final Optional<FullStudyGroupDTO> studyGroupDTOOptional = studyGroupOptional.map((group) -> new FullStudyGroupDTO(group.getId(), group.getName(), group.getDescription(), group.getCreator().getId(), group.getCreationDate(), userContactDTOs));
        return studyGroupDTOOptional.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping(value = "/{id}/{userId}")
    public ResponseEntity addUserToStudyGroup(@PathVariable("id") int studyGroupId, @PathVariable("userId") int userId){
        groupService.addUserToStudyGroup(studyGroupId, userId);
        return ResponseEntity.ok().build();
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity modifyStudyGroupById(@PathVariable("id") int id, @Valid @RequestBody EditStudyGroupForm editGroupForm) {
        return groupService.update(id, editGroupForm) ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    @DeleteMapping(value = "/{id}/me")
    public ResponseEntity leaveGroup(@PathVariable("id") int groupId) {
        groupService.removeCurrentUserFromStudyGroup(groupId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping(value = "/{id}/inviteLink")
    public String getLinkToStudyGroupPage(@PathVariable("id") int studyGroupId){
        return groupService.getInviteLink(studyGroupId);
    }

    @PostMapping(value = "/{id}/forum")
    public ResponseEntity postAnnouncementToStudyGroup(@PathVariable("id") int studyGroupId, @Valid @RequestBody PostForm postForm) {
        int postId = groupService.createPost(studyGroupId, postForm);
        return ResponseEntity.created(URI.create("/studyGroup/" + studyGroupId + "/forum/" + postId)).build();
    }

    @GetMapping(value = "/{id}/forum")
    public List<PostDTO> getAllGroupMessages(@PathVariable("id") int studyGroupId){
        List<Post> groupPosts = groupService.getStudyGroupPosts(studyGroupId);
        List<PostDTO> postDTOS = groupPosts.stream().map(post -> new PostDTO(post.getId(), post.getContent(), post.getStudyGroup().getId(), post.getCreator().getId(), post.getCreationDateAndTime())).collect(Collectors.toList());
        return postDTOS;
    }

    @GetMapping(value = "/{id}/forum/{postId}")
    public ResponseEntity getGroupPostById(@PathVariable("id") int studyGroupId, @PathVariable("postId") int postId){
        Optional<Post> post = groupService.getStudyGroupPostById(studyGroupId, postId);
        Optional<PostDTO> postDTO = post.map(p -> new PostDTO(p.getId(), p.getContent(), p.getStudyGroup().getId(), p.getCreator().getId(), p.getCreationDateAndTime()));
        return postDTO.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }
}
