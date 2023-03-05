package fpt.project.bsmart.util;


import fpt.project.bsmart.entity.Category;
import fpt.project.bsmart.entity.Subject;
import fpt.project.bsmart.entity.dto.CategoryDto;
import fpt.project.bsmart.entity.Slot;
import fpt.project.bsmart.entity.Subject;
import fpt.project.bsmart.entity.dto.SlotDto;
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


    public static SlotDto convertSlotToSlotDto(Slot slot) {
        SlotDto dto = new SlotDto();
        dto.setId(slot.getId());
        dto.setName(slot.getName());
        dto.setCode(slot.getCode());
        dto.setStartTime(slot.getStartTime());
        dto.setEndTime(slot.getEndTime());
        return dto;
    }

    public static Slot convertSlotDtoToSlot(SlotDto dto) {
        Slot entity = new Slot();
        entity.setId(dto.getId());
        entity.setName(dto.getName());
        entity.setCode(dto.getCode());
        entity.setStartTime(dto.getStartTime());
        entity.setEndTime(dto.getEndTime());
        return entity;
    }






}