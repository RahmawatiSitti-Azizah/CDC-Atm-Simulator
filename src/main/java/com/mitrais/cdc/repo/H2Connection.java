package com.mitrais.cdc.repo;

import java.sql.*;
import java.util.List;

/**
 * H2Connection is an abstract class that provides a connection to an H2 in-memory database.
 * It handles the creation of the database connection, initialization of tables,
 * and provides methods for executing queries and retrieving results.
 * <p>
 * This class can be as a example to create abstract class for connect to different types of database
 *
 * @param <T> the type of the result that will be returned from the queries.
 *            This could be any type, such as a model class representing a database entity.
 *            The specific type will be defined in the subclasses that extend this class.
 */
public abstract class H2Connection<T> {
    private static final String H2_URL = "jdbc:h2:mem:atm_simulation;DB_CLOSE_DELAY=-1;";
    private static final String H2_DRIVER = "org.h2.Driver";
    private static final String H2_USER = "sa";
    private static final String H2_PASSWORD = "";
    protected final Connection connection;

    public H2Connection() {
        try {
            Class.forName(H2_DRIVER);
            connection = DriverManager.getConnection(H2_URL, H2_USER, H2_PASSWORD);
            //create table
            initializeTable(connection);
        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Initialize Table for H2 in memory database.
     *
     * @throws SQLException
     */
    protected void initializeTable(Connection connection) throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement(getCreateTableStatement())) {
            preparedStatement.execute();
        }
    }

    /**
     * Get the SQL statement to create the table.
     * This method must be implemented by subclasses to provide the specific SQL statement
     * for creating the table associated with the type T.
     *
     * @return a String representing the SQL CREATE TABLE statement.
     */
    public abstract String getCreateTableStatement();

    /**
     * Set parameters for the PreparedStatement.
     * This method must be implemented by subclasses to set the parameters
     * for the PreparedStatement based on the specific type T.
     *
     * @param ps        the PreparedStatement to set parameters on.
     * @param parameter variable number of parameters to be set in the PreparedStatement.
     * @throws SQLException if an error occurs while setting parameters.
     */
    protected abstract void setParameter(PreparedStatement ps, Object... parameter) throws SQLException;

    /**
     * Get the result from the ResultSet.
     * This method must be implemented by subclasses to extract a single result
     * from the ResultSet based on the specific type T.
     *
     * @param resultSet the ResultSet to extract data from.
     * @return an instance of type T representing the query result.
     * @throws SQLException if an error occurs while processing the ResultSet.
     */
    protected abstract T getQueryResult(ResultSet resultSet) throws SQLException;

    /**
     * Get a list of results from the ResultSet.
     *
     * @param resultSet
     * @return List of type T representing the query results or empty list if no results found.
     * @throws SQLException if an error occurs while processing the ResultSet.
     */
    protected abstract List<T> getQueryResultList(ResultSet resultSet) throws SQLException;

    /**
     * Execute a query with parameters and return a single result.
     *
     * @param query     the SQL query to be executed (not null).
     * @param parameter variable number of parameters to be set in the PreparedStatement.
     * @return an instance of type T representing the query result or null if no result found.
     * @throws SQLException if an error occurs while executing the query or processing the ResultSet.
     */
    protected T queryData(String query, Object... parameter) throws SQLException {
        T result;
        try (PreparedStatement preparedStatement = createPreparedStatement(query)) {
            setParameter(preparedStatement, parameter);
            try (ResultSet rs = preparedStatement.executeQuery()) {
                result = getQueryResult(rs);
            }
        }
        return result;
    }

    /**
     * Execute a query with parameters and return a list of results.
     *
     * @param query     the SQL query to be executed (not null).
     * @param parameter variable number of parameters to be set in the PreparedStatement.
     * @return a List of type T representing the query results or empty list if no results found.
     * @throws SQLException
     */
    protected List<T> queryMultipleData(String query, Object... parameter) throws SQLException {
        try (PreparedStatement preparedStatement = createPreparedStatement(query)) {
            setParameter(preparedStatement, parameter);
            try (ResultSet rs = preparedStatement.executeQuery()) {
                return getQueryResultList(rs);
            }
        }
    }

    /**
     * Execute a query without parameters and return a single result.
     *
     * @param query the SQL query to be executed (not null).
     * @return an instance of type T representing the query result or null if no result found.
     * @throws SQLException if an error occurs while executing the query or processing the ResultSet.
     */
    protected T queryData(String query) throws SQLException {
        T result;
        try (PreparedStatement preparedStatement = createPreparedStatement(query);
             ResultSet rs = preparedStatement.executeQuery()) {
            result = getQueryResult(rs);
        }
        return result;
    }

    /**
     * Execute a query without parameters and return a list of results.
     *
     * @param query the SQL query to be executed (not null).
     * @return a List of type T representing the query results or empty list if no results found.
     * @throws SQLException if an error occurs while executing the query or processing the ResultSet.
     */
    protected List<T> queryMultipleData(String query) throws SQLException {
        try (PreparedStatement preparedStatement = createPreparedStatement(query);
             ResultSet rs = preparedStatement.executeQuery()) {
            return getQueryResultList(rs);
        }
    }

    /**
     * Create PreparedStatement object based on query provided.
     *
     * @param query used for execution of prepared statement object (not null)
     * @return
     */
    public PreparedStatement createPreparedStatement(String query) {
        assert query != null && !query.isEmpty() && !query.isBlank();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            return preparedStatement;
        } catch (SQLException e) {
            System.out.println("Error creating query statement : " + e.getMessage());
            return null;
        }
    }

    /**
     * Execute a query with parameters without returning result.
     *
     * @param query     the SQL query to be executed (not null).
     * @param parameter variable number of parameters to be set in the PreparedStatement.
     * @throws SQLException if an error occurs while executing the query.
     */
    protected void executeQuery(String query, Object... parameter) throws SQLException {
        try (PreparedStatement preparedStatement = createPreparedStatement(query)) {
            setParameter(preparedStatement, parameter);
            preparedStatement.execute();
        }
    }

    /**
     * Execute a query without parameters without returning result.
     *
     * @param query the SQL query to be executed (not null).
     * @throws SQLException if an error occurs while executing the query.
     */
    protected void executeQuery(String query) throws SQLException {
        try (PreparedStatement preparedStatement = createPreparedStatement(query)) {
            preparedStatement.execute();
        }
    }
}
