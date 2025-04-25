import java.math.BigInteger;
import java.util.*;

public class ShamirSecret {

    static class Point {
        int x;
        BigInteger y;
        Point(int x, BigInteger y) {
            this.x = x;
            this.y = y;
        }
    }

    public static void main(String[] args) {
        // ----------- Test Case 1 ----------
        int n1 = 4, k1 = 3;
        Map<Integer, String[]> data1 = new HashMap<>();
        data1.put(1, new String[]{"10", "4"});
        data1.put(2, new String[]{"2", "111"});
        data1.put(3, new String[]{"10", "12"});
        data1.put(6, new String[]{"4", "213"});

        // ----------- Test Case 2 ----------
        int n2 = 10, k2 = 7;
        Map<Integer, String[]> data2 = new HashMap<>();
        data2.put(1, new String[]{"6", "13444211440455345511"});
        data2.put(2, new String[]{"15", "aed7015a346d63"});
        data2.put(3, new String[]{"15", "6aeeb69631c227c"});
        data2.put(4, new String[]{"16", "e1b5e05623d881f"});
        data2.put(5, new String[]{"8", "316034514573652620673"});
        data2.put(6, new String[]{"3", "2122212201122002221120200210011020220200"});
        data2.put(7, new String[]{"3", "20120221122211000100210021102001201112121"});
        data2.put(8, new String[]{"6", "20220554335330240002224253"});
        data2.put(9, new String[]{"12", "45153788322a1255483"});
        data2.put(10, new String[]{"7", "1101613130313526312514143"});

        BigInteger secret1 = findSecret(n1, k1, data1);
        BigInteger secret2 = findSecret(n2, k2, data2);

        System.out.println("Secret 1: " + secret1);
        System.out.println("Secret 2: " + secret2);
    }

    static BigInteger findSecret(int n, int k, Map<Integer, String[]> data) {
        List<Point> points = new ArrayList<>();
        for (Map.Entry<Integer, String[]> entry : data.entrySet()) {
            int x = entry.getKey();
            int base = Integer.parseInt(entry.getValue()[0]);
            String value = entry.getValue()[1];
            BigInteger y = new BigInteger(value, base);
            points.add(new Point(x, y));
        }

        // Sort points by x and select first k points
        points.sort(Comparator.comparingInt(p -> p.x));
        return lagrangeInterpolation(points.subList(0, k));
    }

    static BigInteger lagrangeInterpolation(List<Point> points) {
        BigInteger result = BigInteger.ZERO;
        int k = points.size();

        for (int i = 0; i < k; i++) {
            BigInteger xi = BigInteger.valueOf(points.get(i).x);
            BigInteger yi = points.get(i).y;

            BigInteger num = BigInteger.ONE;
            BigInteger den = BigInteger.ONE;

            for (int j = 0; j < k; j++) {
                if (i == j) continue;
                BigInteger xj = BigInteger.valueOf(points.get(j).x);
                num = num.multiply(xj.negate());          // -xj
                den = den.multiply(xi.subtract(xj));      // (xi - xj)
            }

            BigInteger term = yi.multiply(num).divide(den);
            result = result.add(term);
        }

        return result;
    }
}