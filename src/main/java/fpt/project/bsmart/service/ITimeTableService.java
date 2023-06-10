package fpt.project.bsmart.service;

import fpt.project.bsmart.entity.common.ApiPage;
import fpt.project.bsmart.entity.request.EditClassTimeTableRequest;
import fpt.project.bsmart.entity.response.TimeTableResponse;
import org.springframework.data.domain.Pageable;

public interface ITimeTableService {
    Boolean editTimeTable(EditClassTimeTableRequest request);

    ApiPage<TimeTableResponse> getTimeTableByClass(Long id, Pageable pageable);

}
