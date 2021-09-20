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

@Service
public class UserService {

    private UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public int save(UserForm user) {
        User u = new User(user.getName(),
                user.getSurname(),
                user.getEmail(),
                BCrypt.hashpw(user.getPassword(), BCrypt.gensalt()),
                false,
                UserType.USER);
        userRepository.save(u);
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
