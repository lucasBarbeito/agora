package com.agora.agora;

import com.agora.agora.model.Label;
import com.agora.agora.model.dto.LabelDTO;
import com.agora.agora.model.form.LabelForm;
import com.agora.agora.model.form.StudyGroupForm;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("test")
@JsonTest
@WebAppConfiguration
public class LabelCreationTest {

    @Autowired
    JacksonTester<LabelDTO> json;
    @Autowired
    JacksonTester<LabelForm> jsonForm;


    @Test
    public void testLabelDTOSerialization() throws Exception {
        LabelDTO label = new LabelDTO(1, "SciFi");

        String expectedJson = "{\"id\":1,\"name\":\"SciFi\"}";

        assertEquals(expectedJson,json.write(label).getJson());
    }

    @Test
    public void testLabelDTODeserialization() throws Exception {
        LabelDTO label = new LabelDTO(1, "SciFi");

        String expectedJson = "{\"id\":1,\"name\":\"SciFi\"}";

        LabelDTO labelObtained = json.parse(expectedJson).getObject();

        assertEquals(label.getId(), labelObtained.getId());
        assertEquals(label.getName(), labelObtained.getName());
    }

    @Test
    public void testLabelDTOSetSerialization() throws Exception {
        LabelDTO label = new LabelDTO();
        label.setId(1);
        label.setName("SciFi");

        String expectedJson = "{\"id\":1,\"name\":\"SciFi\"}";

        assertEquals(expectedJson,json.write(label).getJson());
    }

    @Test
    public void testLabelFormSerialization() throws Exception {
        LabelForm label = new LabelForm("SciFi");

        String expectedJson = "{\"name\":\"SciFi\"}";

        assertEquals(expectedJson,jsonForm.write(label).getJson());
    }

    @Test
    public void testLabelFormDeserialization() throws Exception {
        LabelForm label = new LabelForm("SciFi");

        String expectedJson = "{\"name\":\"SciFi\"}";

        LabelForm labelObtained = jsonForm.parse(expectedJson).getObject();

        assertEquals(label.getName(), labelObtained.getName());
    }

    @Test
    public void testLabelFormSetSerialization() throws Exception {
        LabelForm label = new LabelForm();
        label.setName("SciFi");
        String expectedJson = "{\"name\":\"SciFi\"}";

        assertEquals(expectedJson,jsonForm.write(label).getJson());
    }

}
