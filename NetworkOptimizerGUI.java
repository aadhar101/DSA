import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

// Graph representation class
class Graph {
    private final int V;
    private final List<Edge> edges;

    public Graph(int V) {
        this.V = V;
        this.edges = new ArrayList<>();
    }

    public void addEdge(int src, int dest, int cost, int bandwidth) {
        edges.add(new Edge(src, dest, cost, bandwidth));
    }

    public List<Edge> getEdges() {
        return edges;
    }

    public int getV() {
        return V;
    }

    // Kruskal's Algorithm for Minimum Spanning Tree (MST)
    public List<Edge> kruskalMST() {
        List<Edge> result = new ArrayList<>();
        edges.sort(Comparator.comparingInt(e -> e.cost));
        UnionFind uf = new UnionFind(V);
        
        for (Edge edge : edges) {
            if (uf.find(edge.src) != uf.find(edge.dest)) {
                result.add(edge);
                uf.union(edge.src, edge.dest);
            }
        }
        return result;
    }
}

// Edge class to represent network connections
class Edge {
    int src, dest, cost, bandwidth;

    public Edge(int src, int dest, int cost, int bandwidth) {
        this.src = src;
        this.dest = dest;
        this.cost = cost;
        this.bandwidth = bandwidth;
    }
}

// Union-Find (Disjoint Set) for Kruskal's algorithm
class UnionFind {
    private int[] parent, rank;

    public UnionFind(int n) {
        parent = new int[n];
        rank = new int[n];
        for (int i = 0; i < n; i++) {
            parent[i] = i;
            rank[i] = 0;
        }
    }

    public int find(int x) {
        if (parent[x] != x) {
            parent[x] = find(parent[x]);
        }
        return parent[x];
    }

    public void union(int x, int y) {
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
}

// GUI for Network Optimization
public class NetworkOptimizerGUI {
    private JFrame frame;
    private JTextArea resultArea;
    private Graph graph;

    public NetworkOptimizerGUI() {
        frame = new JFrame("Network Optimizer");
        frame.setSize(600, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        JButton optimizeButton = new JButton("Optimize Network");
        resultArea = new JTextArea();
        resultArea.setEditable(false);

        optimizeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                List<Edge> mst = graph.kruskalMST();
                displayResult(mst);
            }
        });

        frame.add(optimizeButton, BorderLayout.NORTH);
        frame.add(new JScrollPane(resultArea), BorderLayout.CENTER);
        frame.setVisible(true);

        // Example graph initialization
        graph = new Graph(5);
        graph.addEdge(0, 1, 10, 100);
        graph.addEdge(0, 2, 15, 80);
        graph.addEdge(1, 3, 20, 90);
        graph.addEdge(2, 3, 30, 70);
        graph.addEdge(3, 4, 5, 60);
    }

    private void displayResult(List<Edge> mst) {
        StringBuilder sb = new StringBuilder("Optimized Network Topology:\n");
        for (Edge edge : mst) {
            sb.append("Node ").append(edge.src).append(" - Node ")
              .append(edge.dest).append(" | Cost: ")
              .append(edge.cost).append(" | Bandwidth: ")
              .append(edge.bandwidth).append("\n");
        }
        resultArea.setText(sb.toString());
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(NetworkOptimizerGUI::new);
    }
}
