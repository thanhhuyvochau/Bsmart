package fpt.project.bsmart.entity.dto;

import java.util.List;


public class FeedBackDto {

    private ClassDto classInfo ;
    private List<FeedBacClassDto> feedBacClass ;

    public ClassDto getClassInfo() {
        return classInfo;
    }

    public void setClassInfo(ClassDto classInfo) {
        this.classInfo = classInfo;
    }

    public List<FeedBacClassDto> getFeedBacClass() {
        return feedBacClass;
    }

    public void setFeedBacClass(List<FeedBacClassDto> feedBacClass) {
        this.feedBacClass = feedBacClass;
    }
}
