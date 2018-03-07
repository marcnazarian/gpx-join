@XmlSchema(namespace = "http://www.topografix.com/GPX/1/1",
        xmlns = {
                @XmlNs(namespaceURI = "http://www.garmin.com/xmlschemas/TrackPointExtension/v1", prefix = "gpxtpx"),
                @XmlNs(namespaceURI = "http://www.topografix.com/GPX/1/1", prefix = "")
        },
        elementFormDefault = javax.xml.bind.annotation.XmlNsForm.QUALIFIED)

package org.marcnazarian.gpxjoiner;

import javax.xml.bind.annotation.XmlNs;
import javax.xml.bind.annotation.XmlSchema;