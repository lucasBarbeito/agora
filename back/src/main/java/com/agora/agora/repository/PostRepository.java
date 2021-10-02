package com.agora.agora.repository;

import com.agora.agora.model.Post;
import com.agora.agora.model.StudyGroup;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PostRepository extends CrudRepository<Post, Integer> {
    @Query(value = "select p from Post p " +
            "where p.studyGroup.id = (:id)" +
            "ORDER BY p.creationDateAndTime ASC")
    List<Post> findAllByStudyGroupId(@Param("id")int id);
}
