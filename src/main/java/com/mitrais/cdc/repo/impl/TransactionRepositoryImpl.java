package com.mitrais.cdc.repo.impl;

import com.mitrais.cdc.model.Account;
import com.mitrais.cdc.model.Dollar;
import com.mitrais.cdc.model.QueryInsertable;
import com.mitrais.cdc.model.Transaction;
import com.mitrais.cdc.repo.H2Connection;
import com.mitrais.cdc.repo.TransactionRepositoryH2;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

@Repository
class TransactionRepositoryImpl extends H2Connection<Transaction> implements TransactionRepositoryH2 {

    TransactionRepositoryImpl() {
        super();
    }

    @Override
    public String getCreateTableStatement() {
        return "CREATE TABLE IF NOT EXISTS transaction" +
                "(" +
                "  id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY," +
                "  source_account VARCHAR(6) NOT NULL," +
                "  destination_account VARCHAR(6) DEFAULT NULL," +
                "  amount LONG NOT NULL," +
                "  reference_number VARCHAR(18) DEFAULT NULL," +
                "  transaction_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                "  note VARCHAR (255) DEFAULT NULL" +
                ");";
    }

    @Override
    protected void setParameter(PreparedStatement ps, Object... parameters) throws SQLException {
        int index = 1;
        for (Object param : parameters) {
            if (param == null) {
                ps.setNull(index++, Types.NULL);
                continue;
            }
            if (param instanceof String) {
                ps.setString(index++, (String) param);
            } else if (param instanceof Integer) {
                ps.setInt(index++, (Integer) param);
            } else if (param instanceof QueryInsertable) {
                QueryInsertable parameter = (QueryInsertable) param;
                parameter.insertToPreparedStatement(ps, index++);
            } else if (param instanceof Long) {
                ps.setLong(index++, (Long) param);
            } else if (param instanceof Double) {
                ps.setDouble(index++, (Double) param);
            }
        }
    }

    @Override
    protected Transaction getQueryResult(ResultSet resultSet) throws SQLException {
        if (resultSet.next()) {
            Account sourceAccount = new Account(null, resultSet.getString("source_account_holder_name"),
                    resultSet.getString("source_account"), null);
            String destinationAcountHolderName = resultSet.getString("destination_acount_holder_name");
            Account destinationAccount = null;
            if (destinationAcountHolderName != null) {
                destinationAccount = new Account(null, destinationAcountHolderName,
                        resultSet.getString("destination_account"), null);
            }
            String referenceNumber = resultSet.getString("reference_number");
            return new Transaction(null, sourceAccount, destinationAccount, new Dollar(resultSet.getDouble("amount")),
                    referenceNumber, resultSet.getString("note"), resultSet.getTimestamp("transaction_date").toLocalDateTime());
        }
        return null;
    }

    @Override
    protected List<Transaction> getQueryResultList(ResultSet resultSet) throws SQLException {
        List<Transaction> result = new ArrayList<>();
        Transaction row = getQueryResult(resultSet);
        while (row != null) {
            result.add(row);
            row = getQueryResult(resultSet);
        }
        return result;
    }

    @Override
    public List<Transaction> findAllTransaction() {
        String query = "SELECT t.*, " +
                "  s.account_holder_name AS source_account_holder_name," +
                "  d.account_holder_name AS destination_acount_holder_name " +
                "FROM transaction t " +
                "INNER JOIN account s ON s.account_number = t.source_account " +
                "LEFT JOIN account d ON d.account_number = t.destination_account OR t.destination_account = ";
        try {
            List<Transaction> result = queryMultipleData(query);
            return result;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Transaction> findTransactionBySourceAccount(String sourceAccountNumber) {
        String query = "SELECT t.*, " +
                "  s.account_holder_name AS source_account_holder_name," +
                "  d.account_holder_name AS destination_acount_holder_name " +
                "FROM transaction t " +
                "INNER JOIN account s ON s.account_number = t.source_account " +
                "LEFT JOIN account d ON d.account_number = t.destination_account " +
                "WHERE t.source_account = ? OR t.destination_account = ?";
        try {
            List<Transaction> result = queryMultipleData(query, sourceAccountNumber, sourceAccountNumber);
            return result;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Transaction findTransactionByReferenceNumber(String referenceNumber) {
        String query = "SELECT t.*, " +
                "  s.account_holder_name AS source_account_holder_name," +
                "  d.account_holder_name AS destination_acount_holder_name " +
                "FROM transaction t " +
                "INNER JOIN account s ON s.account_number = t.source_account " +
                "LEFT JOIN account d ON d.account_number = t.destination_account " +
                "WHERE t.reference_number = ? ";
        try {
            Transaction result = queryData(query, referenceNumber);
            return result;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Transaction saveTransaction(Transaction transaction) {
        String query = "INSERT INTO transaction (source_account, destination_account, amount, reference_number, note) " +
                "VALUES (?, ?, ?, ?, ?)";
        try {
            Account sourceAccount = transaction.getSourceAccount();
            Account destinationAccount = transaction.getDestinationAccount();
            executeQuery(query, sourceAccount.getAccountNumber(),
                    destinationAccount != null ? destinationAccount.getAccountNumber() : null,
                    transaction.getAmount(), transaction.getReferenceNumber(), transaction.getNote());
            return transaction;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Transaction> findLastTransactionBySourceAccount(String sourceAccountNumber, int size) {
        String query = "SELECT t.*, " +
                "  s.account_holder_name AS source_account_holder_name," +
                "  d.account_holder_name AS destination_acount_holder_name " +
                "FROM transaction t " +
                "INNER JOIN account s ON s.account_number = t.source_account " +
                "LEFT JOIN account d ON d.account_number = t.destination_account " +
                "WHERE t.source_account = ? OR t.destination_account = ? " +
                "LIMIT ?";
        try {
            List<Transaction> result = queryMultipleData(query, sourceAccountNumber, sourceAccountNumber, size);
            result = result.stream().sorted((t1, t2) -> t2.getTransactionDate().compareTo(t1.getTransactionDate())).toList();
            return result;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
