package app.bluefig.service;

import app.bluefig.entity.NotificationJpa;
import app.bluefig.repository.NotificationJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class NotificationServiceImpl implements NotificationService{
    @Autowired
    NotificationJpaRepository notificationJpaRepository;

    @Override
    public List<NotificationJpa> getNotificationsByUserId(String userId) {
        return notificationJpaRepository.findNotificationsByUserId(userId);
    }

    @Override
    public void addNotification(String userId, String text, LocalDateTime datetime) {
        notificationJpaRepository.addNotification(userId, text, datetime);
    }

}
