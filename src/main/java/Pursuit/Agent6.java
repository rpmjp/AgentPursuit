package Pursuit;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

class Agent6 extends Agent {
    private double[] beliefState = new double[41]; // Assuming nodes are numbered from 1 to 40
    private Random rand = new Random();
    private int stepsTaken = 0;
    private int successfulCaptures = 0;

    public Agent6(int startNode) {
        super(startNode);
        Arrays.fill(beliefState, 1.0 / 40); // Initially, the target is equally likely to be in any node
    }

    @Override
    public void move(Environment env, Target target) {
        updateBeliefStateAndBestMove(env, target);
    }

    public void updateBeliefStateAndBestMove(Environment env, Target target) {
        // Examine a random node with the highest belief
        int examinedNode = examineNode();

        // Update belief state based on the result of examining the node
        updateBeliefState(env, target, examinedNode);

        // Move to reduce the distance to the node with the highest belief
        int nextNode = bestMove(env, examinedNode);
        if (nextNode != currentNode) {
            stepsTaken++;
            currentNode = nextNode;
        }
    }

    public int examineNode() {
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

    public void updateBeliefState(Environment env, Target target, int examinedNode) {
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
    }

    public int bestMove(Environment env, int examinedNode) {
        // Find the adjacent node that reduces the distance to the examined node
        List<Integer> neighbors = env.getNeighbors(currentNode);
        int bestNode = currentNode; // Stay in the current node if no better option
        int currentDistance = Math.abs(currentNode - examinedNode);
        for (int neighbor : neighbors) {
            int newDistance = Math.abs(neighbor - examinedNode);
            if (newDistance < currentDistance) {
                bestNode = neighbor;
                currentDistance = newDistance;
            }
        }

        return bestNode;
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
    public Agent6 reset(int startNode) {
        return new Agent6(startNode);
    }
}
