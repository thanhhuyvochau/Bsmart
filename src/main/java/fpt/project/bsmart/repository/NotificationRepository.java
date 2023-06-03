package fpt.project.bsmart.repository;

import fpt.project.bsmart.entity.Notification;
import fpt.project.bsmart.entity.User;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


import java.util.List;
import java.util.Optional;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    Long countAllByUser(User user);
}
