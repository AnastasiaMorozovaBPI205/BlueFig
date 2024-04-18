package app.bluefig.controller;

import app.bluefig.MapStructMapper;
import app.bluefig.dto.ModuleWithParametersDTO;
import app.bluefig.model.Notification;
import app.bluefig.service.NotificationServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
public class NotificationController {
    @Autowired
    NotificationServiceImpl notificationService;
    @Autowired
    private MapStructMapper mapper;

    @RequestMapping(path="/notifications/{userId}", method=RequestMethod.GET,
            produces = "application/json;charset=UTF-8")
    public List<Notification> getNotifications(@PathVariable String userId) {
        if (userId == null) {
            throw new ResponseStatusException(
                    HttpStatus.UNSUPPORTED_MEDIA_TYPE, "Не заполнены поля."
            );
        }

        return mapper.NotificationJpasToNotifications(notificationService.getNotificationsByUserId(userId));
    }
}
