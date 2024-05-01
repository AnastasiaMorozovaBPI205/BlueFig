package app.bluefig;

import app.bluefig.controller.NotificationController;
import app.bluefig.entity.NotificationJpa;
import app.bluefig.mapper.MapStructMapper;
import app.bluefig.model.Notification;
import app.bluefig.service.NotificationServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import(MapStructMapper.class)
@WebMvcTest(NotificationController.class)
public class NotificationTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private NotificationServiceImpl service;
    @MockBean
    private MapStructMapper mapper;

    @Test
    void getNotificationByUserIdTest() throws Exception {
        List<NotificationJpa> notificationJpaList = List.of(getNotificationJpa());
        Mockito.when(this.service.getNotificationsByUserId("11")).thenReturn(notificationJpaList);
        Mockito.when(this.mapper.NotificationJpasToNotifications(notificationJpaList))
                .thenReturn(List.of(getNotification()));

        mockMvc.perform(get("/notifications/11"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value("1"));
    }

    private NotificationJpa getNotificationJpa() {
        NotificationJpa notificationJpa = new NotificationJpa();
        notificationJpa.setId("1");
        notificationJpa.setDatetime(LocalDateTime.now());
        notificationJpa.setUserId("11");
        notificationJpa.setText("Get well!");

        return notificationJpa;
    }

    private Notification getNotification() {
        Notification notification = new Notification();
        notification.setId("1");
        notification.setDatetime(LocalDateTime.now());
        notification.setUserId("11");
        notification.setText("Get well!");

        return notification;
    }
}
