package v1ch03.InputTest;

import java.util.Scanner;

public class InputTest {
    public static void main(String[] args) {
//        Scanner in = System.in;
        Scanner in = new Scanner(System.in);

        //get first input
        System.out.println("What's your name?");
        String name = in.nextLine();

        System.out.println("What's your age?");
        int age = in.nextInt();

        System.out.println("Hello, " + name + " . Next year, you'll be " + (age + 1));
    }
}
