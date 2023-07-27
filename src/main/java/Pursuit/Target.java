package Pursuit;

import java.util.*;

class Target {
    private int currentNode;
    private Random rand = new Random();
    private int stepsTaken = 0; // Add step counter

    private Environment environment; // The environment in which the target is moving

    public Target(Environment environment, int startNode) {
        this.currentNode = startNode;
        this.environment = environment;

    }

    public void move(Environment env) {
        List<Integer> neighbors = env.getNeighbors(currentNode);
        currentNode = neighbors.get(rand.nextInt(neighbors.size()));
        stepsTaken++; // Increment the step counter every time the target moves
    }

    public int getCurrentNode() {
        return currentNode;
    }

    public int getStepsTaken() { // Add a getter for the step counter
        return stepsTaken;
    }

    public Environment getEnvironment() {
        return environment;
    }
}
