package app.pickmaven.businessdays.api;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.time.LocalDate;
import java.util.List;

/**
 * This functional interface provides a single method for searching holidays based on country.
 *
 */
@FunctionalInterface
public interface HolidaySearcher {
    List<LocalDate> searchHolidaysFor(String countryCode) throws IOException, GeneralSecurityException;
}
