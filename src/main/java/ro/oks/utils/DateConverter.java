package ro.oks.utils;

import ro.oks.exceptions.InvalidValueException;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;

public class DateConverter {

    /**
     *
     * @param date au format yyyy-MM-dd
     * @return {@link Instant}
     */
    public static Instant convertDateToInstant(String date) {

        if (date == null) {
            throw new IllegalArgumentException("Invalid date: null");
        }

        // Date au format yyyy-MM-dd
        String[] dateTab = date.split("-");

        if (dateTab.length != 3) {
            throw new InvalidValueException("Invalid date. The format should be yyyy-MM-dd");
        }

        //int year = Integer.parseInt(dateTab[0]);
        int month = Integer.parseInt(dateTab[1]);
        int day = Integer.parseInt(dateTab[2]);

        if (month > 12 || day > 31) {
            throw new InvalidValueException("Invalid date. The format should be yyyy-MM-dd and check that the number of days and months is correct");
        }

        try {
            LocalDate localDate = LocalDate.parse(date);
            // Fuseau horaire (par exemple, UTC)
            ZoneId zoneId = ZoneId.of("UTC");

            // Conversion en Instant
            return localDate.atStartOfDay(zoneId).toInstant();
        }catch (Exception e) {
            throw new InvalidValueException("Invalid date. The format should be yyyy-MM-dd. " + e.getMessage());
        }
    }

    public static Instant instantStringToInstant(String instantString){
        try {
            return Instant.parse(instantString);
        }catch (Exception e){
            throw new InvalidValueException("Invalid date format. The format should be for exemple: 2007-12-03T10:15:30.00Z");
        }
    }
}
