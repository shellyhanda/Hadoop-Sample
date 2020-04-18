package com.demo.hbase;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.security.UserGroupInformation;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import com.google.protobuf.ServiceException;

// Connecting to Kerberos secured HBase cluster
// Hortonworks - HBase 1.1.2.2.4.2.11-1 and Hadoop 2.7.3
public class HBaseClient {
	public static void main(String[] args) throws IOException, ServiceException {
		Logger.getRootLogger().setLevel(Level.DEBUG);
		System.out.println("START................................");
		Configuration configuration = HBaseConfiguration.create();
		
		// Zookeeper quorum
		configuration.set("hbase.zookeeper.quorum", "mho201-2.ca.boeing.com,mho201-3.ca.boeing.com,hdo201-4.ca.boeing.com");//hdo4240-2.ca.boeing.com,hdo4240-3.ca.boeing.com,hdo4240-4.ca.boeing.com
		configuration.set("hbase.zookeeper.property.clientPort", "2181");
		configuration.set("hadoop.security.authentication", "kerberos");
		configuration.set("hbase.security.authentication", "kerberos");
		//configuration.set("hbase.cluster.distributed", "true");
		configuration.set("zookeeper.znode.parent", "/hbase-secure");
		configuration.set("hbase.client.retries.number", "3");
		configuration.set("zookeeper.session.timeout", "60000");
		configuration.set("zookeeper.recovery.retry", "3");

		// check this setting on HBase side
		//configuration.set("hbase.rpc.protection", "authentication"); 

		//what principal the master/region. servers use.
		configuration.set("hbase.regionserver.kerberos.principal", "hbase/_HOST@NOS.BOEING.COM"); 
		//configuration.set("hbase.regionserver.keytab.file", "src/hbase.service.keytab"); 
		
		// this is needed even if you connect over rpc/zookeeper
		configuration.set("hbase.master.kerberos.principal", "hbase/_HOST@NOS.BOEING.COM"); 
		//configuration.set("hbase.master.keytab.file", "src/hbase.service.keytab");
		
		//System.setProperty("java.security.krb5.conf","src/krb5.conf");
		// Enable/disable krb5 debugging 
		//System.setProperty("sun.security.krb5.debug", "false");

		String principal = System.getProperty("kerberosPrincipal","svchdpprdbdapapp/hdo202-18.ca.boeing.com@NOS.BOEING.COM");//svchdphdo4ecfd/hdo4241-23.ca.boeing.com@NOS.BOEING.COM
		String keytabLocation = System.getProperty("kerberosKeytab","/data/home/svchdpprdbdapapp/svchdpprdbdapapp.service.keytab");///etc/security/keytabs/svchdphdo4ecfd.service.keytab

		// kinit with principal and keytab
		UserGroupInformation.setConfiguration(configuration);
		System.out.println("Config added................");
		UserGroupInformation.loginUserFromKeytab(principal, keytabLocation);
		System.out.println("UserGroupInformation added................");
		Connection connection = ConnectionFactory.createConnection(HBaseConfiguration.create(configuration));
		System.out.println("connection..........."+connection);
		 Admin hAdmin =connection.getAdmin();
		System.out.println("cddw:app_table_secure Table FOUND -->" + hAdmin.isTableAvailable(TableName.valueOf("cddw:app_table_secure")));
		 try {
			 System.out.println("Reading HBase tables list...");
		      for (TableName tableName : hAdmin.listTableNames()) {
		    	  System.out.println(tableName.getNameAsString());
		      }
		      System.out.println("Reading HBase tables list: SUCCESS");
		    } catch (IOException e) {
		    	System.out.println("Reading HBase tables list: FAILED");
		    	e.printStackTrace();
		    }
		System.out.println("END...............");
	}
}
