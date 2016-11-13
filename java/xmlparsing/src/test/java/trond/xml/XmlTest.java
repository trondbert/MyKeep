package trond.xml;

import org.junit.Test;
import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class XmlTest {

    @Test
    public void testXerces() throws Exception {
        DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = builderFactory.newDocumentBuilder();

        String xmlString = "<?xml version=\"1.0\" encoding=\"UTF-8\"?> <foo xmlns=\"http://www.vg.no\"><bar></bar></foo>";
        ByteArrayInputStream xmlInputStream = new ByteArrayInputStream(xmlString.getBytes("UTF-8"));
        Document document = builder.parse(xmlInputStream);

        assertEquals("foo", document.getDocumentElement().getTagName());
        assertEquals("bar", document.getDocumentElement().getChildNodes().item(0).getNodeName());

    }

    @Test
    public void writeXmlJava() throws Exception {
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream("target/test.xml");
            XMLOutputFactory xmlOutFact = XMLOutputFactory.newInstance();
            XMLStreamWriter writer = xmlOutFact.createXMLStreamWriter(fos);
            writer.writeStartDocument();
            writer.writeStartElement("test");
            // write stuff
            writer.writeEndElement();
            writer.flush();
        }
        catch(IOException exc) {
        }
        catch(XMLStreamException exc) {
        }
        finally {
        }

    }
}