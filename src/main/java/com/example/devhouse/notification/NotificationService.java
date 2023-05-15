package com.example.devhouse.notification;

import com.example.devhouse.user_things.user.User;
import com.example.devhouse.user_things.user.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class NotificationService {

    @Autowired
    private NotificationRepo notificationRepo;

    @Autowired
    private UserRepo userRepo;


    public List<Notification> getNewNotifications(Date lastChecked) {
        return notificationRepo.findByCreatedAtGreaterThan(lastChecked);
    }

    public List<Notification> getNotificationsByUserId(UUID userId) {
        User user = userRepo.findUserByUserId(userId);
        return notificationRepo.findAllByUser(user);
    }

    public void createNotification(String title, String description, User user) {
        Notification notification = new Notification();
        notification.setTitle(title);
        notification.setDescription(description);
        notification.setUser(user);
        notification.setStatus("unseen");
        notification.setCreatedAt(new Date());
        notificationRepo.save(notification);
    }

    public void updateNotificationStatus(Long notificationId) {
        Notification notification = notificationRepo.findById(notificationId).orElse(null);
        if (notification != null) {
            notification.setStatus("seen");
            notificationRepo.save(notification);
        }
    }

    public void clearAllMyNotifications(UUID userId) {
        User user = userRepo.findUserByUserId(userId);
        List<Notification> notifications = notificationRepo.findAllByUser(user);
        notificationRepo.deleteAll(notifications);
    }
}
