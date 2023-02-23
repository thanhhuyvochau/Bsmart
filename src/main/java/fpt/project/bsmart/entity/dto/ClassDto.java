package fpt.project.bsmart.entity.dto;


import fpt.project.bsmart.entity.common.EClassLevel;
import fpt.project.bsmart.entity.common.EClassStatus;
import fpt.project.bsmart.entity.common.EClassType;
import fpt.project.bsmart.entity.response.AccountSimpleResponse;
import fpt.project.bsmart.entity.response.CourseResponse;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

public class ClassDto {

    private Long id;
    private String name;
    private String code;

    private EClassStatus status;

    private EClassType classType;

    private EClassLevel classLevel;


    private Instant startDate;

    private Instant endDate;


    private Long numberStudent;

    private Long maxNumberStudent;
    private Long minNumberStudent;

    private CourseResponse course;

    private BigDecimal unitPrice;

    private BigDecimal teacherReceivedPrice;

    private List<SectionDto> resources;
    private AccountSimpleResponse teacher;
    private ArchetypeDto archetype;
    private boolean isEnrolled = false;
    private boolean isApplied = false;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AccountSimpleResponse getTeacher() {
        return teacher;
    }

    public void setTeacher(AccountSimpleResponse teacher) {
        this.teacher = teacher;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;

    }

    public BigDecimal getTeacherReceivedPrice() {
        return teacherReceivedPrice;
    }

    public void setTeacherReceivedPrice(BigDecimal teacherReceivedPrice) {
        this.teacherReceivedPrice = teacherReceivedPrice;

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


    public EClassType getClassType() {
        return classType;
    }

    public void setClassType(EClassType classType) {
        this.classType = classType;
    }

    public EClassLevel getClassLevel() {
        return classLevel;
    }

    public void setClassLevel(EClassLevel classLevel) {
        this.classLevel = classLevel;
    }

    public Long getNumberStudent() {
        return numberStudent;
    }

    public void setNumberStudent(Long numberStudent) {
        this.numberStudent = numberStudent;
    }

    public EClassStatus getStatus() {
        return status;
    }

    public void setStatus(EClassStatus status) {
        this.status = status;
    }

    public Long getMaxNumberStudent() {
        return maxNumberStudent;
    }

    public void setMaxNumberStudent(Long maxNumberStudent) {
        this.maxNumberStudent = maxNumberStudent;
    }

    public CourseResponse getCourse() {
        return course;
    }

    public void setCourse(CourseResponse course) {
        this.course = course;
    }

    public Long getMinNumberStudent() {
        return minNumberStudent;
    }

    public void setMinNumberStudent(Long minNumberStudent) {
        this.minNumberStudent = minNumberStudent;
    }

    public List<SectionDto> getResources() {
        return resources;
    }

    public void setResources(List<SectionDto> resources) {
        this.resources = resources;
    }

    public ArchetypeDto getArchetype() {
        return archetype;
    }

    public void setArchetype(ArchetypeDto archetype) {
        this.archetype = archetype;
    }

    public boolean getEnrolled() {
        return isEnrolled;
    }

    public void setEnrolled(boolean enrolled) {
        isEnrolled = enrolled;
    }

    public boolean isApplied() {
        return isApplied;
    }

    public void setApplied(boolean applied) {
        isApplied = applied;
    }
}
