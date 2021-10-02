package com.agora.agora.service;

import com.agora.agora.model.User;
import com.agora.agora.model.form.UserForm;
import com.agora.agora.model.type.UserType;
import com.agora.agora.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {

    private UserRepository userRepository;
    private EmailService emailService;

    @Autowired
    public UserService(UserRepository userRepository, EmailService emailService) {
        this.userRepository = userRepository;
        this.emailService = emailService;
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
}
