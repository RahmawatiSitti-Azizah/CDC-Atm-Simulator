package com.mitrais.cdc.repo;

import java.sql.*;

public abstract class H2Connection<T> {
    private static final String H2_URL = "jdbc:h2:mem:atm_simulation;DB_CLOSE_DELAY=-1;";
    private static final String H2_DRIVER = "org.h2.Driver";
    private static final String H2_USER = "sa";
    private static final String H2_PASSWORD = "";
    protected Connection connection;

    public H2Connection() throws ClassNotFoundException, SQLException {
        Class.forName(H2_DRIVER);
        connection = DriverManager.getConnection(H2_URL, H2_USER, H2_PASSWORD);
        // Create initial table
        initializeTable(connection);
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

    public abstract String getCreateTableStatement();

    protected abstract void setParameter(PreparedStatement ps);

    protected abstract T getQueryResult(ResultSet resultSet);

    public T execute(String query, Object parameter1, ...,) {
    }

    /**
     * Create PreparedStatement object based on query provided.
     *
     * @param query used for execution of prepared statement object (not null)
     * @return
     */
    public PreparedStatement createPreparedStatement(String query) {
        assert query != null && !query.isEmpty() && !query.isBlank();
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            return preparedStatement;
        } catch (SQLException e) {
            System.out.println("Error creating query statement : " + e.getMessage());
            return null;
        }
    }
}
