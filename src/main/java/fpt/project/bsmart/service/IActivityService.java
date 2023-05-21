package fpt.project.bsmart.service;

import fpt.project.bsmart.entity.dto.ActivityDto;
import fpt.project.bsmart.entity.request.ActivityRequest;

import java.io.IOException;

public interface IActivityService {
    Boolean addActivity(ActivityRequest activityRequest) throws IOException;

    Boolean deleteActivity(Long id);

    Boolean changeActivityVisible(Long id);

    Boolean editActivity(Long id, ActivityRequest activityRequest) throws IOException;

    ActivityDto getDetailActivity(Long id);

}
