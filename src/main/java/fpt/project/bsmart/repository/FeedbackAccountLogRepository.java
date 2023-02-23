package fpt.project.bsmart.repository;

import fpt.project.bsmart.entity.FeedbackAccountLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface FeedbackAccountLogRepository extends JpaRepository<FeedbackAccountLog, Long> {

    List<FeedbackAccountLog> findFeedbackAccountLogByAccountDetailId(Long accountDetail);


}
