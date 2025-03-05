import java.util.*;

public class MinimumRewards {
    public static int minRewards(int[] ratings) {
        int n = ratings.length;
        int[] rewards = new int[n];
        Arrays.fill(rewards, 1);

        for (int i = 1; i < n; i++) {
            if (ratings[i] > ratings[i - 1]) {
                rewards[i] = rewards[i - 1] + 1;
            }
        }

        for (int i = n - 2; i >= 0; i--) {
            if (ratings[i] > ratings[i + 1]) {
                rewards[i] = Math.max(rewards[i], rewards[i + 1] + 1);
            }
        }

        int sum = 0;
        for (int reward : rewards) {
            sum += reward;
        }
        return sum;
    }

    public static void main(String[] args) {
        System.out.println(minRewards(new int[]{1, 0, 2})); // Output: 5
        System.out.println(minRewards(new int[]{1, 2, 2})); // Output: 4
    }
}