package fpt.project.bsmart.util;


import fpt.project.bsmart.entity.Subject;
import fpt.project.bsmart.entity.dto.SubjectDto;
import org.springframework.stereotype.Component;

@Component
public class ConvertUtil {



    public static SubjectDto convertSubjectToSubjectDto(Subject subject ){
        SubjectDto subjectDto = ObjectUtil.copyProperties(subject, new SubjectDto(), SubjectDto.class);
        if (subject.getCategory()!=null){
            subjectDto.setCategoryId(subject.getCategory().getId());
        }
        return subjectDto;
    }






}