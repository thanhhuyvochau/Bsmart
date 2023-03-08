package fpt.project.bsmart.util;


import fpt.project.bsmart.entity.Class;
import fpt.project.bsmart.entity.*;
import fpt.project.bsmart.entity.dto.*;
import org.springframework.stereotype.Component;

@Component
public class ConvertUtil {


    public static CategoryDto convertCategoryToCategoryDto(Category category) {
        return ObjectUtil.copyProperties(category, new CategoryDto(), CategoryDto.class);
    }

    public static SubjectDto convertSubjectToSubjectDto(Subject subject) {
        SubjectDto subjectDto = ObjectUtil.copyProperties(subject, new SubjectDto(), SubjectDto.class);
        if (subject.getCategory() != null) {
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


    public static DayOfWeekDTO convertDayOfWeekToDto(DayOfWeek dayOfWeek) {
        return new DayOfWeekDTO(dayOfWeek.getId(), dayOfWeek.getName(), dayOfWeek.getCode());
    }

    public static SimpleClassDto convertClassToSimpleDto(Class clazz) {
        return ObjectUtil.copyProperties(clazz, new SimpleClassDto(), SimpleClassDto.class, true);
    }

    public static TimeInWeekDTO convertTimeInWeekToDto(TimeInWeek timeInWeek) {
        Slot slot = timeInWeek.getSlot();
        Class clazz = timeInWeek.getClazz();
        DayOfWeek dayOfWeek = timeInWeek.getDayOfWeek();

        DayOfWeekDTO dayOfWeekDTO = convertDayOfWeekToDto(dayOfWeek);
        SlotDto slotDto = convertSlotToSlotDto(slot);
        SimpleClassDto simpleClazz = convertClassToSimpleDto(clazz);

        return new TimeInWeekDTO(dayOfWeekDTO, simpleClazz, slotDto);
    }

}