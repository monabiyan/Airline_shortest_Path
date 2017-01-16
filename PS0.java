// You can import more interface and class types here.

import java.lang.reflect.Array;
import java.util.*;


public  class PS0 {
    public static final int bigValue= 999999;
    public static final int verybigValue= Integer.MAX_VALUE;

    // canGetThere : String String ListOfFlight -> Boolean
    // GIVEN: the names of two airports, ap1 and ap2 (respectively),
    //     and a ListOfFlight that describes all of the flights a
    //     traveller is willing to consider taking
    // RETURNS: true if and only if it is possible to fly from the
    //     first airport (ap1) to the second airport (ap2) using
    //     only the given flights
    // EXAMPLES:
    //     canGetThere ( "06N", "JFK", panAmFlights )  =>  false
    //     canGetThere ( "LGA", "PDX", deltaFlights )  =>  true
    //
    // (For the definitions of panAmFlights and deltaFlights assumed
    // by those examples, see the FlightExamples class.)

    public static boolean canGetThere (String ap1, String ap2, List<Flight> flights)
    {

        List<Flight> bestPath=ShortestFlightPath(flights,ap1,ap2);
        if (bestPath.size()==0){
            return (Boolean.FALSE);
        }
        else{
            return (Boolean.TRUE);
        }
    }

    // fastestItinerary : String String ListOfFlight -> ListOfFlight
    // GIVEN: the names of two airports, ap1 and ap2 (respectively),
    //     and a ListOfFlight that describes all of the flights a
    //     traveller is willing to consider taking
    // WHERE: it is possible to fly from the first airport (ap1) to
    //     the second airport (ap2) using only the given flights
    // RETURNS: a list of flights that tells how to fly from the
    //     first airport (ap1) to the second airport (ap2) in the
    //     least possible time, using only the given flights
    // EXAMPLES:
    //     fastestItinerary ("LGA", "PDX", deltaFlights)
    // =>  [ makeFlight ("Delta 0121", "LGA", "MSP", 1100, 1409),
    //       makeFlight ("Delta 2163", "MSP", "PDX", 1500, 1902) ]

    public static List<Flight> fastestItinerary (String ap1, String ap2,
                                                 List<Flight> flights)
    {
        List<Flight> bestPath=ShortestFlightPath(flights,ap1,ap2);
        return(bestPath);
    }

    // travelTime : String String ListOfFlight -> PosInt
    // GIVEN: the names of two airports, ap1 and ap2 (respectively),
    //     and a ListOfFlight that describes all of the flights a
    //     traveller is willing to consider taking
    // WHERE: it is possible to fly from the first airport (ap1) to
    //     the second airport (ap2) using only the given flights
    // RETURNS: the number of minutes it takes to fly from the first
    //     airport (ap1) to the second airport (ap2), including any
    //     layovers, by the fastest possible route that uses only
    //     the given flights
    // EXAMPLES:
    //     travelTime ( "LGA", "PDX", deltaFlights )  =>  482

    public static int travelTime (String ap1, String ap2, List<Flight> flights)
    {
        List<Flight> bestPath=ShortestFlightPath(flights,ap1,ap2);
        int ll1=bestPath.size();
        int end_minute=clockFormat(Integer.toString(bestPath.get(ll1-1).arrivalTime()));
        int start_minute=clockFormat(Integer.toString(bestPath.get(0).departureTime()));
        return(end_minute-start_minute);
    }




    // ShortestFlightPath :  ListOfFlight String String -> ListOfFlight
    // GIVEN: a ListOfFlight that describes all of the flights a
    //     traveller is willing to consider taking and the names of two airports, source and target (respectively),
    //
    // RETURNS: a list of flights that tells how to fly from the
    //     first airport (source) to the second airport (target) in the
    //     least possible time, using only the given flights
    // EXAMPLES:
    //     ShortestFlightPath ("LGA", "PDX", deltaFlights)
    // =>  [ makeFlight ("Delta 0121", "LGA", "MSP", 1100, 1409),
    //       makeFlight ("Delta 2163", "MSP", "PDX", 1500, 1902) ]

