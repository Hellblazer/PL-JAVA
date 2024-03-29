/*
 * Copyright (c) 2004, 2005 TADA AB - Taby Sweden
 * Distributed under the terms shown in the file COPYRIGHT
 * found in the root folder of this project or at
 * http://eng.tada.se/osprojects/COPYRIGHT.html
 */
package org.postgresql.pljava.example;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.TimeZone;
import java.util.logging.Logger;

import org.postgresql.pljava.annotation.Function;

/**
 * Some methods used for testing parameter and return value coersion and
 * resolution of overloaded methods.
 * 
 * @author Thomas Hallgren
 */
public class Parameters {
    @Function
    public static double addNumbers(short a, int b, long c, BigDecimal d,
                                    BigDecimal e, float f, double g) {
        return d.doubleValue() + e.doubleValue() + a + b + c + f + g;
    }

    @Function
    public static int addOne(int value) {
        return value + 1;
    }

    @Function
    public static int addOne(Integer value) {
        return value.intValue() + 1;
    }

    @Function
    public static int addOneLong(long value) {
        return (int) value + 1;
    }

    @Function
    public static int countNulls(Integer[] intArray) throws SQLException {
        int nullCount = 0;
        int top = intArray.length;
        for (int idx = 0; idx < top; ++idx) {
            if (intArray[idx] == null) {
                nullCount++;
            }
        }
        return nullCount;
    }

    @Function
    public static int countNulls(ResultSet input) throws SQLException {
        int nullCount = 0;
        int top = input.getMetaData().getColumnCount();
        for (int idx = 1; idx <= top; ++idx) {
            input.getObject(idx);
            if (input.wasNull()) {
                nullCount++;
            }
        }
        return nullCount;
    }

    @Function
    public static Date getDate() {
        return new Date(System.currentTimeMillis());
    }

    @Function
    public static Time getTime() {
        return new Time(System.currentTimeMillis());
    }

    @Function
    public static Timestamp getTimestamp() {
        return new Timestamp(System.currentTimeMillis());
    }

    @Function
    public static Integer nullOnEven(int value) {
        return value % 2 == 0 ? null : new Integer(value);
    }

    @Function
    public static byte print(byte value) {
        log("byte " + value);
        return value;
    }

    @Function
    public static byte[] print(byte[] byteArray) {
        StringBuffer buf = new StringBuffer();
        int top = byteArray.length;
        buf.append("byte[] of size " + top);
        if (top > 0) {
            buf.append(" {");
            buf.append(byteArray[0]);
            for (int idx = 1; idx < top; ++idx) {
                buf.append(',');
                buf.append(byteArray[idx]);
            }
            buf.append('}');
        }
        log(buf.toString());
        return byteArray;
    }

    @Function
    public static void print(Date time) {
        DateFormat p = DateFormat.getDateInstance(DateFormat.FULL);
        log("Local Date is " + p.format(time));
        p.setTimeZone(TimeZone.getTimeZone("UTC"));
        log("UTC Date is " + p.format(time));
        log("TZ =  " + TimeZone.getDefault().getDisplayName());
    }

    @Function
    public static double print(double value) {
        log("double " + value);
        return value;
    }

    @Function
    public static double[] print(double[] doubleArray) {
        StringBuffer buf = new StringBuffer();
        int top = doubleArray.length;
        buf.append("double[] of size " + top);
        if (top > 0) {
            buf.append(" {");
            buf.append(doubleArray[0]);
            for (int idx = 1; idx < top; ++idx) {
                buf.append(',');
                buf.append(doubleArray[idx]);
            }
            buf.append('}');
        }
        log(buf.toString());
        return doubleArray;
    }

    @Function
    public static float print(float value) {
        log("float " + value);
        return value;
    }

    @Function
    public static float[] print(float[] floatArray) {
        StringBuffer buf = new StringBuffer();
        int top = floatArray.length;
        buf.append("float[] of size " + top);
        if (top > 0) {
            buf.append(" {");
            buf.append(floatArray[0]);
            for (int idx = 1; idx < top; ++idx) {
                buf.append(',');
                buf.append(floatArray[idx]);
            }
            buf.append('}');
        }
        log(buf.toString());
        return floatArray;
    }

    @Function
    public static int print(int value) {
        log("int " + value);
        return value;
    }

    @Function
    public static int[] print(int[] intArray) {
        StringBuffer buf = new StringBuffer();
        int top = intArray.length;
        buf.append("int[] of size " + top);
        if (top > 0) {
            buf.append(" {");
            buf.append(intArray[0]);
            for (int idx = 1; idx < top; ++idx) {
                buf.append(',');
                buf.append(intArray[idx]);
            }
            buf.append('}');
        }
        log(buf.toString());
        return intArray;
    }

    @Function
    public static Integer[] print(Integer[] intArray) {
        StringBuffer buf = new StringBuffer();
        int top = intArray.length;
        buf.append("Integer[] of size " + top);
        if (top > 0) {
            buf.append(" {");
            buf.append(intArray[0]);
            for (int idx = 1; idx < top; ++idx) {
                buf.append(',');
                buf.append(intArray[idx]);
            }
            buf.append('}');
        }
        log(buf.toString());
        return intArray;
    }

    @Function
    public static long print(long value) {
        log("long " + value);
        return value;
    }

    @Function
    public static long[] print(long[] longArray) {
        StringBuffer buf = new StringBuffer();
        int top = longArray.length;
        buf.append("long[] of size " + top);
        if (top > 0) {
            buf.append(" {");
            buf.append(longArray[0]);
            for (int idx = 1; idx < top; ++idx) {
                buf.append(',');
                buf.append(longArray[idx]);
            }
            buf.append('}');
        }
        log(buf.toString());
        return longArray;
    }

    @Function
    public static short print(short value) {
        log("short " + value);
        return value;
    }

    @Function
    public static short[] print(short[] shortArray) {
        StringBuffer buf = new StringBuffer();
        int top = shortArray.length;
        buf.append("short[] of size " + top);
        if (top > 0) {
            buf.append(" {");
            buf.append(shortArray[0]);
            for (int idx = 1; idx < top; ++idx) {
                buf.append(',');
                buf.append(shortArray[idx]);
            }
            buf.append('}');
        }
        log(buf.toString());
        return shortArray;
    }

    @Function
    public static void print(Time time) {
        DateFormat p = new SimpleDateFormat("HH:mm:ss z Z");
        log("Local Time is " + p.format(time));
        p.setTimeZone(TimeZone.getTimeZone("UTC"));
        log("UTC Time is " + p.format(time));
        log("TZ =  " + TimeZone.getDefault().getDisplayName());
    }

    @Function
    public static void print(Timestamp time) {
        DateFormat p = DateFormat.getDateTimeInstance(DateFormat.FULL,
                                                      DateFormat.FULL);
        log("Local Timestamp is " + p.format(time));
        p.setTimeZone(TimeZone.getTimeZone("UTC"));
        log("UTC Timestamp is " + p.format(time));
        log("TZ =  " + TimeZone.getDefault().getDisplayName());
    }

    @Function
    static void log(String msg) {
        // GCJ has a somewhat serious bug (reported)
        //
        if ("GNU libgcj".equals(System.getProperty("java.vm.name"))) {
            System.out.print("INFO: ");
            System.out.println(msg);
        } else {
            Logger.getAnonymousLogger().info(msg);
        }
    }
}
