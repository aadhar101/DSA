import java.util.*;

public class PackageCollector {

    // Build the graph from roads
    private static List<List<Integer>> buildGraph(int n, int[][] roads) {
        List<List<Integer>> graph = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            graph.add(new ArrayList<>());
        }
        for (int[] road : roads) {
            graph.get(road[0]).add(road[1]);
            graph.get(road[1]).add(road[0]); // Undirected graph
        }
        return graph;
    }

    // Get all nodes reachable within 2 roads
    private static Set<Integer> getReachableInTwo(int start, List<List<Integer>> graph) {
        Set<Integer> reachable = new HashSet<>();
        Queue<Integer> queue = new LinkedList<>();
        int[] dist = new int[graph.size()];
        Arrays.fill(dist, -1);

        queue.offer(start);
        dist[start] = 0;
        reachable.add(start);

        while (!queue.isEmpty()) {
            int curr = queue.poll();

            for (int next : graph.get(curr)) {
                if (dist[next] == -1) { // Not visited
                    dist[next] = dist[curr] + 1;
                    if (dist[next] <= 2) {
                        reachable.add(next);
                        queue.offer(next);
                    }
                }
            }
        }
        return reachable;
    }

    public static int minRoadsToTraverse(int[] packages, int[][] roads) {
        int n = packages.length;
        List<List<Integer>> graph = buildGraph(n, roads);

        int totalPackages = 0;
        List<Integer> packageNodes = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            if (packages[i] == 1) {
                totalPackages++;
                packageNodes.add(i);
            }
        }

        if (totalPackages == 0) return 0;

        // Precompute reachable nodes within 2 roads for all nodes
        List<Set<Integer>> reachableFromEachNode = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            reachableFromEachNode.add(getReachableInTwo(i, graph));
        }

        // Try starting from each node, but we only care about the package nodes now
        int minRoads = Integer.MAX_VALUE;

        for (int start : packageNodes) {
            Queue<Integer> moves = new LinkedList<>();
            Set<Integer> visitedPackages = new HashSet<>();
            Set<Integer> visitedNodes = new HashSet<>();
            moves.offer(start);
            visitedNodes.add(start);
            int roadsUsed = 0;
            boolean allCollected = false;

            while (!moves.isEmpty() && !allCollected) {
                int size = moves.size();
                for (int i = 0; i < size; i++) {
                    int curr = moves.poll();

                    // Use precomputed reachable nodes
                    Set<Integer> reachable = reachableFromEachNode.get(curr);

                    for (int node : reachable) {
                        if (packages[node] == 1 && !visitedPackages.contains(node)) {
                            visitedPackages.add(node);
                        }
                    }

                    // Check if all packages are collected
                    if (visitedPackages.size() == totalPackages) {
                        // Stop once we have all packages
                        allCollected = true;
                    }

                    // Move to adjacent unvisited nodes
                    for (int next : graph.get(curr)) {
                        if (!visitedNodes.contains(next)) {
                            visitedNodes.add(next);
                            moves.offer(next);
                        }
                    }
                }
                roadsUsed++;
            }

            // Update minimum roads used if all packages were collected
            if (allCollected && visitedPackages.size() == totalPackages) {
                minRoads = Math.min(minRoads, roadsUsed);
            }
        }

        return minRoads == Integer.MAX_VALUE ? -1 : minRoads;
    }

    public static void main(String[] args) {
        int[] packages1 = {1, 0, 0, 0, 0, 1};
        int[][] roads1 = {{0, 1}, {1, 2}, {2, 3}, {3, 4}, {4, 5}};
        System.out.println("Test case 1: " + minRoadsToTraverse(packages1, roads1));  // Expected output: 2

        int[] packages2 = {0, 0, 0, 1, 1, 0, 0, 1};
        int[][] roads2 = {{0, 1}, {0, 2}, {1, 3}, {1, 4}, {2, 5}, {5, 6}, {5, 7}};
        System.out.println("Test case 2: " + minRoadsToTraverse(packages2, roads2));  // Expected output: 2
    }
}
