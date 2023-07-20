package Pursuit;

import java.util.*;

class Target {
    private int currentNode;
    private Random rand = new Random();
    private int stepsTaken = 0; // Add a step counter

    public Target(Environment env, int startNode) {
        this.currentNode = startNode;
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
}
