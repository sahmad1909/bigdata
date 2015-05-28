package bigdata;

import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class ParseWeatherData {

	public static String WEATHER_JSON_FILE = "C:/Users/L404008/git/bigdata/resource/weather_file.json";

	public static String CONDITION = "conds";

	public static String ORIGIN = "origin_weather";

	public static String DEST = "dest_weather";

	public static List<String> weatherCond = new ArrayList<String>();
	
	public static Map<String,String> condtionMap = new HashMap<String,String>();

	public static void main(String[] args) throws Exception {

		JSONArray jsonArray = readJsonFileitoArray(WEATHER_JSON_FILE);

		for (Object object : jsonArray) {

			JSONObject jsonObject = (JSONObject) object;

			addCondtion(jsonObject, ORIGIN);
			addCondtion(jsonObject, DEST);
		}

		for (String cond : weatherCond) {
			System.out.println(cond);
		}

		System.out
				.print("  ********************************************************* ");
	}

	public static void addCondtion(JSONObject jsonObject, String key) {

		JSONObject json = (JSONObject) jsonObject.get(key);

		if (json != null) {
			JSONArray jsonArray = (JSONArray) json.get(CONDITION);

			if (jsonArray != null) {
				for (Object value : jsonArray) {
//					System.out.println((String) value);
					
					String cond = (String)value;					
					if(!weatherCond.contains(cond)){
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
	public static void condtionValue(){
				
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
}
