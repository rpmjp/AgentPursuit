/**
 * Abstract class representing a generic agent in the Pursuit simulation.
 * <p>
 * This class provides the basic structure and common functionality for different types of agents.
 * Specific agent behaviors, such as movement and capture strategies, are defined in subclasses.
 * </p>
 * <p>
 * The agent maintains a current node, a count of steps taken, and a count of successful captures.
 * </p>
 *
 * @author Robert Jean Pierre
 */
package Pursuit;

abstract class Agent {
    protected int currentNode;
    protected int stepsTaken = 0;
    protected int successfulCaptures = 0;

    /**
     * Constructs a new Agent with the given starting node.
     *
     * @param startNode The starting node for the agent.
     */
    public Agent(int startNode) {
        this.currentNode = startNode;
    }

    /**
     * Moves the agent within the environment, pursuing the target.
     * The specific movement strategy is defined in subclasses.
     *
     * @param env    The environment in which the agent is moving.
     * @param target The target that the agent is pursuing.
     */
    public abstract void move(Environment env, Target target);

    /**
     * Attempts to capture the target.
     * The specific capture strategy is defined in subclasses.
     *
     * @param target The target that the agent is attempting to capture.
     * @return True if the target is captured, false otherwise.
     */
    public abstract boolean capture(Target target);

    /**
     * Increments the count of steps taken by the agent.
     */
    public void incrementStepsTaken() {
        this.stepsTaken++;
    }

    /**
     * Increments the count of successful captures by the agent.
     */
    public void incrementSuccessfulCaptures() {
        this.successfulCaptures++;
    }

    /**
     * Gets the number of steps taken by the agent.
     *
     * @return The number of steps taken.
     */
    public abstract int getStepsTaken(); // Changed return type to int

    /**
     * Gets the number of successful captures by the agent.
     *
     * @return The number of successful captures.
     */
    public int getSuccessfulCaptures() {
        return this.successfulCaptures;
    }

    /**
     * Resets the agent to a new starting node.
     * The specific reset behavior is defined in subclasses.
     *
     * @param startNode The new starting node for the agent.
     * @return A new Agent instance with the specified starting node.
     */
    public abstract Agent reset(int startNode);
}
