package fpt.project.bsmart.repository;

import fpt.project.bsmart.entity.Notifier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface NotifierRepository extends JpaRepository<Notifier, Long> {
}
