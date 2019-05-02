package v2ch03.read;

import org.w3c.dom.*;

import javax.swing.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.awt.*;
import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.File;
import java.lang.reflect.Field;

/**
 * This panel uses an XML file to describe its components and their grid bag layout positions.
 */
public class GridBagPane extends JPanel {
    private GridBagConstraints gridBagConstraints;

    /**
     * Constructs a grid bag pane.
     * @param file the name of the XML file that describes the pane's components and their position
     */
    public GridBagPane(File file) {
        setLayout(new GridBagLayout());
        gridBagConstraints = new GridBagConstraints();

        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setValidating(true);

            if (file.toString().contains("-schema")) {
                factory.setNamespaceAware(true);
                final String JAXP_SCHEMA_LANGUAGE = "http://java.sun.com/xml/jaxp/properties/schemaLanguage";
                final String W3C_XML_SCHEMA = "http://www.w3.org/2001/XMLSchema";
                factory.setAttribute(JAXP_SCHEMA_LANGUAGE, W3C_XML_SCHEMA);
            }

            factory.setIgnoringElementContentWhitespace(true);

            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(file);
            parseGridbag(document.getDocumentElement());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Gets a component with a given name.
     * @param name a component name
     * @return the component with the given name, or null if no component in this grid bag pane has the given name
     */
    public Component get(String name) {
        Component[] components = getComponents();
        for (int i = 0; i < components.length; i++) {
            if (components[i].getName().equals(name)) {
                return components[i];
            }
        }
        return null;
    }

    /**
     * Parses a gridbag element.
     * @param element a gridbag element
     */
    private void parseGridbag(Element element) {
        NodeList rows = element.getChildNodes();
        for (int i = 0; i < rows.getLength(); i++) {
            Element row = (Element) rows.item(i);
            NodeList cells = row.getChildNodes();
            for (int j = 0; j < cells.getLength(); j++) {
                Element cell = (Element) cells.item(j);
                parseCell(cell, i, j);
            }
        }
    }

    private void parseCell(Element element, int r, int c) {
        // get attributes

        String value = element.getAttribute("gridx");
        if (value.length() == 0) // use default
        {
            if (c == 0) {
                gridBagConstraints.gridx = 0;
            } else {
                gridBagConstraints.gridx += gridBagConstraints.gridwidth;
            }
        } else {
            gridBagConstraints.gridx = Integer.parseInt(value);
        }
        value = element.getAttribute("gridy");
        if (value.length() == 0) {
            gridBagConstraints.gridy = r;
        } else {
            gridBagConstraints.gridy = Integer.parseInt(value);
        }

        gridBagConstraints.gridwidth = Integer.parseInt(element.getAttribute("gridwidth"));
        gridBagConstraints.gridheight = Integer.parseInt(element.getAttribute("gridheight"));
        gridBagConstraints.weightx = Integer.parseInt(element.getAttribute("weightx"));
        gridBagConstraints.weighty = Integer.parseInt(element.getAttribute("weighty"));
        gridBagConstraints.ipadx = Integer.parseInt(element.getAttribute("ipadx"));
        gridBagConstraints.ipady = Integer.parseInt(element.getAttribute("ipady"));

        // use reflection to get integer values of static fields
        Class<GridBagConstraints> cl = GridBagConstraints.class;

        try {
            String name = element.getAttribute("fill");
            Field field = cl.getField(name);
            gridBagConstraints.fill = field.getInt(cl);

            name = element.getAttribute("anchor");
            field = cl.getField(name);
            gridBagConstraints.anchor = field.getInt(cl);
        } catch (Exception e) // the reflection methods can throw various exceptions
        {
            e.printStackTrace();
        }

        Component component = (Component) parseBean((Element) element.getFirstChild());
        add(component, gridBagConstraints);
    }

    /**
     * Parses a bean element.
     *
     * @parse element a bean element
     */
    private Object parseBean(Element element) {
        try {
            NodeList children = element.getChildNodes();
            Element classElement = (Element) children.item(0);
            String className = ((Text) classElement.getFirstChild()).getData();

            Class<?> cl = Class.forName(className);

            Object object = cl.newInstance();

            if (object instanceof Component) {
                ((Component) object).setName(element.getAttribute("id"));
            }

            for (int i = 1; i < children.getLength(); i++) {
                Node propertyElement = children.item(i);
                Element nameElement = (Element) propertyElement.getFirstChild();
                String propertyName = ((Text) nameElement.getFirstChild()).getData();

                Element valueElement = (Element) propertyElement.getLastChild();
                Object value = parseValue(valueElement);
                BeanInfo beanInfo = Introspector.getBeanInfo(cl);
                PropertyDescriptor[] descriptors = beanInfo.getPropertyDescriptors();
                boolean done = false;
                for (int j = 0; !done && j < descriptors.length; j++) {
                    if (descriptors[j].getName().equals(propertyName)) {
                        descriptors[j].getWriteMethod().invoke(object, value);
                        done = true;
                    }
                }
            }
            return object;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Parses a value element.
     *
     * @param element a value element
     */
    private Object parseValue(Element element) {
        Element child = (Element) element.getFirstChild();
        if (child.getTagName().equals("bean")) {
            return parseBean(child);
        }
        String text = ((Text) child.getFirstChild()).getData();
        if (child.getTagName().equals("int")) {
            return new Integer(text);
        } else if (child.getTagName().equals("boolean")) {
            return new Boolean(text);
        } else if (child.getTagName().equals("string")) {
            return text;
        } else return null;
    }
}
