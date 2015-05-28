package bigdata;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class WeatherData {

	public static final String API_KEY = "86a9b3e36957b844";

	public static String WEATHER_JSON_FILE = "C:/Users/L404008/git/bigdata/resource/weather_file.json";

	public static String path = "C:/Users/L404008/git/bigdata/resource/weather.json";

	public static String RESULT_JSON = "C:/Users/L404008/git/bigdata/resource/result.json";

	public static String HISTORY = "history";

	public static String DAILY_SUMMRY = "dailysummary";

	public static String MIN_VISIBILITY = "minvisi";

	public static String MAX_VISIBILITY = "maxvisi";

	public static String MAX_WIND_SPEED = "maxwspdi";

	public static String MAX_WIND_SPEED_MEAN = "maxwspdm";

	public static String OBSERVATION = "observations";

	public static String CONDITION = "conds";

	public static String ORIGIN_CITY = "origin";

	public static String DEST_CITY = "dest";

	public static String FLIGHT_DATE = "flightdate";

	public static String WEATHER_DETALY = "weatherdelay";

	// history -> dailysummary -> [0].minvisi
	// history -> dailysummary -> [0].maxvisi
	//
	// history -> dailysummary -> [0].maxwspdm
	// history -> dailysummary -> [0].maxwspdi

	public static void main(String[] args) throws Exception {

		// Read Flight Result files.
		JSONArray jsonArray = readJsonFileitoArray(RESULT_JSON);

		createWeatherURL(jsonArray);

		writeJsonToFile(jsonArray, WEATHER_JSON_FILE);

		System.out.println(" ******************************* ");
		// Read the Json file and create the Weather URL

		// ISO8601DateFormat df = new ISO8601DateFormat();

		//

		// String dateString1 = "2012-08-24T09:59:59Z";
		// Date date = new
		// SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").parse(dateString1);

		/*
		 * System.out.println(" Min Visibility : " +
		 * parseWeatherJSON(readJsonFile(path), MIN_VISIBILITY));
		 * 
		 * System.out.println(" Max Visibility : " +
		 * parseWeatherJSON(readJsonFile(path), MAX_VISIBILITY));
		 * 
		 * System.out.println(" Max Wind Speeed : " +
		 * parseWeatherJSON(readJsonFile(path), MAX_WIND_SPEED));
		 * 
		 * System.out.println(" Max Wind Speeed Mean: " +
		 * parseWeatherJSON(readJsonFile(path), MAX_WIND_SPEED_MEAN));
		 * 
		 * 
		 * System.out.println(
		 * " ********************************************************************************************* "
		 * );
		 * 
		 * weatherCond(readJsonFile(path));
		 */
	}

	public static void createWeatherURL(JSONArray jsonArray) throws Exception {

		int i = 0;
		for (Object object : jsonArray) {
			JSONObject jsonObject = (JSONObject) object;

			if (isWeatherExist(jsonObject)) {
				url(jsonObject);
				i++;
			}

		}

		System.out.println("Total : " + i);
	}

	/**
	 * 
	 * @param jsonObject
	 * @return
	 */
	public static boolean isWeatherExist(JSONObject jsonObject) {

		String weatherDeal = (String) jsonObject.get(WEATHER_DETALY);

		if (weatherDeal != null) {
			Double value = new Double(weatherDeal);
			if (value > 0) {
				return true;
			}
		}

		return false;
	}

	/**
	 * 
	 * @param jsonObject
	 * @throws Exception
	 */
	public static void url(JSONObject jsonObject) throws Exception {
		String origin = (String) jsonObject.get(ORIGIN_CITY);
		String destination = (String) jsonObject.get(DEST_CITY);
		String flightDate = ISODateToString((String) jsonObject
				.get(FLIGHT_DATE));

		String originURL = "http://api.wunderground.com/api/86a9b3e36957b844/history_"
				+ flightDate + "/q/" + origin + ".json";
		String destURL = "http://api.wunderground.com/api/86a9b3e36957b844/history_"
				+ flightDate + "/q/" + destination + ".json";

		System.out.println(originURL);
		System.out.println(destURL);

		String UUIDString = UUID.randomUUID().toString();

		// Get Json from Orign URL
		String originJSON = getWeatherFromURL(originURL);

		JSONObject originWeatherJson = readJsonFile(originJSON);

		writeJsonToFile(originJSON, UUIDString + "_origin");

		// Get Json from Dest URL
		String destJSON = getWeatherFromURL(destURL);

		JSONObject destWeatherJson = readJsonFile(originJSON);

		writeJsonToFile(destJSON, UUIDString + "_dest");

		getWeatherAPI(jsonObject, "origin_weather", originWeatherJson,
				UUIDString);

		getWeatherAPI(jsonObject, "dest_weather", destWeatherJson, UUIDString);

	}

	public static void getWeatherAPI(JSONObject jsonObject1, String weatherFor,
			JSONObject weatherJson, String UUID) throws Exception {

		// weatherJson = readJsonFile(path);

		JSONObject weatherjsonObject = new JSONObject();

		weatherjsonObject.put(MIN_VISIBILITY,
				parseWeatherJSON(weatherJson, MIN_VISIBILITY));

		weatherjsonObject.put(MAX_VISIBILITY,
				parseWeatherJSON(weatherJson, MAX_VISIBILITY));

		weatherjsonObject.put(MAX_WIND_SPEED,
				parseWeatherJSON(weatherJson, MAX_WIND_SPEED));

		weatherjsonObject.put(MAX_WIND_SPEED_MEAN,
				parseWeatherJSON(weatherJson, MAX_WIND_SPEED_MEAN));

		weatherjsonObject.put(CONDITION, weatherCond(weatherJson));

		jsonObject1.put(weatherFor, weatherjsonObject);

		jsonObject1.put("UUID", UUID);

	}

	/**
	 * 
	 * @param jSONObject
	 * @param key
	 * @return
	 */
	public static String parseWeatherJSON(JSONObject jSONObject, String key) {

		JSONObject jSONObjectWeather = (JSONObject) jSONObject.get(HISTORY);

		if (jSONObjectWeather!=null && jSONObjectWeather.get(DAILY_SUMMRY) != null) {
			JSONArray jSONObjectDailySummry = (JSONArray) jSONObjectWeather
					.get(DAILY_SUMMRY);

			if (jSONObjectDailySummry != null
					&& jSONObjectDailySummry.size() > 0) {

				JSONObject dailyWeather = (JSONObject) jSONObjectDailySummry
						.get(0);
				return (String) dailyWeather.get(key);

			}
		}

		return "0.0";

	}

	/**
	 * 
	 * @param jSONObject
	 */
	public static JSONArray weatherCond(JSONObject jSONObject) {

		JSONObject jSONObjectWeather = (JSONObject) jSONObject.get(HISTORY);

		if (jSONObjectWeather!= null && jSONObjectWeather.get(OBSERVATION) != null) {
			JSONArray observationSummary = (JSONArray) jSONObjectWeather
					.get(OBSERVATION);

			JSONArray jsonArray = new JSONArray();

			if (observationSummary != null && observationSummary.size() > 0) {

				for (Object object : observationSummary) {
					JSONObject jsonObject = (JSONObject) object;
					System.out.println(" Condition : "
							+ jsonObject.get(CONDITION));

					jsonArray.add(jsonObject.get(CONDITION));
				}
			}
			return jsonArray;
		}

		return null;
	}

	public static String getWeatherFromURL(String url) throws Exception {

		// http://api.wunderground.com/api/86a9b3e36957b844/history_20101018/q/SFO.json

		// String url =
		// "http://api.wunderground.com/api/86a9b3e36957b844/history_20101018/q/SFO.json";

		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		// optional default is GET
		con.setRequestMethod("GET");

		int responseCode = con.getResponseCode();
		System.out.println("\nSending 'GET' request to URL : " + url);
		System.out.println("Response Code : " + responseCode);

		BufferedReader in = new BufferedReader(new InputStreamReader(
				con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();

		// print result
		System.out.println(response.toString());

		Thread.sleep(7000);

		return response.toString();
	}

	/**
	 * 
	 * @param fileName
	 * @return
	 * @throws Exception
	 */
	public static JSONObject readJsonFile(String fileName) throws Exception {
		JSONParser parser = new JSONParser();
		// Object object = parser.parse(new FileReader(fileName));

		Object object = parser.parse(fileName);
		JSONObject jSONObject = (JSONObject) object;
		return jSONObject;
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

	/**
	 * 
	 * @param date
	 * @return
	 * @throws Exception
	 */
	public static String ISODateToString(String date) throws Exception {
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
		Date d = format.parse(date);
		SimpleDateFormat format1 = new SimpleDateFormat("yyyyMMdd");
		return format1.format(d);
	}

	/**
	 * 
	 * @param jsonObject
	 * @param filePath
	 * @throws IOException
	 */
	public static void writeJsonToFile(JSONArray jsonObject, String filePath)
			throws Exception {

		FileWriter file = new FileWriter(filePath, true);
		file.write(jsonObject.toJSONString());
		file.flush();
		file.close();

	}

	/**
	 * 
	 * @param jsonObject
	 * @param filePath
	 * @throws Exception
	 */
	public static void writeJsonToFile(String jsonString, String fileName)
			throws Exception {

		String fileP = "C:/Users/L404008/git/bigdata/resource/" + fileName
				+ ".json";

		FileWriter file = new FileWriter(fileP, true);
		file.write(jsonString);
		file.flush();
		file.close();

	}

}
