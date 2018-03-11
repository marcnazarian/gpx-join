# gpx-join

Java program to combine 2 GPS tracks (gpx format) into a single one 


## Installation:

```
mvn clean install
```

## Usage:

```
cd gpx-join

java -jar target/gpx-join-1.0.0.jar /path/to/file1.gpx /path/to/file2.gpx /path/to/output-combined-file.gpx
```


### Examples:

Examples of usages and behaviors can be found in unit tests (see src/test/java/org.marcnazarian.gpxjoin folder)

Examples of gpx files can be found in unit tests (see src/test/resources folder)


## Limitations:

Program tested with gpx files from __Strava__ application.

__Does not fully implement GPX 1.1 format.__
