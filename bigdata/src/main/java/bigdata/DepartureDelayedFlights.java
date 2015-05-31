package bigdata;

import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class DepartureDelayedFlights {

	public static String RESULT_JSON = "C:/Users/L404008/git/bigdata/resource/result.json";

	static String[] flightAttributes = { "flightdate", "flightnum",
			"airlineid", "carrier", "originairportid", "origincityname",
			"originstatename", "destairportid", "destcityname", "deststate",
			"deptime", "arrtime", "lateaircraftdelay", "arrdelay",
			"divarrdelay", "depdelay", "weatherdelay", "securitydelay",
			"depdelayminutes", "nasdelay", "carrierdelay", "origin", "dest",
			"dayofweek", "dayofmonth", "month", "year", "distance" };

	public static Map<String, Integer> airportCountNO = new TreeMap<String, Integer>();

	public static Map<String, Integer> carrierCountNO = new TreeMap<String, Integer>();

	public static Map<String, Integer> airportCountYES = new TreeMap<String, Integer>();

	public static Map<String, Integer> carrierCountYES = new TreeMap<String, Integer>();

	public static void main(String[] args) throws Exception {

		JSONArray jsonArray = readJsonFileitoArray(RESULT_JSON);
//		FileWriter fileWriter = new FileWriter(
//				"C:/Users/L404008/git/bigdata/resource/departure_data.csv");

		for (Object object : jsonArray) {
			JSONObject jsonObject = (JSONObject) object;

			jsonObject.get("flightdate");
			jsonObject.get("origin");
			jsonObject.get("carrier");
			jsonObject.get("depdelay");

//			fileWriter.append((String) jsonObject.get("flightdate"));
//			fileWriter.append(COMMA_DELIMITER);

			String originAirport = (String) jsonObject.get("origin");

//			fileWriter.append(originAirport);
//			fileWriter.append(COMMA_DELIMITER);

			String carrier = (String) jsonObject.get("carrier");
//			fileWriter.append(carrier);
//			fileWriter.append(COMMA_DELIMITER);

			String delayed = isWeatherExist(jsonObject);
//			fileWriter.append(delayed);
//			fileWriter.append(NEW_LINE_SEPARATOR);

			if (delayed.endsWith("YES")) {

				// Airport Count YES
				if (airportCountYES.get(originAirport) != null) {
					int value = airportCountYES.get(originAirport);
					value++;
					airportCountYES.put(originAirport, value);
				} else {
					airportCountYES.put(originAirport, new Integer(0));
				}

				// Carrier Count Yes

				if (carrierCountYES.get(carrier) != null) {
					int value = carrierCountYES.get(carrier);
					value++;
					carrierCountYES.put(carrier, value);
				} else {
					carrierCountYES.put(carrier, new Integer(0));
				}

			}

			if (delayed.endsWith("NO")) {
				// Airport Count YES
				if (airportCountNO.get(originAirport) != null) {
					int value = airportCountNO.get(originAirport);
					value++;
					airportCountNO.put(originAirport, value);
				} else {
					airportCountNO.put(originAirport, new Integer(0));
				}

				// Carrier Count Yes

				if (carrierCountNO.get(carrier) != null) {
					int value = carrierCountNO.get(carrier);
					value++;
					carrierCountNO.put(carrier, value);
				} else {
					carrierCountNO.put(carrier, new Integer(0));
				}
			}

		}


		
	System.out.println("******************* Name of airport delayed        ***************************************");	
		printMap(airportCountYES);
		
		
		System.out.println("******************* Name of Carrier delayed        ***************************************");
		printMap(carrierCountYES);
		
		
		System.out.println("******************* Name of airport Not Delayed        ***************************************");
		printMap(airportCountNO);
		
		
		System.out.println("******************* Name of Carrier Not Delayed        ***************************************");
		printMap(carrierCountNO);
		
		System.out
				.println(" ***************************** Done ************************************");
	}

	public static void printMap(Map<String, Integer> map) {

		for (Map.Entry<String, Integer> entry : map.entrySet()) {
			String key = entry.getKey();
			Integer value = entry.getValue();
			System.out.println(key + "," + value);
		}

	}

	/**
	 * 
	 * @param fileName
	 * @return
	 * @throws Exception
	 */
	public static JSONArray readJsonFileitoArray(String fileName)
			throws Exception {
		JSONParser parser = new JSONParser();
		Object object = parser.parse(new FileReader(fileName));
		JSONArray jsonArray = (JSONArray) object;
		return jsonArray;
	}

	public static String isWeatherExist(JSONObject jsonObject) {

		String weatherDeal = (String) jsonObject.get("depdelay");

		if (weatherDeal != null) {
			Double value = new Double(weatherDeal);
			if (value > 0) {
				return "YES";
			}
		}

		return "NO";
	}

	private static final String COMMA_DELIMITER = ",";

	private static final String NEW_LINE_SEPARATOR = "\n";

}
