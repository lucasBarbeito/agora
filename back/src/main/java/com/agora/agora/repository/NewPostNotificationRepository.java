package com.agora.agora.repository;

import com.agora.agora.model.NewMemberNotification;
import com.agora.agora.model.NewPostNotification;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface NewPostNotificationRepository extends CrudRepository<NewPostNotification, Integer> {

    List<NewPostNotification> findAllByUserId(int userId);
}
