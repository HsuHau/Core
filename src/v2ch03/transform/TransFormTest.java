package v2ch03.transform;

import org.xml.sax.*;
import org.xml.sax.helpers.AttributesImpl;
import v2ch02.randomAccess.Employee;

import javax.xml.stream.XMLStreamWriter;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.StringTokenizer;
import java.util.stream.StreamSupport;

/**
 * This program demonstrates XSL transformations. It applies a transformation to a set of employee
 * records. The records are stored in the file employee.dat and turned into XML format. Specify
 * the stylesheet on the command line, e.g.
 * java transform.TransformTest transform.makeprop.xsl
 */
public class TransFormTest {
    public static void main(String[] args) throws Exception{
        Path path;
        if (args.length > 0) {
            path = Paths.get(args[0]);
        } else {
            path = Paths.get("src/v2ch03/transform", "makehtml.xsl");
        }
        try (InputStream styleIn = Files.newInputStream(path)) {
            StreamSource styleSource = new StreamSource(styleIn);

            Transformer t = TransformerFactory.newInstance().newTransformer(styleSource);
            t.setOutputProperty(OutputKeys.INDENT, "yes");
            t.setOutputProperty(OutputKeys.METHOD, "xml");
            t.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");

            try (InputStream docIn = Files.newInputStream(Paths.get("src/v2ch03/transform", "employee.dat"))) {
                t.transform(new SAXSource(new EmployeeReader(), new InputSource(docIn)), new StreamResult(System.out));
            }
        }
    }
}

class EmployeeReader implements XMLReader {
    private ContentHandler handler;

    @Override
    public boolean getFeature(String name) throws SAXNotRecognizedException, SAXNotSupportedException {
        return false;
    }

    @Override
    public void setFeature(String name, boolean value) throws SAXNotRecognizedException, SAXNotSupportedException {

    }

    @Override
    public Object getProperty(String name) throws SAXNotRecognizedException, SAXNotSupportedException {
        return null;
    }

    @Override
    public void setProperty(String name, Object value) throws SAXNotRecognizedException, SAXNotSupportedException {

    }

    @Override
    public void setEntityResolver(EntityResolver resolver) {

    }

    @Override
    public EntityResolver getEntityResolver() {
        return null;
    }

    @Override
    public void setDTDHandler(DTDHandler handler) {

    }

    @Override
    public DTDHandler getDTDHandler() {
        return null;
    }

    @Override
    public void setContentHandler(ContentHandler newValue) {
        handler = newValue;
    }

    @Override
    public ContentHandler getContentHandler() {
        return handler;
    }

    @Override
    public void setErrorHandler(ErrorHandler handler) {

    }

    @Override
    public ErrorHandler getErrorHandler() {
        return null;
    }

    @Override
    public void parse(InputSource source) throws IOException, SAXException {
        InputStream stream = source.getByteStream();
        BufferedReader in = new BufferedReader(new InputStreamReader(stream));
        String rootElement = "staff";
        AttributesImpl attributes = new AttributesImpl();

        if (handler == null) throw new SAXException("No content handler");
        handler.startDocument();
        handler.startElement("", rootElement, rootElement, attributes);
        String line;
        while ((line = in.readLine()) != null) {
            handler.startElement("", "employee", "employee", attributes);
            StringTokenizer tokenizer = new StringTokenizer(line, "|");

            handler.startElement("", "name", "name", attributes);
            String s = tokenizer.nextToken();
            handler.characters(s.toCharArray(), 0, s.length());
            handler.endElement("", "name", "name");

            handler.startElement("", "salary", "salary", attributes);
            s = tokenizer.nextToken();
            handler.characters(s.toCharArray(), 0, s.length());
            handler.endElement("", "salary", "salary");

            attributes.addAttribute("", "year", "year", "CDATA", tokenizer.nextToken());
            attributes.addAttribute("", "month", "month", "CDATA", tokenizer.nextToken());
            attributes.addAttribute("", "day", "day", "CDATA", tokenizer.nextToken());
            handler.startElement("", "hiredate", "hiredate", attributes);
            handler.endElement("", "hiredate", "hiredate");
            attributes.clear();

            handler.endElement("", "employee", "employee");
        }
        handler.endElement("", rootElement, rootElement);
        handler.endDocument();
    }

    @Override
    public void parse(String systemId) throws IOException, SAXException {

    }
}