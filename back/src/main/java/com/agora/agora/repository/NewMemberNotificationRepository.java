package com.agora.agora.repository;

import com.agora.agora.model.NewMemberNotification;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface NewMemberNotificationRepository extends CrudRepository<NewMemberNotification, Integer> {

    List<NewMemberNotification> findAllByUserId(int userId);
}
