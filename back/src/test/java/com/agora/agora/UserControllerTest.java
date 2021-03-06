package com.agora.agora;

import com.agora.agora.controller.StudyGroupController;
import com.agora.agora.model.*;
import com.agora.agora.model.dto.FullUserDTO;
import com.agora.agora.model.dto.NotificationDTO;
import com.agora.agora.model.dto.PostDTO;
import com.agora.agora.model.dto.StudyGroupDTO;
import com.agora.agora.model.form.EditUserForm;
import com.agora.agora.model.form.PostForm;
import com.agora.agora.model.form.UserForm;
import com.agora.agora.model.form.UserVerificationForm;
import com.agora.agora.model.type.LinkType;
import com.agora.agora.model.type.NotificationType;
import com.agora.agora.model.type.UserType;
import com.agora.agora.repository.*;
import com.fasterxml.jackson.core.type.TypeReference;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.request.RequestPostProcessor;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.*;

import javax.swing.*;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
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
    private ContactLinkRepository contactLinkRepository;

    @Autowired
    private NewPostNotificationRepository newPostNotificationRepository;

    @Autowired
    private NewMemberNotificationRepository newMemberNotificationRepository;

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
        NewMemberNotification notification2;
        StudyGroupUser group1User1;
        StudyGroupUser group2User2;
        StudyGroupUser group1User4;
        ContactLink contactLink1;

        void setup() {
            contactLink1 = new ContactLink(LinkType.EMAIL, "jrr@mail.com");
            List<ContactLink> contactLinks = new ArrayList<>();
            contactLinks.add(contactLink1);
            contactLinkRepository.save(contactLink1);

            user1 = new User("Carlos", "Gimenez", "carlos@mail.com", "Carlos123", false, UserType.USER);
            user2 = new User("Frank", "Herbert", "herbert@gmail.com", "Frankherbert2021", false, UserType.USER);
            user3 = new User("J. R. R.", "Tolkien", "tolkien@gmail.com", "Jrrtolkien2021", false, UserType.USER);
            user4 = new User("Frank", "Gimenez", "frankgimenez@gmail.com", "Frankherbert2021", false, UserType.USER);


            user3.setContactLinks(contactLinks);
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

            group1User1 = new StudyGroupUser(user1, group1);
            group2User2 = new StudyGroupUser(user2, group2);
            group1User4 = new StudyGroupUser(user4, group1);
            studyGroupUsersRepository.save(group1User1);
            studyGroupUsersRepository.save(group2User2);
            studyGroupUsersRepository.save(group1User4);

            post = new Post("...", group1, user2, LocalDateTime.now());
            postRepository.save(post);

            post1 = new Post("LOTR 2 is out", group1, user1, LocalDateTime.of(2021, 9, 23, 3, 15));
            post2 = new Post("LOTR 2 is great", group1, user1, LocalDateTime.of(2021, 9, 24, 12, 3));
            postRepository.save(post1);
            postRepository.save(post2);

            notification1 = new NewPostNotification(user1, false, LocalDate.of(2021, 9, 23), post1, group1);
            newPostNotificationRepository.save(notification1);
            notification2 = new NewMemberNotification(user2, false, LocalDate.of(2021, 9, 23),user1, group1);
        }

        void rollback() {
            newPostNotificationRepository.deleteAll();
            newMemberNotificationRepository.deleteAll();
            postRepository.deleteAll();
            studyGroupLabelRepository.deleteAll();
            studyGroupUsersRepository.deleteAll();
            labelRepository.deleteAll();
            groupRepository.deleteAll();
            userRepository.deleteAll();
            contactLinkRepository.deleteAll();
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

    @Test
    @WithMockUser("herbert@gmail.com")
    public void getUserNotificationWhenNewMemberIsAddedShouldReturnOkAndNotification() throws Exception {
        String uri = "/studyGroup";

        MvcResult mvcResult = mvc.perform(
                MockMvcRequestBuilders.post(uri + "/" + data.group2.getId() + "/" + data.user3.getId())
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
        ).andReturn();
        int statusPostCode = mvcResult.getResponse().getStatus();
        assertEquals(200, statusPostCode);

        String uri2 = "/user/notification/me";
        MvcResult mvcGETResult = mvc.perform(MockMvcRequestBuilders.get(uri2)
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        int statusCode = mvcGETResult.getResponse().getStatus();
        assertEquals(200, statusCode);

        String status = mvcGETResult.getResponse().getContentAsString();
        List<NotificationDTO> notificationDTO = super.mapFromJson(status, new TypeReference<List<NotificationDTO>>(){});

        assertEquals(NotificationType.NEW_MEMBER_NOTIFICATION, notificationDTO.get(0).getNotificationType());
        assertEquals(data.group2.getId(), notificationDTO.get(0).getStudyGroupId());
    }

    @Test
    public void getUserNotificationWhenAnotherMemberAddsAPostShouldReturnOkAndNotification() throws Exception {
        StudyGroup studyGroup = data.group1;
        String uri = "/studyGroup/" + studyGroup.getId() + "/forum";
        PostForm groupForm = new PostForm("Aguante Tolkien", LocalDateTime.now());
        MvcResult mvcResult = mvc.perform(
                MockMvcRequestBuilders.post(uri)
                        .with(user("carlos@mail.com"))
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(mapToJson(groupForm))
        ).andReturn();
        int status = mvcResult.getResponse().getStatus();
        assertEquals(201, status);

        String uri2 = "/user/notification/me";
        MvcResult mvcGETResult = mvc.perform(MockMvcRequestBuilders.get(uri2)
                        .with(user("frankgimenez@gmail.com"))
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        int statusCode = mvcGETResult.getResponse().getStatus();
        assertEquals(200, statusCode);

        String getStatus = mvcGETResult.getResponse().getContentAsString();
        List<NotificationDTO> notificationDTO = super.mapFromJson(getStatus, new TypeReference<List<NotificationDTO>>(){});

        assertEquals(NotificationType.NEW_POST_NOTIFICATION, notificationDTO.get(0).getNotificationType());
        assertEquals(data.group1.getId(), notificationDTO.get(0).getStudyGroupId());
    }

    @Test
    public void getUserNotificationWhenTheSameUserAddsAPostShouldReturnNoNotification() throws Exception {
        StudyGroup studyGroup = data.group1;
        String uri = "/studyGroup/" + studyGroup.getId() + "/forum";
        PostForm groupForm = new PostForm("Aguante Tolkien", LocalDateTime.now());
        MvcResult mvcResult = mvc.perform(
                MockMvcRequestBuilders.post(uri)
                        .with(user("frankgimenez@gmail.com"))
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(mapToJson(groupForm))
        ).andReturn();
        int status = mvcResult.getResponse().getStatus();
        assertEquals(201, status);

        String uri2 = "/user/notification/me";
        MvcResult mvcGETResult = mvc.perform(MockMvcRequestBuilders.get(uri2)
                .with(user("frankgimenez@gmail.com"))
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        int statusCode = mvcGETResult.getResponse().getStatus();
        assertEquals(200, statusCode);

        String getStatus = mvcGETResult.getResponse().getContentAsString();
        List<NotificationDTO> notificationDTO = super.mapFromJson(getStatus, new TypeReference<List<NotificationDTO>>(){});

        assertEquals(0, notificationDTO.size());
    }

    @Test
    @WithMockUser("tolkien@gmail.com")
    public void getUserNotificationWhenTheSameUserIsAddedToAGroupShouldReturnNoNotifications() throws Exception {
        String uri = "/studyGroup";

        MvcResult mvcResult = mvc.perform(
                MockMvcRequestBuilders.post(uri + "/" + data.group2.getId() + "/" + data.user3.getId())
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
        ).andReturn();
        int statusPostCode = mvcResult.getResponse().getStatus();
        assertEquals(200, statusPostCode);

        String uri2 = "/user/notification/me";
        MvcResult mvcGETResult = mvc.perform(MockMvcRequestBuilders.get(uri2)
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        int statusCode = mvcGETResult.getResponse().getStatus();
        assertEquals(200, statusCode);

        String getStatus = mvcGETResult.getResponse().getContentAsString();
        List<NotificationDTO> notificationDTO = super.mapFromJson(getStatus, new TypeReference<List<NotificationDTO>>(){});

        assertEquals(0, notificationDTO.size());
    }

    @Test
    @WithMockUser("tolkien@gmail.com")
    public void editUserReturnsOk() throws Exception {
        String uri = "/user/me";
        List<ContactLink> contactLinks = new ArrayList<>();
        EditUserForm form = new EditUserForm("Manuel","Gimenez","Manuel123", contactLinks);
        MvcResult mvcResult = mvc.perform(
                MockMvcRequestBuilders.post(uri)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(mapToJson(form))
        ).andReturn();
        int statusCode = mvcResult.getResponse().getStatus();
        assertEquals(200, statusCode);
    }

    @Test
    @WithMockUser("USER")
    public void editUserWithNotExistingUserReturnsNotFound() throws Exception {
        String uri = "/user/me";
        List<ContactLink> contactLinks = new ArrayList<>();
        EditUserForm form = new EditUserForm("Manuel","Gimenez","Manuel123", contactLinks);
        MvcResult mvcResult = mvc.perform(
                MockMvcRequestBuilders.post(uri)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(mapToJson(form))
        ).andReturn();
        int statusCode = mvcResult.getResponse().getStatus();
        assertEquals(404, statusCode);
    }

    @Test
    @Transactional
    @WithMockUser("tolkien@gmail.com")
    public void editUserEditsCorrectly() throws Exception {
        String uri = "/user/me";
        List<ContactLink> contactLinks = new ArrayList<>();
        ContactLink contactLink = data.contactLink1;
        contactLink.setLink("manuel@mail.com");
        contactLinks.add(contactLink);
        EditUserForm form = new EditUserForm("Manuel","Gimenez","Manuel123", contactLinks);
        mvc.perform(
                MockMvcRequestBuilders.post(uri)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(mapToJson(form))
        ).andReturn();
        Optional<User> userOptional = userRepository.findById(data.user3.getId());
        User user = userOptional.get();
        assertEquals(form.getName(), user.getName());
        assertEquals(form.getSurname(), user.getSurname());
        assertEquals(contactLinks.get(0).getId(), user.getContactLinks().get(0).getId());
    }

    @Test
    @Transactional
    @WithMockUser("tolkien@gmail.com")
    public void editUserEditsCorrectlyWithoutPassword() throws Exception {
        String uri = "/user/me";
        List<ContactLink> contactLinks = new ArrayList<>();
        ContactLink contactLink = data.contactLink1;
        contactLink.setLink("manuel@mail.com");
        contactLinks.add(contactLink);
        EditUserForm form = new EditUserForm("Manuel","Gimenez",null, contactLinks);
        mvc.perform(
                MockMvcRequestBuilders.post(uri)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(mapToJson(form))
        ).andReturn();
        Optional<User> userOptional = userRepository.findById(data.user3.getId());
        User user = userOptional.get();
        assertEquals(form.getName(), user.getName());
        assertEquals(form.getSurname(), user.getSurname());
        assertEquals(contactLinks.get(0).getId(), user.getContactLinks().get(0).getId());
    }

    @Test
    @WithMockUser(username = "herbert@gmail.com")
    public void whenDeletingUserShouldDeleteAllInformation() throws Exception {
        String uri = "/user/me";

        MvcResult mvcResult = mvc.perform(
                MockMvcRequestBuilders.delete(uri)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
        ).andReturn();

        assertFalse(userRepository.findById(data.user2.getId()).isPresent());
        assertFalse(studyGroupUsersRepository.findById(data.group2User2.getId()).isPresent());
        assertFalse(groupRepository.findById(data.group2.getId()).isPresent());
        assertFalse(postRepository.findById(data.post.getId()).isPresent());
        assertFalse(newMemberNotificationRepository.findById(data.notification2.getId()).isPresent());
    }

    @Test
    @WithMockUser("carlos@mail.com")
    public void whenDeletingCreatorUserGroupCreatorShouldPassToSecondUser() throws Exception {
        String uri = "/user/me";

        MvcResult mvcResult = mvc.perform(
                MockMvcRequestBuilders.delete(uri)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
        ).andReturn();

        assertTrue(groupRepository.findById(data.group1.getId()).isPresent());
        assertEquals(data.user4.getId(), groupRepository.findById(data.group1.getId()).get().getCreator().getId());
    }
}


