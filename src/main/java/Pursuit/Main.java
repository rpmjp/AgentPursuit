package Pursuit;

import java.util.*;

public class Main {
    public static void main(String[] args) {
        int numTrials = 100; // Number of trials to run for each agent

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
        int[][] totalSteps = new int[agents.size()][numTrials];
        int[][] totalCaptures = new int[agents.size()][numTrials];

        // Run trials
        for (int trial = 0; trial < numTrials; trial++) {
            // Reset the environment for a new trial
            env = new Environment();

            // Run the simulation until the target is captured
            boolean gameOver = false;
            while (!gameOver) {
                // Move the target
                Target target = new Target(env, new Random().nextInt(40) + 1);
                target.move(env);

                // Move each agent and check if it captures the target
                for (int i = 0; i < agents.size(); i++) {
                    if (!agents.get(i).capture(target)) {
                        agents.get(i).move(env, target);
                        agents.get(i).incrementStepsTaken();
                    } else {
                        totalCaptures[i][trial]++;
                    }
                }

                // Check if any agent captured the target
                for (int i = 0; i < agents.size(); i++) {
                    if (agents.get(i).capture(target)) {
                        gameOver = true;
                        break;
                    }
                }
            }

            // Record the number of steps taken for each agent in this trial
            for (int i = 0; i < agents.size(); i++) {
                totalSteps[i][trial] = agents.get(i).getStepsTaken();
            }
        }

        // Calculate the average number of steps taken for each agent across all trials
        double[] avgSteps = new double[agents.size()];
        double[] avgCaptures = new double[agents.size()];
        for (int i = 0; i < agents.size(); i++) {
            avgSteps[i] = Arrays.stream(totalSteps[i]).average().orElse(0);
            avgCaptures[i] = Arrays.stream(totalCaptures[i]).average().orElse(0);
        }

        // Print the results
        for (int i = 0; i < agents.size(); i++) {
            System.out.println("Agent" + (i) + " Average Steps: " + avgSteps[i] +
                    " (Average Captures: " + avgCaptures[i] + ")");
        }
    }
}
