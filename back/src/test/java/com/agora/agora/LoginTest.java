package com.agora.agora;

import com.agora.agora.model.User;
import com.agora.agora.model.form.LoginForm;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;


import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("test")
@JsonTest
@WebAppConfiguration
public class LoginTest {
    @Autowired
    JacksonTester<LoginForm> json;


    @Test
    public void testFormSerialization() throws Exception {
        LoginForm form = new LoginForm("nico@gmail.com", "Password123");

        String expectedJson = "{\"email\":\"nico@gmail.com\",\"password\":\"Password123\"}";

        assertEquals(expectedJson,json.write(form).getJson());
    }

    @Test
    public void testFormDeserialization() throws Exception{
        String expectedJson = "{\"email\":\"nico@gmail.com\",\"password\":\"Password123\"}";
        LoginForm form = new LoginForm("nico@gmail.com", "Password123");

        LoginForm loginFormObtained = json.parse(expectedJson).getObject();

        assertEquals(form.getEmail(),loginFormObtained.getEmail());
        assertEquals(form.getPassword(),loginFormObtained.getPassword());
    }

}
