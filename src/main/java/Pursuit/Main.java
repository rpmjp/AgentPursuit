package Pursuit;

import java.util.*;

public class Main {
    public static void main(String[] args) {
        int numTrials = 100; // Number of trials to run for each agent
        long seed = 42; // Use any seed value you prefer (e.g., 42)

        Random random = new Random(seed); // Use any seed value you prefer (e.g., 42)
        Environment env = new Environment();

        // Initialize the agents
        List<Agent> agents = new ArrayList<>();
        agents.add(new Agent0(new Random().nextInt(40) + 1));
        agents.add(new Agent1(new Random().nextInt(40) + 1));
        agents.add(new Agent2(new Random().nextInt(40) + 1));
        agents.add(new Agent3(new Random().nextInt(40) + 1));
        agents.add(new Agent4(new Random().nextInt(40) + 1));
        agents.add(new Agent5(new Random().nextInt(40) + 1));
        agents.add(new Agent6(new Random().nextInt(40) + 1));
        agents.add(new Agent7(new Random().nextInt(40) + 1));

        // Run trials for each agent individually
        for (Agent agent : agents) {
            double totalSteps = 0;

            for (int trial = 0; trial < numTrials; trial++) {
                // Reset the agent for a new trial
                agent = agent.reset(new Random().nextInt(40) + 1);

                // Run the simulation until the target is captured
                boolean gameOver = false;
                Target target = new Target(env, new Random().nextInt(40) + 1);
                while (!gameOver) {
                    // Move the target
                    target.move(env);

                    // Move the agent and check if it captures the target
                    if (!agent.capture(target)) {
                        agent.move(env, target);
                    }

                    // Check if the agent captured the target
                    if (agent.capture(target)) {
                        gameOver = true;
                    }
                }

                // Record the number of steps taken for the agent in this trial
                totalSteps += agent.getStepsTaken();
            }

            // Calculate the average number of steps taken for the agent across all trials
            double avgSteps = totalSteps / numTrials;

            // Print the results
            System.out.println(agent.getClass().getSimpleName() + " Average Steps: " + avgSteps);
        }
    }
}
