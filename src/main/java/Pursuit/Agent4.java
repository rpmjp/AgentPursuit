/**
 * Agent4 class representing an agent in the Pursuit simulation.
 * <p>
 * This agent maintains a belief state, representing the probability distribution over the possible locations of the target.
 * The agent moves to a random node with the highest belief and updates its belief state based on the observed environment.
 * </p>
 * <p>
 * The belief state is initialized with equal probability for all nodes, and it is updated based on the result of examining
 * the current node and the known movement of the target.
 * </p>
 *
 * @author Robert Jean Pierre
 */
package Pursuit;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

class Agent4 extends Agent {
    private double[] beliefState = new double[41]; // Assuming nodes are numbered from 1 to 40
    private Random rand = new Random();
    private int stepsTaken = 0;
    private int successfulCaptures = 0;
    private Environment env; // Store the Environment object here

    /**
     * Constructs a new Agent4 with the given starting node.
     *
     * @param startNode The starting node for the agent.
     */
    public Agent4(int startNode) {
        super(startNode);
        Arrays.fill(beliefState, 1.0 / 40); // Initially, the target is equally likely to be in any node
    }

    /**
     * Moves the agent to a random node with the highest belief.
     *
     * @param env    The environment in which the agent is moving.
     * @param target The target that the agent is pursuing.
     */
    @Override
    public void move(Environment env, Target target) {
        // Store the Environment object
        this.env = env;

        // Increment steps taken
        stepsTaken++;

        // Move to a random node with the highest belief
        currentNode = bestMove();
    }

    /**
     * Determines the best move for the agent based on the belief state.
     *
     * @return The node with the highest belief.
     */
    public int bestMove() {
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

    /**
     * Attempts to capture the target and updates the belief state.
     *
     * @param target The target that the agent is attempting to capture.
     * @return True if the target is captured, false otherwise.
     */
    @Override
    public boolean capture(Target target) {
        // Store the Environment object
        this.env = target.getEnvironment();

        // Update belief state based on the result of examining the node
        boolean captured = target.getCurrentNode() == currentNode;
        if (captured) {
            Arrays.fill(beliefState, 0);
            beliefState[currentNode] = 1;
            // Increment successful captures
            successfulCaptures++;
            return true;
        } else {
            beliefState[currentNode] = 0;
            // Update belief state based on the known movement of the target
            for (int neighbor : env.getNeighbors(currentNode)) {
                beliefState[neighbor] += 1.0 / env.getNeighbors(neighbor).size();
            }
            return false;
        }
    }

    /**
     * Gets the number of steps taken by the agent.
     *
     * @return The number of steps taken.
     */
    @Override
    public int getStepsTaken() {
        return stepsTaken;
    }

    /**
     * Gets the number of successful captures by the agent.
     *
     * @return The number of successful captures.
     */
    public int getSuccessfulCaptures() {
        return successfulCaptures;
    }

    /**
     * Resets the agent to a new starting node.
     *
     * @param startNode The new starting node for the agent.
     * @return A new Agent4 instance with the specified starting node.
     */
    @Override
    public Agent4 reset(int startNode) {
        return new Agent4(startNode);
    }
}
