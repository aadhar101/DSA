public class CriticalTemperature {
    public static int findMinMeasurements(int k, int n) {
        int[][] dp = new int[k + 1][n + 1];
        
        for (int i = 1; i <= k; i++) {
            for (int j = 1; j <= n; j++) {
                dp[i][j] = j;
            }
        }

        for (int i = 2; i <= k; i++) {
            for (int j = 1; j <= n; j++) {
                dp[i][j] = Integer.MAX_VALUE;
                for (int x = 1; x <= j; x++) {
                    int worstCase = 1 + Math.max(dp[i - 1][x - 1], dp[i][j - x]);
                    dp[i][j] = Math.min(dp[i][j], worstCase);
                }
            }
        }

        return dp[k][n];
    }

    public static void main(String[] args) {
        System.out.println(findMinMeasurements(1, 2)); // Output: 2
        System.out.println(findMinMeasurements(2, 6)); // Output: 3
        System.out.println(findMinMeasurements(3, 14)); // Output: 4
    }
}
