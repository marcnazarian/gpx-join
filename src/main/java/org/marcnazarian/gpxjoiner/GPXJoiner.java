package org.marcnazarian.gpxjoiner;

import javax.xml.bind.*;
import javax.xml.namespace.QName;
import java.io.File;

public class GPXJoiner {
    
    public static GPX combine(GPX gpx1, GPX gpx2) {
        if (gpx1.time().isBefore(gpx2.time())) {
            gpx1.addTrackPoints(gpx2.trackPoints());
            return gpx1;
        } else {
            gpx2.addTrackPoints(gpx1.trackPoints());
            return gpx2;
        }
    }

    public static void combine(String gpxFileLocation1, String gpxFileLocation2, String gpxCombinedOutputLocation) throws JAXBException {
        GPX gpx1 = GPXReader.read(gpxFileLocation1);
        GPX gpx2 = GPXReader.read(gpxFileLocation2);

        if (gpx1 == null || gpx2 == null) {
            throw new JAXBException("Unable to read " + gpxFileLocation1 + " or " + gpxFileLocation2);
        }

        GPX gpxCombined = combine(gpx1, gpx2);

        JAXBContext jaxbContext = JAXBContext.newInstance(GpxType.class);
        Marshaller marshaller = jaxbContext.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        QName qName = new QName("http://www.topografix.com/GPX/1/1", "gpx");
        JAXBElement<GpxType> root = new JAXBElement<>(qName, GpxType.class, gpxCombined.gpxType);
        marshaller.marshal(root, new File(gpxCombinedOutputLocation));
    }

}