package com.boeing.da.ping;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.hadoop.hbase.HbaseTemplate;
import org.springframework.stereotype.Service;

import com.boeing.da.ping.service.KerberosService;

@Service
public class PhoenixConnectivity {

  private static final Logger LOGGER = LoggerFactory.getLogger(PhoenixConnectivity.class);

  @Autowired
  private KerberosService kerberosService;

  @Autowired
  private HbaseTemplate hbaseTemplate;

  @Value("${phoenix.jdbc.url}")
  private String jdbcUrl;

  @Value("${phoenix.test.upsert}")
  private String testUpsertQuery;

  @Value("${kerberos.user.principal}")
  private String krbUserPrincipal;

  public void run(String[] args) {
    kerberosService.kinit();

    try {
      Class.forName("org.apache.phoenix.jdbc.PhoenixDriver");
      LOGGER.info("Finding PhoenixDriver: SUCCESS");
    } catch (ClassNotFoundException e) {
      LOGGER.error("Finding PhoenixDriver: FAILED");
      LOGGER.debug("Finding PhoenixDriver: FAILED", e);
    }

    try (Connection connection = DriverManager.getConnection(jdbcUrl, getConnectionProperties())) {

      LOGGER.info("Phoenix connection: SUCCESS");

      createTestTable(connection);

      testUpsert(connection, "upsert into phoenixtest values (11,'Hello')");
      testUpsert(connection, "upsert into phoenixtest values (61,'Java Application')");

      testUpsert(connection, testUpsertQuery);

      dropTable(connection);
    } catch (SQLException e) {
      LOGGER.error("Phoenix connection: FAILED");
      LOGGER.debug("Phoenix connection: FAILED", e);
    }
  }

  private Properties getConnectionProperties() {
    Properties props = new Properties();

    props.setProperty("zookeeper.znode.parent", hbaseTemplate.getConfiguration().get("zookeeper.znode.parent"));
    props.setProperty("hbase.zookeeper.quorum", hbaseTemplate.getConfiguration().get("hbase.zookeeper.quorum"));
    props.setProperty("hbase.zookeeper.property.clientPort", hbaseTemplate.getConfiguration().get("hbase.zookeeper.property.clientPort"));
    props.setProperty("hbase.master.kerberos.principal", hbaseTemplate.getConfiguration().get("hbase.master.kerberos.principal"));
    props.setProperty("hbase.regionserver.kerberos.principal", hbaseTemplate.getConfiguration().get("hbase.regionserver.kerberos.principal"));
    props.setProperty("hbase.security.authentication", hbaseTemplate.getConfiguration().get("hbase.security.authentication"));
    props.setProperty("phoenix.queryserver.kerberos.principal", krbUserPrincipal);
    props.setProperty("hadoop.security.authentication", hbaseTemplate.getConfiguration().get("hadoop.security.authentication"));
    props.setProperty("hadoop.security.authorization", hbaseTemplate.getConfiguration().get("hadoop.security.authorization"));

    return props;
  }

  private void createTestTable(Connection connection) {

    try (Statement statement = connection.createStatement()) {
      statement.executeUpdate("create table if not exists phoenixtest (mykey integer not null primary key, mycolumn varchar)");

      connection.commit();
      LOGGER.info("Creating phoenixtest table: SUCCESS");
    } catch (SQLException e) {
      LOGGER.error("Creating phoenixtest table: FAILED");
      LOGGER.debug("Creating phoenixtest table: FAILED", e);
    }
  }

  private void dropTable(Connection connection) {

    try (Statement statement = connection.createStatement()) {
      statement.executeUpdate("drop table if exists phoenixtest");

      connection.commit();
      LOGGER.info("Dropping phoenixtest table: SUCCESS");
    } catch (SQLException e) {
      LOGGER.error("Dropping phoenixtest table: FAILED");
      LOGGER.debug("Dropping phoenixtest table: FAILED", e);
    }
  }

  private void testUpsert(Connection connection, String upsertQuery) {
    try (Statement statement = connection.createStatement()) {
      int rowsUpdated = statement.executeUpdate(upsertQuery);
      connection.commit();

      LOGGER.info(upsertQuery + " - " + rowsUpdated + " : SUCCESS");
    } catch (SQLException e) {
      LOGGER.error(upsertQuery + ": FAILED");
      LOGGER.debug(upsertQuery + ": FAILED", e);
    }
  }

}
