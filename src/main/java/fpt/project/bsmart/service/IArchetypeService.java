package fpt.project.bsmart.service;

import fpt.project.bsmart.entity.dto.ArchetypeTeacherDto;

import java.util.List;

public interface IArchetypeService {


    List<ArchetypeTeacherDto> getArchetypeOfTeacher(long teacherId);
}
