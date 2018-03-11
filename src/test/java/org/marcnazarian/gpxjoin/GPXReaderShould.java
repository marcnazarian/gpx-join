package org.marcnazarian.gpxjoin;

import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;

import static org.junit.Assert.assertNotNull;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.*;

public class GPXReaderShould {

    private GPX gpxCombeEmay;

    @Before
    public void setUp() {
        // act
        gpxCombeEmay = GPXReader.read("src/test/resources/combe-emay.gpx");

        // assert
        assertNotNull(gpxCombeEmay);
    }

    @Test
    public void readGPXFile() {
        // arrange
        Instant expectedTime = Instant.parse("2018-02-13T08:18:33Z");

        // assert
        assertThat(gpxCombeEmay.trackName(), is("Combe de l'Emay, en aller retour"));
        assertThat(gpxCombeEmay.time(), is(expectedTime));
        assertThat(gpxCombeEmay.numberOfTrackPoints(), is(4499));
    }

    @Test
    public void readEmptyGPXFile() {
        // arrange
        // act
        GPX gpx = GPXReader.read("src/test/resources/empty-track.gpx");

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
        GPX gpx = GPXReader.read("src/test/resources/simple-track-without-metadata-time.gpx");

        // assert
        assertNotNull(gpx);
        assertThat(gpx.time(), is(expectedTime));
    }

    @Test
    public void readFirstTrackPoint() {
        // assert
        assertThat(gpxCombeEmay.firstTrackPoint().getTime().toString(), is("2018-02-13T08:18:33Z"));
        assertThat(gpxCombeEmay.firstTrackPoint().getEle().doubleValue(), is(1121.4));
    }

    @Test
    public void readLastTrackPoint() {
        // assert
        assertThat(gpxCombeEmay.lastTrackPoint().getTime().toString(), is("2018-02-13T15:20:36Z"));
        assertThat(gpxCombeEmay.lastTrackPoint().getEle().doubleValue(), is(1148.0));
    }

    @Test
    public void readStartAndStopTime() {
        // arrange
        Instant expectedStartTime = Instant.parse("2018-02-13T08:18:33Z");
        Instant expectedStopTime = Instant.parse("2018-02-13T15:20:36Z");

        // assert
        assertThat(gpxCombeEmay.startTime(), is(expectedStartTime));
        assertThat(gpxCombeEmay.stopTime(), is(expectedStopTime));
    }

    @Test
    public void readActivityDuration() {
        // arrange
        Instant expectedStartTime = Instant.parse("2018-02-13T08:18:33Z");
        Instant expectedStopTime = Instant.parse("2018-02-13T15:20:36Z");
        Duration activityDuration = Duration.between(expectedStartTime, expectedStopTime);
        String humanReadableActivityDuration = "7 hours 2 minutes 3 seconds";

        // assert
        assertThat(gpxCombeEmay.activityDuration(), is(activityDuration));
        assertThat(gpxCombeEmay.humanReadableActivityDuration(), is(humanReadableActivityDuration));
    }

    @Test
    public void readStartAndStopAltitude() {
        // arrange
        double expectedStartAltitude = 1121.4;
        double expectedStopAltitude = 1148.0;

        // assert
        assertThat(gpxCombeEmay.startAltitude().doubleValue(), is(expectedStartAltitude));
        assertThat(gpxCombeEmay.stopAltitude().doubleValue(), is(expectedStopAltitude));
    }

    @Test
    public void readLowestAndHighestAltitude() {
        // arrange
        double expectedLowestAltitude = 1121.4;
        double expectedHighestAltitude = 2265.4;

        // assert
        assertThat(gpxCombeEmay.lowestAltitude().doubleValue(), is(expectedLowestAltitude));
        assertThat(gpxCombeEmay.highestAltitude().doubleValue(), is(expectedHighestAltitude));
    }

}