package com.agora.agora.repository;

import com.agora.agora.model.Post;
import com.agora.agora.model.StudyGroup;
import jdk.vm.ci.meta.Local;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface PostRepository extends CrudRepository<Post, Integer> {
    @Query(value = "select p from Post p " +
            "where p.studyGroup.id = (:id)" +
            "ORDER BY p.creationDateTime ASC")
    List<Post> findAllByStudyGroupId(@Param("id")int id);

    Page<Post> findAllByStudyGroupId(Pageable pageable, @Param("id") int id);

    Page<Post> findAllByCreationDateTimeIsBetweenAndStudyGroupId(Pageable pageable, @Param("dateFrom") LocalDateTime dateFrom, @Param("dateTo") LocalDateTime dateTo, @Param("id") int studyGroupId);

    Page<Post> findAllByStudyGroupIdAndCreationDateTimeBetweenAndContentIsContainingIgnoreCase(Pageable pageable, @Param("id") int studyGroupId, @Param("dateFrom")LocalDateTime dateFrom, @Param("dateTo") LocalDateTime dateTo, @Param("text") String text);

    Page<Post> findAllByCreationDateTimeAfterAndStudyGroupId(Pageable pageable, @Param("dateFrom")LocalDateTime dateFrom, @Param("id") int studyGroupId);

    Page<Post> findAllByStudyGroupIdAndCreationDateTimeAfterAndContentIsContainingIgnoreCase(Pageable pageable, @Param("id") int studyGroupId, @Param("dateFrom")LocalDateTime dateFrom, @Param("text") String text);

    Page<Post> findAllByCreationDateTimeBeforeAndStudyGroupId(Pageable pageable, @Param("dateTo") LocalDateTime dateTo, @Param("id") int studyGroupId);

    Page<Post> findAllByStudyGroupIdAndCreationDateTimeBeforeAndContentIsContainingIgnoreCase(Pageable pageable, @Param("id") int studyGroupId, @Param("dateTo") LocalDateTime dateTo, @Param("text") String text);

    Page<Post> findAllByStudyGroupIdAndContentIsContainingIgnoreCase(Pageable pageable, @Param("id") int studyGroupId, @Param("text") String text);

    void deleteAllByStudyGroupId(int studyGroupId);
}
