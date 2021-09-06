package com.agora.agora.controller;

import com.agora.agora.model.StudyGroup;
import com.agora.agora.model.dto.StudyGroupDTO;
import com.agora.agora.model.form.StudyGroupForm;
import com.agora.agora.service.StudyGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequestMapping("/studyGroup")
public class StudyGroupController {

    private StudyGroupService groupService;

    @Autowired
    public StudyGroupController(StudyGroupService groupService) {
        this.groupService = groupService;
    }

    @PostMapping
    public ResponseEntity createStudyGroup(@Valid @RequestBody StudyGroupForm studyGroup){
        int id = groupService.create(studyGroup);
        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity getStudyGroupInfoById(@PathVariable("id") int id){
        final Optional<StudyGroup> studyGroupOptional = groupService.findStudyGroupById(id);
        final Optional<StudyGroupDTO> studyGroupDTOOptional = studyGroupOptional.map((group) -> new StudyGroupDTO(group.getId(), group.getName(), group.getDescription(), group.getCreator().getId(), group.getCreationDate()));
        return studyGroupDTOOptional.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

}
