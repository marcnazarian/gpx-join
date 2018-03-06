package org.marcnazarian.gpxjoiner;

import javax.xml.bind.*;
import javax.xml.namespace.QName;
import java.io.File;

public class GPXJoin {

    public static void main(String[] args) {
        if (args.length == 3) {
            try {
                String gpxFile1 = args[0];
                String gpxFile2 = args[1];
                String outputCombinedGpxFile = args[2];

                combine(gpxFile1, gpxFile2, outputCombinedGpxFile);
            } catch (JAXBException e) {
                System.out.println("Unable to combine the GPX file: " + e.getMessage());
                e.printStackTrace();
                System.exit(1);
            } catch (Exception e) {
                System.out.println("Unable to combine the GPX file. Unexpected exception: " + e.getMessage());
                e.printStackTrace();
                System.exit(1);
            }
        } else {
            usage();
        }
    }

    private static void usage() {
        System.out.println("GPXJoin - combine two gpx tracks into a single one.");
        System.out.println("  Usage: java GPXJoin file1.gpx file2.gpx outputCombinedGpxFile.gpx");
    }

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