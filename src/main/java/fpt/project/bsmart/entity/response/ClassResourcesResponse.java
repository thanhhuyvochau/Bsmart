package fpt.project.bsmart.entity.response;


import fpt.project.bsmart.moodle.response.MoodleRecourseDtoResponse;

import java.util.List;

public class ClassResourcesResponse {



    private List<MoodleRecourseDtoResponse> resources;

    public List<MoodleRecourseDtoResponse> getResources() {
        return resources;
    }

    public void setResources(List<MoodleRecourseDtoResponse> resources) {
        this.resources = resources;
    }
}
