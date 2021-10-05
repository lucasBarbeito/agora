package com.agora.agora;

import com.agora.agora.model.form.StudyGroupForm;
import com.agora.agora.model.form.UserForm;
import com.agora.agora.service.StudyGroupService;
import com.agora.agora.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class StartUpDatabase {
    private final UserService userService;
    private final StudyGroupService studyGroupService;

    @Autowired
    public StartUpDatabase(UserService userService, StudyGroupService studyGroupService) {
        this.userService = userService;
        this.studyGroupService = studyGroupService;
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

                //Study Groups
                StudyGroupForm group1 = new StudyGroupForm("Teologia", "Estudios de Dios",userId4,LocalDate.of(2020, 10, 25));
                StudyGroupForm group2 = new StudyGroupForm("Platon", "Estudios de Platon", userId5, LocalDate.of(2020, 10, 25));
                StudyGroupForm group3 = new StudyGroupForm("Aristoteles", "Estudios de Aristoteles", userId6, LocalDate.of(2019, 3, 6));
                StudyGroupForm group4 = new StudyGroupForm("Analisis matematico", "Estudios de matematicas", userId7, LocalDate.of(2016, 7, 1));
                StudyGroupForm group5 = new StudyGroupForm("Geometria", "Estudios de las formas", userId8, LocalDate.of(2020, 10, 25));
                StudyGroupForm group6 = new StudyGroupForm("Geografia", "Estudios de la tierra", userId9, LocalDate.of(2019, 3, 6));
                StudyGroupForm group7 = new StudyGroupForm("Historia romana", "Estudios de la historia romana", userId10, LocalDate.of(2016, 7, 1));
                StudyGroupForm group8 = new StudyGroupForm("Legales", "Estudios de leyes", userId11, LocalDate.of(2020, 10, 25));
                StudyGroupForm group9 = new StudyGroupForm("Composicion musical", "Estudios de la musica", userId12, LocalDate.of(2019, 3, 6));
                StudyGroupForm group10 = new StudyGroupForm("Jardineria", "Estudios del cuidado de las plantas", userId1, LocalDate.of(2016, 7, 1));
                StudyGroupForm group11 = new StudyGroupForm("Biologia", "Estudios de los seres vivos", userId2, LocalDate.of(2020, 10, 25));
                StudyGroupForm group12 = new StudyGroupForm("Cartografia", "Estudios de los mapas", userId3, LocalDate.of(2019, 3, 6));
                StudyGroupForm group13 = new StudyGroupForm("Lenguajes", "Estudios de lenguajes de programacion", userId4, LocalDate.of(2016, 7, 1));
                StudyGroupForm group14 = new StudyGroupForm("Analisis sintactico", "Estudio y analisis de la composicion del lenguaje", userId5, LocalDate.of(2020, 10, 25));
                StudyGroupForm group15 = new StudyGroupForm("Deportes", "Estudios de los deportes", userId6, LocalDate.of(2019, 3, 6));
                StudyGroupForm group16 = new StudyGroupForm("Diseño grafico", "Estudios de diseño de imagen y sonido", userId7, LocalDate.of(2016, 7, 1));
                StudyGroupForm group17 = new StudyGroupForm("Corte y confeccion", "Estudios de las telas", userId8, LocalDate.of(2020, 10, 25));
                StudyGroupForm group18 = new StudyGroupForm("Teatro", "Estudios de obras teatrales", userId9, LocalDate.of(2019, 3, 6));
                StudyGroupForm group19 = new StudyGroupForm("Algebra", "Estudios de algebra tradicional", userId10, LocalDate.of(2016, 7, 1));
                StudyGroupForm group20 = new StudyGroupForm("IntroCom", "Estudios de introduccion a la computacion", userId11, LocalDate.of(2020, 10, 25));
                StudyGroupForm group21 = new StudyGroupForm("AYED", "Estudio de algoritmos y estructura de datos", userId12, LocalDate.of(2019, 3, 6));

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
