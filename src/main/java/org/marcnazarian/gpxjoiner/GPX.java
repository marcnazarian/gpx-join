package org.marcnazarian.gpxjoiner;

import java.time.Instant;
import java.util.List;

public class GPX {
    
    final GpxType gpxType;

    public GPX(GpxType gpxType) {
        this.gpxType = gpxType;
    }

    public String trackName() {
        return gpxType.getTrk().get(0).getName();
    }

    public int numberOfTrackPoints() {
        return trackPoints().size();
    }

    public Instant time() {
        if (gpxType.getMetadata() != null && gpxType.getMetadata().getTime() != null) {
            return Instant.parse(gpxType.getMetadata().getTime().toString());
        }

        if (gpxType.getTrk().size() > 0 && gpxType.getTrk().get(0).getTrkseg().size() > 0 && trackPoints().size() > 0) {
            return Instant.parse(trackPoints().get(0).getTime().toString());
        }

        return null;
    }

    public List<WptType> trackPoints() {
        return gpxType.getTrk().get(0).getTrkseg().get(0).getTrkpt();
    }

    public void addTrackPoints(List<WptType> trackPoints) {
        gpxType.getTrk().get(0).getTrkseg().get(0).getTrkpt().addAll(trackPoints);
    }

    public WptType firstTrackPoint() {
        return trackPoints().get(0);
    }

    public WptType lastTrackPoint() {
        return trackPoints().get(trackPoints().size() - 1);
    }
}
