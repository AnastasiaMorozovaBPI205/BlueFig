package app.bluefig.repository;

import app.bluefig.entity.NotificationJpa;
import app.bluefig.entity.QuestionaryJpa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface NotificationJpaRepository extends JpaRepository<NotificationJpa, String> {
    @Query(value = "insert into notifications (user_id, text, datetime) values (:user_id, :text, :datetime)",
            nativeQuery = true)
    void addNotification(@Param("user_id") String doctorId, @Param("text") String text,
                         @Param("datetime") LocalDateTime datetime);

    @Query(value = "select * from notifications where user_id = :user_id order by datetime desc",
            nativeQuery = true)
    List<NotificationJpa> findNotificationsByUserId(@Param("user_id") String userId);
}
