/*
 * Copyright (c) 2004, 2005 TADA AB - Taby Sweden
 * Distributed under the terms shown in the file COPYRIGHT
 * found in the root folder of this project or at
 * http://eng.tada.se/osprojects/COPYRIGHT.html
 */
package org.postgresql.pljava.sqlgen;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Iterator;

import org.objectweb.asm.Type;

/**
 * @author Thomas Hallgren
 */

public class TypeMapper {
    private static final TypeMapper s_singleton = new TypeMapper();

    public static TypeMapper getDefault() {
        return s_singleton;
    }

    private final Type                    ITERATOR  = Type.getType(Iterator.class);

    private final HashMap<String, String> m_typeMap = new HashMap<String, String>();

    private TypeMapper() {
        // Primitives
        //
        addMap(boolean.class, "boolean");
        addMap(Boolean.class, "boolean");
        addMap(byte.class, "smallint");
        addMap(Byte.class, "smallint");
        addMap(char.class, "smallint");
        addMap(Character.class, "smallint");
        addMap(double.class, "double precision");
        addMap(Double.class, "double precision");
        addMap(float.class, "real");
        addMap(Float.class, "real");
        addMap(int.class, "integer");
        addMap(Integer.class, "integer");
        addMap(long.class, "bigint");
        addMap(Long.class, "bigint");
        addMap(short.class, "smallint");
        addMap(Short.class, "smallint");

        // Known common mappings
        //
        addMap(String.class, "varchar");
        addMap(java.util.Date.class, "timestamp");
        addMap(Timestamp.class, "timestamp");
        addMap(Time.class, "time");
        addMap(java.sql.Date.class, "date");
        addMap(BigInteger.class, "numeric");
        addMap(BigDecimal.class, "numeric");

        addMap(Integer[].class, "int[]");
        addMap(int[].class, "int[]");
        addMap(short[].class, "shortint[]");
        addMap(Short[].class, "shortint[]");
        addMap(float[].class, "real[]");
        addMap(Float[].class, "real[]");
        addMap(long[].class, "bigint[]");
        addMap(Long[].class, "bigint[]");
        addMap(double[].class, "double precision[]");
        addMap(Double[].class, "double precision[]");

        addMap(byte[].class, "bytea");

        m_typeMap.put("V", "void");
    }

    public String getSQLType(GenericType type) {
        String sqlType = m_typeMap.get(type.getType().getDescriptor());
        if (sqlType != null) {
            return sqlType;
        }

        // The type descriptor might contain significant
        // generic info.
        //
        GenericType gType = type.getGeneric();
        if (gType == null) {
            throw new UnknownTypeException(type);
        }

        if (type.getType().equals(ITERATOR)) {
            return "SET OF " + getSQLType(gType);
        }

        throw new UnknownTypeException(type);
    }

    private void addMap(Class<?> c, String sqlType) {
        m_typeMap.put(Type.getDescriptor(c), sqlType);
    }
}
