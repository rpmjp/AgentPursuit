package Pursuit;

import java.util.*;

class Environment {
    private Map<Integer, List<Integer>> graph = new HashMap<>();

    public Environment() {
        // Create a loop of nodes
        for (int i = 1; i <= 40; i++) {
            addEdge(i, i % 40 + 1);
        }

        // Add 10 additional edges
        Random rand = new Random();
        int addedEdges = 0;
        while (addedEdges < 10) {
            int node1 = rand.nextInt(40) + 1;
            int node2 = rand.nextInt(40) + 1;
            if (node1 != node2 && !graph.get(node1).contains(node2) && graph.get(node1).size() < 3 && graph.get(node2).size() < 3) {
                addEdge(node1, node2);
                addedEdges++;
            }
        }
    }

    private void addEdge(int node1, int node2) {
        graph.putIfAbsent(node1, new ArrayList<>());
        graph.putIfAbsent(node2, new ArrayList<>());
        graph.get(node1).add(node2);
        graph.get(node2).add(node1);
    }

    public List<Integer> getNeighbors(int node) {
        return graph.get(node);
    }
}
