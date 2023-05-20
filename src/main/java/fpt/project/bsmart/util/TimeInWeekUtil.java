package fpt.project.bsmart.util;

import fpt.project.bsmart.entity.Class;
import fpt.project.bsmart.entity.TimeInWeek;
import fpt.project.bsmart.entity.TimeTable;
import fpt.project.bsmart.entity.common.ApiException;
import fpt.project.bsmart.entity.constant.EDayOfWeekCode;
import org.springframework.http.HttpStatus;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class TimeInWeekUtil {

    public static List<TimeTable> generateTimeTable(List<TimeInWeek> timeInWeeks, Integer numberOfSlot, Instant startDate, Class clazz) {
        List<EDayOfWeekCode> availableDOW = timeInWeeks.stream().map(timeInWeek -> timeInWeek.getDayOfWeek().getCode()).distinct().collect(Collectors.toList());
        List<TimeTable> timeTables = new ArrayList<>();

        Instant date = startDate;
        int i = numberOfSlot;
        while (i > 0) {
            EDayOfWeekCode dayOfWeekCode = TimeUtil.getDayOfWeek(date);
            if (dayOfWeekCode == null) {
                throw ApiException.create(HttpStatus.NOT_FOUND).withMessage("Không thể nhận diện thứ trong tuần, lỗi hệ thống vui lòng liên hệ Admin!");
            }
            if (availableDOW.contains(dayOfWeekCode)) {
                List<TimeInWeek> dateOfWeeks = timeInWeeks.stream().filter(timeInWeek -> Objects.equals(timeInWeek.getDayOfWeek().getCode(), dayOfWeekCode)).collect(Collectors.toList());
                for (TimeInWeek dow : dateOfWeeks) {
                    if (i <= 0) break;
                    TimeTable timeTable = new TimeTable();
                    timeTable.setDate(date);
                    timeTable.setCurrentSlotNums((numberOfSlot - i) + 1);
                    timeTable.setSlot(dow.getSlot());
                    timeTable.setClazz(clazz);
                    timeTables.add(timeTable);
                    i--;
                }
            }
            date = date.plus(1, ChronoUnit.DAYS);
        }
        return timeTables;
    }


}
