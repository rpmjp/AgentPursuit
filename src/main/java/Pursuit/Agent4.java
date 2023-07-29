package Pursuit;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

class Agent4 extends Agent {
    private double[] beliefState = new double[41]; // Assuming nodes are numbered from 1 to 40
    private Random rand = new Random();
    private int stepsTaken = 0;
    private int successfulCaptures = 0;
    private Environment env; // Store the Environment object here

    public Agent4(int startNode) {
        super(startNode);
        Arrays.fill(beliefState, 1.0 / 40); // Initially, the target is equally likely to be in any node
    }

    @Override
    public void move(Environment env, Target target) {
        // Store the Environment object
        this.env = env;

        // Increment steps taken
        stepsTaken++;

        // Move to a random node with the highest belief
        currentNode = bestMove();
    }

    public int bestMove() {
        // Find the node(s) with the highest belief
        List<Integer> bestNodes = new ArrayList<>();
        double maxBelief = 0;
        for (int i = 1; i <= 40; i++) {
            if (beliefState[i] > maxBelief) {
                bestNodes.clear();
                bestNodes.add(i);
                maxBelief = beliefState[i];
            } else if (beliefState[i] == maxBelief) {
                bestNodes.add(i);
            }
        }

        // Return a random node with the highest belief
        return bestNodes.get(rand.nextInt(bestNodes.size()));

    }

    @Override
    public boolean capture(Target target) {
        // Store the Environment object
        this.env = target.getEnvironment();

        // Increment steps taken
        //stepsTaken++;

        // Update belief state based on the result of examining the node
        boolean captured = target.getCurrentNode() == currentNode;
        if (captured) {
            Arrays.fill(beliefState, 0);
            beliefState[currentNode] = 1;
            // Increment successful captures
            successfulCaptures++;
            return true;
        } else {
            beliefState[currentNode] = 0;
            // Update belief state based on the known movement of the target
            for (int neighbor : env.getNeighbors(currentNode)) {
                beliefState[neighbor] += 1.0 / env.getNeighbors(neighbor).size();
            }
            return false;
        }
    }


    @Override
    public int getStepsTaken() {
        return stepsTaken;
    }

    public int getSuccessfulCaptures() {
        return successfulCaptures;
    }
    @Override
    public Agent4 reset(int startNode) {
        return new Agent4(startNode);
    }
}
