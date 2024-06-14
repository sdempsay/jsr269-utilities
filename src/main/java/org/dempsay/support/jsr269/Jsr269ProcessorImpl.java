package org.dempsay.support.jsr269;

import java.io.IOException;
import java.io.Writer;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.StandardLocation;

import org.dempsay.support.jsr269.annotation.Jsr269Processor;

/**
 * Handles annotations with the {@link Jsr269Processor} included
 * by adding the needed entries in META-INF.
 *
 * @since 1.0.0
 * @author Shawn Dempsay {@literal <shawn@dempsay.org}
 */
@SupportedAnnotationTypes("org.dempsay.support.jsr269.annotation.Jsr269Processor")
@SupportedSourceVersion(SourceVersion.RELEASE_21)
public class Jsr269ProcessorImpl extends AbstractProcessor {
    private static final String METADATA_TARGET = "META-INF/services/javax.annotation.processing.Processor";
    private final TreeSet<String> processors = new TreeSet<>();

    @Override
    public boolean process(final Set<? extends TypeElement> annotations, final RoundEnvironment roundEnv) {
        processServices(annotations, roundEnv);
        return false;
    }

    protected void processServices(final Set<? extends TypeElement> annotations, final RoundEnvironment roundEnv) {
        if (roundEnv.processingOver()) {
            writeMetadata();
            return;
        }

        for (Element e : roundEnv.getElementsAnnotatedWith(Jsr269Processor.class)) {
            // TODO: Safety check that this is a real processor
            String processorName = String.format("%s.%s",
                this.processingEnv.getElementUtils().getPackageOf(e).toString(),
                e.getSimpleName());
            this.processors.add(processorName);
        }
    }

    protected void writeMetadata() {
        Filer filer = processingEnv.getFiler();
        // Simple implementation that just overwrites everything if it was there
        // TODO: A merge here would be nice
        if (!processors.isEmpty()) {
            try (Writer writer = filer.createResource(StandardLocation.CLASS_OUTPUT, "", METADATA_TARGET).openWriter()) {
                Iterator<String> iterator = processors.descendingIterator();
                while (iterator.hasNext()) {
                    writer.append(iterator.next());
                    writer.append(System.lineSeparator());
                }
                writer.flush();
            } catch (IOException e) {
                processingEnv.getMessager().printError(String.format("Failed creating file %s", METADATA_TARGET));
            }
        }
    }
}
