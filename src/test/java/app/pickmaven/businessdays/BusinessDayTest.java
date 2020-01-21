package app.pickmaven.businessdays;

import app.pickmaven.businessdays.BusinessDay;
import app.pickmaven.businessdays.Holidays;
import app.pickmaven.businessdays.TemporalRange;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BusinessDayTest {

    Holidays holidays;

    @BeforeEach
    void setUp() {
        List<LocalDate> holidayList = new ArrayList<LocalDate>(){{
                add(LocalDate.of(2019, 04, 25));
                add(LocalDate.of(2020, 04, 25));
                add(LocalDate.of(2019, 12, 26));
            }};
        holidays = new Holidays(holidayList);
    }


    // NEXT BUSINESS DAY FORMATTING

    @Test
    public void givenNoHolidays_thenNextWorkingDay_isTomorrow_asLocalDate() {
        BusinessDay b = BusinessDay.Builder.aBusinessDay()
                .build();

        assertEquals( LocalDate.of(2020, 01, 8), b.nextBusinessDay().asLocalDate() );
    }

    @Test
    public void givenNoHolidays_thenNextWorkingDay_isTomorrow_asStringWithFormatter() {
        BusinessDay b = BusinessDay.Builder.aBusinessDay()
                .build();

        assertEquals( "2019-12-20", b.nextBusinessDay().asString(DateTimeFormatter.ISO_DATE) );
    }

    @Test
    public void givenNoHolidays_thenNextWorkingDay_isTomorrow_asStringWithPattern() {
        BusinessDay b = BusinessDay.Builder.aBusinessDay()
                .build();

        assertEquals( "2019/12/20", b.nextBusinessDay().asString("yyyy/MM/dd") );
    }

    @Test
    public void givenNoHolidays_thenNextWorkingDay_isTomorrow_asDate() throws ParseException {
        BusinessDay b = BusinessDay.Builder.aBusinessDay()
                .build();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date d = formatter.parse("2019-12-20");
        assertEquals(d, b.nextBusinessDay().asDate() );
    }

    @Test
    public void givenNoHolidays_thenNextWorkingDay_isTomorrow_asLongValue() {
        BusinessDay b = BusinessDay.Builder.aBusinessDay()
                .build();
        System.out.println(b.nextBusinessDay().asLong(0, 0, 0));
        assertTrue(b.nextBusinessDay().asLong(0,0,0) instanceof Long );
    }

    @Test
    public void givenNoHolidays_thenNextWorkingDay_asDuration() {
        BusinessDay b = BusinessDay.Builder.aBusinessDay()
                .build();
        Duration d = b.nextBusinessDay(3).asDuration();
        System.out.println(d);
        assertTrue(d instanceof Duration );
        assertEquals(4, d.toDays());
    }

    @Test
    public void givenNoHolidays_thenNextWorkingDay_asDuration_givingHoursAndMinutes() {
        BusinessDay b = BusinessDay.Builder.aBusinessDay()
                .build();
        Duration d = b.nextBusinessDay(3).asDuration("8", "0");
        System.out.println(d);
        assertTrue(d instanceof Duration );
        assertEquals(4, d.toDays());
    }

    @Test
    public void givenNoHolidays_thenNextWorkingDay_asDuration_givingHours() {
        BusinessDay b = BusinessDay.Builder.aBusinessDay()
                .build();
        Duration d = b.nextBusinessDay(3).asDuration("8");
        System.out.println(d);
        assertTrue(d instanceof Duration );
        assertEquals(4, d.toDays());
    }

    @Test
    public void givenNoHolidays_thenNextWorkingDay_asDateTimeDuration() {
        BusinessDay b = BusinessDay.Builder.aBusinessDay()
                .build();
        String d = b.nextBusinessDay(3).asDateTimeDuration("8", "0");
        System.out.println(d);

        assertTrue(d.contains("4D"));
    }

    @Test
    public void givenNoHolidays_thenNextWorkingDay_asPeriod() {
        BusinessDay b = BusinessDay.Builder.aBusinessDay()
                .build();
        Period p = b.nextBusinessDay(3).asPeriod();
        System.out.println(p);

        assertTrue(p instanceof Period );
        assertEquals(5, p.getDays());
    }

    // NO HOLIDAYS

    @Test
    public void givenNoHolidays_thenNextWorkingDay_isTomorrow() {
        BusinessDay b = BusinessDay.Builder.aBusinessDay()
                .build();

        assertEquals( LocalDate.of(2019, 12, 20), b.nextBusinessDay().asLocalDate() );
    }

    @Test
    public void givenNoHolidays_thenNextWorkingDay_after3Days() {
        BusinessDay b = BusinessDay.Builder.aBusinessDay()
                .build();

        assertEquals( LocalDate.of(2020, 01, 10), b.nextBusinessDay(3).asLocalDate() );
    }

    @Test
    public void givenNoHolidays_startingFromTomorrow_thenNextWorkingDay_after3Days() {
        BusinessDay b = BusinessDay.Builder.aBusinessDay()
                .fromStartingDate(LocalDate.of(2019, 12, 18))
                .build();

        assertEquals( LocalDate.of(2019, 12, 23), b.nextBusinessDay(3).asLocalDate() );
    }

    @Test
    public void givenNoHolidays_startingFromTomorrow_withBusinessSaturday_thenNextWorkingDay_after3Days() {
        BusinessDay b = BusinessDay.Builder.aBusinessDay()
                .fromStartingDate(LocalDate.of(2019, 12, 18))
                .withBusinessSaturday()
                .build();

        assertEquals( LocalDate.of(2019, 12, 21), b.nextBusinessDay(3).asLocalDate() );
    }

    @Test
    public void givenNoHolidays_startingFromTomorrow_withBusinessSaturdayAndSundayNot_thenNextWorkingDay_after3Days() {
        BusinessDay b = BusinessDay.Builder.aBusinessDay()
                .fromStartingDate(LocalDate.of(2019, 12, 19))
                .withBusinessSaturday()
                .build();

        assertEquals( LocalDate.of(2019, 12, 23), b.nextBusinessDay(3).asLocalDate() );
    }

    @Test
    public void givenNoHolidays_startingFromTomorrow_withBusinessSaturdayAndSunday_thenNextWorkingDay_after3Days() {
        BusinessDay b = BusinessDay.Builder.aBusinessDay()
                .fromStartingDate(LocalDate.of(2019, 12, 19))
                .withBusinessSaturday()
                .withBusinessSunday()
                .build();

        assertEquals( LocalDate.of(2019, 12, 22), b.nextBusinessDay(3).asLocalDate() );
    }

    @Test
    public void givenNoHolidays_computingChristmas_thenNextWorkingDay_after24_12() {
        BusinessDay b = BusinessDay.Builder.aBusinessDay()
                .fromStartingDate(LocalDate.of(2019, 12, 24))
                .computingChristmas()
                .build();

        assertEquals( LocalDate.of(2019, 12, 26), b.nextBusinessDay().asLocalDate() );

    }

    @Test
    public void givenNoHolidays_computingChristmas_thenNextWorkingDayAfter3Days_after24_12() {
        BusinessDay b = BusinessDay.Builder.aBusinessDay()
                .fromStartingDate(LocalDate.of(2019, 12, 24))
                .computingChristmas()
                .build();

        assertEquals( LocalDate.of(2019, 12, 30), b.nextBusinessDay(3).asLocalDate() );

    }

    @Test
    public void givenNoHolidays_computingEaster_withSaturdayAndSundayBusiness_thenNextWorkingDay_after3Days() {
        BusinessDay b = BusinessDay.Builder.aBusinessDay()
                .fromStartingDate(LocalDate.of(2019, 04, 19))
                .withBusinessSaturday()
                .withBusinessSunday()
                .computingEaster()
                .build();

        assertEquals( LocalDate.of(2019, 04, 23), b.nextBusinessDay(3).asLocalDate() );

    }

    @Test
    public void givenNoHolidays_computingEasterAndEasterMonday_thenNextWorkingDay_after3Days() {
        BusinessDay b = BusinessDay.Builder.aBusinessDay()
                .fromStartingDate(LocalDate.of(2019, 04, 19))
                .computingEaster()
                .computingEasterMonday()
                .build();

        assertEquals( LocalDate.of(2019, 04, 25), b.nextBusinessDay(3).asLocalDate() );

    }

    @Test
    public void givenNoHolidays_withHolidayOnFriday_thenNextWorkingDay_after3Days() {
        BusinessDay b = BusinessDay.Builder.aBusinessDay()
                .fromStartingDate(LocalDate.of(2019, 12, 18))
                .holidayOnWeekDays(DayOfWeek.FRIDAY, DayOfWeek.THURSDAY)
                .build();

        assertEquals( LocalDate.of(2019, 12, 25), b.nextBusinessDay(3).asLocalDate() );

    }

    @Test
    public void givenNoHolidays_justFor2019_comutingEasterMonday_thenNextWorkingDay_after3Days() {
        BusinessDay b = BusinessDay.Builder.aBusinessDay()
                .fromStartingDate(LocalDate.of(2019, 04, 18))
                .computingEasterMonday()
                .applyToYears(2019)
                .build();

        assertEquals( LocalDate.of(2019, 04, 24), b.nextBusinessDay(3).asLocalDate() );

        BusinessDay b2 = BusinessDay.Builder.aBusinessDay()
                .fromStartingDate(LocalDate.of(2020, 04, 9))
                .computingEasterMonday()
                .applyToYears(2019)
                .build();

        assertEquals( LocalDate.of(2020, 04, 14), b2.nextBusinessDay(3).asLocalDate() );

    }



    // WITH HOLIDAYS

    @Test
    public void givenHolidays_thenNextWorkingDay_after3Days() {
        BusinessDay b = BusinessDay.Builder.aBusinessDay()
                .fromStartingDate(LocalDate.of(2019, 04, 23))
                .givenHolidays(holidays)
                .build();

        assertEquals( LocalDate.of(2019, 04, 29), b.nextBusinessDay(3).asLocalDate() );

    }

    @Test
    public void givenHolidays_thenNextWorkingDay_withBusinessSaturday_after3Days() {
        BusinessDay b = BusinessDay.Builder.aBusinessDay()
                .fromStartingDate(LocalDate.of(2019, 04, 23))
                .withBusinessSaturday()
                .givenHolidays(holidays)
                .build();

        assertEquals( LocalDate.of(2019, 04, 27), b.nextBusinessDay(3).asLocalDate() );

    }

    @Test
    public void givenHolidays_justFor2019_thenNextWorkingDay_after3Days() {
        BusinessDay b = BusinessDay.Builder.aBusinessDay()
                .fromStartingDate(LocalDate.of(2019, 04, 22))
                .givenHolidays(holidays)
                .applyToYears(2019)
                .build();

        assertEquals( LocalDate.of(2019, 04, 26), b.nextBusinessDay(3).asLocalDate() );

        BusinessDay b2 = BusinessDay.Builder.aBusinessDay()
                .fromStartingDate(LocalDate.of(2020, 04, 22))
                .givenHolidays(holidays)
                .withBusinessSaturday()
                .applyToYears(2019)
                .build();

        assertEquals( LocalDate.of(2020, 04, 25), b2.nextBusinessDay(3).asLocalDate() );

    }

    @Test
    public void givenHolidays_businessSaturdayFor2019_thenNextWorkingDay_after3Days() {
        BusinessDay b2019 = BusinessDay.Builder.aBusinessDay()
                .fromStartingDate(LocalDate.of(2019, 12, 18))
                .withBusinessSaturday(2019, 2021)
                .build();

        assertEquals( LocalDate.of(2019, 12, 21), b2019.nextBusinessDay(3).asLocalDate() );

        BusinessDay b2020 = BusinessDay.Builder.aBusinessDay()
                .fromStartingDate(LocalDate.of(2020, 01, 01))
                .withBusinessSaturday(2019, 2021)
                .build();

        assertEquals( LocalDate.of(2020, 01, 06), b2020.nextBusinessDay(3).asLocalDate() );

        BusinessDay b2021 = BusinessDay.Builder.aBusinessDay()
                .fromStartingDate(LocalDate.of(2021, 01, 06))
                .withBusinessSaturday(2019, 2021)
                .build();

        assertEquals( LocalDate.of(2021, 01, 9), b2021.nextBusinessDay(3).asLocalDate() );
    }

    @Test
    public void givenHolidays_businessSaturdayForDecemberAndFeb_thenNextWorkingDay_after3Days() {
        BusinessDay bDec = BusinessDay.Builder.aBusinessDay()
                .fromStartingDate(LocalDate.of(2019, 12, 18))
                .withBusinessSaturday(02, 12)
                .build();

        assertEquals( LocalDate.of(2019, 12, 21), bDec.nextBusinessDay(3).asLocalDate() );

        BusinessDay bNov = BusinessDay.Builder.aBusinessDay()
                .fromStartingDate(LocalDate.of(2019, 11, 06))
                .withBusinessSaturday(02, 12)
                .build();

        assertEquals( LocalDate.of(2019, 11, 11), bNov.nextBusinessDay(3).asLocalDate() );

        BusinessDay bFeb = BusinessDay.Builder.aBusinessDay()
                .fromStartingDate(LocalDate.of(2019, 02, 06))
                .withBusinessSaturday(02, 12)
                .build();

        assertEquals( LocalDate.of(2019, 02, 9), bFeb.nextBusinessDay(3).asLocalDate() );
    }

    @Test
    public void givenHolidays_businessSaturdayForDecember2020_thenNextWorkingDay_after3Days() {
        BusinessDay b2019 = BusinessDay.Builder.aBusinessDay()
                .fromStartingDate(LocalDate.of(2019, 12, 18))
                .withBusinessSaturday(2020, 12)
                .build();

        assertEquals( LocalDate.of(2019, 12, 23), b2019.nextBusinessDay(3).asLocalDate() );

        BusinessDay b2020 = BusinessDay.Builder.aBusinessDay()
                .fromStartingDate(LocalDate.of(2020, 12, 23))
                .withBusinessSaturday(2020, 12)
                .build();

        assertEquals( LocalDate.of(2020, 12, 26), b2020.nextBusinessDay(3).asLocalDate() );

    }

    @Test
    public void givenHolidays_businessSundayForDecember2020_thenNextWorkingDay_after3Days() {
        BusinessDay b2019 = BusinessDay.Builder.aBusinessDay()
                .fromStartingDate(LocalDate.of(2019, 12, 18))
                .withBusinessSunday(2020, 12)
                .build();

        assertEquals( LocalDate.of(2019, 12, 23), b2019.nextBusinessDay(3).asLocalDate() );

        BusinessDay b2020 = BusinessDay.Builder.aBusinessDay()
                .fromStartingDate(LocalDate.of(2020, 12, 23))
                .withBusinessSunday(2020, 12)
                .build();

        assertEquals( LocalDate.of(2020, 12, 27), b2020.nextBusinessDay(3).asLocalDate() );

    }

    @Test
    public void givenHolidays_businessSundayForSpecificRange_thenNextWorkingDay_after3Days() {

        TemporalRange range = TemporalRange.Builder.aTemporalRange()
                .from("20/04/2019", "dd/MM/yyyy")
                .to("10/12/2019", "dd/MM/yyyy")
                .build();

        TemporalRange rangeSat = TemporalRange.Builder.aTemporalRange()
                .from("20/04/2019", "dd/MM/yyyy")
                .to("10/11/2019", "dd/MM/yyyy")
                .build();

        BusinessDay b = BusinessDay.Builder.aBusinessDay()
                .fromStartingDate(LocalDate.of(2019, 12, 4))
                .withBusinessSunday(range)
                .withBusinessSaturday(rangeSat)
                .build();

        assertEquals( LocalDate.of(2019, 12, 8), b.nextBusinessDay(3).asLocalDate() );

        BusinessDay b1 = BusinessDay.Builder.aBusinessDay()
                .fromStartingDate(LocalDate.of(2019, 12, 11))
                .withBusinessSunday(range)
                .build();

        assertEquals( LocalDate.of(2019, 12, 16), b1.nextBusinessDay(3).asLocalDate() );

    }

    @Test
    public void givenHolidays_withHolidayOnFridayAndThursday_ForSpecificRange_thenNextWorkingDay_after3Days() {
        TemporalRange range = TemporalRange.Builder.aTemporalRange()
                .from("20/04/2019", "dd/MM/yyyy")
                .to("10/12/2019", "dd/MM/yyyy")
                .build();

        BusinessDay b = BusinessDay.Builder.aBusinessDay()
                .fromStartingDate(LocalDate.of(2019, 12, 18))
                .holidayOnWeekDays(range, DayOfWeek.FRIDAY, DayOfWeek.THURSDAY)
                .build();

        assertEquals( LocalDate.of(2019, 12, 23), b.nextBusinessDay(3).asLocalDate() );

    }

    @Test
    public void givenHolidays_thenNextWorkingDay_ForSpecificRange_after3Days() {
        BusinessDay b = BusinessDay.Builder.aBusinessDay()
                .fromStartingDate(LocalDate.of(2019, 04, 23))
                .givenHolidays(holidays)
                .build();

        assertEquals( LocalDate.of(2019, 04, 27), b.nextBusinessDay(3).asLocalDate() );

    }



    // SHOWING HOLIDAYS

    @Test
    public void givenHolidays_showHolidays() {
        BusinessDay b = BusinessDay.Builder.aBusinessDay()
                .fromStartingDate(LocalDate.of(2019, 12, 18))
                .givenHolidays(holidays)
                .build();

        assertEquals( 3, b.getHolidays().size());

    }

    @Test
    public void givenHolidays_showHolidaysForYear() {
        BusinessDay b = BusinessDay.Builder.aBusinessDay()
                .fromStartingDate(LocalDate.of(2019, 12, 18))
                .givenHolidays(holidays)
                .build();

        assertEquals( 2, b.getHolidays(2019).size());

    }

    @Test
    public void givenHolidays_showHolidaysFromCurrentDate() {
        BusinessDay b = BusinessDay.Builder.aBusinessDay()
                .fromStartingDate(LocalDate.of(2019, 12, 18))
                .givenHolidays(holidays)
                .build();

        assertEquals( 1, b.getHolidaysFromNow().size());

    }

    @Test
    public void givenHolidays_showHolidaysFromCurrentDateFor2020() {
        BusinessDay b = BusinessDay.Builder.aBusinessDay()
                .fromStartingDate(LocalDate.of(2019, 12, 18))
                .givenHolidays(holidays)
                .build();

        assertEquals( 1, b.getHolidaysFromNow(2020).size());

    }

    @Test
    public void givenHolidays_showHolidaysFromStartingDate() {
        BusinessDay b = BusinessDay.Builder.aBusinessDay()
                .fromStartingDate(LocalDate.of(2019, 04, 01))
                .givenHolidays(holidays)
                .build();

        assertEquals( 3, b.getHolidaysFromStartingDate().size());

    }

    @Test
    public void givenHolidays_showHolidaysFromStartingDateFor2019() {
        BusinessDay b = BusinessDay.Builder.aBusinessDay()
                .fromStartingDate(LocalDate.of(2019, 04, 01))
                .givenHolidays(holidays)
                .build();

        assertEquals( 2, b.getHolidaysFromStartingDate(2019).size());

    }

    @Test
    public void givenHolidays_showHolidaysForTemporalRange() {
        BusinessDay b = BusinessDay.Builder.aBusinessDay()
                .fromStartingDate(LocalDate.of(2019, 04, 01))
                .givenHolidays(holidays)
                .build();

        TemporalRange range = TemporalRange.Builder.aTemporalRange()
                .from("20/04/2019", "dd/MM/yyyy")
                .to("30/12/2019", "dd/MM/yyyy")
                .build();

        assertEquals(2, b.getHolidaysForTemporalRange(range).size() );
    }




}