package postGresConnection;

import java.sql.*;
import java.util.UUID;
public class JdbcDaoDemo {

	public static void main(String[] args) {
		//Dao <- data access object
		CarDao cd=CarDao.getInstance();
		Car c= cd.getCar(UUID.fromString("92830c03-5434-4400-bb67-9c27f1f89545"));
		System.out.println("car_id= "+c.car_id+" make= "+c.make+" model= "+c.model+" price= "+c.price);
		
		
		
		UUID car_id = UUID.randomUUID();
		System.out.println(cd.addCar(car_id, "pritam", "sarkar", 19600.52));
		
		c= cd.getCar(car_id);
		System.out.println("car_id= "+c.car_id+" make= "+c.make+" model= "+c.model+" price= "+c.price);
	}
}
class Car{
	UUID car_id;
	String make;
	String model;
	double price;
}
class CarDao{
	private static CarDao obj = null; 
	private CarDao() {
		System.out.println("Instance created");
	}
	public static CarDao getInstance() {
		if(obj==null) {
			obj =new CarDao();
		}
		return obj;
	}
	public static Connection setConnection() {
		Connection conn = null;
		try {
			Class.forName("org.postgresql.Driver");
			String database = "test";
			String url="jdbc:postgresql://localhost:5432/"+database;
			String username="postgres";
			String password="pritams1";
			conn =  DriverManager.getConnection(url,username,password);
			return conn;
		}catch(Exception e){
			e.getStackTrace();
			System.out.println(e.getClass().getName()+" "+e.getMessage());
			return null;
		}
	}
	public Car getCar(UUID car_id) {
		PreparedStatement pst=null;
		try {
			Car c=new Car();
			Connection conn=setConnection();
			if(conn==null) return null;
			String query="SELECT * FROM car WHERE car_id=?";
			pst = conn.prepareStatement(query);
			pst.setObject(1, car_id);
			ResultSet rs = pst.executeQuery();
			rs.next();
			c.car_id=(UUID)rs.getObject("car_id"); //down casting
			c.make=rs.getString("make");
			c.model=rs.getString("model");
			c.price=rs.getDouble("price");
			pst.close();
			conn.close();
			return c;
		}catch(Exception e) {
			e.getStackTrace();
			System.out.println(e.getClass().getName()+" "+e.getMessage());
			return null;
		}
	}
	public boolean addCar(UUID car_id,String make,String model,double price) {
		PreparedStatement pst = null;
		try {
			Connection conn=setConnection();
			if(conn==null) return false;
			String query="insert into car (car_id, make, model, price)"+
			" values (?, ?, ?, ?)";
			// OR get only 3 parameters
		// String query="insert into car (car_id, make, model, price) values (uuid_generate_v4() , ?, ?, ?)";
			pst = conn.prepareStatement(query);
			pst.setObject(1, car_id); // up casting
			pst.setString(2, make);
			pst.setString(3, model);
			pst.setDouble(4, price);
			int affected_rows = pst.executeUpdate(); // no of rows affected
			if(affected_rows==0) return false; // if insertion not done -_-
			pst.close();
			conn.close();
			return true;
		}catch(Exception e) {
			e.getStackTrace();
			System.out.println(e.getClass().getName()+" "+e.getMessage());
			return false;
		}
	}
}

