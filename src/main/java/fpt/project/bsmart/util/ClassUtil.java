package fpt.project.bsmart.util;

import fpt.project.bsmart.entity.*;
import fpt.project.bsmart.entity.Class;
import fpt.project.bsmart.entity.dto.ClassProgressTimeDto;
import fpt.project.bsmart.entity.dto.StudentClassDto;
import fpt.project.bsmart.repository.OrderDetailRepository;
import fpt.project.bsmart.repository.OrderRepository;
import fpt.project.bsmart.repository.SubCourseRepository;
import fpt.project.bsmart.repository.UserRepository;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
//@Component
public class ClassUtil {


    public static ClassProgressTimeDto getPercentageOfClassTime(Class clazz) {
        List<TimeTable> timeTables = clazz.getTimeTables();
        Instant now = Instant.now().plus(10, ChronoUnit.DAYS);
        Optional<TimeTable> nearestTimeTable = timeTables.stream().filter(timeTable -> timeTable.getDate().compareTo(now) <= 0).max(new Comparator<TimeTable>() {
            @Override
            public int compare(TimeTable o1, TimeTable o2) {
                return o1.getDate().compareTo(o2.getDate());
            }
        });
        if (nearestTimeTable.isPresent()) {
            TimeTable presentTimeTable = nearestTimeTable.get();
            Integer currentSlotNums = presentTimeTable.getCurrentSlotNums();
            Integer numberOfSlot = clazz.getSubCourse().getNumberOfSlot();
            double percentage = (double) currentSlotNums / (double) numberOfSlot;
            return new ClassProgressTimeDto(currentSlotNums, BigDecimal.valueOf(percentage).setScale(2, RoundingMode.UP).doubleValue());
        }
        return null;
    }



}
