package org.marcnazarian.gpxjoin;

import org.junit.Test;

import java.time.Instant;

import static org.junit.Assert.assertNotNull;
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



}
