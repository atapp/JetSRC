/**
 * 
 */
package utils;


import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import configuration_manager.ConfigFileFormatException;

/**
 * @author simonhogg
 *
 */
public class ApprovedConfigurationsConnection extends DatabaseConnection{
	
	private static final String NAME_STRING = "APPROVEDCONFIGURATIONS";
	private static final String TABLE_NAME = "configs";
	
	public ApprovedConfigurationsConnection() {
		super(NAME_STRING);
		super.connect(null);
		//addPreparedStatements();
	}
	
	public void addPreparedStatements() {
		createTableIfNoneExists(TABLE_NAME, "(UID varchar(12)," + 
				" pylon1 varchar(255)," + 
				" pylon2 varchar(255)," + 
				" pylon3 varchar(255)," + 
				" pylon4 varchar(255)," + 
				" pylon5 varchar(255)," + 
				" pylon6 varchar(255)," + 
				" pylon7 varchar(255)," + 
				" pylon8 varchar(255)," + 
				" pylon9 varchar(255)," +  
				" details varchar(255)" + 
				")"
			    );
		createPreparedStatement("insert", "insert into "+TABLE_NAME+" values (?,?,?,?,?,?,?,?,?,?,?)");
	}
	
	public void insert(String[] stringArray) throws ConfigFileFormatException{
		if (stringArray.length == 11) {
			PreparedStatement insert = statements.get("insert");
			int count = 1;
			for (String string : stringArray) {
				try {
					insert.setString(count, string);
					count++;
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			try {
				insert.execute();
				//insert.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			throw new ConfigFileFormatException("Approved Configs CSV file", "Problem reading file, confirm each row has 11 values");
		}
	}
	
	public ResultSet filter(String[] pylons, String[] stores) {
		Statement filterStatement = null; 
		try {
			filterStatement = connection.createStatement();
			StringBuilder filterString = new StringBuilder();
			if (pylons.length == stores.length) {
				filterString.append("SELECT * FROM "+TABLE_NAME+" WHERE ");
				for (int i = 0; i < pylons.length; i++) {
					filterString.append("PYLON");
					filterString.append(pylons[i]);
					filterString.append(" = '");
					filterString.append(stores[i]);
					filterString.append("'");
					if (pylons.length > 1 && i < (pylons.length - 1)) {
						filterString.append(" AND ");
					}
				}
				filterStatement.execute(filterString.toString());
				return filterStatement.getResultSet();
				
			} else {
				StdOut.println("Problem with filter argument in ApprovedConfigsConnection");
				return null;
			}
		} catch (SQLException e) {
			StdOut.println("Exception in ApprovedConfigurationsConnection.filter()");
			e.printStackTrace();
		} 
		// shouldnt reach here
		return null;
	}

}
