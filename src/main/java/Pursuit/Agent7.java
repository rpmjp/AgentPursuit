package Pursuit;

import java.util.*;

class Agent7 extends Agent {
    private double[] beliefState = new double[41]; // Assuming nodes are numbered from 1 to 40
    private Set<Integer> visitedNodes = new HashSet<>();
    private Random rand = new Random();
    private int stepsTaken = 0;
    private int successfulCaptures = 0;
    private int lastKnownTargetPosition; // Last known position of the target

    // Define HMM transition probabilities
    private double[][] transitionMatrix = new double[41][41];

    public Agent7(int startNode) {
        super(startNode);
        visitedNodes.add(startNode);
        Arrays.fill(beliefState, 1.0 / 40); // Initially, the target is equally likely to be in any node
        lastKnownTargetPosition = startNode;
        initializeTransitionMatrix();
    }

    @Override
    public void move(Environment env, Target target) {
        // Increment steps taken
        stepsTaken++;

        // Predict the next position of the target using the HMM-based prediction
        int predictedPosition = predictNextPosition();

        // Find the node(s) with the highest belief and the most unvisited neighbors
        List<Integer> bestNodes = new ArrayList<>();
        double maxBelief = 0;
        int maxUnvisitedNeighbors = 0;
        for (int i = 1; i <= 40; i++) {
            int unvisitedNeighbors = (int) env.getNeighbors(i).stream().filter(n -> !visitedNodes.contains(n)).count();
            if (beliefState[i] > maxBelief || (beliefState[i] == maxBelief && unvisitedNeighbors > maxUnvisitedNeighbors)) {
                bestNodes.clear();
                bestNodes.add(i);
                maxBelief = beliefState[i];
                maxUnvisitedNeighbors = unvisitedNeighbors;
            } else if (beliefState[i] == maxBelief && unvisitedNeighbors == maxUnvisitedNeighbors) {
                bestNodes.add(i);
            }
        }

        // Examine a random node from the best nodes if available
        if (!bestNodes.isEmpty()) {
            int examinedNode = bestNodes.get(rand.nextInt(bestNodes.size()));
            visitedNodes.add(examinedNode);

            // Update belief state based on the result of examining the node
            if (target.getCurrentNode() == examinedNode) {
                Arrays.fill(beliefState, 0);
                beliefState[examinedNode] = 1;
            } else {
                updateBeliefState(env, examinedNode);
            }
        }

        // Implement Forward-Backward algorithm to update the belief state
        updateBeliefState(env, predictedPosition);

        // Move to reduce the distance to the predicted position
        currentNode = getClosestNeighbor(env, predictedPosition);
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

    public int getStepsTaken() {
        return stepsTaken;
    }

    public int getSuccessfulCaptures() {
        return successfulCaptures;
    }

    private void updateBeliefState(Environment env, int examinedNode) {
        // Calculate the likelihoods based on the HMM emission probabilities
        double[] likelihoods = new double[41];
        for (int i = 1; i <= 40; i++) {
            likelihoods[i] = emissionProbability(examinedNode, i);
        }

        // Calculate the forward messages
        double[] forwardMessages = new double[41];
        for (int i = 1; i <= 40; i++) {
            forwardMessages[i] = beliefState[i] * likelihoods[i];
        }

        // Calculate the backward messages (using the next time step's likelihoods)
        double[] backwardMessages = new double[41];
        for (int i = 1; i <= 40; i++) {
            double backwardMessage = 0;
            for (int neighbor : env.getNeighbors(i)) {
                backwardMessage += beliefState[neighbor] * transitionMatrix[i][neighbor];
            }
            backwardMessages[i] = backwardMessage;
        }

        // Calculate the smoothed estimates
        double[] smoothedEstimates = new double[41];
        double totalProbability = 0;
        for (int i = 1; i <= 40; i++) {
            smoothedEstimates[i] = forwardMessages[i] * backwardMessages[i];
            totalProbability += smoothedEstimates[i];
        }

        // Normalize the new belief state
        for (int i = 1; i <= 40; i++) {
            beliefState[i] = smoothedEstimates[i] / totalProbability;
        }
    }

    private int predictNextPosition() {
        // Use the HMM transition probabilities to predict the next position of the target
        // In this example, we'll use the last known position as the prediction
        return lastKnownTargetPosition;
    }

    private double emissionProbability(int examinedNode, int targetPosition) {
        // Implement a Gaussian emission probability function
        double mean = targetPosition;
        double variance = 4.0; // A tuning parameter for emission probability

        // Calculate the probability density function value of the Gaussian distribution
        double exponent = -Math.pow(examinedNode - mean, 2) / (2 * variance);
        double denominator = Math.sqrt(2 * Math.PI * variance);
        return Math.exp(exponent) / denominator;
    }

    private void initializeTransitionMatrix() {
        // Define a simple transition probability matrix based on the last known position
        double selfTransitionProb = 0.8;
        double neighborTransitionProb = (1.0 - selfTransitionProb) / 3;

        for (int i = 1; i <= 40; i++) {
            for (int j = 1; j <= 40; j++) {
                if (i == j) {
                    transitionMatrix[i][j] = selfTransitionProb;
                } else if (Math.abs(i - j) == 1) {
                    transitionMatrix[i][j] = neighborTransitionProb;
                } else {
                    transitionMatrix[i][j] = 0.0;
                }
            }
        }
    }

    private int getClosestNeighbor(Environment env, int targetPosition) {
        // Find the neighbor node closest to the target position
        List<Integer> neighbors = env.getNeighbors(currentNode);
        int closestNeighbor = -1;
        double minDistance = Double.MAX_VALUE;
        for (int neighbor : neighbors) {
            double distance = distanceToTarget(neighbor, targetPosition);
            if (distance < minDistance) {
                minDistance = distance;
                closestNeighbor = neighbor;
            }
        }
        return closestNeighbor;
    }

    private double distanceToTarget(int node, int targetPosition) {
        // Simple distance calculation between two nodes
        return Math.abs(node - targetPosition);
    }
    public int getCurrentNode() {
        return currentNode;
    }
}
