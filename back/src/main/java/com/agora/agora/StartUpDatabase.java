package com.agora.agora;

import com.agora.agora.model.dto.LabelDTO;
import com.agora.agora.model.dto.LabelIdDTO;
import com.agora.agora.model.form.LabelForm;
import com.agora.agora.model.form.PostForm;
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
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class StartUpDatabase {
    private final UserService userService;
    private final StudyGroupService studyGroupService;
    private final LabelRepository labelRepository;
    private final PostRepository postRepository;

    @Autowired

    public StartUpDatabase(UserService userService, StudyGroupService studyGroupService, LabelRepository labelRepository, PostRepository postRepository) {
        this.userService = userService;
        this.studyGroupService = studyGroupService;
        this.labelRepository = labelRepository;
        this.postRepository = postRepository;
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

                User u1 = userService.findById(userId1).get();
                User u2 = userService.findById(userId2).get();
                User u3 = userService.findById(userId3).get();
                User u4 = userService.findById(userId4).get();
                User u5 = userService.findById(userId5).get();
                User u6 = userService.findById(userId6).get();
                User u7 = userService.findById(userId7).get();
                User u8 = userService.findById(userId8).get();
                User u9 = userService.findById(userId9).get();
                User u10 = userService.findById(userId10).get();
                User u11 = userService.findById(userId11).get();
                User u12 = userService.findById(userId12).get();

                StudyGroup sg1 = studyGroupService.findStudyGroupById(groupId1).get();
                StudyGroup sg2 = studyGroupService.findStudyGroupById(groupId2).get();
                StudyGroup sg3 = studyGroupService.findStudyGroupById(groupId3).get();
                StudyGroup sg4 = studyGroupService.findStudyGroupById(groupId4).get();
                StudyGroup sg5 = studyGroupService.findStudyGroupById(groupId5).get();
                StudyGroup sg6 = studyGroupService.findStudyGroupById(groupId6).get();
                StudyGroup sg7 = studyGroupService.findStudyGroupById(groupId7).get();
                StudyGroup sg8 = studyGroupService.findStudyGroupById(groupId8).get();
                StudyGroup sg9 = studyGroupService.findStudyGroupById(groupId9).get();
                StudyGroup sg10 = studyGroupService.findStudyGroupById(groupId10).get();
                StudyGroup sg11 = studyGroupService.findStudyGroupById(groupId11).get();
                StudyGroup sg12 = studyGroupService.findStudyGroupById(groupId12).get();
                StudyGroup sg13 = studyGroupService.findStudyGroupById(groupId13).get();
                StudyGroup sg14 = studyGroupService.findStudyGroupById(groupId14).get();
                StudyGroup sg15 = studyGroupService.findStudyGroupById(groupId15).get();
                StudyGroup sg16 = studyGroupService.findStudyGroupById(groupId16).get();
                StudyGroup sg17 = studyGroupService.findStudyGroupById(groupId17).get();
                StudyGroup sg18 = studyGroupService.findStudyGroupById(groupId18).get();
                StudyGroup sg19 = studyGroupService.findStudyGroupById(groupId19).get();
                StudyGroup sg20 = studyGroupService.findStudyGroupById(groupId20).get();
                StudyGroup sg21 = studyGroupService.findStudyGroupById(groupId21).get();

                //Posts
                //PostForm post1 = new PostForm("Dios es bueno", LocalDateTime.of(2019, 2,18, 0, 0));
                //studyGroupService.createPost(groupId1, post1);
                Post post1 = new Post("Dios es bueno", sg1, u1, LocalDateTime.of(2019, 2,18, 0, 0));
                //PostForm post2 = new PostForm("Platon era Crack", LocalDateTime.of(2021, 5,18, 0, 0));
                //studyGroupService.createPost(groupId2, post2);
                Post post2 = new Post("Platon era Crack", sg2, u2, LocalDateTime.of(2021, 5,18, 0, 0));
                //PostForm post3 = new PostForm("Aristoteles era un ^%*^**^#", LocalDateTime.of(2020, 1,2, 0, 0));
                //studyGroupService.createPost(groupId3, post3);
                Post post3 = new Post("Aristoteles era un ^%*^**^#", sg3, u3, LocalDateTime.of(2020, 1,2, 0, 0));
                //PostForm post4 = new PostForm("Como odio estas integrales del #*&%&", LocalDateTime.of(2017, 4,9, 0, 0));
                //studyGroupService.createPost(groupId4, post4);
                Post post4 = new Post("Como odio estas integrales del #*&%&", sg4, u4, LocalDateTime.of(2017, 4,9, 0, 0));
                //PostForm post5 = new PostForm("Argentina queda en America", LocalDateTime.of(2021, 4,23, 0, 0));
                //studyGroupService.createPost(groupId6, post5);
                Post post5 = new Post("Argentina queda en America", sg6, u6, LocalDateTime.of(2021, 4,23, 0, 0));
                //PostForm post6 = new PostForm("La ley 27546 claramente dice que esta mal", LocalDateTime.of(2019, 4,15, 0, 0));
                //studyGroupService.createPost(groupId8, post6);
                Post post6 = new Post("La ley 27546 claramente dice que esta mal", sg8, u8, LocalDateTime.of(2019, 4,15, 0, 0));
                //PostForm post7 = new PostForm("Cuando ocurre la Fotosintesis?", LocalDateTime.of(2021, 7,2, 0, 0));
                //studyGroupService.createPost(groupId11, post7);
                Post post7 = new Post("Cuando ocurre la Fotosintesis?", sg11, u11, LocalDateTime.of(2021, 7,2, 0, 0));
                //PostForm post8 = new PostForm("Para mañana aprendase la letra de Persiana Americana de Soda Stereo", LocalDateTime.of(2021, 3,18, 0, 0));
                //studyGroupService.createPost(groupId9, post8);
                Post post8 = new Post("Para mañana aprendase la letra de Persiana Americana de Soda Stereo", sg9, u9, LocalDateTime.of(2021, 3,18, 0, 0));
                //PostForm post9 = new PostForm("Cual era la diferencia entre lenguaje Funcional y OO?", LocalDateTime.of(2021, 6,20, 0, 0));
                //studyGroupService.createPost(groupId13, post9);
                Post post9 = new Post("Cual era la diferencia entre lenguaje Funcional y OO?", sg13, u1,  LocalDateTime.of(2021, 6,20, 0, 0));
                //PostForm post10 = new PostForm("Ser o no ser, esa es la cuestion", LocalDateTime.of(2020, 11,6, 0, 0));
                //studyGroupService.createPost(groupId18, post10);
                Post post10 = new Post("Ser o no ser, esa es la cuestion.", sg18, u7, LocalDateTime.of(2020, 11,6, 0, 0));
                //PostForm post11 = new PostForm("Acuerdense de la tarea d Photoshop", LocalDateTime.of(2019, 7,28, 0, 0));
                //studyGroupService.createPost(groupId16, post11);
                Post post11 = new Post("Acuerdense de la tarea d Photoshop", sg16, u4, LocalDateTime.of(2019, 7,28, 0, 0));
                //PostForm post12 = new PostForm("La matriz por su identidad es igual a la matriz.", LocalDateTime.of(2021, 12,18, 0, 0));
                //studyGroupService.createPost(groupId19, post12);
                Post post12 = new Post("La matriz por su identidad es igual a la matriz.", sg19, u8, LocalDateTime.of(2021, 12,18, 0, 0));
                //PostForm post13 = new PostForm("Acuerdense de descargar Ubuntu 20.04", LocalDateTime.of(2021, 7,3, 0, 0));
                //studyGroupService.createPost(groupId20, post13);
                Post post13 = new Post("Acuerdense de descargar Ubuntu 20.04", sg20, u10, LocalDateTime.of(2021, 7,3, 0, 0));
                //PostForm post14 = new PostForm("Acuerdense de la tarea de Alicia", LocalDateTime.of(2020, 5,19, 0, 0));
                //studyGroupService.createPost(groupId21, post14);
                Post post14 = new Post("Acuerdense de la tarea de Alicia", sg21, u3, LocalDateTime.of(2020, 5,19, 0, 0));
                //PostForm post15 = new PostForm("En que año se fundo Roma?", LocalDateTime.of(2021, 1,24, 0, 0));
                //studyGroupService.createPost(groupId7, post15);
                Post post15 = new Post("En que año se fundo Roma?", sg7, u7, LocalDateTime.of(2021, 1,24, 0, 0));
                //PostForm post16 = new PostForm("Alguno tiene tela para el lunes?", LocalDateTime.of(2020, 4,29, 0, 0));
                //studyGroupService.createPost(groupId17, post16);
                Post post16 = new Post("Alguno tiene tela para el lunes?", sg17, u3, LocalDateTime.of(2020, 4,29, 0, 0));
                //PostForm post17 = new PostForm("Las rosas tienen espinas, cuidado!", LocalDateTime.of(2021, 5,30, 0, 0));
                //studyGroupService.createPost(groupId10, post17);
                Post post17 = new Post("Las rosas tienen espinas, cuidado!", sg10, u10, LocalDateTime.of(2021, 5,30, 0, 0));
                //PostForm post18 = new PostForm("El area de un circulo es pi*r^2", LocalDateTime.of(2021, 2,2, 0, 0));
                //studyGroupService.createPost(groupId5, post18);
                Post post18 = new Post("El area de un circulo es pi*r^2", sg5, u5, LocalDateTime.of(2021, 2,2, 0, 0));
                //PostForm post19 = new PostForm("Podemos ver que la corriente fluye hacia el norte", LocalDateTime.of(2020, 6,12, 0, 0));
                //studyGroupService.createPost(groupId12, post19);
                Post post19 = new Post("Podemos ver que la corriente fluye hacia el norte", sg12, u12, LocalDateTime.of(2020, 6,12, 0, 0));
                //PostForm post20 = new PostForm("Pablito clavo un clavito. Cual es. Cual es el sujeto y el predicado?", LocalDateTime.of(2020, 10,18, 0, 0));
                //studyGroupService.createPost(groupId14, post20);
                Post post20 = new Post("Pablito clavo un clavito. Cual es. Cual es el sujeto y el predicado?", sg14, u2, LocalDateTime.of(2020, 10,18, 0, 0));
                //PostForm post21 = new PostForm("Elixir es funcional y Java OO, saca conclusiones...", LocalDateTime.of(2021, 6,21, 0, 0));
                //studyGroupService.createPost(groupId13, post21);
                Post post21 = new Post("Elixir es funcional y Java OO, saca conclusiones...", sg13, u3, LocalDateTime.of(2021, 6,21, 0, 0));
                //PostForm post22 = new PostForm("Por algo pregunto imbecil!", LocalDateTime.of(2021, 6,22, 0, 0));
                //studyGroupService.createPost(groupId13, post22);
                Post post22 = new Post("Por algo pregunto imbecil!", sg13, u1, LocalDateTime.of(2021, 6,22, 0, 0));
                //PostForm post23 = new PostForm("El primer mundial de futbol fue en el año 1930", LocalDateTime.of(2021, 7,7, 0, 0));
                //studyGroupService.createPost(groupId15, post23);
                Post post23 = new Post("El primer mundial de futbol fue en el año 1930", sg15, u5, LocalDateTime.of(2021, 7,7, 0, 0));
                //PostForm post24 = new PostForm("El primer mundial de rugby fue en el año 1987", LocalDateTime.of(2021, 4,19, 0, 0));
                //studyGroupService.createPost(groupId15, post24);
                Post post24 = new Post("El primer mundial de rugby fue en el año 1987", sg15, u4, LocalDateTime.of(2021, 4,19, 0, 0));

                postRepository.save(post1);
                postRepository.save(post2);
                postRepository.save(post3);
                postRepository.save(post4);
                postRepository.save(post5);
                postRepository.save(post6);
                postRepository.save(post7);
                postRepository.save(post8);
                postRepository.save(post9);
                postRepository.save(post10);
                postRepository.save(post11);
                postRepository.save(post12);
                postRepository.save(post13);
                postRepository.save(post14);
                postRepository.save(post15);
                postRepository.save(post16);
                postRepository.save(post17);
                postRepository.save(post18);
                postRepository.save(post19);
                postRepository.save(post20);
                postRepository.save(post21);
                postRepository.save(post22);
                postRepository.save(post23);
                postRepository.save(post24);



            } catch (Exception ignored){}
        }
    }
}
