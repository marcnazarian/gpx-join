package main.java;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.lang.reflect.MalformedParametersException;
import java.time.Instant;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class GPXReader {

    private static final String TRACK_NODE_NAME = "trk";
    private static final String METADATA_NODE_NAME = "metadata";
    private static final String TIME_NODE_NAME = "time";
    private static final String TRACK_NAME_NODE_NAME = "name";
    private static final String TRACK_SEGMENT_NODE_NAME = "trkseg";
    private static final String TRACK_POINT_NODE_NAME = "trkpt";

    public static GPXFile read(String gpxFileLocation) {

        try {
            GPXFile gpxFile = new GPXFile();

            File gpxXmlFile = new File(gpxFileLocation);
            Document gpxDocument = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(gpxXmlFile);
            gpxDocument.getDocumentElement().normalize();

            String time = readTime(gpxDocument);
            if (time != null) gpxFile.setTime(Instant.parse(time));

            gpxFile.setTrackName(readName(gpxDocument));

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
        NodeList nodeListMetadata = gpxDocument.getElementsByTagName(METADATA_NODE_NAME);
        if (nodeListMetadata.getLength() > 0) {
            Node nodeMetadata = nodeListMetadata.item(0);

            NodeList nodeListMetadataTime = ((Element) nodeMetadata).getElementsByTagName(TIME_NODE_NAME);
            if (nodeListMetadataTime.getLength() > 0) {
                return nodeListMetadataTime.item(0).getTextContent();
            }
        }
        return null;
    }

    private static String tryToReadTimeFromTrack(Document gpxDocument) {
        NodeList nodeListTracks = gpxDocument.getElementsByTagName(TRACK_NODE_NAME);
        if (nodeListTracks.getLength() > 0) {
            Node nodeTrack = nodeListTracks.item(0);

            NodeList nodeListTrackSegments = ((Element) nodeTrack).getElementsByTagName(TRACK_SEGMENT_NODE_NAME);
            if (nodeListTrackSegments.getLength() > 0) {
                Node nodeTrackSegment = nodeListTrackSegments.item(0);

                NodeList nodeListTrackPoints = ((Element) nodeTrackSegment).getElementsByTagName(TRACK_POINT_NODE_NAME);
                if (nodeListTrackPoints.getLength() > 0) {
                    Node firstNodeTrackPoint = nodeListTrackPoints.item(0);

                    NodeList nodeListTrackPointTimes = ((Element) firstNodeTrackPoint).getElementsByTagName(TIME_NODE_NAME);
                    if (nodeListTrackPointTimes != null && nodeListTrackPointTimes.getLength() != 0) {
                        return nodeListTrackPointTimes.item(0).getTextContent();
                    }
                }
            }
        }
        return null;
    }

    private static String readName(Document gpxDocument) {
        NodeList nodeListTracks = gpxDocument.getElementsByTagName(TRACK_NODE_NAME);
        if (nodeListTracks.getLength() > 0) {
            Node nodeTrack = nodeListTracks.item(0);

            NodeList nodeListTrackNames = ((Element) nodeTrack).getElementsByTagName(TRACK_NAME_NODE_NAME);
            if (nodeListTrackNames != null && nodeListTrackNames.getLength() != 0) {
                return nodeListTrackNames.item(0).getTextContent();
            }
        }
        return "";
    }

    private static NodeList readTrackPoints(Document gpxDocument) {
        NodeList nodeListTracks = gpxDocument.getElementsByTagName(TRACK_NODE_NAME);
        if (nodeListTracks.getLength() > 0) {
            Node nodeTrack = nodeListTracks.item(0);

            NodeList nodeListTrackSegments = ((Element) nodeTrack).getElementsByTagName(TRACK_SEGMENT_NODE_NAME);
            if (nodeListTrackSegments.getLength() > 0) {
                Node nodeTrackSegment = nodeListTrackSegments.item(0);

                return ((Element) nodeTrackSegment).getElementsByTagName(TRACK_POINT_NODE_NAME);
            }
        }
        return null;
    }

    public static GPXFile combine(String gpxFileLocation1, String gpxFileLocation2) throws ParserConfigurationException, TransformerException {
        GPXFile gpxFile1 = read(gpxFileLocation1);
        GPXFile gpxFile2 = read(gpxFileLocation2);

        Document combinedFiles = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
        Element rootGpxElement = combinedFiles.createElementNS("http://www.topografix.com/GPX/1/1", "gpx");

        Element track = combinedFiles.createElement(TRACK_NODE_NAME);
        rootGpxElement.appendChild(track);

        Element trackName = combinedFiles.createElement(TRACK_NAME_NODE_NAME);
        trackName.appendChild(combinedFiles.createTextNode(gpxFile1.trackName()));
        track.appendChild(trackName);

        Element trackSegment = combinedFiles.createElement(TRACK_SEGMENT_NODE_NAME);
        track.appendChild(trackSegment);

        for (Node trackPoint : iterable(gpxFile1.trackPoints())) {
            Node trackPointNodeCopy = combinedFiles.importNode(trackPoint, true);
            trackSegment.appendChild(trackPointNodeCopy);
        }

        for (Node trackPoint : iterable(gpxFile2.trackPoints())) {
            Node trackPointNodeCopy = combinedFiles.importNode(trackPoint, true);
            trackSegment.appendChild(trackPointNodeCopy);
        }


        combinedFiles.appendChild(rootGpxElement);


        // Write XML file
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        DOMSource source = new DOMSource(combinedFiles);
        StreamResult file = new StreamResult(new File("src/test/resources/out/combined.gpx"));
        transformer.transform(source, file);


        return read("src/test/resources/out/combined.gpx");
    }

    private static Iterable<Node> iterable(final NodeList nodeList) {
        return () -> new Iterator<Node>() {

            private int index = 0;

            @Override
            public boolean hasNext() {
                return index < nodeList.getLength();
            }

            @Override
            public Node next() {
                if (!hasNext())
                    throw new NoSuchElementException();
                return nodeList.item(index++);
            }
        };
    }
}
