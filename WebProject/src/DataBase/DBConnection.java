package DataBase;
import java.sql.*;
import org.json.*;
import Yelp.Restaurant;
import Yelp.YelpAPI;

import java.util.*;
public class DBConnection {
	private Connection conn = null;
	  private static final int MAX_RECOMMENDED_RESTAURANTS = 20;
	  public DBConnection() {
	 	 try {
	 		 Class.forName("com.mysql.jdbc.Driver").newInstance();
	 		 conn = DriverManager
	 				 .getConnection("jdbc:mysql://ec2-35-161-62-175.us-west-2.compute.amazonaws.com:3306/mysql?"
	 						 + "user=root&password=root");
	 	 } catch (Exception e) {
	 		 e.printStackTrace();
	 	 }
	  }
	  public JSONArray GetRestaurantsNearLoationViaYelpAPI(double lat, double lon) {
		   	 try {
		   		 YelpAPI api = new YelpAPI();
		   		 JSONObject response = new JSONObject(
		   				 api.searchForBusinessesByLocation(lat, lon));
		   		 JSONArray array = (JSONArray) response.get("businesses");
		   		 if (conn == null) {
		   			 return null;
		   		 }
		   		 Statement stmt = conn.createStatement();
		   		 String sql = "";
		   		 List<JSONObject> list = new ArrayList<JSONObject>();
		   		 for (int i = 0; i < array.length(); i++) {
		   			 JSONObject object = array.getJSONObject(i);
		   			 Restaurant restaurant = new Restaurant(object);
		   			 String business_id = restaurant.getBusinessId();
		   			 String name = restaurant.getName();
		   			 String categories = restaurant.getCategories();
		   			 String city = restaurant.getCity();
		   			 String state = restaurant.getState();
		   			 String fullAddress = restaurant.getFullAddress();
		   			 double stars = restaurant.getStars();
		   			 double latitude = restaurant.getLatitude();
		   			 double longitude = restaurant.getLongitude();
		   			 String imageUrl = restaurant.getImageUrl();
		   			 JSONObject obj = new JSONObject();
		   			 obj.append("business_id", business_id);
		   			 obj.append("name", name);
		   			 obj.append("stars", stars);
		   			 obj.append("latitude", latitude);
		   			 obj.append("longitude", longitude);
		   			 obj.append("full_address", fullAddress);
		   			 obj.append("city", city);
		   			 obj.append("state", state);
		   			 obj.append("categories", categories);
		   			 obj.append("image_url", imageUrl);
		   			 sql = "INSERT IGNORE INTO RESTAURANTS " + "VALUES ('"
		   					 + business_id + "', \"" + name + "\", \"" + categories
		   					 + "\", '" + city + "', '" + state + "', " + stars
		   					 + ", \"" + fullAddress + "\", " + latitude + ","
		   					 + longitude + ",\"" + imageUrl + "\")";
		   			 System.out.println(sql);
		   			 stmt.executeUpdate(sql);
		   			 list.add(obj);
		   		 }
		   		 return new JSONArray(list);
		   	 } catch (Exception e) { /* report an error */
		   		 System.out.println(e.getMessage());
		   	 }
		   	 return null;
		    }

