package v1ch06.lambda;

import javax.swing.*;
import java.util.Arrays;
import java.util.Date;

/**
 * This program demonstrates the use of lambda expressions.
 */
public class LambdaTest {
    public static void main(String[] args) {
        String[] plants = new String[] {"Mercury", "Venus", "Earth", "Mars", "Jupiter", "Saturn", "Uranus", "Neptune"};
        System.out.println(Arrays.toString(plants));
        System.out.println("Sorted in dictionary order:");
        Arrays.sort(plants);
        System.out.println("Sorted by length");
        Arrays.sort(plants, (first, second) -> first.length() - second.length());
        System.out.println(Arrays.toString(plants));

        Timer timer = new Timer(1000, event -> System.out.println("The time is " + new Date()));
        timer.start();

        // keep program sunning until user selects "OK"
        JOptionPane.showMessageDialog(null, "Quit program?");
        System.exit(0);
    }
}
