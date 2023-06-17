package fpt.project.bsmart.entity.dto.course;

import com.fasterxml.jackson.annotation.JsonFilter;
import fpt.project.bsmart.entity.Section;
import fpt.project.bsmart.entity.dto.section.SectionDto;

import java.util.List;

import static fpt.project.bsmart.util.Constants.CustomFilterConstants.CONTENT_FILTER;

@JsonFilter(CONTENT_FILTER)
public class CourseContentDto {

    private SectionDto sections;

    public SectionDto getSections() {
        return sections;
    }

    public void setSections(SectionDto sections) {
        this.sections = sections;
    }
}
