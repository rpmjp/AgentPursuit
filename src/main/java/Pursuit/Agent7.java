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

    public Agent7(int startNode) {
        super(startNode);
        Arrays.fill(beliefState, 1.0 / 40); // Initially, the target is equally likely to be in any node
        lastKnownTargetPosition = startNode;
        initializeParticles();
        initializeTransitionMatrix();
    }

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

    @Override
    public boolean capture(Target target) {
        boolean captured = currentNode == target.getCurrentNode();
        if (captured) {
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

    @Override
    public Agent7 reset(int startNode) {
        return new Agent7(startNode);
    }

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

    private int predictNextPosition() {
        int sumPosition = 0;
        for (Particle particle : particles) {
            sumPosition += particle.getPosition();
        }
        return sumPosition / NUM_PARTICLES;
    }

    private double distanceToTarget(int node, int targetPosition) {
        return Math.abs(node - targetPosition);
    }

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

    private void initializeParticles() {
        for (int i = 0; i < NUM_PARTICLES; i++) {
            int position = rand.nextInt(40) + 1;
            particles.add(new Particle(position));
        }
    }

    private void updateParticles(int targetPosition) {
        for (Particle particle : particles) {
            particle.setPosition(targetPosition);
        }
    }

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