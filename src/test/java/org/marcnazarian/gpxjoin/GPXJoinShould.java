package org.marcnazarian.gpxjoin;

import org.junit.Test;

import javax.xml.bind.JAXBException;
import java.time.Instant;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

public class GPXJoinShould {
    
    @Test
    public void combineTwoSimpleGPSFiles() {
        // arrange
        GPX gpx1 = GPXReader.read("src/test/resources/simple-track-split-1.gpx");
        assertNotNull(gpx1);
        Instant timeOfTrack1 = gpx1.time();
        String nameOfTrack1 = gpx1.trackName();
        int numberOfTrackPointsOfFile1 = gpx1.numberOfTrackPoints();

        GPX gpx2 = GPXReader.read("src/test/resources/simple-track-split-2.gpx");
        assertNotNull(gpx2);
        Instant timeOfTrack2 = gpx2.time();
        int numberOfTrackPointsOfFile2 = gpx2.numberOfTrackPoints();

        // act
        try {
            GPX gpxFileCombined = GPXJoin.combine(gpx1, gpx2);

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
        GPX gpx1 = GPXReader.read("src/test/resources/file1.gpx");
        assertNotNull(gpx1);
        Instant timeOfTrack1 = gpx1.time();
        String nameOfTrack1 = gpx1.trackName();
        int numberOfTrackPointsOfFile1 = gpx1.numberOfTrackPoints();

        GPX gpx2 = GPXReader.read("src/test/resources/file2.gpx");
        assertNotNull(gpx2);
        Instant timeOfTrack2 = gpx2.time();
        int numberOfTrackPointsOfFile2 = gpx2.numberOfTrackPoints();

        // act
        try {
            GPX gpxFileCombined = GPXJoin.combine(gpx1, gpx2);

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
        String gpxCombinedOutputLocation = "src/test/resources/out/combined.gpx";
        GPX expectedCombinedFile = GPXReader.read("src/test/resources/combe-emay.gpx");
        assertNotNull(expectedCombinedFile);

        // act
        try {
            GPXJoin.combine("src/test/resources/file1.gpx", "src/test/resources/file2.gpx", gpxCombinedOutputLocation);
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
        GPX gpx1 = GPXReader.read("src/test/resources/simple-track-split-1.gpx");
        assertNotNull(gpx1);
        Instant timeOfTrack1 = gpx1.time();
        String nameOfTrack1 = gpx1.trackName();
        int numberOfTrackPointsOfFile1 = gpx1.numberOfTrackPoints();

        GPX gpx2 = GPXReader.read("src/test/resources/simple-track-split-2.gpx");
        assertNotNull(gpx2);
        Instant timeOfTrack2 = gpx2.time();
        int numberOfTrackPointsOfFile2 = gpx2.numberOfTrackPoints();

        // act
        try {
            // revert the files here: gpx1 is chronologically before gpx2
            GPX gpxFileCombined = GPXJoin.combine(gpx2, gpx1);

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
