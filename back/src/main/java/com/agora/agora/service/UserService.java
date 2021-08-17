package com.agora.agora.service;

import com.agora.agora.model.User;
import com.agora.agora.model.form.UserForm;
import com.agora.agora.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

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
                false);
        userRepository.save(u);
        return u.getId();
    }
}
