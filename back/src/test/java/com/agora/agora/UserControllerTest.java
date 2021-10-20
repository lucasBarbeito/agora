package com.agora.agora;

import com.agora.agora.controller.StudyGroupController;
import com.agora.agora.model.*;
import com.agora.agora.model.dto.FullUserDTO;
import com.agora.agora.model.dto.NotificationDTO;
import com.agora.agora.model.dto.PostDTO;
import com.agora.agora.model.dto.StudyGroupDTO;
import com.agora.agora.model.form.UserForm;
import com.agora.agora.model.form.UserVerificationForm;
import com.agora.agora.model.type.UserType;
import com.agora.agora.repository.*;
import com.fasterxml.jackson.core.type.TypeReference;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import javax.swing.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.CoreMatchers.hasItems;
import static org.junit.Assert.*;

public class UserControllerTest extends AbstractTest{

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

    @Autowired
    private StudyGroupLabelRepository studyGroupLabelRepository;

    @Autowired
    private LabelRepository labelRepository;

    @Autowired
    private NewPostNotificationRepository newPostNotificationRepository;

    private Data data = new Data();

    // Storing all the info we'll load into the DB here.
    private class Data {
        User user1;
        User user2;
        User user3;
        User user4;
        StudyGroup group1;
        StudyGroup group2;
        Post post;
        Post post1;
        Post post2;
        Label label1;
        Label label2;
        NewPostNotification notification1;

        void setup() {
            user1 = new User("Carlos", "Gimenez", "carlos@mail.com", "Carlos123", false, UserType.USER);
            user2 = new User("Frank", "Herbert", "herbert@gmail.com", "Frankherbert2021", false, UserType.USER);
            user3 = new User("J. R. R.", "Tolkien", "tolkien@gmail.com", "Jrrtolkien2021", false, UserType.USER);
            user4 = new User("Frank", "Gimenez", "frankgimenez@gmail.com", "Frankherbert2021", false, UserType.USER);
            userRepository.save(user1);
            userRepository.save(user2);
            userRepository.save(user3);
            userRepository.save(user4);

            label1 = new Label("SciFi");
            label2 = new Label("History");
            labelRepository.save(label1);
            labelRepository.save(label2);

            group1 = new StudyGroup("Lord of the rings", "...", user1, LocalDate.of(2021, 8, 17));
            group2 = new StudyGroup("Dune", "....", user2, LocalDate.of(2021, 8, 16));
            groupRepository.save(group1);
            groupRepository.save(group2);

            StudyGroupLabel l1g1 = new StudyGroupLabel(label1, group1);
            StudyGroupLabel l2g2 = new StudyGroupLabel(label2, group2);
            studyGroupLabelRepository.save(l1g1);
            studyGroupLabelRepository.save(l2g2);

            StudyGroupUser group1User1 = new StudyGroupUser(user1, group1);
            StudyGroupUser group2User2 = new StudyGroupUser(user2, group2);
            studyGroupUsersRepository.save(group1User1);
            studyGroupUsersRepository.save(group2User2);

            post = new Post("...", group1, user2, LocalDateTime.now());
            postRepository.save(post);

            post1 = new Post("LOTR 2 is out", group1, user1, LocalDateTime.of(2021, 9, 23, 3, 15));
            post2 = new Post("LOTR 2 is great", group1, user1, LocalDateTime.of(2021, 9, 24, 12, 3));
            postRepository.save(post1);
            postRepository.save(post2);

            notification1 = new NewPostNotification(0, user1, false, LocalDate.of(2021, 9, 23), post1, group1);
            newPostNotificationRepository.save(notification1);
        }

        void rollback() {
            newPostNotificationRepository.deleteAll();
            postRepository.deleteAll();
            studyGroupLabelRepository.deleteAll();
            studyGroupUsersRepository.deleteAll();
            labelRepository.deleteAll();
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
        assertEquals(userDTO.getUserGroups().get(0).getId(), data.group1.getId());
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
        assertEquals(userDTO.getUserGroups().get(0).getId(), data.group1.getId());
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
    @WithMockUser("USER")
    public void getAllUsersShouldReturnOk() throws Exception {
        String uri = "/user";
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri)
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        int statusCode = mvcResult.getResponse().getStatus();
        assertEquals(200, statusCode);
    }

    @Test
    @WithMockUser("USER")
    public void getAllUsersShouldReturnAmountExpected() throws Exception {
        String uri = "/user";
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri)
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        String status = mvcResult.getResponse().getContentAsString();
        FullUserDTO[] fullUserDTO = super.mapFromJson(status, FullUserDTO[].class);
        assertEquals(4,fullUserDTO.length);
        assertEquals(fullUserDTO[0].getUserGroups().get(0).getId(), data.group1.getId());
    }

    @Test
    @WithMockUser("USER")
    public void getAllUsersShouldReturnExpectedValues() throws Exception {
        String uri = "/user";
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri)
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        String status = mvcResult.getResponse().getContentAsString();
        FullUserDTO[] fullUserDTO = super.mapFromJson(status, FullUserDTO[].class);

