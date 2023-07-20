package Pursuit;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

class Agent1 extends Agent {
    private Random rand = new Random();
    private int stepsTaken = 0;
    private int successfulCaptures = 0;

    public Agent1(int startNode) {
        super(startNode);
    }

    @Override
    public void move(Environment env, Target target) {
        // Increment steps taken
        stepsTaken++;

        List<Integer> neighbors = env.getNeighbors(currentNode);
        List<Integer> bestNeighbors = new ArrayList<>();
        int minDistance = Integer.MAX_VALUE;

        for (int neighbor : neighbors) {
            int distance = Math.abs(neighbor - target.getCurrentNode());
            if (distance < minDistance) {
                bestNeighbors.clear();
                bestNeighbors.add(neighbor);
                minDistance = distance;
            } else if (distance == minDistance) {
                bestNeighbors.add(neighbor);
            }
        }

        currentNode = bestNeighbors.get(rand.nextInt(bestNeighbors.size()));
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

    public int getStepsTaken() {
        return stepsTaken;
    }

    public int getSuccessfulCaptures() {
        return successfulCaptures;
    }
}
