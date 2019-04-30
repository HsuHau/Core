/*
package v2ch08.script;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineFactory;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.swing.*;
import java.awt.*;
import java.beans.EventSetDescriptor;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class ScriptTest {
    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                ScriptEngineManager scriptEngineManager = new ScriptEngineManager();
                String language;
                if (args.length == 0) {
                    System.out.println("Available factories: ");
                    for (ScriptEngineFactory scriptEngineFactory : scriptEngineManager.getEngineFactories()) {
                        System.out.println(scriptEngineFactory.getEngineName());
                    }
                    language = "nashorn";
                } else {
                    language = args[0];
                }
                final ScriptEngine engine = scriptEngineManager.getEngineByName(language);
                if (engine == null) {
                    System.err.println("No engine for" + language);
                    System.exit(1);
                }

                final String frameClassName = args.length < 2 ? "buttons1.ButtonFrame" : args[1];
                JFrame frame = (JFrame) Class.forName(frameClassName).newInstance();
                InputStream inputStream = frame.getClass().getResourceAsStream("init." + language);
                if (inputStream != null) {
                    engine.eval(new InputStreamReader(inputStream));
                }
                Map<String, Component> components = new HashMap<>();
                getComponentBindings(frame, components);
                components.forEach((name, c) -> engine.put(name, c));

                final Properties properties = new Properties();
                inputStream = frame.getClass().getResourceAsStream(language + ".properties");
                properties.load(inputStream);

                for (final Object o : properties.keySet()) {
                    String[] s = ((String) o).split("\\.");
                    addListener(s[0], s[1], (String) properties.get(o), engine, components);
                }
                frame.setTitle("ScriptTest");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setVisible(true);
            } catch (ReflectiveOperationException | IOException | ScriptException | IntrospectionException e) {
                e.printStackTrace();
            }
        });
    }

    */
/**
     * Gathers all named components in a container.
     * @param component the component
     * @param namedComponents a map into which to enter the component names and components
     *//*

    private static void getComponentBindings(Component component, Map<String, Component> namedComponents) {
        String name = component.getName();
        if (name != null) {
            namedComponents.put(name, component);
        }
        if (component instanceof  Container) {
            for (Component child : ((Container) component).getComponents()) {
                getComponentBindings(child, namedComponents);
            }
        }
    }

    */
/**
     * Adds a listener to an object whose listener method executes a script.
     * @param beanName the name of the bean to which the listener should be added
     * @param eventName the name of the listener type, such as "action" or "change"
     * @param scriptCode the script code to be executed
     * @param engine the engine that executes the world
     * @param componentMap the bindings for the execution
     * @throws ReflectiveOperationException
     * @throws IntrospectionException
     *//*

    private static void addListener(String beanName, String eventName, final String scriptCode, final ScriptEngine engine, Map<String, Component> componentMap) throws ReflectiveOperationException, IntrospectionException {
        Object bean = componentMap.get(beanName);
        EventSetDescriptor descriptor = getEventSetDescriptor(bean, eventName);
        if (descriptor == null) {
            return;
        }
        descriptor.getAddListenerMethod().invoke(bean,
                Proxy.newProxyInstance(null, new Class[] { descriptor.getListenerType()},
                        (proxy, method, args) -> {
                                engine.eval(scriptCode);
                                return null;
                        }));
    }

    private static EventSetDescriptor getEventSetDescriptor(Object bean, String eventName) throws IntrospectionException {
        for (EventSetDescriptor eventSetDescriptor : Introspector.getBeanInfo(bean.getClass()).getEventSetDescriptors()) {
            {
                if (eventSetDescriptor.getName().equals(eventName)) {
                    return eventSetDescriptor;
                }
            }
        }
        return null;
    }
}
*/
