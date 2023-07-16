package fpt.project.bsmart.repository;

import fpt.project.bsmart.entity.Notification;
import fpt.project.bsmart.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    Long countAllByUser(User user);
}
