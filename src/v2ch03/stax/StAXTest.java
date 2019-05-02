package v2ch03.stax;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamReader;
import java.io.InputStream;
import java.net.URL;

/**
 * This program demonstrates how to use a SAtX parser. The program prints all hyperlinks of an XHTML web page. <br>
 * Usage: java stax.StAXTest URL
 */
public class StAXTest {
    public static void main(String[] args) throws Exception{
        String urlString;
        if (args.length == 0) {
            urlString = "http://www.w3c.org";
            System.out.println("Using " + urlString);
        } else {
            urlString = args[0];
            URL url = new URL(urlString);
            InputStream in = url.openStream();
            XMLInputFactory factory = XMLInputFactory.newInstance();
            XMLStreamReader parser = factory.createXMLStreamReader(in);
            while (parser.hasNext()) {
                int event = parser.next();
                if (event == XMLStreamConstants.START_ELEMENT) {
                    String href = parser.getAttributeValue(null, "href");
                    if (href != null) {
                        System.out.println(href);
                    }
                }
            }
        }
    }
}
