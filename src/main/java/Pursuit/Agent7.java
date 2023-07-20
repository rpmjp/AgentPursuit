package Pursuit;

import java.util.*;

class Agent7 extends Agent {
    private double[] beliefState = new double[41]; // Assuming nodes are numbered from 1 to 40
    private Set<Integer> visitedNodes = new HashSet<>();
    private Random rand = new Random();

    public Agent7(int startNode) {
        super(startNode);
        visitedNodes.add(startNode);
        Arrays.fill(beliefState, 1.0 / 40); // Initially, the target is equally likely to be in any node
    }

    @Override
    public void move(Environment env, Target target) {
        // Find the node(s) with the highest belief and the most unvisited neighbors
        List<Integer> bestNodes = new ArrayList<>();
        double maxBelief = 0;
        int maxUnvisitedNeighbors = 0;
        for (int i = 1; i <= 40; i++) {
            int unvisitedNeighbors = (int) env.getNeighbors(i).stream().filter(n -> !visitedNodes.contains(n)).count();
            if (beliefState[i] > maxBelief || (beliefState[i] == maxBelief && unvisitedNeighbors > maxUnvisitedNeighbors)) {
                bestNodes.clear();
                bestNodes.add(i);
                maxBelief = beliefState[i];
                maxUnvisitedNeighbors = unvisitedNeighbors;
            } else if (beliefState[i] == maxBelief && unvisitedNeighbors == maxUnvisitedNeighbors) {
                bestNodes.add(i);
            }
        }

        // Examine a random node from the best nodes
        int examinedNode = bestNodes.get(rand.nextInt(bestNodes.size()));
        visitedNodes.add(examinedNode);

        // Update belief state based on the result of examining the node
        if (target.getCurrentNode() == examinedNode) {
            Arrays.fill(beliefState, 0);
            beliefState[examinedNode] = 1;
        } else {
            beliefState[examinedNode] = 0;
            // Update belief state based on the known movement of the target
            for (int neighbor : env.getNeighbors(examinedNode)) {
                beliefState[neighbor] += 1.0 / env.getNeighbors(neighbor).size();
            }
        }

        // Move to reduce the distance to the node with the highest belief
        List<Integer> neighbors = env.getNeighbors(currentNode);
        bestNodes.clear();
        maxBelief = 0;
        maxUnvisitedNeighbors = 0;
        for (int neighbor : neighbors) {
            int unvisitedNeighbors = (int) env.getNeighbors(neighbor).stream().filter(n -> !visitedNodes.contains(n)).count();
            if (beliefState[neighbor] > maxBelief || (beliefState[neighbor] == maxBelief && unvisitedNeighbors > maxUnvisitedNeighbors)) {
                bestNodes.clear();
                bestNodes.add(neighbor);
                maxBelief = beliefState[neighbor];
                maxUnvisitedNeighbors = unvisitedNeighbors;
            } else if (beliefState[neighbor] == maxBelief && unvisitedNeighbors == maxUnvisitedNeighbors) {
                bestNodes.add(neighbor);
            }
        }
        currentNode = bestNodes.get(rand.nextInt(bestNodes.size()));
    }

    @Override
    public boolean capture(Target target) {
        return currentNode == target.getCurrentNode();
    }
}