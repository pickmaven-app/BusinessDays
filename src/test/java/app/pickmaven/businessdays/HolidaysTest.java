package app.pickmaven.businessdays;

import app.pickmaven.businessdays.GoogleCalendarAPI;
import app.pickmaven.businessdays.Holiday;
import app.pickmaven.businessdays.Holidays;
import app.pickmaven.businessdays.PublicHolidayAPI;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class HolidaysTest {

    private Holidays holidays;
    private Holiday holidayToday;
    private Holiday holiday;

    @BeforeEach
    void setUp() {
        holidays = new Holidays();
        holidayToday = Holiday.today();
        holiday = Holiday.of(2020, 02, 14);
    }

    @Test
    void add() {
        holidays.add(holidayToday);
        holidays.add(holiday);
        assertEquals(2, holidays.size());
    }

    @Test
    void remove() {
        holidays.add(holidayToday);
        holidays.add(holiday);

        holidays.remove(holiday);
        assertEquals(1, holidays.size());
        assertEquals(LocalDate.of(2019, 12, 16), holidays.get(0).getDate());
    }

    @Test
    void removeIf() {
        holidays.add(holidayToday);
        holidays.add(holiday);

        holidays.removeIf( hol -> hol.getYear() == 2020);
        assertEquals(1, holidays.size());
    }

    @Test
    public void stream() {
        holidays.add(holidayToday);
        holidays.add(holiday);
        List<LocalDate> dateList = holidays.stream()
                .filter( hol -> hol.getYear() == 2020 )
                .map(Holiday::getDate)
                .collect(Collectors.toList());
        assertEquals(1, dateList.size());
    }

    @Test
    public void getAsString() {
        holidays.add(holiday);
        assertEquals("2020-02-14", holidays.getAsString(0, DateTimeFormatter.ISO_DATE));
    }

    @Test
    void contains() {
        holidays.add(holidayToday);
        holidays.add(holiday);

        assertTrue(holidays.contains(holidayToday));
    }

    @Test
    void containsAll() {
        holidays.add(holidayToday);
        Holidays otherHolidays = new Holidays(holidays);
        otherHolidays.add(holiday);

        assertTrue(otherHolidays.containsAll(holidays));
    }

    @Test
    void holidaysByMonthDayList() {
        String delimiter = "-";

        Holidays holidays = new Holidays(delimiter, "01 -01", "02-14", "04-25", "08-15", "12-25");
        System.out.println(holidays.getHolidays());
        assertEquals(5, holidays.size());
    }

    @Test
    void holidaysByMonthDayListAsString() {
        String delimiter = "-";

        Holidays holidays = new Holidays(delimiter, "[|]"," 01 - 01 | 02-14 | 04-25 | 08-15 | 12-25");
        System.out.println(holidays.getHolidays());
        assertEquals(5, holidays.size());
    }


    @Test
    public void holidaysByPublicHolidayAPI() {
        PublicHolidayAPI api = new PublicHolidayAPI("44d8d06c54msh205ce87b9c95966p1f3265jsndbf4ed7a8d0d");
        List<LocalDate> dates = null;
        try {
            dates = api.searchHolidaysFor("IT");
        } catch (IOException e) {
            e.printStackTrace();
        }
        Holidays holidays = new Holidays(dates);
        System.out.println(dates);

        assertEquals(12, holidays.size());
    }

    @Test
    public void holidaysByPublicHolidayAPI_perYear() {
        PublicHolidayAPI api = new PublicHolidayAPI("44d8d06c54msh205ce87b9c95966p1f3265jsndbf4ed7a8d0d");
        List<LocalDate> dates = null;
        try {
            dates = api.searchHolidaysFor("IT", 2031);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Holidays holidays = new Holidays(dates);
        System.out.println(dates);

        assertEquals(12, holidays.size());
    }

    @Test
    public void holidaysByPublicHolidayAPI_withPredicate() {
        PublicHolidayAPI api = new PublicHolidayAPI("44d8d06c54msh205ce87b9c95966p1f3265jsndbf4ed7a8d0d");
        api.applyPredicate((p -> p.get("fixed").getAsBoolean() == true));
        List<LocalDate> dates = null;
        try {
            dates = api.searchHolidaysFor("IT");
        } catch (IOException e) {
            e.printStackTrace();
        }
        Holidays holidays = new Holidays(dates);
        System.out.println(dates);

        assertEquals(10, holidays.size());
    }

    @Test
    public void holidaysByGoogleAPI() {
        GoogleCalendarAPI calendarAPI = new GoogleCalendarAPI("C:\\Users\\danie\\Desktop\\code\\BusinessDays\\src\\main\\java\\my-project.json");
        List<LocalDate> dates = null;
        try {
            dates = calendarAPI.searchHolidaysFor("it.italian");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        }
        Holidays holidays = new Holidays(dates);
        System.out.println(dates);

        assertEquals(14, holidays.size());
    }

    @Test
    public void holidaysByGoogleAPI_withPredicate() {
        System.out.println("Working Directory = " +
                System.getProperty("user.dir"));
        GoogleCalendarAPI calendarAPI = new GoogleCalendarAPI("src/main/resources/token.json");
        calendarAPI.applyPredicate((p -> LocalDate.parse(p.getStart().getDate().toString(), DateTimeFormatter.ISO_DATE ).getMonthValue() > 4 ));
        List<LocalDate> dates = null;
        try {
            dates = calendarAPI.searchHolidaysFor("it.italian");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        }
        Holidays holidays = new Holidays(dates);
        System.out.println(dates);

        assertEquals(8, holidays.size());
    }



}