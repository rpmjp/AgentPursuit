package Pursuit;

import java.util.*;

class Agent2 extends Agent {
    private Random rand = new Random();
    private double[] beliefState = new double[41]; // Assuming nodes are numbered from 1 to 40
    private int stepsTaken = 0;
    private int successfulCaptures = 0;
    private int[] targetHistory = new int[5]; // Store the last 5 positions of the target

    public Agent2(int startNode) {
        super(startNode);
        Arrays.fill(beliefState, 1.0 / 40); // Initially, the target is equally likely to be in any node
        Arrays.fill(targetHistory, startNode); // Initially, the target's history is assumed to be the start node
    }

    @Override
    public void move(Environment env, Target target) {
        // Increment steps taken
        stepsTaken++;

        // Update the target's history
        System.arraycopy(targetHistory, 1, targetHistory, 0, 4);
        targetHistory[4] = target.getCurrentNode();

        // Predict the target's next position based on its history
        int predictedPosition = predictTargetPosition();

        // Update belief state based on the predicted position of the target
        for (int i = 1; i <= 40; i++) {
            double transitionProb = (i == predictedPosition) ? 0.8 : 0.2 / 39;
            beliefState[i] = transitionProb * beliefState[i];
        }

        // Normalize belief state
        double totalBelief = Arrays.stream(beliefState).sum();
        for (int i = 1; i <= 40; i++) {
            beliefState[i] /= totalBelief;
        }

        // Select the next node based on the updated belief state and the distance to the predicted position
        List<Integer> neighbors = env.getNeighbors(currentNode);
        int nextNode = neighbors.get(0);
        double maxScore = beliefState[nextNode] / (Math.abs(nextNode - predictedPosition) + 1);
        for (int neighbor : neighbors) {
            double score = beliefState[neighbor] / (Math.abs(neighbor - predictedPosition) + 1);
            if (score > maxScore) {
                nextNode = neighbor;
                maxScore = score;
            }
        }

        // Move to the next nodes
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

    private int predictTargetPosition() {
        // Predict the target's next position based on its history
        // In this simple example, we predict that the target will move in the same direction as its last move
        int lastMove = targetHistory[4] - targetHistory[3];
        int predictedPosition = targetHistory[4] + lastMove;
        if (predictedPosition < 1) {
            predictedPosition = 1;
        } else if (predictedPosition > 40) {
            predictedPosition = 40;
        }
        return predictedPosition;
    }
}
