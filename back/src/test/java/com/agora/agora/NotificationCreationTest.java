package com.agora.agora;

import com.agora.agora.model.dto.NotificationDTO;
import com.agora.agora.model.form.UserForm;
import com.agora.agora.model.type.NotificationType;
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
public class NotificationCreationTest {

    @Autowired
    JacksonTester<NotificationDTO> json;

    @Test
    public void testNotificationDTOSerialization() throws Exception {
        NotificationDTO notificationDTO = new NotificationDTO(NotificationType.NEW_MEMBER_NOTIFICATION, 1, 1, 1, true, 1);
        String expectedJson = "{\"notificationType\":\"NEW_MEMBER_NOTIFICATION\",\"notificationId\":1,\"studyGroupId\":1,\"notificationTypeId\":1,\"userRecipientId\":1,\"read\":true}";

        assertEquals(expectedJson,json.write(notificationDTO).getJson());
    }

    @Test
    public void testNotificationDTODeserialization() throws Exception {
        NotificationDTO notificationDTO = new NotificationDTO(NotificationType.NEW_MEMBER_NOTIFICATION,1, 1, 1, true, 1);
        String expectedJson = "{\"notificationType\":\"NEW_MEMBER_NOTIFICATION\",\"notificationId\":1, \"studyGroupId\":1,\"notificationTypeId\":1,\"userRecipientId\":1,\"read\":true}";

        NotificationDTO notificationObtained = json.parse(expectedJson).getObject();

        assertEquals(notificationDTO.getNotificationType(), notificationObtained.getNotificationType());
        assertEquals(notificationDTO.getStudyGroupId(), notificationObtained.getStudyGroupId());
        assertEquals(notificationDTO.getUserRecipientId(), notificationObtained.getUserRecipientId());
        assertEquals(notificationDTO.getNotificationTypeId(), notificationObtained.getNotificationTypeId());
        assertEquals(notificationDTO.isRead(), notificationObtained.isRead());
    }

    @Test
    public void testNotificationDTOSetSerialization() throws Exception {
        NotificationDTO notificationDTO = new NotificationDTO();
        notificationDTO.setNotificationType(NotificationType.NEW_MEMBER_NOTIFICATION);
        notificationDTO.setStudyGroupId(1);
        notificationDTO.setNotificationTypeId(1);
        notificationDTO.setRead(true);
        notificationDTO.setUserRecipientId(1);
        String expectedJson = "{\"notificationType\":\"NEW_MEMBER_NOTIFICATION\",\"notificationId\":0,\"studyGroupId\":1,\"notificationTypeId\":1,\"userRecipientId\":1,\"read\":true}";

        assertEquals(expectedJson,json.write(notificationDTO).getJson());
    }
}
