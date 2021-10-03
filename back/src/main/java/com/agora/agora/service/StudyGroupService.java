package com.agora.agora.service;

import com.agora.agora.exceptions.ForbiddenElementException;
import com.agora.agora.model.Post;
import com.agora.agora.model.StudyGroup;
import com.agora.agora.model.StudyGroupUser;
import com.agora.agora.model.User;
import com.agora.agora.model.dto.StudyGroupDTO;
import com.agora.agora.model.form.EditStudyGroupForm;
import com.agora.agora.model.form.PostForm;
import com.agora.agora.model.form.StudyGroupForm;
import com.agora.agora.repository.PostRepository;
import com.agora.agora.repository.StudyGroupRepository;
import com.agora.agora.repository.StudyGroupUsersRepository;
import com.agora.agora.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class StudyGroupService {

    private StudyGroupRepository groupRepository;
    private UserRepository userRepository;
    private PostRepository postRepository;
    private StudyGroupUsersRepository studyGroupUsersRepository;

    @Autowired
    public StudyGroupService(StudyGroupRepository groupRepository, UserRepository userRepository, PostRepository postRepository, StudyGroupUsersRepository studyGroupUsersRepository) {
        this.groupRepository = groupRepository;
        this.userRepository = userRepository;
        this.postRepository = postRepository;
        this.studyGroupUsersRepository = studyGroupUsersRepository;
    }

    public int create(StudyGroupForm studyGroup){
        Optional<User> userOptional = userRepository.findById(studyGroup.getCreatorId());
        User studyGroupCreator = userOptional.orElseThrow(() -> new NoSuchElementException(String.format("User: %d does not exist.", studyGroup.getCreatorId())));

        Optional<StudyGroup> studyGroupOptional = groupRepository.findByName(studyGroup.getName());
        if(studyGroupOptional.isPresent()){
            throw new DataIntegrityViolationException(String.format("Group name: %s already exists.", studyGroup.getName()));
        }
        //TODO: find by label

        StudyGroup group = new StudyGroup(studyGroup.getName(), studyGroup.getDescription(), studyGroupCreator, studyGroup.getCreationDate());
        groupRepository.save(group);

        StudyGroupUser studyGroupUser = new StudyGroupUser(studyGroupCreator, group);
        studyGroupUsersRepository.save(studyGroupUser);

        return group.getId();
    }

    public Optional<StudyGroup> findStudyGroupById(int id){
         return Optional.of(groupRepository.findById(id)).orElseThrow(() -> new DataIntegrityViolationException(String.format("Group: %d does not exist", id)));
    }

    public List<StudyGroupDTO> findStudyGroups(Optional<String> text) {
        String email = ((org.springframework.security.core.userdetails.User)
                SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        Optional<User> optionalUser = userRepository.findUserByEmail(email);

        if(optionalUser.isPresent()) {
            User user = optionalUser.get();
            List<StudyGroup> studyGroups;
            List<StudyGroupDTO> studyGroupDTOS = new ArrayList<>();
            if (text.isPresent()) {
                studyGroups = studyGroupUsersRepository.findByNameOrDescription(text.get());
            }else {
                studyGroups = groupRepository.findAll();
            }
            for (StudyGroup studyGroup : studyGroups) {
                if (studyGroupUsersRepository.findStudyGroupUserByStudyGroupIdAndAndUserId(studyGroup.getId(), user.getId()).isPresent()) {
                    StudyGroupDTO studyGroupDTO = new StudyGroupDTO(studyGroup.getId(), studyGroup.getName(), studyGroup.getDescription(), studyGroup.getCreator().getId(), studyGroup.getCreationDate());
                    studyGroupDTO.setCurrentUserIsMember(true);
                    studyGroupDTOS.add(studyGroupDTO);
                }else{
                    StudyGroupDTO studyGroupDTO = new StudyGroupDTO(studyGroup.getId(), studyGroup.getName(), studyGroup.getDescription(), studyGroup.getCreator().getId(), studyGroup.getCreationDate());
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
            // TODO: Agregar la modificacion de Labels cuando esten diponibles!!
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
        return "http://localhost:3000/studyGroup/"+id;
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
}
