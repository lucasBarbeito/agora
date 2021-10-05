package com.agora.agora;

import com.agora.agora.model.StudyGroup;
import com.agora.agora.model.StudyGroupUser;
import com.agora.agora.model.User;
import com.agora.agora.model.dto.FullStudyGroupDTO;
import com.agora.agora.model.dto.StudyGroupDTO;
import com.agora.agora.model.dto.UserContactDTO;
import com.agora.agora.model.form.EditStudyGroupForm;
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

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("test")
@JsonTest
@WebAppConfiguration
public class StudyGroupCreationTest {

    @Autowired
    JacksonTester<StudyGroupForm> json;
    @Autowired
    JacksonTester<StudyGroupDTO> jsonU;
    @Autowired
    JacksonTester<FullStudyGroupDTO> jsonFullDTO;
    @Autowired
    JacksonTester<EditStudyGroupForm> jsonEditForm;

    @Test
    public void testStudyGroupFormSerialization() throws Exception {
        StudyGroupForm form = new StudyGroupForm("Lord of the rings", "...", 1,LocalDate.of(2021, 8, 17));

        String expectedJson = "{\"name\":\"Lord of the rings\",\"description\":\"...\",\"creatorId\":1,\"creationDate\":\"2021-08-17\"}";

        assertEquals(expectedJson,json.write(form).getJson());
    }

