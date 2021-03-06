package com.agora.agora;

import com.agora.agora.model.*;
import com.agora.agora.model.dto.*;
import com.agora.agora.model.form.EditStudyGroupForm;
import com.agora.agora.model.form.PostForm;
import com.agora.agora.model.form.StudyGroupForm;
import com.agora.agora.model.type.UserType;
import com.agora.agora.repository.*;
import com.fasterxml.jackson.core.type.TypeReference;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static org.hamcrest.CoreMatchers.hasItems;
import static org.junit.Assert.*;

public class StudyGroupControllerTest extends AbstractTest{

    @Autowired
    private StudyGroupRepository groupRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private LabelRepository labelRepository;

    @Autowired
    private StudyGroupUsersRepository studyGroupUsersRepository;
    @Autowired
    private StudyGroupLabelRepository studyGroupLabelRepository;

    @Autowired
    private NewPostNotificationRepository newPostNotificationRepository;
    @Autowired
    private NewMemberNotificationRepository newMemberNotificationRepository;

    @Autowired
    private PostRepository postRepository;

    private Data data = new Data();

    // Storing all the info we'll load into the DB here.
    private class Data {
        User user1;
        User user2;
        User user3;
        User user4;
        StudyGroup group1;
        StudyGroup group2;
        StudyGroup group3;
        StudyGroup group4;
        StudyGroupUser group1User1;
        StudyGroupUser group2User3;
        StudyGroupUser group2User2;
        Post post;
        Post post1;
        Post post2;
        Post post3;
        Label label1;
        Label label2;
        Label label3;

        void setup() {
            user1 = new User("J. R. R.", "Tolkien", "tolkien@gmail.com", "Jrrtolkien2021", false, UserType.USER);
            user2 = new User("Frank", "Herbert", "herbert@gmail.com", "Frankherbert2021", false, UserType.USER);
            user3 = new User("Carlos", "Gimenez", "carlos@mail.com", "Carlos123", false, UserType.USER);
            user4 = new User("Juan", "Perez", "juan@mail.com", "Juan123", false, UserType.USER);
            userRepository.save(user1);
            userRepository.save(user2);
            userRepository.save(user3);
            userRepository.save(user4);

            label1 = new Label("SciFi");
            label2 = new Label("History");
            label3 = new Label("Not History");
            labelRepository.save(label1);
            labelRepository.save(label2);
            labelRepository.save(label3);


            group1 = new StudyGroup("Lord of the rings", "...", user1, LocalDate.of(2021, 8, 17));
            group2 = new StudyGroup("Dune", "....", user2, LocalDate.of(2021, 8, 16));
            group3 = new StudyGroup("Books of SciFi not History", "Description", user1, LocalDate.of(2021, 8, 16));
            group4 = new StudyGroup("Books Books", "Description", user1, LocalDate.of(2021, 8, 16));
            groupRepository.save(group1);
            groupRepository.save(group2);
            groupRepository.save(group3);
            groupRepository.save(group4);

            StudyGroupLabel l1g1 = new StudyGroupLabel(label1, group1);
            StudyGroupLabel l2g2 = new StudyGroupLabel(label2, group2);
            StudyGroupLabel l2g3 = new StudyGroupLabel(label2, group3);
            StudyGroupLabel l3g3 = new StudyGroupLabel(label3, group3);
            studyGroupLabelRepository.save(l1g1);
            studyGroupLabelRepository.save(l2g2);
            studyGroupLabelRepository.save(l3g3);
            studyGroupLabelRepository.save(l2g3);

            group1User1 = new StudyGroupUser(user1, group1);
            group2User2 = new StudyGroupUser(user2, group2);
            group2User3 = new StudyGroupUser(user3, group2);
            studyGroupUsersRepository.save(group1User1);
            studyGroupUsersRepository.save(group2User2);
            studyGroupUsersRepository.save(group2User3);

            post = new Post("...", group1, user1, LocalDateTime.of(2021, 9, 23, 10, 15));
            postRepository.save(post);

            post1 = new Post("LOTR 2 is out", group1, user1, LocalDateTime.of(2021, 9, 23, 3, 15));
            post2 = new Post("LOTR 2 is great", group1, user1, LocalDateTime.of(2021, 9, 24, 12, 3));
            post3 = new Post("Aguante Dune!", group2, user1, LocalDateTime.of(2021, 9, 24, 12, 3));
            postRepository.save(post1);
            postRepository.save(post2);
            postRepository.save(post3);
        }

        void rollback() {
            newPostNotificationRepository.deleteAll();
            newMemberNotificationRepository.deleteAll();
            postRepository.deleteAll();
            studyGroupLabelRepository.deleteAll();
            studyGroupUsersRepository.deleteAll();
            groupRepository.deleteAll();
            labelRepository.deleteAll();
            userRepository.deleteAll();
        }
    }

    private List<StudyGroupDTO> pageToList(String studyGroupPageJson) throws IOException {
        Page<StudyGroupDTO> gottenStudyGroupPage = super.mapFromJson(studyGroupPageJson, new TypeReference<CustomPageImpl<StudyGroupDTO>>(){});
        return gottenStudyGroupPage.getContent();
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
    @WithMockUser("USER")
    public void createNewStudyGroupShouldReturnCreated() throws Exception {
        String uri = "/studyGroup";
        List<LabelIdDTO> label = new ArrayList<>();
        label.add(new LabelIdDTO(data.label1.getId()));
        StudyGroupForm groupForm = new StudyGroupForm("Testgroup", "....", data.user1.getId(), LocalDate.of(2021, 8, 17), label);
        MvcResult mvcResult = mvc.perform(
                MockMvcRequestBuilders.post(uri)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(mapToJson(groupForm))
        ).andReturn();
        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
    }

    @Test
    @WithMockUser("USER")
    public void createStudyGroupAlreadyExistingShouldReturnError() throws Exception {
        String uri = "/studyGroup";
        List<LabelIdDTO> label = new ArrayList<>();
        label.add(new LabelIdDTO(data.label1.getId()));
        StudyGroupForm groupForm = new StudyGroupForm("Dune", "....", data.user1.getId(), LocalDate.of(2021, 8, 17), label);
        MvcResult mvcResult = mvc.perform(
                MockMvcRequestBuilders.post(uri)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(mapToJson(groupForm))
        ).andReturn();
        int status = mvcResult.getResponse().getStatus();
        assertEquals(409, status);
    }

    @Test
    @WithMockUser("USER")
    public void createStudyGroupWithNoLabelShouldReturnBadRequest() throws Exception {
        String uri = "/studyGroup";
        List<LabelIdDTO> label = new ArrayList<>();

        StudyGroupForm groupForm = new StudyGroupForm("Testgroup", "....", data.user1.getId(), LocalDate.of(2021, 8, 17), label);
        MvcResult mvcResult = mvc.perform(
                MockMvcRequestBuilders.post(uri)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(mapToJson(groupForm))
        ).andReturn();
        int status = mvcResult.getResponse().getStatus();
        assertEquals(400, status);
    }

    @Test
    @WithMockUser("USER")
    public void createStudyGroupWithNonExistingLabelShouldReturnNotFound() throws Exception {
        String uri = "/studyGroup";
        List<LabelIdDTO> label = new ArrayList<>();
        label.add(new LabelIdDTO(-1));
        StudyGroupForm groupForm = new StudyGroupForm("Testgroup", "....", data.user1.getId(), LocalDate.of(2021, 8, 17), label);
        MvcResult mvcResult = mvc.perform(
                MockMvcRequestBuilders.post(uri)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(mapToJson(groupForm))
        ).andReturn();
        int status = mvcResult.getResponse().getStatus();
        assertEquals(404, status);
    }

    @Test
    @WithMockUser("USER")
    public void createNewStudyGroupShouldReturnCreatedAndItsUserListShouldHaveCreatorInIt() throws Exception {
        String uri = "/studyGroup";
        List<LabelIdDTO> label = new ArrayList<>();
        label.add(new LabelIdDTO(data.label1.getId()));
        StudyGroupForm groupForm = new StudyGroupForm("Testgroup", "....", data.user1.getId(), LocalDate.of(2021, 8, 17), label);
        MvcResult mvcResult = mvc.perform(
                MockMvcRequestBuilders.post(uri)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(mapToJson(groupForm))
        ).andReturn();
        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);

        Optional<StudyGroup> studyGroup = groupRepository.findByName("Testgroup");

        MvcResult mvcGETResult = mvc.perform(MockMvcRequestBuilders.get(uri + "/" + studyGroup.get().getId())
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();

        int statusGetCode = mvcGETResult.getResponse().getStatus();
        assertEquals(200, statusGetCode);


        String getStatus = mvcGETResult.getResponse().getContentAsString();
        FullStudyGroupDTO gottenStudyGroup = super.mapFromJson(getStatus, FullStudyGroupDTO.class);

        //StudyGroup Check
        assertEquals(gottenStudyGroup.getId(), studyGroup.get().getId());
        assertEquals(gottenStudyGroup.getCreationDate(), studyGroup.get().getCreationDate());
        assertEquals(gottenStudyGroup.getDescription(), studyGroup.get().getDescription());

        //User in StudyGroup Check
        assertEquals(gottenStudyGroup.getUserContacts().get(0).getId(), data.user1.getId());
        assertEquals(gottenStudyGroup.getUserContacts().get(0).getEmail(), data.user1.getEmail());

        //Label in StudyGroup Check
        assertEquals(gottenStudyGroup.getLabels().get(0).getId(), data.label1.getId());
        assertEquals(gottenStudyGroup.getLabels().get(0).getName(), data.label1.getName());
    }

    @Test
    @WithMockUser("USER")
    public void findStudyGroupAndReturnItsInfo() throws Exception {
        String uri = "/studyGroup";

        Optional<StudyGroup> studyGroupOptional = groupRepository.findByName("Dune");

        StudyGroup studyGroup = studyGroupOptional.get();
        StudyGroupUser studyGroupUser = new StudyGroupUser(data.user1, studyGroup);
        studyGroupUsersRepository.save(studyGroupUser);

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri + "/" + studyGroup.getId())
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();

        int statusCode = mvcResult.getResponse().getStatus();
        assertEquals(200, statusCode);

        String status = mvcResult.getResponse().getContentAsString();
        FullStudyGroupDTO gottenStudyGroup = super.mapFromJson(status, FullStudyGroupDTO.class);

        assertEquals(gottenStudyGroup.getId(), studyGroup.getId());
        assertEquals(gottenStudyGroup.getCreationDate(), studyGroup.getCreationDate());
        assertEquals(gottenStudyGroup.getDescription(), studyGroup.getDescription());
        assertEquals(gottenStudyGroup.getCreatorId(), studyGroup.getCreator().getId());

        //Contact of members in group check.
        assertEquals(gottenStudyGroup.getUserContacts().get(1).getId(), data.user3.getId());
        assertEquals(gottenStudyGroup.getUserContacts().get(2).getEmail(), data.user1.getEmail());
        assertEquals(gottenStudyGroup.getUserContacts().get(2).getName(), data.user1.getName());
    }

