jsr269-utilities
=========================

This is a set of utilities to help building processors based on the JSR 269 standard.

Jsr269Processor Annotation
--------------------------
This annotation is used to create the entries in the processor's manifest file.
As an example, use it like this:
~~~java
package foo.bar;

import org.dempsay.jsr269.processor.annotation.Jsr269Processor;

@Jsr269Processor
public class MyProcessor implements AnnotationProcessor {
    ...
}
~~~

More documentation coming soon :)

