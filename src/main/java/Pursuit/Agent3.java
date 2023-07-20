package Pursuit;

import java.util.Arrays;

class Agent3 extends Agent {
    private double[] beliefState = new double[41]; // Assuming nodes are numbered from 1 to 40
    private int examinedNode;
    private Environment env;

    public Agent3(Environment env, int startNode, int examinedNode) {
        super(startNode);
        this.examinedNode = examinedNode;
        this.env = env;  // Assign the passed environment to the field
        Arrays.fill(beliefState, 1.0 / 40); // Initially, the target is equally likely to be in any node
    }

    @Override
    public void move(Environment env, Target target) {
        // Agent 3 does not move
    }

    @Override
    public boolean capture(Target target) {
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