    @Test
    @WithMockUser("USER")
    public void findStudyGroupWithWrongIdShouldThrowError() throws Exception {
        String uri = "/studyGroup";

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri + "/" + -1)
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();

        int statusCode = mvcResult.getResponse().getStatus();
        assertEquals(404, statusCode);

    }

    @Test
    @WithMockUser("tolkien@gmail.com")
    public void getAllStudentsShouldReturnOk() throws Exception {
        String uri = "/studyGroup/?page=0";
        MvcResult mvcResult = mvc.perform(
                MockMvcRequestBuilders.get(uri)
                        .param("label", "")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
        ).andReturn();
        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
    }

    @Test
    @WithMockUser("tolkien@gmail.com")
    public void findAllStudyGroupsReturnsReturnsAmountExpected() throws Exception {
        String uri = "/studyGroup/paged/?page=0";
        MvcResult mvcResult = mvc.perform(
                MockMvcRequestBuilders.get(uri)
                        .param("label", "")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
        ).andReturn();
        String gottenStatus = mvcResult.getResponse().getContentAsString();
        List<StudyGroupDTO> gottenStudyGroup = pageToList(gottenStatus);

        assertEquals(4,gottenStudyGroup.size());
    }

    @Test
    @WithMockUser("tolkien@gmail.com")
    public void findAllStudyGroupsHasExpectedValues() throws Exception {
        String uri = "/studyGroup/paged/?page=0";
        MvcResult mvcResult = mvc.perform(
                MockMvcRequestBuilders.get(uri)
                        .param("label", "")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
        ).andReturn();
        String gottenStatus = mvcResult.getResponse().getContentAsString();
        List<StudyGroupDTO> gottenStudyGroup = pageToList(gottenStatus);

        List<String> expectedStudyGroupsNames = new ArrayList<>();
        expectedStudyGroupsNames.add(data.group1.getName());
        expectedStudyGroupsNames.add(data.group2.getName());
        expectedStudyGroupsNames.add(data.group3.getName());
        expectedStudyGroupsNames.add(data.group4.getName());
        for (StudyGroupDTO studyGroup : gottenStudyGroup) {
            assertThat(expectedStudyGroupsNames, hasItems(studyGroup.getName()));
        }
    }

    @Test
    @WithMockUser("tolkien@gmail.com")
    public void findAllStudyGroupsReturnsCurrentUserIsMember() throws Exception {
        String uri = "/studyGroup/paged/?page=0";
        MvcResult mvcResult = mvc.perform(
                MockMvcRequestBuilders.get(uri)
                        .param("label", "")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
        ).andReturn();
        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);

        String gottenStatus = mvcResult.getResponse().getContentAsString();
        List<StudyGroupDTO> gottenStudyGroup = pageToList(gottenStatus);

        assertTrue(gottenStudyGroup.get(0).isCurrentUserIsMember());
        assertFalse(gottenStudyGroup.get(1).isCurrentUserIsMember());
    }

    @Test
    @WithMockUser("USER")
    public void addExistingUserToStudyGroupShouldReturnOk() throws Exception {
        Optional<StudyGroup> studyGroupOptional = groupRepository.findByName("Dune");
        Optional<User> userOptional = userRepository.findUserByEmail("tolkien@gmail.com");

        String uri = "/studyGroup";
        MvcResult mvcResult = mvc.perform(
                MockMvcRequestBuilders.post(uri + "/" + studyGroupOptional.get().getId() + "/" + userOptional.get().getId())
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
        ).andReturn();
        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
    }

    @Test
    @WithMockUser("USER")
    public void addNonExistentUserToStudyGroupShouldReturnNotFound() throws Exception {
        Optional<StudyGroup> studyGroupOptional = groupRepository.findByName("Dune");

        String uri = "/studyGroup";
        MvcResult mvcResult = mvc.perform(
                MockMvcRequestBuilders.post(uri + "/" + studyGroupOptional.get().getId() + "/" + -1)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
        ).andReturn();
        int status = mvcResult.getResponse().getStatus();
        assertEquals(404, status);
    }

    @Test
    @WithMockUser("USER")
    public void addExistingUserToNonExistingGroupShouldReturnNotFound() throws Exception {
        Optional<User> userOptional = userRepository.findUserByEmail("tolkien@gmail.com");

        String uri = "/studyGroup";
        MvcResult mvcResult = mvc.perform(
                MockMvcRequestBuilders.post(uri + "/" + -1 + "/" + userOptional.get().getId())
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
        ).andReturn();
        int status = mvcResult.getResponse().getStatus();
        assertEquals(404, status);
    }

    @Test
    @WithMockUser("USER")
    public void addedUserShouldAppearInStudyGroupListOfUsers() throws Exception {
        Optional<StudyGroup> studyGroupOptional = groupRepository.findByName("Dune");
        Optional<User> userOptional = userRepository.findUserByEmail("tolkien@gmail.com");

        String uri = "/studyGroup";

        StudyGroup studyGroup = studyGroupOptional.get();

        MvcResult mvcResult = mvc.perform(
                MockMvcRequestBuilders.post(uri + "/" + studyGroup.getId() + "/" + userOptional.get().getId())
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
        ).andReturn();
        int statusPostCode = mvcResult.getResponse().getStatus();
        assertEquals(200, statusPostCode);


        MvcResult mvcGETResult = mvc.perform(MockMvcRequestBuilders.get(uri + "/" + studyGroup.getId())
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();

        int statusGetCode = mvcGETResult.getResponse().getStatus();
        assertEquals(200, statusGetCode);

        User user = userOptional.get();

        String status = mvcGETResult.getResponse().getContentAsString();
        FullStudyGroupDTO gottenStudyGroup = super.mapFromJson(status, FullStudyGroupDTO.class);

        //StudyGroup Check
        assertEquals(gottenStudyGroup.getId(), studyGroup.getId());
        assertEquals(gottenStudyGroup.getCreationDate(), studyGroup.getCreationDate());
        assertEquals(gottenStudyGroup.getDescription(), studyGroup.getDescription());

        //User in StudyGroup Check
        assertEquals(gottenStudyGroup.getUserContacts().get(2).getId(), user.getId());
        assertEquals(gottenStudyGroup.getUserContacts().get(2).getEmail(), user.getEmail());
    }

    @Test
    @WithMockUser("USER")
    public void addExistentUserToStudyGroupShouldConflict() throws Exception {
        Optional<StudyGroup> studyGroupOptional = groupRepository.findByName("Dune");
        Optional<User> userOptional = userRepository.findUserByEmail("tolkien@gmail.com");

        String uri = "/studyGroup";
        MvcResult mvcResult = mvc.perform(
                MockMvcRequestBuilders.post(uri + "/" + studyGroupOptional.get().getId() + "/" + userOptional.get().getId())
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
        ).andReturn();
        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);

        MvcResult mvcDuplicateResult = mvc.perform(
                MockMvcRequestBuilders.post(uri + "/" + studyGroupOptional.get().getId() + "/" + userOptional.get().getId())
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
        ).andReturn();
        int statusDuplicate = mvcDuplicateResult.getResponse().getStatus();
        assertEquals(409, statusDuplicate);
    }

    @Test
    public void createNewStudyGroupWithoutTokenShouldReturnUnauthorized() throws Exception {
        String uri = "/studyGroup";
        List<LabelIdDTO> label = new ArrayList<>();
        label.add(new LabelIdDTO(data.label1.getId()));
        StudyGroupForm groupForm = new StudyGroupForm("Testgroup", "....", data.user1.getId(), LocalDate.of(2021, 8, 17), label);
        MvcResult mvcResult = mvc.perform(
                MockMvcRequestBuilders.post(uri)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(mapToJson(groupForm))
        ).andReturn();
        int status = mvcResult.getResponse().getStatus();
        assertEquals(401, status);
    }

    @Test
    public void findStudyGroupWithoutTokenShouldReturnUnauthorized() throws Exception {
        String uri = "/studyGroup";

        Optional<StudyGroup> studyGroupOptional = groupRepository.findByName("Dune");

        StudyGroup studyGroup = studyGroupOptional.get();
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri + "/" + studyGroup.getId())
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();

        int statusCode = mvcResult.getResponse().getStatus();
        assertEquals(401, statusCode);
    }

    @Test
    @WithMockUser("tolkien@gmail.com")
    public void findStudyGroupByExistingNameShouldReturnValues() throws Exception {
        String uri = "/studyGroup/paged/?page=0";
        MvcResult mvcResult = mvc.perform(
                MockMvcRequestBuilders.get(uri)
                        .param("text", "Dune")
                        .param("label", "")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
        ).andReturn();
        String status = mvcResult.getResponse().getContentAsString();
        List<StudyGroupDTO> allStudyGroups = pageToList(status);
        assertEquals(1,allStudyGroups.size());
    }

    @Test
    @WithMockUser("tolkien@gmail.com")
    public void findStudyGroupByExistingDescriptionShouldReturnValues() throws Exception {
        String uri = "/studyGroup/paged/?page=0";
        MvcResult mvcResult = mvc.perform(
                MockMvcRequestBuilders.get(uri)
                        .param("text", ".")
                        .param("label", "")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
        ).andReturn();
        String status = mvcResult.getResponse().getContentAsString();
        List<StudyGroupDTO> allStudyGroups = pageToList(status);
        assertEquals(2,allStudyGroups.size());
    }

    @Test
    @WithMockUser("tolkien@gmail.com")
    public void findStudyGroupByNonExistingDescriptionShouldReturnNothing() throws Exception {
        String uri = "/studyGroup/paged/?page=0";
        MvcResult mvcResult = mvc.perform(
                MockMvcRequestBuilders.get(uri)
                        .param("text", "No hay un grupo con esto")
                        .param("label", "")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
        ).andReturn();
        String status = mvcResult.getResponse().getContentAsString();
        List<StudyGroupDTO> allStudyGroups = pageToList(status);
        assertEquals(0,allStudyGroups.size());
    }

    @Test
    @WithMockUser("tolkien@gmail.com")
    public void findStudyGroupByDescriptionHasExpectedValues() throws Exception {
        String uri = "/studyGroup/paged/?page=0";
        MvcResult mvcResult = mvc.perform(
                MockMvcRequestBuilders.get(uri)
                        .param("text", ".")
                        .param("label", "")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
        ).andReturn();
        String status = mvcResult.getResponse().getContentAsString();
        List<StudyGroupDTO> allStudyGroups = pageToList(status);
        List<String> expectedStudyGroupsNames = new ArrayList<>();
        expectedStudyGroupsNames.add(data.group1.getName());
        expectedStudyGroupsNames.add(data.group2.getName());
        for (StudyGroupDTO studyGroup : allStudyGroups) {
            assertThat(expectedStudyGroupsNames, hasItems(studyGroup.getName()));
        }
    }

    @Test
    @WithMockUser("tolkien@gmail.com")
    public void findStudyGroupByNameHasExpectedValue() throws Exception {
        String uri = "/studyGroup/paged/?page=0";
        MvcResult mvcResult = mvc.perform(
                MockMvcRequestBuilders.get(uri)
                        .param("text", "Dune")
                        .param("label", "")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
        ).andReturn();
        String status = mvcResult.getResponse().getContentAsString();
        List<StudyGroupDTO> allStudyGroups = pageToList(status);
        List<String> expectedStudyGroupsNames = new ArrayList<>();
        expectedStudyGroupsNames.add(data.group2.getName());
        for (StudyGroupDTO studyGroup : allStudyGroups) {
            assertThat(expectedStudyGroupsNames, hasItems(studyGroup.getName()));
        }
    }

    @Test
    @WithMockUser("tolkien@gmail.com")
    public void findStudyGroupByPartialNameHasExpectedValue() throws Exception {
        String uri = "/studyGroup/paged/?page=0";
        MvcResult mvcResult = mvc.perform(
                MockMvcRequestBuilders.get(uri)
                        .param("text", "Du")
                        .param("label", "")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
        ).andReturn();
        String status = mvcResult.getResponse().getContentAsString();
        List<StudyGroupDTO> allStudyGroups = pageToList(status);
        List<String> expectedStudyGroupsNames = new ArrayList<>();
        expectedStudyGroupsNames.add(data.group2.getName());
        for (StudyGroupDTO studyGroup : allStudyGroups) {
            assertThat(expectedStudyGroupsNames, hasItems(studyGroup.getName()));
        }
    }

    @Test
    @WithMockUser(username = "tolkien@gmail.com")
    public void deleteExistentUserFromExistentStudyGroupShouldReturnOk() throws Exception {
        Optional<StudyGroup> studyGroupOptional = groupRepository.findByName("Dune");

        String uri = "/studyGroup";
        MvcResult mvcResult = mvc.perform(
                MockMvcRequestBuilders.post(uri + "/" + studyGroupOptional.get().getId() + "/" + data.user1.getId())
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
        ).andReturn();
        int postStatus = mvcResult.getResponse().getStatus();
        assertEquals(200, postStatus);

        MvcResult deleteResult = mvc.perform(
                MockMvcRequestBuilders.delete(uri + "/" + studyGroupOptional.get().getId() + "/me")
        ).andReturn();
        int deleteStatus = deleteResult.getResponse().getStatus();
        assertEquals(204, deleteStatus);
    }

    @Test
    @WithMockUser(username = "tolkien@gmail.com")
    public void deleteExistentUserFromExistentStudyGroupShouldNotAppearInUserList() throws Exception {
        Optional<StudyGroup> studyGroupOptional = groupRepository.findByName("Dune");

        String uri = "/studyGroup";
        MvcResult mvcResult = mvc.perform(
                MockMvcRequestBuilders.post(uri + "/" + studyGroupOptional.get().getId() + "/" + data.user1.getId())
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
        ).andReturn();
        int postStatus = mvcResult.getResponse().getStatus();
        assertEquals(200, postStatus);

        MvcResult deleteResult = mvc.perform(
                MockMvcRequestBuilders.delete(uri + "/" + studyGroupOptional.get().getId() + "/me")
        ).andReturn();
        int deleteStatus = deleteResult.getResponse().getStatus();
        assertEquals(204, deleteStatus);

        MvcResult mvcGETResult = mvc.perform(MockMvcRequestBuilders.get(uri + "/" + studyGroupOptional.get().getId())
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();

        int statusGetCode = mvcGETResult.getResponse().getStatus();
        assertEquals(200, statusGetCode);


        String status = mvcGETResult.getResponse().getContentAsString();
        FullStudyGroupDTO gottenStudyGroup = super.mapFromJson(status, FullStudyGroupDTO.class);

        assertEquals(2, gottenStudyGroup.getUserContacts().size());
    }

    @Test
    @WithMockUser(username = "t@gmail.com")
    public void deleteNonExistentUserFromExistentStudyGroupShouldReturnNotFound() throws Exception {
        Optional<StudyGroup> studyGroupOptional = groupRepository.findByName("Dune");

        String uri = "/studyGroup";
        MvcResult mvcResult = mvc.perform(
                MockMvcRequestBuilders.post(uri + "/" + studyGroupOptional.get().getId() + "/" + data.user1.getId())
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
        ).andReturn();
        int postStatus = mvcResult.getResponse().getStatus();
        assertEquals(200, postStatus);

        MvcResult deleteResult = mvc.perform(
                MockMvcRequestBuilders.delete(uri + "/" + studyGroupOptional.get().getId() + "/me")
        ).andReturn();
        int deleteStatus = deleteResult.getResponse().getStatus();
        assertEquals(404, deleteStatus);
    }

    @Test
    @WithMockUser(username = "tolkien@gmail.com")
    public void deleteExistentUserFromNonExistentStudyGroupShouldNotFound() throws Exception {
        Optional<StudyGroup> studyGroupOptional = groupRepository.findByName("Dune");

        String uri = "/studyGroup";
        MvcResult mvcResult = mvc.perform(
                MockMvcRequestBuilders.post(uri + "/" + studyGroupOptional.get().getId() + "/" + data.user1.getId())
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
        ).andReturn();
        int postStatus = mvcResult.getResponse().getStatus();
        assertEquals(200, postStatus);

        MvcResult deleteResult = mvc.perform(
                MockMvcRequestBuilders.delete(uri + "/" + -1 + "/me")
        ).andReturn();
        int deleteStatus = deleteResult.getResponse().getStatus();
        assertEquals(404, deleteStatus);
    }

    @Test
    @WithMockUser("tolkien@gmail.com")
    public void deleteNonMemberFromGroupShouldReturnNotFound() throws Exception {
        Optional<StudyGroup> studyGroupOptional = groupRepository.findByName("Dune");

        String uri = "/studyGroup";

        MvcResult deleteResult = mvc.perform(
                MockMvcRequestBuilders.delete(uri + "/" + studyGroupOptional.get().getId() + "/me")
        ).andReturn();
        int deleteStatus = deleteResult.getResponse().getStatus();
        assertEquals(404, deleteStatus);
    }

    @Test
    @WithMockUser("herbert@gmail.com")
    public void deleteGroupCreatorFromGroupShouldReturnForbidden() throws Exception {
        Optional<StudyGroup> studyGroupOptional = groupRepository.findByName("Dune");

        String uri = "/studyGroup";

        Iterable<StudyGroupUser> list = studyGroupUsersRepository.findAll();

        MvcResult deleteResult = mvc.perform(
                MockMvcRequestBuilders.delete(uri + "/" + studyGroupOptional.get().getId() + "/me")
        ).andReturn();
        int deleteStatus = deleteResult.getResponse().getStatus();
        assertEquals(403, deleteStatus);
    }

    @Test
    @WithMockUser("USER")
    public void modifyStudyGroupByIdShouldModifyIt() throws Exception {
        Optional<StudyGroup> studyGroupOptional = groupRepository.findByName("Dune");

        String uri = "/studyGroup";

        List<LabelIdDTO> labels = new ArrayList<>();
        LabelIdDTO label = new LabelIdDTO(data.label1.getId());
        labels.add(label);

        EditStudyGroupForm groupForm = new EditStudyGroupForm();
        String descriptionModify = "...2";
        String nameModify = "Dune 2";
        groupForm.setDescription(descriptionModify);
        groupForm.setName(nameModify);
        groupForm.setLabels(labels);

        MvcResult mvcResult = mvc.perform(
                MockMvcRequestBuilders.put(uri + "/" + studyGroupOptional.get().getId())
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(mapToJson(groupForm))
        ).andReturn();

        int statusPut = mvcResult.getResponse().getStatus();
        assertEquals(204, statusPut);

        MvcResult modifiedResult = mvc.perform(MockMvcRequestBuilders.get(uri + "/" + studyGroupOptional.get().getId())
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();

        int status = modifiedResult.getResponse().getStatus();
        assertEquals(200, status);

        String modifiedStatus = modifiedResult.getResponse().getContentAsString();
        FullStudyGroupDTO gottenStudyGroup = super.mapFromJson(modifiedStatus, FullStudyGroupDTO.class);

        assertEquals(descriptionModify, gottenStudyGroup.getDescription());
        assertEquals(nameModify, gottenStudyGroup.getName());
        assertEquals(data.label1.getId(), gottenStudyGroup.getLabels().get(0).getId());
    }

    @Test
    @WithMockUser("USER")
    public void modifyStudyGroupWithPreviousAndNewLabelShouldHaveBoth() throws Exception {
        Optional<StudyGroup> studyGroupOptional = groupRepository.findByName("Dune");

        String uri = "/studyGroup";

        List<LabelIdDTO> labels = new ArrayList<>();
        LabelIdDTO label = new LabelIdDTO(data.label1.getId());
        LabelIdDTO label2 = new LabelIdDTO(data.label2.getId());
        labels.add(label);
        labels.add(label2);

        EditStudyGroupForm groupForm = new EditStudyGroupForm();
        String descriptionModify = "...2";
        String nameModify = "Dune 2";
        groupForm.setDescription(descriptionModify);
        groupForm.setName(nameModify);
        groupForm.setLabels(labels);

        MvcResult mvcResult = mvc.perform(
                MockMvcRequestBuilders.put(uri + "/" + studyGroupOptional.get().getId())
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(mapToJson(groupForm))
        ).andReturn();

        int statusPut = mvcResult.getResponse().getStatus();
        assertEquals(204, statusPut);

        MvcResult modifiedResult = mvc.perform(MockMvcRequestBuilders.get(uri + "/" + studyGroupOptional.get().getId())
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();

        int status = modifiedResult.getResponse().getStatus();
        assertEquals(200, status);

        String modifiedStatus = modifiedResult.getResponse().getContentAsString();
        FullStudyGroupDTO gottenStudyGroup = super.mapFromJson(modifiedStatus, FullStudyGroupDTO.class);

        assertEquals(descriptionModify, gottenStudyGroup.getDescription());
        assertEquals(nameModify, gottenStudyGroup.getName());
        assertEquals(data.label2.getId(), gottenStudyGroup.getLabels().get(0).getId());
        assertEquals(data.label1.getId(), gottenStudyGroup.getLabels().get(1).getId());
    }

    @Test
    @WithMockUser("USER")
    public void modifyStudyGroupWithNonExistentLabelShouldThrowNotFound() throws Exception {
        Optional<StudyGroup> studyGroupOptional = groupRepository.findByName("Dune");

        String uri = "/studyGroup";

        List<LabelIdDTO> labels = new ArrayList<>();
        LabelIdDTO label = new LabelIdDTO(-1);
        labels.add(label);

        EditStudyGroupForm groupForm = new EditStudyGroupForm();
        String descriptionModify = "...2";
        String nameModify = "Dune 2";
        groupForm.setDescription(descriptionModify);
        groupForm.setName(nameModify);
        groupForm.setLabels(labels);

        MvcResult mvcResult = mvc.perform(
                MockMvcRequestBuilders.put(uri + "/" + studyGroupOptional.get().getId())
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(mapToJson(groupForm))
        ).andReturn();

        int statusPut = mvcResult.getResponse().getStatus();
        assertEquals(404, statusPut);
    }

    @Test
    @WithMockUser("USER")
    public void modifyStudyGroupWithWrongIdShouldReturnNotFound() throws Exception {

        String uri = "/studyGroup";

        EditStudyGroupForm groupForm = new EditStudyGroupForm();
        String descriptionModify = "...2";
        String nameModify = "Dune 2";
        groupForm.setDescription(descriptionModify);
        groupForm.setName(nameModify);

        MvcResult mvcResult = mvc.perform(
                MockMvcRequestBuilders.put(uri + "/" + -1)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(mapToJson(groupForm))
        ).andReturn();

        int statusPut = mvcResult.getResponse().getStatus();
        assertEquals(404, statusPut);
    }

    @Test
    @WithMockUser("USER")
    public void gettingLinkToStudyGroupPageShouldReturnLink() throws Exception {
        int groupId = data.group1.getId();
        String uri = "/studyGroup/" + groupId + "/inviteLink";
        String expectedLink = "http://localhost:3000/group/" + groupId;
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri)
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        int statusCode = mvcResult.getResponse().getStatus();
        assertEquals(200, statusCode);

        String link = mvcResult.getResponse().getContentAsString();

        assertEquals(link, expectedLink);
    }

    @Test
    @WithMockUser("USER")
    public void creatingGroupShouldReturnId() throws Exception {
        String uri = "/studyGroup";
        List<LabelIdDTO> label = new ArrayList<>();
        label.add(new LabelIdDTO(data.label1.getId()));
        StudyGroupForm groupForm = new StudyGroupForm("Testgroup", "....", data.user1.getId(), LocalDate.of(2021, 8, 17), label);
        MvcResult mvcResult = mvc.perform(
                MockMvcRequestBuilders.post(uri)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(mapToJson(groupForm))
        ).andReturn();
        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
        String response = mvcResult.getResponse().getContentAsString();
        StudyGroupIdDTO groupDTO = super.mapFromJson(response, StudyGroupIdDTO.class);

        int obtainedId = groupDTO.getId();

        String uriId = "/studyGroup/" + obtainedId;
        MvcResult mvcResultId = mvc.perform(
                MockMvcRequestBuilders.get(uriId)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
        ).andReturn();
        String responseId = mvcResultId.getResponse().getContentAsString();
        FullStudyGroupDTO groupDTOId = super.mapFromJson(responseId, FullStudyGroupDTO.class);

        assertEquals(groupForm.getName(),groupDTOId.getName());
        assertEquals(groupForm.getDescription(), groupDTOId.getDescription());
        assertEquals(groupForm.getCreationDate(), groupDTOId.getCreationDate());
        assertEquals(groupForm.getCreatorId(), groupDTOId.getCreatorId());
        assertEquals(groupForm.getLabels().get(0).getId(), groupDTOId.getLabels().get(0).getId());
    }

    @Test
    @WithMockUser("tolkien@gmail.com")
    public void creatingPostShouldReturnCreated() throws Exception {
        StudyGroup studyGroup = data.group1;
        String uri = "/studyGroup/" + studyGroup.getId() + "/forum";
        PostForm groupForm = new PostForm("Aguante Tolkien", LocalDateTime.now());
        MvcResult mvcResult = mvc.perform(
                MockMvcRequestBuilders.post(uri)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(mapToJson(groupForm))
        ).andReturn();
        int status = mvcResult.getResponse().getStatus();
        assertEquals(201, status);
    }

    @Test
    @WithMockUser("tolkien@gmail.com")
    public void creatingPostShouldReturnForbidden() throws Exception {
        StudyGroup studyGroup = data.group2;
        String uri = "/studyGroup/" + studyGroup.getId() + "/forum";
        PostForm groupForm = new PostForm("Aguante Tolkien", LocalDateTime.now());
        MvcResult mvcResult = mvc.perform(
                MockMvcRequestBuilders.post(uri)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(mapToJson(groupForm))
        ).andReturn();
        int status = mvcResult.getResponse().getStatus();
        assertEquals(403, status);
    }

    @Test
    @WithMockUser("tolkien@gmail.com")
    public void creatingPostWithWrongStudyGroupIdShouldReturnNotFound() throws Exception {
        String uri = "/studyGroup/" + -1 + "/forum";
        PostForm groupForm = new PostForm("Aguante Tolkien", LocalDateTime.now());
        MvcResult mvcResult = mvc.perform(
                MockMvcRequestBuilders.post(uri)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(mapToJson(groupForm))
        ).andReturn();
        int status = mvcResult.getResponse().getStatus();
        assertEquals(404, status);
    }

    @Test
    @WithMockUser("USER")
    public void creatingPostWithWrongUserIdShouldReturnNotFound() throws Exception {
        StudyGroup studyGroup = data.group2;
        String uri = "/studyGroup/" + studyGroup.getId() + "/forum";
        PostForm groupForm = new PostForm("Aguante Tolkien", LocalDateTime.now());
        MvcResult mvcResult = mvc.perform(
                MockMvcRequestBuilders.post(uri)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(mapToJson(groupForm))
        ).andReturn();
        int status = mvcResult.getResponse().getStatus();
        assertEquals(404, status);
    }

    @Test
    @WithMockUser(username = "carlos@mail.com")
    public void addingNotMemberCurrentUserShouldAdd() throws Exception {
        String uri = "/studyGroup/" + data.group1.getId() + "/me";
        MvcResult result = mvc.perform(
                MockMvcRequestBuilders.post(uri)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)).andReturn();

        assertEquals(200, result.getResponse().getStatus());
    }

    @Test
    @WithMockUser(username = "tolkien@gmail.com")
    public void whenAddingUserThatExistsInGroupShouldReturnConflict() throws Exception {
        String uri = "/studyGroup/" + data.group1.getId() + "/me";
        MvcResult result = mvc.perform(
                MockMvcRequestBuilders.post(uri)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)).andReturn();

        assertEquals(409, result.getResponse().getStatus());
    }

    @Test
    @WithMockUser(username = "tolkien@gmail.com")
    public void whenAddingToNonExistingGroupShouldReturnNotFound() throws Exception {
        String uri = "/studyGroup/100/me";
        MvcResult result = mvc.perform(
                MockMvcRequestBuilders.post(uri)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)).andReturn();

        assertEquals(404, result.getResponse().getStatus());
    }

    @Test
    @WithMockUser(username = "NonExistingUser")
    public void whenAddingNonExistingUserShouldReturnNotFound() throws Exception {
        String uri = "/studyGroup/" + data.group1.getId() + "/me";
        MvcResult result = mvc.perform(
                MockMvcRequestBuilders.post(uri)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)).andReturn();

        assertEquals(404, result.getResponse().getStatus());
    }

    @Test
    @WithMockUser("tolkien@gmail.com")
    public void getPostsFromStudyGroupShouldReturnOk() throws Exception {
        StudyGroup studyGroup = data.group1;
        String uri = "/studyGroup/" + studyGroup.getId() + "/forum/?page=0";
        PostForm groupForm = new PostForm("Aguante Tolkien", LocalDateTime.now());
        MvcResult mvcResult = mvc.perform(
                MockMvcRequestBuilders.post(uri)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(mapToJson(groupForm))
        ).andReturn();
        int status = mvcResult.getResponse().getStatus();
        assertEquals(201, status);

        MvcResult mvcGetResult = mvc.perform(
                MockMvcRequestBuilders.get(uri)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
        ).andReturn();
        int getStatus = mvcGetResult.getResponse().getStatus();
        assertEquals(200, getStatus);
    }

    @Test
    @WithMockUser("tolkien@gmail.com")
    public void getPostsFromStudyGroupWithWrongIdShouldReturnNotFound() throws Exception {
        StudyGroup studyGroup = data.group1;
        String uri = "/studyGroup/" + studyGroup.getId() + "/forum";
        PostForm groupForm = new PostForm("Aguante Tolkien", LocalDateTime.now());
        MvcResult mvcResult = mvc.perform(
                MockMvcRequestBuilders.post(uri)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(mapToJson(groupForm))
        ).andReturn();
        int status = mvcResult.getResponse().getStatus();
        assertEquals(201, status);

        String getUri = "/studyGroup/" + -1 + "/forum";
        MvcResult mvcGetResult = mvc.perform(
                MockMvcRequestBuilders.get(getUri)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
        ).andReturn();
        int getStatus = mvcGetResult.getResponse().getStatus();
        assertEquals(404, getStatus);
    }

    @Test
    @WithMockUser("tolk@gmail.com")
    public void getPostsFromStudyGroupWithNonExistingUserShouldReturnNotFound() throws Exception {
        String uri = "/studyGroup/" + data.group1.getId() + "/forum/?page=0";
        MvcResult mvcGetResult = mvc.perform(
                MockMvcRequestBuilders.get(uri)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
        ).andReturn();
        int getStatus = mvcGetResult.getResponse().getStatus();
        assertEquals(404, getStatus);
    }

    @Test
    @WithMockUser("herbert@gmail.com")
    public void getPostsFromStudyGroupWithNonMemberUserShouldReturnForbidden() throws Exception {
        String uri = "/studyGroup/" + data.group1.getId() + "/forum/?page=0";
        MvcResult mvcGetResult = mvc.perform(
                MockMvcRequestBuilders.get(uri)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
        ).andReturn();
        int getStatus = mvcGetResult.getResponse().getStatus();
        assertEquals(403, getStatus);
    }

    @Test
    @WithMockUser("tolkien@gmail.com")
    public void getPostsFromStudyGroupShouldReturnPostsInOrder() throws Exception {
        String uri = "/studyGroup/" + data.group1.getId() + "/forum";
        PostForm groupForm = new PostForm("Aguante Tolkien", LocalDateTime.now());
        MvcResult mvcResult = mvc.perform(
                MockMvcRequestBuilders.post(uri)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(mapToJson(groupForm))
        ).andReturn();
        int status = mvcResult.getResponse().getStatus();
        assertEquals(201, status);

        MvcResult mvcGetResult = mvc.perform(
                MockMvcRequestBuilders.get(uri)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
        ).andReturn();
        int getStatus = mvcGetResult.getResponse().getStatus();
        assertEquals(200, getStatus);

        String gottenStatus = mvcGetResult.getResponse().getContentAsString();
        List<PostDTO> postDTOS = super.mapFromJson(gottenStatus, new TypeReference<List<PostDTO>>(){});

        assertEquals(4, postDTOS.size());
        assertEquals("Aguante Tolkien", postDTOS.get(0).getContent());
        assertEquals(data.post2.getCreationDateTime(), postDTOS.get(1).getCreationDateAndTime());
    }

    @Test
    @WithMockUser(value = "tolkien@gmail.com")
    public void gettingPostsByGivenDateShouldReturnThem() throws Exception {
        String uri = "/studyGroup/" + data.group1.getId() + "/forum/paged/?page=0";
        MvcResult mvcGetResult = mvc.perform(
                MockMvcRequestBuilders.get(uri)
                        .param("dateFrom", data.post1.getCreationDateTime().toString())
                        .param("dateTo", LocalDateTime.of(2021, 9, 25, 3, 15).toString())
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
        ).andReturn();
        int getStatus = mvcGetResult.getResponse().getStatus();
        assertEquals(200, getStatus);

        String gottenStatus = mvcGetResult.getResponse().getContentAsString();
        Page<PostDTO> postDTOS = super.mapFromJson(gottenStatus, new TypeReference<CustomPageImpl<PostDTO>>(){});
        List<PostDTO> gottenPostDTOs = postDTOS.getContent();

        assertEquals(data.post2.getContent(), gottenPostDTOs.get(0).getContent());
    }

    @Test
    @WithMockUser(value = "tolkien@gmail.com")
    public void gettingPostsByGivenDateAndTextShouldReturnThem() throws Exception {
        String uri = "/studyGroup/" + data.group1.getId() + "/forum/paged/?page=0";
        MvcResult mvcGetResult = mvc.perform(
                MockMvcRequestBuilders.get(uri)
                        .param("dateFrom", data.post1.getCreationDateTime().toString())
                        .param("dateTo", LocalDateTime.of(2021, 9, 25, 3, 15).toString())
                        .param("text", "is out")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
        ).andReturn();
        int getStatus = mvcGetResult.getResponse().getStatus();
        assertEquals(200, getStatus);

        String gottenStatus = mvcGetResult.getResponse().getContentAsString();
        Page<PostDTO> postDTOS = super.mapFromJson(gottenStatus, new TypeReference<CustomPageImpl<PostDTO>>(){});
        List<PostDTO> gottenPostDTOs = postDTOS.getContent();

        assertEquals(data.post1.getContent(), gottenPostDTOs.get(0).getContent());
    }

    @Test
    @WithMockUser(value = "tolkien@gmail.com")
    public void gettingPostsByGivenDateFromShouldReturnThem() throws Exception {
        String uri = "/studyGroup/" + data.group1.getId() + "/forum/paged/?page=0";
        MvcResult mvcGetResult = mvc.perform(
                MockMvcRequestBuilders.get(uri)
                        .param("dateFrom", data.post1.getCreationDateTime().toString())
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
        ).andReturn();
        int getStatus = mvcGetResult.getResponse().getStatus();
        assertEquals(200, getStatus);

        String gottenStatus = mvcGetResult.getResponse().getContentAsString();
        Page<PostDTO> postDTOS = super.mapFromJson(gottenStatus, new TypeReference<CustomPageImpl<PostDTO>>(){});
        List<PostDTO> gottenPostDTOs = postDTOS.getContent();

        assertEquals(data.post2.getContent(), gottenPostDTOs.get(0).getContent());
        assertEquals(data.post1.getContent(), gottenPostDTOs.get(2).getContent());
    }

    @Test
    @WithMockUser(value = "tolkien@gmail.com")
    public void gettingPostsByGivenDateFromAndTextShouldReturnThem() throws Exception {
        String uri = "/studyGroup/" + data.group1.getId() + "/forum/paged/?page=0";
        MvcResult mvcGetResult = mvc.perform(
                MockMvcRequestBuilders.get(uri)
                        .param("dateFrom", data.post1.getCreationDateTime().toString())
                        .param("text", "LO")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
        ).andReturn();
        int getStatus = mvcGetResult.getResponse().getStatus();
        assertEquals(200, getStatus);

        String gottenStatus = mvcGetResult.getResponse().getContentAsString();
        Page<PostDTO> postDTOS = super.mapFromJson(gottenStatus, new TypeReference<CustomPageImpl<PostDTO>>(){});
        List<PostDTO> gottenPostDTOs = postDTOS.getContent();

        assertEquals(data.post2.getContent(), gottenPostDTOs.get(0).getContent());
        assertEquals(data.post1.getContent(), gottenPostDTOs.get(1).getContent());
    }

    @Test
    @WithMockUser(value = "tolkien@gmail.com")
    public void gettingPostsByGivenDateToShouldReturnThem() throws Exception {
        String uri = "/studyGroup/" + data.group1.getId() + "/forum/paged/?page=0";
        MvcResult mvcGetResult = mvc.perform(
                MockMvcRequestBuilders.get(uri)
                        .param("dateTo", LocalDateTime.of(2021, 9, 25, 3, 15).toString())
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
        ).andReturn();
        int getStatus = mvcGetResult.getResponse().getStatus();
        assertEquals(200, getStatus);

        String gottenStatus = mvcGetResult.getResponse().getContentAsString();
        Page<PostDTO> postDTOS = super.mapFromJson(gottenStatus, new TypeReference<CustomPageImpl<PostDTO>>(){});
        List<PostDTO> gottenPostDTOs = postDTOS.getContent();

        assertEquals(data.post2.getContent(), gottenPostDTOs.get(0).getContent());
    }

    @Test
    @WithMockUser(value = "tolkien@gmail.com")
    public void gettingPostsByGivenDateToAndTextShouldReturnThem() throws Exception {
        String uri = "/studyGroup/" + data.group1.getId() + "/forum/paged/?page=0";
        MvcResult mvcGetResult = mvc.perform(
                MockMvcRequestBuilders.get(uri)
                        .param("dateTo", LocalDateTime.of(2021, 9, 25, 3, 15).toString())
                        .param("text", "is out")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
        ).andReturn();
        int getStatus = mvcGetResult.getResponse().getStatus();
        assertEquals(200, getStatus);

        String gottenStatus = mvcGetResult.getResponse().getContentAsString();
        Page<PostDTO> postDTOS = super.mapFromJson(gottenStatus, new TypeReference<CustomPageImpl<PostDTO>>(){});
        List<PostDTO> gottenPostDTOs = postDTOS.getContent();

        assertEquals(data.post1.getContent(), gottenPostDTOs.get(0).getContent());
    }

    @Test
    @WithMockUser(value = "tolkien@gmail.com")
    public void gettingPostsWithNoDateOrTextGivenShouldReturnAll() throws Exception {
        String uri = "/studyGroup/" + data.group1.getId() + "/forum/paged/?page=0";
        MvcResult mvcGetResult = mvc.perform(
                MockMvcRequestBuilders.get(uri)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
        ).andReturn();
        int getStatus = mvcGetResult.getResponse().getStatus();
        assertEquals(200, getStatus);

        String gottenStatus = mvcGetResult.getResponse().getContentAsString();
        Page<PostDTO> postDTOS = super.mapFromJson(gottenStatus, new TypeReference<CustomPageImpl<PostDTO>>(){});
        List<PostDTO> gottenPostDTOs = postDTOS.getContent();

        assertEquals(data.post2.getContent(), gottenPostDTOs.get(0).getContent());
        assertEquals(data.post.getContent(), gottenPostDTOs.get(1).getContent());
    }

    @Test
    @WithMockUser(value = "tolkien@gmail.com")
    public void gettingPostsOnlyWithTextGivenShouldReturnTheSpecific() throws Exception {
        String uri = "/studyGroup/" + data.group1.getId() + "/forum/paged/?page=0";
        MvcResult mvcGetResult = mvc.perform(
                MockMvcRequestBuilders.get(uri)
                        .param("text", "OTR")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
        ).andReturn();
        int getStatus = mvcGetResult.getResponse().getStatus();
        assertEquals(200, getStatus);

        String gottenStatus = mvcGetResult.getResponse().getContentAsString();
        Page<PostDTO> postDTOS = super.mapFromJson(gottenStatus, new TypeReference<CustomPageImpl<PostDTO>>(){});
        List<PostDTO> gottenPostDTOs = postDTOS.getContent();

        assertEquals(data.post2.getContent(), gottenPostDTOs.get(0).getContent());
        assertEquals(data.post1.getContent(), gottenPostDTOs.get(1).getContent());
    }


    @Test
    @WithMockUser("tolkien@gmail.com")
    public void deleteStudyGroupWithCreatorShouldReturnOk() throws Exception {
        String uri = "/studyGroup";
        MvcResult mvcResult = mvc.perform(
                MockMvcRequestBuilders.delete(uri + "/" + data.group1.getId())
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
        ).andReturn();
        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
    }

    @Test
    @WithMockUser("herbert@gmail.com")
    public void deleteStudyGroupWithNonCreatorShouldReturnForbidden() throws Exception {
        String uri = "/studyGroup";
        MvcResult mvcResult = mvc.perform(
                MockMvcRequestBuilders.delete(uri + "/" + data.group1.getId())
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
        ).andReturn();
        int status = mvcResult.getResponse().getStatus();
        assertEquals(403, status);
    }

    @Test
    @WithMockUser("tolk@gmail.com")
    public void deleteStudyGroupWithNonExistingUserShouldReturnNotFound() throws Exception {
        String uri = "/studyGroup";
        MvcResult mvcResult = mvc.perform(
                MockMvcRequestBuilders.delete(uri + "/" + data.group1.getId())
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
        ).andReturn();
        int status = mvcResult.getResponse().getStatus();
        assertEquals(404, status);
    }

    @Test
    @WithMockUser("tolkien@gmail.com")
    public void deleteNonExistingStudyGroupUserShouldReturnNotFound() throws Exception {
        String uri = "/studyGroup";
        MvcResult mvcResult = mvc.perform(
                MockMvcRequestBuilders.delete(uri + "/" + -1)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
        ).andReturn();
        int status = mvcResult.getResponse().getStatus();
        assertEquals(404, status);
    }

    @Test
    @WithMockUser("tolkien@gmail.com")
    public void deletedStudyGroupShouldBeDeleted() throws Exception {
        String uri = "/studyGroup";
        MvcResult mvcResult = mvc.perform(
                MockMvcRequestBuilders.delete(uri + "/" + data.group1.getId())
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
        ).andReturn();
        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);

        MvcResult mvcGETResult = mvc.perform(
                MockMvcRequestBuilders.get(uri + "/" + data.group1.getId())
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
        ).andReturn();
        int getStatus = mvcGETResult.getResponse().getStatus();
        assertEquals(404, getStatus);
    }

    @Test
    @WithMockUser(username = "tolkien@gmail.com")
    public void getUserGroupsShouldReturnUserGroups() throws Exception {
        String uri = "/studyGroup/me/paged/?page=0";

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri)
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        int statusCode = mvcResult.getResponse().getStatus();
        assertEquals(200, statusCode);

        String status = mvcResult.getResponse().getContentAsString();
        List<StudyGroupDTO> groupsDTOs = pageToList(status);

        assertEquals(groupsDTOs.get(0).getName(), data.group1.getName());
    }

    @Test
    @WithMockUser("juan@mail.com")
    public void getUserWithNoGroupsShouldReturnEmptyList() throws Exception {
        String uri = "/studyGroup/me/paged";

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri)
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        int statusCode = mvcResult.getResponse().getStatus();
        assertEquals(200, statusCode);

        String status = mvcResult.getResponse().getContentAsString();
        List<StudyGroupDTO> groupsDTOs = pageToList(status);

        assertEquals(groupsDTOs.size(), 0);
    }

    @Test
    @WithMockUser(username = "carl@mail.com")
    public void getNonExistingUserGroupsShouldReturnNotFound() throws Exception {
        String uri = "/studyGroup/me";

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri)
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        int statusCode = mvcResult.getResponse().getStatus();
        assertEquals(404, statusCode);
    }

    @Test
    @WithMockUser("tolkien@gmail.com")
    public void getGroupPostByGroupAndPostIdShouldReturnOk() throws Exception {
        StudyGroup studyGroup = data.group1;
        String uri = "/studyGroup/" + studyGroup.getId() + "/forum/" + data.post1.getId();

        MvcResult mvcGetResult = mvc.perform(
                MockMvcRequestBuilders.get(uri)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
        ).andReturn();
        int getStatus = mvcGetResult.getResponse().getStatus();
        assertEquals(200, getStatus);

        String gottenStatus = mvcGetResult.getResponse().getContentAsString();
        PostDTO postDTO = super.mapFromJson(gottenStatus, PostDTO.class);
        assertEquals(data.post1.getCreator().getId(), postDTO.getCreatorId());
        assertEquals(data.post1.getContent(), postDTO.getContent());
    }

    @Test
    @WithMockUser("tolkien@gmail.com")
    public void deletePostWithBadForumIdShouldReturnNotFound() throws Exception {
        StudyGroup studyGroup = data.group1;
        String uri = "/studyGroup/" + studyGroup.getId() + "/forum/" + -1;
        MvcResult deleteResult = mvc.perform(
                MockMvcRequestBuilders.delete(uri)
        ).andReturn();
        int status = deleteResult.getResponse().getStatus();
        assertEquals(404, status);
    }

    @Test
    @WithMockUser("tolkien@gmail.com")
    public void deletePostWithBadStudyGroupIdShouldReturnNotFound() throws Exception {
        String uri = "/studyGroup/" + -1 + "/forum/" + data.post.getId();
        MvcResult deleteResult = mvc.perform(
                MockMvcRequestBuilders.delete(uri)
        ).andReturn();
        int status = deleteResult.getResponse().getStatus();
        assertEquals(404, status);
    }

    @Test
    @WithMockUser("USER")
    public void deletePostWithBadUserShouldReturnNotFound() throws Exception {
        String uri = "/studyGroup/" + data.group1.getId() + "/forum/" + data.post.getId();
        MvcResult deleteResult = mvc.perform(
                MockMvcRequestBuilders.delete(uri)
        ).andReturn();
        int status = deleteResult.getResponse().getStatus();
        assertEquals(404, status);
    }

    @Test
    @WithMockUser("tolkien@gmail.com")
    public void deletePostWithUserCreatorShould() throws Exception {
        String uri = "/studyGroup/" + data.group1.getId() + "/forum/" + data.post.getId();
        MvcResult deleteResult = mvc.perform(
                MockMvcRequestBuilders.delete(uri)
        ).andReturn();
        int status = deleteResult.getResponse().getStatus();
        assertEquals(204, status);
    }

    @Test
    @WithMockUser("tolkien@gmail.com")
    public void deletePostWithPostCreatorShouldReturnOk() throws Exception {
        String uri = "/studyGroup/" + data.group1.getId() + "/forum/" + data.post.getId();
        MvcResult deleteResult = mvc.perform(
                MockMvcRequestBuilders.delete(uri)
        ).andReturn();
        int status = deleteResult.getResponse().getStatus();
        assertEquals(204, status);
    }

    @Test
    @WithMockUser("herbert@gmail.com")
    public void deletePostWithNonPostCreatorShouldReturnForbidden() throws Exception {
        String uri = "/studyGroup/" + data.group1.getId() + "/forum/" + data.post.getId();
        MvcResult deleteResult = mvc.perform(
                MockMvcRequestBuilders.delete(uri)
        ).andReturn();
        int status = deleteResult.getResponse().getStatus();
        assertEquals(403, status);
    }

    @Test
    @WithMockUser("herbert@gmail.com")
    public void getGroupPostByGroupAndPostIdWithNonMemberShouldReturnForbidden() throws Exception {
        StudyGroup studyGroup = data.group1;
        String uri = "/studyGroup/" + studyGroup.getId() + "/forum/" + data.post1.getId();

        MvcResult mvcGetResult = mvc.perform(
                MockMvcRequestBuilders.get(uri)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
        ).andReturn();
        int getStatus = mvcGetResult.getResponse().getStatus();
        assertEquals(403, getStatus);
    }

    @Test
    @WithMockUser("tolkien@gmail.com")
    public void getGroupPostByGroupAndPostIdWithWrongPostIdShouldReturnNotFound() throws Exception {
        StudyGroup studyGroup = data.group1;
        String uri = "/studyGroup/" + studyGroup.getId() + "/forum/" + -1;

        MvcResult mvcGetResult = mvc.perform(
                MockMvcRequestBuilders.get(uri)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
        ).andReturn();
        int getStatus = mvcGetResult.getResponse().getStatus();
        assertEquals(404, getStatus);
    }

    @Test
    @WithMockUser("tolkien@gmail.com")
    public void getGroupPostByGroupAndPostIdWithWrongStudyGroupIdShouldReturnNotFound() throws Exception {
        String uri = "/studyGroup/" + -1 + "/forum/" + data.post1.getId();

        MvcResult mvcGetResult = mvc.perform(
                MockMvcRequestBuilders.get(uri)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
        ).andReturn();
        int getStatus = mvcGetResult.getResponse().getStatus();
        assertEquals(404, getStatus);
    }

    @Test
    @WithMockUser("tolkien@gmail.com")
    public void getGroupPostByGroupAndPostIdWithWrongStudyGroupIdAndPostIdShouldReturnForbidden() throws Exception {
        String uri = "/studyGroup/" + -1 + "/forum/" + -1;

        MvcResult mvcGetResult = mvc.perform(
                MockMvcRequestBuilders.get(uri)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
        ).andReturn();
        int getStatus = mvcGetResult.getResponse().getStatus();
        assertEquals(404, getStatus);
    }

    @Test
    @WithMockUser("USER")
    public void getAllLabelsInSystemShouldReturnOk() throws Exception {
        String uri = "/studyGroup/label";

        MvcResult mvcGetResult = mvc.perform(
                MockMvcRequestBuilders.get(uri)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
        ).andReturn();
        int getStatus = mvcGetResult.getResponse().getStatus();
        assertEquals(200, getStatus);
    }

    @Test
    @WithMockUser("USER")
    public void getAllLabelsInSystemShouldReturnLabels() throws Exception {
        String uri = "/studyGroup/label";

        MvcResult mvcGetResult = mvc.perform(
                MockMvcRequestBuilders.get(uri)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
        ).andReturn();
        int getStatus = mvcGetResult.getResponse().getStatus();
        assertEquals(200, getStatus);

        String gottenStatus = mvcGetResult.getResponse().getContentAsString();
        List<LabelDTO> labelDTO = super.mapFromJson(gottenStatus, new TypeReference<List<LabelDTO>>(){});

        assertEquals(data.label1.getId(), labelDTO.get(0).getId());
        assertEquals(data.label2.getName(), labelDTO.get(1).getName());
    }

    @Test
    @WithMockUser("tolkien@gmail.com")
    public void whenGettingAllGroupsShouldReturnSorted() throws Exception {
        String uri = "/studyGroup/paged/?page=0";
        MvcResult mvcResult = mvc.perform(
                MockMvcRequestBuilders.get(uri)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
        ).andReturn();

        String gottenStatus = mvcResult.getResponse().getContentAsString();
        Page<StudyGroupDTO> gottenStudyGroupPage = super.mapFromJson(gottenStatus, new TypeReference<CustomPageImpl<StudyGroupDTO>>(){});
        List<StudyGroupDTO> gottenStudyGroup = gottenStudyGroupPage.getContent();

        assertTrue(gottenStudyGroup.get(0).getCreationDate().compareTo(gottenStudyGroup.get(1).getCreationDate()) > 0);
    }


    @Test
    @WithMockUser("tolkien@gmail.com")
    public void getStudyGroupsByLabelIdShouldReturnOk() throws Exception {
        String uri = "/studyGroup";
        MvcResult mvcResult = mvc.perform(
                MockMvcRequestBuilders.get(uri)
                        .param("label", Integer.toString(data.label1.getId()))
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
        ).andReturn();
        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
    }

    @Test
    @WithMockUser("tolkien@gmail.com")
    public void getStudyGroupsByLabelIdReturnsReturnsAmountExpected() throws Exception {
        String uri = "/studyGroup/paged/?page=0";
        MvcResult mvcResult = mvc.perform(
                MockMvcRequestBuilders.get(uri)
                        .param("label", Integer.toString(data.label2.getId()))
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
        ).andReturn();
        String status = mvcResult.getResponse().getContentAsString();
        List<StudyGroupDTO> allStudyGroups = pageToList(status);
        assertEquals(2,allStudyGroups.size());
    }

    @Test
    @WithMockUser("tolkien@gmail.com")
    public void getStudyGroupsByLabelIdHasExpectedValues() throws Exception {
        String uri = "/studyGroup/paged/?page=0";
        MvcResult mvcResult = mvc.perform(
                MockMvcRequestBuilders.get(uri)
                        .param("label", data.label2.getId() + "," + data.label3.getId())
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
        ).andReturn();
        String status = mvcResult.getResponse().getContentAsString();
        List<StudyGroupDTO> allStudyGroups = pageToList(status);
        List<String> expectedStudyGroupsNames = new ArrayList<>();
        expectedStudyGroupsNames.add(data.group3.getName());
        expectedStudyGroupsNames.add(data.group2.getName());
        for (StudyGroupDTO studyGroup : allStudyGroups) {
            assertThat(expectedStudyGroupsNames, hasItems(studyGroup.getName()));
        }
    }

    @Test
    @WithMockUser("tolkien@gmail.com")
    public void getStudyGroupsByLabelIdAndTextShouldReturnOk() throws Exception {
        String uri = "/studyGroup";
        MvcResult mvcResult = mvc.perform(
                MockMvcRequestBuilders.get(uri)
                        .param("text", ".")
                        .param("label", Integer.toString(data.label1.getId()))
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
        ).andReturn();
        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
    }

    @Test
    @WithMockUser("tolkien@gmail.com")
    public void getStudyGroupsByLabelIdAndTextReturnsReturnsAmountExpected() throws Exception {
        String uri = "/studyGroup/paged/?page=0";
        MvcResult mvcResult = mvc.perform(
                MockMvcRequestBuilders.get(uri)
                        .param("text", ".")
                        .param("label", Integer.toString(data.label2.getId()))
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
        ).andReturn();
        String status = mvcResult.getResponse().getContentAsString();
        List<StudyGroupDTO> allStudyGroups = pageToList(status);
        assertEquals(1,allStudyGroups.size());
    }

    @Test
    @WithMockUser("tolkien@gmail.com")
    public void getStudyGroupsByLabelIdAndTextHasExpectedValues() throws Exception {
        String uri = "/studyGroup/paged/?pge=0";
        MvcResult mvcResult = mvc.perform(
                MockMvcRequestBuilders.get(uri)
                        .param("text", ".")
                        .param("label", data.label2.getId() + "," + data.label3.getId())
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
        ).andReturn();
        String status = mvcResult.getResponse().getContentAsString();
        List<StudyGroupDTO> allStudyGroups = pageToList(status);
        List<String> expectedStudyGroupsNames = new ArrayList<>();
        expectedStudyGroupsNames.add(data.group3.getName());
        expectedStudyGroupsNames.add(data.group2.getName());
        expectedStudyGroupsNames.add(data.group1.getName());
        for (StudyGroupDTO studyGroup : allStudyGroups) {
            assertThat(expectedStudyGroupsNames, hasItems(studyGroup.getName()));
        }
    }


    @Test
    @WithMockUser(username = "herbert@gmail.com")
    public void getUserGroupsWithNameShouldReturnUserGroups() throws Exception {
        String uri = "/studyGroup/me";

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri).param("text", "Dune")
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        int statusCode = mvcResult.getResponse().getStatus();
        assertEquals(200, statusCode);

        String status = mvcResult.getResponse().getContentAsString();
        List<StudyGroupDTO> groupsDTOs = super.mapFromJson(status, new TypeReference<List<StudyGroupDTO>>(){});

        assertEquals(data.group2.getName(), groupsDTOs.get(0).getName());
    }

    @Test
    @WithMockUser(username = "herbert@gmail.com")
    public void getUserGroupsWithPartialNameShouldReturnUserGroups() throws Exception {
        String uri = "/studyGroup/me";

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri).param("text", "Du")
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        int statusCode = mvcResult.getResponse().getStatus();
        assertEquals(200, statusCode);

        String status = mvcResult.getResponse().getContentAsString();
        List<StudyGroupDTO> groupsDTOs = super.mapFromJson(status, new TypeReference<List<StudyGroupDTO>>(){});

        assertEquals(data.group2.getName(), groupsDTOs.get(0).getName());
    }

    @Test
    @WithMockUser("tolkien@gmail.com")
    public void getUserGroupsWithDescriptionShouldReturnUserGroups() throws Exception {
        String uri = "/studyGroup/me";
        MvcResult mvcResult = mvc.perform(
                MockMvcRequestBuilders.get(uri)
                        .param("text", "...")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
        ).andReturn();
        String status = mvcResult.getResponse().getContentAsString();
        FullStudyGroupDTO[] gottenStudyGroups = super.mapFromJson(status, FullStudyGroupDTO[].class);
        List<StudyGroupDTO> allStudyGroups = Arrays.stream(gottenStudyGroups).collect(Collectors.toList());
        List<String> expectedStudyGroupsNames = new ArrayList<>();
        expectedStudyGroupsNames.add(data.group1.getName());
        expectedStudyGroupsNames.add(data.group2.getName());
        for (StudyGroupDTO studyGroup : allStudyGroups) {
            assertThat(expectedStudyGroupsNames, hasItems(studyGroup.getName()));
        }
    }

    @Test
    @WithMockUser("tolkien@gmail.com")
    public void getUserGroupsWithPartialDescriptionShouldReturnUserGroups() throws Exception {
        String uri = "/studyGroup/me";
        MvcResult mvcResult = mvc.perform(
                MockMvcRequestBuilders.get(uri)
                        .param("text", ".")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
        ).andReturn();
        String status = mvcResult.getResponse().getContentAsString();
        FullStudyGroupDTO[] gottenStudyGroups = super.mapFromJson(status, FullStudyGroupDTO[].class);
        List<StudyGroupDTO> allStudyGroups = Arrays.stream(gottenStudyGroups).collect(Collectors.toList());
        List<String> expectedStudyGroupsNames = new ArrayList<>();
        expectedStudyGroupsNames.add(data.group1.getName());
        expectedStudyGroupsNames.add(data.group2.getName());
        for (StudyGroupDTO studyGroup : allStudyGroups) {
            assertThat(expectedStudyGroupsNames, hasItems(studyGroup.getName()));
        }
    }

    @Test
    @WithMockUser("herbert@gmail.com")
    public void getUserGroupsWithLabelShouldReturnUserGroups() throws Exception {
        String uri = "/studyGroup/me";
        MvcResult mvcResult = mvc.perform(
                MockMvcRequestBuilders.get(uri)
                        .param("label", Integer.toString(data.label1.getId()))
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
        ).andReturn();
        String status = mvcResult.getResponse().getContentAsString();
        FullStudyGroupDTO[] gottenStudyGroups = super.mapFromJson(status, FullStudyGroupDTO[].class);
        List<StudyGroupDTO> allStudyGroups = Arrays.stream(gottenStudyGroups).collect(Collectors.toList());
        List<String> expectedStudyGroupsNames = new ArrayList<>();
        expectedStudyGroupsNames.add(data.group1.getName());
        for (StudyGroupDTO studyGroup : allStudyGroups) {
            assertThat(expectedStudyGroupsNames, hasItems(studyGroup.getName()));
        }
    }

    @Test
    @WithMockUser("herbert@gmail.com")
    public void getUserGroupsWithLabelsShouldReturnUserGroups() throws Exception {
        String uri = "/studyGroup/me";
        MvcResult mvcResult = mvc.perform(
                MockMvcRequestBuilders.get(uri)
                        .param("label", data.label1.getId() + "," + data.label3.getId())
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
        ).andReturn();
        String status = mvcResult.getResponse().getContentAsString();
        FullStudyGroupDTO[] gottenStudyGroups = super.mapFromJson(status, FullStudyGroupDTO[].class);
        List<StudyGroupDTO> allStudyGroups = Arrays.stream(gottenStudyGroups).collect(Collectors.toList());
        List<String> expectedStudyGroupsNames = new ArrayList<>();
        expectedStudyGroupsNames.add(data.group1.getName());
        for (StudyGroupDTO studyGroup : allStudyGroups) {
            assertThat(expectedStudyGroupsNames, hasItems(studyGroup.getName()));
        }
    }

    @Test
    @WithMockUser("herbert@gmail.com")
    public void getUserGroupsWithLabelAndTextShouldReturnNothing() throws Exception {
        String uri = "/studyGroup/me";
        MvcResult mvcResult = mvc.perform(
                MockMvcRequestBuilders.get(uri)
                        .param("text", "Description")
                        .param("label", Integer.toString(data.label1.getId()))
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
        ).andReturn();
        String status = mvcResult.getResponse().getContentAsString();
        FullStudyGroupDTO[] gottenStudyGroups = super.mapFromJson(status, FullStudyGroupDTO[].class);
        List<StudyGroupDTO> allStudyGroups = Arrays.stream(gottenStudyGroups).collect(Collectors.toList());
        assertTrue(allStudyGroups.isEmpty());
    }

    @Test
    @WithMockUser("herbert@gmail.com")
    public void getUserGroupsWithLabelAndTextShouldReturnUserGroups() throws Exception {
        String uri = "/studyGroup/me";
        MvcResult mvcResult = mvc.perform(
                MockMvcRequestBuilders.get(uri)
                        .param("text", ".")
                        .param("label", Integer.toString(data.label1.getId()))
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
        ).andReturn();
        String status = mvcResult.getResponse().getContentAsString();
        FullStudyGroupDTO[] gottenStudyGroups = super.mapFromJson(status, FullStudyGroupDTO[].class);
        List<StudyGroupDTO> allStudyGroups = Arrays.stream(gottenStudyGroups).collect(Collectors.toList());
        List<String> expectedStudyGroupsNames = new ArrayList<>();
        expectedStudyGroupsNames.add(data.group1.getName());
        for (StudyGroupDTO studyGroup : allStudyGroups) {
            assertThat(expectedStudyGroupsNames, hasItems(studyGroup.getName()));
        }
    }

    @Test
    @WithMockUser("herbert@gmail.com")
    public void getUserGroupsWithLabelsAndTextShouldReturnUserGroups() throws Exception {
        String uri = "/studyGroup/me";
        MvcResult mvcResult = mvc.perform(
                MockMvcRequestBuilders.get(uri)
                        .param("text", ".")
                        .param("label", data.label1.getId() + "," + data.label3.getId())
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
        ).andReturn();
        String status = mvcResult.getResponse().getContentAsString();
        FullStudyGroupDTO[] gottenStudyGroups = super.mapFromJson(status, FullStudyGroupDTO[].class);
        List<StudyGroupDTO> allStudyGroups = Arrays.stream(gottenStudyGroups).collect(Collectors.toList());
        List<String> expectedStudyGroupsNames = new ArrayList<>();
        expectedStudyGroupsNames.add(data.group1.getName());
        for (StudyGroupDTO studyGroup : allStudyGroups) {
            assertThat(expectedStudyGroupsNames, hasItems(studyGroup.getName()));
        }
    }

    @Test
    @WithMockUser("tolkien@gmail.com")
    public void deleteGroupCreatorAndLastUserShouldDeleteGroup() throws Exception {
        String uri = "/studyGroup/" + data.group1.getId() + "/" + data.user1.getId();
        MvcResult mvcResult = mvc.perform(
                MockMvcRequestBuilders.delete(uri)
        ).andReturn();
        assertEquals(200, mvcResult.getResponse().getStatus());
        assertFalse(groupRepository.findById(data.group1.getCreator().getId()).isPresent());
    }

    @Test
    @WithMockUser("tolkien@gmail.com")
    public void deleteGroupUserShouldDelete() throws Exception{
        String uri = "/studyGroup/" + data.group2.getId() + "/" + data.user3.getId();
        MvcResult mvcResult = mvc.perform(
                MockMvcRequestBuilders.delete(uri)
        ).andReturn();
        assertEquals(200, mvcResult.getResponse().getStatus());
        assertFalse(studyGroupUsersRepository.findById(data.group2User3.getId()).isPresent());
    }
}
