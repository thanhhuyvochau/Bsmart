package fpt.project.bsmart.repository;

import fpt.project.bsmart.entity.Activity;
import fpt.project.bsmart.entity.ActivityType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ActivityRepository extends JpaRepository<Activity, Long> {
}
