public class CriticalTemperature {
    public static int findMinMeasurements(int k, int n) {
        int moves = 0;
        int[][] tested = new int[k + 1][n + 1];

        while (tested[k][moves] < n) {
            moves++;
            for (int i = 1; i <= k; i++) {
                tested[i][moves] = tested[i - 1][moves - 1] + tested[i][moves - 1] + 1;
            }
        }
        return moves;
    }

    public static void main(String[] args) {
        System.out.println(findMinMeasurements(1, 2));  // Output: 2
        System.out.println(findMinMeasurements(2, 6));  // Output: 3
        System.out.println(findMinMeasurements(3, 14)); // Output: 4
    }
}
