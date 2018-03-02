package main.java;

import org.w3c.dom.NodeList;

import java.time.Instant;

public class GPXFile {

    private Instant time;
    private NodeList trackPoints;

    public Instant dateTime() {
        return time;
    }

    public void setTime(Instant time) {
        this.time = time;
    }

    public int numberOfTrackPoints() {
        return trackPoints.getLength();
    }

    public void setTrackPoints(NodeList trackPoints) {
        this.trackPoints = trackPoints;
    }
}
