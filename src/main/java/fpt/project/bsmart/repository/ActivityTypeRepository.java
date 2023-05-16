package fpt.project.bsmart.repository;

import fpt.project.bsmart.entity.ActivityType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ActivityTypeRepository extends JpaRepository<ActivityType, Long> {
    ActivityType findByCode(String code) ;
}
