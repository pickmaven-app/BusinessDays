package app.pickmaven.businessdays;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

/**
 * The base unit to refer to a temporal range between two {@code LocalDate} objects.
 * <p>
 *
 * @implSpec
 * This class is immutable and thread-safe
 *
 * @author Daniele Gubbiotti
 *
 */
public class TemporalRange {

    /**
     * The starting date of {@code app.pickmaven.businessdays.TemporalRange}
     */
    private LocalDate startingDate;

    /**
     * The ending date of {@code app.pickmaven.businessdays.TemporalRange}
     */
    private LocalDate endingDate;

    //-----------------------------------------------------------------------

    /**
     * Private constructor for creating app.pickmaven.businessdays.TemporalRange from the Builder.
     */
    private TemporalRange() { }

    //-----------------------------------------------------------------------

    /**
     * Gets the starting date of {@code app.pickmaven.businessdays.TemporalRange}
     * @return
     */
    public LocalDate getStartingDate() {
        return startingDate;
    }

    /**
     * Sets the starting date of {@code app.pickmaven.businessdays.TemporalRange}
     * @param startingDate
     */
    private void setStartingDate(LocalDate startingDate) {
        this.startingDate = startingDate;
    }

    /**
     * Gets the ending date of {@code app.pickmaven.businessdays.TemporalRange}
     * @return
     */
    public LocalDate getEndingDate() {
        return endingDate;
    }

    /**
     * Sets the ending date of {@code app.pickmaven.businessdays.TemporalRange}
     * @param endingDate
     */
    private void setEndingDate(LocalDate endingDate) {
        this.endingDate = endingDate;
    }

    //-----------------------------------------------------------------------

    /**
     * Checks if this temporal range includes the parameter range.
     *
     * @param range must not be null
     * @return true if this temporal range includes the parameter range
     */
    public boolean includes(TemporalRange range) {
        assert range != null : "range must not be null";

        return this.startingDate.isBefore(range.getStartingDate()) &&
                this.endingDate.isAfter(range.getEndingDate());
    }

    /**
     * Checks if this temporal range includes the parameter date.
     *
     * @param date must not be null
     * @return true if this temporal range includes the date
     */
    public boolean includes(LocalDate date) {
        assert date != null : "date must not be null";

        return date.isAfter(this.startingDate) &&
                date.isBefore(this.getEndingDate());
    }

    //-----------------------------------------------------------------------

    /**
     * Computes the hashcode
     *
     * @return
     */
    @Override
    public int hashCode() {
        return Objects.hash(getStartingDate(), getEndingDate());
    }

    /**
     * Checks if two {@code app.pickmaven.businessdays.TemporalRange} objects are equal. It compares the value of startingDate and endingDate.
     *
     * @param o
     * @return true if the two objects are equal
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TemporalRange that = (TemporalRange) o;
        return getStartingDate().equals(that.getStartingDate()) &&
                getEndingDate().equals(that.getEndingDate());
    }


    /**
     * Outputs the temporal range from starting date to ending one.
     *
     * @return
     */
    @Override
    public String toString() {
        return "app.pickmaven.businessdays.TemporalRange{" +
                "startingDate=" + startingDate +
                ", endingDate=" + endingDate +
                '}';
    }

    //-----------------------------------------------------------------------
    // STATIC BUILDER

    /**
     * Public builder for creating instances of {@code app.pickmaven.businessdays.TemporalRange} objects.
     */
    public static class Builder {
        /**
         * {@code app.pickmaven.businessdays.TemporalRange} object.
         */
        private TemporalRange tRange;

        /**
         * Constructor. It initializes the {@code app.pickmaven.businessdays.TemporalRange} field.
         */
        public Builder() {
            tRange = new TemporalRange();
        }

        /**
         * Creates an instance of the {@code Builder}
         * @return this
         */
        public static Builder aTemporalRange() {
            return new Builder();
        }

        /**
         * Sets the starting date of {@code app.pickmaven.businessdays.TemporalRange} from {@code LocalDate} object.
         *
         * @param date must not be null
         * @return this
         */
        public Builder from(LocalDate date) {
            assert date != null : "date must not be null";

            tRange.setStartingDate(date);
            return this;
        }

        /**
         * Sets the starting date of {@code app.pickmaven.businessdays.TemporalRange} from a {@code String} date and a {@code String} pattern.
         *
         * @param date must not be null
         * @param pattern formatter pattern; pattern must not be null
         * @return this
         */
        public Builder from(String date, String pattern) {
            assert date != null : "date must not be null";
            assert pattern != null : "pattern must not be null";

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
            tRange.setStartingDate(LocalDate.parse(date, formatter));
            return this;
        }

        /**
         * Sets the ending date of {@code app.pickmaven.businessdays.TemporalRange} from {@code LocalDate} object.
         *
         * @param date must not be null
         * @return this
         */
        public Builder to(LocalDate date) {
            tRange.setEndingDate(date);
            return this;
        }

        /**
         * Sets the ending date of {@code app.pickmaven.businessdays.TemporalRange} from a {@code String} date and a {@code String} pattern.
         *
         * @param date must not be null
         * @param pattern formatter pattern; pattern must not be null
         * @return this
         */
        public Builder to(String date, String pattern) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
            tRange.setEndingDate(LocalDate.parse(date, formatter));
            return this;
        }

        /**
         * It returns the instance of {@code app.pickmaven.businessdays.TemporalRange} object.
         *
         * @return temporal range object
         */
        public TemporalRange build() {
            assert tRange.startingDate != null : "Starting date must not be null";
            assert tRange.endingDate != null : "Ending date must not be null";

            return tRange;
        }
    }

}
