package com.epam.task04.lib.dao.utils.db;

import com.epam.task04.lib.dao.utils.db.exception.ConnectionPoolException;
import com.epam.task04.lib.dao.utils.db.resource.DBResourceManager;
import com.epam.task04.lib.exception.InitializationException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;

/**
 * Class contains pool of opened connections to database
 */
public final class ConnectionPool {

    /**
     * Object of logger
     */
    private static final Logger logger = LogManager.getLogger();

    private static ConnectionPool instance;

    /**
     * Collection of available opened connections
     */
    private BlockingQueue<Connection> connectionsQueue;

    /**
     * Collection of connections given to work with
     */
    private BlockingQueue<Connection> givenAwayConnectionsQueue;

    private final String USER = "db.user";
    private final String PASSWORD = "db.password";
    private final String URL = "db.url";
    private final String POOL_SIZE = "db.poolsize";

    private final int DEFAULT_POOL_SIZE = 5;

    private String url;
    private String userName;
    private String password;
    private int poolSize;

    private final String SQL_EXCEPTION = "SQL exception during executing";
    private final String INIT_QUEUE_EXCEPTION = "Queue to close isn't initialized";
    private final String ALREADY_CLOSED_CONNECTION_EXCEPTION = "Can't close already closed connection";
    private final String RETURN_CONNECTION_EXCEPTION = "Exception while executing returning connection to pool";
    private final String REMOVE_CONNECTION_EXCEPTION = "Exception while executing removing from pool of given connections";

    /**
     * Constructor gets parameters from .resource file by object of special class
     */
    private ConnectionPool() {
        DBResourceManager dbResourceManager = DBResourceManager.getInstance();
        this.url = dbResourceManager.getString(URL);
        this.userName = dbResourceManager.getString(USER);
        this.password = dbResourceManager.getString(PASSWORD);
        try {
            this.poolSize = Integer.parseInt(dbResourceManager.getString(POOL_SIZE));
        } catch (NumberFormatException e) {
            this.poolSize = DEFAULT_POOL_SIZE;
        }
    }

    /**
     * Singleton implementation
     */
    public static ConnectionPool getInstance() {
        if (instance == null) {
            instance = new ConnectionPool();
        }

        return instance;
    }

    /**
     * Method creates connections and adds them to collection
     *
     * @throws ConnectionPoolException if there are errors of creating connections
     */
    public void initConnectionPool() throws ConnectionPoolException {
        Locale.setDefault(Locale.ENGLISH);
        try {
            if (connectionsQueue == null && givenAwayConnectionsQueue == null) {
                givenAwayConnectionsQueue = new ArrayBlockingQueue<>(poolSize);
                connectionsQueue = new ArrayBlockingQueue<>(poolSize);
                for (int i = 0; i < poolSize; i++) {
                    Connection connection = DriverManager.getConnection(url, userName, password);
                    PoolConnection poolConnection = new PoolConnection(connection);
                    connectionsQueue.add(poolConnection);
                }
            }
        } catch (SQLException e) {
            throw new ConnectionPoolException(SQL_EXCEPTION, e);
        }
    }

    /**
     * Method returns connection to method, that call it
     *
     * @return connection
     * @throws ConnectionPoolException if there are errors of taking connections
     */
    public Connection takeConnection() throws ConnectionPoolException {
        Connection connection = null;
        try {
            connection = connectionsQueue.take();
            givenAwayConnectionsQueue.add(connection);
        } catch (InterruptedException e) {
            throw new ConnectionPoolException(e);
        }
        return connection;
    }

    public boolean clearConnections() {
        try {
            closeConnections(connectionsQueue);
            closeConnections(givenAwayConnectionsQueue);
            return true;
        } catch (InitializationException | SQLException e) {
            logger.error(e);
            return false;
        }
    }

