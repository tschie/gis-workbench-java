package com.esri.arcgisruntime.opensourceapps.gisworkbench.xml;

import javax.xml.bind.*;
import javax.xml.namespace.QName;
import javax.xml.transform.stream.StreamSource;
import java.io.File;

public class XmlFileSerializer {

    public <T> void serialize(File file, T component) {
        try {
            //Create Marshaller
            JAXBContext jaxbContext = JAXBContext.newInstance(component.getClass());

            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

            //Required formatting??
            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

            //Write XML to StringWriter
            jaxbMarshaller.marshal(new JAXBElement<>(new QName(component.getClass().getSimpleName()),
                            (Class<T>) component.getClass(), component), file);

            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

            JAXBElement<T> jaxbElement = jaxbUnmarshaller.unmarshal(new StreamSource(file),
                    (Class<T>) component.getClass());
        } catch (JAXBException ex) {
            ex.printStackTrace();
        }
    }

    public <T> T deserialize(File file, Class<T> componentClass) {
        try {
            if (file != null) {
                JAXBContext jaxbContext = JAXBContext.newInstance(componentClass.getPackage().getName(),
                        componentClass.getClassLoader());

                Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

                JAXBElement<T> jaxbElement = jaxbUnmarshaller.unmarshal(new StreamSource(file), componentClass);

                return jaxbElement.getValue();
            }
        } catch (JAXBException e) {
            e.printStackTrace();
        }
        return null;
    }
}
