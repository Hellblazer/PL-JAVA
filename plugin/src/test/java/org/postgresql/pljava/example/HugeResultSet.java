/*
 * Copyright (c) 2004 TADA AB - Taby Sweden
 * Distributed under the terms shown in the file COPYRIGHT
 * found in the root directory of this distribution or at
 * http://eng.tada.se/osprojects/COPYRIGHT.html
 */
package org.postgresql.pljava.example;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Random;
import java.util.logging.Logger;

import org.postgresql.pljava.ResultSetProvider;
import org.postgresql.pljava.annotation.Function;

public class HugeResultSet implements ResultSetProvider {
    @Function
    public static ResultSetProvider executeSelect(int rowCount)
                                                               throws SQLException {
        return new HugeResultSet(rowCount);
    }

    private final Random m_random;

    private final int    m_rowCount;

    public HugeResultSet(int rowCount) throws SQLException {
        m_rowCount = rowCount;
        m_random = new Random(System.currentTimeMillis());
    }

    @Override
    public boolean assignRowValues(ResultSet receiver, int currentRow)
                                                                      throws SQLException {
        // Stop when we reach rowCount rows.
        //
        if (currentRow >= m_rowCount) {
            Logger.getAnonymousLogger().info("HugeResultSet ends");
            return false;
        }

        receiver.updateInt(1, currentRow);
        receiver.updateInt(2, m_random.nextInt());
        receiver.updateTimestamp(3, new Timestamp(System.currentTimeMillis()));
        return true;
    }

    @Override
    public void close() {
    }
}
