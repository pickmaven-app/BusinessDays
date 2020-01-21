package app.pickmaven.businessdays;

import app.pickmaven.businessdays.Holiday;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class HolidayTest {

    Holiday holidayToday = null;
    LocalDate now = null;
    LocalDate date = null;
    Holiday holiday = null;
    Holiday holidayFromString = null;


    @BeforeEach
    void setUp() {
        now = LocalDate.now();
        holidayToday = Holiday.today();
        date = LocalDate.of(2019, 12, 12);
        holiday = new Holiday(date);
        holidayFromString = new Holiday("2019-12-12", "yyyy-MM-dd");
    }

    @Test
    void getDate() {
        assertNotNull(holidayToday);
        System.out.println(holidayToday.getDate());
    }

    @Test
    void equals() {
        Holiday hol = Holiday.of(2019, 12, 12);
        assertEquals(hol, holiday);
    }

    @Test
    void holidayFromStringTest() {
        assertNotNull(holidayFromString.getDate());
    }



}