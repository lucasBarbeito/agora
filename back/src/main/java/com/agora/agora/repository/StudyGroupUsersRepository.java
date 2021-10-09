package com.agora.agora.repository;

import com.agora.agora.model.StudyGroup;
import com.agora.agora.model.StudyGroupUser;
import com.agora.agora.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface StudyGroupUsersRepository extends PagingAndSortingRepository<StudyGroupUser, Integer> {
    List<StudyGroupUser> findStudyGroupUserByStudyGroupId(int studyGroupId);
    Optional<StudyGroupUser> findStudyGroupUserByStudyGroupIdAndAndUserId(int studyGroupId, int userId);
    List<StudyGroupUser> findStudyGroupUserByUserId(int userId);

    @Query(value = "select sg from StudyGroup sg " +
                        "where (lower(sg.name) like lower(:text)) " +
                            "or (lower(sg.description) like lower(concat('%',concat(:text, '%'))))")
    Page<StudyGroup> findByNameOrDescription(@Param("text")String text, Pageable pageable);

    void deleteAllByStudyGroupId(int studyGroupId);
}
