package bigdata;

import java.io.FileReader;

import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;

import com.mongodb.BasicDBList;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.util.JSON;

public class DataStorage {
	
	
	public static final String DB_NAME = "flights";
	public static final String TABLE_NAME = "flight";

	
	public static void main(String[] args) throws Exception {
		
		MongoClient mongoClient = new MongoClient( "localhost" );
		
		DB db = mongoClient.getDB(DB_NAME);
		
		DBCollection  collection = db.getCollection(TABLE_NAME);
		
					
		insert(readJsonFile(path),collection);
		
		DBCursor cursorDoc = collection.find();
		while (cursorDoc.hasNext()) {
			System.out.println(cursorDoc.next());
		}

		System.out.println("Done");
			
	}
	
	/**
	 * 
	 * @param jsonString
	 * @param collection
	 */
	public static void insert(String jsonString, DBCollection collection){
		
		 BasicDBList data = (BasicDBList) JSON.parse(jsonString);
		    for(int i=0; i < data.size(); i++){
		    	collection.insert((DBObject) data.get(i));
		    }
		
	}
	
	/**
	 * 
	 * @param fileName
	 * @return
	 * @throws Exception
	 */
	public static String readJsonFile(String fileName) throws Exception {
		JSONParser parser = new JSONParser();
		Object object = parser.parse(new FileReader(fileName));
		JSONArray jsonArray = (JSONArray) object;	
		return jsonArray.toJSONString();
	}
	
	
	
	
	
	
	public static String path = "C:/Users/L404008/git/bigdata/resource/result.json";
}
