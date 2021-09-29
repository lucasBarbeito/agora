package com.agora.agora;

import com.agora.agora.model.form.PostForm;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.time.LocalDateTime;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("test")
@JsonTest
@WebAppConfiguration
public class PostCreationTest {

    @Autowired
    JacksonTester<PostForm> json;

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

}
