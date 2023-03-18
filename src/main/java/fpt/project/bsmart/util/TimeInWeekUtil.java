package fpt.project.bsmart.util;

import fpt.project.bsmart.entity.TimeInWeek;
import fpt.project.bsmart.entity.TimeTable;
import fpt.project.bsmart.entity.constant.EDayOfWeekCode;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class TimeInWeekUtil {

    public static List<TimeTable> generateTimeTable(List<TimeInWeek> timeInWeeks, Integer numberOfSlot, Instant startDate) {
        Map<EDayOfWeekCode, TimeInWeek> timeInWeekMap = timeInWeeks.stream().collect(Collectors.toMap(timeInWeek -> timeInWeek.getDayOfWeek().getCode(), Function.identity()));
        List<TimeTable> timeTables = new ArrayList<>();

        Instant date = startDate;
        for (int i = numberOfSlot; i > 0; i--) {
            EDayOfWeekCode dayOfWeek = DayUtil.getDayOfWeek(date);
            TimeInWeek timeInWeek = timeInWeekMap.get(dayOfWeek);
            if (timeInWeek != null) {
                TimeTable timeTable = new TimeTable();
                timeTable.setDate(date);
                timeTable.setCurrentSlotNums(numberOfSlot - i);
                timeTable.setSlot(timeInWeek.getSlot());
                timeTable.setDayOfWeek(timeInWeek.getDayOfWeek());
                timeTables.add(timeTable);
            }
            date = date.plus(1, ChronoUnit.DAYS);
        }
        return timeTables;
    }
}
