package Pursuit;

import java.util.Arrays;
import java.util.Random;

class Agent3 extends Agent {
    private double[] beliefState = new double[41]; // Assuming nodes are numbered from 1 to 40
    private int examinedNode;
    private int stepsTaken = 0;
    private int successfulCaptures = 0;
    private Random rand; // Add a Random object

    public Agent3(int startNode) {
        super(startNode);
        this.examinedNode = startNode; // Assuming the examined node is the start node
        Arrays.fill(beliefState, 1.0 / 40); // Initially, the target is equally likely to be in any node
    }

    @Override
    public void move(Environment env, Target target) {
        // Increment steps taken
        //stepsTaken++;
    }

    @Override
    public boolean capture(Target target) {
        boolean captured = currentNode == target.getCurrentNode();
        if (captured) {
            // Increment successful captures
            successfulCaptures++;
        }
        return captured;
    }

    @Override
    public int getStepsTaken() {
        return stepsTaken;
    }

    public int getSuccessfulCaptures() {
        return successfulCaptures;
    }

    @Override
    public Agent3 reset(int startNode) {
        return new Agent3(startNode);
    }
}
