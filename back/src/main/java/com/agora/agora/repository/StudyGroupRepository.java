package com.agora.agora.repository;

import com.agora.agora.model.StudyGroup;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface StudyGroupRepository extends CrudRepository<StudyGroup, Integer> {
    Optional<StudyGroup> findByName(String name);
    List<StudyGroup> findAll();
}
