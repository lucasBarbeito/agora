package com.agora.agora;

import com.agora.agora.model.StudyGroup;
import com.agora.agora.model.User;
import com.agora.agora.model.dto.StudyGroupDTO;
import com.agora.agora.model.form.StudyGroupForm;
import com.agora.agora.repository.StudyGroupRepository;
import com.agora.agora.repository.UserRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.Assert.assertEquals;

public class StudyGroupControllerTest extends AbstractTest{

    @Autowired
    private StudyGroupRepository groupRepository;
    @Autowired
    private UserRepository userRepository;

    private Data data = new Data();

    // Storing all the info we'll load into the DB here.
    private class Data {
        User user1;
        User user2;
        StudyGroup group1;
        StudyGroup group2;

        void setup() {
            user1 = new User("J. R. R.", "Tolkien", "tolkien@gmail.com", "Jrrtolkien2021", false);
            user2 = new User("Frank", "Herbert", "herbert@gmail.com", "Frankherbert2021", false);
            userRepository.save(user1);
            userRepository.save(user2);

            group1 = new StudyGroup("Lord of the rings", "...", user1, LocalDate.of(2021, 8, 17));
            group2 = new StudyGroup("Dune", "....", user2, LocalDate.of(2021, 8, 16));
            groupRepository.save(group1);
            groupRepository.save(group2);
        }

        void rollback() {
            groupRepository.deleteAll();
            userRepository.deleteAll();
        }
    }

    // Executed before each test
    @Before
    public void setUpDB() {
        data.setup();
    }

    // Executed after each test
    @After
    public void rollBackDB() {
        data.rollback();
    }

    @Test
    public void createNewStudyGroupShouldReturnCreated() throws Exception {
        String uri = "/studyGroup";
        StudyGroupForm groupForm = new StudyGroupForm("Testgroup", "....", data.user1.getId(), LocalDate.of(2021, 8, 17));
        MvcResult mvcResult = mvc.perform(
                MockMvcRequestBuilders.post(uri)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(mapToJson(groupForm))
        ).andReturn();
        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
    }

    @Test
    public void createStudyGroupAlreadyExistingShouldReturnError() throws Exception {
        String uri = "/studyGroup";
        StudyGroupForm groupForm = new StudyGroupForm("Dune", "....", data.user1.getId(), LocalDate.of(2021, 8, 17));
        MvcResult mvcResult = mvc.perform(
                MockMvcRequestBuilders.post(uri)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(mapToJson(groupForm))
        ).andReturn();
        int status = mvcResult.getResponse().getStatus();
        assertEquals(409, status);
    }

    @Test
    public void findStudyGroupAndReturnItsInfo() throws Exception {
        String uri = "/studyGroup";

        Optional<StudyGroup> studyGroupOptional = groupRepository.findByName("Dune");

        StudyGroup studyGroup = studyGroupOptional.get();
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri + "/" + studyGroup.getId())
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();

        int statusCode = mvcResult.getResponse().getStatus();
        assertEquals(200, statusCode);

        String status = mvcResult.getResponse().getContentAsString();
        StudyGroupDTO gottenStudyGroup = super.mapFromJson(status, StudyGroupDTO.class);

        assertEquals(gottenStudyGroup.getId(), studyGroup.getId());
        assertEquals(gottenStudyGroup.getCreationDate(), studyGroup.getCreationDate());
        assertEquals(gottenStudyGroup.getDescription(), studyGroup.getDescription());
        assertEquals(gottenStudyGroup.getCreatorId(), studyGroup.getCreator().getId());
    }

    @Test
    public void findStudyGroupWithWrongIdShouldThrowError() throws Exception {
        String uri = "/studyGroup";

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri + "/" + -1)
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();

        int statusCode = mvcResult.getResponse().getStatus();
        assertEquals(404, statusCode);

    }
}
