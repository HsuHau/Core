package v2ch09.verifier;

import java.applet.Applet;
import java.awt.*;

public class VerifierTest extends Applet {
    public static void main(String[] args) {
        System.out.println("1 + 2 == " + fun());
    }

    public static int fun() {
        int m;
        int n;
        m = 1;
        n = 2;
        // use hex editor to change to "m = 2" in class file
        int r = m + n;
        return r;
    }

    public void paint(Graphics graphics) {
        graphics.drawString("1 + 2 == " + fun(), 20, 20);
    }
}
