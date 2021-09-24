package com.agora.agora.service;

import com.agora.agora.model.StudyGroup;
import com.agora.agora.model.StudyGroupUser;
import com.agora.agora.model.User;
import com.agora.agora.model.form.StudyGroupForm;
import com.agora.agora.repository.StudyGroupRepository;
import com.agora.agora.repository.StudyGroupUsersRepository;
import com.agora.agora.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class StudyGroupService {

    private StudyGroupRepository groupRepository;
    private UserRepository userRepository;
    private StudyGroupUsersRepository studyGroupUsersRepository;

    @Autowired
    public StudyGroupService(StudyGroupRepository groupRepository, UserRepository userRepository, StudyGroupUsersRepository studyGroupUsersRepository) {
        this.groupRepository = groupRepository;
        this.userRepository = userRepository;
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

    public List<StudyGroup> findStudyGroups(Optional<String> text) {
        if (text.isPresent()) {
            return studyGroupUsersRepository.findByNameOrDescription(text.get());
        }
        return groupRepository.findAll();
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
}
