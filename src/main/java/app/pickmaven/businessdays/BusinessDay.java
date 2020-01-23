package app.pickmaven.businessdays;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This class is responsible for computing the next business day.
 *
 * <p>
 * {@code app.pickmaven.businessdays.BusinessDay} stores an {@code app.pickmaven.businessdays.Holidays} object. It offers different construction methods for setting up some
 * configurations such as the weekdays that will be treated as holidays or also the temporal range to which the calculation is applied.
 * <p>
 *
 * @implSpec
 * This class is immutable and thread-safe.
 *
 * @author Daniele Gubbiotti
 *
 */
public class BusinessDay {

    /**
     * {@code app.pickmaven.businessdays.Holidays} object.
     */
    private Holidays holidays;

    /**
     * The starting date, default today.
     */
    private LocalDate startingDate = LocalDate.now();

    /**
     * Date of the next business day.
     */
    private LocalDate nextBusinessDay;

    /**
     * Checks if saturday is business day or not.
     */
    private boolean isBusinessSaturday;

    /**
     * Checks if sunday is business day or not.
     */
    private boolean isBusinessSunday;

    /**
     * List of {@code DayOfWeek} objects to be treated as holiday; default empty list.
     */
    private List<DayOfWeek> holidayOnWeekDays = Collections.EMPTY_LIST;

    /**
     * List of years for which to apply the computing of the next business day; default empty list.
     */
    private List<Integer> years = Collections.EMPTY_LIST;


    //-----------------------------------------------------------------------

    /**
     * Private constructor
     */
    private BusinessDay() { }

    //-----------------------------------------------------------------------
    // GET NEXT BUSINESS DAY METHODS

    /**
     * Computes the next business day based on certain conditions.
     * <p>
     *     This method checks if starting date is not a business saturday or sunday, is not an holiday weekday or an holiday
     *     adding a day to the starting one only if theese conditions are met.
     * </p>
     *
     * @return the next business day
     */
    public BusinessDay nextBusinessDay() {
        int addedDays = 0;

        while (addedDays < 1) {
            startingDate = startingDate.plusDays(1);
            if ( conditionsAreMet() ) {
                ++addedDays;
            }
        }

        nextBusinessDay = startingDate;
        return this;
    }

    /**
     * Computes the next business day based on certain conditions adding a specific number of workdays.
     * <p>
     *   This method checks if starting date is not a business saturday or sunday, is not an holiday weekday or an holiday
     *   adding days to the starting one only if theese conditions are met.
     * </p>
     *
     * @param workDays the number to add from starting date
     * @return the next business day
     */
    public BusinessDay nextBusinessDay(int workDays) {
        int addedDays = 0;

        while (addedDays < workDays) {
            startingDate = startingDate.plusDays(1);
            if ( conditionsAreMet() ) {
                ++addedDays;
            }
        }

        nextBusinessDay = startingDate;
        return this;
    }

    /**
     * Checks if is not an holiday or an holiday on saturday, sunday or another weekday.
     *
     * @return true if one of the following conditions are not met
     */
    private boolean conditionsAreMet() {
        return !( isHolidayOnSaturday() || isHolidayOnSunday() || isHolidayOnWeekDays() || isHoliday() );
    }

    /**
     * @return true if is not a business saturday
     */
    private boolean isHolidayOnSaturday() {
        if (!isBusinessSaturday ) {
            return startingDate.getDayOfWeek() == DayOfWeek.SATURDAY;
        }

        return false;
    }

    /**
     * @return true if is not a business sunday
     */
    private boolean isHolidayOnSunday() {
        if (!isBusinessSunday) {
            return startingDate.getDayOfWeek() == DayOfWeek.SUNDAY;
        }

        return false;
    }

    /**
     * Checks if the dayOfWeek of starting date is in list of holiday-weekdays stored in holidayOnWeekDays.
     *
     * @return true if is holiday on week day
     */
    private boolean isHolidayOnWeekDays() {
        if (checkYears()) {
            return holidayOnWeekDays.stream()
                    .anyMatch(day -> startingDate.getDayOfWeek() == day);
        }
        return false;
    }

