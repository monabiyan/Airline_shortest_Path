// To make this sample implementation as simple as possible,
// everything goes into the default package.

import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;


/**
 * Defines some examples for testing and makes them available as
 * public members of the class.
 */

public class FlightExamples {

    public static List<Flight> panAmFlights = new ArrayList<Flight>();

    public static List<Flight> deltaFlights = initDeltaFlights();

    // Returns a list of flights extracted from schedules published
    // by Delta Airlines.

    private static List<Flight> initDeltaFlights() {
        List<Flight> result = new LinkedList<Flight>();

        // Java interprets a leading 0 to mean octal, so there aren't
        // any leading zeroes here.

        result.add (makeFlight ("Delta 0121", "LGA", "MSP", 1100, 1409));
        result.add (makeFlight ("Delta 2163", "MSP", "PDX", 1500, 1902));
        result.add (makeFlight ("Delta 2079", "BOS", "DTW", 1035, 1259));
        result.add (makeFlight ("Delta 1523", "BOS", "DTW", 2158,   20));
        result.add (makeFlight ("Delta 0058", "BOS", "LHR",   44,  720));
        result.add (makeFlight ("Delta 2531", "BOS", "LAX", 1317, 2020));
        result.add (makeFlight ("Delta 2532", "BOS", "LAX", 2250,  555));
        result.add (makeFlight ("Delta 1959", "BOS", "MSP", 1050, 1417));
        result.add (makeFlight ("Delta 1894", "BOS", "MSP", 1355, 1730));
        result.add (makeFlight ("Delta 2391", "BOS", "MSP", 2135,  105));
        result.add (makeFlight ("Delta 2734", "BOS", "LGA", 1100, 1230));
        result.add (makeFlight ("Delta 3550", "BZN", "LAX", 2020, 2302));
        result.add (makeFlight ("Delta 1601", "DEN", "DTW", 1305, 1611));
        result.add (makeFlight ("Delta 0916", "DEN", "DTW", 2332,  219));
        result.add (makeFlight ("Delta 0010", "DEN", "LHR", 2030,  945));
        result.add (makeFlight ("Delta 5703", "DEN", "LAX", 1404, 1715));
        result.add (makeFlight ("Delta 5743", "DEN", "LAX",   34,  331));
        result.add (makeFlight ("Delta 2437", "DTW", "BOS", 1345, 1546));
        result.add (makeFlight ("Delta 0158", "DTW", "BOS", 1700, 1855));
        result.add (makeFlight ("Delta 1700", "DTW", "BOS", 2240,   42));
        result.add (makeFlight ("Delta 1511", "DTW", "DEN", 1330, 1651));
        result.add (makeFlight ("Delta 1645", "DTW", "DEN", 1711, 2038));
        result.add (makeFlight ("Delta 1706", "DTW", "LAX", 1320, 1845));
        result.add (makeFlight ("Delta 0249", "DTW", "MSP", 1500, 1707));
        result.add (makeFlight ("Delta 2359", "DTW", "MSP", 1715, 1920));
        result.add (makeFlight ("Delta 2476", "DTW", "MSP",  110,  318));
        result.add (makeFlight ("Delta 0059", "LHR", "BOS",  920, 1726));
        result.add (makeFlight ("Delta 4378", "LHR", "BOS", 1645,   20));
        result.add (makeFlight ("Delta 0011", "LHR", "DEN", 1255,  220));
        result.add (makeFlight ("Delta 0302", "LAX", "BOS", 1625, 2214));
        result.add (makeFlight ("Delta 5732", "LAX", "BZN",   30,  318));
        result.add (makeFlight ("Delta 4574", "LAX", "DEN", 1735, 2007));
        result.add (makeFlight ("Delta 5700", "LAX", "DEN",   10,  245));
        result.add (makeFlight ("Delta 2077", "LAX", "PDX", 1735, 2009));
        result.add (makeFlight ("Delta 1728", "MSP", "BOS", 1600, 1851));
        result.add (makeFlight ("Delta 2305", "MSP", "BZN",  221,  513));
        result.add (makeFlight ("Delta 1609", "MSP", "DEN", 2035, 2252));
        result.add (makeFlight ("Delta 1836", "MSP", "DTW", 1224, 1415));
        result.add (makeFlight ("Delta 1734", "MSP", "DTW", 1755, 1941));
        result.add (makeFlight ("Delta 0592", "MSP", "LGA", 1730, 2017));
        result.add (makeFlight ("Delta 2734", "LGA", "BOS", 1100, 1208));
        result.add (makeFlight ("Delta 1294", "LGA", "DEN", 1310, 1754));
        result.add (makeFlight ("Delta 0879", "LGA", "DTW", 1410, 1620));
        result.add (makeFlight ("Delta 1422", "LGA", "MSP", 1500, 1822));
        result.add (makeFlight ("Delta 0950", "PDX", "LAX", 1418, 1655));
        result.add (makeFlight ("Delta 2077", "PDX", "LAX", 2045, 2314));
        result.add (makeFlight ("Delta 2831", "PDX", "LAX", 2346,  225));
        result.add (makeFlight ("Delta 2167", "PDX", "MSP", 2200,  120));

        return result;
    }

    // Help method to make the lines shorter and easier to read.

    private static Flight makeFlight (String name, String ap1, String ap2,
                                      int departs, int arrives) {
        return Flights.makeFlight (name, ap1, ap2, departs, arrives);
    }
}
