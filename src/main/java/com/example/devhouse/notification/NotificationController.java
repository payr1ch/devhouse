package com.example.devhouse.notification;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @GetMapping("/allNotification/{userId}")
    public List<Notification> getNotificationsByUser(@PathVariable UUID userId) {
        return notificationService.getNotificationsByUserId(userId);
    }

    @PutMapping("/update/{notificationId}")
    public void updateNotificationStatus(@PathVariable Long notificationId) {
        notificationService.updateNotificationStatus(notificationId);
    }

    @DeleteMapping("/clear/{userId}")
    public void clearAllMyNotifications(@PathVariable UUID userId) {
        notificationService.clearAllMyNotifications(userId);
    }

    @GetMapping("/poll")
    public List<Notification> pollNotifications(@RequestParam("lastChecked") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Date lastChecked) {
        return notificationService.getNewNotifications(lastChecked);
    }
    @GetMapping(value = "/sse", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ServerSentEvent<Notification>> streamNotifications(@RequestParam("userId") UUID userId) {
        return notificationService.streamAllNotifications(userId);
    }
}
