package fpt.project.bsmart.util;


import fpt.project.bsmart.entity.Category;
import fpt.project.bsmart.entity.Subject;
import fpt.project.bsmart.entity.dto.CategoryDto;
import fpt.project.bsmart.entity.dto.SubjectDto;
import org.springframework.stereotype.Component;

@Component
public class ConvertUtil {

    public static CategoryDto convertCategoryToCategoryDto(Category category){
        CategoryDto categoryDto = ObjectUtil.copyProperties(category, new CategoryDto(), CategoryDto.class);
        return categoryDto;
    }

    public static SubjectDto convertSubjectToSubjectDto(Subject subject ){
        SubjectDto subjectDto = ObjectUtil.copyProperties(subject, new SubjectDto(), SubjectDto.class);
        if (subject.getCategory()!=null){
            subjectDto.setCategoryId(subject.getCategory().getId());
        }
        return subjectDto;
    }






}