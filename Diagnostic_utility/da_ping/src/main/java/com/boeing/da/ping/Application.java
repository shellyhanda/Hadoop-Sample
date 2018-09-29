package com.boeing.da.ping;

import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;

@SpringBootApplication
@EnableAutoConfiguration
@ImportResource("classpath:/hbase-config.xml")
public class Application implements CommandLineRunner {

  private static final Logger LOGGER = LoggerFactory.getLogger(Application.class);

  @Autowired
  private HbaseConnectivity hbaseConnectivity;

  @Autowired
  private PhoenixConnectivity phoenixConnectivity;

  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }

  @Override
  public void run(String... cmdArgs) throws Exception {
    if (cmdArgs != null && cmdArgs.length >= 2) {
      String[] args = Arrays.copyOfRange(cmdArgs, 1, cmdArgs.length);
      if (cmdArgs[0].equalsIgnoreCase("hbase")) {
        hbaseConnectivity.run(args);
      } else if (cmdArgs[0].equalsIgnoreCase("phoenix")) {
        phoenixConnectivity.run(args);
      }
    }
  }
}
