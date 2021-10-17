package com.agora.agora.controller;

import com.agora.agora.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/notification")
public class NotificationController {

    private final NotificationService notificationService;

    @Autowired
    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @PostMapping(value = "/{id}/read")
    public ResponseEntity markNotificationAsRead(@PathVariable("id") int notificationId){
        notificationService.readNotification(notificationId);
        return ResponseEntity.ok().build();
    }
}
