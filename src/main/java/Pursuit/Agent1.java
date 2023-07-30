/**
 * Agent1 is a specific implementation of the abstract Agent class in the Pursuit simulation.
 * <p>
 * This agent uses a simple strategy to move towards the target by calculating the best neighboring
 * node closest to the target's current position. The distance to the target is calculated using
 * the absolute difference between the node numbers.
 * </p>
 */
package Pursuit;

import java.util.*;

class Agent1 extends Agent {
    private Environment environment; // Add a class member to store the Environment instance

    private int stepsTaken = 0;
    private int successfulCaptures = 0;

    public Agent1(int startNode) {
        super(startNode);
    }

    /**
     * Moves the agent within the environment, pursuing the target.
     * The agent calculates the best neighboring node closest to the target and moves to it.
     *
     * @param env    The environment in which the agent is moving.
     * @param target The target that the agent is pursuing.
     */
    @Override
    public void move(Environment env, Target target) {
        // Increment steps taken
        stepsTaken++;

        // Calculate the best neighboring node closest to the target
        int nextNode = calculateBestNextNode(env, target.getCurrentNode());

        // Move to the calculated neighboring node
        currentNode = nextNode;
    }

    /**
     * Attempts to capture the target.
     *
     * @param target The target that the agent is attempting to capture.
     * @return True if the target is captured, false otherwise.
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

    @Override
    public int getStepsTaken() {
        return stepsTaken;
    }

    public int getSuccessfulCaptures() {
        return successfulCaptures;
    }

    public int getCurrentNode() {
        return currentNode;
    }

    /**
     * Calculates the best neighboring node closest to the target position.
     *
     * @param env            The environment in which the agent is moving.
     * @param targetPosition The current position of the target.
     * @return The best neighboring node to move to.
     */
    private int calculateBestNextNode(Environment env, int targetPosition) {
        // Find the best neighboring node closest to the target position
        List<Integer> neighbors = env.getNeighbors(currentNode);
        int bestNextNode = -1;
        double minDistance = Double.MAX_VALUE;

        for (int neighbor : neighbors) {
            double distance = distanceToTarget(neighbor, targetPosition);
            if (distance < minDistance) {
                minDistance = distance;
                bestNextNode = neighbor;
            }
        }
        return bestNextNode;
    }

    /**
     * Calculates the distance to the target from a given node.
     *
     * @param node           The node from which to calculate the distance.
     * @param targetPosition The current position of the target.
     * @return The distance to the target.
     */
    private double distanceToTarget(int node, int targetPosition) {
        // Simple distance calculation between two nodes
        return Math.abs(node - targetPosition);
    }

    /**
     * Resets the agent to a new starting node.
     *
     * @param startNode The new starting node for the agent.
     * @return A new Agent1 instance with the specified starting node.
     */
    @Override
    public Agent1 reset(int startNode) {
        return new Agent1(startNode);
    }
}
