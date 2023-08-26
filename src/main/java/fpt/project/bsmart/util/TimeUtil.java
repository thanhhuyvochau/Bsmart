package fpt.project.bsmart.util;

import fpt.project.bsmart.entity.constant.EDayOfWeekCode;
import fpt.project.bsmart.entity.constant.EUserRole;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;

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

    public static boolean isValidBirthday(Instant birthday, EUserRole role) {
        LocalDate targetDate = birthday.atZone(ZoneOffset.UTC).toLocalDate();
        LocalDate currentDate = Instant.now().atZone(ZoneOffset.UTC).toLocalDate();
        Period period = Period.between(targetDate, currentDate);
        if (role.equals(EUserRole.TEACHER)) {
            return period.getYears() >= 18;
        }
        return period.getYears() >= 16;
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

    public static boolean isLessThanHourDurationOfNow(Instant instant, int duration) {
        Instant currentInstant = Instant.now();
        Duration difference = Duration.between(instant.truncatedTo(ChronoUnit.HOURS), currentInstant.truncatedTo(ChronoUnit.HOURS));
        long differenceInHour = difference.toHours();
        return Math.abs(differenceInHour) < duration;
    }

    public static boolean isLessThanDayDurationOfNow(Instant instant, int duration) {
        Instant currentInstant = Instant.now();
        Duration difference = Duration.between(instant.truncatedTo(ChronoUnit.DAYS), currentInstant.truncatedTo(ChronoUnit.DAYS));
        long differenceInDay = difference.toDays();
        return Math.abs(differenceInDay) <= duration;
    }

    public static long compareTwoInstantTruncated(Instant start, Instant end, ChronoUnit unit) {
        Duration difference = Duration.between(start.truncatedTo(unit), end.truncatedTo(unit));
        return difference.toMinutes();
    }

    public static Instant checkDateToCreateClass(Instant inputDate) throws Exception {

        Instant currentDate = Instant.now();
        Duration duration = Duration.between(currentDate, inputDate);
        if (duration.toDays() < 15) {
            throw new Exception("Ngày bắt đầu lớp phải cách ngày hiện tại ít nhất 15 ngày !");
        }
        return inputDate;
    }

    public static Instant checkDateToStartAndEndClass(Instant startDate, Instant endDate) throws Exception {
        Duration duration = Duration.between(startDate, endDate);
        if (duration.toDays() < 15) {
            throw new Exception("Ngày kết thúc lớp phải sau ngày bắt đầu ít nhất 15 ngày");
        }
        return endDate;
    }

    public static String formatInstant(Instant instant) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")
                .withZone(ZoneId.systemDefault()); // Set the desired time zone

        // Format the Instant object
        return formatter.format(instant);
    }
}