    @Test
    public void testStudyGroupFormDeserialization() throws Exception{
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
    public void testStudyGroupDTOSerialization() throws Exception {
        StudyGroupDTO studyGroup = new StudyGroupDTO(0,"Lord of the rings", "...", 1 ,LocalDate.of(2021, 8, 17));
        String expectedJson = "{\"id\":0,\"name\":\"Lord of the rings\",\"description\":\"...\",\"creatorId\":1,\"creationDate\":\"2021-08-17\",\"currentUserIsMember\":false}";
        assertEquals(expectedJson,jsonU.write(studyGroup).getJson());
    }

    @Test
    public void testStudyGroupDTODeserialization() throws Exception{
        StudyGroupDTO studyGroup = new StudyGroupDTO(0,"Lord of the rings", "...", 1 ,LocalDate.of(2021, 8, 17));
        String expectedJson = "{\"id\":0,\"name\":\"Lord of the rings\",\"description\":\"...\",\"creatorId\":1,\"creationDate\":\"2021-08-17\"}";
        StudyGroupDTO studyGroupObtained = jsonU.parse(expectedJson).getObject();
        assertEquals(studyGroup.getId(),studyGroupObtained.getId());
        assertEquals(studyGroup.getName(),studyGroupObtained.getName());
        assertEquals(studyGroup.getCreationDate(),studyGroupObtained.getCreationDate());
        assertEquals(studyGroup.getCreatorId(),studyGroupObtained.getCreatorId());
        assertEquals(studyGroup.getDescription(),studyGroupObtained.getDescription());
        assertEquals(studyGroup.isCurrentUserIsMember(), studyGroupObtained.isCurrentUserIsMember());
    }

    @Test
    public void testStudyGroupDTOSetInSerialization() throws Exception{
        StudyGroupDTO studyGroup = new StudyGroupDTO(1,"Lord of the rings haters", "We have 0 taste", 2,LocalDate.of(1989, 4, 15));
        studyGroup.setId(0);
        studyGroup.setName("Lord of the rings");
        studyGroup.setDescription("...");
        studyGroup.setCreatorId(1);
        studyGroup.setCreationDate(LocalDate.of(2021, 8, 17));
        studyGroup.setCurrentUserIsMember(false);
        String expectedJson = "{\"id\":0,\"name\":\"Lord of the rings\",\"description\":\"...\",\"creatorId\":1,\"creationDate\":\"2021-08-17\",\"currentUserIsMember\":false}";
        assertEquals(expectedJson,jsonU.write(studyGroup).getJson());
    }

    @Test
    public void testFullStudyGroupDTOSerialization() throws IOException {
        User user = new User("Agustin","Von","a@gmail.com","Agustin123",true, UserType.USER);
        StudyGroup studyGroup = new StudyGroup("Lord of the rings", "...", user,LocalDate.of(2021, 8, 17));
        User user2 = new User("J. R. R.", "Tolkien", "tolkien@gmail.com", "Jrrtolkien2021", false, UserType.USER);
        studyGroup.getUsers().add(new StudyGroupUser(user2, studyGroup));

        UserContactDTO userDTO = new UserContactDTO(user2.getId(), user2.getName(), user2.getEmail());
        List<UserContactDTO> users = new ArrayList<>();
        users.add(userDTO);

        FullStudyGroupDTO dto = new FullStudyGroupDTO(studyGroup.getId(), studyGroup.getName(), studyGroup.getDescription(), studyGroup.getCreator().getId(), studyGroup.getCreationDate(), users);

        String expectedJson = "{\"id\":0,\"name\":\"Lord of the rings\",\"description\":\"...\",\"creatorId\":0,\"creationDate\":\"2021-08-17\",\"currentUserIsMember\":false,\"userContacts\":[{\"id\":0,\"name\":\"J. R. R.\",\"email\":\"tolkien@gmail.com\"}]}";

        assertEquals(expectedJson,jsonFullDTO.write(dto).getJson());
    }

    @Test
    public void testFullStudyGroupDTODeserialization() throws IOException {
        User user = new User("Agustin","Von","a@gmail.com","Agustin123",true, UserType.USER);
        StudyGroup studyGroup = new StudyGroup("Lord of the rings", "...", user,LocalDate.of(2021, 8, 17));
        User user2 = new User("J. R. R.", "Tolkien", "tolkien@gmail.com", "Jrrtolkien2021", false, UserType.USER);
        studyGroup.getUsers().add(new StudyGroupUser(user2, studyGroup));

        UserContactDTO userDTO = new UserContactDTO(user2.getId(), user2.getName(), user2.getEmail());
        List<UserContactDTO> users = new ArrayList<>();
        users.add(userDTO);

        FullStudyGroupDTO dto = new FullStudyGroupDTO(studyGroup.getId(), studyGroup.getName(), studyGroup.getDescription(), studyGroup.getCreator().getId(), studyGroup.getCreationDate(), users);

        String expectedJson = "{\"id\":0,\"name\":\"Lord of the rings\",\"description\":\"...\",\"creatorId\":0,\"creationDate\":\"2021-08-17\",\"userContacts\":[{\"id\":0,\"name\":\"J. R. R.\",\"email\":\"tolkien@gmail.com\"}]}";

        FullStudyGroupDTO obtained = jsonFullDTO.parse(expectedJson).getObject();

        assertEquals(dto.getId(), obtained.getId());
        assertEquals(dto.getName(), obtained.getName());

        assertEquals(dto.getUserContacts().get(0).getId(), obtained.getUserContacts().get(0).getId());
    }

    @Test
    public void testFullStudyGroupDTOSetSerialization() throws IOException {
        User user = new User("Agustin","Von","a@gmail.com","Agustin123",true, UserType.USER);
        StudyGroup studyGroup = new StudyGroup("Lord of the rings", "...", user,LocalDate.of(2021, 8, 17));
        User user2 = new User("J. R. R.", "Tolkien", "tolkien@gmail.com", "Jrrtolkien2021", false, UserType.USER);
        studyGroup.getUsers().add(new StudyGroupUser(user2, studyGroup));

        UserContactDTO userDTO = new UserContactDTO(user2.getId(), user2.getName(), user2.getEmail());
        List<UserContactDTO> users = new ArrayList<>();
        users.add(userDTO);

        FullStudyGroupDTO dto = new FullStudyGroupDTO();
        dto.setCreationDate(studyGroup.getCreationDate());
        dto.setCreatorId(user.getId());
        dto.setCreatorId(studyGroup.getCreator().getId());
        dto.setDescription(studyGroup.getDescription());
        dto.setId(0);
        dto.setUserContacts(users);
        dto.setName(studyGroup.getName());

        String expectedJson = "{\"id\":0,\"name\":\"Lord of the rings\",\"description\":\"...\",\"creatorId\":0,\"creationDate\":\"2021-08-17\",\"currentUserIsMember\":false,\"userContacts\":[{\"id\":0,\"name\":\"J. R. R.\",\"email\":\"tolkien@gmail.com\"}]}";

        assertEquals(expectedJson,jsonFullDTO.write(dto).getJson());
    }

    @Test
    public void testEditStudyGroupFormSerialization() throws IOException {
        EditStudyGroupForm form = new EditStudyGroupForm("Lord of the rings", "...");

        String expectedJson = "{\"name\":\"Lord of the rings\",\"description\":\"...\"}";

        assertEquals(expectedJson, jsonEditForm.write(form).getJson());
    }

    @Test
    public void testEditStudyGroupFormDeserialization() throws Exception {
        EditStudyGroupForm form = new EditStudyGroupForm("Lord of the rings", "...");

        String expectedJson = "{\"name\":\"Lord of the rings\",\"description\":\"...\"}";

        EditStudyGroupForm userFormObtained = jsonEditForm.parse(expectedJson).getObject();

        assertEquals(form.getName(),userFormObtained.getName());
        assertEquals(form.getDescription(),userFormObtained.getDescription());
    }

    @Test
    public void testEditStudyGroupFormSetSerialization() throws IOException {
        EditStudyGroupForm form = new EditStudyGroupForm();
        form.setName("Lord of the rings");
        form.setDescription("...");

        String expectedJson = "{\"name\":\"Lord of the rings\",\"description\":\"...\"}";

        assertEquals(expectedJson, jsonEditForm.write(form).getJson());
    }


}
