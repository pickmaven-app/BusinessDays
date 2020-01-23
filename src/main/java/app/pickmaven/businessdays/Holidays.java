package app.pickmaven.businessdays;

import app.pickmaven.businessdays.utils.HolidayUtils;

import java.time.LocalDate;
import java.time.MonthDay;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * The collection object storing reference to the {@code app.pickmaven.businessdays.Holiday} objects.
 *
 * <p>
 * {@code app.pickmaven.businessdays.Holidays} stores a {@code List<app.pickmaven.businessdays.Holiday>} object and implements some operations of
 * {@code List} interface such as {@code Add}, {@code Remove}, {@code Contains}, {@code Get} and others.
 * <p>
 *
 * The {@code List<app.pickmaven.businessdays.Holiday>} stored iside the {@code app.pickmaven.businessdays.Holidays} object is implemented by default as an {@code ArrayList<T>}.
 * The implemention can be set as {@code LinkedList<T>} calling the method {@code asLinkedList}.
 *
 */
public class Holidays {
    /**
     * List of {@code app.pickmaven.businessdays.Holiday} objects.
     */
    private List<Holiday> holidays;

    /**
     * Implements the list of {@code app.pickmaven.businessdays.Holiday} as {@LinkedList}; default false.
     */
    private boolean linkedlistImpl;

    /**
     * Constructor
     */
    public Holidays () {
        if (!linkedlistImpl) {
            this.holidays = new ArrayList<>();
        } else {
            this.holidays = new LinkedList<>();
        }
    }

    /**
     * Constructor from {@code app.pickmaven.businessdays.Holidays}
     *
     * @param holidays from which get the holiday list
     */
    public Holidays(Holidays holidays) {
        this.holidays = new ArrayList<>(holidays.getHolidays());
    }

    /**
     * Constructor from a list of {@code LocalDate}
     *
     * @param holidayList list of {@code Holiday}
     */
    public Holidays (List<LocalDate> holidayList) {
        if (!linkedlistImpl) {
            this.holidays = new ArrayList<Holiday>(HolidayUtils.toHolidayList(holidayList));
        } else {
            this.holidays = new LinkedList<Holiday>(HolidayUtils.toHolidayList(holidayList));
        }
    }

    /**
     * Constructor from an array of months and days and a delimiter.
     * The year of holiday date is the current year.
     *
     * @param delimiter delimiter for splitting monthAndDay dates
     * @param monthsAndDays {@code String[]} months and days
     */
    public Holidays (String delimiter, String... monthsAndDays) {
        List<MonthDay> holidayList = getLocalDateListByMonthAndDay(delimiter, monthsAndDays);
        if (!linkedlistImpl) {
            this.holidays = new ArrayList<Holiday>(HolidayUtils.toHolidayListByMonthDay(holidayList));
        } else {
            this.holidays = new LinkedList<Holiday>(HolidayUtils.toHolidayListByMonthDay(holidayList));
        }
    }

    /**
     * Constructor from list of holiday, given a date delimiter and a day-month delimiter.
     *
     * @param dayMonthDelimiter delimiter for splitting day and month
     * @param dateDelimiter delimiter for splitting dates
     * @param holidayList list of holiday
     */
    public Holidays (String dayMonthDelimiter, String dateDelimiter, String holidayList) {
        if (!linkedlistImpl) {
            this.holidays = new ArrayList<Holiday>(HolidayUtils.toHolidayList(dayMonthDelimiter, dateDelimiter, holidayList));
        } else {
            this.holidays = new LinkedList<Holiday>(HolidayUtils.toHolidayList(dayMonthDelimiter, dateDelimiter, holidayList));
        }
    }

    /**
     * Splits the array of month-day string and converts it in a list of {@code MonthDay}.
     *
     * @param delimiter delimiter for splitting dates
     * @param monthsAndDays String[] month and day
     * @return list of {@code MonthDay}
     */
    private List<MonthDay> getLocalDateListByMonthAndDay(String delimiter, String[] monthsAndDays) {
        List<String> holidayList = Arrays.asList(monthsAndDays);
        return holidayList.stream()
                .map(el -> el.trim().split(delimiter))
                .map(el ->
                        MonthDay.of(Integer.valueOf(el[0].trim()), Integer.valueOf(el[1].trim())))
                .collect(Collectors.toList());
    }

