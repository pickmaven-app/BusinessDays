package app.pickmaven.businessdays;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * This class implements {@code app.pickmaven.businessdays.HolidaySearcher} interface for getting a list of {@code LocalDate} objects representing
 * the holidays for specific countries using the app.pickmaven.businessdays.PublicHolidayAPI of RapidAPI.
 * <p>
 *
 * This class is immutable and thread-safe.
 *
 */
public class PublicHolidayAPI implements HolidaySearcher {
    /**
     * Base endpoint for calling the Rapid Api of Public app.pickmaven.businessdays.Holiday.
     */
    public static final String PUBLIC_HOLIDAY_P_RAPIDAPI_COM = "public-holiday.p.rapidapi.com";

    /**
     * Full endpoint for calling the Rapid Api of Public app.pickmaven.businessdays.Holiday.
     */
    private String endpoint = "https://public-holiday.p.rapidapi.com";

    /**
     * The personal api key for calling the Public app.pickmaven.businessdays.Holiday Api.
     */
    private String api_key = null;

    /**
     * Year to apply the call for getting holidays. Default current year.
     */
    private int year = LocalDate.now().getYear();

    /**
     * Predicate to apply in order to filter the holidays given as response by the api.
     */
    private Predicate<? super JsonObject> predicate = (p) -> true;

    /**
     * Constructor
     *
     * @param api_key personal api-key, not null
     */
    public PublicHolidayAPI(String api_key) {
        this.api_key = api_key;
    }

    /**
     * Call this method for apply a {@code Predicate<? super JsonObject>} in order to filter the holidays
     *
     * @param predicate for filtering holidays from the response
     * @return this
     */
    public PublicHolidayAPI applyPredicate(Predicate<? super JsonObject> predicate) {
        this.predicate = predicate;
        return this;
    }

    /**
     * Returns a list of {@code LocalDate} representing the holidays for a specific country.
     * This method assumes you are searching holidays for current year.
     *
     * @param countryCode the code of country you are searching for
     * @return a list of holiday dates
     * @throws IOException If connection to the api failed
     */
    @Override
    public List<LocalDate> searchHolidaysFor(String countryCode) throws IOException {
        List<JsonObject> jsonObjects = callApi(countryCode);

        return jsonObjects.stream()
                .filter(predicate)
                .map(PublicHolidayAPI::getDate)
                .collect(Collectors.toList());
    }

    /**
     * Returns a list of {@code LocalDate} representing the holidays for a specific country and for specific year.
     *
     * @param countryCode
     * @param year for which you are searching holidays
     * @return a list of holiday dates
     * @throws IOException If connection to the api failed
     */
    public List<LocalDate> searchHolidaysFor(String countryCode, int year) throws IOException {
        this.year = year;
        List<JsonObject> jsonObjects = callApi(countryCode);

        return jsonObjects.stream()
                .filter(predicate)
                .map(PublicHolidayAPI::getDate)
                .collect(Collectors.toList());
    }

    /**
     * Gets the date from the 'date' field inside the {@code JsonObject} given as response.
     *
     * @param element {@code JsonObject} to search through
     * @return {@code LocalDate} of holiday from the JsonObject
     */
    private static LocalDate getDate(JsonObject element) {
        JsonElement date = element.get("date");
        String dateAsString = date.getAsString();
        return LocalDate.parse(dateAsString, DateTimeFormatter.ISO_DATE);
    }

    /**
     * Returns a list of json objects from the call to api.
     *
     * @param countryCode the code of country you are seacrhing for
     * @return a list of {@code JsonObject}
     * @throws IOException
     */
    private List<JsonObject> callApi(String countryCode) throws IOException {
        String uri = endpoint + "/" + year + "/" + countryCode;
        List<JsonObject> jsonObjects = new ArrayList<>();

        HttpGet request = new HttpGet(uri);
        request.setHeader("X-RapidAPI-Host", PUBLIC_HOLIDAY_P_RAPIDAPI_COM);
        request.setHeader("X-RapidAPI-Key", api_key);

        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse response = httpClient.execute(request);

            HttpEntity entity = response.getEntity();
            if (entity != null) {
                String result = EntityUtils.toString(entity);
                JsonElement el = new JsonParser().parse(result);

                if (el.isJsonObject()) {
                    jsonObjects.add(el.getAsJsonObject());
                }

                if (el.isJsonArray()) {
                    JsonArray jsonArray = el.getAsJsonArray();

                    for (JsonElement element: jsonArray) {
                        jsonObjects.add(element.getAsJsonObject());
                    }
                }
            }

        return jsonObjects;
    }
}
