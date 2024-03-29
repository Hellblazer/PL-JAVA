/*
 * Copyright (c) 2004 TADA AB - Taby Sweden
 * Distributed under the terms shown in the file COPYRIGHT
 * found in the root directory of this distribution or at
 * http://eng.tada.se/osprojects/COPYRIGHT.html
 */
package org.postgresql.pljava.example;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.postgresql.pljava.ResultSetHandle;
import org.postgresql.pljava.annotation.Function;

public class Users implements ResultSetHandle {
    @Function
    public static ResultSetHandle listNonSupers() {
        return new Users("usesuper = false");
    }

    @Function
    public static ResultSetHandle listSupers() {
        return new Users("usesuper = true");
    }

    private final String m_filter;

    private Statement    m_statement;

    public Users(String filter) {
        m_filter = filter;
    }

    @Override
    public void close() throws SQLException {
        m_statement.close();
    }

    @Override
    public ResultSet getResultSet() throws SQLException {
        m_statement = DriverManager.getConnection("jdbc:default:connection").createStatement();
        return m_statement.executeQuery("SELECT * FROM pg_user WHERE "
                                        + m_filter);
    }
}
