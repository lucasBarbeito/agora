package com.agora.agora.controller;

import com.agora.agora.model.form.LabelForm;
import com.agora.agora.service.LabelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/label")
public class LabelController {

    private LabelService labelService;

    @Autowired
    public LabelController(LabelService labelService) {
        this.labelService = labelService;
    }

    @PostMapping
    public ResponseEntity createNewLabel(@Valid @RequestBody LabelForm labelForm){
        int id = labelService.createNewLabel(labelForm);
        return ResponseEntity.ok().build();
    }
}
