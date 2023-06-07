package fpt.project.bsmart.entity.response;

import fpt.project.bsmart.entity.dto.ActivityDto;
import fpt.project.bsmart.entity.dto.ClassSectionDto;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class ClassResponse {
    private Long id;
    private Instant startDate;
    private Instant endDate;
    private Integer numberOfStudent = 0;
    private String subCourseName;
    private String mentorName;
    private List<ClassSectionDto> classSectionList = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getStartDate() {
        return startDate;
    }

    public void setStartDate(Instant startDate) {
        this.startDate = startDate;
    }

    public Instant getEndDate() {
        return endDate;
    }

    public void setEndDate(Instant endDate) {
        this.endDate = endDate;
    }

    public Integer getNumberOfStudent() {
        return numberOfStudent;
    }

    public void setNumberOfStudent(Integer numberOfStudent) {
        this.numberOfStudent = numberOfStudent;
    }

    public String getSubCourseName() {
        return subCourseName;
    }

    public void setSubCourseName(String subCourseName) {
        this.subCourseName = subCourseName;
    }

    public String getMentorName() {
        return mentorName;
    }

    public void setMentorName(String mentorName) {
        this.mentorName = mentorName;
    }

    public List<ClassSectionDto> getClassSectionList() {
        return classSectionList;
    }

    public void setClassSectionList(List<ClassSectionDto> classSectionList) {
        this.classSectionList = classSectionList;
    }
}
