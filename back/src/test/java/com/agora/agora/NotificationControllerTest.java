package com.agora.agora;

import com.agora.agora.model.*;
import com.agora.agora.model.type.UserType;
import com.agora.agora.repository.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.Assert.*;

public class NotificationControllerTest extends AbstractTest{

    @Autowired
    private UserInviteNotificationRepository userInviteNotificationRepository;

    @Autowired
    private NewMemberNotificationRepository newMemberNotificationRepository;

    @Autowired
    private NewPostNotificationRepository newPostNotificationRepository;

    @Autowired
    private GroupInviteNotificationRepository groupInviteNotificationRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StudyGroupRepository studyGroupRepository;

    @Autowired
    private StudyGroupUsersRepository studyGroupUsersRepository;

    @Autowired
    private PostRepository postRepository;

    private Data data = new Data();

    private class Data{
        User user1;
        User user2;
        NewMemberNotification newMemberNotification1;
        NewMemberNotification newMemberNotification2;
        NewPostNotification newPostNotification1;
        NewPostNotification newPostNotification2;
        GroupInviteNotification groupInviteNotification1;
        GroupInviteNotification groupInviteNotification2;
        StudyGroup group1;
        Post post;
        Post post1;

        void setup(){
            user1 = new User("Carlos", "Gimenez", "carlos@mail.com", "Carlos123", false, UserType.USER);
            user2 = new User("Frank", "Herbert", "herbert@gmail.com", "Frankherbert2021", false, UserType.USER);
            userRepository.save(user1);
            userRepository.save(user2);

            group1 = new StudyGroup("Lord of the rings", "...", user1, LocalDate.of(2021, 8, 17));
            studyGroupRepository.save(group1);

            studyGroupUsersRepository.save(new StudyGroupUser(user1, group1));

            post = new Post("...", group1, user2, LocalDateTime.now());
            post1 = new Post("LOTR 2 is out", group1, user1, LocalDateTime.of(2021, 9, 23, 3, 15));
            postRepository.save(post);
            postRepository.save(post1);

            newMemberNotification1 = new NewMemberNotification(user1,false, LocalDate.of(2021, 8, 16),user2,group1);
            newMemberNotification2 = new NewMemberNotification(user2,false, LocalDate.of(2020, 6, 4),user1,group1);
            newMemberNotificationRepository.save(newMemberNotification1);
            newMemberNotificationRepository.save(newMemberNotification2);

            newPostNotification1 = new NewPostNotification(user1,false,LocalDate.of(2021, 8, 16),post,group1);
            newPostNotification2 = new NewPostNotification(user2, false, LocalDate.of(2021, 8, 14),post1,group1);
            newPostNotificationRepository.save(newPostNotification1);
            newPostNotificationRepository.save(newPostNotification2);

            groupInviteNotification1 = new GroupInviteNotification(user1,false,LocalDate.of(2021, 8, 14),group1);
            groupInviteNotification2 = new GroupInviteNotification(user2,false,LocalDate.of(2021, 8, 14),group1);
            groupInviteNotificationRepository.save(groupInviteNotification1);
            groupInviteNotificationRepository.save(groupInviteNotification2);
        }

