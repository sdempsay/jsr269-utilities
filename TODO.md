# TODO List

## High Priority

### Checkstyle Compliance Issues

The following checkstyle violations need to be fixed:

| File | Issue | Description |
|------|-------|-------------|
| `Jsr269ProcessorImpl.java:26` | JavadocStyle | Incomplete HTML tag in author tag - missing `>` |
| `Jsr269ProcessorImpl.java:58` | TodoComment | Contains `// TODO: A merge here would be nice` |
| `Jsr269ProcessorImpl.java:47` | TodoComment | Contains `// TODO: Safety check that this is a real processor` |
| `Jsr269ProcessorImpl.java` (multiple) | DesignForExtension | Class designed for extension but lacks javadoc for methods |
| `Jsr269ProcessorImpl.java` (multiple) | JavadocVariable | Missing javadoc for class fields |

### Test Improvements

| File | Issue | Description |
|------|-------|-------------|
| `TestProcessor.java:20` | TodoComment | Missing test class description |
| `FooProcessor.java:12` | TodoComment | Missing class description |
| `TestProcessor.java:44-46` | CodeQuality | Compiler diagnostics handler prints to System.out instead of capturing/testable output |

## Medium Priority

### Code Quality

- `Jsr269ProcessorImpl.java:61` - Uses `descendingIterator()` for writing - unclear if this is intentional or a bug (alphabetical order would normally use ascending)
- `Jsr269ProcessorImpl.java:67-69` - Error message doesn't include exception details for debugging

### Documentation

- Missing `package-info.java` in both `org.dempsay.support.jsr269` and `org.dempsay.support.jsr269.annotation` packages
- Test classes lack proper javadoc descriptions

## Low Priority

### Dependencies

- `pom.xml:43` - JUnit 4.13.2 version but using JUnit Jupiter (JUnit 5) in tests - potential version confusion
- `pom.xml:9-10` - Typos in project name: "utillties" should be "utilities"

### Build Configuration

- `pom.xml:75` - Compiler arg `-proc:none` may prevent annotation processing during build - should verify this is intentional

## Actions

1. Fix incomplete HTML tag in `@author` javadoc
2. Implement service file merge logic (currently overwrites)
4. Add safety check for processor classes
5. Add package-info.java files
6. Add javadoc to test classes
7. Fix descendingIterator issue (ascending expected)
8. Improve error message with exception details
9. Update dependency versions for consistency
