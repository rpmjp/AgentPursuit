package Pursuit;

import java.util.*;

class Agent2 extends Agent {
    private Random rand = new Random();
    private double[] beliefState = new double[41]; // Assuming nodes are numbered from 1 to 40
    private int stepsTaken = 0;
    private int successfulCaptures = 0;

    public Agent2(int startNode) {
        super(startNode);
        Arrays.fill(beliefState, 1.0 / 40); // Initially, the target is equally likely to be in any node
    }

    @Override
    public void move(Environment env, Target target) {
        // Increment steps taken
        stepsTaken++;

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

        // Select the next node based on the updated belief state
        List<Integer> neighbors = env.getNeighbors(currentNode);
        int nextNode = neighbors.get(0);
        double maxBelief = beliefState[nextNode];
        for (int neighbor : neighbors) {
            if (beliefState[neighbor] > maxBelief) {
                nextNode = neighbor;
                maxBelief = beliefState[neighbor];
            }
        }

        // Move to the next node
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

    public int getStepsTaken() {
        return stepsTaken;
    }

    public int getSuccessfulCaptures() {
        return successfulCaptures;
    }

    public int getCurrentNode() {
        return currentNode;
    }
}
