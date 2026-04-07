# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Build and Test Commands

- **Build**: `mvn clean package`
- **Run tests**: `mvn test`
- **Run checkstyle**: `mvn checkstyle:check`
- **Java version**: 21
- **Test framework**: JUnit 5 (Jupiter)

## Architecture Overview

This is a JSR 269 annotation processor utility library with two main components:

1. **Jsr269Processor annotation** (`src/main/java/org/dempsay/support/jsr269/annotation/Jsr269Processor.java`): A marker annotation that classes can use to declare themselves as annotation processors.

2. **Jsr269ProcessorImpl** (`src/main/java/org/dempsay/support/jsr269/Jsr269ProcessorImpl.java`): The actual annotation processor that scans for classes annotated with `@Jsr269Processor` and automatically generates the `META-INF/services/javax.annotation.processing.Processor` file needed for Java's service loader mechanism.

## How It Works

When you compile code that uses `@Jsr269Processor` on processor classes:
1. Jsr269ProcessorImpl runs as an annotation processor
2. It finds all classes annotated with `@Jsr269Processor`
3. During the processing round, it writes the canonical class names to `META-INF/services/javax.annotation.processing.Processor`
4. This file enables the processors to be discovered at runtime via Java's service loader

## Test Structure

- **TestProcessor** (`src/test/java/org/dempsay/support/jsr269/TestProcessor.java`): Integration test (JUnit 5) that compiles a sample processor (`FooProcessor.java`) using the annotation and verifies the service file is generated correctly.

## Key Files

- `pom.xml` - Maven configuration (Java 21, JUnit 4, Checkstyle)
- `src/main/java/org/dempsay/support/jsr269/` - Main processor implementation
- `src/test/java/org/dempsay/support/jsr269/` - Unit/integration tests
- `src/test/resources/FooTest/FooProcessor.java` - Sample processor for testing
