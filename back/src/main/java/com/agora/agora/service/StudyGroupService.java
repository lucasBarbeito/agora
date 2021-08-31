package com.agora.agora.service;

import com.agora.agora.model.StudyGroup;
import com.agora.agora.model.User;
import com.agora.agora.model.form.StudyGroupForm;
import com.agora.agora.repository.StudyGroupRepository;
import com.agora.agora.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class StudyGroupService {

    private StudyGroupRepository groupRepository;
    private UserRepository userRepository;

    @Autowired
    public StudyGroupService(StudyGroupRepository groupRepository, UserRepository userRepository) {
        this.groupRepository = groupRepository;
        this.userRepository = userRepository;
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
        return group.getId();
    }



}
