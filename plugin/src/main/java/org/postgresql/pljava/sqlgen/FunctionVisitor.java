/*
 * Copyright (c) 2004, 2005 TADA AB - Taby Sweden
 * Distributed under the terms shown in the file COPYRIGHT
 * found in the root folder of this project or at
 * http://eng.tada.se/osprojects/COPYRIGHT.html
 */
package org.postgresql.pljava.sqlgen;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.Type;
import org.objectweb.asm.commons.EmptyVisitor;
import org.postgresql.pljava.TriggerData;
import org.postgresql.pljava.annotation.Function;

/**
 * @author Thomas Hallgren
 */
public class FunctionVisitor extends EmptyVisitor {

    private final String              m_className;

    private String                    m_language    = "java";

    private final String              m_methodDescriptor;

    private final String              m_methodName;

    private final String              m_methodSignature;

    private String                    complexReturnType;

    private String                    m_name        = "";

    private Function.OnNullInput      m_onNullInput = Function.OnNullInput.CALLED;

    private String                    m_schema      = "";

    private Function.Security         m_security    = Function.Security.INVOKER;

    /**
     * The Triggers that will call this function (if any).
     */
    private ArrayList<TriggerVisitor> m_triggers    = new ArrayList<>();

    private Function.Type             m_type        = Function.Type.VOLATILE;

    FunctionVisitor(String className, String methodName,
                    String methodDescriptor, String methodSignature) {
        super();
        m_className = className;
        m_methodName = methodName;
        m_methodSignature = methodSignature;
        m_methodDescriptor = methodDescriptor;
    }

    public void emitOn(PrintWriter writer) {
        emitFunction(writer);
        for (TriggerVisitor trigger : m_triggers) {
            trigger.emit(m_schema, m_name, writer);
        }
    }

    /**
     * @param writer
     */
    protected void emitFunction(PrintWriter writer) {
        writer.print("CREATE OR REPLACE FUNCTION ");
        if (!m_schema.isEmpty()) {
            writer.print(m_schema);
            writer.print(".");
        }
        writer.print(getName());
        writer.print('(');
        String[] argTypes = getArgumentTypes();
        int top = argTypes.length;
        if (top > 0) {
            writer.print(argTypes[0]);
            for (int idx = 1; idx < top; ++idx) {
                writer.print(',');
                writer.print(argTypes[idx]);
            }
        }
        writer.println(")");
        writer.print("\tRETURNS ");
        writer.println(getReturnType());
        writer.print("\tLANGUAGE ");
        writer.println(getLanguage());
        switch (getType()) {
            case STABLE:
                writer.println("\tSTABLE");
                break;
            case IMMUTABLE:
                writer.println("\tIMMUTABLE");
            default:
                break;
        }
        if (getOnNullInput() == Function.OnNullInput.RETURNS_NULL) {
            writer.println("\tRETURNS NULL ON NULL INPUT");
        }
        if (getSecurity() == Function.Security.DEFINER) {
            writer.println("\tSECURITY DEFINER");
        }

        writer.print("\tAS '");
        writer.print(getClassName());
        writer.print('.');
        writer.print(getMethodName());
        writer.print('(');

        String[] paramTypes = getParameterTypes();
        top = paramTypes.length;
        if (top > 0) {
            writer.print(paramTypes[0]);
            for (int idx = 1; idx < top; ++idx) {
                writer.print(',');
                writer.print(paramTypes[idx]);
            }
        }
        writer.print(")'");
        writer.println(";");
    }

    public String[] getArgumentTypes() {
        if (m_triggers.isEmpty()) {
            return new String[0];
        }

        String sign = m_methodSignature == null ? m_methodDescriptor
                                               : m_methodSignature;
        GenericType[] argTypes = GenericType.getArgumentTypes(sign);
        int idx = argTypes.length;

        String[] sqlTypes = new String[idx];
        while (--idx >= 0) {
            sqlTypes[idx] = TypeMapper.getSQLType(argTypes[idx],
                                                  complexReturnType);
        }
        return sqlTypes;
    }

    public final String getClassName() {
        return m_className;
    }

    public final String getLanguage() {
        return m_language;
    }

    public final String getMethodDescriptor() {
        return m_methodDescriptor;
    }

    public final String getMethodName() {
        return m_methodName;
    }

    public final String getMethodSignature() {
        return m_methodSignature;
    }

    public final String getName() {
        return m_name.length() == 0 ? m_methodName : m_name;
    }

    public final Function.OnNullInput getOnNullInput() {
        return m_onNullInput;
    }

    public String[] getParameterTypes() {
        Type[] argTypes = Type.getArgumentTypes(m_methodDescriptor);
        int idx = argTypes.length;
        String[] paramTypes = new String[idx];
        while (--idx >= 0) {
            paramTypes[idx] = argTypes[idx].getClassName();
        }
        return paramTypes;
    }

    public String getReturnType() {
        if (!m_triggers.isEmpty()) {
            return "trigger";
        }

        String sign = m_methodSignature == null ? m_methodDescriptor
                                               : m_methodSignature;
        return TypeMapper.getSQLType(GenericType.getReturnType(sign),
                                     complexReturnType);
    }

    public final String getSchema() {
        return m_schema;
    }

    public final Function.Security getSecurity() {
        return m_security;
    }

    public final List<TriggerVisitor> getTriggers() {
        return m_triggers;
    }

    public final Function.Type getType() {
        return m_type;
    }

    @Override
    public void visit(String name, Object value) {
        if ("name".equals(name)) {
            m_name = (String) value;
        } else if ("language".equals(name)) {
            m_language = (String) value;
        } else if ("schema".equals(name)) {
            m_schema = (String) value;
        } else {
            throw new UnrecognizedAttributeException(name);
        }
    }

    @Override
    public AnnotationVisitor visitArray(String name) {
        if (name.equals("triggers")) {
            return new EmptyVisitor() {
                @Override
                public AnnotationVisitor visitAnnotation(String paramString1,
                                                         String paramString2) {
                    TriggerVisitor ta = new TriggerVisitor();
                    m_triggers.add(ta);
                    return ta;
                }
            };
        }
        throw new UnrecognizedAttributeException(name);
    }

    @Override
    public void visitEnd() {
        // Verify that trigger functions have correct signature
        //
        if (!m_triggers.isEmpty()) {
            String desc = getMethodDescriptor();
            Type[] argTypes = Type.getArgumentTypes(desc);
            if (!(argTypes.length == 1
                  && argTypes[0].equals(Type.getType(TriggerData.class)) && Type.getReturnType(desc).equals(Type.VOID_TYPE))) {
                throw new MalformedTriggerException(this);
            }
        }
    }

    @Override
    public void visitEnum(String name, String desc, String value) {
        if ("onNullInput".equals(name)) {
            m_onNullInput = Function.OnNullInput.valueOf(value);
        } else if ("security".equals(name)) {
            m_security = Function.Security.valueOf(value);
        } else if ("type".equals(name)) {
            m_type = Function.Type.valueOf(value);
        } else {
            throw new UnrecognizedAttributeException(name);
        }
    }

    public String getComplexReturnType() {
        return complexReturnType;
    }

    public void setComplexReturnType(String complexReturnType) {
        this.complexReturnType = complexReturnType;
    }
}
