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

@Component
public class StartUpDatabase {
    private final UserService userService;
    private final StudyGroupService studyGroupService;

    @Autowired
    public StartUpDatabase(UserService userService, StudyGroupService studyGroupService) {
        this.userService = userService;
        this.studyGroupService = studyGroupService;
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
                UserForm user10 = new UserForm("Micaela", "Diaz", "alonso@gmail.com", "Micaela2017");
                UserForm user11 = new UserForm("Fernando", "Rojas", "lopez@gmail.com","Fernando2016");
                UserForm user12 = new UserForm("Juan", "Cruz", "diaz@gmail.com", "Juan2015");

                ArrayList<Integer> userIds = new ArrayList<>();
                userIds.add(userService.saveCustom(user1,true, false));
                userIds.add(userService.saveCustom(user2,true, false));
                userIds.add(userService.saveCustom(user3,true, false));
                userIds.add(userService.saveCustom(user4,true, false));
                userIds.add(userService.saveCustom(user5,true, false));
                userIds.add(userService.saveCustom(user6,false, false));
                userIds.add(userService.saveCustom(user7,false, false));
                userIds.add(userService.saveCustom(user8,false, false));
                userIds.add(userService.saveCustom(user9,false, false));
                userIds.add(userService.saveCustom(user10,false, false));
                userIds.add(userService.saveCustom(user11,false, false));
                userIds.add(userService.saveCustom(user12,false, false));

                //Study Groups
                StudyGroupForm group1 = new StudyGroupForm("Teologia", "Estudios de Dios",userIds.get(0),LocalDate.of(2020, 10, 25));
                StudyGroupForm group2 = new StudyGroupForm("Platon", "Estudios de Platon", userIds.get(2), LocalDate.of(2020, 10, 25));
                StudyGroupForm group3 = new StudyGroupForm("Aristoteles", "Estudios de Aristoteles", userIds.get(4), LocalDate.of(2019, 3, 6));
                StudyGroupForm group4 = new StudyGroupForm("Analisis matematico", "Estudios de matematicas", userIds.get(6), LocalDate.of(2016, 7, 1));
                StudyGroupForm group5 = new StudyGroupForm("Geometria", "Estudios de las formas", userIds.get(8), LocalDate.of(2020, 10, 25));
                StudyGroupForm group6 = new StudyGroupForm("Geografia", "Estudios de la tierra", userIds.get(10), LocalDate.of(2019, 3, 6));
                StudyGroupForm group7 = new StudyGroupForm("Historia romana", "Estudios de la historia romana", userIds.get(1), LocalDate.of(2016, 7, 1));
                StudyGroupForm group8 = new StudyGroupForm("Legales", "Estudios de leyes", userIds.get(3), LocalDate.of(2020, 10, 25));
                StudyGroupForm group9 = new StudyGroupForm("Composicion musical", "Estudios de la musica", userIds.get(5), LocalDate.of(2019, 3, 6));
                StudyGroupForm group10 = new StudyGroupForm("Jardineria", "Estudios del cuidado de las plantas", userIds.get(7), LocalDate.of(2016, 7, 1));
                StudyGroupForm group11 = new StudyGroupForm("Biologia", "Estudios de los seres vivos", userIds.get(9), LocalDate.of(2020, 10, 25));
                StudyGroupForm group12 = new StudyGroupForm("Cartografia", "Estudios de los mapas", userIds.get(11), LocalDate.of(2019, 3, 6));
                StudyGroupForm group13 = new StudyGroupForm("Lenguajes", "Estudios de lenguajes de programacion", userIds.get(0), LocalDate.of(2016, 7, 1));
                StudyGroupForm group14 = new StudyGroupForm("Analisis sintactico", "Estudio y analisis de la composicion del lenguaje", userIds.get(2), LocalDate.of(2020, 10, 25));
                StudyGroupForm group15 = new StudyGroupForm("Deportes", "Estudios de los deportes", userIds.get(4), LocalDate.of(2019, 3, 6));
                StudyGroupForm group16 = new StudyGroupForm("Diseño grafico", "Estudios de diseño de imagen y sonido", userIds.get(6), LocalDate.of(2016, 7, 1));
                StudyGroupForm group17 = new StudyGroupForm("Corte y confeccion", "Estudios de las telas", userIds.get(8), LocalDate.of(2020, 10, 25));
                StudyGroupForm group18 = new StudyGroupForm("Teatro", "Estudios de obras teatrales", userIds.get(10), LocalDate.of(2019, 3, 6));
                StudyGroupForm group19 = new StudyGroupForm("Algebra", "Estudios de algebra tradicional", userIds.get(1), LocalDate.of(2016, 7, 1));
                StudyGroupForm group20 = new StudyGroupForm("IntroCom", "Estudios de introduccion a la computacion", userIds.get(3), LocalDate.of(2020, 10, 25));
                StudyGroupForm group21 = new StudyGroupForm("AYED", "Estudio de algoritmos y estructura de datos", userIds.get(5), LocalDate.of(2019, 3, 6));

                ArrayList<Integer> groupIds = new ArrayList<>();
                groupIds.add(studyGroupService.create(group1));
                groupIds.add(studyGroupService.create(group2));
                groupIds.add(studyGroupService.create(group3));
                groupIds.add(studyGroupService.create(group4));
                groupIds.add(studyGroupService.create(group5));
                groupIds.add(studyGroupService.create(group6));
                groupIds.add(studyGroupService.create(group7));
                groupIds.add(studyGroupService.create(group8));
                groupIds.add(studyGroupService.create(group9));
                groupIds.add(studyGroupService.create(group10));
                groupIds.add(studyGroupService.create(group11));
                groupIds.add(studyGroupService.create(group12));
                groupIds.add(studyGroupService.create(group13));
                groupIds.add(studyGroupService.create(group14));
                groupIds.add(studyGroupService.create(group15));
                groupIds.add(studyGroupService.create(group16));
                groupIds.add(studyGroupService.create(group17));
                groupIds.add(studyGroupService.create(group18));
                groupIds.add(studyGroupService.create(group19));
                groupIds.add(studyGroupService.create(group20));
                groupIds.add(studyGroupService.create(group21));

                int count = 0;
                for (Integer groupId: groupIds) {
                    ArrayList<Integer> list = new ArrayList<>();
                    for (int i = 0; i < 12; i++) {
                        list.add(i);
                    }
                    count++;
                    for (int i = count%10; i < count%10+3; i++) {
                        studyGroupService.addUserToStudyGroup(groupId,userIds.get(list.get(i)));
                    }
                }

            } catch (Exception ignored){}
        }
    }
}
