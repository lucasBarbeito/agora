package com.agora.agora.repository;

import com.agora.agora.model.StudyGroup;
import com.agora.agora.model.StudyGroupUser;
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
    Page<StudyGroup> findByLabelId(@Param("id") int labelId, Pageable pageable);

    @Query(value = "select DISTINCT sg from StudyGroup sg join sg.labels labels join labels.label lbl where lbl.id in (:ids)")
    Page<StudyGroup> findByLabelIdIn(@Param("ids") List<Integer> labelIds, Pageable pageable);

    @Query(value = "select DISTINCT sg from StudyGroup sg join sg.labels labels join labels.label lbl " +
            "where lbl.id = :id and" +
            "((lower(sg.name) like lower(concat('%',concat(:text, '%')))) " +
            "or (lower(sg.description) like lower(concat('%',concat(:text, '%')))))")
    Page<StudyGroup> findByLabelIdAndText(@Param("id") int labelId, @Param("text") String text, Pageable pageable);

    @Query(value = "select DISTINCT sg from StudyGroup sg join sg.labels labels join labels.label lbl " +
            "where lbl.id in (:ids) and" +
            "((lower(sg.name) like lower(concat('%',concat(:text, '%')))) " +
            "or (lower(sg.description) like lower(concat('%',concat(:text, '%')))))")
    Page<StudyGroup> findByLabelIdInAndText(@Param("ids") List<Integer> labelIds, @Param("text") String text,Pageable pageable);

    @Query(value = "select DISTINCT sg from StudyGroup sg " +
            "join sg.users users " +
            "join users.user sg_user " +
            "where (lower(sg.name) like lower(concat('%',concat(:text, '%')))" +
            "or (lower(sg.description) like lower(concat('%',concat(:text, '%')))))" +
            "and sg_user.id = :id")
    Page<StudyGroup> findStudyGroupUserByUserIdAndText(@Param("id")int id, @Param("text")String text, Pageable pageable);

    @Query(value = "select DISTINCT sg from StudyGroup sg " +
            "join sg.users users " +
            "join users.user sg_user " +
            "where sg_user.id = :id")
    Page<StudyGroup> findStudyGroupUserByUserId(@Param("id")int id, Pageable pageable);

    @Query(value = "select DISTINCT sg from StudyGroup sg " +
            "join sg.users users " +
            "join users.user sg_user " +
            "join sg.labels labels " +
            "join labels.label lbl " +
                "where lbl.id = :labelId " +
                    "and ((lower(sg.name) like lower(concat('%',concat(:text, '%'))) " +
                        "or (lower(sg.description) like lower(concat('%',concat(:text, '%')))))) " +
                    "and sg_user.id = :id")
    Page<StudyGroup> findStudyGroupUserByUserIdAndTextAndLabel(@Param("id")int id, @Param("labelId") int labelId, @Param("text") String text, Pageable pageable);

    @Query(value = "select DISTINCT sg from StudyGroup sg " +
            "join sg.users users " +
            "join users.user sg_user " +
            "join sg.labels labels " +
            "join labels.label lbl " +
                "where lbl.id in (:ids) " +
                    "and ((lower(sg.name) like lower(concat('%',concat(:text, '%')))" +
                        "or (lower(sg.description) like lower(concat('%',concat(:text, '%'))))))" +
                    "and sg_user.id = :id")
    Page<StudyGroup> findByUserLabelIdInAndText(@Param("id")int id, @Param("ids") List<Integer> labelIds, @Param("text") String text, Pageable pageable);

    @Query(value = "select DISTINCT sg from StudyGroup sg " +
            "join sg.users users " +
            "join users.user sg_user " +
            "join sg.labels labels " +
            "join labels.label lbl " +
                "where lbl.id = :labelId " +
                    "and sg_user.id = :id")
    Page<StudyGroup> findByUserAndLabelId(@Param("id")int id, @Param("labelId") int labelId, Pageable pageable);

    @Query(value = "select DISTINCT sg from StudyGroup sg " +
            "join sg.users users " +
            "join users.user sg_user " +
            "join sg.labels labels " +
            "join labels.label lbl " +
                "where lbl.id in (:ids) " +
                    "and sg_user.id = :id")
    Page<StudyGroup> findByUserAndLabelIdIn(@Param("id")int id, @Param("ids") List<Integer> labelIds, Pageable pageable);
    List<StudyGroup> findAllByCreatorId(int creatorId);
    void deleteAllByCreatorId(int creatorId);
}
