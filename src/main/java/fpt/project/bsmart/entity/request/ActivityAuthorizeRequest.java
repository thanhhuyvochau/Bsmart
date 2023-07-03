package fpt.project.bsmart.entity.request;

import fpt.project.bsmart.entity.BaseEntity;

import java.util.List;


public class ActivityAuthorizeRequest extends BaseEntity {

    private List<Long> authorizeClasses;

    public List<Long> getAuthorizeClasses() {
        return authorizeClasses;
    }

    public void setAuthorizeClasses(List<Long> authorizeClasses) {
        this.authorizeClasses = authorizeClasses;
    }
}