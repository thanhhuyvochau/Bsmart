package fpt.project.bsmart.repository;

import fpt.project.bsmart.entity.ActivityType;
import fpt.project.bsmart.entity.Bank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface ActivityTypeRepository extends JpaRepository<ActivityType, Long> {
    ActivityType findByCode(String code) ;
}
