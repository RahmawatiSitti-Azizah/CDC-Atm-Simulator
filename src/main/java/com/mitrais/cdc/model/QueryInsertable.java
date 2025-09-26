package com.mitrais.cdc.model;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public interface QueryInsertable {
    public void insertToPreparedStatement(PreparedStatement ps, int index) throws SQLException;
}
