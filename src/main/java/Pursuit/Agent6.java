package Pursuit;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

class Agent6 extends Agent {
    private double[] beliefState = new double[41]; // Assuming nodes are numbered from 1 to 40
    private Random rand = new Random();

    public Agent6(int startNode) {
        super(startNode);
        Arrays.fill(beliefState, 1.0 / 40); // Initially, the target is equally likely to be in any node
    }

    @Override
    public void move(Environment env, Target target) {
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

        // Examine a random node with the highest belief
        int examinedNode = bestNodes.get(rand.nextInt(bestNodes.size()));

        // Update belief state based on the result of examining the node
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

        // Move to reduce the distance to the node with the highest belief
        List<Integer> neighbors = env.getNeighbors(currentNode);
        bestNodes.clear();
        maxBelief = 0;
        for (int neighbor : neighbors) {
            if (beliefState[neighbor] > maxBelief) {
                bestNodes.clear();
                bestNodes.add(neighbor);
                maxBelief = beliefState[neighbor];
            } else if (beliefState[neighbor] == maxBelief) {
                bestNodes.add(neighbor);
            }
        }
        currentNode = bestNodes.get(rand.nextInt(bestNodes.size()));
    }

    @Override
    public boolean capture(Target target) {
        return currentNode == target.getCurrentNode();
    }
}
