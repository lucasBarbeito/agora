package com.agora.agora;

import com.agora.agora.model.User;
import com.agora.agora.model.form.LoginForm;
import com.agora.agora.model.type.UserType;
import com.agora.agora.repository.UserRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class AuthTest extends AbstractTest{

    @Autowired
    private UserRepository userRepository;

    private final Data data = new Data();

    // Storing all the info we'll load into the DB here.
    private class Data {
        User user;

        void setup() {
            user = new User("Carlos",
                    "Gimenez",
                    "carlos@gmail.com",
                    BCrypt.hashpw("Carlos123",BCrypt.gensalt()),
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
    public void LogInWithCorrectDataShouldReturnToken() throws Exception {
        String uri = "/auth";
        LoginForm loginForm = new LoginForm("carlos@gmail.com", "Carlos123");

        MvcResult loginResult = mvc.perform(
                MockMvcRequestBuilders.post(uri)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(mapToJson(loginForm))).andReturn();

        int status = loginResult.getResponse().getStatus();
        assertEquals(200, status);

        ObjectMapper mapper = new ObjectMapper();
        String loginContent = loginResult.getResponse().getContentAsString();
        JsonNode tokenJson = mapper.readTree(loginContent);

        assertTrue(tokenJson.get("token").toString().length() > 0);
    }

    @Test
    public void LogInWithIncorrectPasswordShouldReturnUnauthorized() throws Exception {
        String uri = "/auth";
        LoginForm loginForm = new LoginForm("carlos@gmail.com", "Carlos");

        MvcResult loginResult = mvc.perform(MockMvcRequestBuilders.post(uri)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .content(mapToJson(loginForm))).andReturn();

        int status = loginResult.getResponse().getStatus();
        assertEquals(401, status);
    }

    @Test
    public void LogInWithIncorrectEmailShouldReturnUnauthorized() throws Exception {
        String uri = "/auth";
        LoginForm loginForm = new LoginForm("wrongEmail@gmail.com", "Carlos123");

        MvcResult loginResult = mvc.perform(MockMvcRequestBuilders.post(uri)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .content(mapToJson(loginForm))).andReturn();

        int status = loginResult.getResponse().getStatus();
        assertEquals(401, status);
    }
}
