package Pursuit;

import java.util.*;

class Agent2 extends Agent {
    private Random rand = new Random();
    private int stepsTaken = 0;
    private int successfulCaptures = 0;

    public Agent2(int startNode) {
        super(startNode);
    }

    @Override
    public void move(Environment env, Target target) {
        // Increment steps taken
        stepsTaken++;

        // Find the shortest path using BFS from the current node to the target node
        List<Integer> shortestPath = findShortestPath(env, target.getCurrentNode());

        // Move to the next node in the shortest path (if available)
        if (shortestPath.size() > 1) {
            int nextNode = shortestPath.get(1);
            currentNode = nextNode;
        }
    }

    @Override
    public boolean capture(Target target) {
        // Check if the agent captures the target
        boolean captured = getCurrentNode() == target.getCurrentNode();
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

    public int getCurrentNode() {
        return currentNode;
    }

    private List<Integer> findShortestPath(Environment env, int targetNode) {
        Queue<List<Integer>> queue = new LinkedList<>();
        Set<Integer> visited = new HashSet<>();
        queue.offer(new ArrayList<>(Collections.singletonList(currentNode)));
        visited.add(currentNode);

        while (!queue.isEmpty()) {
            List<Integer> path = queue.poll();
            int currentNode = path.get(path.size() - 1);

            if (currentNode == targetNode) {
                return path;
            }

            for (int neighbor : env.getNeighbors(currentNode)) {
                if (!visited.contains(neighbor)) {
                    List<Integer> newPath = new ArrayList<>(path);
                    newPath.add(neighbor);
                    queue.offer(newPath);
                    visited.add(neighbor);
                }
            }
        }

        // Return an empty list if there is no path to the target
        return new ArrayList<>();
    }
    @Override
    public Agent2 reset(int startNode) {
        return new Agent2(startNode);
    }
}
