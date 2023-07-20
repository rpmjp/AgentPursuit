package Pursuit;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

class Agent1 extends Agent {
    private Random rand = new Random();

    public Agent1(int startNode) {
        super(startNode);
    }

    @Override
    public void move(Environment env, Target target) {
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
        return currentNode == target.getCurrentNode();
    }
}
