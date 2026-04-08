package org.dempsay.support.jsr269;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.tools.Diagnostic;
import javax.tools.JavaCompiler;
import javax.tools.JavaCompiler.CompilationTask;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;

import org.junit.jupiter.api.Test;

/**
 * Integration tests for Jsr269ProcessorImpl annotation processor.
 * Tests verify that the processor correctly generates META-INF/services files
 * for classes annotated with @Jsr269Processor.
 *
 * @since 1.0.0
 * @author Shawn Dempsay {@literal <shawn@dempsay.org>}
 */
public class TestProcessor {
    @Test
    public void testMetadata() throws Exception {
        File target = new File("target/test");
        if (target.exists()) {
            deleteRecursive(target);
         } else {
            target.mkdirs();
         }

        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        StandardJavaFileManager fileManager = compiler.getStandardFileManager(null, null, null);
        var compilerUnits = fileManager.getJavaFileObjectsFromFiles(
                List.of(new File("src/test/resources/FooTest/FooProcessor.java")));
        var compilerOptions = List.of(
                 "-proc:only",
                 "-processor", "org.dempsay.support.jsr269.Jsr269ProcessorImpl",
                 "-cp", "target/classes",
                 "-d", "target/test",
                 "--release", "21");

        StringWriter writer = new StringWriter();
        CompilationTask compilationTask = createCompilationTask(compiler, fileManager,
                compilerOptions, compilerUnits);

        File processorFile = new File(target, "META-INF/services/javax.annotation.processing.Processor");
         // Annotation processing may return false but still generate output
        compilationTask.call();
        assertTrue(processorFile.exists(), "We have a service file");
         // Read the contents of processorFile
        try (BufferedReader reader = new BufferedReader(new FileReader(processorFile))) {
            assertTrue(
                    reader.lines().collect(Collectors.joining(System.lineSeparator()))
                             .contains("org.dempsay.support.jsr269.FooProcessor"),
                     "Processor contains org.dempsay.support.jsr269.FooProcessor");
         }
     }

    @Test
    public void testMergeLogic() throws Exception {
        File target = new File("target/test-merge");
        if (target.exists()) {
            deleteRecursive(target);
         }
        target.mkdirs();

         // Create initial service file with one processor
        File serviceFile = new File(target, "META-INF/services/javax.annotation.processing.Processor");
        serviceFile.getParentFile().mkdirs();
        try (PrintWriter writer = new PrintWriter(serviceFile)) {
            writer.println("org.dempsay.support.jsr269.ExistingProcessor");
         }

        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        StandardJavaFileManager fileManager = compiler.getStandardFileManager(null, null, null);
        var compilerUnits = fileManager.getJavaFileObjectsFromFiles(
                List.of(new File("src/test/resources/FooTest/FooProcessor.java")));
        var compilerOptions = List.of(
                 "-proc:only",
                 "-processor", "org.dempsay.support.jsr269.Jsr269ProcessorImpl",
                 "-cp", "target/classes",
                 "-d", "target/test-merge",
                 "--release", "21");

        StringWriter writer = new StringWriter();
        CompilationTask compilationTask = createCompilationTask(compiler, fileManager,
                compilerOptions, compilerUnits);

         // Annotation processing may return false but still generate output
        compilationTask.call();

         // Verify both processors are in the service file (merge occurred)
        try (BufferedReader reader = new BufferedReader(new FileReader(serviceFile))) {
            String content = reader.lines().collect(Collectors.joining(System.lineSeparator()));
            assertTrue(content.contains("org.dempsay.support.jsr269.ExistingProcessor"),
                     "Existing processor should be preserved");
            assertTrue(content.contains("org.dempsay.support.jsr269.FooProcessor"),
                     "New processor should be added");
         }
     }

    @Test
    public void testMergeOrdering() throws Exception {
        File target = new File("target/test-order");
        if (target.exists()) {
            deleteRecursive(target);
         }
        target.mkdirs();

         // Create service file with processors in non-alphabetical order
        File serviceFile = new File(target, "META-INF/services/javax.annotation.processing.Processor");
        serviceFile.getParentFile().mkdirs();
        try (PrintWriter writer = new PrintWriter(serviceFile)) {
            writer.println("org.dempsay.support.jsr269.ZProcessor");
            writer.println("org.dempsay.support.jsr269.AProcessor");
         }

        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        StandardJavaFileManager fileManager = compiler.getStandardFileManager(null, null, null);
        var compilerUnits = fileManager.getJavaFileObjectsFromFiles(
                List.of(new File("src/test/resources/FooTest/FooProcessor.java")));
        var compilerOptions = List.of(
                 "-proc:only",
                 "-processor", "org.dempsay.support.jsr269.Jsr269ProcessorImpl",
                 "-cp", "target/classes",
                 "-d", "target/test-order",
                 "--release", "21");

        StringWriter writer = new StringWriter();
        CompilationTask compilationTask = createCompilationTask(compiler, fileManager,
                compilerOptions, compilerUnits);

         // Annotation processing may return false but still generate output
        compilationTask.call();

         // Verify processors are in alphabetical order
        try (BufferedReader reader = new BufferedReader(new FileReader(serviceFile))) {
            List<String> lines = reader.lines().collect(Collectors.toList());
            assertEquals("org.dempsay.support.jsr269.AProcessor", lines.get(0),
                     "First processor should be AProcessor");
            assertEquals("org.dempsay.support.jsr269.FooProcessor", lines.get(1),
                     "Second processor should be FooProcessor");
            assertEquals("org.dempsay.support.jsr269.ZProcessor", lines.get(2),
                     "Third processor should be ZProcessor");
         }
}

    private void deleteRecursive(File f) {
        if (f.isDirectory()) {
            for (File c : f.listFiles()) {
                deleteRecursive(c);
            }
        }
        f.delete();
    }

    private CompilationTask createCompilationTask(JavaCompiler compiler,
            StandardJavaFileManager fileManager, List<String> compilerOptions,
            Iterable<? extends JavaFileObject> compilerUnits) {
        StringWriter writer = new StringWriter();
        List<Diagnostic<? extends JavaFileObject>> diagnostics = new ArrayList<>();
        return compiler.getTask(writer, fileManager,
                diagnostics::add,
                compilerOptions, List.of(Jsr269ProcessorImpl.class.getCanonicalName()), compilerUnits);
    }
}
