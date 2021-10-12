package com.agora.agora.repository;

import com.agora.agora.model.StudyGroup;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface StudyGroupRepository extends PagingAndSortingRepository<StudyGroup, Integer> {
    Optional<StudyGroup> findByName(String name);
    Page<StudyGroup> findAll(Pageable pageable);

    @Query(value = "select sg from StudyGroup sg " +
            "where (lower(sg.name) like lower(concat('%',concat(:text, '%')))) " +
            "or (lower(sg.description) like lower(concat('%',concat(:text, '%'))))")
    Page<StudyGroup> findStudyGroupByNameOrDescription(@Param("text")String text, Pageable pageable);
}