    private static List<Flight> ShortestFlightPath(List<Flight> flights,String source,String target )
    {
        // Extracting data from the flight list into 5 arrays.
        // So the corresponding instances have the same index.
        String [] flightNos= Extract_FlightNos(flights);
        String [] startNames=Extract_startNames(flights);
        String [] finishNames=Extract_finishNames(flights);
        int [] startTimes= Extract_startTimes(flights);
        int [] finishTimes= Extract_FinishTimes(flights);

        HashMap<String,Integer> dist =new HashMap<>();  // 'dist' keep the distance information for each node from the source node
        HashMap<String,String> prev =new HashMap<>();   // 'prev' keeps the information for one edge, Ex: (u,v) in G , then prev(v) is u.
        HashMap<String,Boolean> Q =new HashMap<>(); // keeps the name of unvisited nodes.

        // Extract all unique Airports in the list to Vortices.
        HashMap<String,Boolean> Vortices=ListAllUniqueNodes(startNames,finishNames);


        // Initialization of dist,prev and Q
        for (String vortex:Vortices.keySet())
        {
            dist.put(vortex,bigValue); // put the distances to infinity
            prev.put(vortex,null);  // No prev link yet
            Q.put(vortex,Boolean.TRUE);  // put all unique airports in the Q
        }

        //Find the source with the lowest departure time time
        // This step is not necessary but we implement it any way to choose one source out of possible some sources.
        int sourceTime=findMinimumdepartureTime(source,  startTimes,startNames);
        dist.put(source,sourceTime);

        while(Q.keySet().size()!=0)
        {
            int minDist=findMinimumDistanceinQue(Q,dist); // Find Minimum distance in the que
            String u= findKeyGivenValue(dist,minDist); // Find the Airport name corresponding to the minDist
            Q.remove(u);
            if (u.equals(target)) {  break; }  //We reached the destination and exit the loop.
            HashMap<String,Integer> neighbors= findValidNeighbours(flights,u,minDist,Q); //Find neighbors that are valid for the node
            prev = updateLinksForNeighbours(u,neighbors,dist,prev);    // Update Links
            dist = updateDistancesForNeighbours(neighbors,dist);       // Update distances
        }

        Stack lifo = new Stack();
        String u=target;

        while (prev.get(u)!=null)
        {
            lifo.push(u);  // Populating the stack
            u=prev.get(u);  // Updating u
        }
        lifo.push(u);   // Push the last u
        int stack_size=lifo.size();


        String [] Cities=pushStacktoArray (lifo);


        List<Flight> final_Flight_list=ExtractFlightsFromFinalList(Cities,dist,flights);

        return(final_Flight_list);

    }

    // clockFormat :   String -> int
    // GIVEN: 'time' which is a String representation of time,
    //
    // RETURNS: equivalent minutes from time 00:00 in integer format.
    // EXAMPLES:
    //     clockFormat("1409")
    // =>  849

    private static int clockFormat(String time)
    {
        int ll=time.length();
        int a=0;
        int b=0;
        int c=0;
        int d=0;
        if (ll==4){
             a=Character.getNumericValue(time.charAt(0));
             b=Character.getNumericValue(time.charAt(1));
             c=Character.getNumericValue(time.charAt(2));
             d=Character.getNumericValue(time.charAt(3));
        }
        else if (ll==3){

            b=Character.getNumericValue(time.charAt(0));
            c=Character.getNumericValue(time.charAt(1));
            d=Character.getNumericValue(time.charAt(2));
        }
        else if (ll==2){
            c=Character.getNumericValue(time.charAt(0));
            d=Character.getNumericValue(time.charAt(1));
        }
        else if (ll==1){
            d=Character.getNumericValue(time.charAt(0));
        }

        else {
            System.out.print("Time not Correct");
        }


        int minutes=d+c*10+b*60+a*600;
        String minStr= Integer.toString(minutes);

        return(Integer.parseInt(minStr));
    }

    // clockFormat :   String -> int
    // GIVEN: 'timeStr' which is an equivalent String representation of time in minutes from the time 00:00 (example : "71" which shows 01:11)
    //
    // RETURNS: equivalent time in integer format (leading zeros eliminated since it is integer)
    // EXAMPLES:
    //     clockFormat("71")
    // =>  111

