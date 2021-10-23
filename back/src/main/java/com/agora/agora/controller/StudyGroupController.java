package com.agora.agora.controller;

import com.agora.agora.model.*;
import com.agora.agora.model.dto.*;
import com.agora.agora.model.form.EditStudyGroupForm;
import com.agora.agora.model.form.PostForm;
import com.agora.agora.model.form.StudyGroupForm;
import com.agora.agora.service.StudyGroupService;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.Optional;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/studyGroup")
@EnableSpringDataWebSupport
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
    public List<StudyGroupDTO> getAllStudyGroups(@RequestParam Optional<String> text, @RequestParam Optional<List<Integer>> label) {
        return getAllStudyGroups(text,Optional.of(0),label).getContent();
    }

    @GetMapping(value = "/paged")
    public Page<StudyGroupDTO> getAllStudyGroups(@RequestParam Optional<String> text, @ApiParam(value = "Query param for page number") @RequestParam(value = "page") Optional<Integer> page, @RequestParam Optional<List<Integer>> label) {
        return groupService.findStudyGroups(text, page.orElse(0), label);
    }

    @GetMapping(value = "/me")
    public List<StudyGroupDTO> getCurrentUserGroups(@RequestParam Optional<String> text, @RequestParam Optional<List<Integer>> label){
        return getCurrentUserGroups(text,Optional.of(0),label).getContent();
    }

    @GetMapping(value = "/me/paged")
    public Page<StudyGroupDTO> getCurrentUserGroups(@RequestParam Optional<String> text, @ApiParam(value = "Query param for page number") @RequestParam(value = "page") Optional<Integer> page, @RequestParam Optional<List<Integer>> label){
        return groupService.findCurrentUserGroups(text, page.orElse(0), label);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity getStudyGroupInfoById(@PathVariable("id") int id){
        final Optional<StudyGroup> studyGroupOptional = groupService.findStudyGroupById(id);
        final List<StudyGroupUser> studyGroupUsers = groupService.findUsersInStudyGroup(id);
        final List<UserContactDTO> userContactDTOs = studyGroupUsers.stream().map((studyGroupUser) -> new UserContactDTO(studyGroupUser.getUser().getId(), studyGroupUser.getUser().getName(), studyGroupUser.getUser().getSurname(), studyGroupUser.getUser().getEmail(), studyGroupUser.getUser().getContactLinks())).collect(Collectors.toList());
        final List<StudyGroupLabel> studyGroupLabels = groupService.findStudyGroupLabelsById(id);
        final List<LabelDTO> labelDTOs = studyGroupLabels.stream().map(label -> new LabelDTO(label.getLabel().getId(), label.getLabel().getName())).collect(Collectors.toList());
        final Optional<FullStudyGroupDTO> studyGroupDTOOptional = studyGroupOptional.map((group) -> new FullStudyGroupDTO(group.getId(), group.getName(), group.getDescription(), group.getCreator().getId(), group.getCreationDate(), userContactDTOs, labelDTOs));
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

    @DeleteMapping(value = "/{id}/forum/{postId}")
    public ResponseEntity deletePost(@PathVariable("id") int groupId, @PathVariable("postId") int postId) {
        groupService.deletePost(groupId, postId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping(value = "/{id}/forum")
    public List<PostDTO> getAllGroupMessages(@PathVariable("id") int studyGroupId){
        List<Post> groupPosts = groupService.getStudyGroupPosts(studyGroupId);
        List<PostDTO> postDTOS = groupPosts.stream().map(post -> new PostDTO(post.getId(), post.getContent(), post.getStudyGroup().getId(), post.getCreator().getId(), post.getCreationDateAndTime())).collect(Collectors.toList());
        return postDTOS;
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity deleteGroupById(@PathVariable("id") int studyGroupId){
        groupService.deleteGroup(studyGroupId);
        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "/{id}/forum/{postId}")
    public ResponseEntity getGroupPostById(@PathVariable("id") int studyGroupId, @PathVariable("postId") int postId){
        Optional<Post> post = groupService.getStudyGroupPostById(studyGroupId, postId);
        Optional<PostDTO> postDTO = post.map(p -> new PostDTO(p.getId(), p.getContent(), p.getStudyGroup().getId(), p.getCreator().getId(), p.getCreationDateAndTime()));
        return postDTO.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping(value = "/{id}/me")
    public ResponseEntity addCurrentUserToGroup(@PathVariable("id") int studyGroupId){
        groupService.addCurrentUserToStudyGroup(studyGroupId);
        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "/label")
    public List<LabelDTO> getAllLabelsAvailable(){
        List<Label> labels = groupService.findAllLabelsInSystem();
        List<LabelDTO> labelDTO = labels.stream().map(label -> new LabelDTO(label.getId(), label.getName())).collect(Collectors.toList());
        return labelDTO;
    }
}
