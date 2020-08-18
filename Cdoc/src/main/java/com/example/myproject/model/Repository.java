package com.example.myproject.model;

import java.sql.*;
import java.util.*;
/*
class ConStmt{
	Connection c;
	Statement s;
	public ConStmt(Connection c,Statement s) {
		this.c = c;
		this.s = s;
	}
	public Connection getC() {
		return c;
	}
	public void setC(Connection c) {
		this.c = c;
	}
	public Statement getS() {
		return s;
	}
	public void setS(Statement s) {
		this.s = s;
	}
}
*/
// 数据库
public class Repository {
	static final String JDBC_DRIVER = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
	static final String DB_URL = "jdbc:sqlserver://localhost:1433;DatabaseName=Cdoc";
	
//	static private Map<Integer,ConStmt> connections = new HashMap<>();
//	static private int count = 0;
	
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
			con = DriverManager.getConnection(DB_URL,USER,PWD);
			stmt = con.createStatement();
			System.out.println("Database connect successful.");
		} catch(Exception e) {
		}
	}
/*
	private static boolean connectDB() {
		try {
			Class.forName(JDBC_DRIVER);
			// 连接数据库
			Connection con = DriverManager.getConnection(DB_URL,USER,PWD);
			Statement stmt = con.createStatement();
			if(connections.size() > 50) {
				closeAllConnections();
			}
			
			connections.put(count++,new ConStmt(con, stmt) );
			
			return true;
		}catch(Exception e){
			e.printStackTrace();
			System.out.println("Database connect error.");
			return false;
		}
	}
*/	/*
	private static void closeAllConnections() {
		for(int i=0; i<connections.size(); i++) {
			Connection con = connections.get(i).c;
			Statement stmt = connections.get(i).s;
			try{
				if(con!=null)
					con.close();
				if(stmt!=null)
					stmt.close();
				con = null; stmt = null;
			} catch(Exception e) {
			}
		}
		connections.clear();
		count = 0;
	}*/
	/**
	 * 执行sql查找语句
	 * @param sql sql语句
	 * @return 查找结果
	 * @throws Exception
	 */
	public ResultSet doSqlSelectStatement(String sql) throws Exception {
		if(stmt.isClosed() || !con.isValid(1000)) {
			if(stmt!=null)
				stmt.close();
			if(con!=null)
				con.close();
			stmt = null;
			con = null;
			
			Class.forName(JDBC_DRIVER);
			con = DriverManager.getConnection(DB_URL,USER,PWD);
			System.out.println("Database connect again. In SELECT");
			stmt = con.createStatement();
		}
		return stmt.executeQuery(sql);
	}

	/**
	 * 执行sql插入、更新语句
	 * @param sql sql语句
	 * @return 执行结果
	 */
	public boolean doSqlUpdateStatement(String sql) {
		try {
			if(stmt.isClosed() || !con.isValid(1000)) {
				if(stmt!=null)
					stmt.close();
				if(con!=null)
					con.close();
				stmt = null;
				con = null;
				
				Class.forName(JDBC_DRIVER);
				con = DriverManager.getConnection(DB_URL,USER,PWD);
				System.out.println("Database connect again. In UPDATE");
				stmt = con.createStatement();
			}
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
