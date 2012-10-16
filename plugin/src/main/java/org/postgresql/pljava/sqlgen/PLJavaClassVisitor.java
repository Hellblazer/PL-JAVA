/*
 * Copyright (c) 2004, 2005 TADA AB - Taby Sweden
 * Distributed under the terms shown in the file COPYRIGHT
 * found in the root folder of this project or at
 * http://eng.tada.se/osprojects/COPYRIGHT.html
 */
package org.postgresql.pljava.sqlgen;

import static org.objectweb.asm.Opcodes.ACC_PUBLIC;
import static org.objectweb.asm.Opcodes.ACC_STATIC;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;
import org.objectweb.asm.commons.EmptyVisitor;
import org.postgresql.pljava.annotation.ComplexType;
import org.postgresql.pljava.annotation.Function;

/**
 * @author Thomas Hallgren
 */
public class PLJavaClassVisitor extends EmptyVisitor {
    class MethodHandler extends EmptyVisitor {
        private final String    m_descriptor;

        private final String    m_name;

        private final String    m_signature;

        private FunctionVisitor function;

        MethodHandler(String name, String descriptor, String signature) {
            m_name = name;
            m_signature = signature;
            m_descriptor = descriptor;
            function = new FunctionVisitor(m_className, m_name, m_descriptor,
                                           m_signature);
            m_functions.add(function);
        }

        @Override
        public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
            if (FUNCTION.equals(desc)) {
                return function;
            } else if (COMPLEX_TYPE.equals(desc)) {
                return new EmptyVisitor() {
                    @Override
                    public void visit(String name, Object value) {
                        if ("value".equals(name)) {
                            function.setComplexReturnType((String) value);
                        }
                    }

                };
            }

            // Other annotations may exist but we don't care about them.
            //
            return null;
        }
    }

    protected static final String            FUNCTION     = Type.getDescriptor(Function.class);
    protected static final String            COMPLEX_TYPE = Type.getDescriptor(ComplexType.class);

    private String                           m_className;

    private final ArrayList<FunctionVisitor> m_functions  = new ArrayList<FunctionVisitor>();

    public PLJavaClassVisitor() {
    }

    public void emitOn(PrintWriter writer) {
        for (FunctionVisitor function : m_functions) {
            function.emitOn(writer);
        }
    }

    public final List<FunctionVisitor> getFunctions() {
        return m_functions;
    }

    @Override
    public void visit(int version, int access, String name, String signature,
                      String superName, String[] interfaces) {
        m_className = name.replace('/', '.');
    }

    @Override
    public FieldVisitor visitField(int access, String name, String desc,
                                   String signature, Object value) {
        return null;
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String desc,
                                     String signature, String[] exceptions) {
        // Where' only interested in methods declared as public and static.
        //
        return (access & ACC_STATIC) != 0 && (access & ACC_PUBLIC) != 0 ? new MethodHandler(
                                                                                            name,
                                                                                            desc,
                                                                                            signature)
                                                                       : null;
    }
}
