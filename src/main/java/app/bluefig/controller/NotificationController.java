package app.bluefig.controller;

import app.bluefig.entity.ModuleFillInJpa;
import app.bluefig.entity.QuestionaryAnswerJpa;
import app.bluefig.entity.QuestionaryJpa;
import app.bluefig.entity.UserJpa;
import app.bluefig.mapper.MapStructMapper;
import app.bluefig.model.Notification;
import app.bluefig.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@CrossOrigin(origins = "*")
@RestController
public class NotificationController {
    @Autowired
    NotificationServiceImpl notificationService;
    @Autowired
    private UserServiceImpl userService;
    @Autowired
    private QuestionaryServiceImpl questionaryService;
    @Autowired
    private ModuleFillInServiceImpl moduleFillInService;
    @Autowired
    private ModuleServiceImpl moduleService;
    @Autowired
    private MapStructMapper mapper;

    /**
     * Получение всех уведомлений пользователя, отсортированные по дате и времени.
     * @param userId id пользователя
     * @return все уведомления пользователя
     */
    @GetMapping(path="/notifications/{userId}", produces = "application/json;charset=UTF-8")
    public List<Notification> getNotifications(@PathVariable String userId) {
        if (userId == null) {
            throw new ResponseStatusException(
                    HttpStatus.UNSUPPORTED_MEDIA_TYPE, "Не заполнены поля."
            );
        }

        return mapper.NotificationJpasToNotifications(notificationService.getNotificationsByUserId(userId));
    }

    /**
     * Уведомление пациентов о необходимости заполнить модуль при достижении нового срока заполнения.
     * Отправляются каждый день в 9 часов.
     */
    @Scheduled(cron = "0 0 9 * * *")
    public void notifyPatientAboutModule() {
        List<UserJpa> users = userService.findPatients();

        for (UserJpa user : users) {
            List<QuestionaryJpa> questionaries = questionaryService.findQuestionaryJpaByPatientId(user.getId());

            for (QuestionaryJpa questionary : questionaries) {
                ModuleFillInJpa lastFillIn = moduleFillInService.findModulesFillInJpaByPatientIdQuestionaryId(
                        questionary.getId(), user.getId()).get(0);

                if (ifTimeToNotifyToFill(questionary, lastFillIn, questionary.getFrequency())) {
                    String moduleName = moduleService.getQuestionaryModuleName(questionary.getId());
                    notificationService.addNotification(user.getId(),
                            "Сегодня нужно заполнить модуль " + moduleName + ".",
                            LocalDateTime.now());
                }
            }
        }
    }

    private boolean ifTimeToNotifyToFill(QuestionaryJpa questionary, ModuleFillInJpa lastFillIn, int frequency) {
        return lastFillIn == null &&
                (questionary.getDatetime().toLocalDate().plusDays(frequency).equals(LocalDate.now())
                || questionary.getDatetime().toLocalDate().plusDays(frequency).isAfter(LocalDate.now()))
                || lastFillIn != null &&
                (lastFillIn.getDatetime().toLocalDate().plusDays(frequency).equals(LocalDate.now())
                || lastFillIn.getDatetime().toLocalDate().plusDays(frequency).isAfter(LocalDate.now()));
    }
}