    /**
     * Checks if starting date is in holidays list.
     *
     * @return true if starting date is holiday
     */
    private boolean isHoliday() {
        if (checkYears()) {
            return holidays.stream()
                    .anyMatch(hol -> hol.getDate().isEqual(startingDate));
        }

        return false;
    }

    /**
     *
     * @return true if starting date is in years list; true if years is empty
     */
    private boolean checkYears() {
        if (years.isEmpty()) {
            return true;
        }

        return years.stream()
                .anyMatch(y -> y.equals(startingDate.getYear()));
    }


    // FORMATTING NEXT BUSINESS DAY METHODS

    /**
     * @return next business day as {@code LocalDate}
     */
    public LocalDate asLocalDate() {
        return nextBusinessDay;
    }

    /**
     * @return next business day as {@code Date}
     */
    public Date asDate() {
        return Date.from(nextBusinessDay.atStartOfDay()
                .atZone(ZoneId.systemDefault())
                .toInstant());
    }

    /**
     * Return next business day as long value given hours, minutes and seconds to add.
     *
     * @param hoursMinutesSecondsCommaSeparated hours, minutes and seconds comma separated; default '0'
     * @return next business day as long value
     */
    public Long asLong(int... hoursMinutesSecondsCommaSeparated) {
        int hours = hoursMinutesSecondsCommaSeparated.length > 0 ? hoursMinutesSecondsCommaSeparated[0] : 0;
        int minutes = hoursMinutesSecondsCommaSeparated.length > 1 ? hoursMinutesSecondsCommaSeparated[1] : 0;
        int seconds = hoursMinutesSecondsCommaSeparated.length == 3 ? hoursMinutesSecondsCommaSeparated[2] : 0;

        ZonedDateTime zdt = ZonedDateTime.of(
                nextBusinessDay, LocalTime.of(hours, minutes, seconds), ZoneId.systemDefault()
        );
        return zdt.toEpochSecond();
    }

    /**
     * Outputs the date as string given a {@code DateTimeFormatte}.
     *
     * @param formatter {@code DateTimeFormatter} pattern object for formatting the string
     * @return next business day as formatted string
     */
    public String asString(DateTimeFormatter formatter) {
        return nextBusinessDay.format(formatter);
    }

    /**
     * Outputs the date as string given a string formatter.
     *
     * @param pattern pattern for formatting the string
     * @return next business day as formatted string
     */
    public String asString(String pattern) {
        return nextBusinessDay.format(DateTimeFormatter.ofPattern(pattern));
    }

    /**
     * @return next business day as {@code Period}
     */
    public Period asPeriod() {
        LocalDate today = LocalDate.now();
        return Period.between(today, nextBusinessDay);
    }

    /**
     * Prints the {@code Duration} between current date and the next business day.
     *
     * @param hoursMinutesCommaSeparated hours and minutes comma separated, default '00'
     * @return interval between today and next business day as {@code Duration}
     */
    public Duration asDuration( String... hoursMinutesCommaSeparated) {
        for (String s : hoursMinutesCommaSeparated ) {
            assert s.length() <= 2 : "Hours and Minutes cannot contain more than 2 characters";
        }
        String hours = hoursMinutesCommaSeparated.length > 0 ? hoursMinutesCommaSeparated[0] : "00";
        String minutes = hoursMinutesCommaSeparated.length == 2 ? hoursMinutesCommaSeparated[1] : "00";

        hours = hours.length() == 1 ? "0" + hours : hours;
        minutes = minutes.length() == 1 ? "0" + minutes : minutes;

        LocalDateTime oggi = LocalDateTime.now();
        LocalDateTime dataNextWorkingDay = LocalDateTime.parse(asString("yyyy-MM-dd") + " " + hours + ":" + minutes,
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));

