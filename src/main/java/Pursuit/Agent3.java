package Pursuit;

import java.util.Arrays;
import java.util.List;

class Agent3 extends Agent {
    private double[] beliefState = new double[41]; // Assuming nodes are numbered from 1 to 40
    private int examinedNode;
    private int stepsTaken = 0;
    private int successfulCaptures = 0;

    public Agent3(int startNode) {
        super(startNode);
        this.examinedNode = startNode; // Assuming the examined node is the start node
        Arrays.fill(beliefState, 1.0 / 40); // Initially, the target is equally likely to be in any node
    }

    @Override
    public void move(Environment env, Target target) {
        // Examine the same node every time
        boolean foundTarget = examineNode(target, examinedNode);

        // Update belief state based on examination result
        updateBeliefState(env, foundTarget);

        // Increment steps taken
        //stepsTaken++;
    }

    private boolean examineNode(Target target, int node) {
        // Check if the target is at the examined node
        return node == target.getCurrentNode();
    }

    private void updateBeliefState(Environment env, boolean foundTarget) {
        if (foundTarget) {
            // If the target is found, update the belief state to certainty
            Arrays.fill(beliefState, 0);
            beliefState[examinedNode] = 1.0;
            successfulCaptures++;
        } else {
            // If the target is not found, update the belief state based on how the target moves
            double[] newBeliefState = new double[41];
            for (int i = 1; i <= 40; i++) {
                List<Integer> neighbors = env.getNeighbors(i);
                for (int neighbor : neighbors) {
                    newBeliefState[neighbor] += beliefState[i] / neighbors.size();
                }
            }
            // Set the belief state for the examined node to 0 since the target was not found there
            newBeliefState[examinedNode] = 0;
            beliefState = newBeliefState;
        }
    }

    @Override
    public boolean capture(Target target) {
        boolean captured = currentNode == target.getCurrentNode();
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

    @Override
    public Agent3 reset(int startNode) {
        return new Agent3(startNode);
    }
}
