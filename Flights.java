// To make this sample implementation as simple as possible,
// everything goes into the default package.

/**
 * Implements a static factory method for creating flights.
 */

public abstract class Flights {

    /**
     * Static factory method for creating flights.
     *
     * @param name     the name of the flight
     * @param ap1      the name of the airport from which the flight departs
     * @param ap2      the name of the airport at which the flight arrives
     * @param departs  the time of departure (in UTC)
     *                 (UTC times are less than 2400, their two low-order
     *                 digits are less than 60 and indicate minutes, and
     *                 their higher-order digits indicate hours)
     * @param arrives  the time of arrival (in UTC)
     *                 (UTC times are less than 2400, their two low-order
     *                 digits are less than 60 and indicate minutes, and
     *                 their higher-order digits indicate hours)
     * @return         a flight object that encapsulates the given information
     */

    public static Flight makeFlight (String name, String ap1, String ap2,
                                     int departs, int arrives) {

        // Preconditions don't need to be checked as they are here,
        // but checking preconditions that are cheap to check makes
        // programs easier to debug.

        if ((0 <= departs) &&
            (departs <=  2359) &&
            (0 <= arrives) &&
            (arrives <= 2359) &&
            (0 <= (departs % 100)) &&
            ((departs % 100) <= 59) &&
            (0 <= (arrives % 100)) &&
            ((arrives % 100) <= 59)) {

            // Using two different classes to implement flights protects
            // against the common mistake of thinking every flight is
            // an instance of the same class.

            counter = counter + 1;

            if ((counter % 2) == 1)
                return new Flight0 (name, ap1, ap2, departs, arrives);
            else
                return new Flight1 (name, ap1, ap2, departs, arrives);
        }
        else throw new IllegalArgumentException ("bad UTC time");
    }

    private static int counter = 0;  // how many flights have been created

    private static class FlightBase implements Flight {

        private String name;  // name of this flight
        private String ap1;   // name of airport from which this flight departs
        private String ap2;   // name of airport at which this flight arrives
        private int departs;  // time (in UTC) at which this flight departs
        private int arrives;  // time (in UTC) at which this flight arrives

        FlightBase (String name, String ap1, String ap2,
                    int departs, int arrives) {
            this.name = name;
            this.ap1 = ap1;
            this.ap2 = ap2;
            this.departs = departs;
            this.arrives = arrives;
        }

        /**
         * @return         {@inheritDoc}
         */

        public String flightName() { return name; }

        /**
         * @return         {@inheritDoc}
         */

        public String departsFrom() { return ap1; }

        /**
         * @return         {@inheritDoc}
         */

        public String arrivesAt() { return ap2; }

        /**
         * @return         {@inheritDoc}
         */

        public int departureTime() { return departs; }

        /**
         * @return         {@inheritDoc}
         */

        public int arrivalTime() { return arrives; }

        /**
         * @param          a flight to be compared with this flight
         * @return         true if and only if this flight should be considered
         *                 equal to the given flight
         */

        public boolean sameFlight (Flight flight) {
            return this.name.equals (flight.flightName()) &&
                this.ap1.equals (flight.departsFrom()) &&
                this.ap2.equals (flight.arrivesAt()) &&
                (this.departs == (flight.departureTime())) &&
                (this.arrives == (flight.arrivalTime()));
        }

        /**
         * {@inheritdoc}
         */

        public boolean equals (Object x) {
            if (x == null)
                return false;
            else if (! (x instanceof Flight))
                return false;
            else
                return sameFlight ((Flight) x);
        }

        /**
         * {@inheritdoc}
         */

        public String toString() {
            String s = name + ": " + ap1 + " to " + ap2;
            return s + " lv " + departs + " ar " + arrives;
        }

        /**
         * {@inheritdoc}
         */

        public int hashCode() {
            return toString().hashCode();
        }
    }

    private static class Flight0 extends FlightBase {

        Flight0 (String name, String ap1, String ap2,
                 int departs, int arrives) {
            super (name, ap1, ap2, departs, arrives);
        }
    }

    private static class Flight1 extends FlightBase {

        Flight1 (String name, String ap1, String ap2,
                 int departs, int arrives) {
            super (name, ap1, ap2, departs, arrives);
        }
    }
}
