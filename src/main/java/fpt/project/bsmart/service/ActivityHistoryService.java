package fpt.project.bsmart.service;

import fpt.project.bsmart.entity.common.ApiPage;
import fpt.project.bsmart.entity.dto.ActivityTypeDto;
import fpt.project.bsmart.entity.response.ActivityHistoryResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ActivityHistoryService {

    ApiPage<ActivityHistoryResponse> getHistory(Pageable pageable);
}
