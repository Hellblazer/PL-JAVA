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

public class ComplexTuple implements SQLData {
    private static Logger s_logger = Logger.getAnonymousLogger();

    @Function
    public static ComplexTuple logAndReturn(ComplexTuple cpl) {
        s_logger.info(cpl.getSQLTypeName() + "(" + cpl.m_x + ", " + cpl.m_y
                      + ")");
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
        m_typeName = typeName;
        m_x = stream.readDouble();
        m_y = stream.readDouble();
        s_logger.info(typeName + " from SQLInput");
    }

    @Override
    public void writeSQL(SQLOutput stream) throws SQLException {
        stream.writeDouble(m_x);
        stream.writeDouble(m_y);
        s_logger.info(m_typeName + " to SQLOutput");
    }
}
