/*
 * Copyright (c) 2004, 2005 TADA AB - Taby Sweden
 * Distributed under the terms shown in the file COPYRIGHT
 * found in the root folder of this project or at
 * http://eng.tada.se/osprojects/COPYRIGHT.html
 */
package org.postgresql.pljava.example;

import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.logging.Logger;

import org.postgresql.pljava.ResultSetProvider;
import org.postgresql.pljava.annotation.Function;

/**
 * @author Filip Hrbek
 */
public class MetaDataBooleans implements ResultSetProvider {
    @Function
    public static ResultSetProvider getDatabaseMetaDataBooleans()
                                                                 throws SQLException {
        try {
            return new MetaDataBooleans();
        } catch (SQLException e) {
            throw new SQLException("Error reading DatabaseMetaData",
                                   e.getMessage());
        }
    }

    String[]  methodNames;

    Boolean[] methodResults;

    public MetaDataBooleans() throws SQLException {
        Logger log = Logger.getAnonymousLogger();

        class MethodComparator implements Comparator<Object> {
            @Override
            public int compare(Object a, Object b) {
                return ((Method) a).getName().compareTo(((Method) b).getName());
            }
        }

        Connection conn = DriverManager.getConnection("jdbc:default:connection");
        DatabaseMetaData md = conn.getMetaData();
        Method[] m = DatabaseMetaData.class.getMethods();
        Arrays.sort(m, new MethodComparator());
        Class<?> prototype[];
        Class<?> returntype;
        Object[] args = new Object[0];
        Boolean result = null;
        ArrayList<String> mn = new ArrayList<String>();
        ArrayList<Boolean> mr = new ArrayList<Boolean>();

        for (Method element : m) {
            prototype = element.getParameterTypes();
            if (prototype.length > 0) {
                continue;
            }

            returntype = element.getReturnType();
            if (!returntype.equals(boolean.class)) {
                continue;
            }

            try {
                result = (Boolean) element.invoke(md, args);
            } catch (Exception e) {
                log.info("Method: " + element.getName() + " => "
                         + e.getMessage());
            } catch (AbstractMethodError e) {
                // probably a JDBC 4 method that isn't supported yet
                log.info("Method: " + element.getName() + " => "
                         + e.getMessage());
            }

            mn.add(element.getName());
            mr.add(result);
        }

        methodNames = mn.toArray(new String[0]);
        methodResults = mr.toArray(new Boolean[0]);
    }

    @Override
    public boolean assignRowValues(ResultSet receiver, int currentRow)
                                                                      throws SQLException {
        if (currentRow < methodNames.length) {
            receiver.updateString(1, methodNames[currentRow]);
            receiver.updateBoolean(2, methodResults[currentRow].booleanValue());
            return true;
        }
        return false;
    }

    @Override
    public void close() {
    }
}
