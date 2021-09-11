package com.agora.agora;

import com.agora.agora.model.User;
import com.agora.agora.model.form.UserForm;
import com.agora.agora.model.form.UserVerificationForm;
import com.agora.agora.model.type.UserType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("test")
@JsonTest
@WebAppConfiguration
public class UserCreationTest{

    @Autowired
    JacksonTester<UserForm> json;
    @Autowired
    JacksonTester<User> jsonU;
    @Autowired
    JacksonTester<UserVerificationForm> jsonV;

    @Test
    public void testFormSerialization() throws Exception {
        UserForm form = new UserForm("Agustin","Von","a@gmail.com","Agustin123");
        String expectedJson = "{\"name\":\"Agustin\",\"surname\":\"Von\",\"email\":\"a@gmail.com\",\"password\":\"Agustin123\"}";

        assertEquals(expectedJson,json.write(form).getJson());
    }

    @Test
    public void testFormDeserialization() throws Exception{
        String userFormJ = "{\"name\":\"Agustin\",\"surname\":\"Von\",\"email\":\"a@gmail.com\",\"password\":\"Agustin123\"}";
        UserForm userForm = new UserForm("Agustin","Von","a@gmail.com","Agustin123");

        UserForm userFormObtained = json.parse(userFormJ).getObject();

        assertEquals(userForm.getName(),userFormObtained.getName());
        assertEquals(userForm.getSurname(),userFormObtained.getSurname());
        assertEquals(userForm.getPassword(),userFormObtained.getPassword());
        assertEquals(userForm.getEmail(),userFormObtained.getEmail());
    }

    @Test
    public void testUserFormSetInSerialization() throws Exception{
        UserForm userForm = new UserForm("Agustin","Von","a@gmail.com","Agustin123");
        userForm.setName("Carlos");
        userForm.setPassword("Carlos123");
        userForm.setSurname("Mendez");
        userForm.setEmail("Carlos@gmail.com");

        String expectedJson = "{\"name\":\"Carlos\",\"surname\":\"Mendez\",\"email\":\"Carlos@gmail.com\",\"password\":\"Carlos123\"}";

        assertEquals(expectedJson,json.write(userForm).getJson());
    }

    @Test
    public void testUserSerialization() throws Exception {
        User user = new User("Agustin","Von","a@gmail.com","Agustin123",true, UserType.USER);
        String expectedJson = "{\"id\":0,\"name\":\"Agustin\",\"surname\":\"Von\",\"email\":\"a@gmail.com\",\"password\":\"Agustin123\",\"userVerificationToken\":null,\"type\":\"USER\",\"verified\":true}";

        assertEquals(expectedJson,jsonU.write(user).getJson());
    }

    @Test
    public void testUserDeserialization() throws Exception{
        String userJ = "{\"id\":0,\"name\":\"Agustin\",\"surname\":\"Von\",\"email\":\"a@gmail.com\",\"password\":\"Agustin123\",\"userVerificationToken\":null,\"type\":\"USER\",\"verified\":true}";
        User user = new User("Agustin","Von","a@gmail.com","Agustin123",true, UserType.USER);

        User userObtained = jsonU.parse(userJ).getObject();

        assertEquals(user.getName(),userObtained.getName());
        assertEquals(user.getSurname(),userObtained.getSurname());
        assertEquals(user.getPassword(),userObtained.getPassword());
        assertEquals(user.getEmail(),userObtained.getEmail());
        assertEquals(user.getId(),userObtained.getId());
    }

    @Test
    public void testUserSetInSerialization() throws Exception{
        User user = new User("Agustin","Von","a@gmail.com","Agustin123",true, UserType.USER);
        user.setName("Carlos");
        user.setPassword("Carlos123");
        user.setSurname("Mendez");
        user.setEmail("Carlos@gmail.com");
        user.setVerified(false);

        String expectedJson = "{\"id\":0,\"name\":\"Carlos\",\"surname\":\"Mendez\",\"email\":\"Carlos@gmail.com\",\"password\":\"Carlos123\",\"userVerificationToken\":null,\"type\":\"USER\",\"verified\":false}";

        assertEquals(expectedJson,jsonU.write(user).getJson());
    }

    @Test
    public void testUserVerificationFormSerialization() throws Exception{
        UserVerificationForm form = new UserVerificationForm("12345");

        String expectedJson = "{\"userVerificationToken\":\"12345\"}";

        assertEquals(expectedJson,jsonV.write(form).getJson());
    }

    @Test
    public void testUserVerificationFormDeserialization() throws Exception{
        String form = "{\"userVerificationToken\":\"12345\"}";
        UserVerificationForm expected = new UserVerificationForm("12345");

        UserVerificationForm formObtained = jsonV.parse(form).getObject();

        assertEquals(expected.getUserVerificationToken(),formObtained.getUserVerificationToken());
    }

    @Test
    public void testUserVerificationFormSetInSerialization() throws Exception{
        UserVerificationForm form = new UserVerificationForm("12345");
        form.setUserVerificationToken("54321");

        String expectedJson = "{\"userVerificationToken\":\"54321\"}";

        assertEquals(expectedJson,jsonV.write(form).getJson());
    }
}

