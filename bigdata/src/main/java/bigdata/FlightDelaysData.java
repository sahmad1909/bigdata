package bigdata;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class FlightDelaysData {

	static String enigma_api = "6336800936fe4cd1fd565f4865f4e792";
	static String url = "https://api.enigma.io/v2/data/" + enigma_api;
	static String url2 = url
			+ "/us.gov.dot.rita.trans-stats.on-time-performance.2012?page=";

	static String[] flightAttributes = { "flightdate", "flightnum",
			"airlineid", "carrier", "originairportid", "origincityname",
			"originstatename", "destairportid", "destcityname", "deststate",
			"deptime", "arrtime", "lateaircraftdelay", "arrdelay",
			"divarrdelay", "depdelay", "weatherdelay", "securitydelay",
			"depdelayminutes", "nasdelay", "carrierdelay","origin","dest","dayofweek","dayofmonth","month","year","distance"};
	static JSONArray resultJsonArray = new JSONArray();
	static String path = "C:/Users/L404008/git/bigdata/resource/result.json";
	
	

	public static void main(String[] args) throws Exception {

		
		for(int i=1; i < 3; i++){
		URL url = new URL(url2+i);
		URLConnection urlConnection = url.openConnection();
		BufferedReader in = new BufferedReader(new InputStreamReader(
				urlConnection.getInputStream()));

		StringBuffer data = new StringBuffer();

		String inputLine;
		while ((inputLine = in.readLine()) != null) {
			data.append(inputLine);
			System.out.println(inputLine);
		}
		in.close();

		JSONParser jsonParser = new JSONParser();

		Object object = jsonParser.parse(data.toString());

		JSONObject json = (JSONObject) object;

		JSONArray jsonArray = (JSONArray) json.get("result");
		
		addJosnResultIntoArray(jsonArray);

		}
		writeJsonToFile(resultJsonArray,path);
		System.out.println(" === Done ===" + resultJsonArray.size());

	}

	
	/**
	 * 
	 * @param jsonArray
	 */
	public static void addJosnResultIntoArray(JSONArray jsonArray) {

		for (Object object : jsonArray) {
			JSONObject jsonObject = (JSONObject) object;

			JSONObject newJosnObject = new JSONObject();
			for (String att : flightAttributes) {
				newJosnObject.put(att, jsonObject.get(att));
			}

			resultJsonArray.add(newJosnObject);

		}

	}

	/**
	 * 
	 * @param jsonString
	 * @return
	 */
	public static String toPrettyFormat(String jsonString) {
		JsonParser parser = new JsonParser();
		JsonObject json = parser.parse(jsonString).getAsJsonObject();
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		String prettyJson = gson.toJson(json);
		return prettyJson;
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

}
