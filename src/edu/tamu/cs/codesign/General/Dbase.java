package edu.tamu.cs.codesign.General;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class Dbase {
	
	Connection conn = null;
	Statement stmt = null;
	String JDBC_DRIVER = CONF.__JDBC_DRIVER;
	String DB_URL = CONF.__DB_URL;
	String USER = CONF.__SQLUSER;
	String PASS = CONF.__SQLPASS;
	String sql;
	String dbName = CONF.__SQLDBNAME;
	
	Dbase() {
		
	}
	
	public Connection connect() {
		try {
			Class.forName(JDBC_DRIVER);
		}
		catch(Exception e) {
			return null;
		}
		
		try {
			conn = DriverManager.getConnection(DB_URL, USER, PASS);
		}
		catch (Exception e){
			return null;
		}
		return conn;
	}
	
	

}
