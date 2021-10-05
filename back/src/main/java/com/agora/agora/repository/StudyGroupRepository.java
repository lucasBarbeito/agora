package com.agora.agora.repository;

import com.agora.agora.model.StudyGroup;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;
import java.util.Optional;

public interface StudyGroupRepository extends PagingAndSortingRepository<StudyGroup, Integer> {
    Optional<StudyGroup> findByName(String name);
    Page<StudyGroup> findAll(Sort sort);
}
