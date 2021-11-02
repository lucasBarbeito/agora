package com.agora.agora.repository;

import com.agora.agora.model.GroupInviteNotification;
import com.agora.agora.model.NewMemberNotification;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface GroupInviteNotificationRepository extends CrudRepository<GroupInviteNotification,Integer> {
    List<GroupInviteNotification> findAllByUserId(int userId);

    void deleteAllByUserId(int userId);
}
