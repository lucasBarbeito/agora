package com.agora.agora.service;

import com.agora.agora.exceptions.ForbiddenElementException;
import com.agora.agora.model.*;
import com.agora.agora.model.dto.LabelDTO;
import com.agora.agora.model.dto.LabelIdDTO;
import com.agora.agora.model.dto.StudyGroupDTO;
import com.agora.agora.model.form.EditStudyGroupForm;
import com.agora.agora.model.form.PostForm;
import com.agora.agora.model.form.StudyGroupForm;
import com.agora.agora.repository.PostRepository;
import com.agora.agora.repository.StudyGroupRepository;
import com.agora.agora.repository.StudyGroupUsersRepository;
import com.agora.agora.repository.UserRepository;
import com.agora.agora.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class StudyGroupService {

    private StudyGroupRepository groupRepository;
    private UserRepository userRepository;
    private PostRepository postRepository;
    private StudyGroupUsersRepository studyGroupUsersRepository;
    private StudyGroupLabelRepository studyGroupLabelRepository;
    private LabelRepository labelRepository;
    private NewMemberNotificationRepository newMemberNotificationRepository;
    private NewPostNotificationRepository newPostNotificationRepository;

    private UserService userService;

    @Autowired
    public StudyGroupService(StudyGroupRepository groupRepository, UserRepository userRepository, PostRepository postRepository, StudyGroupUsersRepository studyGroupUsersRepository, StudyGroupLabelRepository studyGroupLabelRepository, LabelRepository labelRepository, UserService userService, NewMemberNotificationRepository newMemberNotificationRepository, NewPostNotificationRepository newPostNotificationRepository) {
        this.groupRepository = groupRepository;
        this.userRepository = userRepository;
        this.postRepository = postRepository;
        this.studyGroupUsersRepository = studyGroupUsersRepository;
        this.studyGroupLabelRepository = studyGroupLabelRepository;
        this.labelRepository = labelRepository;
        this.newMemberNotificationRepository = newMemberNotificationRepository;
        this.newPostNotificationRepository = newPostNotificationRepository;
        this.userService = userService;
    }

    public int create(StudyGroupForm studyGroup) {
        Optional<User> userOptional = userRepository.findById(studyGroup.getCreatorId());
        User studyGroupCreator = userOptional.orElseThrow(() -> new NoSuchElementException(String.format("User: %d does not exist.", studyGroup.getCreatorId())));

        Optional<StudyGroup> studyGroupOptional = groupRepository.findByName(studyGroup.getName());
        if(studyGroupOptional.isPresent()){
            throw new DataIntegrityViolationException(String.format("Group name: %s already exists.", studyGroup.getName()));
        }
        //Label Check
        List<LabelIdDTO> labels = studyGroup.getLabels();
        if(labels.isEmpty()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        List<Label> groupLabels = new ArrayList<>();
        for(LabelIdDTO label : labels) {
            Optional<Label> labelOptional = labelRepository.findById(label.getId());
            if (!labelOptional.isPresent()) {
                throw new NoSuchElementException("Label does not exist.");
            }else{
                groupLabels.add(labelOptional.get());
            }
        }

        StudyGroup group = new StudyGroup(studyGroup.getName(), studyGroup.getDescription(), studyGroupCreator, studyGroup.getCreationDate());
        groupRepository.save(group);

        for (Label groupLabel : groupLabels) {
            StudyGroupLabel studyGroupLabel = new StudyGroupLabel(groupLabel, group);
            studyGroupLabelRepository.save(studyGroupLabel);
        }

        StudyGroupUser studyGroupUser = new StudyGroupUser(studyGroupCreator, group);
        studyGroupUsersRepository.save(studyGroupUser);

        return group.getId();
    }

    public Optional<StudyGroup> findStudyGroupById(int id){
         return Optional.of(groupRepository.findById(id)).orElseThrow(() -> new DataIntegrityViolationException(String.format("Group: %d does not exist", id)));
    }

    public List<StudyGroupDTO> findStudyGroups(Optional<String> text, Optional<List<Integer>> labelIds) {
        String email = ((org.springframework.security.core.userdetails.User)
                SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        Optional<User> optionalUser = userRepository.findUserByEmail(email);

        if(optionalUser.isPresent()) {
            User user = optionalUser.get();
            List<StudyGroup> studyGroups;
            List<StudyGroupDTO> studyGroupDTOS = new ArrayList<>();
            if (text.isPresent() && labelIds.isPresent() && labelIds.get().size() > 0) {
                studyGroups = findByLabelIdsAndText(text.get(), labelIds.get());
            } else if (labelIds.isPresent() && labelIds.get().size() > 0){
                studyGroups = findByLabelIds(labelIds.get());
            }else
                if (text.isPresent()) {
                studyGroups = studyGroupUsersRepository.findByNameOrDescription(text.get());
            }else {
                studyGroups = groupRepository.findAll();
            }
            for (StudyGroup studyGroup : studyGroups) {
                if (studyGroupUsersRepository.findStudyGroupUserByStudyGroupIdAndAndUserId(studyGroup.getId(), user.getId()).isPresent()) {
                    List<StudyGroupLabel> studyGroupLabels = studyGroupLabelRepository.findByStudyGroupId(studyGroup.getId());
                    List<LabelDTO> labels = studyGroupLabels.stream().map(label -> new LabelDTO(label.getLabel().getId(), label.getLabel().getName())).collect(Collectors.toList());
                    StudyGroupDTO studyGroupDTO = new StudyGroupDTO(studyGroup.getId(), studyGroup.getName(), studyGroup.getDescription(), studyGroup.getCreator().getId(), studyGroup.getCreationDate(), labels);
                    studyGroupDTO.setCurrentUserIsMember(true);
                    studyGroupDTOS.add(studyGroupDTO);
                }else{
                    List<StudyGroupLabel> studyGroupLabels = studyGroupLabelRepository.findByStudyGroupId(studyGroup.getId());
                    List<LabelDTO> labels = studyGroupLabels.stream().map(label -> new LabelDTO(label.getLabel().getId(), label.getLabel().getName())).collect(Collectors.toList());
                    StudyGroupDTO studyGroupDTO = new StudyGroupDTO(studyGroup.getId(), studyGroup.getName(), studyGroup.getDescription(), studyGroup.getCreator().getId(), studyGroup.getCreationDate(), labels);
                    studyGroupDTO.setCurrentUserIsMember(false);
                    studyGroupDTOS.add(studyGroupDTO);
                }
            }
            return studyGroupDTOS;
        }else{
            throw new NoSuchElementException("User does not exist.");
        }
    }

    public void addUserToStudyGroup(int studyGroupId, int userId){
        Optional<User> userOptional = userRepository.findById(userId);
        User user = userOptional.orElseThrow(() -> new NoSuchElementException(String.format("User: %d does not exist.", userId)));
        Optional<StudyGroup> groupOptional = groupRepository.findById(studyGroupId);
        StudyGroup studyGroup = groupOptional.orElseThrow(() -> new NoSuchElementException(String.format("Study Group: %d does not exist.", studyGroupId)));
        List<StudyGroupUser> checker = studyGroupUsersRepository.findStudyGroupUserByStudyGroupId(studyGroupId);

        for (StudyGroupUser groupUser : checker) {
            if (groupUser.getUser().getId() == userId) {
                throw new DataIntegrityViolationException(String.format("User: %d is already in Study Group", userId));
            }
        }
        //New Notification
        for (StudyGroupUser groupUser : checker) {
            NewMemberNotification notification = new NewMemberNotification(groupUser.getUser(), false, LocalDate.now(), user, studyGroup);
            newMemberNotificationRepository.save(notification);
        }

        StudyGroupUser studyGroupUser = new StudyGroupUser(user, studyGroup);
        studyGroupUsersRepository.save(studyGroupUser);

    }

    public List<StudyGroupUser> findUsersInStudyGroup(int studyGroupId){
        return studyGroupUsersRepository.findStudyGroupUserByStudyGroupId(studyGroupId);
    }

    public boolean update(int id, EditStudyGroupForm editGroupForm) {
        final Optional<StudyGroup> groupId = groupRepository.findById(id);
        if (groupId.isPresent()) {
            StudyGroup studyGroup = groupId.get();
            studyGroup.setName(editGroupForm.getName());
            studyGroup.setDescription(editGroupForm.getDescription());

            //If the list provided is empty, it should not change the existent Labels.
            if (!editGroupForm.getLabels().isEmpty()) {
                List<LabelIdDTO> labels = editGroupForm.getLabels();
                List<Label> groupLabels = new ArrayList<>();
                for (LabelIdDTO label : labels) {
                    Optional<Label> labelOptional = labelRepository.findById(label.getId());
                    if (!labelOptional.isPresent()) {
                        throw new NoSuchElementException("Label does not exist.");
                    } else {
                        groupLabels.add(labelOptional.get());
                    }
                }

                //If given form does not contain existent group labels, this should be deleted.
                for (int i = 0; i < studyGroup.getLabels().size(); i++) {
                    if(!groupLabels.contains(studyGroup.getLabels().get(i).getLabel())){
                        studyGroupLabelRepository.delete(studyGroup.getLabels().get(i));
                    }
                }


                List<StudyGroupLabel> newLabels = new ArrayList<>();
                for (Label groupLabel : groupLabels) {
                    if(!studyGroupLabelRepository.findByStudyGroupIdAndAndLabelId(id, groupLabel.getId()).isPresent()) {
                        StudyGroupLabel studyGroupLabel = new StudyGroupLabel(groupLabel, studyGroup);
                        studyGroupLabelRepository.save(studyGroupLabel);
                        newLabels.add(studyGroupLabel);
                    }
                }
                studyGroup.setLabels(newLabels);
            }
            groupRepository.save(studyGroup);
            return true;
        }
        return false;
    }

    public void removeCurrentUserFromStudyGroup(int groupId) {
        String email = ((org.springframework.security.core.userdetails.User)
                SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        Optional<User> userOptional = userRepository.findUserByEmail(email);
        Optional<StudyGroup> groupOptional = groupRepository.findById(groupId);

        if (groupOptional.isPresent()) {
            if (userOptional.isPresent()) {
                Optional<StudyGroupUser> studyGroupUserOptional = studyGroupUsersRepository.findStudyGroupUserByStudyGroupIdAndAndUserId(groupOptional.get().getId(), userOptional.get().getId());

                if (studyGroupUserOptional.isPresent()) {
                    if (studyGroupUserOptional.get().getUser().getId() != groupOptional.get().getCreator().getId()) {
                        studyGroupUsersRepository.delete(studyGroupUserOptional.get());
                    } else {
                        throw new ForbiddenElementException("Group creator cannot leave a group");
                    }
                } else {
                    throw new NoSuchElementException("User does not belong to study group.");
                }
            }else{
                throw new NoSuchElementException("User does not exist");
            }
        }else {
            throw new NoSuchElementException("Group does not exist");
        }
    }

    public String getInviteLink(int id) {
        return "http://localhost:3000/group/"+id;
    }

    public int createPost(int studyGroupId, PostForm postForm) {
        String email = ((org.springframework.security.core.userdetails.User)
                SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        Optional<User> optionalUser = userRepository.findUserByEmail(email);

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            Optional<StudyGroup> studyGroupOptional = groupRepository.findById(studyGroupId);
            if (studyGroupOptional.isPresent()) {
                StudyGroup studyGroup = studyGroupOptional.get();
                Optional<StudyGroupUser> optionalStudyGroupUser = studyGroupUsersRepository.findStudyGroupUserByStudyGroupIdAndAndUserId(studyGroupId, user.getId());
                if (optionalStudyGroupUser.isPresent()) {
                    Post post = new Post(postForm.getContent(), studyGroup, user, postForm.getCreationDateAndTime());
                    postRepository.save(post);

                    //New Notification
                    List<StudyGroupUser> studyGroupUsers = studyGroupUsersRepository.findStudyGroupUserByStudyGroupId(studyGroupId);
                    for (StudyGroupUser groupUser : studyGroupUsers) {
                        if(groupUser.getUser().getId() != user.getId()) {
                            NewPostNotification notification = new NewPostNotification(groupUser.getUser(), false, LocalDate.now(), post, studyGroup);
                            newPostNotificationRepository.save(notification);
                        }
                    }

                    return post.getId();
                }
                throw new ForbiddenElementException("User does not belong to study group.");
            }
            throw new NoSuchElementException("Group does not exist");
        }
        throw new NoSuchElementException("User does not exist");

    }

    public List<StudyGroup> findCurrentUserGroups(){
        String email = ((org.springframework.security.core.userdetails.User)
                SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        Optional<User> optionalUser = userRepository.findUserByEmail(email);

        if(optionalUser.isPresent()){
            User currentUser = optionalUser.get();
            List<StudyGroupUser> userGroups = studyGroupUsersRepository.findStudyGroupUserByUserId(currentUser.getId());
            List<StudyGroup> myGroups = new ArrayList<>();
            for (StudyGroupUser userGroup : userGroups) {
                myGroups.add(userGroup.getStudyGroup());
            }
            return myGroups;
        }
        else{
            throw new NoSuchElementException("User does not exist.");
        }
    }

    public List<Post> getStudyGroupPosts(int studyGroupId){
        String email = ((org.springframework.security.core.userdetails.User)
                SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        Optional<User> optionalUser = userRepository.findUserByEmail(email);

        if(optionalUser.isPresent()){
            User user = optionalUser.get();
            Optional<StudyGroup> groupOptional = groupRepository.findById(studyGroupId);
            if(groupOptional.isPresent()){
                Optional<StudyGroupUser> optionalStudyGroupUser = studyGroupUsersRepository.findStudyGroupUserByStudyGroupIdAndAndUserId(studyGroupId, user.getId());
                if(optionalStudyGroupUser.isPresent()){
                    return postRepository.findAllByStudyGroupId(studyGroupId);
                }
                throw new ForbiddenElementException("Only members can see posts.");
            }
            throw new NoSuchElementException("Group does not exist");
        }
        throw new NoSuchElementException("User does not exist");
    }

    public void deleteGroup(int studyGroupId){
        String email = ((org.springframework.security.core.userdetails.User)
                SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        Optional<User> optionalUser = userRepository.findUserByEmail(email);

        if(optionalUser.isPresent()){
            User user = optionalUser.get();
            Optional<StudyGroup> groupOptional = groupRepository.findById(studyGroupId);
            if(groupOptional.isPresent()){
                StudyGroup studyGroup = groupOptional.get();
                if(studyGroup.getCreator().getId() == user.getId()){
                    postRepository.deleteAll(postRepository.findAllByStudyGroupId(studyGroupId));
                    studyGroupLabelRepository.deleteAll(studyGroupLabelRepository.findByStudyGroupId(studyGroupId));
                    studyGroupUsersRepository.deleteAll(studyGroupUsersRepository.findStudyGroupUserByStudyGroupId(studyGroupId));
                    groupRepository.deleteById(studyGroupId);
                }else{
                    throw new ForbiddenElementException("Only group creator can delete group.");
                }
            }else{
                throw new NoSuchElementException("Group does not exist.");
            }
        }else{
            throw new NoSuchElementException("User does not exist");
        }
    }

    public Optional<Post> getStudyGroupPostById(int studyGroupId, int postId){
        String email = ((org.springframework.security.core.userdetails.User)
                SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        Optional<User> optionalUser = userRepository.findUserByEmail(email);

        if(optionalUser.isPresent()){
            User user = optionalUser.get();
            Optional<StudyGroup> groupOptional = groupRepository.findById(studyGroupId);
            if(groupOptional.isPresent()){
                Optional<StudyGroupUser> optionalStudyGroupUser = studyGroupUsersRepository.findStudyGroupUserByStudyGroupIdAndAndUserId(studyGroupId, user.getId());
                if(optionalStudyGroupUser.isPresent()){
                    return postRepository.findById(postId);
                }
                throw new ForbiddenElementException("Only members can see posts.");
            }
            throw new NoSuchElementException("Group does not exist");
        }
        throw new NoSuchElementException("User does not exist");
    }
    public void addCurrentUserToStudyGroup(int studyGroupId) {
        int currentUserId = userService.findCurrentUser().orElseThrow(NoSuchElementException::new).getId();
        addUserToStudyGroup(studyGroupId, currentUserId);
    }

    public void deletePost(int groupId, int postId) {
        Optional<StudyGroup> studyGroupOptional = groupRepository.findById(groupId);
        if (!studyGroupOptional.isPresent()) throw new NoSuchElementException("Group does not exist");
        Optional<Post> postOptional = postRepository.findById(postId);
        if (!postOptional.isPresent()) throw new NoSuchElementException("Post does not exist");
        String email = ((org.springframework.security.core.userdetails.User)
                SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        User user = userRepository.findUserByEmail(email).get();
        if (user.getId() == studyGroupOptional.get().getCreator().getId() || user.getId() == postOptional.get().getCreator().getId()) {
            postRepository.delete(postOptional.get());
            return;
        }
        throw new ForbiddenElementException("User cannot delete post.");
    }

    public List<StudyGroupLabel> findStudyGroupLabelsById(int studyGroupId){
        Optional<StudyGroup> groupOptional = groupRepository.findById(studyGroupId);

        if(groupOptional.isPresent()){
            return studyGroupLabelRepository.findByStudyGroupId(studyGroupId);
        }
        else{
            throw new NoSuchElementException("Group does not exist");
        }
    }

    public List<Label> findAllLabelsInSystem(){
        List<Label> labels = labelRepository.findAll();
        if(labels.isEmpty()){
            throw new NoSuchElementException("There are no Labels available");
        }else {
            return labels;
        }
    }

    private List<StudyGroup> findByLabelIds(List<Integer> labelIds) {
        if (labelIds.size() == 1) {
            return groupRepository.findByLabelId(labelIds.get(0));
        } else {
            return groupRepository.findByLabelIdIn(labelIds);
        }
    }

    private List<StudyGroup> findByLabelIdsAndText(String text, List<Integer> labelIds) {
        if (labelIds.size() == 1) {
            return groupRepository.findByLabelIdAndText(labelIds.get(0), text);
        } else {
            return groupRepository.findByLabelIdInAndText(labelIds, text);
        }
    }
}
