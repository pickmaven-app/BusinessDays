package app.pickmaven.businessdays;

import java.time.Duration;
import java.time.LocalDate;
import java.time.MonthDay;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Utility class for computing holidays and convert dates.
 * <p>
 */
public class HolidayUtils {

    // COMPUTING HOLIDAY METHODS

    public static LocalDate getEaster(final int year) {
        final int dayOfMarch = getEasterDelay(year);
        return LocalDate.of(year, 3, 1).plusDays(dayOfMarch - 1);
    }

    public static LocalDate getEasterMonday(final int year) {
        final int dayOfMarch = getEasterDelay(year);
        return LocalDate.of(year, 3, 1).plusDays(dayOfMarch);
    }

    private static int getEasterDelay(final int year) {
        if(year < 0){
            throw new IllegalArgumentException("getEaster: year must be non-negative, but was called with year = " + year);
        }

        final int a = year % 19;
        final int b = year >> 2;
        final int c = b / 25 + 1;
        final int d = (c * 3) >> 2;
        int e = ((a * 19) - ((c * 8 + 5) / 25) + d + 15) % 30;
        e += (29578 - a - e * 32) >> 10;
        e -= ((year % 7) + b - d + e + 2) % 7;
        return e;
    }

    // HOLIDAY CONVERSION METHODS

    public static List<Holiday> toHolidayList(List<LocalDate> holidayList) {
        return holidayList.stream()
                .map(date -> new Holiday(date))
                .collect(Collectors.toList());
    }

    public static List<Holiday> toHolidayListByMonthDay(List<MonthDay> holidayList) {
        return holidayList.stream()
                .map(date -> new Holiday(date))
                .collect(Collectors.toList());
    }

    public static List<Holiday> toHolidayList(String dayMonthDelimiter, String dateDelimiter, String inputHolidayList) {
        String[] holidaysArr = inputHolidayList.split(dateDelimiter);
        ArrayList<String> holidaysArrList = new ArrayList<String>(Arrays.asList(holidaysArr));
        int currentYear = LocalDate.now().getYear();

        List<LocalDate> localDateList = holidaysArrList.stream()
                .map(dateAsString ->
                        LocalDate.of( currentYear, Integer.parseInt(dateAsString.trim().split(dayMonthDelimiter)[0].trim()),
                                Integer.parseInt(dateAsString.trim().split(dayMonthDelimiter)[1].trim()) ) )
                .collect(Collectors.toList());

        return toHolidayList(localDateList);
    }


    // PRINTING DURATION METHODS

    public static String printDuration(Duration diff) {
        return "P" + diff.toDays() + "DT" + toHours(diff) + "H" + toMinutes(diff) + "M" + toSeconds(diff) + "S";
    }

    public static int toHours(Duration diff) {
        return (int) (diff.toHours() % 24);
    }

    public static int toMinutes(Duration diff) {
        return (int) (diff.toMinutes() % 60);
    }

    public static int toSeconds(Duration diff) {
        return (int) (diff.getSeconds() % 60);
    }


    // CHECKING YEARS AND MONTH VALIDITY METHODS

    public static void checkYearsOrMonthsValidity(Integer[] yearsOrMonths) {
        for (Integer el : Arrays.asList(yearsOrMonths)) {
            int valueOf_digits = String.valueOf(el).length();
            assert valueOf_digits != 3 && valueOf_digits <= 4 : "Number of digits must not be greater than 4 neither equal to 3";
        }
    }

    public static void checkYearsValidity(Integer[] years) {
        for (Integer el : Arrays.asList(years)) {
            assert String.valueOf(el).length() == 4 : "Number of digits must be equal to 4";
        }
    }

    public static boolean isInRange(LocalDate date, TemporalRange range) {
        return date.isAfter(range.getStartingDate()) && date.isBefore(range.getEndingDate());
    }

}
