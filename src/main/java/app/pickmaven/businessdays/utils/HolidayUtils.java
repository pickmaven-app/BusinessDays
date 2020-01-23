package app.pickmaven.businessdays.utils;

import app.pickmaven.businessdays.Holiday;
import app.pickmaven.businessdays.TemporalRange;

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

    /**
     * Get Easter {@code LocalDate} for specific year.
     *
     * @param year for computing Easter
     * @return Easter date
     */
    public static LocalDate getEaster(final int year) {
        final int dayOfMarch = getEasterDelay(year);
        return LocalDate.of(year, 3, 1).plusDays(dayOfMarch - 1);
    }

    /**
     * Get Easter Monday {@code LocalDate} for specific year.
     *
     * @param year for computing Easter Monday
     * @return Easter Monday date
     */
    public static LocalDate getEasterMonday(final int year) {
        final int dayOfMarch = getEasterDelay(year);
        return LocalDate.of(year, 3, 1).plusDays(dayOfMarch);
    }

    /**
     * Get delay of days to add from 1st March to get Easter Monday.
     */
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

    /**
     * @param holidayList list of dates
     * @return list of {@code Holiday} from a list of {@code LocalDate}
     */
    public static List<Holiday> toHolidayList(List<LocalDate> holidayList) {
        return holidayList.stream()
                .map(date -> new Holiday(date))
                .collect(Collectors.toList());
    }

    /**
     * @param holidayList list of month-day dates
     * @return list of {@code Holiday} from a list of {@code MonthDay}
     */
    public static List<Holiday> toHolidayListByMonthDay(List<MonthDay> holidayList) {
        return holidayList.stream()
                .map(date -> new Holiday(date))
                .collect(Collectors.toList());
    }

    /**
     * @param dayMonthDelimiter delimiter between month and day
     * @param dateDelimiter delimiter among dates
     * @param inputHolidayList list of date strings
     * @return list of {@code Holiday} from list of dates as strings
     */
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

    /**
     * Prints the {@code Duration} diff in format: P_DT_H_M_S
     *
     * @param diff duration
     * @return string representing duration
     */
    public static String printDuration(Duration diff) {
        return "P" + diff.toDays() + "DT" + toHours(diff) + "H" + toMinutes(diff) + "M" + toSeconds(diff) + "S";
    }

    /**
     * Gets number of remaining hours element from a {@code Duration} object.
     *
     * @param diff duration
     * @return number of hours
     */
    public static int toHours(Duration diff) {
        return (int) (diff.toHours() % 24);
    }

    /**
     * Gets number of remaining minutes element from a {@code Duration} object.
     *
     * @param diff duration
     * @return number of minutes
     */
    public static int toMinutes(Duration diff) {
        return (int) (diff.toMinutes() % 60);
    }

    /**
     * Gets number of remaining seconds element from a {@code Duration} object.
     *
     * @param diff duration
     * @return number of seconds
     */
    public static int toSeconds(Duration diff) {
        return (int) (diff.getSeconds() % 60);
    }


    // CHECKING YEARS AND MONTH VALIDITY METHODS

    /**
     * Checks that integers in yearsOrMonths are 4-digit or 2-digit; otherwise throw assertion.
     *
     * @param yearsOrMonths to check validity
     */
    public static void checkYearsOrMonthsValidity(Integer[] yearsOrMonths) {
        for (Integer el : Arrays.asList(yearsOrMonths)) {
            int valueOf_digits = String.valueOf(el).length();
            assert valueOf_digits == 2 || valueOf_digits == 4 : "Number of digits must be 4 or 2";
        }
    }

    /**
     * Checks that integers in years are 4-digit; otherwise throw assertion.
     *
     * @param years to check
     */
    public static void checkYearsValidity(Integer[] years) {
        for (Integer el : Arrays.asList(years)) {
            assert String.valueOf(el).length() == 4 : "Number of digits must be equal to 4";
        }
    }

    /**
     * Checks if {@code LocalDate} date is included in {@code TemporalRange} range.
     *
     * @param date to search in range
     * @param range to check if contains date
     * @return true if date in in range
     */
    public static boolean isInRange(LocalDate date, TemporalRange range) {
        return date.isAfter(range.getStartingDate()) && date.isBefore(range.getEndingDate());
    }

    /**
     * Checks if year of starting date is among years(4 digits) and if month of starting
     * date is among months(2 digits) in yearsOrMonths_forBusinessSaturday list; if list is empty it returns true.
     *
     * @return true if starting date year and month is in yearsOrMonths list
     */
    public static boolean checkYearsOrMonths(List<Integer> yearsOrMonths, LocalDate startingDate) {
        if (yearsOrMonths.isEmpty()) {
            return true;
        }

        List<Integer> years = getByCharNumber(yearsOrMonths, 4);
        List<Integer> months = getByCharNumber(yearsOrMonths, 2);

        boolean yearsChecked = true;
        boolean monthsChecked = true;

        if (!years.isEmpty()) {
            yearsChecked =  years.stream()
                    .anyMatch(y -> y.equals(startingDate.getYear()));
        }

        if (!months.isEmpty()) {
            monthsChecked =  months.stream()
                    .anyMatch(y -> y.equals(startingDate.getMonthValue()));
        }

        return yearsChecked && monthsChecked;
    }


    /**
     * Filters the list of integer by number of digits.
     *
     * @param yearsOrMonths
     * @param digits
     * @return a list of integers filtered by number of digits
     */
    private static List<Integer> getByCharNumber(List<Integer> yearsOrMonths, int digits) {
        assert yearsOrMonths != null : "yearsOrMonths must not be null";
        assert digits == 4 | digits == 2 : "digits must be only 4 or 2";

        return yearsOrMonths.stream()
                .filter(el -> normalizeNumber(el).length() == digits)
                .collect(Collectors.toList());
    }

    /**
     * Normalizes the integer adding a leading '0' where is the case, id est where is a 1 digit number.
     *
     * @param el number to be normalized
     * @return String value normalized
     */
    private static String normalizeNumber(Integer el) {
        String value = String.valueOf(el);
        return value.length() == 1 ? String.format("0%s", value) : value;
    }

}
