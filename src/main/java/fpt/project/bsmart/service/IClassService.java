package fpt.project.bsmart.service;

import fpt.project.bsmart.entity.request.category.CreateClassRequest;

public interface IClassService {

    Boolean createClass(CreateClassRequest request);
}
