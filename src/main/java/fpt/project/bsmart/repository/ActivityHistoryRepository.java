package fpt.project.bsmart.repository;

import fpt.project.bsmart.entity.ActivityHistory;
import fpt.project.bsmart.entity.Course;
import fpt.project.bsmart.entity.constant.EActivityType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ActivityHistoryRepository extends JpaRepository<ActivityHistory, Long> {
    List<ActivityHistory> findByUserId(Long userId);

    Page<ActivityHistory> findByUserId(Long userId, Pageable pageable);

    ActivityHistory findByUserIdAndType(Long userId, EActivityType type);

    ActivityHistory findByType( EActivityType type);
}