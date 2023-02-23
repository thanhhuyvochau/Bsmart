package fpt.project.bsmart.service;

import fpt.project.bsmart.entity.request.RoleRequest;
import fpt.project.bsmart.entity.response.RoleResponse;

import java.util.List;

public interface IRoleService {

    List<RoleResponse> getAll();

    RoleResponse create(RoleRequest roleRequest);

    RoleResponse update(RoleRequest roleRequest, Long id);

    Boolean delete(Long id);


}
