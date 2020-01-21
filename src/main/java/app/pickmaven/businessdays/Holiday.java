package app.pickmaven.businessdays;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.MonthDay;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * The base unit to refer to the holiday date.
 *
 * <p>
 * {@code app.pickmaven.businessdays.Holiday} is an immutable object that wraps a date as a {@code LocalDate} object.
 * <p>
 *
 * @implSpec
 * This class is immutable and thread-safe
 *
 * @author Daniele Gubbiotti
 *
 */
public class Holiday {

    /**
     * The {@code app.pickmaven.businessdays.Holiday} represented by today.
     */
    public static final Holiday HOLIDAY_NOW = new Holiday(LocalDate.now());

    /**
     * The {@code app.pickmaven.businessdays.Holiday} represented by Easter.
     */
    public static final Holiday EASTER = getEaster();

    /**
     * The {@code app.pickmaven.businessdays.Holiday} represented by Easter Monday.
     */
    public static final Holiday EASTER_MONDAY = getEasterMonday();

    /**
     * The {@code app.pickmaven.businessdays.Holiday} represented by Christmas day.
     */
    public static final Holiday CHRISTMAS = getChristmasDay();

    /**
     * The {@code LocalDate} representing the holiday.
     */
    private final LocalDate date;

    //-----------------------------------------------------------------------

    /**
     * Create an holiday from a {@code LocalDate} object.
     * @param date
     */
    public Holiday(LocalDate date) {
        this.date = date;
    }

    /**
     * Create an holiday from a {@code String} object representing the date and a {@code DateTimeFormatter}.
     * @param date
     * @param formatter
     */
    public Holiday(String date, DateTimeFormatter formatter) {
        this.date = LocalDate.parse(date, formatter);
    }

    /**
     * Create an holiday from a {@code String} object representing the date and a {@code String} representing the formatter pattern.
     * @param date
     * @param formatter
     */
    public Holiday(String date, String formatter) {
        this.date = LocalDate.parse(date, DateTimeFormatter.ofPattern(formatter));
    }

    /**
     * Create an holiday from a {@code String} object representing the date and a {@code SimpleDateFormat} object.
     * @param date
     * @param formatter
     */
    public Holiday(String date, SimpleDateFormat formatter) {
        this.date = LocalDate.parse(date, DateTimeFormatter.ofPattern(formatter.toPattern()));
    }

    /**
     * Create an holiday from a {@code Date} object.
     * @param date
     */
    public Holiday(Date date) {
        this.date = LocalDate.from(date.toInstant().atZone(ZoneId.systemDefault()));
    }

    /**
     * Create an holiday from a {@code MonthDay} object assuming the year as the current one.
     * @param date
     */
    public Holiday(MonthDay date) {
        this.date = date.atYear(LocalDate.now().getYear());
    }

    //-----------------------------------------------------------------------

    /**
     * Return instance of @code app.pickmaven.businessdays.Holiday} with the current date.
     * @return
     */
    public static Holiday today() {
        return HOLIDAY_NOW;
    }

    /**
     * Return instance of {@code app.pickmaven.businessdays.Holiday} from year, month and day.
     * @param year
     * @param month
     * @param day
     * @return
     */
    public static Holiday of(int year, int month, int day) {
        return new Holiday(LocalDate.of(year, month, day));
    }

    /**
     * Return an instance of {@code app.pickmaven.businessdays.Holiday} from the Easter date for the current year.
     * @return
     */
    private static Holiday getEaster() {
        LocalDate easter = HolidayUtils.getEaster(LocalDate.now().getYear());
        return new Holiday(easter);
    }

    /**
     * Return an instance of {@code app.pickmaven.businessdays.Holiday} from the Easter Monday date for the current year.
     * @return
     */
    private static Holiday getEasterMonday() {
        LocalDate easterMonday = HolidayUtils.getEasterMonday(LocalDate.now().getYear());
        return new Holiday(easterMonday);
    }

    /**
     * Return an instance of {@code app.pickmaven.businessdays.Holiday} from the Easter Monday date for the specified year.
     * @param year
     * @return
     */
    public static Holiday getEasterMonday(int year) {
        LocalDate easterMonday = HolidayUtils.getEasterMonday(year);
        return new Holiday(easterMonday);
    }

    /**
     * Return an instance of {@code app.pickmaven.businessdays.Holiday} from the Christmas date.
     * @return
     */
    private static Holiday getChristmasDay() {
        return new Holiday(LocalDate.of(LocalDate.now().getYear(), 12, 25));
    }

    //-----------------------------------------------------------------------

    /**
     * Gets the holiday date.
     * @return
     */
    public LocalDate getDate() {
        return date;
    }

    /**
     * Gets the year of holiday.
     * @return
     */
    public int getYear() {
        return date.getYear();
    }

    /**
     * Gets the month of holiday.
     * @return
     */
    public int getMonth() {
        return date.getMonth().getValue();
    }

    /**
     * Gets the day of holiday.
     * @return
     */
    public int getDay() {
        return date.getDayOfMonth();
    }

    //-----------------------------------------------------------------------

    /**
     * Check if this holiday date is equal to another holiday date.
     * @param o
     * @return
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Holiday holiday = (Holiday) o;
        return getDate().compareTo(holiday.getDate()) == 0;
        //return Objects.equals(getDate().format(DateTimeFormatter.ISO_DATE), holiday.getDate().format(DateTimeFormatter.ISO_DATE));
    }

    /**
     * A hash code for this holiday date.
     *
     * @return
     */
    @Override
    public int hashCode() {
        return Objects.hash(getDate());
    }

    /**
     * Outputs the holiday date as date.
     * @return
     */
    @Override
    public String toString() {
        return "app.pickmaven.businessdays.Holiday{" +
                "date=" + date +
                '}';
    }

}
