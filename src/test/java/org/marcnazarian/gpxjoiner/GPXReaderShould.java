package org.marcnazarian.gpxjoiner;

import org.junit.Test;

import javax.xml.bind.JAXBException;
import java.time.Instant;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.*;

public class GPXReaderShould {

    @Test
    public void readGPXFile() {
        // arrange
        Instant expectedTime = Instant.parse("2018-02-13T08:18:33Z");

        // act
        GPX gpx = GPXReader.read("src/test/java/resources/combe-emay.gpx");

        // assert
        assertNotNull(gpx);
        assertThat(gpx.trackName(), is("Combe de l'Emay, en aller retour"));
        assertThat(gpx.time(), is(expectedTime));
        assertThat(gpx.numberOfTrackPoints(), is(4499));
    }

    @Test
    public void readEmptyGPXFile() {
        // arrange
        // act
        GPX gpx = GPXReader.read("src/test/java/resources/empty-track.gpx");

        // assert
        assertNotNull(gpx);
        assertThat(gpx.trackName(), nullValue());
        assertThat(gpx.time(), nullValue());
        assertThat(gpx.numberOfTrackPoints(), is(0));
    }

    @Test
    public void readTimeFromFirstTrackPointIfNoTimeInMetadata() {
        // arrange
        Instant expectedTime = Instant.parse("2018-02-02T08:54:47Z");

        // act
        GPX gpx = GPXReader.read("src/test/java/resources/simple-track-without-metadata-time.gpx");

        // assert
        assertNotNull(gpx);
        assertThat(gpx.time(), is(expectedTime));
    }

    @Test
    public void readFirstTrackPoint() {
        // arrange
        // act
        GPX gpx = GPXReader.read("src/test/java/resources/combe-emay.gpx");

        // assert
        assertNotNull(gpx);
        assertThat(gpx.firstTrackPoint().getTime().toString(), is("2018-02-13T08:18:33Z"));
        assertThat(gpx.firstTrackPoint().getEle().doubleValue(), is(1121.4));
    }

    @Test
    public void readLastTrackPoint() {
        // arrange
        // act
        GPX gpx = GPXReader.read("src/test/java/resources/combe-emay.gpx");

        // assert
        assertNotNull(gpx);
        assertThat(gpx.lastTrackPoint().getTime().toString(), is("2018-02-13T15:20:36Z"));
        assertThat(gpx.lastTrackPoint().getEle().doubleValue(), is(1148.0));
    }

    @Test
    public void combineTwoSimpleGPSFiles() {
        // arrange
        GPX gpx1 = GPXReader.read("src/test/java/resources/simple-track-split-1.gpx");
        assertNotNull(gpx1);
        Instant timeOfTrack1 = gpx1.time();
        String nameOfTrack1 = gpx1.trackName();
        int numberOfTrackPointsOfFile1 = gpx1.numberOfTrackPoints();

        GPX gpx2 = GPXReader.read("src/test/java/resources/simple-track-split-2.gpx");
        assertNotNull(gpx2);
        Instant timeOfTrack2 = gpx2.time();
        int numberOfTrackPointsOfFile2 = gpx2.numberOfTrackPoints();

        // act
        try {
            GPX gpxFileCombined = GPXJoiner.combine(gpx1, gpx2);

            // assert
            assertNotNull(gpxFileCombined);
            assertThat(gpxFileCombined.time(), is(timeOfTrack1));
            assertThat(gpxFileCombined.time(), not(timeOfTrack2));

            assertThat(gpxFileCombined.trackName(), is(nameOfTrack1));

            assertThat(gpxFileCombined.numberOfTrackPoints(), is(numberOfTrackPointsOfFile1 + numberOfTrackPointsOfFile2));

            assertThat(gpxFileCombined.firstTrackPoint(), is(gpx1.firstTrackPoint()));
            assertThat(gpxFileCombined.lastTrackPoint(), is(gpx2.lastTrackPoint()));
        } catch (Exception e) {
            fail("Unable to combine files.");
        }
    }

