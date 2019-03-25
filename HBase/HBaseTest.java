package hbase;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.KeyValue;
import org.apache.hadoop.hbase.MasterNotRunningException;
import org.apache.hadoop.hbase.ZooKeeperConnectionException;
import org.apache.hadoop.hbase.client.Delete;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.security.UserGroupInformation;

public class HBaseTest {

	private static Configuration conf = null;

	/**
	 * Initialization
	 */
	static {
		conf = HBaseConfiguration.create();
		conf.set("hadoop.security.authentication", "kerberos");
		conf.set("hadoop.security.authorization", "true");
		conf.set("hbase.security.authentication", "kerberos");
		conf.set("hbase.security.authorization", "true");
		conf.set("hbase.zookeeper.quorum",
				"ec2-52-36-200-220.us-west-2.compute.amazonaws.com,ec2-52-37-201-37.us-west-2.compute.amazonaws.com,ec2-52-37-201-77.us-west-2.compute.amazonaws.com");
		conf.set("hbase.zookeeper.property.clientPort", "2181");
		conf.set("zookeeper.znode.parent", "/hbase-secure");
		conf.set("hbase.master.kerberos.principal", "hbase/_HOST@HCL.COM");
		conf.set("hbase.regionserver.kerberos.principal", "hbase/_HOST@HCL.COM");
		try {
			UserGroupInformation.setConfiguration(conf);
			UserGroupInformation userGroupInformation = UserGroupInformation
					.loginUserFromKeytabAndReturnUGI("hbaseuser@HCL.COM", "/home/ubuntu/hbaseuser.keytab");
			UserGroupInformation.setLoginUser(userGroupInformation);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Create a table
	 */
	public static void creatTable(String tableName, String[] familys) throws Exception {

		HBaseAdmin admin = new HBaseAdmin(conf);
		if (admin.tableExists(tableName)) {
			System.out.println("table already exists!");
		} else {
			HTableDescriptor tableDesc = new HTableDescriptor(tableName);
			for (int i = 0; i < familys.length; i++) {
				tableDesc.addFamily(new HColumnDescriptor(familys[i]));
			}
			admin.createTable(tableDesc);
			System.out.println("create table " + tableName + " ok.");
		}
	}

	/**
	 * Delete a table
	 */
	public static void deleteTable(String tableName) throws Exception {
		try {
			HBaseAdmin admin = new HBaseAdmin(conf);
			admin.disableTable(tableName);
			admin.deleteTable(tableName);
			System.out.println("delete table " + tableName + " ok.");
		} catch (MasterNotRunningException e) {
			e.printStackTrace();
		} catch (ZooKeeperConnectionException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Put (or insert) a row
	 */
	public static void addRecord(String tableName, String rowKey, String family, String qualifier, String value)
			throws Exception {
		try {
			HTable table = new HTable(conf, tableName);
			Put put = new Put(Bytes.toBytes(rowKey));
			put.add(Bytes.toBytes(family), Bytes.toBytes(qualifier), Bytes.toBytes(value));
			table.put(put);
			System.out.println("insert recored " + rowKey + " to table " + tableName + " ok.");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Delete a row
	 */
	public static void delRecord(String tableName, String rowKey) throws IOException {
		HTable table = new HTable(conf, tableName);
		List<Delete> list = new ArrayList<Delete>();
		Delete del = new Delete(rowKey.getBytes());
		list.add(del);
		table.delete(list);
		System.out.println("del recored " + rowKey + " ok.");
	}

	/**
	 * Get a row
	 */
	public static void getOneRecord(String tableName, String rowKey) throws IOException {
		HTable table = new HTable(conf, tableName);
		Get get = new Get(rowKey.getBytes());
		Result rs = table.get(get);
		for (KeyValue kv : rs.raw()) {
			System.out.print(new String(kv.getRow()) + " ");
			System.out.print(new String(kv.getFamily()) + ":");
			System.out.print(new String(kv.getQualifier()) + " ");
			System.out.print(kv.getTimestamp() + " ");
			System.out.println(new String(kv.getValue()));
		}
	}

	/**
	 * Scan (or list) a table
	 */
	public static int getAllRecord(String tableName) {
		int count = 0;
		try {
			HTable table = new HTable(conf, tableName);
			Scan s = new Scan();
			s.setBatch(10);
			ResultScanner ss = table.getScanner(s);
			
			for (Result r : ss) {
				for (KeyValue kv : r.raw()) {
					new String(kv.getRow());
					new String(kv.getFamily());
					new String(kv.getQualifier());
					new String(kv.getValue());
				}
				count++;
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
		return count;
	}

	public static void main(String[] agrs) {
		try {
			String tablename = "report";
			String[] familys = { "data" };
			try {
				UserGroupInformation.setConfiguration(conf);
				UserGroupInformation userGroupInformation = UserGroupInformation
						.loginUserFromKeytabAndReturnUGI("hbaseuser@HCL.COM", "/home/ubuntu/hbaseuser.keytab");
				UserGroupInformation.setLoginUser(userGroupInformation);
			} catch (IOException e) {
				e.printStackTrace();
			}
			/*
			 * HBaseTest.creatTable(tablename, familys);
			 * 
			 * // add record zkb HBaseTest.addRecord(tablename, "zkb", "grade",
			 * "", "5"); HBaseTest.addRecord(tablename, "zkb", "course", "",
			 * "90"); HBaseTest.addRecord(tablename, "zkb", "course", "math",
			 * "97"); HBaseTest.addRecord(tablename, "zkb", "course", "art",
			 * "87"); // add record baoniu HBaseTest.addRecord(tablename,
			 * "baoniu", "grade", "", "4"); HBaseTest.addRecord(tablename,
			 * "baoniu", "course", "math", "89");
			 * 
			 * System.out.println("===========get one record========");
			 * HBaseTest.getOneRecord(tablename, "zkb");
			 * 
			 * System.out.println("===========show all record========");
			 * HBaseTest.getAllRecord(tablename);
			 * 
			 * System.out.println("===========del one record========");
			 * HBaseTest.delRecord(tablename, "baoniu");
			 * HBaseTest.getAllRecord(tablename);
			 * 
			 * System.out.println("===========show all record========");
			 */
			HBaseTest.getAllRecord(tablename);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
