package test.java;

import main.java.GPXFile;
import main.java.GPXReader;
import org.junit.Assert;
import org.junit.Test;

import java.time.Instant;

public class GPXReaderShould {

    @Test
    public void readDateFromMetadataGPXFile() {
        // arrange
        // act
        GPXFile gpxFile = GPXReader.read("src/test/resources/simple-track.gpx");

        // assert
        Instant expectedInstant = Instant.parse("2018-02-02T08:54:46Z");
        Assert.assertEquals(expectedInstant, gpxFile.dateTime());
    }

    @Test
    public void readDateFromGPXFileWhenNoMetadata() {
        // arrange
        // act
        GPXFile gpxFile = GPXReader.read("src/test/resources/simple-track-without-metadata.gpx");

        // assert
        Instant expectedInstant = Instant.parse("2018-02-02T08:54:49Z");
        Assert.assertEquals(expectedInstant, gpxFile.dateTime());
    }

    @Test
    public void readDateFromGPXFileWhenNoMetadataTime() {
        // arrange
        // act
        GPXFile gpxFile = GPXReader.read("src/test/resources/simple-track-without-metadata-time.gpx");

        // assert
        Instant expectedInstant = Instant.parse("2018-02-02T08:54:47Z");
        Assert.assertEquals(expectedInstant, gpxFile.dateTime());
    }

    @Test
    public void returnNullIfTimeCantBeFound() {
        // arrange
        // act
        GPXFile gpxFile = GPXReader.read("src/test/resources/empty-track.gpx");

        // assert
        Assert.assertNull(gpxFile.dateTime());
    }

    @Test
    public void readTrackName() {
        // arrange
        // act
        GPXFile gpxFile = GPXReader.read("src/test/resources/simple-track.gpx");

        // assert
        Assert.assertEquals("Morning activity", gpxFile.trackName());
    }

    @Test
    public void returnEmptyStringIfNoNameFound() {
        // arrange
        // act
        GPXFile gpxFile = GPXReader.read("src/test/resources/empty-track.gpx");

        // assert
        Assert.assertEquals("", gpxFile.trackName());
    }

    @Test
    public void readTrack() {
        // arrange
        // act
        GPXFile gpxFile = GPXReader.read("src/test/resources/simple-track.gpx");

        // assert
        Assert.assertEquals(3, gpxFile.numberOfTrackPoints());
    }

    @Test
    public void readTrackBigFile() {
        // arrange
        // act
        GPXFile gpxFile = GPXReader.read("src/test/resources/combe-emay.gpx");

        // assert
        Assert.assertEquals(4499, gpxFile.numberOfTrackPoints());
    }

    @Test
    public void combineTwoSimpleGPSFiles() {
        // arrange
        GPXFile gpxFile1 = GPXReader.read("src/test/resources/simple-track-split-1.gpx");
        Instant timeOfTrack1 = gpxFile1.dateTime();
        String nameOfTrack1 = gpxFile1.trackName();
        int numberOfTrackPointsOfFile1 = gpxFile1.numberOfTrackPoints();

        GPXFile gpxFile2 = GPXReader.read("src/test/resources/simple-track-split-2.gpx");
        Instant timeOfTrack2 = gpxFile2.dateTime();
        int numberOfTrackPointsOfFile2 = gpxFile2.numberOfTrackPoints();

        // act
        GPXFile gpxFileCombined = GPXReader.combine("src/test/resources/simple-track-split-1.gpx", "src/test/resources/simple-track-split-2.gpx");

        // assert
        Assert.assertEquals(timeOfTrack1, gpxFileCombined.dateTime());
        Assert.assertNotEquals(timeOfTrack2, gpxFileCombined.dateTime());

        Assert.assertEquals(nameOfTrack1, gpxFileCombined.trackName());

        Assert.assertEquals(numberOfTrackPointsOfFile1 + numberOfTrackPointsOfFile2, gpxFileCombined.numberOfTrackPoints());
    }

    @Test
    public void combineTwoGPSFiles() {
        // arrange
        GPXFile gpxFile1 = GPXReader.read("src/test/resources/file1.gpx");
        Instant timeOfTrack1 = gpxFile1.dateTime();
        int numberOfTrackPointsOfFile1 = gpxFile1.numberOfTrackPoints();

        GPXFile gpxFile2 = GPXReader.read("src/test/resources/file1.gpx");
        Instant timeOfTrack2 = gpxFile2.dateTime();
        int numberOfTrackPointsOfFile2 = gpxFile2.numberOfTrackPoints();

        // act
        GPXFile gpxFileCombined = GPXReader.combine("src/test/resources/file1.gpx", "src/test/resources/file2.gpx");

        // assert
        Assert.assertEquals(timeOfTrack1, gpxFileCombined.dateTime());
        Assert.assertNotEquals(timeOfTrack2, gpxFileCombined.dateTime());
        Assert.assertEquals(numberOfTrackPointsOfFile1 + numberOfTrackPointsOfFile2, gpxFileCombined.numberOfTrackPoints());
    }


}
