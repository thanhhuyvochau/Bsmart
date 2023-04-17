package fpt.project.bsmart.util;

import fpt.project.bsmart.entity.Class;
import fpt.project.bsmart.entity.TimeTable;

import java.time.Instant;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class ClassUtil {
    public static Integer getPercentageOfClassTime(Class clazz) {
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
            Integer numberOfSlot = clazz.getSubCourse().getNumberOfSlot();
            return currentSlotNums / numberOfSlot;
        }
        return 0;
    }
}
