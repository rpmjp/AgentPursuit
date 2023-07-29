package Pursuit;

import java.util.*;

class Agent1 extends Agent {
    private Environment environment; // Add a class member to store the Environment instance

    private int stepsTaken = 0;
    private int successfulCaptures = 0;

    public Agent1(int startNode) {
        super(startNode);
    }

    @Override
    public void move(Environment env, Target target) {
        // Increment steps taken
        stepsTaken++;

        // Calculate the best neighboring node closest to the target
        int nextNode = calculateBestNextNode(env, target.getCurrentNode());

        // Move to the calculated neighboring node
        currentNode = nextNode;
    }

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

    private double distanceToTarget(int node, int targetPosition) {
        // Simple distance calculation between two nodes
        return Math.abs(node - targetPosition);
    }
    @Override
    public Agent1 reset(int startNode) {
        return new Agent1(startNode);
    }
}
