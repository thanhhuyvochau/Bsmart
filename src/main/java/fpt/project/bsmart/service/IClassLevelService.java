package fpt.project.bsmart.service;

import fpt.project.bsmart.entity.request.ClassLevelRequest;
import fpt.project.bsmart.entity.response.ClassLevelResponse;

import java.util.List;

public interface IClassLevelService {
    ClassLevelResponse create(ClassLevelRequest classLevelRequest);

    ClassLevelResponse update(ClassLevelRequest classLevelRequest, Long id);

    Boolean delete(Long id);

    List<ClassLevelResponse> getAll();
}
