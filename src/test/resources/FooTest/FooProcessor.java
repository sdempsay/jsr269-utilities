package org.dempsay.support.jsr269;

import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.TypeElement;

import org.dempsay.support.jsr269.annotation.Jsr269Processor;

/**
 * TODO: describe
 *
 * @since 1.0.0
 * @author Shawn Dempsay {@literal <shawn@dempsay.org}
 */
@Jsr269Processor
public class FooProcessor extends AbstractProcessor {
    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        return false;
    }

}
