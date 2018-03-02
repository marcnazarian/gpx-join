package main.java;

import org.w3c.dom.NodeList;

import java.time.Instant;

public class GPXFile {

    private Instant time;
    private NodeList trackPoints;
    private String trackName;

    public Instant dateTime() {
        return time;
    }

    public void setTime(Instant time) {
        this.time = time;
    }

    public int numberOfTrackPoints() {
        return trackPoints.getLength();
    }

    public NodeList trackPoints() {
        return trackPoints;
    }

    public void setTrackPoints(NodeList trackPoints) {
        this.trackPoints = trackPoints;
    }

    public String trackName() {
        return trackName;
    }

    public void setTrackName(String trackName) {
        this.trackName = trackName;
    }

}
