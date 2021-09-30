package com.agora.agora;

import com.agora.agora.model.User;
import com.agora.agora.model.form.LoginForm;
import com.agora.agora.model.form.UserForm;
import com.agora.agora.model.type.UserType;
import com.agora.agora.repository.JwtBlacklistRepository;
import com.agora.agora.repository.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.util.NestedServletException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class AuthTest extends AbstractTest{

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JwtBlacklistRepository blacklistRepository;

    private final Data data = new Data();

    // Storing all the info we'll load into the DB here.
    private class Data {
        User user;

        void setup() {
            user = new User("Carlos",
                    "Gimenez",
                    "carlos@gmail.com",
                    BCrypt.hashpw("Carlos123",BCrypt.gensalt()),
                    true,
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

    @Test
    public void LogInWithCorrectDataButNotVerifiedShouldReturnForbidden() throws Exception{
        User user = new User("Carla",
                "Gimenez",
                "carla@gmail.com",
                BCrypt.hashpw("Carla123",BCrypt.gensalt()),
                false,
                UserType.USER);
        userRepository.save(user);

        String uri = "/auth";
        LoginForm loginForm = new LoginForm("carla@gmail.com", "Carla123");

        MvcResult loginResult = mvc.perform(
                MockMvcRequestBuilders.post(uri)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(mapToJson(loginForm))).andReturn();

        int status = loginResult.getResponse().getStatus();
        assertEquals(403, status);
    }

    @Test
    @WithMockUser("USER")
    public void addingTokenToBlackListShouldAdd() throws Exception {
        String uri = "/auth";
        String token = "123";
        MvcResult deleteTokenResult = mvc.perform(
                MockMvcRequestBuilders.delete(uri)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .header("Authorization", "Bearer " + token)).andReturn();
        int status = deleteTokenResult.getResponse().getStatus();
        assertEquals(200,status);
        String tokenInBlackList = blacklistRepository.findByToken(token).getToken();
        assertEquals(token, tokenInBlackList);
    }

    /*
    @Test(expected = NestedServletException.class)
    @WithMockUser("USER")
    public void deletingExistingTokenShouldForbid() throws Exception {
        String uri = "/auth";
        String protectedRoute= "/user/me";
        LoginForm loginForm = new LoginForm("carlos@gmail.com", "Carlos123");

        MvcResult loginResult = mvc.perform(
                MockMvcRequestBuilders.post(uri)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(mapToJson(loginForm))).andReturn();

        ObjectMapper mapper = new ObjectMapper();
        String loginContent = loginResult.getResponse().getContentAsString();
        String token = mapper.readTree(loginContent).get("token").toString();

        mvc.perform(
                MockMvcRequestBuilders.delete(uri)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .header("Authorization", "Bearer " + token)).andReturn();


        MvcResult getUserResult =mvc.perform(
                MockMvcRequestBuilders.get(protectedRoute)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .header("Authorization", "Bearer " + token)).andReturn();
        int status = getUserResult.getResponse().getStatus();
        System.out.println(status);
        assertEquals(401,status);
    }
     */
}
