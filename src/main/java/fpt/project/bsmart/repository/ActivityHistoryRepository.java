package fpt.project.bsmart.repository;

import fpt.project.bsmart.entity.ActivityHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ActivityHistoryRepository extends JpaRepository<ActivityHistory, Long> {
    List<ActivityHistory> findByUserId(Long userId);

    Page<ActivityHistory> findByUserId(Long userId, Pageable pageable);



}