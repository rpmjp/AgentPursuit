/**
 * <p><code>Agent6</code> is a specialized agent class that extends the {@link Agent} class.
 * It represents an agent that uses belief states to predict the target's location and
 * makes decisions based on the belief state.</p>
 *
 * <p>The belief state is an array where each element represents the probability that the target
 * is in a specific node. The agent examines nodes, updates its belief state, and moves accordingly.</p>
 *
 * @see Agent
 * @see Environment
 * @see Target
 */
package Pursuit;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

class Agent6 extends Agent {
    private double[] beliefState = new double[41]; // Assuming nodes are numbered from 1 to 40
    private Random rand = new Random();
    private int stepsTaken = 0;
    private int successfulCaptures = 0;

    /**
     * Constructs a new <code>Agent6</code> object and initializes the belief state.
     *
     * @param startNode The starting node of the agent.
     */
    public Agent6(int startNode) {
        super(startNode);
        Arrays.fill(beliefState, 1.0 / 40); // Initially, the target is equally likely to be in any node
    }

    /**
     * Overrides the move method to update the belief state and make the best move.
     *
     * @param env    The environment in which the agent operates.
     * @param target The target that the agent is trying to capture.
     */
    @Override
    public void move(Environment env, Target target) {
        updateBeliefStateAndBestMove(env, target);
    }

    /**
     * Updates the belief state of the agent based on the examination of a node with the highest belief.
     * Then, determines the best move to reduce the distance to the node with the highest belief and
     * updates the current node if a better option is found.
     *
     * <p>This method encapsulates the logic for examining a node, updating the belief state, and
     * making the best move. It is a key part of the agent's decision-making process.</p>
     *
     * @param env    The environment in which the agent operates.
     * @param target The target that the agent is trying to capture.
     * @see #examineNode()
     * @see #updateBeliefState(Environment, Target, int)
     * @see #bestMove(Environment, int)
     */
    public void updateBeliefStateAndBestMove(Environment env, Target target) {
        // Examine a random node with the highest belief
        int examinedNode = examineNode();

        // Update belief state based on the result of examining the node
        updateBeliefState(env, target, examinedNode);

        // Move to reduce the distance to the node with the highest belief
        int nextNode = bestMove(env, examinedNode);
        if (nextNode != currentNode) {
            stepsTaken++;
            currentNode = nextNode;
        }
    }

    /**
     * Examines a random node with the highest belief and returns its index.
     *
     * @return The index of the examined node.
     */
    public int examineNode() {
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
     * Updates the belief state based on the result of examining a node.
     *
     * @param env           The environment in which the agent operates.
     * @param target        The target that the agent is trying to capture.
     * @param examinedNode  The node that was examined by the agent.
     */
    public void updateBeliefState(Environment env, Target target, int examinedNode) {
        if (target.getCurrentNode() == examinedNode) {
            Arrays.fill(beliefState, 0);
            beliefState[examinedNode] = 1;
        } else {
            beliefState[examinedNode] = 0;
            // Update belief state based on the known movement of the target
            for (int neighbor : env.getNeighbors(examinedNode)) {
                beliefState[neighbor] += 1.0 / env.getNeighbors(neighbor).size();
            }
        }
    }

    /**
     * Determines the best move to reduce the distance to the examined node.
     *
     * @param env           The environment in which the agent operates.
     * @param examinedNode  The node that was examined by the agent.
     * @return The index of the best node to move to.
     */
    public int bestMove(Environment env, int examinedNode) {
        // Find the adjacent node that reduces the distance to the examined node
        List<Integer> neighbors = env.getNeighbors(currentNode);
        int bestNode = currentNode; // Stay in the current node if no better option
        int currentDistance = Math.abs(currentNode - examinedNode);
        for (int neighbor : neighbors) {
            int newDistance = Math.abs(neighbor - examinedNode);
            if (newDistance < currentDistance) {
                bestNode = neighbor;
                currentDistance = newDistance;
            }
        }

        return bestNode;
    }

    /**
     * Attempts to capture the target and increments successful captures if successful.
     *
     * @param target The target that the agent is trying to capture.
     * @return <code>true</code> if the target was captured; <code>false</code> otherwise.
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
     * Returns the number of steps taken by the agent.
     *
     * @return The number of steps taken.
     */
    @Override
    public int getStepsTaken() {
        return stepsTaken;
    }

    /**
     * Returns the number of successful captures by the agent.
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
     * @return A new <code>Agent6</code> object with the specified starting node.
     */
    @Override
    public Agent6 reset(int startNode) {
        return new Agent6(startNode);
    }
}
