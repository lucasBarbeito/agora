package com.agora.agora;

import com.agora.agora.controller.StudyGroupController;
import com.agora.agora.model.StudyGroup;
import com.agora.agora.model.StudyGroupUser;
import com.agora.agora.model.User;
import com.agora.agora.model.dto.FullUserDTO;
import com.agora.agora.model.dto.StudyGroupDTO;
import com.agora.agora.model.form.UserForm;
import com.agora.agora.model.form.UserVerificationForm;
import com.agora.agora.model.type.UserType;
import com.agora.agora.repository.PostRepository;
import com.agora.agora.repository.StudyGroupRepository;
import com.agora.agora.repository.StudyGroupUsersRepository;
import com.agora.agora.repository.UserRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class UserControlerTest extends AbstractTest{

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
        User user3;
        StudyGroup group1;
        StudyGroup group2;

        void setup() {
            user1 = new User("Carlos", "Gimenez", "carlos@mail.com", "Carlos123", false, UserType.USER);
            user2 = new User("Frank", "Herbert", "herbert@gmail.com", "Frankherbert2021", false, UserType.USER);
            user3 = new User("J. R. R.", "Tolkien", "tolkien@gmail.com", "Jrrtolkien2021", false, UserType.USER);
            userRepository.save(user1);
            userRepository.save(user2);
            userRepository.save(user3);

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

    @Before
    public void setUpDB() {
        data.setup();
    }

    @After
    public void rollBackDB() {
        data.rollback();
    }

    @Test
    public void createUserShouldReturnCreated() throws Exception {
        String uri = "/user";
        UserForm form= new UserForm("Manuel","Gimenez","manuel@mail.com","Manuel123");
        MvcResult mvcResult = mvc.perform(
                MockMvcRequestBuilders.post(uri)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(mapToJson(form))
        ).andReturn();
        int status = mvcResult.getResponse().getStatus();
        assertEquals(201, status);
    }

    @Test
    public void createUserShouldReturnDataIntegrityViolation() throws Exception {
        String uri = "/user";
        UserForm form= new UserForm("Manuel","Gimenez","carlos@mail.com","Manuel123");
        MvcResult mvcResult = mvc.perform(
                MockMvcRequestBuilders.post(uri)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(mapToJson(form))
        ).andReturn();
        int status = mvcResult.getResponse().getStatus();
        assertEquals(409, status);
    }

    @Test
    public void createUserWithWrongPassShouldReturnError() throws Exception {
        String uri = "/user";
        UserForm form= new UserForm("Manuel","Gimenez","manuel@mail.com","Manuel");
        MvcResult mvcResult = mvc.perform(
                MockMvcRequestBuilders.post(uri)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(mapToJson(form))
        ).andReturn();
        int status = mvcResult.getResponse().getStatus();
        assertEquals(400, status);
    }

    @Test
    public void createUserWithInvalidEmailShouldReturnError() throws Exception{
        String uri = "/user";
        UserForm form= new UserForm("Manuel","Gimenez","manuel","Manuel");
        MvcResult mvcResult = mvc.perform(
                MockMvcRequestBuilders.post(uri)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(mapToJson(form))
        ).andReturn();
        int status = mvcResult.getResponse().getStatus();
        assertEquals(400, status);
    }

    @Test
    public void verifyUserShouldReturnOk() throws Exception {
        String uri = "/user/verify_user";
        User user = new User("Manuel","Gimenez","manuel@mail.com","Manuel", false, UserType.USER);
        user.setUserVerificationToken("1");
        userRepository.save(user);
        UserVerificationForm form = new UserVerificationForm("1");
        MvcResult mvcResult = mvc.perform(
                MockMvcRequestBuilders.post(uri)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(mapToJson(form))
        ).andReturn();
        int statusCode = mvcResult.getResponse().getStatus();
        assertEquals(200, statusCode);
        user = userRepository.findUserByEmail("manuel@mail.com").get();
        assertTrue(user.isVerified());
    }

    @Test
    public void verifyUserShouldReturnNotFound() throws Exception {
        String uri = "/user/verify_user";
        User user = new User("Manuel","Gimenez","manuel@mail.com","Manuel", false, UserType.USER);
        user.setUserVerificationToken("2");
        userRepository.save(user);
        UserVerificationForm form = new UserVerificationForm("1");
        MvcResult mvcResult = mvc.perform(
                MockMvcRequestBuilders.post(uri)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(mapToJson(form))
        ).andReturn();
        int statusCode = mvcResult.getResponse().getStatus();
        assertEquals(404, statusCode);
    }

    @Test
    @WithMockUser(username = "carlos@mail.com")
    public void getCurrentUserShouldReturnCurrentFullUserDTO() throws Exception {
        data.user1.setUserVerificationToken("1234");
        userRepository.save(data.user1);

        String uri = "/user/me";

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri)
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        int statusCode = mvcResult.getResponse().getStatus();
        assertEquals(200, statusCode);

        String status = mvcResult.getResponse().getContentAsString();
        FullUserDTO userDTO = super.mapFromJson(status, FullUserDTO.class);

        assertEquals(userDTO.getId(), data.user1.getId());
        assertEquals(userDTO.getEmail(), data.user1.getEmail());
        assertEquals(userDTO.getName(), data.user1.getName());
        assertEquals(userDTO.getSurname(), data.user1.getSurname());
    }

    @Test
    @WithMockUser(username = "carlitos@mail.com")
    public void getCurrentUserThatIsNotRegisteredShouldReturnNotFound() throws Exception {

        String uri = "/user/me";

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri)
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        int status = mvcResult.getResponse().getStatus();
        assertEquals(404, status);
    }

    @Test
    @WithMockUser("USER")
    public void getUserByIdShouldReturnUser() throws Exception {
       int userId = data.user1.getId();
        String uri = "/user/"+userId;

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri)
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        int statusCode = mvcResult.getResponse().getStatus();
        assertEquals(200, statusCode);

        String status = mvcResult.getResponse().getContentAsString();
        FullUserDTO userDTO = super.mapFromJson(status, FullUserDTO.class);

        assertEquals(userDTO.getId(), data.user1.getId());
        assertEquals(userDTO.getEmail(), data.user1.getEmail());
        assertEquals(userDTO.getName(), data.user1.getName());
        assertEquals(userDTO.getSurname(), data.user1.getSurname());
    }

    @Test
    @WithMockUser("USER")
    public void getUserByIdShouldFailIfNotFound() throws Exception {
        String uri = "/user/-1";
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri)
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        int statusCode = mvcResult.getResponse().getStatus();
        assertEquals(404, statusCode);
    }

    @Test
    @WithMockUser(username = "carlos@mail.com")
    public void getUserGroupsWithTrueParameterShouldReturnUserGroups() throws Exception {
        String uri = "/user";

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri + "/onlyMyGroups=true")
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        int statusCode = mvcResult.getResponse().getStatus();
        assertEquals(200, statusCode);

        String status = mvcResult.getResponse().getContentAsString();
        List<StudyGroupDTO> groupsDTOs = super.mapFromJson(status, new TypeReference<List<StudyGroupDTO>>(){});

        assertEquals(groupsDTOs.get(0).getName(), data.group1.getName());
    }

    @Test
    @WithMockUser(username = "tolkien@gmail.com")
    public void getUserWithNoGroupsWithTrueParameterShouldReturnEmptyList() throws Exception {
        String uri = "/user";

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri + "/onlyMyGroups=true")
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        int statusCode = mvcResult.getResponse().getStatus();
        assertEquals(200, statusCode);

        String status = mvcResult.getResponse().getContentAsString();
        List<StudyGroupDTO> groupsDTOs = super.mapFromJson(status, new TypeReference<List<StudyGroupDTO>>(){});

        assertEquals(groupsDTOs.size(), 0);
    }

    @Test
    @WithMockUser(username = "carl@mail.com")
    public void getNonExistingUserGroupsWithTrueParameterShouldReturnNotFound() throws Exception {
        String uri = "/user";

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri + "/onlyMyGroups=true")
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        int statusCode = mvcResult.getResponse().getStatus();
        assertEquals(404, statusCode);
    }

    @Test
    @WithMockUser(username = "tolkien@gmail.com")
    public void getUserGroupsWithFalseParameterShouldReturnAllGroups() throws Exception {
        String uri = "/user";

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri + "/onlyMyGroups=false")
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        int statusCode = mvcResult.getResponse().getStatus();
        assertEquals(200, statusCode);

        String status = mvcResult.getResponse().getContentAsString();
        List<StudyGroupDTO> groupsDTOs = super.mapFromJson(status, new TypeReference<List<StudyGroupDTO>>(){});

        assertEquals(groupsDTOs.get(0).getName(), data.group1.getName());
        assertEquals(groupsDTOs.get(1).getName(), data.group2.getName());
    }

    @Test
    @WithMockUser(username = "carlos@mail.com")
    public void getUserGroupsWithEmptyParameterShouldReturnAllGroups() throws Exception {
        String uri = "/user";

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri + "/onlyMyGroups=")
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        int statusCode = mvcResult.getResponse().getStatus();
        assertEquals(200, statusCode);

        String status = mvcResult.getResponse().getContentAsString();
        List<StudyGroupDTO> groupsDTOs = super.mapFromJson(status, new TypeReference<List<StudyGroupDTO>>(){});

        assertEquals(groupsDTOs.get(0).getName(), data.group1.getName());
        assertEquals(groupsDTOs.get(1).getName(), data.group2.getName());
    }

    @Test
    @WithMockUser(username = "carl@mail.com")
    public void getNonExistingUserGroupsWithEmptyParameterShouldReturnNotFound() throws Exception {
        String uri = "/user";

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri + "/onlyMyGroups=")
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        int statusCode = mvcResult.getResponse().getStatus();
        assertEquals(404, statusCode);
    }
}
