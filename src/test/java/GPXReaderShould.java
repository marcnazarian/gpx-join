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


}