    private static int clockFormatC (String timeStr)
    {
        int time=Integer.parseInt(timeStr);
        String minute=Integer.toString(time%60);
        String hour=Integer.toString(time/60);

        if (minute.equals("0")){
            minute="00";
        }
        if (!(hour.equals("0"))&&(minute.length()==1)){
            minute="0"+minute;
         }
        if (hour.equals("0")){
            return (Integer.parseInt(minute));
        }
        else {
            return (Integer.parseInt(hour+minute));
        }

    }

    // ListAllUniqueNodes :   String [] String [] -> ListOfFlight
    // GIVEN: 2 List of the names of airports for departure and arrivals (starName,finishName) respectively
    // RETURNS: a HashMap with the key of airport names that exists in startName and finishName and with the value True
    // COMMENT: HashMap is used for great efficiency.
    // EXAMPLES:
    //     ListAllUniqueNodes(["BOS","MSP"],["PDX","BOS"])
    // =>  [ "BOS","MSP","PDX" ]
    private static HashMap<String,Boolean> ListAllUniqueNodes(String [] startName,String [] finishName)
    {
        HashMap<String,Boolean> map =new HashMap<>();
        int l1=startName.length;
        int l2=finishName.length;

        for (int i=0;i<l1;i++){
            map.put(startName[i],Boolean.TRUE);
        }

        for (int i=0;i<l2;i++){
            map.put(finishName[i],Boolean.TRUE);
        }
        return(map);
    }



    // ListAllUniqueNodes :   ListOfFlight  String  int HashMap -> HashMap
    // GIVEN: a ListOfFlight that describes all of the flights a
    //     traveller is willing to consider taking, the airport name (node), the arrival time (minute) in minute for that airport
    // and the HashMap 'qq' containing the names of the some airports as keys and all TRUE as values
    // RETURNS: a HashMap with the key of airport names that there is a possible flight from the input 'node' airport to them
    // with the values as integer which represents those airports arrival time in those mentioned flights. The times will
    // be in minute integer from 00:00.
    // COMMENT: HashMap is used for its great efficiency.
    // EXAMPLES:
    //     findValidNeighbours(ListOfFlights,"BOS",459,{"PDX" :TRUE,"MSP":TRUE})
    // =>  {"MSP" : 517}

    private static HashMap<String,Integer> findValidNeighbours(List<Flight> flights,String node,int minute,HashMap<String,Boolean> qq )
    {
        String [] flightNo= Extract_FlightNos(flights);
        String [] startName=Extract_startNames(flights);
        String [] finishName=Extract_finishNames(flights);
        int [] startTime= Extract_startTimes(flights);
        int [] finishTime= Extract_FinishTimes(flights);
        HashMap<String, Integer> map = new HashMap<>();
        int ll = flightNo.length;
        for (int i = 0; i < ll; i++)
        {
            if (startName[i].equals(node))
            {
                if (minute > startTime[i])
                {
                    continue;
                }
                if (qq.containsKey((finishName[i])))
                {
                    int cost = (finishTime[i]);
                    if (map.containsKey(finishName[i]))
                    {
                        if (map.get(finishName[i]) > finishTime[i])
                        {
                            map.put(finishName[i], cost);
                        }
                    }
                    else
                    {
                        map.put(finishName[i], cost);
                    }
                }
            }
        }
        return (map);
    }



    // updateLinksForNeighbours : String, HashMap<String,Integer> HashMap<String,Integer> HashMap<String,String> -> HashMap<String,String>
    // GIVEN: a String name u, a Hashmap neighbors, Hashmap of dist and Hashmap Prev


    //RETURNS: Updates the values in dist hashmap if a key in in neighbors hashmap has a value that this value
    // is smaller that comparing to the value of the same key in the dist. As a result we update all the values in dist
    // and return the updated dist
    // EXAMPLES:
    //    updateDistancesForNeighbours({"A":10,"B":20}{"A":12,"B":15,"C":500})
    // =>  {"A":10,"B":15,"C":500}
    private static HashMap<String,String> updateLinksForNeighbours(String u,HashMap<String,Integer> neighbors,HashMap<String,Integer> dist,HashMap<String,String> prev)
    {
        for (Map.Entry<String, Integer> neighbor : neighbors.entrySet())
        {
            String v=neighbor.getKey();
            int time=neighbor.getValue();
            if (time<dist.get(v))
            {
                prev.put(v,u);
            }
        }
        return prev;
    }


