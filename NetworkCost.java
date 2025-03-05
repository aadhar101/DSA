import java.util.Arrays;
import java.util.Comparator;

public class NetworkCost {
    public static int minNetworkCost(int n, int[] modules, int[][] connections) {
        int[] parent = new int[n + 1];
        for (int i = 1; i <= n; i++) parent[i] = i;

        int[][] allConnections = new int[connections.length + n][3];
        System.arraycopy(connections, 0, allConnections, 0, connections.length);
        for (int i = 1; i <= n; i++) {
            allConnections[connections.length + i - 1] = new int[]{0, i, modules[i - 1]};
        }

        Arrays.sort(allConnections, Comparator.comparingInt(a -> a[2]));

        int totalCost = 0, edges = 0;
        for (int[] conn : allConnections) {
            int a = find(parent, conn[0]);
            int b = find(parent, conn[1]);
            if (a != b) {
                parent[a] = b;
                totalCost += conn[2];
                edges++;
            }
        }
        return edges == n ? totalCost : -1;
    }

    private static int find(int[] parent, int i) {
        if (parent[i] != i) parent[i] = find(parent, parent[i]);
        return parent[i];
    }

    public static void main(String[] args) {
        System.out.println(minNetworkCost(3, new int[]{1, 2, 2}, new int[][]{{1, 2, 1}, {2, 3, 1}}));
    }
}

