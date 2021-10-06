package com.agora.agora.repository;

import com.agora.agora.model.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User,Integer> {
    Optional<User> findFirstById(int id);

    Optional<User> findByUserVerificationToken(String userVerificationToken);

    Optional<User> findUserByEmail(String email);

    List<User> findAll();

    @Query(value = "select us from User us " +
            "where lower(concat(us.name, concat(' ', us.surname))) like lower(concat('%',concat(:text, '%')))")
    List<User> findByNameAndOrSurname(String text);
}
