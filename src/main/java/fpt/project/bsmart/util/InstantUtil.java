package fpt.project.bsmart.util;

import java.time.*;
import java.util.ArrayList;
import java.util.List;

public class InstantUtil {
    public static YearMonth getYearMonthFromInstant(Instant instant) {
        LocalDateTime localDateTime = instant.atZone(ZoneId.systemDefault()).toLocalDateTime();
        return YearMonth.of(localDateTime.getYear(), localDateTime.getMonth());
    }

    public static List<YearMonth> getAllMonthInYear(){
        List<YearMonth> yearMonths = new ArrayList<>();
        YearMonth currentMonth = YearMonth.now();
        for (int month = 1; month <= 12; month++){
            yearMonths.add(currentMonth.withMonth(month));
        }
        return yearMonths;
    }

    public static Instant getFirstDayOfYear(Integer year){
        LocalDate firstDay= LocalDate.of(year, 1, 1);
        return firstDay.atStartOfDay(ZoneId.systemDefault()).toInstant();
    }

    public static Instant getLastDayOfYear(Integer year){
        LocalDate lastDay = LocalDate.of(year, 12, 31);
        return lastDay.atTime(23, 59, 59)
                .atZone(ZoneId.systemDefault())
                .toInstant();
    }
}
