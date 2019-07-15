/**
 * Copyright (c) 2017 Netflix, Inc.  All rights reserved.
 */
package com.netflix.ndbench.plugin.cass;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.PoolingOptions;
import com.datastax.driver.core.HostDistance;
import com.datastax.driver.core.policies.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author vchella
 */
public class CassJavaDriverManagerImpl implements CassJavaDriverManager {
    private static final Logger logger = LoggerFactory.getLogger(CassJavaDriverManagerImpl.class);

    Cluster cluster;
    Session session;

    @Override
    public Cluster registerCluster(String clName, String contactPoint, int connections, int port) {
        return registerCluster(clName,contactPoint,connections,port,null,null);
    }
        @Override
    public Cluster registerCluster(String clName, String contactPoint, int connections, int port, String username, String password) {
    
        logger.info("[MAGRAO] Contact Point: " + contactPoint);
        logger.info("[MAGRAO] ClusterName: " + clName);
        cluster = Cluster
          .builder()
          .addContactPoint("192.168.250.10")
          .build();
        return cluster;
    }

    @Override
    public Session getSession(Cluster cluster) {
        logger.info("[MAGRAO] preparando pra conectar no cluster...");
        try {
          session = cluster.connect();
        } catch(Exception e) {
          logger.info("[MAGRAO] DAMNN!!...");
          logger.info(e.toString());
          cluster = Cluster
            .builder()
            .addContactPoint("192.168.250.10")
            .build();
          cluster.getConfiguration().getSocketOptions().setReadTimeoutMillis(1000000);
          return cluster.connect();
        }
          return session;
    }

    @Override
    public void shutDown() {
        cluster.close();
    }
}
