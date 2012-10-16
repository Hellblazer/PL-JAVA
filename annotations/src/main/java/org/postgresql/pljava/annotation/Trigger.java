/*
 * Copyright (c) 2004 TADA AB - Taby Sweden
 * Distributed under the terms shown in the file COPYRIGHT
 * found in the root directory of this distribution or at
 * http://eng.tada.se/osprojects/COPYRIGHT.html
 */
package org.postgresql.pljava.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Thomas Hallgren
 */
@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.CLASS)
public @interface Trigger {
    enum When {
        BEFORE, AFTER
    };

    enum Event {
        DELETE, INSERT, UPDATE
    };

    enum Scope {
        STATEMENT, ROW
    };

    Event[] events();

    Scope scope() default Scope.STATEMENT;

    String table();

    When when();
}
