import java.util.*;

public class KthSmallestCombinedReturn {
    public static int findKthSmallest(int[] returns1, int[] returns2, int k) {
        PriorityQueue<int[]> minHeap = new PriorityQueue<>(Comparator.comparingInt(a -> a[0]));
        for (int i = 0; i < returns1.length; i++) {
            minHeap.offer(new int[]{returns1[i] * returns2[0], i, 0});
        }

        int count = 0;
        while (!minHeap.isEmpty()) {
            int[] current = minHeap.poll();
            count++;
            if (count == k) {
                return current[0];
            }
            int i = current[1];
            int j = current[2];
            if (j + 1 < returns2.length) {
                minHeap.offer(new int[]{returns1[i] * returns2[j + 1], i, j + 1});
            }
        }
        return -1;
    }

    public static void main(String[] args) {
        System.out.println(findKthSmallest(new int[]{2, 5}, new int[]{3, 4}, 2)); // Output: 8
        System.out.println(findKthSmallest(new int[]{-4, -2, 0, 3}, new int[]{2, 4}, 6)); // Output: 0
    }
}
