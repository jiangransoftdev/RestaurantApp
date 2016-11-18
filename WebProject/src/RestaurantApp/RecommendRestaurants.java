package RestaurantApp;
import java.io.*;
import java.util.*;
import org.json.*;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import DataBase.DBConnection;
/**
 * Servlet implementation class RecommendRestaurants
 */
@WebServlet("/RecommendRestaurants")
public class RecommendRestaurants extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final DBConnection connection = new DBConnection();

    /**
     * @see HttpServlet#HttpServlet()
     */
    public RecommendRestaurants() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
	 		 if (input.has("user_id")) {
	 			 String user_id = (String) input.get("user_id");
	 			 array = connection.RecommendRestaurants(user_id);
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
