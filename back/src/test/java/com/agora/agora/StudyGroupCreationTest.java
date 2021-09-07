package com.agora.agora;

import com.agora.agora.model.StudyGroup;
import com.agora.agora.model.User;
import com.agora.agora.model.form.StudyGroupForm;
import com.agora.agora.model.type.UserType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.time.LocalDate;
import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("test")
@JsonTest
@WebAppConfiguration
public class StudyGroupCreationTest {

    @Autowired
    JacksonTester<StudyGroupForm> json;
    @Autowired
    JacksonTester<StudyGroup> jsonU;

    @Test
    public void testFormSerialization() throws Exception {
        StudyGroupForm form = new StudyGroupForm("Lord of the rings", "...", 1,LocalDate.of(2021, 8, 17));

        String expectedJson = "{\"name\":\"Lord of the rings\",\"description\":\"...\",\"creatorId\":1,\"creationDate\":\"2021-08-17\"}";

        assertEquals(expectedJson,json.write(form).getJson());
    }

    @Test
    public void testFormDeserialization() throws Exception{
        String expectedJson = "{\"name\":\"Lord of the rings\",\"description\":\"...\",\"creatorId\":1,\"creationDate\":\"2021-08-17\"}";
        StudyGroupForm form = new StudyGroupForm("Lord of the rings", "...", 1,LocalDate.of(2021, 8, 17));


        StudyGroupForm userFormObtained = json.parse(expectedJson).getObject();

        assertEquals(form.getName(),userFormObtained.getName());
        assertEquals(form.getDescription(),userFormObtained.getDescription());
        assertEquals(form.getCreatorId(),userFormObtained.getCreatorId());
        assertEquals(form.getCreationDate(),userFormObtained.getCreationDate());
    }

    @Test
    public void testStudyGroupFormSetInSerialization() throws Exception{
        StudyGroupForm form = new StudyGroupForm("yes", "yes", 12,LocalDate.of(1989, 4, 15));
        form.setName("Lord of the rings");
        form.setDescription("...");
        form.setCreatorId(1);
        form.setCreationDate(LocalDate.of(2021, 8, 17));

        String expectedJson = "{\"name\":\"Lord of the rings\",\"description\":\"...\",\"creatorId\":1,\"creationDate\":\"2021-08-17\"}";

        assertEquals(expectedJson,json.write(form).getJson());
    }

    @Test
    public void testStudyGroupSerialization() throws Exception {
        User user = new User("Agustin","Von","a@gmail.com","Agustin123",true, UserType.USER);
        StudyGroup studyGroup = new StudyGroup("Lord of the rings", "...", user,LocalDate.of(2021, 8, 17));
        String expectedJson = "{\"id\":0,\"name\":\"Lord of the rings\",\"description\":\"...\",\"creator\":{\"id\":0,\"name\":\"Agustin\",\"surname\":\"Von\",\"email\":\"a@gmail.com\",\"password\":\"Agustin123\",\"userVerificationToken\":null,\"type\":\"USER\",\"verified\":true},\"creationDate\":\"2021-08-17\"}";


        assertEquals(expectedJson,jsonU.write(studyGroup).getJson());
    }

    @Test
    public void testStudyGroupDeserialization() throws Exception{
        User user = new User("Agustin","Von","a@gmail.com","Agustin123",true, UserType.USER);
        StudyGroup studyGroup = new StudyGroup("Lord of the rings", "...", user,LocalDate.of(2021, 8, 17));
        String expectedJson = "{\"id\":0,\"name\":\"Lord of the rings\",\"description\":\"...\",\"creator\":{\"id\":0,\"name\":\"Agustin\",\"surname\":\"Von\",\"email\":\"a@gmail.com\",\"password\":\"Agustin123\",\"userVerificationToken\":null,\"type\":\"USER\",\"verified\":true},\"creationDate\":\"2021-08-17\"}";


        StudyGroup studyGroupObtained = jsonU.parse(expectedJson).getObject();

        assertEquals(studyGroup.getId(),studyGroupObtained.getId());
        assertEquals(studyGroup.getName(),studyGroupObtained.getName());
        assertEquals(studyGroup.getCreationDate(),studyGroupObtained.getCreationDate());

        assertEquals(user.getName(),studyGroupObtained.getCreator().getName());
        assertEquals(user.getSurname(),studyGroupObtained.getCreator().getSurname());
        assertEquals(user.getPassword(),studyGroupObtained.getCreator().getPassword());
        assertEquals(user.getEmail(),studyGroupObtained.getCreator().getEmail());
        assertEquals(user.getId(),studyGroupObtained.getCreator().getId());

    }

    @Test
    public void testUserSetInSerialization() throws Exception{
        User user = new User("Carlos","Mendez","Carlos@gmail.com","Carlos123",false, UserType.USER);
        user.setName("Agustin");
        user.setSurname("Von");
        user.setPassword("Agustin123");
        user.setEmail("a@gmail.com");
        user.setVerified(true);
        StudyGroup studyGroup = new StudyGroup("Lord of the rings", "...", user,LocalDate.of(2021, 8, 17));
        String expectedJson = "{\"id\":0,\"name\":\"Lord of the rings\",\"description\":\"...\",\"creator\":{\"id\":0,\"name\":\"Agustin\",\"surname\":\"Von\",\"email\":\"a@gmail.com\",\"password\":\"Agustin123\",\"userVerificationToken\":null,\"type\":\"USER\",\"verified\":true},\"creationDate\":\"2021-08-17\"}";

       assertEquals(expectedJson,jsonU.write(studyGroup).getJson());
    }
}
