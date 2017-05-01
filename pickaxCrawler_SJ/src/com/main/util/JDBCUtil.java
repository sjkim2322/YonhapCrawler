package com.main.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class JDBCUtil {
	
	public static Connection connect() throws ClassNotFoundException, SQLException{
		Connection conn = null;
		Class.forName("");
		conn = DriverManager.getConnection("", "", "");
		
		return conn;
	}
}
