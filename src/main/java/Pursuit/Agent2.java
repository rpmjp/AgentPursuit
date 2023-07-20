package Pursuit;

import java.util.*;

class Agent2 extends Agent {
    private Random rand = new Random();
    private Set<Integer> visitedNodes = new HashSet<>();
    private int stepsTaken = 0;
    private int successfulCaptures = 0;

    public Agent2(int startNode) {
        super(startNode);
        visitedNodes.add(startNode);
    }

    @Override
    public void move(Environment env, Target target) {
        // Increment steps taken
        stepsTaken++;

        List<Integer> neighbors = env.getNeighbors(currentNode);
        List<Integer> bestNeighbors = new ArrayList<>();
        int minDistance = Integer.MAX_VALUE;
        int maxUnvisitedNeighbors = 0;

        for (int neighbor : neighbors) {
            int distance = Math.abs(neighbor - target.getCurrentNode());
            int unvisitedNeighbors = (int) env.getNeighbors(neighbor).stream().filter(n -> !visitedNodes.contains(n)).count();

            if (distance < minDistance || (distance == minDistance && unvisitedNeighbors > maxUnvisitedNeighbors)) {
                bestNeighbors.clear();
                bestNeighbors.add(neighbor);
                minDistance = distance;
                maxUnvisitedNeighbors = unvisitedNeighbors;
            } else if (distance == minDistance && unvisitedNeighbors == maxUnvisitedNeighbors) {
                bestNeighbors.add(neighbor);
            }
        }

        currentNode = bestNeighbors.get(rand.nextInt(bestNeighbors.size()));
        visitedNodes.add(currentNode);
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
