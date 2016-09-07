package com.lenkp.asteriskmonitor.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Logger;

public class DatabaseServiceProvider implements DatabaseService {

private String dbPath;
	
	public final static Logger LOGGER =
			Logger.getLogger(DatabaseServiceProvider.class.getName());
	
	@Override
	public Connection dbConnection(String databasePath) {
		LOGGER.info("connecting to PTUInfo DataBase");
		
        String url = "jdbc:sqlite:"+databasePath;
        System.out.println(url);
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
        	System.out.println("notfound");
            System.out.println(e.getMessage());
        }
        return conn;
	}

	@Override
	public ResultSet selectAllFromTable(String tableName) {
		LOGGER.info("select all from "+tableName);
		
		String sql = "SELECT * FROM " + tableName;
		ResultSet rs = null;
				
		try{
			Connection conn = this.dbConnection(this.dbPath);
            Statement stmt  = conn.createStatement();
            rs = stmt.executeQuery(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
		
		return rs;
	}

	public String getDbPath() {
		return dbPath;
	}

	public void setDbPath(String dbPath) {
		this.dbPath = dbPath;
	}

	@Override
	public void updatePTUTable(String tableName, String sipId, String hostId, String ptuUsername, String ptuPassword) {
		String sql = "UPDATE "+tableName+" SET "
						+ "hostId = \""+hostId+"\" , "
						+ "ptuUsername = \""+ptuUsername+"\" , "
						+ "ptuPassword = \""+ptuPassword+"\" WHERE "
						+ "sipId = "+sipId;
		
		try (Connection conn = this.dbConnection(this.dbPath);
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
            // update 
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
		
	}

	@Override
	public void deleteDataPTUTable(String tableName, String sipId) {
		String sql = "DELETE FROM "+tableName+" WHERE sipId = "+sipId;
		
		try (Connection conn = this.dbConnection(this.dbPath);
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.executeUpdate();
 
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
		
	}

	@Override
	public void insertDataPTUTable(String tableName, String sipId, String hostId, String ptuUsername, String ptuPassword) {
		String sql = "INSERT INTO "+tableName+" VALUES ( "+sipId+", \""+hostId+"\", \""+ptuUsername+"\", \""+ptuPassword+"\")";
		
		try (Connection conn = this.dbConnection(this.getDbPath());
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
	}

}
