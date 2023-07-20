package Pursuit;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

class Agent4 extends Agent {
    private double[] beliefState = new double[41]; // Assuming nodes are numbered from 1 to 40
    private Random rand = new Random();
    private Environment env;

    public Agent4(Environment env, int startNode) {
        super(startNode);
        this.env = env;
        Arrays.fill(beliefState, 1.0 / 40); // Initially, the target is equally likely to be in any node
    }

    @Override
    public void move(Environment env, Target target) {
        // Agent 4 does not move
    }

    @Override
    public boolean capture(Target target) {
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
            return true;
        } else {
            beliefState[examinedNode] = 0;
            // Update belief state based on the known movement of the target
            for (int neighbor : env.getNeighbors(examinedNode)) {
                beliefState[neighbor] += 1.0 / env.getNeighbors(neighbor).size();
            }
            return false;
        }
    }
}