    // pushStacktoArray :  Stack  -> String []
    // GIVEN: a stack of string values

    // RETURNS: Converts the stack into an array of string. The first one that is
    // pooped out from the stack will become the first element in the array
    //
    // EXAMPLES:
    //    pushStacktoArray(Stack["M","D","B"])
    // =>  ["B","D","M"]

    private static String[] pushStacktoArray (Stack lifo)
    {
        int a=lifo.size();

        String[] Cities=new String[a];

        for (int i=0;i<a;i++)
        {
            Cities[i]=lifo.pop().toString();
        }
        return Cities;
    }





    // findMinimumdepartureTime :  String int[] String[]  -> int
    // GIVEN: a String name as source, a list of integer values as startTime and a list of String values as startName

    // RETURNS: returns a value which is in the  startTime list such that its corresponding  startName with the same
    // index is equal to source, also this output is the minumum possible output that can be extracted given that
    // condition
    //
    // EXAMPLES:
    //     findMinimumdepartureTime("B",[17,19,100,8,5],["B","A","C","B","M"])
    // =>  8


    private static int findMinimumdepartureTime(String source, int [] startTime,String [] startName)
    {
        int ll = startName.length;
        int sourceTime = verybigValue;
        for (int i = 0; i < ll; i++) {
            if ((startName[i].equals(source)) && ((sourceTime > startTime[i]))) {
                sourceTime = startTime[i];
            }
        }
        return (sourceTime);
    }


    // findMinimumDistanceinQue :  HashMap<String,Boolean> HashMap<String,Integer>  -> int
    // GIVEN: a Hashmap Q with keys containing all the String names and given a Hashmap dist which
    // contains a series of names as key and corresponding integer values as value

    // RETURNS: the minimum value in the dist Hashmap that its corresponding key exists in the hashmap of Q
    //
    // EXAMPLES:
    //     findMinimumDistanceinQue({"A":True,"C":True},{"C":17,"D":100,"E":5})
    // =>  "C"
    private static int findMinimumDistanceinQue(HashMap<String,Boolean> Q,HashMap<String,Integer> dist)
    {
        int minDist=verybigValue;
        for (String node :Q.keySet()){
            if (minDist>dist.get(node)){
                minDist=dist.get(node);
            }
        }
        return minDist;
    }


    // findExactFlight :   ListOfFlight  String  String int int  -> String
    // GIVEN: a ListOfFlight that describes all of the flights a
    // traveller is willing to consider taking,the departure airport 'begin',the arrival airport 'destination'
    //  and arrivaltime in integer minute format as well as  mindeparturetime in integer minute format
    // RETURNS: an array of string which contains the full information of the flight, that its information was
    // partly given as input
    //
    // EXAMPLES:
    //     findExactFlight(ListOfFlights,"BOS","MSP",1050,987)
    // =>  {"BOS",1035,"MSP",1050,"Delta 1894"}
    private static String [] findExactFlight(List<Flight> flights,String begin,String destination,int arrivalTime,int minDepartureTime)
    {

        String [] flightNo= Extract_FlightNos(flights);
        String [] startName=Extract_startNames(flights);
        String [] finishName=Extract_finishNames(flights);
        int [] startTime= Extract_startTimes(flights);
        int [] finishTime= Extract_FinishTimes(flights);
        int m=finishName.length;

        String [] M=new String[5];
        for(int i=0;i<m;i++){
            if((finishName[i].equals(destination))&&(startName[i].equals(begin))&&(finishTime[i]==arrivalTime)){
                int departureTime=startTime[i];
                M[0]=begin;
                M[1]=Integer.toString(departureTime);
                M[2]=destination;
                M[3]=Integer.toString(arrivalTime);
                M[4]=flightNo[i];
                break;
            }
        }
        return(M);
    }

