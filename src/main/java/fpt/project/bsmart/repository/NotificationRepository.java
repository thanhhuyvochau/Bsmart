package fpt.project.bsmart.repository;

import fpt.project.bsmart.entity.Notification;
import fpt.project.bsmart.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import vn.grooo.vietdang.entity.Account;
import vn.grooo.vietdang.entity.Notification;
import vn.grooo.vietdang.util.common.Constants;

import java.util.List;
import java.util.Optional;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long>, JpaSpecificationExecutor<Notification> {

    Long countAllByUser(User user);
}
