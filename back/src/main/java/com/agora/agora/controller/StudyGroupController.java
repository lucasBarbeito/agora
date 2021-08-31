package com.agora.agora.controller;

import com.agora.agora.model.form.StudyGroupForm;
import com.agora.agora.service.StudyGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

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


}
