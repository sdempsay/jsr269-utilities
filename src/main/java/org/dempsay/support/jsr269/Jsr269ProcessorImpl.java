package org.dempsay.support.jsr269;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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
 * @author Shawn Dempsay {@literal <shawn@dempsay.org>}
 */
@SupportedAnnotationTypes(
     "org.dempsay.support.jsr269.annotation.Jsr269Processor")
@SupportedSourceVersion(SourceVersion.RELEASE_21)
public class Jsr269ProcessorImpl extends AbstractProcessor {
    /** Target path for the generated services file. */
    private static final String METADATA_TARGET =
          "META-INF/services/javax.annotation.processing.Processor";
    /** Accumulator for processor class names. */
    private final TreeSet<String> processors = new TreeSet<>();

    /**
     * Process annotation elements and collect processor class names.
     *
     * @param annotations the set of annotation type elements
     * @param roundEnv the round environment
     * @return false to avoid further processing
     */
    @Override
    public boolean process(final Set<? extends TypeElement> annotations,
        final RoundEnvironment roundEnv) {
        processServices(annotations, roundEnv);
        return false;
    }

    /**
     * Collect processor names from annotated elements and write metadata
     * when processing is complete.
     *
     * @param annotations the set of annotation type elements
     * @param roundEnv the round environment
     */
    protected void processServices(final Set<? extends TypeElement> annotations,
        final RoundEnvironment roundEnv) {
        if (roundEnv.processingOver()) {
            writeMetadata();
            return;
        }

        for (Element e : roundEnv.getElementsAnnotatedWith(
            Jsr269Processor.class)) {
            if (!isValidProcessor(e)) {
                processingEnv.getMessager().printError(
                    String.format("%s is not a valid annotation processor "
                        + "(does not extend AbstractProcessor)",
                        e.getSimpleName()), e);
                continue;
            }
            String processorName = String.format(
                  "%s.%s",
                this.processingEnv.getElementUtils().getPackageOf(e).toString(),
                e.getSimpleName());
            this.processors.add(processorName);
        }
    }

    /**
     * Write the accumulated processor names to the services file.
     */
    protected void writeMetadata() {
        Filer filer = processingEnv.getFiler();
        TreeSet<String> allProcessors = new TreeSet<>(processors);
        if (!allProcessors.isEmpty()) {
            readExistingProcessors(filer, allProcessors);
            try (Writer writer = filer.createResource(StandardLocation.CLASS_OUTPUT,
                "", METADATA_TARGET).openWriter()) {
                Iterator<String> iterator = allProcessors.iterator();
                while (iterator.hasNext()) {
                    writer.append(iterator.next());
                    writer.append(System.lineSeparator());
                }
                writer.flush();
            } catch (IOException e) {
                processingEnv.getMessager().printError(
                    String.format("Failed creating file %s: %s", METADATA_TARGET, e));
            }
        }
    }

    /**
     * Read existing processor entries from the services file and add them
     * to the given set.
     *
     * @param filer the file writer
     * @param processors the set to add existing processors to
     */
    private void readExistingProcessors(final Filer filer,
            final TreeSet<String> processors) {
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(filer.getResource(
                    StandardLocation.CLASS_OUTPUT, "", METADATA_TARGET)
                    .openInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    processors.add(line.trim());
                }
            }
        } catch (IOException e) {
            // File doesn't exist yet, which is fine
        }
    }

    /**
     * Check if the given element is a valid annotation processor.
     *
     * @param element the element to check
     * @return true if the element extends AbstractProcessor
     */
    private boolean isValidProcessor(final Element element) {
        TypeElement abstractProcessor = processingEnv.getElementUtils()
            .getTypeElement("javax.annotation.processing.AbstractProcessor");
        if (abstractProcessor == null) {
            return false;
        }
        return processingEnv.getTypeUtils().isAssignable(
            processingEnv.getTypeUtils().erasure(((TypeElement) element).asType()),
            processingEnv.getTypeUtils().erasure(abstractProcessor.asType()));
    }
}
