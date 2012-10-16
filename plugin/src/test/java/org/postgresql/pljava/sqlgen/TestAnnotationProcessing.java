package org.postgresql.pljava.sqlgen;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.junit.Test;
import org.objectweb.asm.ClassReader;
import org.postgresql.pljava.example.BinaryColumnTest;
import org.postgresql.pljava.example.ComplexScalar;
import org.postgresql.pljava.example.Parameters;

public class TestAnnotationProcessing {

    @Test
    public void simpleTest() throws Exception {
        ClassReader reader = new ClassReader(
                                             Parameters.class.getCanonicalName());
        PLJavaClassVisitor pljavaVisitor = new PLJavaClassVisitor();
        reader.accept(pljavaVisitor, ClassReader.SKIP_CODE);
        StringWriter buf = new StringWriter();
        PrintWriter writer = new PrintWriter(buf);
        pljavaVisitor.emitOn(writer);
        writer.flush();
        String stmt = buf.toString();
        System.out.println(stmt);
    }

    @Test
    public void complextTypeReturnTest() throws Exception {
        ClassReader reader = new ClassReader(
                                             BinaryColumnTest.class.getCanonicalName());
        PLJavaClassVisitor pljavaVisitor = new PLJavaClassVisitor();
        reader.accept(pljavaVisitor, ClassReader.SKIP_CODE);
        StringWriter buf = new StringWriter();
        PrintWriter writer = new PrintWriter(buf);
        pljavaVisitor.emitOn(writer);
        writer.flush();
        String stmt = buf.toString();
        System.out.println(stmt);
    }

    @Test
    public void complextTypeParameterTest() throws Exception {
        ClassReader reader = new ClassReader(
                                             ComplexScalar.class.getCanonicalName());
        PLJavaClassVisitor pljavaVisitor = new PLJavaClassVisitor();
        reader.accept(pljavaVisitor, ClassReader.SKIP_CODE);
        StringWriter buf = new StringWriter();
        PrintWriter writer = new PrintWriter(buf);
        pljavaVisitor.emitOn(writer);
        writer.flush();
        String stmt = buf.toString();
        System.out.println(stmt);
    }
}
