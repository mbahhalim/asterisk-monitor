package com.lenkp.asteriskmonitor.service;

import java.sql.Connection;
import java.sql.ResultSet;

public interface DatabaseService {

	public void setDbPath(String dbPath);
	public String getDbPath();
	public Connection dbConnection (String databasePath);
	public ResultSet selectAllFromTable (String tableName);
	public void updatePTUTable (String tableName, String sipId, String hostId, String ptuUsername, String ptuPassword);
	public void deleteDataPTUTable (String tableName, String sipId);
	public void insertDataPTUTable (String tableName, String sipId, String hostId, String ptuUsername, String ptuPassword);
	
}
