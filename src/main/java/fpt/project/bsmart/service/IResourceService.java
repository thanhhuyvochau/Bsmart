package fpt.project.bsmart.service;

import fpt.project.bsmart.entity.dto.ResourceDto;
import fpt.project.bsmart.entity.request.UploadAvatarRequest;

public interface IResourceService {


    ResourceDto uploadFile(UploadAvatarRequest uploadFile);
}
