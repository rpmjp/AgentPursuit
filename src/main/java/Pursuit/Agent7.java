package Pursuit;

import java.util.*;

class Agent7 extends Agent {
    private double[] beliefState = new double[41]; // Assuming nodes are numbered from 1 to 40
    private Random rand = new Random();
    private int stepsTaken = 0;
    private int successfulCaptures = 0;
    private double[][] transitionMatrix = new double[41][41]; // Add transition matrix

    public Agent7(int startNode) {
        super(startNode);
        Arrays.fill(beliefState, 1.0 / 40); // Initially, the target is equally likely to be in any node
        initializeTransitionMatrix();
    }

    @Override
    public void move(Environment env, Target target) {
        // Increment steps taken


        updateBeliefStateAndBestMove(env, target);
    }

    public void updateBeliefStateAndBestMove(Environment env, Target target) {
        // Examine a random node with the highest belief
        int examinedNode = examineNode();

        // Update belief state based on the result of examining the node
        updateBeliefState(env, target, examinedNode);

        // Move to reduce the distance to the node with the highest belief
        bestMove(env);
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

    public void bestMove(Environment env) {
        // Find the node(s) with the highest belief
        List<Integer> bestNodes = new ArrayList<>();
        double maxBelief = 0;
        for (int neighbor : env.getNeighbors(currentNode)) {
            if (beliefState[neighbor] > maxBelief) {
                bestNodes.clear();
                bestNodes.add(neighbor);
                maxBelief = beliefState[neighbor];
            } else if (beliefState[neighbor] == maxBelief) {
                bestNodes.add(neighbor);
            }
        }

        // Move to a random node with the highest belief
        currentNode = bestNodes.get(rand.nextInt(bestNodes.size()));
        stepsTaken++;
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

    private void initializeTransitionMatrix() {
        // Initialize the transition matrix based on the graph
        for (int i = 1; i <= 40; i++) {
            for (int j = 1; j <= 40; j++) {
                if (i == j) {
                    transitionMatrix[i][j] = 0.025;
                } else if (Math.abs(i - j) == 1) {
                    transitionMatrix[i][j] = (1.0 - 0.025) / 3;
                } else {
                    transitionMatrix[i][j] = 0.0;
                }
            }
        }
    }

    @Override
    public Agent7 reset(int startNode) {
        return new Agent7(startNode);
    }
}
