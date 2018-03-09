package org.marcnazarian.gpxjoin;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;

public class GPXReader {

    public static GPX read(String gpxFileLocation) {
        File xml = new File(gpxFileLocation);

        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(GpxType.class);
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            ObjectFactory factory = new ObjectFactory();
            GpxType gpxType = factory.createGpxType();

            JAXBElement<GpxType> jaxbElement =  factory.createGpx(gpxType);
            jaxbElement = (JAXBElement<GpxType>)unmarshaller.unmarshal(xml);

            gpxType = jaxbElement.getValue();
            return new GPX(gpxType);
        } catch (JAXBException e) {
            System.out.println("Unable to read GPX file " + gpxFileLocation);
            e.printStackTrace();
        }
        return null;
    }

}
