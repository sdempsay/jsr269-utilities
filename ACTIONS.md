# Actions Log

## 2026-04-07

- Fixed incomplete HTML tag in `@author` javadoc (missing `>`)
- Fixed project name typos "utillties" -> "utilities" in pom.xml
- Added missing Javadoc to FooProcessor.java

## 2026-04-08

- Added safety check to verify classes extend AbstractProcessor before adding to service file
- Refactored TestProcessor to capture diagnostics instead of printing to System.err
- Fixed bug: changed descendingIterator() to iterator() for alphabetical ordering
- Added readExistingProcessors() to merge with existing service file entries
- Fixed whitespace issues in TestProcessor.java