    @Test
    public void combineTwoBigGPSFiles() {
        // arrange
        GPX gpx1 = GPXReader.read("src/test/java/resources/file1.gpx");
        assertNotNull(gpx1);
        Instant timeOfTrack1 = gpx1.time();
        String nameOfTrack1 = gpx1.trackName();
        int numberOfTrackPointsOfFile1 = gpx1.numberOfTrackPoints();

        GPX gpx2 = GPXReader.read("src/test/java/resources/file2.gpx");
        assertNotNull(gpx2);
        Instant timeOfTrack2 = gpx2.time();
        int numberOfTrackPointsOfFile2 = gpx2.numberOfTrackPoints();

        // act
        try {
            GPX gpxFileCombined = GPXJoiner.combine(gpx1, gpx2);

            // assert
            assertNotNull(gpxFileCombined);
            assertThat(gpxFileCombined.time(), is(timeOfTrack1));
            assertThat(gpxFileCombined.time(), not(timeOfTrack2));

            assertThat(gpxFileCombined.trackName(), is(nameOfTrack1));

            assertThat(gpxFileCombined.numberOfTrackPoints(), is(numberOfTrackPointsOfFile1 + numberOfTrackPointsOfFile2));

            assertThat(gpxFileCombined.firstTrackPoint(), is(gpx1.firstTrackPoint()));
            assertThat(gpxFileCombined.lastTrackPoint(), is(gpx2.lastTrackPoint()));
        } catch (Exception e) {
            fail("Unable to combine files.");
        }
    }

    @Test
    public void generateCombinedFileInXML() {
        // arrange
        String gpxCombinedOutputLocation = "src/test/java/resources/out/combined.gpx";
        GPX expectedCombinedFile = GPXReader.read("src/test/java/resources/combe-emay.gpx");
        assertNotNull(expectedCombinedFile);

        // act
        try {
            GPXJoiner.combine("src/test/java/resources/file1.gpx", "src/test/java/resources/file2.gpx", gpxCombinedOutputLocation);
            GPX actualCombinedFile = GPXReader.read(gpxCombinedOutputLocation);

            // assert
            assertNotNull(actualCombinedFile);
            assertThat(actualCombinedFile.time(), is(expectedCombinedFile.time()));
            assertThat(actualCombinedFile.trackName(), is(expectedCombinedFile.trackName()));
            assertThat(actualCombinedFile.numberOfTrackPoints(), is(expectedCombinedFile.numberOfTrackPoints()));
            assertThat(actualCombinedFile.firstTrackPoint().getEle().doubleValue(), is(expectedCombinedFile.firstTrackPoint().getEle().doubleValue()));
            assertThat(actualCombinedFile.lastTrackPoint().getEle().doubleValue(), is(expectedCombinedFile.lastTrackPoint().getEle().doubleValue()));
        } catch (JAXBException e) {
            e.printStackTrace();
            fail("Unable to combine files.");
        }
    }

    @Test
    public void combineGPSFilesChronologically() {
        // arrange
        GPX gpx1 = GPXReader.read("src/test/java/resources/simple-track-split-1.gpx");
        assertNotNull(gpx1);
        Instant timeOfTrack1 = gpx1.time();
        String nameOfTrack1 = gpx1.trackName();
        int numberOfTrackPointsOfFile1 = gpx1.numberOfTrackPoints();

        GPX gpx2 = GPXReader.read("src/test/java/resources/simple-track-split-2.gpx");
        assertNotNull(gpx2);
        Instant timeOfTrack2 = gpx2.time();
        int numberOfTrackPointsOfFile2 = gpx2.numberOfTrackPoints();

        // act
        try {
            // revert the files here: gpx1 is chronologically before gpx2
            GPX gpxFileCombined = GPXJoiner.combine(gpx2, gpx1);

            // assert
            assertNotNull(gpxFileCombined);
            assertThat(gpxFileCombined.time(), is(timeOfTrack1));
            assertThat(gpxFileCombined.time(), not(timeOfTrack2));

            assertThat(gpxFileCombined.trackName(), is(nameOfTrack1));

            assertThat(gpxFileCombined.numberOfTrackPoints(), is(numberOfTrackPointsOfFile1 + numberOfTrackPointsOfFile2));

            assertThat(gpxFileCombined.firstTrackPoint(), is(gpx1.firstTrackPoint()));
            assertThat(gpxFileCombined.lastTrackPoint(), is(gpx2.lastTrackPoint()));
        } catch (Exception e) {
            fail("Unable to combine files.");
        }
    }

}
