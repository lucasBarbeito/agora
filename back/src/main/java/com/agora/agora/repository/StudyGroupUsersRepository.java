package com.agora.agora.repository;

import com.agora.agora.model.StudyGroup;
import com.agora.agora.model.StudyGroupUser;
import com.agora.agora.model.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface StudyGroupUsersRepository extends CrudRepository<StudyGroupUser, Integer> {
    List<StudyGroupUser> findStudyGroupUserByStudyGroupId(int studyGroupId);
    Optional<StudyGroupUser> findStudyGroupUserByStudyGroupIdAndAndUserId(int studyGroupId, int userId);
    List<StudyGroupUser> findStudyGroupUserByUserId(int userId);

    @Query(value = "select sg from StudyGroup sg " +
                        "where (lower(sg.name) like lower(concat('%',concat(:text, '%')))) " +
                            "or (lower(sg.description) like lower(concat('%',concat(:text, '%'))))")
    List<StudyGroup> findByNameOrDescription(@Param("text")String text);

    void deleteAllByStudyGroupId(int studyGroupId);
}
