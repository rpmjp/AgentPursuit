package Pursuit;

import java.util.*;

class Target {
    private int currentNode;
    private Random rand = new Random();

    public Target(Environment env, int startNode) {
        this.currentNode = startNode;
    }

    public void move(Environment env) {
        List<Integer> neighbors = env.getNeighbors(currentNode);
        currentNode = neighbors.get(rand.nextInt(neighbors.size()));
    }

    public int getCurrentNode() {
        return currentNode;
    }
}
