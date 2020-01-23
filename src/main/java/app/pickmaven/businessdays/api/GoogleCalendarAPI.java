package app.pickmaven.businessdays.api;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.Events;

import java.io.*;
import java.security.GeneralSecurityException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * This class implements {@code app.pickmaven.businessdays.api.HolidaySearcher} interface for getting a list of {@code LocalDate} objects representing
 * the holidays for specific countries using the app.pickmaven.businessdays.api.GoogleCalendarAPI of Google.
 * <p>
 *
 * This class is immutable and thread-safe.
 *
 *
 */
public class GoogleCalendarAPI implements HolidaySearcher {
    /**
     * The application name.
     */
    private static final String APPLICATION_NAME = "Google Calendar API";

    /**
     * Default instance of {@code JsonFactory}.
     */
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

    /**
     * The token path.
     */
    private static String token_path = null;

    /**
     * {@code Predicate<? super Event>} to apply; defauly true.
     */
    private Predicate<? super Event> predicate = (p) -> true;

    /**
     * By default logging response is false.
     */
    private boolean logResponse;


    /**
     * Constructor.
     * <p>
     * In order to create the json file with the Google Calendar Api service token you have to create
     * credentials from Google Developer Console (https://console.developers.google.com). Just have to create a project,
     * click on Create Credentials and select the Account Service Key type; finally
     * select the service and the json type and you will get the json file downloaded.
     *
     * @param token_path the token path of json file
     */
    public GoogleCalendarAPI(String token_path) {
        this.token_path = token_path;
    }

    /**
     * Apply the {@code Predicate<? super Event} in order to filter the holidays.
     *
     *  <p>
     *      In order to know the fields that Api will give you in response you must call the activateLogResponse method,
     *      this way you will see the system.out outputs of the response.
     * </p>
     * @param predicate to apply in order to filter the holidays given as response from the Google Calendar Api
     * @return this
     */
    public GoogleCalendarAPI applyPredicate(Predicate<? super Event> predicate) {
        this.predicate = predicate;
        return this;
    }

    /**
     * Creates an authorized Credential object.
     *
     * @return An authorized Credential object
     * @throws IOException If the json file at the token_path cannot be found
     */
    private static Credential getCredentials() throws IOException {
        return GoogleCredential.fromStream(new FileInputStream(token_path))
                .createScoped(Collections.singleton(CalendarScopes.CALENDAR_READONLY));
    }

    /**
     * Returns a list of {@code LocalDate} objects representing the holidays for a specific country.
     * <p>
     *     You can find a list of county code in the format 'language.country' under
     *     <a href="file:../resources/GoogleCalendarAPI_CountryCodeList.txt">/resources/GoogleCalendarAPI_CountryCodeList.txt</a>.
     *     You can change the language part as you prefer. By default is english.
     * </p>
     * @param countryCode for which return holidays
     * @return list of holiday dates by country code
     */
    @Override
    public List<LocalDate> searchHolidaysFor(String countryCode) throws IOException, GeneralSecurityException {

            List<DateTime> holidays = getCalendarHolidays(countryCode);
            return holidays.stream()
                    .map(DateTime::toString)
                    .map(e -> LocalDate.parse(e, DateTimeFormatter.ISO_DATE))
                    .collect(Collectors.toList());
    }

    /**
     * Returns a list of DateTime objects representing the holidays for a specific country.
     * This method get an Event list from the Google Calendar API and filter it in order to return a list
     * of events distinct by date key.
     * Then applies the client Predicate for further filtering.
     *
     * @param countryCode
     * @return a list of holidays
     * @throws IOException
     * @throws GeneralSecurityException
     */
    private List<DateTime> getCalendarHolidays(String countryCode) throws IOException, GeneralSecurityException {
        // Build a new authorized API client service.
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        Calendar service = new Calendar.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials())
                .setApplicationName(APPLICATION_NAME)
                .build();

        List<DateTime> holidays = null;
        String pageToken = null;
        do {
            Events events = service.events().list(countryCode + "#holiday@group.v.calendar.google.com").setPageToken(pageToken).execute();
            List<Event> distinctEvents = events.getItems().stream()
                    .filter( distinctByKey(p -> p.getStart().getDate() ) )
                    .collect(Collectors.toList());

            if (logResponse) {
                System.out.println(distinctEvents);
            }

            holidays = distinctEvents.stream()
                    .filter(predicate)
                    .map(p -> p.getStart().getDate()).distinct()
                    .filter(e -> e.toString().contains(String.valueOf(LocalDate.now().getYear())))
                    .collect(Collectors.toList());
            pageToken = events.getNextPageToken();
        } while (pageToken != null);

        return holidays;
    }

    // Returns a Predicate for getting distinct results by specific key.
    private static <T> Predicate<T> distinctByKey(
            Function<? super T, ?> keyExtractor) {

        Map<Object, Boolean> seen = new ConcurrentHashMap<>();
        return t -> seen.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
    }

    /**
     * Sets the response logging to true.
     */
    public void activateLogResponse() {
        this.logResponse = true;
    }
}
