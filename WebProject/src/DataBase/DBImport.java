package DataBase;
import java.sql.*;
import org.json.*;
import java.io.*;
public class DBImport {
	
	public static String parseString(String str) {
	 	 return str.replace("\"", "\\\"").replace("/", " or ").replace("'"," ");
	  }
	public static String jsonArrayToString(JSONArray array) {
	 	 StringBuilder sb = new StringBuilder();
	 	 try {
	 		 for (int i = 0; i < array.length(); i++) {
	 			 String obj = (String) array.get(i);
	 			 sb.append(obj);
	 			 if (i != array.length() - 1) {
	 				 sb.append(",");
	 			 }


	 		 }
	 	 } catch (JSONException e) {
	 		 e.printStackTrace();
	 	 }
	 	 return sb.toString();
	}
	public static JSONArray stringToJSONArray(String str) {
	 	 try {
	 		 return new JSONArray("[" + str + "]");
	 	 } catch (JSONException e) {
	 		 e.printStackTrace();
	 	 }
	 	 return null;
	  }

	public static void main(String[] args) {
	 	 try {
			//Step 1: Initialize the driver and connect to a SQL server
	 		 Class.forName("com.mysql.jdbc.Driver").newInstance();


	 		 Connection conn = null;
	 		 String line = null;


	 		 try {
	 			 conn = DriverManager
	 					 .getConnection("jdbc:mysql://localhost:3306/mysql?"
	 							 + "user=root&password=root");
	 		 } catch (SQLException e) {
	 			 System.out.println("SQLException " + e.getMessage());
	 			System.out.println("SQLState " + e.getSQLState());
	 			 System.out.println("VendorError " + e.getErrorCode());
	 		 }
	 		 if (conn == null) {
	 			 return;
	 		 }
			//Step 2: drop tables
	 		Statement stmt = conn.createStatement();
	 		 String sql = "DROP TABLE IF EXISTS USER_VISIT_HISTORY";
	 		 stmt.executeUpdate(sql);
	 		 sql = "DROP TABLE IF EXISTS USERS";
	 		 stmt.executeUpdate(sql);
	 		 sql = "DROP TABLE IF EXISTS RESTAURANTS";
	 		 stmt.executeUpdate(sql);
	  	 	 sql = "DROP TABLE IF EXISTS USER_REVIEW_HISTORY";
	  		 stmt.executeUpdate(sql);		
			 sql = "DROP TABLE IF EXISTS USER_CATEGORY_HISTORY";
	  		 stmt.executeUpdate(sql);
	  		sql = "CREATE TABLE RESTAURANTS "
	  				 + "(business_id VARCHAR(255) NOT NULL, "
	  				 + " name VARCHAR(255), " + "categories VARCHAR(255), "
	  				 + "city VARCHAR(255), " + "state VARCHAR(255), "
	  				 + "stars FLOAT," + "full_address VARCHAR(255), "
	  				 + "latitude FLOAT, " + " longitude FLOAT, "
	  				 + "image_url VARCHAR(255), "
	  				 + " PRIMARY KEY ( business_id ))";
	  		 stmt.executeUpdate(sql);
	  	 	sql = "CREATE TABLE USERS "
	  				 + "(user_id VARCHAR(255) NOT NULL, "
	  				 + " first_name VARCHAR(255), last_name VARCHAR(255), "
	  				 + " PRIMARY KEY ( user_id ))";
	  		 stmt.executeUpdate(sql);
			
	  		 sql = "CREATE TABLE USER_VISIT_HISTORY "
	  				 + "(visit_history_id bigint(20) unsigned NOT NULL AUTO_INCREMENT, "
	  				 + " user_id VARCHAR(255) NOT NULL , "
	  				 + " business_id VARCHAR(255) NOT NULL, "
	  				 + " last_visited_time timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP, "
	  				 + " PRIMARY KEY (visit_history_id),"
	  				 + "FOREIGN KEY (business_id) REFERENCES RESTAURANTS(business_id),"
	  				 + "FOREIGN KEY (user_id) REFERENCES users(user_id))";
	  		 stmt.executeUpdate(sql);
	  		
	  		 sql = "CREATE TABLE USER_REVIEW_HISTORY "
	  				 + "(visit_review_id bigint(20) unsigned NOT NULL AUTO_INCREMENT, "
	  				 + " user_id VARCHAR(255) NOT NULL , "
	  				 + " business_id VARCHAR(255) NOT NULL, "
	  				 + " PRIMARY KEY (visit_review_id))";
	  		 stmt.executeUpdate(sql);
	  		BufferedReader reader = new BufferedReader(new FileReader(
	  				 "/Users/ranjiang/Downloads/yelp_dataset_challenge_academic_dataset/yelp_academic_dataset_business.json"));
	  		 while ((line = reader.readLine()) != null) {
	  			 JSONObject restaurant = new JSONObject(line);
	  			 String business_id = restaurant.getString("business_id");
	  			 String name = parseString(restaurant.getString("name"));
	  			 String categories = parseString(jsonArrayToString(restaurant
	  					 .getJSONArray("categories")));
	  			 String city = parseString(restaurant.getString("city"));
	  			 String state = restaurant.getString("state");
	  			 String fullAddress = parseString(restaurant
	  					 .getString("full_address"));
	  			 double stars = restaurant.getDouble("stars");
	  			 double latitude = restaurant.getDouble("latitude");
	  			 double longitude = restaurant.getDouble("longitude");
	  			 String imageUrl = "http://www.example.com/img.JPG";
	  			 sql = "INSERT INTO RESTAURANTS " + "VALUES ('" + business_id
	  					 + "', \"" + name + "\", \"" + categories + "\", '"
	  					+ city + "', '" + state + "', " + stars + ", \""
	  					 + fullAddress + "\", " + latitude + "," + longitude
	  					 + ", \"" +imageUrl + "\""
	  					 + ")";
	  			 System.out.println(sql);
	  			 stmt.executeUpdate(sql);
	  		 }
	  		 reader.close();

	 	 } catch (Exception e) {
	 	 	e.printStackTrace();
	 	 }
	 	 System.out.println("Done Importing");
	  }

}
