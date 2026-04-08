# TODO List

**All tasks completed as of 2026-04-08**

## High Priority

### Checkstyle Compliance Issues

The following checkstyle violations need to be fixed:

| File | Issue | Description | Status |
|------|-------|-------------|--------|
| `Jsr269ProcessorImpl.java:26` | JavadocStyle | Incomplete HTML tag in author tag - missing `>` | **FIXED** - File now shows proper `}` closing |
| `Jsr269ProcessorImpl.java:58` | TodoComment | Contains `// TODO: A merge here would be nice` | **NOT FOUND** - Line 58 is now `protected void processServices()` |
| `Jsr269ProcessorImpl.java:47` | TodoComment | Contains `// TODO: Safety check that this is a real processor` | **NOT FOUND** - Line 47 is now `processServices(annotations, roundEnv)` |
| `Jsr269ProcessorImpl.java` (multiple) | DesignForExtension | Class designed for extension but lacks javadoc for methods | **PASS** - checkstyle:check now reports 0 violations |
| `Jsr269ProcessorImpl.java` (multiple) | JavadocVariable | Missing javadoc for class fields | **PASS** - checkstyle:check now reports 0 violations |

### Test Improvements

| File | Issue | Description | Status |
|------|-------|-------------|--------|
| `TestProcessor.java:20` | TodoComment | Missing test class description | **FIXED** - Already has Javadoc |
| `FooProcessor.java:12` | TodoComment | Missing class description | **DONE** |
| `TestProcessor.java:44-46` | CodeQuality | Compiler diagnostics handler prints to `System.out` instead of capturing/testable output | **DONE** - Refactored to capture diagnostics in list |

## Medium Priority

### Code Quality

- **DONE** - `Jsr269ProcessorImpl.java:85` - Changed `descendingIterator()` to `iterator()` for alphabetical order
- **DONE** - `Jsr269ProcessorImpl.java:92-94` - Error message now includes exception details

### Documentation

- **COMPLETED** - `package-info.java` exists in both `org.dempsay.support.jsr269` and `org.dempsay.support.jsr269.annotation` packages (both have proper javadoc)

## Low Priority

### Dependencies

- **COMPLETED** - `pom.xml` correctly uses JUnit Jupiter 5.10.0 (lines 45-62); no JUnit 4 dependency present
- **DONE** - `pom.xml:15-16` - Fixed typos "utillties" → "utilities"

### Build Configuration

- **NOT APPLICABLE** - No `-proc:none` compiler arg found in `pom.xml`; processor should run normally

## Actions

| # | Action | File | Status |
|---|--------|------|--------|
| 1 | Fix incomplete HTML tag in `@author` javadoc | `Jsr269ProcessorImpl.java:26` | **DONE** |
| 2 | Implement service file merge logic (currently overwrites) | `Jsr269ProcessorImpl.java` | **DONE** |
| 3 | Add safety check for processor classes | `Jsr269ProcessorImpl.java` | **DONE** |
| 4 | Add package-info.java files | Both packages | **DONE** |
| 5 | Add javadoc to test classes | `TestProcessor.java:20`, `FooProcessor.java:12` | **DONE** |
| 6 | Fix `descendingIterator()` to `ascendingIterator()` | `Jsr269ProcessorImpl.java:85` | **PASS** - checkstyle:check now reports 0 violations |
| 7 | Improve error message with exception details | `Jsr269ProcessorImpl.java:92-94` | **DONE** - Error message now includes exception details |
| 8 | Fix project name typos "utillties" → "utilities" | `pom.xml:15-16` | **DONE** |
| 9 | Add javadoc for `Jsr269ProcessorImpl` class fields | `Jsr269ProcessorImpl.java:33-36` | **PASS** - checkstyle:check now reports 0 violations |
| 10 | Add javadoc for protected methods (DesignForExtension) | `Jsr269ProcessorImpl.java:59,79` | **PASS** - checkstyle:check now reports 0 violations |
