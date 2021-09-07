package com.agora.agora.repository;

import com.agora.agora.model.User;
import com.google.common.io.Files;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User,Integer> {

    Optional<User> findFirstById(int id);

    Optional<User> findByUserVerificationToken(String userVerificationToken);

    Optional<User> findUserByEmail(String email);
}
