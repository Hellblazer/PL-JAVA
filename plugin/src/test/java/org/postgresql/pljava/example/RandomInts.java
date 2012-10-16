/*
 * Copyright (c) 2004 TADA AB - Taby Sweden
 * Distributed under the terms shown in the file COPYRIGHT
 * found in the root directory of this distribution or at
 * http://eng.tada.se/osprojects/COPYRIGHT.html
 */
package org.postgresql.pljava.example;

import java.sql.SQLException;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Random;

import org.postgresql.pljava.annotation.Function;

public class RandomInts implements Iterator<Object> {
    @Function
    public static Iterator<?> createIterator(int rowCount) throws SQLException {
        return new RandomInts(rowCount);
    }

    private int          m_currentRow;
    private final Random m_random;

    private final int    m_rowCount;

    public RandomInts(int rowCount) throws SQLException {
        m_rowCount = rowCount;
        m_random = new Random(System.currentTimeMillis());
    }

    @Override
    public boolean hasNext() {
        return m_currentRow < m_rowCount;
    }

    @Override
    public Object next() {
        if (m_currentRow < m_rowCount) {
            ++m_currentRow;
            return new Integer(m_random.nextInt());
        }
        throw new NoSuchElementException();
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }
}
