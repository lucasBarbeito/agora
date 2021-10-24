package com.agora.agora.service;

import com.agora.agora.exceptions.ForbiddenElementException;
import com.agora.agora.model.*;
import com.agora.agora.repository.GroupInviteNotificationRepository;
import com.agora.agora.repository.NewMemberNotificationRepository;
import com.agora.agora.repository.NewPostNotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

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

    public void readNotification(int notificationId) {
        Optional<NewMemberNotification> newMember = newMemberNotificationRepository.findById(notificationId);
        Optional<NewPostNotification> newPost = newPostNotificationRepository.findById(notificationId);
        Optional<GroupInviteNotification> groupInvite = groupInviteNotificationRepository.findById(notificationId);

        if(newMember.isPresent()){
            NewMemberNotification notification = newMember.get();
            if(checkIfNotificationIsForCurrentUser(notification)){
                notification.setRead(true);
                newMemberNotificationRepository.save(notification);
            } else throw new ForbiddenElementException("Notification is not for current user. Only the recipient user can mark notification as read");
        } else if(newPost.isPresent()){
            NewPostNotification notification = newPost.get();
            if(checkIfNotificationIsForCurrentUser(notification)){
                notification.setRead(true);
                newPostNotificationRepository.save(notification);
            } else throw new ForbiddenElementException("Notification is not for current user. Only the recipient user can mark notification as read");
        } else if(groupInvite.isPresent()){
            GroupInviteNotification notification = groupInvite.get();
            if(checkIfNotificationIsForCurrentUser(notification)){
                notification.setRead(true);
                groupInviteNotificationRepository.save(notification);
            } else throw new ForbiddenElementException("Notification is not for current user. Only the recipient user can mark notification as read");
        } else throw new NoSuchElementException();
    }

    private boolean checkIfNotificationIsForCurrentUser(Notification notification){
        User currentUser = userService.findCurrentUser().orElseThrow(()->
                new NoSuchElementException("Current user credentials are wrong"));
        return notification.getUser().getId() == currentUser.getId();
    }
}
