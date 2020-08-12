package com.example.myproject.model;

import java.sql.*;
import java.util.*;

// 数据库
public class Repository {
	static final String JDBC_DRIVER = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
	static final String DB_URL = "jdbc:sqlserver://localhost:1433;DatabaseName=Cdoc";
	
	static final String USER = "JavaSql";
	static final String PWD = "JavaSql";
	
	private static Repository instance = new Repository(); 
	private Connection con = null;
	private Statement stmt = null;
	
	// 单例模式

	/**
	 * 单例模式，获取类
	 * @return 类
	 */
	public static Repository getInstance() {
		return instance;
	}
	
	// 连接数据库
	private Repository() {
		try {
			Class.forName(JDBC_DRIVER);
			// 连接数据库
			con = DriverManager.getConnection(DB_URL,USER,PWD);
			System.out.println("Database connect successful.");
			stmt = con.createStatement();
		}catch(Exception e){
			e.printStackTrace();
			System.out.println("Database connect error.");
		}
	}


	/**
	 * 执行sql查找语句
	 * @param sql sql语句
	 * @return 查找结果
	 * @throws Exception
	 */
	public ResultSet doSqlSelectStatement(String sql) throws Exception {
		return stmt.executeQuery(sql);
	}

	/**
	 * 执行sql插入、更新语句
	 * @param sql sql语句
	 * @return 执行结果
	 */
	public boolean doSqlUpdateStatement(String sql) {
		try {
			stmt.executeUpdate(sql);
			return true;
		}catch(Exception e) {
			e.printStackTrace();
			return false;
		}
	}


	/**
	 * 终止和数据库的连接
	 */
	public void closeConnect() {
		try{
			if(con != null)
				con.close();
			if(stmt != null)
				stmt.close();
		}catch(Exception e) {
			// SQL Exception
			e.printStackTrace();
		}
	}
}
