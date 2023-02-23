package fpt.project.bsmart.repository;

import fpt.project.bsmart.entity.NotificationType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface NotificationTypeRepository extends JpaRepository<NotificationType, Long> {
}
