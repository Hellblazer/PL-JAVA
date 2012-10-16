/*
 * Copyright (c) 2004, 2005 TADA AB - Taby Sweden
 * Distributed under the terms shown in the file COPYRIGHT
 * found in the root folder of this project or at
 * http://eng.tada.se/osprojects/COPYRIGHT.html
 */
package org.postgresql.pljava.sqlgen;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.ResultSet;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Iterator;

import org.objectweb.asm.Type;
import org.postgresql.pljava.ResultSetProvider;

/**
 * @author Thomas Hallgren
 */

public class TypeMapper {

    private static final Type                    ITERATOR            = Type.getType(Iterator.class);
    private static String                        RESULT_SET_PROVIDER = String.format("L%s;",
                                                                                     ResultSetProvider.class.getCanonicalName().replace('.',
                                                                                                                                        '/'));
    protected static String                      RESULT_SET          = String.format("L%s;",
                                                                                     ResultSet.class.getCanonicalName().replace('.',
                                                                                                                                '/'));

    private static final HashMap<String, String> TYPE_MAP            = new HashMap<String, String>();

    static {
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

        TYPE_MAP.put("V", "void");
        TYPE_MAP.put(RESULT_SET, "record");
    }

    private TypeMapper() {
    }

    public static String getSQLType(GenericType type, String complexType) {
        String descriptor = type.getType().getDescriptor();
        if (RESULT_SET_PROVIDER.equals(descriptor)) {
            return String.format("SETOF %s", complexType);
        }
        String sqlType = TYPE_MAP.get(descriptor);
        if (sqlType != null) {
            return sqlType;
        }

        // The type descriptor might contain significant
        // generic info.
        //
        GenericType gType = type.getGeneric();
        if (gType == null) {
            if (complexType != null) {
                return complexType;
            }
            throw new UnknownTypeException(type);
        }

        if (type.getType().equals(ITERATOR)) {
            return "SET OF " + getSQLType(gType, complexType);
        }

        throw new UnknownTypeException(type);
    }

    private static void addMap(Class<?> c, String sqlType) {
        TYPE_MAP.put(Type.getDescriptor(c), sqlType);
    }
}
