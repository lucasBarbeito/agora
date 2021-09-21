package com.agora.agora;

import com.agora.agora.controller.UserController;
import com.agora.agora.model.form.UserForm;
import com.agora.agora.repository.UserRepository;
import com.agora.agora.service.EmailService;
import com.agora.agora.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.util.UriComponentsBuilder;
import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest(classes = AgoraApplication.class)
@ActiveProfiles("test")
@WebAppConfiguration
public class EmailVerificationTest{


    @Mock
    private EmailService emailService;
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    UserService userService;
    private UserController userController;
    @Captor
    ArgumentCaptor<String> toCaptor;
    @Captor
    ArgumentCaptor<String> subjectCaptor;
    @Captor
    ArgumentCaptor<String> textCaptor;

    @Test
    public void checkEmailServiceCorrectParameters() {
        userController = new UserController(userService);
        String uri = "/user";
        UserForm form = new UserForm("Manuel","Gimenez","david@mail.com","Manuel123");
        userController.createUser(form, UriComponentsBuilder.newInstance().scheme("http").host("localhost").path(uri));
        Mockito.verify(emailService).sendSimpleMessage(toCaptor.capture(),subjectCaptor.capture(),textCaptor.capture());
        assertEquals(form.getEmail(), toCaptor.getValue());
        assertEquals("Verificar usuario", subjectCaptor.getValue());
    }
}