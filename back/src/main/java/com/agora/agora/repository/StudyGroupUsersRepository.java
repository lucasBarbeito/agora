package com.agora.agora.repository;

import com.agora.agora.model.StudyGroupUser;
import com.agora.agora.model.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface StudyGroupUsersRepository extends CrudRepository<StudyGroupUser, Integer> {
    List<StudyGroupUser> findStudyGroupUserByStudyGroupId(int studyGroupId);
}
