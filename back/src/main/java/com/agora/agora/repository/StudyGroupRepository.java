package com.agora.agora.repository;

import com.agora.agora.model.StudyGroup;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface StudyGroupRepository extends PagingAndSortingRepository<StudyGroup, Integer> {
    Optional<StudyGroup> findByName(String name);
    Page<StudyGroup> findAll(Pageable pageable);

    @Query(value = "select sg from StudyGroup sg " +
            "where (lower(sg.name) like lower(concat('%',concat(:text, '%')))) " +
            "or (lower(sg.description) like lower(concat('%',concat(:text, '%'))))")
    Page<StudyGroup> findStudyGroupByNameOrDescription(@Param("text")String text, Pageable pageable);
    @Query(value = "select DISTINCT sg from StudyGroup sg join sg.labels labels join labels.label lbl where lbl.id = :id")
    List<StudyGroup> findByLabelId(@Param("id") int labelId);

    @Query(value = "select DISTINCT sg from StudyGroup sg join sg.labels labels join labels.label lbl where lbl.id in (:ids)")
    List<StudyGroup> findByLabelIdIn(@Param("ids") List<Integer> labelIds);

    @Query(value = "select DISTINCT sg from StudyGroup sg join sg.labels labels join labels.label lbl " +
            "where lbl.id = :id and" +
            "(lower(sg.name) like lower(concat('%',concat(:text, '%')))) " +
            "or (lower(sg.description) like lower(concat('%',concat(:text, '%'))))")
    List<StudyGroup> findByLabelIdAndText(@Param("id") int labelId, @Param("text") String text);

    @Query(value = "select DISTINCT sg from StudyGroup sg join sg.labels labels join labels.label lbl " +
            "where lbl.id in (:ids) and" +
            "(lower(sg.name) like lower(concat('%',concat(:text, '%')))) " +
            "or (lower(sg.description) like lower(concat('%',concat(:text, '%'))))")
    List<StudyGroup> findByLabelIdInAndText(@Param("ids") List<Integer> labelIds, @Param("text") String text);
}
