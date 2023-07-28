package Pursuit;

import java.util.*;

public class Main {
    public static void main(String[] args) {
        int numTrials = 50; // Number of trials to run for each agent

        // Initialize the environment with 40 nodes
        Environment env = new Environment();

        // Initialize the agents
        List<Agent> agents = new ArrayList<>();
        agents.add(new Agent0(new Random().nextInt(40) + 1));
        agents.add(new Agent1(new Random().nextInt(40) + 1));
        agents.add(new Agent2(new Random().nextInt(40) + 1));
        agents.add(new Agent3(env, new Random().nextInt(40) + 1, new Random().nextInt(40) + 1));
        agents.add(new Agent4(env, new Random().nextInt(40) + 1));
        agents.add(new Agent5(env, new Random().nextInt(40) + 1)); // Pass the environment instance here
        agents.add(new Agent6(new Random().nextInt(40) + 1));
        agents.add(new Agent7(new Random().nextInt(40) + 1));

        // Arrays to store the total steps and captures for each agent across all trials
        int[] totalSteps = new int[numTrials];
        int totalCaptures;

        // Run trials for each agent individually
        for (Agent agent : agents) {
            totalCaptures = 0;
            for (int trial = 0; trial < numTrials; trial++) {
                // Reset the environment for a new trial
                env = new Environment();

                // Run the simulation until the target is captured
                boolean gameOver = false;
                while (!gameOver) {
                    // Move the target
                    Target target = new Target(env, new Random().nextInt(40) + 1);
                    target.move(env);

                    // Move the agent and check if it captures the target
                    if (!agent.capture(target)) {
                        agent.move(env, target);
                        agent.incrementStepsTaken();
                    } else {
                        totalCaptures++;
                    }

                    // Check if the agent captured the target
                    if (agent.capture(target)) {
                        gameOver = true;
                    }
                }

                // Record the number of steps taken for the agent in this trial
                totalSteps[trial] = agent.getStepsTaken();
            }

            // Calculate the average number of steps taken and captures for the agent across all trials
            double avgSteps = Arrays.stream(totalSteps).average().orElse(0);
            double capturePercentage = (double) totalCaptures / numTrials * 100;

            // Print the results
            System.out.println(agent.getClass().getSimpleName() + " Average Steps: " + avgSteps +
                    " (Capture Percentage: " + capturePercentage + "%)");
        }
    }
}
