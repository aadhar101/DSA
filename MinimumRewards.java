import java.util.PriorityQueue;

public class MinimumRewards {
    public static int minRewards(int[] ratings) {
        int n = ratings.length;
        int[] rewards = new int[n];

        // Min-Heap to process employees by increasing rating
        PriorityQueue<Integer> minHeap = new PriorityQueue<>((a, b) -> ratings[a] - ratings[b]);

        // Initialize heap with all employee indices
        for (int i = 0; i < n; i++) {
            minHeap.offer(i);
        }

        // Process employees in order of their ratings
        while (!minHeap.isEmpty()) {
            int i = minHeap.poll();
            rewards[i] = 1; // Every employee gets at least 1 reward

            // Check left neighbor
            if (i > 0 && ratings[i] > ratings[i - 1]) {
                rewards[i] = Math.max(rewards[i], rewards[i - 1] + 1);
            }

            // Check right neighbor
            if (i < n - 1 && ratings[i] > ratings[i + 1]) {
                rewards[i] = Math.max(rewards[i], rewards[i + 1] + 1);
            }
        }

        // Sum up total rewards
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
