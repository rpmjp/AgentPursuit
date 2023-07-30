/**
 * <h1>Agent7</h1>
 * The Agent7 class extends the {@link Agent} class and represents an intelligent agent
 * that uses Particle Filtering and Hidden Markov Models (HMM) to capture a target in an environment.
 * <p>
 * This agent maintains a belief state for the target's location and uses a combination of
 * probabilistic reasoning and search algorithms to make decisions.
 * </p>
 * <h2>Key Features:</h2>
 * <ul>
 *   <li>Particle Filtering for state estimation.</li>
 *   <li>Hidden Markov Models for transition probabilities.</li>
 *   <li>Epsilon-greedy policy for action selection.</li>
 * </ul>
 *
 * @author [Robert Jean Pierre]
 * @see Environment
 * @see Target
 * @see Particle
 */
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
    private double[] beliefState = new double[41]; // Belief state for the target's location

    /**
     * Constructs a new Agent7 with the given starting node.
     * Initializes belief state, particles, and transition matrix.
     *
     * @param startNode The starting node for the agent.
     */
    public Agent7(int startNode) {
        super(startNode);
        Arrays.fill(beliefState, 1.0 / 40); // Initially, the target is equally likely to be in any node
        lastKnownTargetPosition = startNode;
        initializeParticles();
        initializeTransitionMatrix();
    }

    /**
     * Moves the agent in the environment towards the target.
     * Uses Particle Filtering and HMM to update the belief state and make decisions.
     *
     * @param env    The environment in which the agent operates.
     * @param target The target that the agent is trying to capture.
     */
    @Override
    public void move(Environment env, Target target) {
        stepsTaken++;
        int predictedPosition = predictNextPosition();
        int examinedNode = getHighestProbabilityNode();
        if (target.getCurrentNode() == examinedNode) {
            updateParticles(target.getCurrentNode());
        } else {
            updateBeliefState(env, examinedNode);
        }

        Map<Integer, Integer> prev = new HashMap<>();
        Map<Integer, Double> dist = new HashMap<>();
        for (int i = 1; i <= 40; i++) {
            dist.put(i, Double.MAX_VALUE);
        }
        dist.put(currentNode, 0.0);

        PriorityQueue<Integer> queue = new PriorityQueue<>(Comparator.comparingDouble(dist::get));
        queue.add(currentNode);

        while (!queue.isEmpty()) {
            int node = queue.poll();
            for (int neighbor : env.getNeighbors(node)) {
                double newDist = dist.get(node) + distanceToTarget(node, neighbor);
                if (newDist < dist.get(neighbor)) {
                    dist.put(neighbor, newDist);
                    prev.put(neighbor, node);
                    queue.remove(neighbor);
                    queue.add(neighbor);
                }
            }
        }

        currentNode = getClosestNeighbor(env, getHighestProbabilityNode());
    }

    /**
     * Attempts to capture the target at the current node.
     *
     * @param target The target to capture.
     * @return true if the target is captured, false otherwise.
     */
    @Override
    public boolean capture(Target target) {
        boolean captured = currentNode == target.getCurrentNode();
        if (captured) {
            successfulCaptures++;
        }
        return captured;
    }

    /**
     * Returns the total number of steps taken by the agent.
     *
     * @return The number of steps taken.
     */
    public int getStepsTaken() {
        return stepsTaken;
    }

    public int getSuccessfulCaptures() {
        return successfulCaptures;
    }

    @Override
    public Agent7 reset(int startNode) {
        return new Agent7(startNode);
    }

    /**
     * Updates the belief state based on the examined node and the environment.
     * Utilizes distance calculations and observation counts to update the transition matrix.
     *
     * @param env           The environment in which the agent operates.
     * @param examinedNode  The node that was examined by the agent.
     */
    private void updateBeliefState(Environment env, int examinedNode) {
        double[] likelihoods = new double[41];
        for (int i = 1; i <= 40; i++) {
            double distance = distanceToTarget(examinedNode, i);
            likelihoods[i] = 1.0 / (distance + 1);
        }

        observationCounts[lastKnownTargetPosition][examinedNode]++;

        for (int i = 1; i <= 40; i++) {
            int totalCount = observationCounts[lastKnownTargetPosition][i];
            if (totalCount > 0) {
                transitionMatrix[lastKnownTargetPosition][i] = (double) observationCounts[lastKnownTargetPosition][i] / totalCount;
            }
        }

        updateBeliefStateWithHMM(examinedNode);
        lastKnownTargetPosition = examinedNode;
    }

    /**
     * Updates the belief state using the Hidden Markov Model (HMM).
     * Applies transition probabilities to calculate the new belief state.
     *
     * @param examinedNode  The node that was examined by the agent.
     */
    private void updateBeliefStateWithHMM(int examinedNode) {
        double[] newBeliefState = new double[41];
        for (int i = 1; i <= 40; i++) {
            double prob = 0.0;
            for (int j = 1; j <= 40; j++) {
                prob += beliefState[j] * transitionMatrix[j][i];
            }
            newBeliefState[i] = prob;
        }
        beliefState = newBeliefState;

        double totalBelief = Arrays.stream(beliefState).sum();
        for (int i = 1; i <= 40; i++) {
            beliefState[i] /= totalBelief;
        }
    }

    /**
     * Predicts the next position of the target based on the particles.
     *
     * @return The predicted position of the target.
     */
    private int predictNextPosition() {
        int sumPosition = 0;
        for (Particle particle : particles) {
            sumPosition += particle.getPosition();
        }
        return sumPosition / NUM_PARTICLES;
    }

    /**
     * Calculates the distance to the target from a given node.
     *
     * @param node            The node from which the distance is calculated.
     * @param targetPosition  The position of the target.
     * @return The distance to the target.
     */
    private double distanceToTarget(int node, int targetPosition) {
        return Math.abs(node - targetPosition);
    }

    /**
     * Initializes the transition matrix for the Hidden Markov Model (HMM).
     * Sets initial probabilities based on the environment's topology.
     */
    private void initializeTransitionMatrix() {
        double initialProb = 0.025;
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

    /**
     * Initializes the particles for the Particle Filter.
     * Creates particles with random positions.
     */
    private void initializeParticles() {
        for (int i = 0; i < NUM_PARTICLES; i++) {
            int position = rand.nextInt(40) + 1;
            particles.add(new Particle(position));
        }
    }

    /**
     * Updates the positions of the particles based on the target's position.
     *
     * @param targetPosition  The current position of the target.
     */
    private void updateParticles(int targetPosition) {
        for (Particle particle : particles) {
            particle.setPosition(targetPosition);
        }
    }

    /**
     * Finds the closest neighbor to the target position from the current node.
     *
     * @param env             The environment in which the agent operates.
     * @param targetPosition  The position of the target.
     * @return The closest neighbor node to the target position.
     */
    private int getClosestNeighbor(Environment env, int targetPosition) {
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

    /**
     * Finds the node with the highest probability in the belief state.
     *
     * @return The node with the highest probability.
     */
    private int getHighestProbabilityNode() {
        int highestProbNode = -1;
        double highestProb = Double.MIN_VALUE;
        for (int i = 1; i <= 40; i++) {
            if (beliefState[i] > highestProb) {
                highestProb = beliefState[i];
                highestProbNode = i;
            }
        }
        return highestProbNode;
    }
}