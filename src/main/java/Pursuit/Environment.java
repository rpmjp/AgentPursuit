/**
 * Represents the environment in which agents and targets operate.
 * The environment is modeled as an undirected graph with nodes and edges.
 * <p>
 * The graph is initialized with a loop of 40 nodes, and ten additional random edges are added.
 * Each node can have at most three edges.
 * </p>
 * @author Robert Jean Pierre
 */
package Pursuit;

import java.util.*;

class Environment {
    // Graph represented as a map of nodes to their neighbors
    private Map<Integer, List<Integer>> graph = new HashMap<>();

    /**
     * Constructs an Environment with a loop of 40 nodes and ten additional random edges.
     * Ensures that each node has at most three edges.
     */
    public Environment() {
        // Create a loop of nodes
        for (int i = 1; i <= 40; i++) {
            addEdge(i, i % 40 + 1);
        }

        // Add ten additional edges
        Random rand = new Random();
        int addedEdges = 0;
        while (addedEdges < 10) {
            int node1 = rand.nextInt(40) + 1;
            int node2 = rand.nextInt(40) + 1;
            if (node1 != node2 && !graph.get(node1).contains(node2)
                    && graph.get(node1).size() < 3 && graph.get(node2).size() < 3) {
                addEdge(node1, node2);
                addedEdges++;
            }
        }
    }

    /**
     * Adds an edge between the given nodes in the graph.
     * If the nodes do not exist, they are added to the graph.
     *
     * @param node1 the first node
     * @param node2 the second node
     * @implNote This method is private and used internally to construct the graph.
     */
    private void addEdge(int node1, int node2) {
        graph.putIfAbsent(node1, new ArrayList<>());
        graph.putIfAbsent(node2, new ArrayList<>());
        graph.get(node1).add(node2);
        graph.get(node2).add(node1);
    }

    /**
     * Returns the neighbors of the given node in the graph.
     * If the node does not exist, returns an empty list.
     *
     * @param node the node whose neighbors are to be returned
     * @return a list of neighboring nodes
     */
    public List<Integer> getNeighbors(int node) {
        return graph.getOrDefault(node, new ArrayList<>());
    }

}