        return Duration.between(oggi, dataNextWorkingDay);
    }

    /**
     * Prints the duration between current date and the next business day as a string in format 'PnDTnHnMnS'.
     *
     * @param hoursMinutesCommaSeparated hours and minutes comma separated, default '00'
     * @return interval between today and next business day as {@code String}
     */
    public String asDateTimeDuration(String... hoursMinutesCommaSeparated) {
        Duration duration = asDuration(hoursMinutesCommaSeparated);

        if (duration.isNegative()) {
            return "P0DT0H0M0S";
        } else {
            return HolidayUtils.printDuration(duration);
        }
    }

    // GETTING HOLIDAYS METHODS

    /**
     * Gets the {@code app.pickmaven.businessdays.Holidays} object; this method gives you the possibility to filter the holidays based on years
     * you pass as integers.
     *
     * @param years Integer[] years for filtering the holidays in {@code app.pickmaven.businessdays.Holidays} object
     * @return {@code app.pickmaven.businessdays.Holidays}
     */
    public Holidays getHolidays(Integer... years) {
        HolidayUtils.checkYearsValidity(years);
        List<LocalDate> holidayList;
        if (years.length == 0) {
            holidayList = holidays.stream().map(Holiday::getDate).collect(Collectors.toList());
            return new Holidays(holidayList);
        }

        holidayList =  holidays.getHolidays().stream()
                .filter(y -> Arrays.asList(years).contains(y.getYear()))
                .map(Holiday::getDate)
                .collect(Collectors.toList());

        return new Holidays(holidayList);
    }

    /**
     * Gets the {@code app.pickmaven.businessdays.Holidays} object starting from current date; this method gives you the possibility to filter the holidays based on years
     * you pass as integers.
     *
     * @param years Integer[] years for filtering the holidays in {@code app.pickmaven.businessdays.Holidays} object
     * @return {@code app.pickmaven.businessdays.Holidays}
     */
    public Holidays getHolidaysFromNow(Integer... years) {
        List<LocalDate> holidayList;
        if (years.length > 0) {
            HolidayUtils.checkYearsValidity(years);
            holidayList = holidays.stream()
                    .filter( y -> y.getDate().isAfter(LocalDate.now())
                            && Arrays.asList(years).contains(y.getYear()) )
                    .map(Holiday::getDate)
                    .collect(Collectors.toList());

            return new Holidays(holidayList);
        }

        holidayList =  holidays.stream()
                .filter(y -> y.getDate().isAfter(LocalDate.now()) )
                .map(Holiday::getDate)
                .collect(Collectors.toList());

        return new Holidays(holidayList);
    }

    /**
     * Gets the {@code app.pickmaven.businessdays.Holidays} object starting from starting date; this method gives you the possibility to filter the holidays based on years
     * you pass as integers.
     *
     * @param years Integer[] years for filtering the holidays in {@code app.pickmaven.businessdays.Holidays} object
     * @return {@code app.pickmaven.businessdays.Holidays}
     */
    public Holidays getHolidaysFromStartingDate(Integer... years) {
        List<LocalDate> holidayList;
        if (years.length > 0) {
            HolidayUtils.checkYearsValidity(years);
            holidayList = holidays.stream()
                    .filter( y -> y.getDate().isAfter(startingDate)
                            && Arrays.asList(years).contains(y.getYear()) )
                    .map(Holiday::getDate)
                    .collect(Collectors.toList());
            return new Holidays(holidayList);
        }

        holidayList =  holidays.stream()
                .filter(y -> y.getDate().isAfter(startingDate) )
                .map(Holiday::getDate)
                .collect(Collectors.toList());

        return new Holidays(holidayList);
    }

    /**
     * Gets the {@code app.pickmaven.businessdays.Holidays} object filtered based temporal range you pass as parameter.
     *
     * @param range {@code app.pickmaven.businessdays.TemporalRange} range for filtering the holidays in {@code app.pickmaven.businessdays.Holidays} object
     * @return {@code app.pickmaven.businessdays.Holidays}
     */
    public Holidays getHolidaysForTemporalRange(TemporalRange range) {
        List<LocalDate> holidayList =  holidays.stream()
                .filter(holiday -> holiday.getDate().isAfter(range.getStartingDate()) &&
                        holiday.getDate().isBefore(range.getEndingDate()))
                .map(Holiday::getDate)
                .collect(Collectors.toList());

        return new Holidays(holidayList);
    }


    //-----------------------------------------------------------------------
    // GETTERS AND SETTERS

    /**
     * @return {@code app.pickmaven.businessdays.Holidays}
     */
    private Holidays getHolidays() {
        return holidays;
    }

    /**
     * Sets {@code app.pickmaven.businessdays.Holidays}
     *
     * @param holidays
     */
    private void setHolidays(Holidays holidays) {
        this.holidays = holidays;
    }

    /**
     * @return true if is business saturday
     */
    private boolean isBusinessSaturday() {
        return isBusinessSaturday;
    }

    /**
     * Sets business saturday; this method gives you the possibility to set business saturday only to specific years or months.
     *
     * @param yearsOrMonths Integer[] years or months to apply the business saturday
     */
    private void setBusinessSaturday(Integer... yearsOrMonths) {
        isBusinessSaturday = HolidayUtils.checkYearsOrMonths(Arrays.asList(yearsOrMonths), startingDate);
    }

    /**
     * Sets business saturday only for specific {@code app.pickmaven.businessdays.TemporalRange}.
     *
     * @param range
     */
    private void setBusinessSaturday(TemporalRange range) {
        isBusinessSaturday = range.includes(startingDate);
    }

    /**
     * @return true if is business sunday
     */
    private boolean isBusinessSunday() {
        return isBusinessSunday;
    }

    /**
     * Sets business sunday; this method gives you the possibility to set business sunday only to specific years or months.
     *
     * @param yearsOrMonths Integer[] years or months to apply the business sunday
     */
    private void setBusinessSunday(Integer... yearsOrMonths) {
        isBusinessSunday = HolidayUtils.checkYearsOrMonths(Arrays.asList(yearsOrMonths), startingDate);
    }

    /**
     * Sets business sunday only for specific {@code app.pickmaven.businessdays.TemporalRange}.
     *
     * @param range
     */
    private void setBusinessSunday(TemporalRange range) {
        isBusinessSunday = range.includes(startingDate);
    }

    /**
     * @return starting date
     */
    private LocalDate getStartingDate() {
        return startingDate;
    }

    /**
     * Sets the starting date
     *
     * @param startingDate
     */
    private void setStartingDate(LocalDate startingDate) {
        this.startingDate = startingDate;
    }

    /**
     * Sets weekdays to be treated as {@code app.pickmaven.businessdays.Holiday}.
     *
     * @param weekDays
     */
    private void setHolidayOnWeekDays(DayOfWeek... weekDays) {
        this.holidayOnWeekDays = Arrays.asList(weekDays);
    }

    /**
     * Sets years to apply computing of the next business day.
     *
     * @param years Integer[] years to apply computing of next business day
     */
    private void setYears(Integer... years) {
        this.years = Arrays.asList(years);
    }

    //-----------------------------------------------------------------------
    // STATIC BUILDER

    /**
     * Public builder for creating instances of {@code app.pickmaven.businessdays.BusinessDay} objects.
     */
    public static final class Builder {

        /**
         * {@code app.pickmaven.businessdays.BusinessDay} object.
         */
        private BusinessDay businessDay;

        /**
         * Constructor. It sets by default an empty {@code app.pickmaven.businessdays.Holidays} object.
         */
        public Builder() {
            businessDay = new BusinessDay();
            businessDay.setHolidays(new Holidays());
        }

        /**
         * @return instance of builder
         */
        public static Builder aBusinessDay() {
            return new Builder();
        }

        /**
         * Sets the {@code app.pickmaven.businessdays.Holidays} object in the {@code app.pickmaven.businessdays.BusinessDay} object.
         *
         * @param holidays to assign to {@code app.pickmaven.businessdays.BusinessDay}
         * @return this
         */
        public Builder givenHolidays(Holidays holidays) {
            assert holidays != null : "app.pickmaven.businessdays.Holidays must not be null";
            Holidays hol = businessDay.getHolidays();
            if ( hol.addAll(holidays) ) {
                businessDay.setHolidays(hol);
            }
            return this;
        }

        /**
         * Adds the Easter as {@code app.pickmaven.businessdays.Holiday} if is not already present in {@code app.pickmaven.businessdays.Holidays}.
         *
         * @return this
         */
        public Builder computingEaster() {
            Holiday easter = Holiday.EASTER;
            if (!businessDay.getHolidays().contains(easter)) {
                businessDay.getHolidays().add(easter);
            }
            return this;
        }

        /**
         * Adds the Easter Monday as {@code app.pickmaven.businessdays.Holiday} if is not already present in {@code app.pickmaven.businessdays.Holidays}.
         *
         * @return
         */
        public Builder computingEasterMonday() {
            Holiday easterMonday = null;
            easterMonday = Holiday.EASTER_MONDAY;

            if (businessDay.getStartingDate().getYear() != LocalDate.now().getYear() ) {
                easterMonday = Holiday.getEasterMonday(businessDay.getStartingDate().getYear());
            }

            if (!businessDay.getHolidays().contains(easterMonday)) {
                businessDay.getHolidays().add(easterMonday);
            }
            return this;
        }

        /**
         * Adds the Christmas as {@code app.pickmaven.businessdays.Holiday} if is not already present in {@code app.pickmaven.businessdays.Holidays}.
         *
         * @return
         */
        public Builder computingChristmas() {
            Holiday christmas = Holiday.CHRISTMAS;
            if (!businessDay.getHolidays().contains(christmas)) {
                businessDay.getHolidays().add(christmas);
            }
            return this;
        }

        /**
         * Sets saturday as business day for years or months passed as parameter; if no years or months are passed in it sets to true.
         *
         * @param yearsOrMonths
         * @return this
         */
        public Builder withBusinessSaturday(Integer... yearsOrMonths) {
            HolidayUtils.checkYearsOrMonthsValidity(yearsOrMonths);
            businessDay.setBusinessSaturday(yearsOrMonths);
            return this;
        }

        /**
         * Sets saturday as business day for the {@code app.pickmaven.businessdays.TemporalRange} passed as parameter.
         *
         * @param range
         * @return this
         */
        public Builder withBusinessSaturday(TemporalRange range) {
            assert range != null : "Temporal Range must not be null!";
            businessDay.setBusinessSaturday(range);
            return this;
        }

        /**
         * Sets sunday as business day for years or months passed as parameter; if no years or months are passed in it sets to true.
         *
         * @param yearsOrMonths
         * @return this
         */
        public Builder withBusinessSunday(Integer... yearsOrMonths) {
            HolidayUtils.checkYearsOrMonthsValidity(yearsOrMonths);
            businessDay.setBusinessSunday(yearsOrMonths);
            return this;
        }

        /**
         * Sets sunday as business day for the {@code app.pickmaven.businessdays.TemporalRange} passed as parameter.
         *
         * @param range, not null
         * @return this
         */
        public Builder withBusinessSunday(TemporalRange range) {
            assert range != null : "Temporal Range must not be null!";
            businessDay.setBusinessSunday(range);
            return this;
        }

        /**
         * Sets weekdays as holiday. This method takes precedence over 'withBusinessSaturday()' and 'withBusinessSunday()' ones.
         *
         * @param weekDays DayOfWeek[] weekdays to be holiday
         * @return this
         */
        public Builder holidayOnWeekDays(DayOfWeek... weekDays) {
            businessDay.setHolidayOnWeekDays(weekDays);
            return this;
        }

        /**
         * Sets weekdays as holiday, applied to specific temporal range.
         * This method takes precedence over 'withBusinessSaturday()' and 'withBusinessSunday()' ones.
         *
         * @param range {@code app.pickmaven.businessdays.TemporalRange} to apply weekdays as holidays, not null
         * @param weekDays
         * @return this
         */
        public Builder holidayOnWeekDays(TemporalRange range, DayOfWeek... weekDays) {
            assert range != null : "Temporal Range must not be null!";
            if (HolidayUtils.isInRange(businessDay.getStartingDate(), range)) {
                businessDay.setHolidayOnWeekDays(weekDays);
            }

            return this;
        }

        /**
         * Sets the starting day for computing the next business day.
         *
         * @param startingDate for computing next business day
         * @return this
         */
        public Builder fromStartingDate(LocalDate startingDate) {
            businessDay.setStartingDate(startingDate);
            return this;
        }

        /**
         * Sets years to apply holidays for computing next business day.
         *
         * @param years to apply holidays
         * @return this
         */
        public Builder applyToYears(Integer... years) {
            HolidayUtils.checkYearsValidity(years);
            businessDay.setYears(years);
            return this;
        }

        /**
         * @return {@code app.pickmaven.businessdays.BusinessDay} instance
         */
        public BusinessDay build() {
            return businessDay;
        }


    }


}


