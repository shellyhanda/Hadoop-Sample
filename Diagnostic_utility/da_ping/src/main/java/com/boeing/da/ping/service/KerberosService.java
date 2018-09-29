package com.boeing.da.ping.service;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.security.UserGroupInformation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.hadoop.hbase.HbaseTemplate;
import org.springframework.stereotype.Service;

@Service
public class KerberosService {

  private static final Logger LOGGER = LoggerFactory.getLogger(KerberosService.class);

  @Autowired
  private HbaseTemplate hbaseTemplate;

  @Value("${kerberos.user.principal}")
  private String krbUserPrincipal;

  @Value("${kerberos.user.keytab}")
  private String krbUserKeytab;

  public void kinit() {
    Configuration config = hbaseTemplate.getConfiguration();

    try {
      UserGroupInformation.setConfiguration(config);
      UserGroupInformation userGroupInformation = UserGroupInformation.loginUserFromKeytabAndReturnUGI(krbUserPrincipal, krbUserKeytab);
      UserGroupInformation.setLoginUser(userGroupInformation);
      LOGGER.info("Kerberos login: SUCCESS");
    } catch (IOException e) {
      LOGGER.error("Kerberos login: FAILED");
      LOGGER.debug("Kerberos login: FAILED", e);
    }
  }

}
