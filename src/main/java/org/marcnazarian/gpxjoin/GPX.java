package org.marcnazarian.gpxjoin;

import org.apache.commons.lang3.time.DurationFormatUtils;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class GPX {

    private static final double ASCENT_THRESHOLD_FOR_ELEVATION = 1.8;

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

    public Instant startTime() {
        return Instant.parse(firstTrackPoint().getTime().toString());
    }

    public Instant stopTime() {
        return Instant.parse(lastTrackPoint().getTime().toString());
    }

    public Duration activityDuration() {
        return Duration.between(startTime(), stopTime());
    }

    public String humanReadableActivityDuration() {
        return DurationFormatUtils.formatDurationWords(activityDuration().toMillis(), true, true);
    }

    public BigDecimal startAltitude() {
        return firstTrackPoint().getEle();
    }

    public BigDecimal stopAltitude() {
        return lastTrackPoint().getEle();
    }

    public BigDecimal lowestAltitude() {
        return trackPoints().stream().min(Comparator.comparing(WptType::getEle)).get().getEle();
    }

    public BigDecimal highestAltitude() {
        return trackPoints().stream().max(Comparator.comparing(WptType::getEle)).get().getEle();
    }

    public double elevation() {
        return elevation(ASCENT_THRESHOLD_FOR_ELEVATION);
    }

    double elevation(double ascentThresholdForElevation) {
        double elevation = 0.0;
        double lastAltitudeForElevationComputation = firstTrackPoint().getEle().doubleValue();
        double previousAltitude = firstTrackPoint().getEle().doubleValue();

        List<BigDecimal> altitudes = trackPoints().stream().map(WptType::getEle).collect(Collectors.toList());

        for (BigDecimal altitude: altitudes) {
            if (altitude.doubleValue() < previousAltitude) { // is initiating descent
                if (previousAltitude - lastAltitudeForElevationComputation > ascentThresholdForElevation) {
                    elevation += altitude.doubleValue() - lastAltitudeForElevationComputation;
                }
                lastAltitudeForElevationComputation = altitude.doubleValue();
            }
            previousAltitude = altitude.doubleValue();
        }
        return elevation;
    }
}