    // findKeyGivenValue :   HashMap, int   -> String
    // GIVEN: a HashMap 'map' with keys as string and values as integer, and an integer value 'v'
    // RETURNS: the corresponding key of the value 'v'
    // EXAMPLES:
    //     findKeyGivenValue({"A":2,"B":5,"C":17,"D":20},17)
    // =>  "C"

    private static String findKeyGivenValue(HashMap<String,Integer> map,int v)
    {
        String s="NNN";
        for (Map.Entry<String, Integer> entry : map.entrySet())
        {
            if (entry.getValue()==v)

            {
                s=entry.getKey();
                break;
            }
        }
        return (s);
    }


    // Extract_FlightNos :   ListOfFlight   -> String[]
    // GIVEN: a ListOfFlight that describes all of the flights a
    //     traveller is willing to consider taking,
    // RETURNS: an String list of all the flights names listed in the ListOfFlight
    // in the same order that was provided in the ListOfFlight
    // EXAMPLES:
    //     Extract_FlightNos(ListOfFlights)
    // =>  ["Delta 2734","Delta 2835","Delta 2930","Delta 2124",...]


    private static String [] Extract_FlightNos(List<Flight> flights)
    {
        int fsize=flights.size();
        String [] flightNo= new String[fsize];
        for (int i=0;i<fsize;i++) {
            flightNo[i] = flights.get(i).flightName();
        }
        return(flightNo);
    }


    // Extract_startNames :   ListOfFlight  -> String[]
    // GIVEN: a ListOfFlight that describes all of the flights a
    //     traveller is willing to consider taking,
    // RETURNS: an String list of all the departure airports listed in the ListOfFlight
    // in the same order that was provided in the ListOfFlight
    // EXAMPLES:
    //     Extract_startNames(ListOfFlights)
    // =>  ["MSP","LGA","BOS","PDX",...]

    private static String [] Extract_startNames(List<Flight> flights)
    {
        int fsize=flights.size();
        String [] startName= new String[fsize];
        for (int i=0;i<fsize;i++) {
            startName[i] = flights.get(i).departsFrom();
        }
        return(startName);
    }
    // ExtractFlightsFromFinalList :  String[], HashMap<String,Integer> , ListOfFlights ->ListOfFlights
    // GIVEN: a list of airports, the Hashmap of airports with their corresponding distances as values and
    // a ListOfFlight that describes all of the flights a
    //     traveller is willing to consider taking

    // RETURNS: the corresponding flights, in the order of cities that are povided and getting help from the distance Hashmap
    //
    // EXAMPLES:
    //     ExtractFlightsFromFinalList(["BOS","MSP"],{"MSP":234:"BOS":1230},ListOFFlight)
    // =>  [ makeFlight ("Delta 0121", "BOS", "MSP", 1100, 1409)]
    private static List<Flight> ExtractFlightsFromFinalList(String [] Cities,HashMap<String,Integer> dist,List<Flight> flights)
    {

        List<Flight> F_list1 = new ArrayList<Flight>();

        int stack_size=Cities.length;

        for (int i=(stack_size-1);i>(0);i--)
        {
            String destination = Cities[i];  //extract destination airport
            String begin = Cities[i-1];  //extract departure airport
            int arrivalTime=dist.get(destination); //extract arrival time
            int minDepartureTime=dist.get(destination); //extract arrival time

            // Find Full information given the values
            String [] Values=findExactFlight(flights,begin,destination,arrivalTime,minDepartureTime);
            String beginStation=Values[0];
            String departureTime=Values[1];
            String destinationStation=Values[2];
            String arrivingTime=Values[3];
            String flight_name=Values[4];
            Flight SS=Flights.makeFlight(flight_name,beginStation,destinationStation,clockFormatC(departureTime),clockFormatC(arrivingTime));
            F_list1.add(SS);
        }

        int ll1=F_list1.size();
        List<Flight> final_Flight_list = new ArrayList<Flight>();

        for (int i=0;i<ll1;i++)
        {
            final_Flight_list.add(F_list1.get(ll1-i-1));
        }
        return (final_Flight_list);
    }

