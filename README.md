jsr269-utilities
=================

A utility library for building JSR 269 annotation processors in Java.

Overview
--------

This library simplifies the creation of annotation processors by automatically generating the `META-INF/services/javax.annotation.processing.Processor` service file. Instead of manually maintaining this file, simply annotate your processor class with `@Jsr269Processor`.

Installation
------------

Add the following dependency to your `pom.xml`:

```xml
<dependency>
    <groupId>org.dempsay.support.jsr269</groupId>
    <artifactId>jsr269-utilities</artifactId>
    <version>1.1.0-SNAPSHOT</version>
</dependency>
```

Usage
-----

### 1. Annotate your processor class

Extend `AbstractProcessor` and add the `@Jsr269Processor` annotation:

```java
package foo.bar;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.TypeElement;
import java.util.Set;

import org.dempsay.support.jsr269.annotation.Jsr269Processor;

@Jsr269Processor
public class MyProcessor extends AbstractProcessor {
    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        // Your processing logic here
        return false;
    }
}
```

### 2. The service file is auto-generated

During compilation, the library automatically creates `META-INF/services/javax.annotation.processing.Processor` containing your processor's fully qualified class name.

### 3. Merge with existing processors

If other processors already exist in the service file, they are preserved and merged with your new processor. All processor names are sorted alphabetically.

Features
--------

- **Auto-registration**: Automatically registers your processor with Java's service loader
- **Merge support**: Merges with existing service file entries
- **Safety check**: Validates that annotated classes extend `AbstractProcessor`
- **Alphabetical ordering**: Maintains sorted processor list for consistent output

Building
--------

```bash
mvn clean package
```

Running tests:

```bash
mvn test
```

Requirements
------------

- Java 21 or higher
- Maven 3.6+

License
-------

MIT License - see LICENSE file for details.