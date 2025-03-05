import java.util.*;

public class ClosestPoints {
    public static int[] findClosestPoints(int[] x_coords, int[] y_coords) {
        int minDistance = Integer.MAX_VALUE;
        int[] result = new int[2];

        for (int i = 0; i < x_coords.length; i++) {
            for (int j = i + 1; j < x_coords.length; j++) { // Compare pairs of points
                int distance = Math.abs(x_coords[i] - x_coords[j]) + Math.abs(y_coords[i] - y_coords[j]);
                if (distance < minDistance || (distance == minDistance && (i < result[0] || (i == result[0] && j < result[1])))) {
                    minDistance = distance;
                    result[0] = i;
                    result[1] = j;
                }
            }
        }
        return result;
    }

    public static void main(String[] args) {
        int[] result = findClosestPoints(new int[]{1, 2, 3, 2, 4}, new int[]{2, 3, 1, 2, 3});
        System.out.println(Arrays.toString(result)); 
    }
}
