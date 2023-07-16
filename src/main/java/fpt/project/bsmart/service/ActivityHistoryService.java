package fpt.project.bsmart.service;

import fpt.project.bsmart.entity.common.ApiPage;
import fpt.project.bsmart.entity.response.ActivityHistoryResponse;
import org.springframework.data.domain.Pageable;

public interface ActivityHistoryService {
    ApiPage<ActivityHistoryResponse> getHistory(Pageable pageable);

}
