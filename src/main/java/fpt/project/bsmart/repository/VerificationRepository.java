package fpt.project.bsmart.repository;

import fpt.project.bsmart.entity.DayOfWeek;
import fpt.project.bsmart.entity.Verification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VerificationRepository extends JpaRepository<Verification, Long> {
}
