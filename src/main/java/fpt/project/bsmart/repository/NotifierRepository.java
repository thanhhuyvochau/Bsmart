package fpt.project.bsmart.repository;

import fpt.project.bsmart.entity.Notifier;
import fpt.project.bsmart.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotifierRepository extends JpaRepository<Notifier, Long> {
    Page<Notifier> findAllByUser(User user, Pageable pageable);
}
