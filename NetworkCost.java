import java.util.List;
import java.util.ArrayList;
import java.util.Comparator;

public class NetworkCost {
    public static int minNetworkCost(int n, int[] modules, int[][] connections) {
        // Initialize Union-Find (Disjoint Set) for n devices
        int[] parent = new int[n + 1];  // parent[i] points to the parent of i
        int[] rank = new int[n + 1];    // rank for union by rank
        
        // Initialize each device to be its own parent (disjoint sets)
        for (int i = 1; i <= n; i++) {
            parent[i] = i;
        }

        // Helper function to find the root of a device (with path compression)
        int find(int x) {
            if (parent[x] != x) {
                parent[x] = find(parent[x]);  // Path compression
            }
            return parent[x];
        }

        // Helper function to union two sets (with union by rank)
        void union(int x, int y) {
            int rootX = find(x);
            int rootY = find(y);
            if (rootX != rootY) {
                if (rank[rootX] > rank[rootY]) {
                    parent[rootY] = rootX;
                } else if (rank[rootX] < rank[rootY]) {
                    parent[rootX] = rootY;
                } else {
                    parent[rootY] = rootX;
                    rank[rootX]++;
                }
            }
        }

        // Create a list to store all possible edges (either via modules or direct connections)
        List<int[]> allEdges = new ArrayList<>();

        // Add the module installation costs as edges connecting the device to a virtual node (0)
        for (int i = 1; i <= n; i++) {
            allEdges.add(new int[]{0, i, modules[i - 1]});
        }

        // Add the direct connections between devices
        for (int[] conn : connections) {
            allEdges.add(new int[]{conn[0], conn[1], conn[2]});
        }

        // Sort all edges by their cost in ascending order (Kruskal's algorithm step)
        allEdges.sort(Comparator.comparingInt(a -> a[2]));

        // Perform Kruskal's algorithm to find the MST
        int totalCost = 0;
        int edgesUsed = 0;

        for (int[] edge : allEdges) {
            int device1 = edge[0];
            int device2 = edge[1];
            int cost = edge[2];

            if (find(device1) != find(device2)) {
                union(device1, device2);
                totalCost += cost;
                edgesUsed++;
            }

            // If we have used n edges (to connect n devices), we can stop
            if (edgesUsed == n) {
                break;
            }
        }

        // If we used n edges, we successfully connected all devices
        return edgesUsed == n ? totalCost : -1;
    }

    public static void main(String[] args) {
        // Test with the sample input
        System.out.println(minNetworkCost(3, new int[]{1, 2, 2}, new int[][]{{1, 2, 1}, {2, 3, 1}}));  // Output should be 3
    }
}
