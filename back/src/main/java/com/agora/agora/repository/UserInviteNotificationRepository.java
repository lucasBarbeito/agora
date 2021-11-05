package com.agora.agora.repository;

import com.agora.agora.model.NewMemberNotification;
import com.agora.agora.model.UserInviteNotification;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UserInviteNotificationRepository extends CrudRepository<UserInviteNotification, Integer> {

    List<UserInviteNotification> findAllByUserId(int userId);
}
