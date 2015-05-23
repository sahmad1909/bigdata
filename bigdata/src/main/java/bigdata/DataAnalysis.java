package bigdata;

import java.io.FileReader;
import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class DataAnalysis {

	public static final String WEATHER_DELAY = "weatherdelay";
	

	static String[] flightAttributes = { "flightdate", "flightnum",
			"airlineid", "carrier", "originairportid", "origincityname",
			"originstatename", "destairportid", "destcityname", "deststate",
			"deptime", "arrtime", "lateaircraftdelay", "arrdelay",
			"divarrdelay", "depdelay", "weatherdelay", "securitydelay",
			"depdelayminutes", "nasdelay", "carrierdelay" };

	public static void main(String[] args) throws Exception {
		JSONArray jsonArray = readJsonFile(path);

		// Calculating Mean
		System.out.println("Weather Mean = "+ calculateMean(jsonArray, WEATHER_DELAY));

		System.out.println("Arr Delay = " + calculateMean(jsonArray, "arrdelay"));		
		
		System.out.println("Late Aircract Delay = " + calculateMean(jsonArray, "lateaircraftdelay"));		
		
		System.out.println("Divarrdelay Delay = " + calculateMean(jsonArray, "divarrdelay"));		
		
		System.out.println("Depdelay Delay = " + calculateMean(jsonArray, "depdelay"));
		
		System.out.println("securitydelay Delay = " + calculateMean(jsonArray, "securitydelay"));
		
		System.out.println("NASdelay Delay = " + calculateMean(jsonArray, "nasdelay"));		
		
		System.out.println("carrierdelay Delay = " + calculateMean(jsonArray, "carrierdelay"));
		
		
		// Standard Devitaion 
		
		System.out.println(" ********************************************************************************************************  ");
		
		Statistics statistics = new Statistics(getFlightData(jsonArray,WEATHER_DELAY));		
		System.out.println("Weather Std Deviation : " + statistics.getStdDev());
		
		 statistics = new Statistics(getFlightData(jsonArray,"arrdelay"));		
		System.out.println("arrdelay Std Deviation : " + statistics.getStdDev());
		
		
		 statistics = new Statistics(getFlightData(jsonArray,"lateaircraftdelay"));		
		System.out.println("lateaircraftdelay Std Deviation : " + statistics.getStdDev());
		
		 statistics = new Statistics(getFlightData(jsonArray,"divarrdelay"));		
		System.out.println("divarrdelay Std Deviation : " + statistics.getStdDev());
		
		 statistics = new Statistics(getFlightData(jsonArray,"depdelay"));		
		System.out.println("depdelay Std Deviation : " + statistics.getStdDev());
		
		
		 statistics = new Statistics(getFlightData(jsonArray,"securitydelay"));		
		 System.out.println("securitydelay Std Deviation : " + statistics.getStdDev());
			
			
		  statistics = new Statistics(getFlightData(jsonArray,"nasdelay"));		
		  System.out.println("nasdelay Std Deviation : " + statistics.getStdDev());
		  
		  
		  statistics = new Statistics(getFlightData(jsonArray,"carrierdelay"));		
		  System.out.println("carrierdelay Std Deviation : " + statistics.getStdDev());
		
		

	}
	
	
	public static double[] getFlightData(JSONArray jsonArray,String dealyAttribute) {
		
		ArrayList<Double> list = new ArrayList<Double>();
		
		for (Object object : jsonArray) {
			JSONObject jsonObject = (JSONObject) object;
			
			String attributeValue = (String) jsonObject.get(dealyAttribute);
			
			if (attributeValue != null) {

				
				Double value = new Double(attributeValue);

				if (value > 0) {
					list.add(new Double(attributeValue));
				}
			}
		}
		
		
		double[] values = new double[list.size()];
		
		if(!list.isEmpty()){
			int i = 0;
			for(Double d:list){
				values[i] = d.doubleValue();
				i++;
			}
		}
		
	return values;	
	}

	public static double calculateMean(JSONArray jsonArray,
			String dealyAttribute) {

		int totalSize = 0;

		double meanValue = 0;

		for (Object object : jsonArray) {
			JSONObject jsonObject = (JSONObject) object;
			String attributeValue = (String) jsonObject.get(dealyAttribute);

			if (attributeValue != null) {

				
				Double value = new Double(attributeValue);

				if (value > 0) {
					meanValue += new Double(attributeValue);
					totalSize++;
				}
			}

		}

		if (jsonArray.size() > 0 && meanValue > 0) {
			return meanValue / totalSize;
		}

		return 0;
	}

	public static JSONArray readJsonFile(String fileName) throws Exception {
		JSONParser parser = new JSONParser();
		Object object = parser.parse(new FileReader(fileName));
		JSONArray jsonArray = (JSONArray) object;
		return jsonArray;
	}

	public static String path = "C:/Users/L404008/git/bigdata/resource/result.json";
}
