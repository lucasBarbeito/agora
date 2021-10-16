package com.agora.agora.service;

import com.agora.agora.model.*;
import com.agora.agora.model.dto.NotificationDTO;
import com.agora.agora.model.form.UserForm;
import com.agora.agora.model.type.NotificationType;
import com.agora.agora.model.type.UserType;
import com.agora.agora.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService {

    private UserRepository userRepository;
    private StudyGroupRepository studyGroupRepository;
    private StudyGroupUsersRepository studyGroupUsersRepository;
    private EmailService emailService;
    private NewMemberNotificationRepository newMemberNotificationRepository;
    private NewPostNotificationRepository newPostNotificationRepository;

    @Autowired
    public UserService(UserRepository userRepository,StudyGroupRepository studyGroupRepository, StudyGroupUsersRepository studyGroupUsersRepository, EmailService emailService, NewPostNotificationRepository newPostNotificationRepository, NewMemberNotificationRepository newMemberNotificationRepository) {
        this.userRepository = userRepository;
        this.studyGroupRepository = studyGroupRepository;
        this.studyGroupUsersRepository = studyGroupUsersRepository;
        this.emailService = emailService;
        this.newMemberNotificationRepository = newMemberNotificationRepository;
        this.newPostNotificationRepository = newPostNotificationRepository;
    }


    public int save(UserForm user) {
        return saveCustom(user, false, true);
    }

    public int saveCustom(UserForm user, boolean isVerified, boolean sendMail){
        User u = new User(user.getName(),
                user.getSurname(),
                user.getEmail(),
                BCrypt.hashpw(user.getPassword(), BCrypt.gensalt()),
                isVerified,
                UserType.USER);
        u.setUserVerificationToken(UUID.randomUUID().toString());
        userRepository.save(u);
        String url = "http://localhost:3000/user/verify-user/" + u.getUserVerificationToken();
        String body = "Verifica tu usuario: \n" + url;
        if(sendMail){
            emailService.sendSimpleMessage(user.getEmail(), "Verificar usuario", body);
        }
        return u.getId();
    }

    public Optional<User> findById(int id) {
        return userRepository.findFirstById(id);
    }

    public void justSave(User user) {
        userRepository.save(user);
    }

    public Optional<User> findByUserVerificationToken(String userVerificationToken) {
        return userRepository.findByUserVerificationToken(userVerificationToken);
    }

    public Optional<User> findCurrentUser() {
        String email = ((org.springframework.security.core.userdetails.User)
                SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        return userRepository.findUserByEmail(email);
    }

    public String verifyUser(String userVerificationToken) {
        Optional<User> userOptional = findByUserVerificationToken(userVerificationToken);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setVerified(true);
            user.setUserVerificationToken(null);
            userRepository.save(user);
            return user.getName();
        }
        throw new NoSuchElementException();
    }

    public List<User> findUsers(Optional<String> name) {
        if (name.isPresent()) {
            return userRepository.findByNameAndOrSurname(name.get());
        } else {
            return userRepository.findAll();
        }
    }

    public List<NotificationDTO> getCurrentUserNotifications(){
        String email = ((org.springframework.security.core.userdetails.User)
                SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        Optional<User> userOptional = userRepository.findUserByEmail(email);

        if(userOptional.isPresent()){
            User user = userOptional.get();
            List<NewMemberNotification> memberNotifications = newMemberNotificationRepository.findAllByUserId(user.getId());
            List<NewPostNotification> postNotifications = newPostNotificationRepository.findAllByUserId(user.getId());

            if(!memberNotifications.isEmpty() && !postNotifications.isEmpty()){
                List<NotificationDTO> notifications = new ArrayList<>();
                List<NotificationDTO> memberDTO = memberNotifications.stream().map(notification -> new NotificationDTO(NotificationType.NEW_MEMBER_NOTIFICATION, notification.getStudyGroup().getId(), notification.getNewMember().getId(), true, notification.getUser().getId())).collect(Collectors.toList());
                List<NotificationDTO> postDTO = postNotifications.stream().map(notification -> new NotificationDTO(NotificationType.NEW_POST_NOTIFICATION, notification.getGroup().getId(), notification.getNewPost().getId(), true, notification.getUser().getId())).collect(Collectors.toList());
                notifications.addAll(memberDTO);
                notifications.addAll(postDTO);
                return notifications;
            }
            else if(memberNotifications.isEmpty() && !postNotifications.isEmpty()){
                List<NotificationDTO> postDTO = postNotifications.stream().map(notification -> new NotificationDTO(NotificationType.NEW_POST_NOTIFICATION, notification.getGroup().getId(), notification.getNewPost().getId(), true, notification.getUser().getId())).collect(Collectors.toList());
                return new ArrayList<>(postDTO);
            }
            else if(postNotifications.isEmpty() && !memberNotifications.isEmpty()){
                List<NotificationDTO> memberDTO = memberNotifications.stream().map(notification -> new NotificationDTO(NotificationType.NEW_MEMBER_NOTIFICATION, notification.getStudyGroup().getId(), notification.getNewMember().getId(), true, notification.getUser().getId())).collect(Collectors.toList());
                return new ArrayList<>(memberDTO);
            }
            else {
                throw new NoSuchElementException("No new notifications");
            }
        }else{
            throw new NoSuchElementException("User does not exist.");
        }
    }


}
