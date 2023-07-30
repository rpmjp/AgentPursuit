/**
 * Main class for running the Pursuit simulation.
 * <p>
 * This class initializes and runs a simulation of various agents pursuing a target within an environment.
 * The simulation is run for a specified number of trials, and the average number of steps taken by each agent
 * to capture the target is calculated and printed.
 * </p>
 * <p>
 * The simulation includes the following steps:
 * <ul>
 *   <li>Initialization of the environment and agents.</li>
 *   <li>Running trials for each agent individually.</li>
 *   <li>Resetting the agent for a new trial.</li>
 *   <li>Moving the target and agent until the target is captured.</li>
 *   <li>Recording the number of steps taken for the agent in each trial.</li>
 *   <li>Calculating and printing the average number of steps taken for each agent.</li>
 * </ul>
 * </p>
 *
 * @author Robert Jean Pierre
 */
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
