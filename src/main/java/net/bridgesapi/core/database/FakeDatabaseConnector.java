package net.bridgesapi.core.database;

import redis.clients.jedis.Jedis;

/**
 * @author zyuiop
 */
public class FakeDatabaseConnector implements DatabaseConnector {
    @Override
    public Jedis getResource() {
        return new FakeJedis();
    }

    @Override
    public Jedis getBungeeResource() {
        return new FakeJedis();
    }

    @Override
    public void killConnections() {

    }

    @Override
    public void initiateConnections() {

    }
}
