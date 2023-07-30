/**
 * Represents an agent that uses breadth-first search (BFS) to navigate an environment and capture a target.
 * The agent maintains a count of the number of steps taken and successful captures.
 * <p>
 * The agent finds the shortest path to the target using BFS and moves along that path.
 * </p>
 * @author [Robert Jean Pierre]
 */
package Pursuit;

import java.util.*;

class Agent2 extends Agent {
    private Random rand = new Random();
    private int stepsTaken = 0;
    private int successfulCaptures = 0;

    /**
     * Constructs an Agent2 with the given starting node.
     *
     * @param startNode the starting node of the agent
     */
    public Agent2(int startNode) {
        super(startNode);
    }

    /**
     * Moves the agent to the next node in the shortest path to the target using BFS.
     *
     * @param env    the environment
     * @param target the target
     */
    @Override
    public void move(Environment env, Target target) {
        // Increment steps taken
        stepsTaken++;

        // Find the shortest path using BFS from the current node to the target node
        List<Integer> shortestPath = findShortestPath(env, target.getCurrentNode());

        // Move to the next node in the shortest path (if available)
        if (shortestPath.size() > 1) {
            int nextNode = shortestPath.get(1);
            currentNode = nextNode;
        }
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
        }
        return captured;
    }

    /**
     * Returns the number of steps taken by the agent.
     *
     * @return the number of steps taken
     */
    @Override
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
     * Returns the current node of the agent.
     *
     * @return the current node
     */
    public int getCurrentNode() {
        return currentNode;
    }

    /**
     * Finds the shortest path from the current node to the target node using BFS.
     *
     * @param env        the environment
     * @param targetNode the target node
     * @return a list representing the shortest path to the target node
     */
    private List<Integer> findShortestPath(Environment env, int targetNode) {
        Queue<List<Integer>> queue = new LinkedList<>();
        Set<Integer> visited = new HashSet<>();
        queue.offer(new ArrayList<>(Collections.singletonList(currentNode)));
        visited.add(currentNode);

        while (!queue.isEmpty()) {
            List<Integer> path = queue.poll();
            int currentNode = path.get(path.size() - 1);

            if (currentNode == targetNode) {
                return path;
            }

            for (int neighbor : env.getNeighbors(currentNode)) {
                if (!visited.contains(neighbor)) {
                    List<Integer> newPath = new ArrayList<>(path);
                    newPath.add(neighbor);
                    queue.offer(newPath);
                    visited.add(neighbor);
                }
            }
        }

        // Return an empty list if there is no path to the target
        return new ArrayList<>();
    }

    /**
     * Resets the agent with the given starting node.
     *
     * @param startNode the starting node for the reset
     * @return a new Agent2 instance with the given starting node
     */
    @Override
    public Agent2 reset(int startNode) {
        return new Agent2(startNode);
    }
}
