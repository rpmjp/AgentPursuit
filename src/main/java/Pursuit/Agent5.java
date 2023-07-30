/**
 * Represents an agent that uses belief states and breadth-first search (BFS) to navigate
 * an environment and capture a target. The agent maintains a belief state for the target's
 * location and uses BFS to find the shortest path to the target.
 * <p>
 * The belief state is updated based on the known movement of the target, and the agent
 * moves along the shortest path to the target. The agent also keeps track of the number
 * of steps taken and successful captures.
 * </p>
 * @author [Robert Jean Pierre]
 */
package Pursuit;

import java.util.*;

class Agent5 extends Agent {
    private double[] beliefState = new double[41]; // Assuming nodes are numbered from 1 to 40.
    private Set<Integer> visitedNodes = new HashSet<>();
    private Random rand = new Random();
    private int stepsTaken = 0;
    private int successfulCaptures = 0;

    /**
     * Constructs an Agent5 with the given starting node.
     *
     * @param startNode the starting node of the agent
     */
    public Agent5(int startNode) {
        super(startNode);
        visitedNodes.add(startNode);
        Arrays.fill(beliefState, 1.0 / 40); // Initially, the target is equally likely to be in any node
    }

    /**
     * Moves the agent based on the belief state and finds the shortest path to the target using BFS.
     *
     * @param env    the environment
     * @param target the target
     */
    @Override
    public void move(Environment env, Target target) {
        // Update belief state based on the known movement of the target
        int targetNode = target.getCurrentNode();
        for (int i = 1; i <= 40; i++) {
            double transitionProb = (i == targetNode) ? 0.8 : 0.2 / 39;
            beliefState[i] = transitionProb * beliefState[i];
        }

        // Normalize belief state
        double totalBelief = Arrays.stream(beliefState).sum();
        for (int i = 1; i <= 40; i++) {
            beliefState[i] /= totalBelief;
        }

        // Find the shortest path to the target using BFS
        int shortestPathNode = bfsShortestPath(env, currentNode, targetNode);

        // Move to the next node in the shortest path
        currentNode = shortestPathNode;

        // Update belief state based on the result of examining the node
        beliefState[currentNode] = 0;
        // Update belief state based on the known movement of the target
        for (int neighbor : env.getNeighbors(currentNode)) {
            beliefState[neighbor] += 1.0 / env.getNeighbors(neighbor).size();
        }
        stepsTaken++;
    }

    /**
     * Helper method to find the shortest path from startNode to targetNode using BFS.
     *
     * @param env        the environment
     * @param startNode  the starting node
     * @param targetNode the target node
     * @return the next node in the shortest path
     */
    // Helper method to find the shortest path from startNode to targetNode using BFS
    private int bfsShortestPath(Environment env, int startNode, int targetNode) {
        Queue<Integer> queue = new LinkedList<>();
        queue.add(startNode);

        // Array to keep track of visited nodes during BFS
        boolean[] visited = new boolean[41];
        visited[startNode] = true;

        // Array to keep track of the parent nodes in the shortest path
        int[] parent = new int[41];
        Arrays.fill(parent, -1);

        while (!queue.isEmpty()) {
            int currentNode = queue.poll();

            // If the target node is found, reconstruct the shortest path and return the next node to move
            if (currentNode == targetNode) {
                int nextNode = targetNode;
                while (parent[nextNode] != startNode) {
                    nextNode = parent[nextNode];
                }
                return nextNode;
            }

            for (int neighbor : env.getNeighbors(currentNode)) {
                if (!visited[neighbor]) {
                    queue.add(neighbor);
                    visited[neighbor] = true;
                    parent[neighbor] = currentNode;
                }
            }
        }

        // If the target node is not reachable, return the current node
        return startNode;
    }

    /**
     * Captures the target if the agent's current node matches the target's current node.
     *
     * @param target the target
     * @return true if the target is captured, false otherwise
     */
    @Override
    public boolean capture(Target target) {
        // Check if the agent captures the target
        boolean captured = getCurrentNode() == target.getCurrentNode();
        if (captured) {
            // Increment successful captures
            successfulCaptures++;
            // Reset belief state to capture the target
            Arrays.fill(beliefState, 0);
            beliefState[target.getCurrentNode()] = 1;
        }
        return captured;
    }

    /**
     * Returns the number of steps taken by the agent.
     *
     * @return the number of steps taken
     */
    public int getStepsTaken() {
        return stepsTaken;
    }

    /**
     * Returns the number of successful captures by the agent.
     *
     * @return the number of successful captures
     */
    public int getSuccessfulCaptures() {
        return successfulCaptures;
    }

    /**
     * Resets the agent with the given starting node.
     *
     * @param startNode the starting node for the reset
     * @return a new Agent5 instance with the given starting node
     */
    @Override
    public Agent5 reset(int startNode) {
        return new Agent5(startNode);
    }

    /**
     * Returns the current node of the agent.
     *
     * @return the current node
     */
    public int getCurrentNode() {
        return currentNode;
    }
}
