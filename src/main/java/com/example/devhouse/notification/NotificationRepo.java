package com.example.devhouse.notification;

import com.example.devhouse.user_things.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface NotificationRepo extends JpaRepository<Notification, Long> {
    List<Notification> findAllByUser(User user);

    List<Notification> findByCreatedAtGreaterThan(Date lastChecked);

    List<Notification> findAllByUserAndCreatedAtGreaterThan(User user, Date lastNotificationDate);
}