        void rollback() {
            studyGroupUsersRepository.deleteAll();
            userInviteNotificationRepository.deleteAll();
            newMemberNotificationRepository.deleteAll();
            newPostNotificationRepository.deleteAll();
            groupInviteNotificationRepository.deleteAll();
            postRepository.deleteAll();
            studyGroupRepository.deleteAll();
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
    @WithMockUser("carlos@mail.com")
    public void whenReadingNewMemberNotificationForCorrectUserShouldRead() throws Exception {
        String uri = "/notification/" + data.newMemberNotification1.getId() + "/read";
        MvcResult mvcResult = mvc.perform(
                MockMvcRequestBuilders.post(uri)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        int status = mvcResult.getResponse().getStatus();
        assertEquals(200,status);
        boolean isRead = newMemberNotificationRepository.findById(data.newMemberNotification1.getId()).get().isRead();
        assertTrue(isRead);
    }

    @Test
    @WithMockUser("herbert@gmail.com")
    public void whenAccessingNotificationThatIsNotYoursShouldReturnForbidden() throws Exception{
        String uri = "/notification/" + data.newMemberNotification1.getId() + "/read";
        MvcResult mvcResult = mvc.perform(
                MockMvcRequestBuilders.post(uri)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        int status = mvcResult.getResponse().getStatus();
        assertEquals(403,status);
        boolean isRead = newMemberNotificationRepository.findById(data.newMemberNotification1.getId()).get().isRead();
        assertFalse(isRead);
    }

    @Test
    @WithMockUser("carlos@mail.com")
    public void whenAccessingNotExistingNotificationShouldFail() throws Exception{
        String uri = "/notification/" + -1 + "/read";
        MvcResult mvcResult = mvc.perform(
                MockMvcRequestBuilders.post(uri)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        int status = mvcResult.getResponse().getStatus();
        assertEquals(404,status);
    }

    @Test
    @WithMockUser("noExisting@mail.com")
    public void whenAccessingWithNonExistingUserShouldFail() throws Exception{
        String uri = "/notification/" + data.newMemberNotification1.getId() + "/read";
        MvcResult mvcResult = mvc.perform(
                MockMvcRequestBuilders.post(uri)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        int status = mvcResult.getResponse().getStatus();
        assertEquals(404,status);
        boolean isRead = newMemberNotificationRepository.findById(data.newMemberNotification1.getId()).get().isRead();
        assertFalse(isRead);
    }

    @Test
    @WithMockUser("herbert@gmail.com")
    public void senderNotInStudyGroupNotAllow() throws Exception {
        String uri = "/studyGroup/" + data.group1.getId() + "/invite/" + data.user2.getId();
        MvcResult mvcResult = mvc.perform(
                MockMvcRequestBuilders.post(uri)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
        ).andReturn();
        int status = mvcResult.getResponse().getStatus();
        assertEquals(403, status);
    }

    @Test
    @WithMockUser("carlos@mail.com")
    public void sendNonExistingUserStudyGroupInviteNotificationShouldFail() throws Exception {
        String uri = "/studyGroup/"+ data.group1.getId() + "/invite/-1";
        MvcResult mvcResult = mvc.perform(
                MockMvcRequestBuilders.post(uri)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
        ).andReturn();
        int status = mvcResult.getResponse().getStatus();
        assertEquals(404, status);
    }

    @Test
    @WithMockUser("carlos@mail.com")
    public void sendUserNonExistingStudyGroupInviteNotificationShouldFail() throws Exception {
        String uri = "/studyGroup/-1/invite/" + data.user2.getId();
        MvcResult mvcResult = mvc.perform(
                MockMvcRequestBuilders.post(uri)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
        ).andReturn();
        int status = mvcResult.getResponse().getStatus();
        assertEquals(404, status);
    }

    @Test
    @WithMockUser("carlos@mail.com")
    public void sendUserStudyGroupInviteNotificationShouldSucceed() throws Exception {
        String uri = "/studyGroup/" + data.group1.getId() + "/invite/" + data.user2.getId();
        MvcResult mvcResult = mvc.perform(
                MockMvcRequestBuilders.post(uri)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
        ).andReturn();
        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
        assertEquals(1, userInviteNotificationRepository.findAllByUserId(data.user2.getId()).size());
    }

    @Test
    @WithMockUser("carlos@mail.com")
    public void whenReadingNewPostNotificationForCorrectUserShouldRead() throws Exception {
        String uri = "/notification/" + data.newPostNotification1.getId() + "/read";
        MvcResult mvcResult = mvc.perform(
                MockMvcRequestBuilders.post(uri)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        int status = mvcResult.getResponse().getStatus();
        assertEquals(200,status);
        boolean isRead = newPostNotificationRepository.findById(data.newPostNotification1.getId()).get().isRead();
        assertTrue(isRead);
    }

    @Test
    @WithMockUser("carlos@mail.com")
    public void whenReadingGroupInviteNotificationForCorrectUserShouldRead() throws Exception {
        String uri = "/notification/" + data.groupInviteNotification1.getId() + "/read";
        MvcResult mvcResult = mvc.perform(
                MockMvcRequestBuilders.post(uri)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        int status = mvcResult.getResponse().getStatus();
        assertEquals(200,status);
        boolean isRead = groupInviteNotificationRepository.findById(data.groupInviteNotification1.getId()).get().isRead();
        assertTrue(isRead);
    }
}
