package Pursuit;

import java.util.*;

class Agent1 extends Agent {
    private Random rand = new Random();
    private int stepsTaken = 0;
    private int successfulCaptures = 0;
    private Environment env;

    public Agent1(int startNode) {
        super(startNode);
    }

    @Override
    public void move(Environment env, Target target) {
        // Increment steps taken
        stepsTaken++;

        List<Integer> shortestPath = findShortestPath(currentNode, target.getCurrentNode(), env);
        if (shortestPath.size() > 1) {
            // 70% of the time, choose the next node in the shortest path
            if (rand.nextDouble() < 0.7) {
                currentNode = shortestPath.get(1); // get the next node in the path
            } else {
                // 30% of the time, choose a random neighbor that is closer to the target
                List<Integer> closerNeighbors = new ArrayList<>();
                for (int neighbor : env.getNeighbors(currentNode)) {
                    if (Math.abs(neighbor - target.getCurrentNode()) < Math.abs(currentNode - target.getCurrentNode())) {
                        closerNeighbors.add(neighbor);
                    }
                }
                if (!closerNeighbors.isEmpty()) {
                    currentNode = closerNeighbors.get(rand.nextInt(closerNeighbors.size()));
                } else {
                    currentNode = shortestPath.get(1); // if no closer neighbors, follow the shortest path
                }
            }
        }
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

    private List<Integer> findShortestPath(int startNode, int targetNode, Environment env) {
        Queue<List<Integer>> queue = new LinkedList<>();
        Set<Integer> visited = new HashSet<>();
        queue.offer(new ArrayList<>(Collections.singletonList(startNode)));
        visited.add(startNode);

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

        // return an empty list, if there is no path
        return new ArrayList<>();
    }
}
