package app.bluefig.service;

import app.bluefig.entity.NotificationJpa;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface NotificationService {
    List<NotificationJpa> getNotificationsByUserId(String userId);
    void addNotification(String userId, String text, LocalDateTime datetime);
}
