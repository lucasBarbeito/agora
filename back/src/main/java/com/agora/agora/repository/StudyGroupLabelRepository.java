package com.agora.agora.repository;

import com.agora.agora.model.Post;
import com.agora.agora.model.StudyGroupLabel;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface StudyGroupLabelRepository extends CrudRepository<StudyGroupLabel, Integer> {
    List<StudyGroupLabel> findByStudyGroupId(int studyGroupId);
    Optional<StudyGroupLabel> findByStudyGroupIdAndAndLabelId(int studyGroupId, int labelId);
}
