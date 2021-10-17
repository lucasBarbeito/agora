package com.agora.agora.service;

import com.agora.agora.exceptions.ForbiddenElementException;
import com.agora.agora.model.GroupInviteNotification;
import com.agora.agora.model.Notification;
import com.agora.agora.model.User;
import com.agora.agora.repository.GroupInviteNotificationRepository;
import com.agora.agora.repository.NewMemberNotificationRepository;
import com.agora.agora.repository.NewPostNotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class NotificationService {

    NewMemberNotificationRepository newMemberNotificationRepository;
    NewPostNotificationRepository newPostNotificationRepository;
    GroupInviteNotificationRepository groupInviteNotificationRepository;
    UserService userService;

    @Autowired
    public NotificationService(NewMemberNotificationRepository newMemberNotificationRepository, NewPostNotificationRepository newPostNotificationRepository, GroupInviteNotificationRepository groupInviteNotificationRepository, UserService userService) {
        this.newMemberNotificationRepository = newMemberNotificationRepository;
        this.newPostNotificationRepository = newPostNotificationRepository;
        this.groupInviteNotificationRepository = groupInviteNotificationRepository;
        this.userService = userService;
    }

    public boolean readNotification(int notificationId) {
        if(newMemberNotificationRepository.findById(notificationId).isPresent()){
            Notification notification = newMemberNotificationRepository.findById(notificationId).get();
            return checkIfNotificationIsForCurrentUser(notification);
        } else if(newPostNotificationRepository.findById(notificationId).isPresent()){
            Notification notification = newPostNotificationRepository.findById(notificationId).get();
            return checkIfNotificationIsForCurrentUser(notification);
        } else if(groupInviteNotificationRepository.findById(notificationId).isPresent()){
            Notification notification = groupInviteNotificationRepository.findById(notificationId).get();
            return checkIfNotificationIsForCurrentUser(notification);
        } else throw new NoSuchElementException();
    }

    private boolean checkIfNotificationIsForCurrentUser(Notification notification){
        User currentUser = userService.findCurrentUser().orElseThrow(()->
                new NoSuchElementException("Current user credentials are wrong"));
        if(notification.getUser().getId() == currentUser.getId()){
            notification.setRead(true);
            return true;
        } else {
            throw new ForbiddenElementException("Notification is not for current user. Only the recipient user can mark notification as read");
        }
    }
}
