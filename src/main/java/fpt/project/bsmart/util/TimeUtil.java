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

    public static boolean isLessThanHourDurationOfNow(Instant instant, int duration) {
        Instant currentInstant = Instant.now();
        Duration difference = Duration.between(instant.truncatedTo(ChronoUnit.HOURS), currentInstant.truncatedTo(ChronoUnit.HOURS));
        long differenceInHour = difference.toHours();
        return differenceInHour < duration && differenceInHour >= 0;
    }

    public static boolean isLessThanDayDurationOfNow(Instant instant, int duration) {
        Instant currentInstant = Instant.now();
        Duration difference = Duration.between(instant.truncatedTo(ChronoUnit.DAYS), currentInstant.truncatedTo(ChronoUnit.DAYS));
        long differenceInDay = difference.toDays();
        return differenceInDay <= duration && differenceInDay >= 0;
    }
}
