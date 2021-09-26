package com.agora.agora;

import com.agora.agora.model.StudyGroup;
import com.agora.agora.model.StudyGroupUser;
import com.agora.agora.model.User;
import com.agora.agora.model.type.UserType;
import com.agora.agora.repository.StudyGroupRepository;
import com.agora.agora.repository.StudyGroupUsersRepository;
import com.agora.agora.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class StartUpDatabase {
    private UserRepository userRepository;
    private StudyGroupRepository studyGroupRepository;
    private StudyGroupUsersRepository studyGroupUsersRepository;

    @Autowired
    public StartUpDatabase(UserRepository userRepository, StudyGroupRepository studyGroupRepository, StudyGroupUsersRepository studyGroupUsersRepository) {
        this.userRepository = userRepository;
        this.studyGroupRepository = studyGroupRepository;
        this.studyGroupUsersRepository = studyGroupUsersRepository;
    }

    @EventListener
    public void appReady(ApplicationReadyEvent readyEvent){
        Boolean init = readyEvent.getApplicationContext().getEnvironment().getProperty("initdb", boolean.class);

        if(init){
            try{
                //Users
                User user1 = new User("Pepe", "Perez", "perez@gmail.com", BCrypt.hashpw("Perez2020", BCrypt.gensalt()), true, UserType.USER);
                User user2 = new User("Tomas", "Gomez", "gomez@gmail.com", BCrypt.hashpw("Gomez2019", BCrypt.gensalt()), false, UserType.USER);
                User user3 = new User("Agustin", "Rodriguez", "rodriguez@gmail.com", BCrypt.hashpw("Rodriguez2018", BCrypt.gensalt()), true, UserType.USER);
                User user4 = new User("Delfina", "Alonso", "alonso@gmail.com", BCrypt.hashpw("Alonso2017", BCrypt.gensalt()), true, UserType.USER);
                User user5 = new User("Agustina", "Lopez", "lopez@gmail.com", BCrypt.hashpw("Lopez2016", BCrypt.gensalt()), false, UserType.USER);
                User user6 = new User("Romina", "Diaz", "diaz@gmail.com", BCrypt.hashpw("Diaz2015", BCrypt.gensalt()), false, UserType.USER);

                userRepository.save(user1);
                userRepository.save(user2);
                userRepository.save(user3);
                userRepository.save(user4);
                userRepository.save(user5);
                userRepository.save(user6);

                //Study Groups
                StudyGroup group1 = new StudyGroup("Teologia", "Estudios de Dios", user1, LocalDate.of(2016, 7, 1));
                StudyGroup group2 = new StudyGroup("Platon", "Estudios de Platon", user3, LocalDate.of(2020, 10, 25));
                StudyGroup group3 = new StudyGroup("Aristoteles", "Estudios de Aristoteles", user4, LocalDate.of(2019, 3, 6));

                studyGroupRepository.save(group1);
                studyGroupRepository.save(group2);
                studyGroupRepository.save(group3);

                //StudyGroups with creator User relations (to appear in users list in studyGroup)
                StudyGroupUser group1User1 = new StudyGroupUser(user1, group1);
                StudyGroupUser group2User3 = new StudyGroupUser(user3, group2);
                StudyGroupUser group3User4 = new StudyGroupUser(user4, group3);

                studyGroupUsersRepository.save(group1User1);
                studyGroupUsersRepository.save(group2User3);
                studyGroupUsersRepository.save(group3User4);

            } catch (Exception ignored){}
        }
    }
}