    /**
     * Method closes all opened connections in list of available connections
     *
     * @param queue collection of connections to close
     * @throws SQLException            if there are SQL errors
     * @throws InitializationException if there are not initialized arguments
     */
    public void closeConnections(BlockingQueue<Connection> queue) throws SQLException, InitializationException {
        if (queue == null) {
            throw new InitializationException(INIT_QUEUE_EXCEPTION);
        }

        Connection connection;
        while ((connection = queue.poll()) != null) {
            if (!connection.getAutoCommit()) {
                connection.commit();
            }
            ((PoolConnection) connection).permanentClose();
        }
    }

    /**
     * Class-shell that implements interface Connections and overrides method close
     */
    private class PoolConnection implements Connection {
        private Connection connection;

        public PoolConnection(Connection connection) throws SQLException {
            this.connection = connection;
            this.connection.setAutoCommit(true);
        }

        /**
         * Method closes current connection
         *
         * @throws SQLException if there are SQL errors
         */
        public void permanentClose() throws SQLException {
            connection.close();
        }

        /**
         * Method return current connection to collection of available connections
         *
         * @throws SQLException if there are SQL errors
         */
        @Override
        public void close() throws SQLException {
            if (connection.isClosed()) {
                throw new SQLException(ALREADY_CLOSED_CONNECTION_EXCEPTION);
            }

            if (connection.isReadOnly()) {
                connection.setReadOnly(false);
            }

            if (!connectionsQueue.offer(this)) {
                throw new SQLException(RETURN_CONNECTION_EXCEPTION);
            }

            if (!givenAwayConnectionsQueue.remove(this)) {
                throw new SQLException(REMOVE_CONNECTION_EXCEPTION);
            }
        }

        @Override
        public Statement createStatement() throws SQLException {
            return connection.createStatement();
        }

        @Override
        public PreparedStatement prepareStatement(String sql) throws SQLException {
            return connection.prepareStatement(sql);
        }

        @Override
        public CallableStatement prepareCall(String sql) throws SQLException {
            return connection.prepareCall(sql);
        }

        @Override
        public String nativeSQL(String sql) throws SQLException {
            return connection.nativeSQL(sql);
        }

        @Override
        public void setAutoCommit(boolean autoCommit) throws SQLException {
            connection.setAutoCommit(autoCommit);
        }

        @Override
        public boolean getAutoCommit() throws SQLException {
            return connection.getAutoCommit();
        }

        @Override
        public void commit() throws SQLException {
            connection.commit();
        }

        @Override
        public void rollback() throws SQLException {
            connection.rollback();
        }

        @Override
        public boolean isClosed() throws SQLException {
            return connection.isClosed();
        }

        @Override
        public DatabaseMetaData getMetaData() throws SQLException {
            return connection.getMetaData();
        }

        @Override
        public void setReadOnly(boolean readOnly) throws SQLException {
            connection.setReadOnly(readOnly);
        }

        @Override
        public boolean isReadOnly() throws SQLException {
            return connection.isReadOnly();
        }

        @Override
        public void setCatalog(String catalog) throws SQLException {
            connection.setCatalog(catalog);
        }

        @Override
        public String getCatalog() throws SQLException {
            return connection.getCatalog();
        }

        @Override
        public void setTransactionIsolation(int level) throws SQLException {
            connection.setTransactionIsolation(level);
        }

        @Override
        public int getTransactionIsolation() throws SQLException {
            return connection.getTransactionIsolation();
        }

        @Override
        public SQLWarning getWarnings() throws SQLException {
            return connection.getWarnings();
        }

        @Override
        public void clearWarnings() throws SQLException {
            connection.clearWarnings();
        }

        @Override
        public Statement createStatement(int resultSetType, int resultSetConcurrency) throws SQLException {
            return connection.createStatement(resultSetType, resultSetConcurrency);
        }

        @Override
        public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
            return connection.prepareStatement(sql, resultSetType, resultSetConcurrency);
        }

