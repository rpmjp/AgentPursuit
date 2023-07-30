/**
 * Agent3 class representing an agent in the Pursuit simulation.
 * <p>
 * This agent maintains a belief state, representing the probability distribution over the possible locations of the target.
 * The agent repeatedly examines a specific node and updates its belief state based on the examination result.
 * </p>
 * <p>
 * The belief state is initialized with equal probability for all nodes, and it is updated based on the result of examining
 * the specified node and the known movement of the target.
 * </p>
 *
 * @author Robert Jean Pierre
 */
package Pursuit;

import java.util.Arrays;
import java.util.List;

class Agent3 extends Agent {
    private double[] beliefState = new double[41]; // Assuming nodes are numbered from 1 to 40
    private int examinedNode;
    private int stepsTaken = 0;
    private int successfulCaptures = 0;

    /**
     * Constructs a new Agent3 with the given starting node.
     *
     * @param startNode The starting node for the agent.
     */
    public Agent3(int startNode) {
        super(startNode);
        this.examinedNode = startNode; // Assuming the examined node is the start node
        Arrays.fill(beliefState, 1.0 / 40); // Initially, the target is equally likely to be in any node
    }

    /**
     * Moves the agent by examining the specified node and updating the belief state.
     *
     * @param env    The environment in which the agent is moving.
     * @param target The target that the agent is pursuing.
     */
    @Override
    public void move(Environment env, Target target) {
        // Examine the same node every time
        boolean foundTarget = examineNode(target, examinedNode);

        // Update belief state based on examination result
        updateBeliefState(env, foundTarget);

        // Increment steps taken
        //stepsTaken++;
    }

    /**
     * Examines the specified node to check if the target is present.
     *
     * @param target The target that the agent is examining.
     * @param node   The node to be examined.
     * @return True if the target is at the examined node, false otherwise.
     */
    private boolean examineNode(Target target, int node) {
        // Check if the target is at the examined node
        return node == target.getCurrentNode();
    }

    /**
     * Updates the belief state based on the examination result.
     *
     * @param env         The environment in which the agent is moving.
     * @param foundTarget True if the target was found at the examined node, false otherwise.
     */
    private void updateBeliefState(Environment env, boolean foundTarget) {
        if (foundTarget) {
            // If the target is found, update the belief state to certainty
            Arrays.fill(beliefState, 0);
            beliefState[examinedNode] = 1.0;
            successfulCaptures++;
        } else {
            // If the target is not found, update the belief state based on how the target moves
            double[] newBeliefState = new double[41];
            for (int i = 1; i <= 40; i++) {
                List<Integer> neighbors = env.getNeighbors(i);
                for (int neighbor : neighbors) {
                    newBeliefState[neighbor] += beliefState[i] / neighbors.size();
                }
            }
            // Set the belief state for the examined node to 0 since the target was not found there
            newBeliefState[examinedNode] = 0;
            beliefState = newBeliefState;
        }
    }

    /**
     * Attempts to capture the target.
     *
     * @param target The target that the agent is attempting to capture.
     * @return True if the target is captured, false otherwise.
     */
    @Override
    public boolean capture(Target target) {
        boolean captured = currentNode == target.getCurrentNode();
        if (captured) {
            // Increment successful captures
            successfulCaptures++;
        }
        return captured;
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
     * @return A new Agent3 instance with the specified starting node.
     */
    @Override
    public Agent3 reset(int startNode) {
        return new Agent3(startNode);
    }
}
