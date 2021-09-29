package com.agora.agora;

import com.agora.agora.controller.StudyGroupController;
import com.agora.agora.model.StudyGroup;
import com.agora.agora.model.StudyGroupUser;
import com.agora.agora.model.User;
import com.agora.agora.model.dto.*;
import com.agora.agora.model.form.EditStudyGroupForm;
import com.agora.agora.model.form.PostForm;
import com.agora.agora.model.form.StudyGroupForm;
import com.agora.agora.model.type.UserType;
import com.agora.agora.repository.PostRepository;
import com.agora.agora.repository.StudyGroupRepository;
import com.agora.agora.repository.StudyGroupUsersRepository;
import com.agora.agora.repository.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.hasItems;
import static org.junit.Assert.*;

public class StudyGroupControllerTest extends AbstractTest{

    @Autowired
    private StudyGroupRepository groupRepository;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StudyGroupUsersRepository studyGroupUsersRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private StudyGroupController studyGroupController;

    private Data data = new Data();

    // Storing all the info we'll load into the DB here.
    private class Data {
        User user1;
        User user2;
        StudyGroup group1;
        StudyGroup group2;

        void setup() {
            user1 = new User("J. R. R.", "Tolkien", "tolkien@gmail.com", "Jrrtolkien2021", false, UserType.USER);
            user2 = new User("Frank", "Herbert", "herbert@gmail.com", "Frankherbert2021", false, UserType.USER);
            userRepository.save(user1);
            userRepository.save(user2);

            group1 = new StudyGroup("Lord of the rings", "...", user1, LocalDate.of(2021, 8, 17));
            group2 = new StudyGroup("Dune", "....", user2, LocalDate.of(2021, 8, 16));
            groupRepository.save(group1);
            groupRepository.save(group2);

            StudyGroupUser group1User1 = new StudyGroupUser(user1, group1);
            StudyGroupUser group2User2 = new StudyGroupUser(user2, group2);
            studyGroupUsersRepository.save(group1User1);
            studyGroupUsersRepository.save(group2User2);
        }

        void rollback() {
            postRepository.deleteAll();
            studyGroupUsersRepository.deleteAll();
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
    @WithMockUser("USER")
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
    @WithMockUser("USER")
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
    @WithMockUser("USER")
    public void createNewStudyGroupShouldReturnCreatedAndItsUserListShouldHaveCreatorInIt() throws Exception {
        String uri = "/studyGroup";
        StudyGroupForm groupForm = new StudyGroupForm("Testgroup", "....", data.user1.getId(), LocalDate.of(2021, 8, 17));
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
        assertEquals(gottenStudyGroup.getUserContacts().get(1).getId(), data.user1.getId());
        assertEquals(gottenStudyGroup.getUserContacts().get(1).getEmail(), data.user1.getEmail());
        assertEquals(gottenStudyGroup.getUserContacts().get(1).getName(), data.user1.getName());
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
    @WithMockUser("USER")
    public void getAllStudentsShouldReturnOk() throws Exception {
        String uri = "/studyGroup";
        MvcResult mvcResult = mvc.perform(
                MockMvcRequestBuilders.get(uri)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
        ).andReturn();
        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
    }

    @Test
    @WithMockUser("USER")
    public void findAllStudyGroupsReturnsReturnsAmountExpected() {
        List<StudyGroupDTO> allStudyGroups = studyGroupController.getAllStudyGroups(Optional.empty());
        assertEquals(2,allStudyGroups.size());
    }

    @Test
    @WithMockUser("USER")
    public void findAllStudyGroupsHasExpectedValues() {
        List<StudyGroupDTO> allStudyGroups = studyGroupController.getAllStudyGroups(Optional.empty());
        List<String> expectedStudyGroupsNames = new ArrayList<>();
        expectedStudyGroupsNames.add(data.group1.getName());
        expectedStudyGroupsNames.add(data.group2.getName());
        for (StudyGroupDTO studyGroup : allStudyGroups) {
            assertThat(expectedStudyGroupsNames, hasItems(studyGroup.getName()));
        }
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
        assertEquals(gottenStudyGroup.getUserContacts().get(1).getId(), user.getId());
        assertEquals(gottenStudyGroup.getUserContacts().get(1).getEmail(), user.getEmail());
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
        StudyGroupForm groupForm = new StudyGroupForm("Testgroup", "....", data.user1.getId(), LocalDate.of(2021, 8, 17));
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
    @WithMockUser("USER")
    public void findStudyGroupByExistingNameShouldReturnValues(){
        List<StudyGroupDTO> allStudyGroups = studyGroupController.getAllStudyGroups(Optional.of("Dune"));
        assertEquals(1,allStudyGroups.size());
    }

    @Test
    @WithMockUser("USER")
    public void findStudyGroupByExistingDescriptionShouldReturnValues(){
        List<StudyGroupDTO> allStudyGroups = studyGroupController.getAllStudyGroups(Optional.of("."));
        assertEquals(2,allStudyGroups.size());
    }

    @Test
    @WithMockUser("USER")
    public void findStudyGroupByNonExistingDescriptionShouldReturnNothing(){
        List<StudyGroupDTO> allStudyGroups = studyGroupController.getAllStudyGroups(Optional.of(("No hay un grupo con esto")));
        assertEquals(0,allStudyGroups.size());
    }

    @Test
    @WithMockUser("USER")
    public void findStudyGroupByDescriptionHasExpectedValues() {
        List<StudyGroupDTO> allStudyGroups = studyGroupController.getAllStudyGroups(Optional.of((".")));
        List<String> expectedStudyGroupsNames = new ArrayList<>();
        expectedStudyGroupsNames.add(data.group1.getName());
        expectedStudyGroupsNames.add(data.group2.getName());
        for (StudyGroupDTO studyGroup : allStudyGroups) {
            assertThat(expectedStudyGroupsNames, hasItems(studyGroup.getName()));
        }
    }

    @Test
    @WithMockUser("USER")
    public void findStudyGroupByNameHasExpectedValue() {
        List<StudyGroupDTO> allStudyGroups = studyGroupController.getAllStudyGroups(Optional.of(("Dune")));
        List<String> expectedStudyGroupsNames = new ArrayList<>();
        expectedStudyGroupsNames.add(data.group2.getName());
        for (StudyGroupDTO studyGroup : allStudyGroups) {
            assertThat(expectedStudyGroupsNames, hasItems(studyGroup.getName()));
        }
    }

    @Test
    @WithMockUser("USER")
    public void findStudyGroupByPartialNameHasExpectedValue() {
        List<StudyGroupDTO> allStudyGroups = studyGroupController.getAllStudyGroups(Optional.of(("Du")));
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

        assertEquals(1, gottenStudyGroup.getUserContacts().size());
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

        EditStudyGroupForm groupForm = new EditStudyGroupForm();
        String descriptionModify = "...2";
        String nameModify = "Dune 2";
        groupForm.setDescription(descriptionModify);
        groupForm.setName(nameModify);

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
    public void gettingLinkToStudyGrouPageShouldReturnLink() throws Exception {
        int groupId = data.group1.getId();
        String uri = "/studyGroup/" + groupId + "/inviteLink";
        String expectedLink = "http://localhost:3000/studyGroup/" + groupId;
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
        StudyGroupForm groupForm = new StudyGroupForm("Testgroup", "....", data.user1.getId(), LocalDate.of(2021, 8, 17));
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


}
