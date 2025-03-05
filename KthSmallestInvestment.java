public class KthSmallestInvestment {
    public static int findKthSmallestSum(int[] returns1, int[] returns2, int k) {
        int low = returns1[0] + returns2[0];
        int high = returns1[returns1.length - 1] + returns2[returns2.length - 1];

        while (low < high) {
            int mid = low + (high - low) / 2;
            if (countLessOrEqual(returns1, returns2, mid) >= k) {
                high = mid;
            } else {
                low = mid + 1;
            }
        }
        return low;
    }

    private static int countLessOrEqual(int[] returns1, int[] returns2, int target) {
        int count = 0, j = returns2.length - 1;
        for (int num : returns1) {
            while (j >= 0 && num + returns2[j] > target) {
                j--;  // Move left in returns2 to find valid sums
            }
            count += (j + 1);
        }
        return count;
    }

    public static void main(String[] args) {
        System.out.println(findKthSmallestSum(new int[]{2, 5}, new int[]{3, 4}, 2)); // Output: 8
        System.out.println(findKthSmallestSum(new int[]{-4, -2, 0, 3}, new int[]{2, 4}, 6)); // Output: 0
    }
}
