package fpt.project.bsmart.repository;

import fpt.project.bsmart.entity.ActivityHistory;
import fpt.project.bsmart.entity.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ActivityHistoryRepository extends JpaRepository<ActivityHistory, Long> {

}