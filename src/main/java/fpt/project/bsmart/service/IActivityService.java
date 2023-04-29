package fpt.project.bsmart.service;

import fpt.project.bsmart.entity.request.AddActivityRequest;

import java.io.IOException;

public interface IActivityService {
    Boolean addActivity(AddActivityRequest addActivityRequest) throws IOException;
}
