package test.java;

import main.java.GPXFile;
import main.java.GPXReader;
import org.junit.Assert;
import org.junit.Test;

import java.time.Instant;

public class GPXReaderShould {

    @Test
    public void readDateFromGPXFile() {

        // arrange
        // act
        GPXFile gpxFile = GPXReader.read("src/test/resources/simple-track.gpx");

        // assert
        Instant expectedInstant = Instant.parse("2018-02-02T08:54:46Z");
        Assert.assertEquals(expectedInstant, gpxFile.dateTime());

    }

}
