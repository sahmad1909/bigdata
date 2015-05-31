package bigdata;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class ParseWeatherData {

	public static String WEATHER_JSON_FILE = "C:/Users/L404008/git/bigdata/resource/weather_file.json";

	public static String CONDITION = "conds";

	public static String ORIGIN = "origin_weather";

	public static String DEST = "dest_weather";

	public static List<String> weatherCond = new ArrayList<String>();

	public static Map<String, String> condtionMap = new HashMap<String, String>();

	public static void main(String[] args) throws Exception {

		condtionValue();

		JSONArray jsonArray = readJsonFileitoArray(WEATHER_JSON_FILE);

		createARFF(jsonArray);

		System.out
				.print("  ********************************************************* ");
	}

	/**
	 * 
	 * @param min
	 * @param max
	 * @return
	 */

	public static int randInt(int min, int max) {

		Random rand = new Random();
		int randomNum = rand.nextInt((max - min) + 1) + min;

		return randomNum;
	}

	/**
	 * 
	 * @param jsonObject
	 * @param key
	 */
	public static void addCondtion(JSONObject jsonObject, String key) {

		JSONObject json = (JSONObject) jsonObject.get(key);

		if (json != null) {
			JSONArray jsonArray = (JSONArray) json.get(CONDITION);

			if (jsonArray != null) {
				for (Object value : jsonArray) {
					String cond = (String) value;
					if (!weatherCond.contains(cond)) {
						weatherCond.add(cond);
					}
				}
			}
		}
	}

	public static JSONArray readJsonFileitoArray(String fileName)
			throws Exception {
		JSONParser parser = new JSONParser();
		Object object = parser.parse(new FileReader(fileName));
		JSONArray jsonArray = (JSONArray) object;
		return jsonArray;
	}

	public static void writeJsonToFile(JSONArray jsonObject, String filePath)
			throws Exception {

		FileWriter file = new FileWriter(filePath, true);
		file.write(jsonObject.toJSONString());
		file.flush();
		file.close();
	}

	/**
	 * 
	 */
	public static void condtionValue() {

		// {Snow,Light Snow, Fog, Widespread Dust, Patches of Fog, Freezing
		// Rain, Thunderstorms and Rain,Heavy Rain,Thunderstorm,Light Freezing
		// Rain,Light Ice Pellets,Light Freezing Drizzle,Rain, Light Drizzle,
		// Light Thunderstorms and Rain, Light Rain, Drizzle, Mostly
		// Cloudy,Overcast,Partly Cloudy,Haze,Scattered Clouds,Clear}

		condtionMap.put("1", "Snow");
		condtionMap.put("2", "Light Snow");
		condtionMap.put("3", "Fog");
		condtionMap.put("4", "Widespread Dust");
		condtionMap.put("5", "Patches of Fog");
		condtionMap.put("6", "Freezing Rain");
		condtionMap.put("7", "Thunderstorms and Rain");
		condtionMap.put("8", "Heavy Rain");
		condtionMap.put("9", "Thunderstorm");
		condtionMap.put("10", "Light Freezing Rain");
		condtionMap.put("11", "Light Ice Pellets");
		condtionMap.put("12", "Light Freezing Drizzle");
		condtionMap.put("13", "Rain");
		condtionMap.put("14", "Light Drizzle");
		condtionMap.put("15", "Light Thunderstorms and Rain");
		condtionMap.put("16", "Light Rain");
		condtionMap.put("17", "Drizzle");
		condtionMap.put("18", "Mostly Cloudy");
		condtionMap.put("19", "Overcast");
		condtionMap.put("20", "Partly Cloudy");
		condtionMap.put("21", "Haze");
		condtionMap.put("22", "Scattered Clouds");
		condtionMap.put("23", "Clear");

	}

	public static void createARFF(JSONArray jsonArray) throws Exception {
		// Day of week
		// Carrier
		// Orig Airport
		// Orig Visisbility
		// origin Wind speed
		// origin Condition
		// Dest Aiport
		// Dest Visisbility
		// Dest Wind speed
		// Dest Condition
		// Flight Distance

		FileWriter fileWriter = new FileWriter(
				"C:/Users/L404008/git/bigdata/resource/datafile.txt");

		for (Object object : jsonArray) {
			JSONObject jsonObject = (JSONObject) object;

//			if (isWeatherExist(jsonObject).equals("YES")) {
				
				fileWriter.append(String.valueOf(jsonObject.get(CARRIER)));
				fileWriter.append(COMMA_DELIMITER);
				
				fileWriter.append(String.valueOf(jsonObject.get(DAYS_OF_WEEK)));
				fileWriter.append(COMMA_DELIMITER);
			
				fileWriter.append(String.valueOf(jsonObject.get(ORIGIN_AIRPORT)));
				fileWriter.append(COMMA_DELIMITER);

				fileWriter.append(getWeatherValue(jsonObject, ORIGIN,ORIGIN_WIND_SPEED));
				fileWriter.append(COMMA_DELIMITER);

				fileWriter.append(getWeatherValue(jsonObject, ORIGIN,ORIGIN_VISIBILITY));
				fileWriter.append(COMMA_DELIMITER);

				fileWriter.append("'"+ getCondtion(jsonObject, ORIGIN, ORIGIN_CONDITION)+ "'");
				fileWriter.append(COMMA_DELIMITER);

				fileWriter.append(String.valueOf(jsonObject.get(DEST_AIRPORT)));
				fileWriter.append(COMMA_DELIMITER);
//
//				fileWriter.append(getWeatherValue(jsonObject, DEST,	DEST_WIND_SPEED));
//				fileWriter.append(COMMA_DELIMITER);
//
//				fileWriter.append(getWeatherValue(jsonObject, DEST,	DEST_VISIBILITY));
//				fileWriter.append(COMMA_DELIMITER);
//
//				fileWriter.append("'"+ getCondtion(jsonObject, DEST, DEST_CONDITION) + "'");
//				fileWriter.append(COMMA_DELIMITER);
//
				fileWriter.append(String.valueOf(jsonObject.get(DISTANCE)));
				fileWriter.append(COMMA_DELIMITER);

				fileWriter.append(isWeatherExist(jsonObject));
				fileWriter.append(NEW_LINE_SEPARATOR);
//			}

		}
	}

	public static String isWeatherExist(JSONObject jsonObject) {

		String weatherDeal = (String) jsonObject.get(WEATHER_DETALY);

		if (weatherDeal != null) {
			Double value = new Double(weatherDeal);
			if (value > 0) {
				return "YES";
			}
		}

		return "NO";
	}

	/**
	 * 
	 * @param jsonObject
	 * @param weatherFor
	 * @param key
	 * @return
	 */
	public static String getWeatherValue(JSONObject jsonObject,
			String weatherFor, String key) {

		JSONObject jsonWeather = (JSONObject) jsonObject.get(weatherFor);

		if (jsonWeather != null) {
			return String.valueOf(jsonWeather.get(key));
		}

		if (key.equals("maxwspdm")) {
			return randWindSpeed();
		}

		if (key.equals("minvisi")) {
			return randomVisibility();
		}

		return null;
	}

	public static String getCondtion(JSONObject jsonObject, String weatherFor,
			String key) {

		JSONObject jsonWeather = (JSONObject) jsonObject.get(weatherFor);

		if (jsonWeather != null) {
			JSONArray jsonArray = (JSONArray) jsonWeather.get(CONDITION);

			if (jsonArray != null && jsonArray.size() > 0) {
				for (Object object : jsonArray) {
					String condtion = (String) object;

					System.out.println(condtion);
					for (String st : condtionMap.values()) {
						System.out.println("Condtion :" + st);
						if (st.equals(condtion)) {

							System.out.println("Condtion :" + st);
							return st;
						}
					}

				}
			}
		}

		return randCondition();
	}

	/**
	 * 
	 * @return
	 */
	public static String randCondition() {
		randInt(0, 6);

		Map<Integer, String> condition = new HashMap<Integer, String>();

		condition.put(new Integer("1"), "Sunny");
		condition.put(new Integer("2"), "clear sky");
		condition.put(new Integer("3"), "few clouds");
		condition.put(new Integer("4"), "calm");
		condition.put(new Integer("5"), "Rain");
		condition.put(new Integer("6"), "Hot");

		return condition.get(new Integer(randInt(0, 6)));
	}

	/**
	 * 
	 * @return
	 */
	public static String randWindSpeed() {
		return new Integer(randInt(2, 13)).toString();
	}

	/**
	 * 
	 * @return
	 */
	public static String randomVisibility() {
		return new Integer(randInt(31, 45)).toString();
	}

	public static String CARRIER = "carrier";
	public static String DAYS_OF_WEEK = "dayofweek";
	public static String ORIGIN_AIRPORT = "origin";
	public static String ORIGIN_WIND_SPEED = "maxwspdm";
	public static String ORIGIN_VISIBILITY = "minvisi";
	public static String ORIGIN_CONDITION = "conds";
	public static String DEST_AIRPORT = "dest";
	public static String DEST_WIND_SPEED = "maxwspdm";
	public static String DEST_VISIBILITY = "minvisi";
	public static String DEST_CONDITION = "conds";
	public static String DISTANCE = "distance";

	private static final String COMMA_DELIMITER = ",";

	private static final String NEW_LINE_SEPARATOR = "\n";

	public static String WEATHER_DETALY = "weatherdelay";

}
