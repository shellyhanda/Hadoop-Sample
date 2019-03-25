package com.poc.phoneix;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class PhoneixConnection {
	public static void main(String[] args) {
        // Create variables
        Connection connection = null;
        Statement statement = null;
        ResultSet rs = null;
        PreparedStatement ps = null;

        try {
        	try {
				Class.forName("org.apache.phoenix.jdbc.PhoenixDriver");
				//Class.forName("org.apache.phoenix.queryserver.client.Driver");
				
			} catch (ClassNotFoundException e) {
				
				e.printStackTrace();
			}
            // Connect to the database
        	//String url="jdbc:phoenix:sandbox.hortonworks.com:2181";
        	//jdbc:phoenix:thin:url=http://queryserver.domain:8765;serialization=PROTOBUF;authentication=SPENGO;principal=phoenix@EXAMPLE.COM;keytab=/etc/security/keytabs/phoenix.keytab


        	String url="jdbc:phoenix:hdo4240-2.ca.boeing.com,hdo4240-3.ca.boeing.com,hdo4240-4.ca.boeing.com:2181";
        	//String url="jdbc:phoenix:thin:url=http://hdo4240-2.ca.boeing.com:8765;serialization=PROTOBUF";
        	//String url="jdbc:phoenix:thin:url=http://sandbox.hortonworks.com:8765;serialization=PROTOBUF";
        	System.out.println("Going to connect to URL="+url);
            connection = DriverManager.getConnection(url);
            System.out.println("Connection="+connection.isReadOnly());
            // Create a JDBC statement
            statement = connection.createStatement();

            // Execute our statements
            //statement.executeUpdate("create table javatest (mykey integer not null primary key, mycolumn varchar)");
           // statement.executeUpdate("upsert into javatest values (1,'Hello')");
           // statement.executeUpdate("upsert into javatest values (2,'Java Application')");
          //  connection.commit();

            // Query for table
            ps = connection.prepareStatement("select * from \"ecfd_dev:testssh\"");
            rs = ps.executeQuery();
            System.out.println("Table Values****************");
            while(rs.next()) {
                Integer myKey = rs.getInt(1);
                String myColumn = rs.getString(2);
                System.out.println("\tRow: " + myKey + " = " + myColumn);
            }
        }
        catch(SQLException e) {
            e.printStackTrace();
        }
        finally {
            if(ps != null) {
                try {
                    ps.close();
                }
                catch(Exception e) {}
            }
            if(rs != null) {
                try {
                    rs.close();
                }
                catch(Exception e) {}
            }
            if(statement != null) {
                try {
                    statement.close();
                }
                catch(Exception e) {}
            }
            if(connection != null) {
                try {
                    connection.close();
                }
                catch(Exception e) {}
            }
        }
    }
}