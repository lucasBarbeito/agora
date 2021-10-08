package com.agora.agora.repository;

import com.agora.agora.model.Label;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface LabelRepository extends CrudRepository<Label, Integer> {
    Optional<Label> findByName(String labelName);
    List<Label> findAll();
}
