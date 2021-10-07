package com.agora.agora;

import com.agora.agora.model.*;
import com.agora.agora.model.type.UserType;
import com.agora.agora.repository.*;
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
    private LabelRepository labelRepository;
    private StudyGroupLabelRepository studyGroupLabelRepository;

    @Autowired
    public StartUpDatabase(UserRepository userRepository, StudyGroupRepository studyGroupRepository, StudyGroupUsersRepository studyGroupUsersRepository, LabelRepository labelRepository, StudyGroupLabelRepository studyGroupLabelRepository) {
        this.userRepository = userRepository;
        this.studyGroupRepository = studyGroupRepository;
        this.studyGroupUsersRepository = studyGroupUsersRepository;
        this.labelRepository = labelRepository;
        this.studyGroupLabelRepository = studyGroupLabelRepository;
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

                //Labels
                Label label1 = new Label("SciFi");
                Label label2 = new Label("History");
                Label label3 = new Label("Science");
                Label label4 = new Label("Math");
                Label label5 = new Label("Programming");
                Label label6 = new Label("Design");
                Label label7 = new Label("Statistics");
                Label label8 = new Label("Physics");
                Label label9 = new Label("Algebra");
                Label label10 = new Label("Religion");

                labelRepository.save(label1);
                labelRepository.save(label2);
                labelRepository.save(label3);
                labelRepository.save(label4);
                labelRepository.save(label5);
                labelRepository.save(label6);
                labelRepository.save(label7);
                labelRepository.save(label8);
                labelRepository.save(label9);
                labelRepository.save(label10);


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

                //Labels in studyGroups
                StudyGroupLabel g1l10 = new StudyGroupLabel(label10, group1);
                StudyGroupLabel g2l10 = new StudyGroupLabel(label10, group2);
                StudyGroupLabel g3l10 = new StudyGroupLabel(label10, group3);

                studyGroupLabelRepository.save(g1l10);
                studyGroupLabelRepository.save(g2l10);
                studyGroupLabelRepository.save(g3l10);

            } catch (Exception ignored){}
        }
    }
}