    /**
     * Adds the {@code app.pickmaven.businessdays.Holiday} to {@code app.pickmaven.businessdays.Holidays}
     *
     * @param holiday to add, not null
     */
    public void add(Holiday holiday) {
        assert Objects.nonNull(holiday)  : "app.pickmaven.businessdays.Holiday to add must be non null";
        holidays.add(holiday);
    }

    /**
     * Removes holiday from {@code app.pickmaven.businessdays.Holidays} if exists
     *
     * @param holiday to search for and remove, not null
     * @return true if holiday has been removed
     */
    public boolean remove(Holiday holiday) {
        assert Objects.nonNull(holiday)  : "app.pickmaven.businessdays.Holiday to remove must be non null";
        return holidays.remove(holiday);
    }

    /**
     * Checks if {@code app.pickmaven.businessdays.Holidays} contains {@code app.pickmaven.businessdays.Holiday} passed as parameter.
     *
     * @param holiday to search for, not null
     * @return true if {@code app.pickmaven.businessdays.Holidays} contains holiday
     */
    public boolean contains(Holiday holiday) {
        assert Objects.nonNull(holiday)  : "app.pickmaven.businessdays.Holiday to verify must be non null";
        return holidays.contains(holiday);
    }

    /**
     * Checks if this {@code app.pickmaven.businessdays.Holidays} contains all elements of the {@code app.pickmaven.businessdays.Holidays} object
     * passed in as parameter.
     *
     * @param holidayList {@code app.pickmaven.businessdays.Holidays} this should contain
     * @return true if all elements of holidayList is contained in this {@code app.pickmaven.businessdays.Holidays}
     */
    public boolean containsAll(Holidays holidayList) {
        assert Objects.nonNull(holidayList)  : "app.pickmaven.businessdays.Holidays to verify must be non null";
        assert holidayList.size() <= holidays.size()  : "app.pickmaven.businessdays.Holidays to verify exceed in size";
        return holidays.containsAll(holidayList.getHolidays());
    }

    /**
     * @return number of elements in {@code app.pickmaven.businessdays.Holidays}
     */
    public int size() {
        return holidays.size();
    }

    /**
     * Sets the internal implementation of list of holidas as {@code LinkedList}.
     *
     * @return this
     */
    public Holidays asLinkedList() {
        this.linkedlistImpl = true;
        return this;
    }

    /**
     * Gets the list of {@code app.pickmaven.businessdays.Holiday}. Side-effect free.
     *
     * @return list of {@code app.pickmaven.businessdays.Holiday}
     */
    public List<Holiday> getHolidays() {
        return new ArrayList<Holiday>(holidays);
    }

    /**
     * Returns the {@code app.pickmaven.businessdays.Holiday} at position i.
     *
     * @param i position of {@code app.pickmaven.businessdays.Holiday in the list}
     * @return {@code app.pickmaven.businessdays.Holiday}
     */
    public Holiday get(int i) {
        return holidays.get(i);
    }

    /**
     * Returns the date as string of {@code app.pickmaven.businessdays.Holiday} at position i, given a formatter pattern.
     *
     * @param i position in the list
     * @param formatter pattern for formatting the string
     * @return string representing date of {@code app.pickmaven.businessdays.Holiday}
     */
    public String getAsString(int i, DateTimeFormatter formatter) {
        return get(i).getDate().format(formatter);
    }

    /**
     * This method removes all {@code app.pickmaven.businessdays.Holiday} in {@code app.pickmaven.businessdays.Holidays} where the filter applies.
     *
     * @param filter predicate to apply
     * @return true if element has been removed
     */
    public boolean removeIf(Predicate<? super Holiday> filter) {
        Objects.requireNonNull(filter);
        boolean removed = false;
        final Iterator<Holiday> each = holidays.iterator();
        while (each.hasNext()) {
            if (filter.test(each.next())) {
                each.remove();
                removed = true;
            }
        }
        return removed;
    }

    /**
     * @return a {@code Stream<app.pickmaven.businessdays.Holiday>} from the {@code app.pickmaven.businessdays.Holidays}.
     */
    public Stream<Holiday> stream() {
        return holidays.stream();
    }

    /**
     * Adds all {@code app.pickmaven.businessdays.Holiday} of holidays to this.
     *
     * @param holidays {@code app.pickmaven.businessdays.Holidays} to add
     * @return true if all elements in holidays has been added to this
     */
    public boolean addAll(Holidays holidays) {
       return this.holidays.addAll(holidays.getHolidays());
    }
}
