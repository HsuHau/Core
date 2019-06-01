package v1ch06.staticInnerClass;

/**
 * This program demonstrates the use of static inner classes.
 */
public class StaticInnderClassTest {
    public static void main(String[] args) {
        double[] d = new double[20];
        for (int i = 0; i < d.length; i++) {
            d[i] = 100 * Math.random();
        }
        ArrayAlg.Pair pair = ArrayAlg.minmax(d);
        System.out.println("min = " + pair.getFirst());
        System.out.println("min = " + pair.getSecond());
    }
}

class ArrayAlg {
    public static class Pair {
        private double first;
        private double second;

        /**
         * Constructs a pair from two floating-point numbers
         *
         * @param f the first number
         * @param s the second number
         */
        public Pair(double f, double s) {
            first = f;
            second = s;
        }

        /**
         * Returns the first number of the pair
         * @return the first number
         */
        public double getFirst() {
            return first;
        }

        /**
         * Returns the second number of the pair
         * @return the second number
         */
        public double getSecond() {
            return second;
        }
    }

    public static Pair minmax(double[] values) {
        double min = Double.POSITIVE_INFINITY;
        double max = Double.NEGATIVE_INFINITY;
        for (double value : values) {
            if (min > value) {
                min = value;
            }
            if (max < value) {
                max = value;
            }
        }
        return new Pair(min, max);
    }
}
