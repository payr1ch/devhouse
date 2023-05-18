package com.example.devhouse.notification;

import com.example.devhouse.user_things.user.User;
import com.example.devhouse.user_things.user.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import java.time.Duration;
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


    public Flux<ServerSentEvent<Notification>> streamAllNotifications(UUID userId) {
        User user = userRepo.findUserByUserId(userId);
        List<Notification> existingNotifications = notificationRepo.findAllByUser(user);
        final Date[] lastNotificationDate = {existingNotifications.isEmpty() ? new Date() : existingNotifications.get(existingNotifications.size() - 1).getCreatedAt()};

        Flux<ServerSentEvent<Notification>> existingNotificationsFlux = Flux.fromIterable(existingNotifications)
                .map(notification -> ServerSentEvent.builder(notification).build());

        Flux<ServerSentEvent<Notification>> newNotificationsFlux = Flux.interval(Duration.ofSeconds(1))
                .flatMap(i -> {
                    List<Notification> newNotifications = notificationRepo.findAllByUserAndCreatedAtGreaterThan(user, lastNotificationDate[0]);
                    if (!newNotifications.isEmpty()) {
                        lastNotificationDate[0] = newNotifications.get(newNotifications.size() - 1).getCreatedAt();
                    }
                    return Flux.fromIterable(newNotifications);
                })
                .map(notification -> ServerSentEvent.builder(notification).build());

        return Flux.concat(existingNotificationsFlux, newNotificationsFlux);
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
