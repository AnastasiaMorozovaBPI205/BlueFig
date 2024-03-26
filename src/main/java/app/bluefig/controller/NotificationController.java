package app.bluefig.controller;

import app.bluefig.MapStructMapper;
import app.bluefig.dto.ModuleWithParametersDTO;
import app.bluefig.model.Notification;
import app.bluefig.service.NotificationServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
public class NotificationController {
    @Autowired
    NotificationServiceImpl notificationService;
    @Autowired
    private MapStructMapper mapper;

    @GetMapping("/notifications/{userId}")
    public List<Notification> getNotifications(@PathVariable String userId) {
        if (userId == null) {
            throw new ResponseStatusException(
                    HttpStatus.UNSUPPORTED_MEDIA_TYPE, "Не заполнены поля."
            );
        }

        return mapper.NotificationJpasToNotifications(notificationService.getNotificationsByUserId(userId));
    }
}
