/**
 * Represents a target within a pursuit environment.
 * <p>
 * The target moves randomly within the environment, selecting one of its neighboring nodes to move to at each step.
 * The class also keeps track of the number of steps taken by the target.
 * </p>
 * @author Robert Jean Pierre
 */
package Pursuit;

import java.util.*;

class Target {
    private int currentNode;// The current node where the target is located
    private Random rand = new Random(); // Random object used to select the next move
    private int stepsTaken = 0; // Add step counter

    private Environment environment; // The environment in which the target is moving

    /**
     * Constructs a Target with the given environment and starting node.
     *
     * @param environment the environment in which the target will move
     * @param startNode   the starting node for the target
     */
    public Target(Environment environment, int startNode) {
        this.currentNode = startNode;
        this.environment = environment;


    }

    /**
     * Moves the target to a randomly selected neighboring node.
     * Increments the step counter every time the target moves.
     *
     * @param env the environment in which the target is moving
     */
    public void move(Environment env) {
        List<Integer> neighbors = (List<Integer>) env.getNeighbors(currentNode);
        currentNode = neighbors.get(rand.nextInt(neighbors.size()));
        stepsTaken++; // Increment the step counter every time the target moves
    }

    /**
     * Returns the current node where the target is located.
     *
     * @return the current node
     */
    public int getCurrentNode() {
        return currentNode;
    }

    /**
     * Returns the number of steps taken by the target.
     *
     * @return the step counter
     */
    public int getStepsTaken() { // Add a getter for the step counter
        return stepsTaken;
    }

    /**
     * Returns the environment in which the target is moving.
     *
     * @return the environment
     */
    public Environment getEnvironment() {
        return environment;
    }
}
