package Pursuit;

import java.util.*;

class Agent7 extends Agent {
    private final int NUM_PARTICLES = 1000; // Number of particles for the Particle Filter
    private List<Particle> particles = new ArrayList<>();
    private Random rand = new Random();
    private int stepsTaken = 0;
    private int successfulCaptures = 0;
    private int lastKnownTargetPosition; // Last known position of the target

    // Define HMM transition probabilities
    private double[][] transitionMatrix = new double[41][41];
    private int[][] observationCounts = new int[41][41];

    public Agent7(int startNode) {
        super(startNode);
        initializeParticles();
        lastKnownTargetPosition = startNode;
        initializeTransitionMatrix();
    }

    @Override
    public void move(Environment env, Target target) {
        // Increment steps taken
        stepsTaken++;

        // Predict the next position of the target using the Particle Filter-based prediction
        int predictedPosition = predictNextPosition();

        // Examine a random node
        int examinedNode = rand.nextInt(40) + 1;

        // Update belief state based on the result of examining the node
        if (target.getCurrentNode() == examinedNode) {
            updateParticles(target.getCurrentNode());
        } else {
            updateBeliefState(env, examinedNode);
        }

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
        // Calculate the likelihoods based on the distances between the examined node and the target's current position
        double[] likelihoods = new double[41];
        for (int i = 1; i <= 40; i++) {
            double distance = distanceToTarget(examinedNode, i);
            likelihoods[i] = 1.0 / (distance + 1); // Add 1 to avoid division by zero and to make likelihoods smaller for farther nodes
        }

        // Update observation counts
        observationCounts[lastKnownTargetPosition][examinedNode]++;

        // Update transition probabilities based on recent observations
        for (int i = 1; i <= 40; i++) {
            int totalCount = observationCounts[lastKnownTargetPosition][i];
            if (totalCount > 0) {
                transitionMatrix[lastKnownTargetPosition][i] = (double) observationCounts[lastKnownTargetPosition][i] / totalCount;
            }
        }

        // Update particles using the Particle Filter
        updateParticlesUsingParticleFilter(likelihoods);
    }

    private int predictNextPosition() {
        // Use the Particle Filter to predict the next position of the target
        // In this example, we'll use the last known position as the prediction
        int sumPosition = 0;
        for (Particle particle : particles) {
            sumPosition += particle.getPosition();
        }
        return sumPosition / NUM_PARTICLES;
    }

    private double distanceToTarget(int node, int targetPosition) {
        // Simple distance calculation between two nodes
        return Math.abs(node - targetPosition);
    }

    private void initializeTransitionMatrix() {
        // Define initial transition probability matrix
        double initialProb = 0.025; // A small initial probability to encourage exploration

        for (int i = 1; i <= 40; i++) {
            for (int j = 1; j <= 40; j++) {
                if (i == j) {
                    transitionMatrix[i][j] = initialProb;
                } else if (Math.abs(i - j) == 1) {
                    transitionMatrix[i][j] = (1.0 - initialProb) / 3;
                } else {
                    transitionMatrix[i][j] = 0.0;
                }
            }
        }
    }

    private void initializeParticles() {
        // Initialize particles randomly across the nodes
        for (int i = 0; i < NUM_PARTICLES; i++) {
            int position = rand.nextInt(40) + 1;
            particles.add(new Particle(position));
        }
    }

    private void updateParticles(int targetPosition) {
        // Update particles to match the target position
        for (Particle particle : particles) {
            particle.setPosition(targetPosition);
        }
    }

    private void updateParticlesUsingParticleFilter(double[] likelihoods) {
        // Update particles using the Particle Filter
        for (Particle particle : particles) {
            particle.update(likelihoods, transitionMatrix, rand, particles, NUM_PARTICLES);
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
}
