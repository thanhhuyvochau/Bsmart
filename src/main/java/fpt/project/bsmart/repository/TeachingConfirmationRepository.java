package fpt.project.bsmart.repository;

import fpt.project.bsmart.entity.TeachingConfirmation;
import fpt.project.bsmart.entity.common.EConfirmStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface TeachingConfirmationRepository extends JpaRepository<TeachingConfirmation, Long> {
    TeachingConfirmation findByCode(String code);

    List<TeachingConfirmation> findByStatus(EConfirmStatus status);
}
