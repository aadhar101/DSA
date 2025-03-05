import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class NetworkOptimizerJavaFX extends Application {
    
    private static final int NODE_RADIUS = 20; // radius of nodes (servers/clients)
    private List<Node> nodes = new ArrayList<>();
    private List<Edge> edges = new ArrayList<>();
    
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        // Set up the main layout
        BorderPane root = new BorderPane();
        Canvas canvas = new Canvas(600, 600);  // Canvas for drawing the network graph
        GraphicsContext gc = canvas.getGraphicsContext2D();
        
        // Add nodes and edges (For demonstration purposes)
        initializeGraph();
        
        // Draw the initial network graph
        drawGraph(gc);

        // Add a button to optimize the network
        Button optimizeButton = new Button("Optimize Network");
        optimizeButton.setOnAction(e -> {
            // Apply network optimization algorithms (e.g., Kruskal's or Prim's)
            List<Edge> mst = kruskalMST();
            drawGraph(gc, mst);
        });

        // Set up the layout with the canvas and optimize button
        StackPane topPanel = new StackPane();
        topPanel.getChildren().add(optimizeButton);
        
        root.setTop(topPanel);
        root.setCenter(canvas);
        
        Scene scene = new Scene(root, 600, 600);
        primaryStage.setTitle("Network Topology Optimizer");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // Initialize some sample nodes and edges for demonstration
    private void initializeGraph() {
        Random rand = new Random();
        nodes.add(new Node(100, 100, "Server 1"));
        nodes.add(new Node(300, 100, "Server 2"));
        nodes.add(new Node(500, 100, "Client 1"));
        nodes.add(new Node(100, 300, "Client 2"));
        nodes.add(new Node(300, 300, "Server 3"));
        
        edges.add(new Edge(0, 1, 10, 100));
        edges.add(new Edge(1, 2, 15, 50));
        edges.add(new Edge(0, 3, 20, 30));
        edges.add(new Edge(2, 4, 25, 80));
        edges.add(new Edge(3, 4, 30, 60));
    }
    
    // Draw the network graph on the canvas
    private void drawGraph(GraphicsContext gc) {
        drawGraph(gc, edges);
    }
    
    // Draw the network graph with specific edges
    private void drawGraph(GraphicsContext gc, List<Edge> edgesToDraw) {
        gc.clearRect(0, 0, 600, 600); // Clear the canvas
        // Draw edges
        for (Edge edge : edgesToDraw) {
            Node srcNode = nodes.get(edge.src);
            Node destNode = nodes.get(edge.dest);
            gc.strokeLine(srcNode.x, srcNode.y, destNode.x, destNode.y);
            
            // Draw cost and bandwidth labels on edges
            gc.fillText("Cost: " + edge.cost + " Bandwidth: " + edge.bandwidth, 
                    (srcNode.x + destNode.x) / 2, (srcNode.y + destNode.y) / 2);
        }
        
        // Draw nodes (servers and clients)
        for (Node node : nodes) {
            gc.fillOval(node.x - NODE_RADIUS, node.y - NODE_RADIUS, 2 * NODE_RADIUS, 2 * NODE_RADIUS);
            gc.fillText(node.name, node.x + 10, node.y + 10);
        }
    }
    
    // Kruskal's algorithm for Minimum Spanning Tree (MST)
    private List<Edge> kruskalMST() {
        List<Edge> mst = new ArrayList<>();
        edges.sort((e1, e2) -> Integer.compare(e1.cost, e2.cost));
        UnionFind uf = new UnionFind(nodes.size());

        for (Edge edge : edges) {
            if (uf.find(edge.src) != uf.find(edge.dest)) {
                mst.add(edge);
                uf.union(edge.src, edge.dest);
            }
        }

        return mst;
    }

    // Node class to represent servers/clients
    class Node {
        int x, y;
        String name;

        Node(int x, int y, String name) {
            this.x = x;
            this.y = y;
            this.name = name;
        }
    }

    // Edge class to represent network connections
    class Edge {
        int src, dest, cost, bandwidth;

        Edge(int src, int dest, int cost, int bandwidth) {
            this.src = src;
            this.dest = dest;
            this.cost = cost;
            this.bandwidth = bandwidth;
        }
    }

    // Union-Find (Disjoint Set) for Kruskal's algorithm
    class UnionFind {
        private int[] parent, rank;

        UnionFind(int n) {
            parent = new int[n];
            rank = new int[n];
            for (int i = 0; i < n; i++) {
                parent[i] = i;
                rank[i] = 0;
            }
        }

        int find(int x) {
            if (parent[x] != x) {
                parent[x] = find(parent[x]);
            }
            return parent[x];
        }

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
    }
}
