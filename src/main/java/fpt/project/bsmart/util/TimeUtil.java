package fpt.project.bsmart.util;

import fpt.project.bsmart.entity.constant.EDayOfWeekCode;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class TimeUtil {

    public static Boolean checkDay(String one, String two) throws ParseException {

        Instant datOne = Instant.parse(one);
        String oneSubString = datOne.toString().substring(0, 10).replaceAll("-", " ");


        Instant dayTwo = Instant.parse(two);
        String twoSubString = dayTwo.toString().substring(0, 10).replaceAll("-", " ");


        SimpleDateFormat myFormat = new SimpleDateFormat("yyyy MM dd");

        Date dateOne = myFormat.parse(oneSubString);
        Date dateTwo = myFormat.parse(twoSubString);
        long check = dateOne.getTime() - dateTwo.getTime();
        if (check > 0) {
            return false;
        }
        return true;

    }

    public static Instant convertDayInstant(String day) {
        String s = Instant.parse(day)
                .truncatedTo(ChronoUnit.DAYS)
                .toString();
        return Instant.parse(s);
    }

    public static Boolean checkTwoDateBigger(String one, String two, Integer plusDay) throws ParseException {

        Instant datOne = Instant.parse(one).plus(plusDay, ChronoUnit.DAYS);
        String oneSubString = datOne.toString().substring(0, 10).replaceAll("-", " ");


        Instant dayTwo = Instant.parse(two);
        String twoSubString = dayTwo.toString().substring(0, 10).replaceAll("-", " ");

        SimpleDateFormat myFormat = new SimpleDateFormat("yyyy MM dd");

        Date dateOne = myFormat.parse(oneSubString);
        Date dateTwo = myFormat.parse(twoSubString);
        long check = dateOne.getTime() - dateTwo.getTime();
        if (check > 0) {
            return false;
        }
        return true;

    }

    public static LocalDate getDatesBetweenUsingJava8(String startDate, java.time.DayOfWeek dow) throws ParseException {
        Instant start = Instant.parse(startDate);
        String oneSubString = start.toString().substring(0, 10);
        LocalDate startLocalDate = LocalDate.parse(oneSubString);
        LocalDate endDate = startLocalDate.plusDays(8);

        // lấy tất cả ngày / thứ trong 1 tuần : tính từ ngày bắt dầu

        long numOfDaysBetween = ChronoUnit.DAYS.between(startLocalDate, endDate);
        List<LocalDate> collectDay = IntStream.iterate(0, i -> i + 1)
                .limit(numOfDaysBetween)
                .mapToObj(startLocalDate::plusDays)
                .collect(Collectors.toList());
        for (LocalDate ld : collectDay) {
            java.time.DayOfWeek dayf = ld.getDayOfWeek();
            System.out.println(dayf);

            if (dow.equals(dayf)) {
                System.out.println(dayf.getValue());
                return ld;

            }
        }
        return null;

    }

    public static boolean isValidBirthday(Instant birthday) {
        LocalDate localDate = birthday.atOffset(ZoneOffset.UTC).toLocalDate();
        return localDate.isBefore(LocalDate.now());
    }

    public static EDayOfWeekCode getDayOfWeek(Instant instant) {
        int dayOfWeekKey = instant.atZone(ZoneOffset.UTC).getDayOfWeek().getValue();
        switch (dayOfWeekKey) {
            case 1:
                return EDayOfWeekCode.MONDAY;
            case 2:
                return EDayOfWeekCode.TUESDAY;

            case 3:
                return EDayOfWeekCode.WEDNESDAY;

            case 4:
                return EDayOfWeekCode.THURSDAY;

            case 5:
                return EDayOfWeekCode.FRIDAY;

            case 6:
                return EDayOfWeekCode.SATURDAY;

            case 7:
                return EDayOfWeekCode.SUNDAY;
        }
        return null;
    }

    public static boolean isLessThanConfigHour(Instant instant, int hour) {
        Instant currentInstant = Instant.now();
        Duration difference = Duration.between(instant.truncatedTo(ChronoUnit.HOURS), currentInstant.truncatedTo(ChronoUnit.HOURS));
        long differenceInHour = difference.toHours();
        return differenceInHour < hour;
    }
}
