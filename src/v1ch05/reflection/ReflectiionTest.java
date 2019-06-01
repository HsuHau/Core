package v1ch05.reflection;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Objects;
import java.util.Scanner;

/**
 * This program uses reflection to print all features of a class.
 */
public class ReflectiionTest {
    public static void main(String[] args) {
        // read class name form command line args or user input
        String name;
        if (args.length > 0) {
            name = args[0];
        } else {
            Scanner in = new Scanner(System.in);
            System.out.println("Enter class name (e.g. java.util.Date");
            name = in.next();
        }

        try {
            // print class name and superclass name (if != Object)
            Class cl = Class.forName(name);
            Class superCl = cl.getSuperclass();
            String modifiers = Modifier.toString(cl.getModifiers());
            if (modifiers.length() > 0) {
                System.out.print(modifiers + " ");
            }
            System.out.println("class " + name);
            if (superCl != null && superCl != Object.class) {
                System.out.println(" extends " + superCl.getName());
            }
            System.out.print("\n{\n");
            printConstructors(cl);
            System.out.println();
            printMethods(cl);
            System.out.println();
            printFields(cl);
            System.out.println("}");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        System.exit(0);
    }


    /**
     * Prints all constructors of a class
     * @param cl a class
     */
    public static void printConstructors(Class cl) {
        Constructor[] constructors = cl.getDeclaredConstructors();

        for (Constructor constructor : constructors) {
            String name = constructor.getName();
            System.out.println("  ");
            String modifiers = Modifier.toString(constructor.getModifiers());
            if (modifiers.length() > 0) {
                System.out.println(modifiers + " ");
            }
            System.out.println(")");
        }
    }


    public static void printMethods(Class cl) {
        Method[] methods = cl.getDeclaredMethods();

        for (Method method : methods) {
            Class retType = method.getReturnType();
            String name = method.getName();

            System.out.println("  ");
            // print modifiers, return type and method name
            String modifiers = Modifier.toString(method.getModifiers());
            if (modifiers.length() > 0) {
                System.out.println(modifiers + " ");
            }
            System.out.println(retType.getName() + " " + name + "(");

            // prints parameter types
            Class[] paramType = method.getParameterTypes();
            for (int j = 0; j < paramType.length; j++) {
                if (j > 0) {
                    System.out.println(", ");
                }
            }
            System.out.println(");");
        }
    }

    /**
     * Prints all fields of a class.
     * @param cl
     */
    public static void printFields(Class cl) {
        Field[] fields = cl.getDeclaredFields();

        for (Field field : fields) {
            Class type = field.getType();
            String name = field.getName();
            System.out.println("  ");
            String modifiers = Modifier.toString(field.getModifiers());
            if (modifiers.length() > 0) {
                System.out.println(modifiers + " ");
            }
            System.out.println(type.getName() + " " + name + ";");
        }
    }
}