        @Override
        public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
            return connection.prepareCall(sql, resultSetType, resultSetConcurrency);
        }

        @Override
        public Map<String, Class<?>> getTypeMap() throws SQLException {
            return connection.getTypeMap();
        }

        @Override
        public void setTypeMap(Map<String, Class<?>> map) throws SQLException {
            connection.setTypeMap(map);
        }

        @Override
        public void setHoldability(int holdability) throws SQLException {
            connection.setHoldability(holdability);
        }

        @Override
        public int getHoldability() throws SQLException {
            return connection.getHoldability();
        }

        @Override
        public Savepoint setSavepoint() throws SQLException {
            return connection.setSavepoint();
        }

        @Override
        public Savepoint setSavepoint(String name) throws SQLException {
            return connection.setSavepoint(name);
        }

        @Override
        public void rollback(Savepoint savepoint) throws SQLException {
            connection.rollback(savepoint);
        }

        @Override
        public void releaseSavepoint(Savepoint savepoint) throws SQLException {
            connection.releaseSavepoint(savepoint);
        }

        @Override
        public Statement createStatement(int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
            return connection.createStatement(resultSetType, resultSetConcurrency, resultSetHoldability);
        }

        @Override
        public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
            return connection.prepareStatement(sql, resultSetType, resultSetConcurrency, resultSetHoldability);
        }

        @Override
        public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
            return connection.prepareCall(sql, resultSetType, resultSetConcurrency, resultSetHoldability);
        }

        @Override
        public PreparedStatement prepareStatement(String sql, int autoGeneratedKeys) throws SQLException {
            return connection.prepareStatement(sql, autoGeneratedKeys);
        }

        @Override
        public PreparedStatement prepareStatement(String sql, int[] columnIndexes) throws SQLException {
            return connection.prepareStatement(sql, columnIndexes);
        }

        @Override
        public PreparedStatement prepareStatement(String sql, String[] columnNames) throws SQLException {
            return connection.prepareStatement(sql, columnNames);
        }

        @Override
        public Clob createClob() throws SQLException {
            return connection.createClob();
        }

        @Override
        public Blob createBlob() throws SQLException {
            return connection.createBlob();
        }

        @Override
        public NClob createNClob() throws SQLException {
            return connection.createNClob();
        }

        @Override
        public SQLXML createSQLXML() throws SQLException {
            return connection.createSQLXML();
        }

        @Override
        public boolean isValid(int timeout) throws SQLException {
            return connection.isValid(timeout);
        }

        @Override
        public void setClientInfo(String name, String value) throws SQLClientInfoException {
            connection.setClientInfo(name, value);
        }

        @Override
        public void setClientInfo(Properties properties) throws SQLClientInfoException {
            connection.setClientInfo(properties);
        }

        @Override
        public String getClientInfo(String name) throws SQLException {
            return connection.getClientInfo(name);
        }

        @Override
        public Properties getClientInfo() throws SQLException {
            return connection.getClientInfo();
        }

        @Override
        public Array createArrayOf(String typeName, Object[] elements) throws SQLException {
            return connection.createArrayOf(typeName, elements);
        }

        @Override
        public Struct createStruct(String typeName, Object[] attributes) throws SQLException {
            return connection.createStruct(typeName, attributes);
        }

        @Override
        public void setSchema(String schema) throws SQLException {
            connection.setSchema(schema);
        }

        @Override
        public String getSchema() throws SQLException {
            return connection.getSchema();
        }

        @Override
        public void abort(Executor executor) throws SQLException {
            connection.abort(executor);
        }

        @Override
        public void setNetworkTimeout(Executor executor, int milliseconds) throws SQLException {
            connection.setNetworkTimeout(executor, milliseconds);
        }

        @Override
        public <T> T unwrap(Class<T> iface) throws SQLException {
            return connection.unwrap(iface);
        }

        @Override
        public boolean isWrapperFor(Class<?> iface) throws SQLException {
            return connection.isWrapperFor(iface);
        }

        @Override
        public int getNetworkTimeout() throws SQLException {
            return connection.getNetworkTimeout();
        }
    }
}
