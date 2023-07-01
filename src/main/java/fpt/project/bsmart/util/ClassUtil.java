package fpt.project.bsmart.util;

import fpt.project.bsmart.entity.Class;
import fpt.project.bsmart.entity.TimeTable;
import fpt.project.bsmart.entity.dto.ClassProgressTimeDto;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class ClassUtil {
    public static double CLASS_PERCENTAGE_FOR_FIRST_FEEDBACK = 0.5f;
    public static double CLASS_PERCENTAGE_FOR_SECOND_FEEDBACK = 0.8f;
    public static double PERCENTAGE_RANGE = 0.1f;
    public static ClassProgressTimeDto getPercentageOfClassTime(Class clazz) {
        List<TimeTable> timeTables = clazz.getTimeTables();
        Instant now = Instant.now();
        Optional<TimeTable> nearestTimeTable = timeTables.stream().filter(timeTable -> timeTable.getDate().compareTo(now) <= 0).max(new Comparator<TimeTable>() {
            @Override
            public int compare(TimeTable o1, TimeTable o2) {
                return o1.getDate().compareTo(o2.getDate());
            }
        });
        if (nearestTimeTable.isPresent()) {
            TimeTable presentTimeTable = nearestTimeTable.get();
            Integer currentSlotNums = presentTimeTable.getCurrentSlotNums();
//            Integer numberOfSlot = clazz.getSubCourse().getNumberOfSlot();
//            double percentage = (double) currentSlotNums / (double) numberOfSlot;
//            return new ClassProgressTimeDto(currentSlotNums, BigDecimal.valueOf(percentage).setScale(2, RoundingMode.UP).doubleValue());
        }
        return null;
    }
}
