package com.agora.agora;

import com.agora.agora.model.dto.PostDTO;
import com.agora.agora.model.form.PostForm;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.io.IOException;
import java.time.LocalDateTime;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("test")
@JsonTest
@WebAppConfiguration
public class PostCreationTest {

    @Autowired
    JacksonTester<PostForm> json;
    @Autowired
    JacksonTester<PostDTO> jsonDTO;

    @Test
    public void testPostFormSerialization() throws Exception {
        PostForm form = new PostForm("content", LocalDateTime.of(1989,4,15,10,10, 10, 10));
        String expectedJson = "{\"content\":\"content\",\"creationDateAndTime\":\"1989-04-15T10:10:10.00000001\"}";
        assertEquals(expectedJson,json.write(form).getJson());
    }

    @Test
    public void testPostFormDeserialization() throws Exception {
        String expectedJson = "{\"content\":\"content\",\"creationDateAndTime\":\"1989-04-15T10:10:10.00000001\"}";
        PostForm form = new PostForm("content", LocalDateTime.of(1989,4,15,10,10, 10, 10));

        PostForm expectedPostForm = json.parse(expectedJson).getObject();

        assertEquals(form.getContent(),expectedPostForm.getContent());
        assertEquals(form.getCreationDateAndTime(),expectedPostForm.getCreationDateAndTime());
    }

    @Test
    public void testPostFormSetInSerialization() throws Exception {
        PostForm form = new PostForm("no content", LocalDateTime.of(1988,3,14,10,10, 10, 10));
        form.setContent("content");
        form.setCreationDateAndTime(LocalDateTime.of(1989,4,15,10,10, 10, 10));
        String expectedJson = "{\"content\":\"content\",\"creationDateAndTime\":\"1989-04-15T10:10:10.00000001\"}";

        assertEquals(expectedJson,json.write(form).getJson());
    }

    @Test
    public void testPostDTOSerialization() throws IOException {
        PostDTO dto = new PostDTO(0, "content", 1, 1,  LocalDateTime.of(1988,3,14,10,10, 10, 10));
        String expectedJson = "{\"id\":0,\"content\":\"content\",\"studyGroupId\":1,\"creatorId\":1,\"creationDateAndTime\":\"1988-03-14T10:10:10.00000001\"}";
        assertEquals(expectedJson,jsonDTO.write(dto).getJson());
    }

    @Test
    public void testPostDTODesrialization() throws IOException {
        PostDTO dto = new PostDTO(0, "content", 1, 1,  LocalDateTime.of(1988,3,14,10,10, 10, 10));
        String expectedJson = "{\"id\":0,\"content\":\"content\",\"studyGroupId\":1,\"creatorId\":1,\"creationDateAndTime\":\"1988-03-14T10:10:10.00000001\"}";

        PostDTO expected = jsonDTO.parse(expectedJson).getObject();

        assertEquals(dto.getId(), expected.getId());
        assertEquals(dto.getContent(),expected.getContent());
        assertEquals(dto.getStudyGroupId(), expected.getStudyGroupId());
        assertEquals(dto.getCreatorId(), expected.getCreatorId());
        assertEquals(dto.getCreationDateAndTime(),expected.getCreationDateAndTime());
    }

    @Test
    public void testPostDTOSetSerialization() throws IOException {
        PostDTO dto = new PostDTO();
        dto.setId(0);
        dto.setContent("content");
        dto.setStudyGroupId(1);
        dto.setCreatorId(1);
        dto.setCreationDateAndTime(LocalDateTime.of(1988,3,14,10,10, 10, 10));
        String expectedJson = "{\"id\":0,\"content\":\"content\",\"studyGroupId\":1,\"creatorId\":1,\"creationDateAndTime\":\"1988-03-14T10:10:10.00000001\"}";
        assertEquals(expectedJson,jsonDTO.write(dto).getJson());
    }
}
