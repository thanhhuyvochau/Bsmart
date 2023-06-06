package fpt.project.bsmart.service.Impl;

import fpt.project.bsmart.entity.*;
import fpt.project.bsmart.entity.common.ApiPage;
import fpt.project.bsmart.entity.response.ActivityHistoryResponse;
import fpt.project.bsmart.repository.*;
import fpt.project.bsmart.service.ActivityHistoryService;
import fpt.project.bsmart.util.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ActivityHistoryServiceImpl implements ActivityHistoryService {
private final ActivityHistoryRepository activityHistoryRepository ;

    public ActivityHistoryServiceImpl(ActivityHistoryRepository activityHistoryRepository) {
        this.activityHistoryRepository = activityHistoryRepository;
    }

    @Override
    public ApiPage<ActivityHistoryResponse> getHistory(Pageable pageable) {
        User currentUserAccountLogin = SecurityUtil.getCurrentUser();
        Page<ActivityHistory> activityHistories = activityHistoryRepository.findByUserId(currentUserAccountLogin.getId(), pageable);
        return PageUtil.convert(activityHistories.map(ConvertUtil::convertActivityHistoryToActivityHistoryResponse));

    }
}
