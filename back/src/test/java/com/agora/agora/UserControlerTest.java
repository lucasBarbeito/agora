package com.agora.agora;

import com.agora.agora.model.User;
import com.agora.agora.model.dto.FullUserDTO;
import com.agora.agora.model.form.UserForm;
import com.agora.agora.model.form.UserVerificationForm;
import com.agora.agora.model.type.UserType;
import com.agora.agora.repository.UserRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class UserControlerTest extends AbstractTest{

    @Autowired
    private UserRepository userRepository;

    private final Data data = new Data();

    // Storing all the info we'll load into the DB here.
    private class Data {
        User user;

        void setup() {
            user = new User("Carlos",
                    "Gimenez",
                    "carlos@mail.com",
                    "Carlos123",
                    false,
                    UserType.USER);
            userRepository.save(user);
        }

        void rollback() {
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
        data.user.setUserVerificationToken("1234");
        userRepository.save(data.user);

        String uri = "/user/me";

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri)
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        int statusCode = mvcResult.getResponse().getStatus();
        assertEquals(200, statusCode);

        String status = mvcResult.getResponse().getContentAsString();
        FullUserDTO userDTO = super.mapFromJson(status, FullUserDTO.class);

        assertEquals(userDTO.getId(), data.user.getId());
        assertEquals(userDTO.getEmail(), data.user.getEmail());
        assertEquals(userDTO.getName(), data.user.getName());
        assertEquals(userDTO.getSurname(), data.user.getSurname());
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
}