        List<FullUserDTO> fullUserDTOList = Arrays.stream(fullUserDTO).collect(Collectors.toList());
        List<Integer> fullUserDTOListExpectedNames = new ArrayList<>();
        fullUserDTOListExpectedNames.add(data.user1.getId());
        fullUserDTOListExpectedNames.add(data.user2.getId());
        fullUserDTOListExpectedNames.add(data.user3.getId());
        fullUserDTOListExpectedNames.add(data.user4.getId());

        for (FullUserDTO userDTO : fullUserDTOList) {
            assertThat(fullUserDTOListExpectedNames, hasItems(userDTO.getId()));
        }
    }

    @Test
    @WithMockUser("USER")
    public void getAllUsersWithNameShouldReturnExpectedValues() throws Exception {
        String uri = "/user";
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri)
                .param("name", "Frank")
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        String status = mvcResult.getResponse().getContentAsString();
        FullUserDTO[] fullUserDTO = super.mapFromJson(status, FullUserDTO[].class);

        List<FullUserDTO> fullUserDTOList = Arrays.stream(fullUserDTO).collect(Collectors.toList());
        List<Integer> fullUserDTOListExpectedNames = new ArrayList<>();
        fullUserDTOListExpectedNames.add(data.user2.getId());
        fullUserDTOListExpectedNames.add(data.user4.getId());

        for (FullUserDTO userDTO : fullUserDTOList) {
            assertThat(fullUserDTOListExpectedNames, hasItems(userDTO.getId()));
        }
    }

    @Test
    @WithMockUser("USER")
    public void getAllUsersWithSurnameShouldReturnExpectedValues() throws Exception {
        String uri = "/user";
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri)
                .param("name", "Gimenez")
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        String status = mvcResult.getResponse().getContentAsString();
        FullUserDTO[] fullUserDTO = super.mapFromJson(status, FullUserDTO[].class);

        List<FullUserDTO> fullUserDTOList = Arrays.stream(fullUserDTO).collect(Collectors.toList());
        List<Integer> fullUserDTOListExpectedNames = new ArrayList<>();
        fullUserDTOListExpectedNames.add(data.user1.getId());
        fullUserDTOListExpectedNames.add(data.user4.getId());

        for (FullUserDTO userDTO : fullUserDTOList) {
            assertThat(fullUserDTOListExpectedNames, hasItems(userDTO.getId()));
        }
    }

    @Test
    @WithMockUser("USER")
    public void getAllUsersWithNameAndSurnameShouldReturnExpectedValues() throws Exception {
        String uri = "/user";
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri)
                .param("name", "J. R. R. Tolkien")
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        String status = mvcResult.getResponse().getContentAsString();
        FullUserDTO[] fullUserDTO = super.mapFromJson(status, FullUserDTO[].class);

        List<FullUserDTO> fullUserDTOList = Arrays.stream(fullUserDTO).collect(Collectors.toList());
        List<Integer> fullUserDTOListExpectedNames = new ArrayList<>();
        fullUserDTOListExpectedNames.add(data.user3.getId());

        for (FullUserDTO userDTO : fullUserDTOList) {
            assertThat(fullUserDTOListExpectedNames, hasItems(userDTO.getId()));
        }
    }

    @Test
    @WithMockUser("carlos@mail.com")
    public void getUserNotificationsShouldReturnOk() throws Exception {
        String uri = "/user/notification/me";
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri)
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        int statusCode = mvcResult.getResponse().getStatus();
        assertEquals(200, statusCode);
    }

    @Test
    @WithMockUser("carlos@mail.com")
    public void getUserNotificationsShouldReturnNotifications() throws Exception {
        String uri = "/user/notification/me";
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri)
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        int statusCode = mvcResult.getResponse().getStatus();
        assertEquals(200, statusCode);

        String status = mvcResult.getResponse().getContentAsString();
        List<NotificationDTO> notificationDTO = super.mapFromJson(status, new TypeReference<List<NotificationDTO>>(){});

        assertEquals(data.notification1.getUser().getId(), notificationDTO.get(0).getUserRecipientId());
        assertEquals(data.notification1.getGroup().getId(), notificationDTO.get(0).getStudyGroupId());
    }

    @Test
    @WithMockUser("carl@mail.com")
    public void getNonExistentUserNotificationsShouldReturnNotFound() throws Exception {
        String uri = "/user/notification/me";
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri)
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        int statusCode = mvcResult.getResponse().getStatus();
        assertEquals(404, statusCode);
    }

    @Test
    @WithMockUser("tolkien@mail.com")
    public void getUserWithNoNotificationsShouldReturnNotFound() throws Exception {
        String uri = "/user/notification/me";
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri)
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        int statusCode = mvcResult.getResponse().getStatus();
        assertEquals(404, statusCode);
    }
}
