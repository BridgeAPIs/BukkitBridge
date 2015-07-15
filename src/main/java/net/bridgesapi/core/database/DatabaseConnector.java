package net.bridgesapi.core.database;

import redis.clients.jedis.Jedis;
/**
 * @author zyuiop
 */
public interface DatabaseConnector {
    Jedis getResource();

    Jedis getBungeeResource();

    void killConnections();

    void initiateConnections();
}
