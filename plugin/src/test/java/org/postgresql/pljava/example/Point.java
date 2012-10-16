/*
 * Copyright (c) 2004, 2005, 2006 TADA AB - Taby Sweden
 * Distributed under the terms shown in the file COPYRIGHT
 * found in the root directory of this distribution or at
 * http://eng.tada.se/osprojects/COPYRIGHT.html
 */
package org.postgresql.pljava.example;

import java.sql.SQLData;
import java.sql.SQLException;
import java.sql.SQLInput;
import java.sql.SQLOutput;
import java.util.logging.Logger;

import org.postgresql.pljava.annotation.Function;

public class Point implements SQLData {
    private static Logger s_logger = Logger.getAnonymousLogger();

    @Function
    public static Point logAndReturn(Point cpl) {
        s_logger.info(cpl.getSQLTypeName() + cpl);
        return cpl;
    }

    private String m_typeName;
    private double m_x;

    private double m_y;

    @Override
    public String getSQLTypeName() {
        return m_typeName;
    }

    @Override
    public void readSQL(SQLInput stream, String typeName) throws SQLException {
        s_logger.info(typeName + " from SQLInput");
        m_x = stream.readDouble();
        m_y = stream.readDouble();
        m_typeName = typeName;
    }

    @Override
    public void writeSQL(SQLOutput stream) throws SQLException {
        s_logger.info(m_typeName + " to SQLOutput");
        stream.writeDouble(m_x);
        stream.writeDouble(m_y);
    }
}
