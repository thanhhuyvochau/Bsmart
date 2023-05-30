package fpt.project.bsmart.util;


import fpt.project.bsmart.entity.ActivityHistory;
import fpt.project.bsmart.entity.constant.EActivityType;
import fpt.project.bsmart.repository.ActivityHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class ActivityHistoryUtil {


    private static ActivityHistoryRepository staticActivityHistoryRepository;

    public ActivityHistoryUtil(ActivityHistoryRepository activityHistoryRepository) {
        staticActivityHistoryRepository = activityHistoryRepository ;
    }

    public static void logHistoryForCourseApprove(Long subCourseId , String message ) {
        ActivityHistory activityHistory = new ActivityHistory() ;
        activityHistory.setType(EActivityType.SUBCOURSE);
        activityHistory.setActivityTime(LocalDateTime.now());
        activityHistory.setActivityId(subCourseId);
        activityHistory.setDetail(message);
        staticActivityHistoryRepository.save(activityHistory) ;
    }
}