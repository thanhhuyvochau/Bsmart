package fpt.project.bsmart.entity.builder;

import fpt.project.bsmart.entity.Activity;
import fpt.project.bsmart.entity.ActivityAuthorize;
import fpt.project.bsmart.entity.Course;
import fpt.project.bsmart.entity.common.ApiException;
import fpt.project.bsmart.entity.constant.ECourseActivityType;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ActivityBuilder {
    private String name;
    private ECourseActivityType type;
    private Boolean visible;
    private Activity parent;
    private List<ActivityAuthorize> activityAuthorizes = new ArrayList<>();
    private Course course;

    public ActivityBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public ActivityBuilder withType(ECourseActivityType type) {
        this.type = type;
        return this;
    }

    public ActivityBuilder withVisible(Boolean visible) {
        this.visible = visible;
        return this;
    }

    public ActivityBuilder withParent(Activity parent) {
        this.parent = parent;
        return this;
    }

    public ActivityBuilder withActivityAuthorizes(List<ActivityAuthorize> activityAuthorizes) {
        this.activityAuthorizes = activityAuthorizes;
        return this;
    }

    public ActivityBuilder withCourse(Course course) {
        this.course = course;
        return this;
    }

    public Activity build() {
        // TODO: Validation
        if (type == null) {
            throw ApiException.create(HttpStatus.CONFLICT).withMessage("Loại hoạt động không thể trống");
        } else if (name == null) {
            throw ApiException.create(HttpStatus.CONFLICT).withMessage("Tên hoạt động không thể trống");
        } else if (course == null) {
            throw ApiException.create(HttpStatus.CONFLICT).withMessage("Khóa học của hoạt động không thể trống");
        } else if (parent == null && !Objects.equals(type, ECourseActivityType.SECTION)) {
            throw ApiException.create(HttpStatus.CONFLICT).withMessage("Khóa học không thể tạo độc lập");
        } else if (parent != null && Objects.equals(type, ECourseActivityType.SECTION)) {
            throw ApiException.create(HttpStatus.CONFLICT).withMessage("Section không thể tạo dưới một section khác");
        }
        return new Activity(name, type, visible, parent, activityAuthorizes, course);
    }


    public static ActivityBuilder getBuilder() {
        return new ActivityBuilder();
    }
}
