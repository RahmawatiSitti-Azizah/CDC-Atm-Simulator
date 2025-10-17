package com.mitrais.cdc.repo.impl;

import com.mitrais.cdc.model.Account;
import com.mitrais.cdc.model.Dollar;
import com.mitrais.cdc.model.Money;
import com.mitrais.cdc.model.QueryInsertable;
import com.mitrais.cdc.repo.AccountRepositoryH2;
import com.mitrais.cdc.repo.H2Connection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

class AccountRepositoryH2Impl extends H2Connection<Account> implements AccountRepositoryH2 {
    AccountRepositoryH2Impl() {
        super();
    }

    @Override
    public String getCreateTableStatement() {
        return "CREATE TABLE IF NOT EXISTS account " +
                "(" +
                "  account_number VARCHAR(6) PRIMARY KEY," +
                "  account_holder_name VARCHAR(255) NOT NULL," +
                "  pin VARCHAR(6) NOT NULL," +
                "  balance LONG NOT NULL" +
                ");";
    }

    @Override
    protected void setParameter(PreparedStatement ps, Object... parameters) throws SQLException {
        int index = 1;
        for (Object param : parameters) {
            if (param == null) {
                ps.setNull(index++, java.sql.Types.NULL);
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
    protected Account getQueryResult(ResultSet resultSet) throws SQLException {
        if (resultSet.next()) {
            Money balance = new Dollar(resultSet.getDouble("balance"));
            Account result = new Account(balance, resultSet.getString("account_holder_name"), resultSet.getString("account_number"), resultSet.getString("pin"));
            return result;
        }
        return null;
    }

    @Override
    protected List<Account> getQueryResultList(ResultSet resultSet) throws SQLException {
        List<Account> result = new ArrayList<>();
        Account row = getQueryResult(resultSet);
        while (row != null) {
            result.add(row);
        }
        return result;
    }

    @Override
    public List<Account> getAllAccount() {
        String query = "SELECT * FROM account";
        try {
            return queryMultipleData(query);
        } catch (SQLException e) {
            System.out.println(e.getStackTrace());
            throw new RuntimeException(e);
        }
    }

    @Override
    public Account getAccountByAccountNumber(String accountNumber) {
        String query = "SELECT * FROM account WHERE account_number = ?";
        try {
            return queryData(query, accountNumber);
        } catch (SQLException e) {
            System.out.println(e.getStackTrace());
            throw new RuntimeException(e);
        }
    }

    @Override
    public Account saveAccount(Account account) {
        String query = "INSERT INTO account (account_number, account_holder_name, pin, balance) VALUES (?, ?, ?, ?)";
        System.out.println(account.toString());
        try {
            executeQuery(query, account.getAccountNumber(), account.getAccountHolderName(), account.getPin(), account.getBalance());
        } catch (SQLException e) {
            System.out.println(e.getStackTrace());
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public Account updateAccount(Account account) {
        String query = "UPDATE account " +
                "SET " +
                "  account_holder_name = ?," +
                "  pin = ?, " +
                "  balance = ? " +
                "WHERE account_number = ?";
        System.out.println(account.toString());
        try {
            executeQuery(query, account.getAccountHolderName(), account.getPin(), account.getBalance(), account.getAccountNumber());
        } catch (SQLException e) {
            System.out.println(e.getStackTrace());
            throw new RuntimeException(e);
        }
        return null;
    }
}
