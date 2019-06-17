package gui.model;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TableDataModel {
	public TableData readData(Connection conn, String dbTableName) throws SQLException {
	    try (
	        Statement stmnt = conn.createStatement() ;
	        ResultSet resultSet = stmnt.executeQuery("select * from "+dbTableName) ) {

	        ResultSetMetaData meta = resultSet.getMetaData() ;
	        List<String> colNames = new ArrayList<>();
	        for (int i = 1; i <= meta.getColumnCount(); i++) {
	            String columnName = meta.getColumnName(i);
	            colNames.add(columnName);                
	        }

	        List<Map<String, String>> data = new ArrayList<>();

	        while (resultSet.next()) {
	            Map<String, String> row = new HashMap<>();
	            for (String col : colNames) {
	                row.put(col, resultSet.getString(col));
	            }
	            data.add(row);
	        }

	        return new TableData(colNames, data);
	    }
	}

	public static class TableData {
	    private final List<String> columnNames ;
	    private final List<Map<String, String>> data ;

	    public TableData(List<String> columnNames, List<Map<String, String>> data) {
	        this.columnNames = columnNames ;
	        this.data = data ;
	    }

	    public List<String> getColumnNames() {
	        return columnNames;
	    }

	    public List<Map<String, String>> getData() {
	        return data;
	    }


	}
}
