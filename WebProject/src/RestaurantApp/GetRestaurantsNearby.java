package RestaurantApp;
import DataBase.DBConnection;
import java.io.*;
import java.util.*;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.*;
/**
 * Servlet implementation class GetRestaurantsNearby
 */
@WebServlet("/GetRestaurantsNearby")
public class GetRestaurantsNearby extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final DBConnection connection = new DBConnection();
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GetRestaurantsNearby() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.setContentType("application/json");
	  	 response.addHeader("Access-Control-Allow-Origin", "*");
	  	 String username = "";
	  	 if (request.getParameter("username") != null) {
	  		 username = request.getParameter("username");
	  	 }
	  	 JSONObject obj = new JSONObject();
	  	 try {
	  		 obj.append("username", username);
	  	 } catch (JSONException e) {
	  		 // TODO Auto-generated catch block
	  		 e.printStackTrace();
	  	 }
	  	 PrintWriter out = response.getWriter();
	  	 out.print(obj);
	  	 out.flush();
	  	 out.close();

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request,
	 		 HttpServletResponse response) throws ServletException, IOException {
	 	 StringBuffer jb = new StringBuffer();
	 	 String line = null;
	 	 try {
	 		 BufferedReader reader = request.getReader();
	 		 while ((line = reader.readLine()) != null) {
	 			 jb.append(line);
	 		 }
	 		 reader.close();
	 	 } catch (Exception e) { /* report an error */
	 	 }


	 	 try {
	 		 JSONObject input = new JSONObject(jb.toString());
	 		 JSONArray array = null;
	 		if (input.has("lat") && input.has("lon")) {
	 			 double lat = (Double) input.get("lat");
	 			 double lon = (Double) input.get("lon");
	 			 array = connection.GetRestaurantsNearLoationViaYelpAPI(lat, lon);
	 		 }
	 		 response.setContentType("application/json");
	 		 response.addHeader("Access-Control-Allow-Origin", "*");
	 		 PrintWriter out = response.getWriter();
	 		 out.print(array);
	 		 out.flush();
	 		 out.close();
	 	 } catch (JSONException e) {
	 		 e.printStackTrace();
	 	 }
	  }


}
