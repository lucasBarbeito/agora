package com.agora.agora.repository;

import com.agora.agora.model.Post;
import com.agora.agora.model.StudyGroup;
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
            "ORDER BY p.creationDateTime DESC")
    List<Post> findAllByStudyGroupId(@Param("id")int id);

    Page<Post> findAllByStudyGroupIdOrderByCreationDateTimeDesc(Pageable pageable, @Param("id") int id);

    Page<Post> findAllByCreationDateTimeIsBetweenAndStudyGroupIdOrderByCreationDateTimeDesc(Pageable pageable, @Param("dateFrom") LocalDateTime dateFrom, @Param("dateTo") LocalDateTime dateTo, @Param("id") int studyGroupId);

    Page<Post> findAllByStudyGroupIdAndCreationDateTimeBetweenAndContentIsContainingIgnoreCaseOrderByCreationDateTimeDesc(Pageable pageable, @Param("id") int studyGroupId, @Param("dateFrom")LocalDateTime dateFrom, @Param("dateTo") LocalDateTime dateTo, @Param("text") String text);

    Page<Post> findAllByCreationDateTimeAfterAndStudyGroupIdOrderByCreationDateTimeDesc(Pageable pageable, @Param("dateFrom")LocalDateTime dateFrom, @Param("id") int studyGroupId);

    Page<Post> findAllByStudyGroupIdAndCreationDateTimeAfterAndContentIsContainingIgnoreCaseOrderByCreationDateTimeDesc(Pageable pageable, @Param("id") int studyGroupId, @Param("dateFrom")LocalDateTime dateFrom, @Param("text") String text);

    Page<Post> findAllByCreationDateTimeBeforeAndStudyGroupIdOrderByCreationDateTimeDesc(Pageable pageable, @Param("dateTo") LocalDateTime dateTo, @Param("id") int studyGroupId);

    Page<Post> findAllByStudyGroupIdAndCreationDateTimeBeforeAndContentIsContainingIgnoreCaseOrderByCreationDateTimeDesc(Pageable pageable, @Param("id") int studyGroupId, @Param("dateTo") LocalDateTime dateTo, @Param("text") String text);

    Page<Post> findAllByStudyGroupIdAndContentIsContainingIgnoreCaseOrderByCreationDateTimeDesc(Pageable pageable, @Param("id") int studyGroupId, @Param("text") String text);

    void deleteAllByStudyGroupId(int studyGroupId);

    void deleteAllByCreatorId(int userId);
}
