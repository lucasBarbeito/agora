package com.agora.agora.service;

import com.agora.agora.model.Label;
import com.agora.agora.model.form.LabelForm;
import com.agora.agora.repository.LabelRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service

public class LabelService {
    private LabelRepository labelRepository;

    public LabelService(LabelRepository labelRepository) {
        this.labelRepository = labelRepository;
    }

    public LabelService() {
    }

    public int createNewLabel(LabelForm form){
        Optional<Label> labelOptional = labelRepository.findByName(form.getName());
        if(labelOptional.isPresent()){
            throw new DataIntegrityViolationException("Label already exists.");
        }
        else{
            Label label = new Label(form.getName());
            labelRepository.save(label);
            return label.getId();
        }
    }
}
