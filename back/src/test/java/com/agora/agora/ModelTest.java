package com.agora.agora;

import com.agora.agora.model.*;
import com.agora.agora.model.form.EditUserForm;
import com.agora.agora.model.form.LabelForm;
import com.agora.agora.model.type.LinkType;
import com.agora.agora.model.type.NotificationType;
import com.agora.agora.model.type.UserType;
import com.agora.agora.repository.*;
import com.agora.agora.service.LabelService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;

public class ModelTest extends AbstractTest{

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

    private ModelTest.Data data = new ModelTest.Data();

    // Storing all the info we'll load into the DB here.
    private class Data {
        User user1;
        User user2;
        User user3;
        StudyGroup group1;
        StudyGroup group2;
        StudyGroup group3;
        StudyGroup group4;
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
            userRepository.save(user1);
            userRepository.save(user2);
            userRepository.save(user3);

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

            StudyGroupUser group1User1 = new StudyGroupUser(user1, group1);
            StudyGroupUser group2User2 = new StudyGroupUser(user2, group2);
            studyGroupUsersRepository.save(group1User1);
            studyGroupUsersRepository.save(group2User2);

            post = new Post("...", group1, user2, LocalDateTime.now());
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
    public void studyGroupUserModelTest(){
        StudyGroupUser model = new StudyGroupUser(data.user1, data.group1);
        model.setId(1);
        model.setUser(data.user2);
        model.setStudyGroup(data.group2);
        assertEquals(data.user2, model.getUser());
        assertEquals(data.user2, model.getUser());
        assertEquals(data.group2, model.getStudyGroup());
    }

    @Test
    public void studyGroupLabelModelTest(){
        StudyGroupLabel model = new StudyGroupLabel(data.label1, data.group1);
        model.setLabel(data.label2);
        model.setStudyGroup(data.group2);
        assertEquals(data.label2, model.getLabel());
        assertEquals(data.group2, model.getStudyGroup());
    }

    @Test
    public void studyGroupModelTest(){
        StudyGroup model = data.group1;
        model.setId(1);
        model.setCreationDate(LocalDate.of(2021, 8, 17));
        model.setId(Integer.valueOf(1));
        model.setUsers(Arrays.asList(new StudyGroupUser(data.user1, data.group1)));
        assertEquals(data.user1, model.getUsers().get(0).getUser());
        assertEquals(LocalDate.of(2021, 8, 17), model.getCreationDate());
        assertEquals(1, model.getId());
        assertEquals(1, model.getId());

    }

    @Test
    public void postModelTest(){
        Post model = data.post;
        model.setCreationDateTime(LocalDateTime.of(2021, 8, 17, 12, 3));
        model.setCreator(data.user2);
        model.setContent("...");
        assertEquals(data.user2, model.getCreator());
        assertEquals(LocalDateTime.of(2021, 8, 17, 12, 3), model.getCreationDateTime());
        assertEquals("...", model.getContent());
    }

    @Test
    public void notificationModelTest(){
        Notification model = new Notification(data.user1, true, LocalDate.now());
        model.setId(1);
        model.setUser(data.user2);
        model.setCreationDate(LocalDate.of(2021, 8, 17));
        assertEquals(data.user2, model.getUser());
        assertEquals(LocalDate.of(2021, 8, 17), model.getCreationDate());
        assertEquals(1, model.getId());
    }

    @Test
    public void newPostNotificationModelTest(){
        NewPostNotification model = new NewPostNotification(data.user1, true, LocalDate.of(2021, 5, 17), data.post, data.group1);
        model.setUser(data.user2);
        model.setCreationDate(LocalDate.of(2021, 8, 17));
        model.setNewPost(data.post2);
        model.setGroup(data.group2);
        assertEquals(data.user2, model.getUser());
        assertEquals(LocalDate.of(2021, 8, 17), model.getCreationDate());
        assertEquals(data.post2, model.getNewPost());
        assertEquals(data.group2, model.getGroup());
    }

    @Test
    public void newMemberNotificationModelTest(){
        NewMemberNotification model = new NewMemberNotification(data.user1, true, LocalDate.of(2021, 5, 17), data.user2, data.group1);
        model.setNewMember(data.user2);
        model.setStudyGroup(data.group2);
        assertEquals(data.user2, model.getNewMember());
        assertEquals(data.group2, model.getStudyGroup());
    }

    @Test
    public void labelModelTest(){
        Label model = data.label1;
        model.setName("label");
        model.setStudyGroups(Arrays.asList(new StudyGroupLabel(data.label1, data.group1)));
        assertEquals("label", model.getName());
        assertEquals(data.group1, model.getStudyGroups().get(0).getStudyGroup());
    }

    @Test
    public void jwtBlackListModelTest(){
        JwtBlacklist model = new JwtBlacklist("token");
        model.setId(1);
        model.setToken("tokencito");
        assertEquals("tokencito", model.getToken());
        assertEquals(1, model.getId());
    }

    @Test
    public void linkTypeTest(){
        assertEquals("TWITTER", LinkType.toString(LinkType.TWITTER));
        assertEquals("FACEBOOK", LinkType.toString(LinkType.FACEBOOK));
        assertEquals("INSTAGRAM", LinkType.toString(LinkType.INSTAGRAM));
        assertEquals("EMAIL", LinkType.toString(LinkType.EMAIL));
        assertEquals("PHONE", LinkType.toString(LinkType.PHONE));
    }

    @Test
    public void notificationTypeTest(){
        assertEquals("NEW_MEMBER_NOTIFICATION", NotificationType.toString(NotificationType.NEW_MEMBER_NOTIFICATION));
        assertEquals("NEW_POST_NOTIFICATION", NotificationType.toString(NotificationType.NEW_POST_NOTIFICATION));
    }

    @Test
    public void userTypeTest(){
        assertEquals("USER", UserType.toString(UserType.USER));
    }

    @Test
    public void editUserFormTest(){
        EditUserForm model = new EditUserForm("pepe", "peres", "123", new ArrayList<>());
        model.setName("name");
        model.setSurname("surname");
        model.setPassword("password");
        model.setContactLinks(Arrays.asList(new ContactLink(LinkType.PHONE, "phone")));
        assertEquals("name", model.getName());
        assertEquals("password", model.getPassword());
        assertEquals("surname", model.getSurname());
        assertEquals("phone", model.getContactLinks().get(0).getLink());
    }

    @Test
    public void labelServiceTest(){
        LabelService model = new LabelService(labelRepository);
        model.createNewLabel(new LabelForm("label"));
    }
}