	  public JSONArray GetRestaurantsNearLoation(double lat, double lon) {
		  	 try {
		  		 if (conn == null) {
		  			 return null;
		  		 }
		  		 Statement stmt = conn.createStatement();
		  		 String sql = "SELECT business_id, name, full_address, categories, stars, latitude, longitude, city, state from RESTAURANTS LIMIT 10";
		  		 ResultSet rs = stmt.executeQuery(sql);
		  		 List<JSONObject> list = new ArrayList<JSONObject>();
		  		 while (rs.next()) {
		  			 JSONObject obj = new JSONObject();
		  			 obj.append("business_id", rs.getString("business_id"));
		  			 obj.append("name", rs.getString("name"));
		  			 obj.append("stars", rs.getFloat("stars"));
		  			 obj.append("latitude", rs.getFloat("latitude"));
		  			 obj.append("longitude", rs.getFloat("longitude"));
		  			 obj.append("full_address", rs.getString("full_address"));
		  			 obj.append("city", rs.getString("city"));
		  			 obj.append("state", rs.getString("state"));
		  			obj.append("categories",
		  					 DBImport.stringToJSONArray(rs.getString("categories")));
		  			 list.add(obj);
		  		 }
		  		 return new JSONArray(list);
		  	 } catch (Exception e) { /* report an error */
		  		 System.out.println(e.getMessage());
		  	 }
		  	 return null;
		   }
	  public void SetVisitedRestaurants(String userId, List<String> businessIds) {
		  	 try {
		  		 if (conn == null) {
		  			 return;
		  		 }
		  		 Statement stmt = conn.createStatement();
		  		 String sql = "";
		  		 for (String businessId : businessIds) {
		  			 sql = "INSERT INTO USER_VISIT_HISTORY (`user_id`, `business_id`) VALUES (\""
		  					 + userId + "\", \"" + businessId + "\")";
		  			 stmt.executeUpdate(sql);
		  		 }


		  	 } catch (Exception e) { /* report an error */
		  		 System.out.println(e.getMessage());
		  	 }
		   }
	  public JSONArray RecommendRestaurants(String userId) {
		  	 try {
		  		 if (conn == null) {
		  			 return null;
		  		 }


		  		 Set<String> visitedRestaurants = getVisitedRestaurants(userId);
		  		 Set<String> allCategories = new HashSet<>();// why hashSet?
		  		 for (String restaurant : visitedRestaurants) {
		  			 allCategories.addAll(getCategories(restaurant));
		  		 }
		  		 Set<String> allRestaurants = new HashSet<>();
		  		 for (String category : allCategories) {
		  			 Set<String> set = getBusinessId(category);
		  			 allRestaurants.addAll(set);
		  		 }
		  		 Set<JSONObject> diff = new HashSet<>();
		  		 int count = 0;
		  		 for (String business_id : allRestaurants) {
		  			 if (!visitedRestaurants.contains(business_id)) {
		  				diff.add(getRestaurantsById(business_id));
		  				 count++;
		  				 if (count >= MAX_RECOMMENDED_RESTAURANTS) {
		  					 break;
		  				 }
		  			 }
		  		 }
		  		 return new JSONArray(diff);
		  	 } catch (Exception e) { /* report an error */
		  		 System.out.println(e.getMessage());
		  	 }
		  	 return null;
		   }
	  private Set<String> getVisitedRestaurants(String userId) {
		  	 Set<String> visitedRestaurants = new HashSet<String>();
		  	 try {
		  		 Statement stmt = conn.createStatement();
		  		 String sql = "SELECT business_id from USER_VISIT_HISTORY WHERE user_id="
		  				 + userId;
		  		 ResultSet rs = stmt.executeQuery(sql);
		  		 while (rs.next()) {
		  			 String visited_restaurant = rs.getString("business_id");
		  			 visitedRestaurants.add(visited_restaurant);
		  		 }
		  	 } catch (Exception e) {
		  		 e.printStackTrace();
		  	 }
		  	 return visitedRestaurants;
		   }
	  private Set<String> getCategories(String business_id) {
		  	 try {
		  		 if (conn == null) {
		  			 return null;
		  		 }
		  		 Statement stmt = conn.createStatement();
		  		 String sql = "SELECT categories from RESTAURANTS WHERE business_id='"
		  				 + business_id + "'";
		  		 ResultSet rs = stmt.executeQuery(sql);
		  		 if (rs.next()) {
		  			 Set<String> set = new HashSet<>();
		  			 String[] categories = rs.getString("categories").split(",");
		  			for (String category : categories) {
		  				 set.add(category.trim());
		  			 }
		  			 return set;
		  		 }
		  	 } catch (Exception e) { /* report an error */
		  		 System.out.println(e.getMessage());
		  	 }
		  	 return new HashSet<String>();
		   }
	  private Set<String> getBusinessId(String category) {
		  	 Set<String> set = new HashSet<>();
		  	 try {
		  		 if (conn == null) {
		  			 return null;
		  		 }
		  		 Statement stmt = conn.createStatement();
		  		 String sql = "SELECT business_id from RESTAURANTS WHERE categories LIKE '%"
		  				 + category + "%'";
		  		 ResultSet rs = stmt.executeQuery(sql);
		  		 while (rs.next()) {
		  			 String business_id = rs.getString("business_id");
		  			 set.add(business_id);
		  		 }
		  		 return set;
		  	 } catch (Exception e) { /* report an error */
		  		 System.out.println(e.getMessage());
		  	 }
		  	 return set;
		   }
	  private JSONObject getRestaurantsById(String businessId) {
		  	 try {
		  		 Statement stmt = conn.createStatement();
		  		 String sql = "SELECT business_id, name, full_address, categories, stars, latitude, longitude, city, state, image_url from "
		  				 + "RESTAURANTS where business_id='" + businessId + "'" + " ORDER BY stars DESC";
		  		 ResultSet rs = stmt.executeQuery(sql);
		  		 if (rs.next()) {
		  			 JSONObject obj = new JSONObject();
		  			 obj.append("business_id", rs.getString("business_id"));
		  			 obj.append("name", rs.getString("name"));
		  			 obj.append("stars", rs.getFloat("stars"));
		  			 obj.append("latitude", rs.getFloat("latitude"));
		  			 obj.append("longitude", rs.getFloat("longitude"));
		  			 obj.append("full_address", rs.getString("full_address"));
		  			 obj.append("city", rs.getString("city"));
		  			obj.append("state", rs.getString("state"));
		  			 obj.append("categories",
		  					 DBImport.stringToJSONArray(rs.getString("categories")));
		  			 obj.append("image_url", rs.getString("image_url"));
		  			 return obj;
		  		 }
		  	 } catch (Exception e) { /* report an error */
		  		 System.out.println(e.getMessage());
		  	 }
		  	 return null;
		   }


}
