package org.dempsay.support.jsr269;

import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.StringWriter;
import java.util.List;
import java.util.stream.Collectors;

import javax.tools.JavaCompiler;
import javax.tools.JavaCompiler.CompilationTask;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;

import org.junit.Test;

/**
 * TODO: describe
 *
 * @since 1.0.0
 * @author Shawn Dempsay {@literal <shawn@dempsay.org}
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

        JavaCompiler compiler =  ToolProvider.getSystemJavaCompiler();
        StandardJavaFileManager fileManager = compiler.getStandardFileManager(null, null, null);
        var compilerUnits = fileManager.getJavaFileObjectsFromFiles(List.of(new File("src/test/resources/FooTest/FooProcessor.java")));
        var compilerOptions = List.of(
            "-cp", "target/classes",
            "-d", "target/test",
            "-proc:only",
            "-target", "21",
            "-source", "21");

        StringWriter writer = new StringWriter();
        CompilationTask compilationTask = compiler.getTask(writer, fileManager,
            d -> System.out.println(d.getMessage(null)),
            compilerOptions, List.of(Jsr269ProcessorImpl.class.getCanonicalName()), compilerUnits);

        File processorFile = new File(target, "META-INF/services/javax.annotation.processing.Processor");
        assertTrue("Compilation success", compilationTask.call());
        assertTrue("We have a service file", processorFile.exists());
        // Read the contents of processorFile
        try (BufferedReader reader = new BufferedReader(new FileReader(processorFile))) {
            assertTrue("Processor contains org.dempsay.support.jsr269.FooProcessor",
                reader.lines().collect(Collectors.joining(System.lineSeparator()))
                    .contains("org.dempsay.support.jsr269.FooProcessor"));
        }
    }

    private void deleteRecursive(File f)  {
        if (f.isDirectory())  {
            for (File c: f.listFiles())  {
                deleteRecursive(c);
             }
         }
         f.delete();
     }
}
