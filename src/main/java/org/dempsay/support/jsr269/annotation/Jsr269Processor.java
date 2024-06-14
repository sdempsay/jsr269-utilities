package org.dempsay.support.jsr269.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks the annotated class as a processor of class annotations.
 * This will end up creating the metadata needed for a plugin to
 * used as an annotation processor
 *
 * @since 1.0.0
 * @author Shawn Dempsay {@literal <shawn@dempsay.org>}
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.CLASS)
public @interface Jsr269Processor {

}
