package Pursuit;

import java.util.*;

public class Main {
    public static void main(String[] args) {
        // Initialize the environment with 40 nodes
        Environment env = new Environment();
        // Initialize the target at a random node
        Target target = new Target(env, new Random().nextInt(40) + 1);

        // Initialize the agents
        List<Agent> agents = new ArrayList<>();
        agents.add(new Agent0(new Random().nextInt(40) + 1));
        agents.add(new Agent1(new Random().nextInt(40) + 1));
        agents.add(new Agent2(new Random().nextInt(40) + 1));
        agents.add(new Agent3(env, new Random().nextInt(40) + 1, new Random().nextInt(40) + 1));
        agents.add(new Agent4(env, new Random().nextInt(40) + 1));
        agents.add(new Agent5(env, new Random().nextInt(40) + 1));
        agents.add(new Agent6(new Random().nextInt(40) + 1));
        agents.add(new Agent7(new Random().nextInt(40) + 1));

        // Run the simulation until the target is captured
        boolean gameOver = false;
        while (!gameOver) {
            // Move the target
            target.move(env);

            // Move each agent and check if it captures the target
            for (Agent agent : agents) {
                agent.move(env, target);
                if (agent.capture(target)) {
                    System.out.println("Target captured by " + agent.getClass().getSimpleName());
                    gameOver = true;
                    break;
                }
            }
        }
    }
}
