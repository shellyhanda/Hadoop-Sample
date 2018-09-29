package com.boeing.da.ping;

import java.io.IOException;
import java.util.Iterator;

import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.hadoop.hbase.HbaseTemplate;
import org.springframework.data.hadoop.hbase.ResultsExtractor;
import org.springframework.stereotype.Service;

import com.boeing.da.ping.service.KerberosService;

@Service
public class HbaseConnectivity {

  private static final Logger LOGGER = LoggerFactory.getLogger(HbaseConnectivity.class);

  @Autowired
  private HbaseTemplate hbaseTemplate;

  @Autowired
  private KerberosService kerberosService;

  @Value("${table.name}")
  private String tableName;

  public void run(String... args) {
    kerberosService.kinit();

    if (StringUtils.isBlank(tableName)) {
      tableName = "test";
    }

    Connection conn = null;
    try {
      conn = ConnectionFactory.createConnection(hbaseTemplate.getConfiguration());
      LOGGER.info("HBase connection: SUCCESS");
    } catch (IOException e) {
      LOGGER.error("HBase connection: FAILED");
      LOGGER.debug("HBase connection: FAILED", e);
      return;
    }
    Admin hAdmin = null;
    try {
      hAdmin = conn.getAdmin();
      LOGGER.info("HBase admin access: SUCCESS");
    } catch (IOException e) {
      LOGGER.error("HBase admin access: FAILED");
      LOGGER.debug("HBase admin access: FAILED", e);
      return;
    }

    try {
      LOGGER.info("Reading HBase tables list...");
      for (TableName tableName : hAdmin.listTableNames()) {
        LOGGER.info(tableName.getNameAsString());
      }
      LOGGER.info("Reading HBase tables list: SUCCESS");
    } catch (IOException e) {
      LOGGER.error("Reading HBase tables list: FAILED");
      LOGGER.debug("Reading HBase tables list: FAILED", e);
    }

    HTableDescriptor hTableDesc = new HTableDescriptor(TableName.valueOf(tableName));
    hTableDesc.addFamily(new HColumnDescriptor("cf1"));

    try {
      hAdmin.createTable(hTableDesc);
      LOGGER.info("Creating table '" + tableName + "': SUCCESS");
    } catch (Exception e) {
      LOGGER.error("Creating table '" + tableName + "': FAILED");
      LOGGER.debug("Creating table '" + tableName + "': FAILED", e);
    }

    try {
      hAdmin.disableTable(TableName.valueOf(tableName));
      hAdmin.deleteTable(TableName.valueOf(tableName));
      LOGGER.info("Deleting table '" + tableName + "': SUCCESS");
    } catch (Exception e) {
      LOGGER.error("Deleting table '" + tableName + "': FAILED");
      LOGGER.debug("Deleting table '" + tableName + "': FAILED", e);
    }

    if (!StringUtils.isBlank(args[0]) && StringUtils.isAlphanumeric(args[0])) {

      LOGGER.info("Counting rows in table " + args[0] + " ...");
      try {
        int count = hbaseTemplate.find(args[0], new Scan(), new ResultsExtractor<Integer>() {

          @Override
          public Integer extractData(ResultScanner scanner) throws Exception {

            int count = 0;
            for (Iterator<Result> iterator = scanner.iterator(); iterator.hasNext();) {
              Result result = iterator.next();

              count++;
            }
            return count;

          }
        });
        LOGGER.info("Number of rows: " + count);
        LOGGER.info("Counting rows in table " + args[0] + ": SUCCESS");
      } catch (Exception ex) {
        LOGGER.error("Counting rows in table " + args[0] + ": FAILED");
        LOGGER.debug("Counting rows in table " + args[0] + ": FAILED", ex);
      }

    }

    try {
      conn.close();
    } catch (Exception e) {
      // do nothing.
    }
  }

}
