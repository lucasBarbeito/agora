package com.agora.agora;

import com.agora.agora.model.User;
import com.agora.agora.model.form.UserForm;
import com.agora.agora.model.type.UserType;
import com.agora.agora.repository.UserRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;

import static org.junit.Assert.assertEquals;

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
}