    // Extract_finishNames :   ListOfFlight  -> String[]
    // GIVEN: a ListOfFlight that describes all of the flights a
    //     traveller is willing to consider taking,
    // RETURNS: an String list of all the destination airports in the ListOfFlight
    // in the same order that was provided in the ListOfFlight
    // EXAMPLES:
    //     Extract_finishNames(ListOfFlights)
    // =>  ["BOS","PDX","MSP","LGA",...]
    private static String [] Extract_finishNames(List<Flight> flights)
    {
        int fsize=flights.size();
        String [] finishName= new String[fsize];
        for (int i=0;i<fsize;i++) {
            finishName[i] = flights.get(i).arrivesAt();
        }
        return(finishName);
    }

    // updateDistancesForNeighbours : HashMap<String,Integer>  -> HashMap<String,Integer>
    // GIVEN: a Hashmap neighbors and a Hashmap of dist

    // RETURNS: Updates Prev hashmap to capture the links
    // EXAMPLES:
    //    updateDistancesForNeighbours({"A":10,"B":20}{"A":12,"B":15,"C":500})
    // =>  {"A":10,"B":15,"C":500}

    private static HashMap<String,Integer> updateDistancesForNeighbours(HashMap<String,Integer> neighbors,HashMap<String,Integer> dist)
    {
        for (Map.Entry<String, Integer> neighbor : neighbors.entrySet())
        {
            String v=neighbor.getKey();
            int time=neighbor.getValue();
            if (time<dist.get(v))
            {
                dist.put(v,time);
            }
        }
        return dist;
    }
    // Extract_startTimes :   ListOfFlight  -> int []
    // GIVEN: a ListOfFlight that describes all of the flights a
    //     traveller is willing to consider taking,
    // RETURNS: an integer list of all the departure times (finishTimes) in the minute format from 00:00
    // in the same order that was provided in the ListOfFlight
    // EXAMPLES:
    //     Extract_startTimes(ListOfFlights)
    // =>  [134,356,278,1350,1067,...]
    private static int [] Extract_startTimes(List<Flight> flights)
    {
        int fsize=flights.size();
        int [] startTime= new int [fsize];
        for (int i=0;i<fsize;i++) {
            startTime[i] = clockFormat(Integer.toString(flights.get(i).departureTime()));
        }
        return(startTime);
    }

    // Extract_FinishTimes :   ListOfFlight  -> int []
    // GIVEN: a ListOfFlight that describes all of the flights a
    //     traveller is willing to consider taking,
    // RETURNS: an integer list of all the arrival times (finishTimes) in the minute format from 00:00
    //of the flight days that were reported
    // in the same order that was provided in the ListOfFlight
    // EXAMPLES:
    //     Extract_FinishTimes(ListOfFlights)
    // =>  [534,456,678,135,1067,...]
    private static int [] Extract_FinishTimes(List<Flight> flights)
    {
        int fsize=flights.size();
        int [] startTime= Extract_startTimes(flights);
        int [] finishTime= new int [fsize];

        for (int i=0;i<fsize;i++) {
            finishTime[i] = clockFormat(Integer.toString(flights.get(i).arrivalTime()));
            if (finishTime[i]<startTime[i])
            {
                finishTime[i]=finishTime[i]+1440;
            }
        }
        return(finishTime);
    }


    //main function is the api function
    public static void main(String[] args)
    {
        List<Flight> deltaFlights=FlightExamples.deltaFlights;
        //////////////////////////////////////////////////////////////////////////////////////////
        Boolean b=canGetThere ("06N", "PDX", deltaFlights);
        System.out.println("############  Final Answer for canGetThere ##########");
        System.out.println(b);

        Boolean c=canGetThere ("LGA", "PDX", deltaFlights);
        System.out.println("############  Final Answer for canGetThere ##########");
        System.out.println(c);
        //////////////////////////////////////////////////////////////////////////////////////////
        List<Flight> bestPath=fastestItinerary ("LGA", "PDX", deltaFlights);
        System.out.println("############  Final Answer for fastestItinerary ##########");
        System.out.println(bestPath.toString());
        //////////////////////////////////////////////////////////////////////////////////////////
        int cc=travelTime("LGA", "PDX", deltaFlights);
        System.out.println("############  Final Answer for travelTime ##########");
        System.out.println("Traveling Time minutes  = "+ cc);
        //////////////////////////////////////////////////////////////////////////////////////////

    }
}

