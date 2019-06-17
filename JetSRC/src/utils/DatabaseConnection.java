/**
 * 
 */
package utils;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * @author simonhogg
 *
 */
abstract class DatabaseConnection {
    private String protocol = "jdbc:derby:";
    private String name;
	public Connection connection;
	
	Map<String,PreparedStatement> statements = new HashMap<>();
	
	public DatabaseConnection(String name) {
		this.name = name;
	}
	
	public void connect(Properties props) {
		try {
			DriverManager.registerDriver(new org.apache.derby.jdbc.EmbeddedDriver());
			connection = DriverManager.getConnection(protocol + name
			        + ";create=true", props);
			connection.setAutoCommit(false);
			StdOut.println("Database "+ name +" connected");
			StdOut.println(connection.getMetaData());
		} catch (SQLException  e) {
			StdOut.println("connect "+ e.getMessage());
			e.printStackTrace();
		}
	}
	
	public void createPreparedStatement(String name, String sql) {
		PreparedStatement pStatement = null;
		try {
			pStatement = connection.prepareStatement(sql);
			statements.put(name, pStatement);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			StdOut.println("createPreparedStatement "+ e.getMessage());
			e.printStackTrace();
		} finally {
			//try { pStatement.close(); } catch (Exception e) { /* ignored */ }
		}
	}
	
	void createTableIfNoneExists(String table, String sqlString) {
		String sql = "CREATE TABLE " + table.toUpperCase() + " " + sqlString;
		try {
			DatabaseMetaData dbmd = connection.getMetaData();
	        ResultSet rs = dbmd.getTables(null, null, table.toUpperCase(),null);
	        if(!rs.next())
	        {
	        	connection.createStatement().execute(sql);
	        }
	        else
	        {
	            System.out.println("Table already exists");
	        }
		} catch (Exception e) {
			StdOut.println("createTableIfNoneExist " + e.getMessage());
			e.printStackTrace();
		}
		
		
	}
	
	public void closeAllConnections() {
		//connection.
	}

}
