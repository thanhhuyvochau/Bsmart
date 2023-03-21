package fpt.project.bsmart.service;

import fpt.project.bsmart.entity.dto.CategoryDto;
import fpt.project.bsmart.entity.request.EditClassTimeTableRequest;
import fpt.project.bsmart.entity.request.category.CategoryRequest;

import java.util.List;

public interface ITimeTableService {
    Boolean editTimeTable(EditClassTimeTableRequest request );
}
