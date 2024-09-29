package connect;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

public class Connect {
	private final String USERNAME ="root";
	private final String PASSWORD ="";
	private final String DATABASE ="njuice";
	private final String HOST ="localhost:3306";
	private final String CONNECTION = String.format("jdbc:mysql://%s/%s", HOST, DATABASE);

	private Connection con;
	private Statement st;

	public static Connect instance;
	
	public ResultSet rs;
	public ResultSetMetaData rsm;
	
	private Connect() {
		try {
			con = DriverManager.getConnection(CONNECTION, USERNAME, PASSWORD);
			st = con.createStatement();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static Connect getInstance() {
		if(instance == null) {
			instance = new Connect();
		}
		return instance;
	}
	public ResultSet executeQuery(String query) {
		try {
			rs = st.executeQuery(query);
			rsm = rs.getMetaData();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return rs;
	}
	
	public void executeUpdate(String query){
		try {
			st.executeUpdate(query);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		}
	public void closeConnection() {
        try {
            if (rs != null) {
                rs.close();
            }
            if (con != null) {
            	con.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exceptions appropriately
        }
    }
	
	public PreparedStatement prepareStatement(String query) {
		  PreparedStatement ps = null;
		  
		  try {
		   ps = con.prepareStatement(query);
		  } catch (SQLException e) {
		   // TODO Auto-generated catch block
		   e.printStackTrace();
		  }
		  
		  return ps;
		 }
	}

