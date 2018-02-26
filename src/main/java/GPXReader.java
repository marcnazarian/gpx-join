package main.java;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.lang.reflect.MalformedParametersException;
import java.time.Instant;

public class GPXReader {

    public static GPXFile read(String gpxFileLocation) {

        try {
            GPXFile gpxFile = new GPXFile();

            File gpxXmlFile = new File(gpxFileLocation);
            Document gpxDocument = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(gpxXmlFile);
            gpxDocument.getDocumentElement().normalize();

            NodeList nodeListMetadata = gpxDocument.getElementsByTagName("metadata");
            Node nodeMetadata = nodeListMetadata.item(0);
            if (nodeMetadata.getNodeType() == Node.ELEMENT_NODE) {
                Element elementMetadata = (Element) nodeMetadata;

                String gpxFileTime = elementMetadata.getElementsByTagName("time").item(0).getTextContent();

                gpxFile.setTime(Instant.parse(gpxFileTime));
            }

            return gpxFile;
        } catch (Exception e) {
            e.printStackTrace();
            throw new MalformedParametersException("Can't open the file " + gpxFileLocation);
        }
    }
}
