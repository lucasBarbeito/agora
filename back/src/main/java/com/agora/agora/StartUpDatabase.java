package com.agora.agora;

import com.agora.agora.model.dto.LabelDTO;
import com.agora.agora.model.dto.LabelIdDTO;
import com.agora.agora.model.form.LabelForm;
import com.agora.agora.model.form.StudyGroupForm;
import com.agora.agora.model.form.UserForm;
import com.agora.agora.service.*;
import com.agora.agora.model.*;
import com.agora.agora.model.type.UserType;
import com.agora.agora.repository.*;
import com.agora.agora.service.StudyGroupService;
import com.agora.agora.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class StartUpDatabase {
    private final UserService userService;
    private final StudyGroupService studyGroupService;
    private final LabelRepository labelRepository;

    @Autowired

    public StartUpDatabase(UserService userService, StudyGroupService studyGroupService, LabelRepository labelRepository) {
        this.userService = userService;
        this.studyGroupService = studyGroupService;
        this.labelRepository = labelRepository;
    }

    private void addManyToGroup(int groupId, List<Integer> userId){
        userId.forEach(user -> studyGroupService.addUserToStudyGroup(groupId, user));
    }

    @EventListener
    public void appReady(ApplicationReadyEvent readyEvent){
        Boolean init = readyEvent.getApplicationContext().getEnvironment().getProperty("initdb", boolean.class);

        if(init){
            try{
                //Users
                UserForm user1 = new UserForm("Pepe", "Perez", "perez@gmail.com", "Perez2020");
                UserForm user2 = new UserForm("Tomas", "Gomez", "gomez@gmail.com", "Gomez2019");
                UserForm user3 = new UserForm("Agustin", "Rodriguez", "rodriguez@gmail.com","Rodriguez2018");
                UserForm user4 = new UserForm("Delfina", "Alonso", "alonso@gmail.com","Alonso2017");
                UserForm user5 = new UserForm("Agustina", "Lopez", "lopez@gmail.com","Lopez2016");
                UserForm user6 = new UserForm("Romina", "Diaz", "diaz@gmail.com", "Diaz2015");
                UserForm user7 = new UserForm("Matias", "Lopez", "matias@gmail.com","Matias2020");
                UserForm user8 = new UserForm("Carlos", "Mendez", "carlos@gmail.com", "Carlos2019");
                UserForm user9 = new UserForm("Sofia", "Gimenez", "sofia@gmail.com", "Sofia2018");
                UserForm user10 = new UserForm("Micaela", "Diaz", "mica@gmail.com", "Micaela2017");
                UserForm user11 = new UserForm("Fernando", "Rojas", "rojas@gmail.com","Fernando2016");
                UserForm user12 = new UserForm("Juan", "Cruz", "cruz@gmail.com", "Juan2015");

                int userId1 = userService.saveCustom(user1,true, false);
                int userId2 = userService.saveCustom(user2,true, false);
                int userId3 = userService.saveCustom(user3,true, false);
                int userId4 = userService.saveCustom(user4,true, false);
                int userId5 = userService.saveCustom(user5,true, false);
                int userId6 = userService.saveCustom(user6,false, false);
                int userId7 = userService.saveCustom(user7,false, false);
                int userId8 = userService.saveCustom(user8,false, false);
                int userId9 = userService.saveCustom(user9,false, false);
                int userId10 = userService.saveCustom(user10,false, false);
                int userId11 = userService.saveCustom(user11,false, false);
                int userId12 = userService.saveCustom(user12,false, false);

                //Labels
                Label label1 = new Label("SciFi");
                Label label2 = new Label("Historia");
                Label label3 = new Label("Deportes");
                Label label4 = new Label("Matematicas");
                Label label5 = new Label("Programacion");
                Label label6 = new Label("Diseño");
                Label label7 = new Label("Estadistica");
                Label label8 = new Label("Fisica");
                Label label9 = new Label("Algebra");
                Label label10 = new Label("Filosofia");
                Label label11 = new Label("Jardineria");
                Label label12 = new Label("Musica");
                Label label13 = new Label("Geografia");
                Label label14 = new Label("Lenguajes");
                Label label15 = new Label("Politica");
                Label label16 = new Label("Biologia");
                Label label17 = new Label("Computacion");
                Label label18 = new Label("Artes");
                Label label19 = new Label("Legal");
                Label label20 = new Label("Artesania");

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
                labelRepository.save(label11);
                labelRepository.save(label12);
                labelRepository.save(label13);
                labelRepository.save(label14);
                labelRepository.save(label15);
                labelRepository.save(label16);
                labelRepository.save(label17);
                labelRepository.save(label18);
                labelRepository.save(label19);
                labelRepository.save(label20);

                List<LabelIdDTO> philosophy = new ArrayList<>();
                philosophy.add(new LabelIdDTO(label10.getId()));
                List<LabelIdDTO> math = new ArrayList<>();
                math.add(new LabelIdDTO(label4.getId()));
                List<LabelIdDTO> geography = new ArrayList<>();
                geography.add(new LabelIdDTO(label13.getId()));
                List<LabelIdDTO> history = new ArrayList<>();
                history.add(new LabelIdDTO(label2.getId()));
                List<LabelIdDTO> music = new ArrayList<>();
                music.add(new LabelIdDTO(label12.getId()));
                List<LabelIdDTO> garden = new ArrayList<>();
                garden.add(new LabelIdDTO(label11.getId()));
                List<LabelIdDTO> biology = new ArrayList<>();
                biology.add(new LabelIdDTO(label16.getId()));
                List<LabelIdDTO> programming = new ArrayList<>();
                programming.add(new LabelIdDTO(label5.getId()));
                programming.add(new LabelIdDTO(label17.getId()));
                List<LabelIdDTO> languages = new ArrayList<>();
                languages.add(new LabelIdDTO(label14.getId()));
                List<LabelIdDTO> legal = new ArrayList<>();
                legal.add(new LabelIdDTO(label19.getId()));
                List<LabelIdDTO> handcraft = new ArrayList<>();
                handcraft.add(new LabelIdDTO(label20.getId()));
                List<LabelIdDTO> algebra = new ArrayList<>();
                algebra.add(new LabelIdDTO(label9.getId()));
                List<LabelIdDTO> arts = new ArrayList<>();
                arts.add(new LabelIdDTO(label18.getId()));
                List<LabelIdDTO> sports = new ArrayList<>();
                sports.add(new LabelIdDTO(label3.getId()));
                List<LabelIdDTO> design = new ArrayList<>();
                design.add(new LabelIdDTO(label6.getId()));


                //Study Groups
                StudyGroupForm group1 = new StudyGroupForm("Teologia", "Estudios de Dios",userId4,LocalDate.of(2020, 10, 25), philosophy);
                StudyGroupForm group2 = new StudyGroupForm("Platon", "Estudios de Platon", userId5, LocalDate.of(2020, 10, 25), philosophy);
                StudyGroupForm group3 = new StudyGroupForm("Aristoteles", "Estudios de Aristoteles", userId6, LocalDate.of(2019, 3, 6), philosophy);
                StudyGroupForm group4 = new StudyGroupForm("Analisis matematico", "Estudios de matematicas", userId7, LocalDate.of(2016, 7, 1), math);
                StudyGroupForm group5 = new StudyGroupForm("Geometria", "Estudios de las formas", userId8, LocalDate.of(2020, 10, 25), math);
                StudyGroupForm group6 = new StudyGroupForm("Geografia", "Estudios de la tierra", userId9, LocalDate.of(2019, 3, 6), geography);
                StudyGroupForm group7 = new StudyGroupForm("Historia romana", "Estudios de la historia romana", userId10, LocalDate.of(2016, 7, 1), history);
                StudyGroupForm group8 = new StudyGroupForm("Legales", "Estudios de leyes", userId11, LocalDate.of(2020, 10, 25), legal);
                StudyGroupForm group9 = new StudyGroupForm("Composicion musical", "Estudios de la musica", userId12, LocalDate.of(2019, 3, 6), music);
                StudyGroupForm group10 = new StudyGroupForm("Jardineria", "Estudios del cuidado de las plantas", userId1, LocalDate.of(2016, 7, 1), garden);
                StudyGroupForm group11 = new StudyGroupForm("Biologia", "Estudios de los seres vivos", userId2, LocalDate.of(2020, 10, 25), biology);
                StudyGroupForm group12 = new StudyGroupForm("Cartografia", "Estudios de los mapas", userId3, LocalDate.of(2019, 3, 6), geography);
                StudyGroupForm group13 = new StudyGroupForm("Lenguajes", "Estudios de lenguajes de programacion", userId4, LocalDate.of(2016, 7, 1), programming);
                StudyGroupForm group14 = new StudyGroupForm("Analisis sintactico", "Estudio y analisis de la composicion del lenguaje", userId5, LocalDate.of(2020, 10, 25), languages);
                StudyGroupForm group15 = new StudyGroupForm("Deportes", "Estudios de los deportes", userId6, LocalDate.of(2019, 3, 6), sports);
                StudyGroupForm group16 = new StudyGroupForm("Diseño grafico", "Estudios de diseño de imagen y sonido", userId7, LocalDate.of(2016, 7, 1), design);
                StudyGroupForm group17 = new StudyGroupForm("Corte y confeccion", "Estudios de las telas", userId8, LocalDate.of(2020, 10, 25), handcraft);
                StudyGroupForm group18 = new StudyGroupForm("Teatro", "Estudios de obras teatrales", userId9, LocalDate.of(2019, 3, 6), arts);
                StudyGroupForm group19 = new StudyGroupForm("Algebra", "Estudios de algebra tradicional", userId10, LocalDate.of(2016, 7, 1), algebra);
                StudyGroupForm group20 = new StudyGroupForm("IntroCom", "Estudios de introduccion a la computacion", userId11, LocalDate.of(2020, 10, 25), programming);
                StudyGroupForm group21 = new StudyGroupForm("AYED", "Estudio de algoritmos y estructura de datos", userId12, LocalDate.of(2019, 3, 6), programming);

                ArrayList<Integer> groupIds = new ArrayList<>();
                int groupId1 = studyGroupService.create(group1);
                int groupId2 = studyGroupService.create(group2);
                int groupId3 = studyGroupService.create(group3);
                int groupId4 = studyGroupService.create(group4);
                int groupId5 = studyGroupService.create(group5);
                int groupId6 = studyGroupService.create(group6);
                int groupId7 = studyGroupService.create(group7);
                int groupId8 = studyGroupService.create(group8);
                int groupId9 = studyGroupService.create(group9);
                int groupId10 = studyGroupService.create(group10);
                int groupId11 = studyGroupService.create(group11);
                int groupId12 = studyGroupService.create(group12);
                int groupId13 = studyGroupService.create(group13);
                int groupId14 = studyGroupService.create(group14);
                int groupId15 = studyGroupService.create(group15);
                int groupId16 = studyGroupService.create(group16);
                int groupId17 = studyGroupService.create(group17);
                int groupId18 = studyGroupService.create(group18);
                int groupId19 = studyGroupService.create(group19);
                int groupId20 = studyGroupService.create(group20);
                int groupId21 = studyGroupService.create(group21);

                addManyToGroup(groupId1, Arrays.asList(userId1, userId2, userId3));
                addManyToGroup(groupId2, Arrays.asList(userId2, userId3, userId4));
                addManyToGroup(groupId3, Arrays.asList(userId3, userId4, userId5));
                addManyToGroup(groupId4, Arrays.asList(userId4, userId5, userId6));
                addManyToGroup(groupId5, Arrays.asList(userId5, userId6, userId7));
                addManyToGroup(groupId6, Arrays.asList(userId6, userId7, userId8));
                addManyToGroup(groupId7, Arrays.asList(userId7, userId8, userId9));
                addManyToGroup(groupId8, Arrays.asList(userId8, userId9, userId10));
                addManyToGroup(groupId9, Arrays.asList(userId9, userId10, userId11));
                addManyToGroup(groupId10, Arrays.asList(userId10, userId11, userId12));
                addManyToGroup(groupId11, Arrays.asList(userId11, userId12, userId1));
                addManyToGroup(groupId12, Arrays.asList(userId12, userId1, userId2));
                addManyToGroup(groupId13, Arrays.asList(userId1, userId2, userId3));
                addManyToGroup(groupId14, Arrays.asList(userId2, userId3, userId4));
                addManyToGroup(groupId15, Arrays.asList(userId3, userId4, userId5));
                addManyToGroup(groupId16, Arrays.asList(userId4, userId5, userId6));
                addManyToGroup(groupId17, Arrays.asList(userId3, userId6, userId7));
                addManyToGroup(groupId18, Arrays.asList(userId3, userId7, userId8));
                addManyToGroup(groupId19, Arrays.asList(userId3, userId8, userId9));
                addManyToGroup(groupId20, Arrays.asList(userId3, userId9, userId10));
                addManyToGroup(groupId21, Arrays.asList(userId3, userId10, userId11));

            } catch (Exception ignored){}
        }
    }
}
