package postGresConnection;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.PreparedStatement;

public class ConnectToPostGres {

	public static void main(String[] args) {
		getting_first_10_rows();
		get_the_car_whose_maker_is_ford();
	}
	public static void getting_first_10_rows() {
		Connection conn = null;
		Statement st = null;
		try {
			String database = "test";
			String url="jdbc:postgresql://localhost:5432/"+database;
			String username="postgres";
			String password="pritams1";
			Class.forName("org.postgresql.Driver");
			conn = DriverManager.getConnection(url,username,password);
			System.out.println("Database opened succesfully");
			st=conn.createStatement();
			String query="SELECT * FROM car LIMIT 10";
			ResultSet rs=st.executeQuery(query);
			while(rs.next()) {
				String car_id = rs.getString("car_id");
				String make = rs.getString("make");
				String model = rs.getString("model");
				double price =  rs.getDouble("price");
				System.out.println("car_id= "+car_id+" make= "+make+" model= "+model+" price= "+price);
			}
			st.close();
			conn.close();
			
		}catch(Exception e) {
			e.getStackTrace();
			System.out.println(e.getClass().getName()+" "+e.getMessage());
		}
	}
	public static void get_the_car_whose_maker_is_ford() {
		Connection conn = null;
		PreparedStatement pst=null;
		try {
			String database = "test";
			String url="jdbc:postgresql://localhost:5432/"+database;
			String username="postgres";
			String password="pritams1";
			Class.forName("org.postgresql.Driver");
			conn = DriverManager.getConnection(url,username,password);
			System.out.println("Database opened succesfully");
			String query="SELECT * FROM car WHERE make=? LIMIT ?";
			pst = conn.prepareStatement(query);
			pst.setString(1, "Ford");
			pst.setInt(2, 10);
			ResultSet rs=pst.executeQuery();
			while(rs.next()) {
				String car_id = rs.getString("car_id");
				String make = rs.getString("make");
				String model = rs.getString("model");
				double price =  rs.getDouble("price");
				System.out.println("car_id= "+car_id+" make= "+make+" model= "+model+" price= "+price);
			}
			pst.close();
			conn.close();
		}catch(Exception e) {
			e.getStackTrace();
			System.out.println(e.getClass().getName()+" "+e.getMessage());
		}
	}
}
