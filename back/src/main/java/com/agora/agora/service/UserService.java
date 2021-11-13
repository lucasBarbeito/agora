package com.agora.agora.service;

import com.agora.agora.model.*;
import com.agora.agora.model.dto.NotificationDTO;
import com.agora.agora.model.form.EditUserForm;
import com.agora.agora.model.form.UserForm;
import com.agora.agora.model.type.NotificationType;
import com.agora.agora.model.type.UserType;
import com.agora.agora.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserService {

    private UserRepository userRepository;
    private StudyGroupRepository studyGroupRepository;
    private StudyGroupUsersRepository studyGroupUsersRepository;
    private EmailService emailService;
    private NewMemberNotificationRepository newMemberNotificationRepository;
    private NewPostNotificationRepository newPostNotificationRepository;
    private GroupInviteNotificationRepository groupInviteNotificationRepository;
    private PostRepository postRepository;
    private ContactLinkRepository contactLinkRepository;
    private UserInviteNotificationRepository userInviteNotificationRepository;

    @Autowired
    public UserService(PostRepository postRepository, UserRepository userRepository, StudyGroupRepository studyGroupRepository, StudyGroupUsersRepository studyGroupUsersRepository, EmailService emailService, NewPostNotificationRepository newPostNotificationRepository, NewMemberNotificationRepository newMemberNotificationRepository, GroupInviteNotificationRepository groupInviteNotificationRepository, ContactLinkRepository contactLinkRepository, UserInviteNotificationRepository userInviteNotificationRepository) {
        this.userRepository = userRepository;
        this.studyGroupRepository = studyGroupRepository;
        this.studyGroupUsersRepository = studyGroupUsersRepository;
        this.emailService = emailService;
        this.newMemberNotificationRepository = newMemberNotificationRepository;
        this.newPostNotificationRepository = newPostNotificationRepository;
        this.groupInviteNotificationRepository = groupInviteNotificationRepository;
        this.postRepository = postRepository;
        this.contactLinkRepository = contactLinkRepository;
        this.userInviteNotificationRepository = userInviteNotificationRepository;
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
            List<UserInviteNotification> userInviteNotifications = userInviteNotificationRepository.findAllByUserId(user.getId());

            if(!memberNotifications.isEmpty() && !postNotifications.isEmpty()){
                List<NotificationDTO> notifications = new ArrayList<>();
                List<NotificationDTO> memberDTO = memberNotifications.stream().map(notification -> new NotificationDTO(NotificationType.NEW_MEMBER_NOTIFICATION, notification.getId(), notification.getStudyGroup().getId(), notification.getNewMember().getId(), notification.isRead(), notification.getUser().getId())).collect(Collectors.toList());
                List<NotificationDTO> postDTO = postNotifications.stream().map(notification -> new NotificationDTO(NotificationType.NEW_POST_NOTIFICATION, notification.getId(), notification.getGroup().getId(), notification.getNewPost().getId(), notification.isRead(), notification.getUser().getId())).collect(Collectors.toList());
                List<NotificationDTO> inviteDTO = userInviteNotifications.stream().map(notification -> new NotificationDTO(NotificationType.USER_INVITE_NOTIFICATION, notification.getId(), notification.getGroup().getId(), notification.getGroup().getId(), notification.isRead(), notification.getUser().getId())).collect(Collectors.toList());
                notifications.addAll(memberDTO);
                notifications.addAll(postDTO);
                notifications.addAll(inviteDTO);
                return notifications;
            }
            else if(memberNotifications.isEmpty() && !postNotifications.isEmpty()){
                List<NotificationDTO> postDTO = postNotifications.stream().map(notification -> new NotificationDTO(NotificationType.NEW_POST_NOTIFICATION, notification.getId(), notification.getGroup().getId(), notification.getNewPost().getId(), notification.isRead(), notification.getUser().getId())).collect(Collectors.toList());
                return new ArrayList<>(postDTO);
            }
            else if(postNotifications.isEmpty() && !memberNotifications.isEmpty()){
                List<NotificationDTO> memberDTO = memberNotifications.stream().map(notification -> new NotificationDTO(NotificationType.NEW_MEMBER_NOTIFICATION, notification.getId(), notification.getStudyGroup().getId(), notification.getNewMember().getId(), notification.isRead(), notification.getUser().getId())).collect(Collectors.toList());
                return new ArrayList<>(memberDTO);
            }
            else {
                return new ArrayList<>();
            }
        }else{
            throw new NoSuchElementException("User does not exist.");
        }
    }


    public void deleteUser(int userId) {
        groupInviteNotificationRepository.deleteAllByUserId(userId);
        newMemberNotificationRepository.deleteAllByUserId(userId);
        newMemberNotificationRepository.deleteAllByNewMemberId(userId);

        newPostNotificationRepository.deleteAllByUserId(userId);

        studyGroupUsersRepository.deleteAllByUserId(userId);

        postRepository.deleteAllByCreatorId(userId);

        userRepository.deleteById(userId);
    }

    public int editUser(EditUserForm changedUserData) {
        String email = ((org.springframework.security.core.userdetails.User)
                SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        Optional<User> userOptional = userRepository.findUserByEmail(email);

        if(userOptional.isPresent()){
            User user = userOptional.get();
            if (!changedUserData.getContactLinks().isEmpty()) {
                List<ContactLink> finalContactLinks = new ArrayList<>();
                for (ContactLink newContactLink : changedUserData.getContactLinks()) {
                    for (ContactLink contactLink : user.getContactLinks()) {
                        if (newContactLink.getId() == contactLink.getId()) {
                            finalContactLinks.add(newContactLink);
                            break;
                        }
                    }
                }
                if (finalContactLinks.size() != changedUserData.getContactLinks().size()){
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
                }
                for (ContactLink contactLink : changedUserData.getContactLinks()) {
                    contactLinkRepository.save(contactLink);
                }
            }
            if (changedUserData.getName() != null) {
                user.setName(changedUserData.getName());
            }
            if (changedUserData.getSurname() != null) {
                user.setSurname(changedUserData.getSurname());
            }
            if (changedUserData.getPassword() != null) {
                user.setPassword(BCrypt.hashpw(changedUserData.getPassword(), BCrypt.gensalt()));
            }
            userRepository.save(user);
            return user.getId();
        } else {
            throw new NoSuchElementException("User does not exist.");
        }
    }
}
