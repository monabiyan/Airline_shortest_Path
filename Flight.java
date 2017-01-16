// To make this sample implementation as simple as possible,
// everything goes into the default package.

/**
 * Represents schedule information for (one leg of) an airplane flight.
 */

public interface Flight {

    /**
     * @return         the name of the Flight
     */

    public String flightName();

    /**
     * @return         the name of the airport from which the flight departs
     */

    public String departsFrom();

    /**
     * @return         the name of the airport at which the flight arrives
     */

    public String arrivesAt();

    /**
     * @return         the time (in UTC) at which the flight departs
     *                 (UTC times are less than 2400, their two low-order
     *                 digits are less than 60 and indicate minutes, and
     *                 their higher-order digits indicate hours)
     */

    public int departureTime();

    /**
     * @return         the time (in UTC, see below) at which the flight arrives
     *                 (UTC times are less than 2400, their two low-order
     *                 digits are less than 60 and indicate minutes, and
     *                 their higher-order digits indicate hours)
     */

    public int arrivalTime();

    /**
     * @param  flight  a flight to be compared with this flight
     * @return         true if and only if this flight should be considered
     *                 equal to the given flight
     */

    public boolean sameFlight(Flight flight);

}


// The following examples are written as though makeFlight is a static
// factory method of the <code>Flights</code> class.
//
// Flights.makeFlight("United 448", "BOS", "DEN", 2003, 0053).flightName()
// =>  "United 448"
//
// Flights.makeFlight("United 448", "BOS", "DEN", 2003, 0053).departsFrom()
// =>  "BOS"
//
// Flights.makeFlight("United 448", "BOS", "DEN", 2003, 0053).arrivesAt()
// =>  "DEN"
//
// Flights.makeFlight("United 448", "BOS", "DEN", 2003, 0053).departureTime()
// =>  2003
//
// Flights.makeFlight("United 448", "BOS", "DEN", 2003, 0053).arrivalTime()
// =>  0053
//
// Flights.makeFlight("United 448", "BOS", "DEN", 2003, 0053)
//     .sameFlight(Flights.makeFlight("United 448", "BOS", "DEN", 2003, 0053))
// =>  true
//
// Flights.makeFlight("United 448", "BOS", "DEN", 2000, 0055)
//     .sameFlight(Flights.makeFlight("United 448", "BOS", "DEN", 2003, 0053))
// =>  false
