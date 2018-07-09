package com.shelly.poc.hive;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import org.apache.hive.jdbc.HiveDriver;
public class SelectQuery {

	//private static String driverName = "org.apache.hadoop.hive.jdbc.HiveDriver";
	private static String driverName = "org.apache.hive.jdbc.HiveDriver";
	public static void main(String[] args) {
		// Register driver and create driver instance
	      try {
			Class.forName(driverName);
			
	
	      // get connection
	      Connection con = DriverManager.getConnection("jdbc:hive2://127.0.0.1:10000/shellytest", "", "");
	      
	      // create statement
	      Statement stmt = con.createStatement();
	      
	      // execute statement
	      ResultSet res = stmt.executeQuery("select BEMSID.Query as Query,BEMSID.Type as Type,BEMSID.Value as Value,BEMSID.Label as Label from state_vector_serde");
	      
	      System.out.println("Result:");
	      System.out.println(" ID \t Name \t Salary \t Designation \t Dept ");
	      
	      while (res.next()) {
	    	  System.out.println(res);
	         System.out.println(res.getString(1) + " " + res.getString(2) + " " + res.getString(3) + " " + res.getString(4));
	      }
	      con.close();
	  	} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	      
	   }
	}
