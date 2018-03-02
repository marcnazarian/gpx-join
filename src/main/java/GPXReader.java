package main.java;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.lang.reflect.MalformedParametersException;
import java.time.Instant;
import java.util.List;

public class GPXReader {

    public static GPXFile read(String gpxFileLocation) {

        try {
            GPXFile gpxFile = new GPXFile();

            File gpxXmlFile = new File(gpxFileLocation);
            Document gpxDocument = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(gpxXmlFile);
            gpxDocument.getDocumentElement().normalize();

            String time = readTime(gpxDocument);
            if (time != null) gpxFile.setTime(Instant.parse(time));

            gpxFile.setTrackPoints(readTrackPoints(gpxDocument));

            return gpxFile;
            
        } catch (Exception e) {
            e.printStackTrace();
            throw new MalformedParametersException("Can't open the file " + gpxFileLocation);
        }
    }

    private static String readTime(Document gpxDocument) {
        String gpxFileTime = tryToReadTimeFromMetadata(gpxDocument);

        if (gpxFileTime == null) {
            gpxFileTime = tryToReadTimeFromTrack(gpxDocument);
        }

        return gpxFileTime;
    }

    private static String tryToReadTimeFromMetadata(Document gpxDocument) {
        NodeList nodeListMetadata = gpxDocument.getElementsByTagName("metadata");
        if (nodeListMetadata.getLength() > 0) {
            Node nodeMetadata = nodeListMetadata.item(0);

            NodeList nodeListMetadataTime = ((Element) nodeMetadata).getElementsByTagName("time");
            if (nodeListMetadataTime.getLength() > 0) {
                return nodeListMetadataTime.item(0).getTextContent();
            }
        }
        return null;
    }

    private static String tryToReadTimeFromTrack(Document gpxDocument) {
        NodeList nodeListTracks = gpxDocument.getElementsByTagName("trk");
        if (nodeListTracks.getLength() > 0) {
            Node nodeTrack = nodeListTracks.item(0);

            NodeList nodeListTrackSegments = ((Element) nodeTrack).getElementsByTagName("trkseg");
            if (nodeListTrackSegments.getLength() > 0) {
                Node nodeTrackSegment = nodeListTrackSegments.item(0);

                NodeList nodeListTrackPoints = ((Element) nodeTrackSegment).getElementsByTagName("trkpt");
                if (nodeListTrackPoints.getLength() > 0) {
                    Node firstNodeTrackPoint = nodeListTrackPoints.item(0);

                    NodeList nodeListTrackPointTimes = ((Element) firstNodeTrackPoint).getElementsByTagName("time");
                    if (nodeListTrackPointTimes != null && nodeListTrackPointTimes.getLength() != 0) {
                        return nodeListTrackPointTimes.item(0).getTextContent();
                    }
                }
            }
        }
        return null;
    }

    private static NodeList readTrackPoints(Document gpxDocument) {
        NodeList nodeListTracks = gpxDocument.getElementsByTagName("trk");
        if (nodeListTracks.getLength() > 0) {
            Node nodeTrack = nodeListTracks.item(0);

            NodeList nodeListTrackSegments = ((Element) nodeTrack).getElementsByTagName("trkseg");
            if (nodeListTrackSegments.getLength() > 0) {
                Node nodeTrackSegment = nodeListTrackSegments.item(0);

                return ((Element) nodeTrackSegment).getElementsByTagName("trkpt");
            }
        }
        return null;
    }
}
