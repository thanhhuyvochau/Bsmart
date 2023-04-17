package fpt.project.bsmart.service;

import fpt.project.bsmart.entity.request.category.CreateClassRequest;
import fpt.project.bsmart.entity.dto.ClassProgressTimeDto;

public interface IClassService {

    Boolean createClass(CreateClassRequest request);
    ClassProgressTimeDto getClassProgression(Long clazzId);
}
