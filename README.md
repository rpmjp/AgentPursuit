# Agent Pursuit Game (Java Version) 🕵️‍♂️🤖

## 📝 Description

The **Agent Pursuit Game** is a simulation-based AI project written in Java, where multiple agents employ different strategies to pursue a moving target within a connected environment (graph). The target moves between nodes in the environment, and each agent uses distinct movement algorithms to predict, track, and capture the target. This project showcases various AI approaches, including pathfinding, particle filtering, and belief-based systems.

If you're interested in the Python version, check it out here: [Agent Pursuit (Python Version)](https://github.com/rpmjp/AgentPursuit_Py.git).

## 🚀 Features

- **8 unique agents** each implementing different strategies for pursuing a moving target, ranging from basic random movement to advanced particle filtering and pathfinding algorithms.
- **Dynamic environment** with a connected graph structure where the target moves randomly between nodes.
- **Extensive simulation** that runs multiple trials to assess each agent’s efficiency, recording the average number of steps taken to capture the target.
  
## 🛠️ Technologies Used

- **Java**: The core programming language used for the game logic and agent implementations.
- **OOP (Object-Oriented Programming)**: For the modular structure of agents, environment, and target.
- **Maven**: Used for project build management (`pom.xml` file).

## 📂 Project Structure

The project folder is organized as follows:

```bash
AgentPursuit/
├── src/main/java/Pursuit/
│   ├── Agent.java          # Abstract base class for all agents
│   ├── Agent0.java         # Agent 0 (Does not move)
│   ├── Agent1.java         # Agent 1 (Heuristic-based movement)
│   ├── Agent2.java         # Agent 2 (Breadth-First Search algorithm)
│   ├── Agent3.java         # Agent 3 (Belief state updating)
│   ├── Agent4.java         # Agent 4 (Belief-based movement and updates)
│   ├── Agent5.java         # Agent 5 (Belief state and BFS combo)
│   ├── Agent6.java         # Agent 6 (Examining and belief state refinement)
│   ├── Agent7.java         # Agent 7 (Particle Filtering and Resampling)
│   ├── Environment.java    # Graph environment with nodes and edges
│   ├── Target.java         # Target object that moves in the environment
│   ├── Particle.java       # Particle filtering system for Agent7
│   └── Main.java           # Simulation loop and agent evaluation
├── .gitignore              # Ignoring unnecessary files
├── pom.xml                 # Maven configuration for build management
├── README.md               # Project documentation
└── LICENSE                 # License for the project
```

## 🎮 How It Works

The simulation runs multiple trials where each agent attempts to capture the target within the environment. Each agent uses its own algorithm, and the performance is measured based on the number of steps taken to capture the target across all trials.

- **Agent 0**: Does not move, serves as a baseline.
- **Agent 1**: Uses heuristic-based movement, where the agent moves to the closest node to the target.
- **Agent 2**: Employs Breadth-First Search (BFS) to find the shortest path to the target.
- **Agent 3**: Maintains and updates a belief state about the target’s location.
- **Agent 4**: Combines belief state updates with movement based on the most probable location.
- **Agent 5**: Uses BFS in combination with belief states for target prediction.
- **Agent 6**: Examines nodes and refines belief states to improve tracking efficiency.
- **Agent 7**: Leverages Particle Filtering to maintain a probabilistic estimate of the target’s location and resamples particles based on observations.

## 🖼️ Screenshots

Here’s a snippet of **Agent7’s** particle filtering algorithm and **Agent5’s** BFS algorithm for clarity:

![Particle Filtering Algorithm]

```java
/**
     * Updates the particle's position and weight based on the given likelihoods, transition matrix,
     * random number generator, particles list, and number of particles.
     *
     * <p>This method performs the particle filter update step, including prediction and resampling.</p>
     *
     * @param likelihoods        the likelihoods for each position
     * @param transitionMatrix   the transition probabilities matrix
     * @param rand               the random number generator
     * @param particles          the list of particles
     * @param NUM_PARTICLES      the number of particles
     */
    public void update(double[] likelihoods, double[][] transitionMatrix, Random rand, List<Particle> particles, int NUM_PARTICLES) {
        // Particle Filter update step
        int newPosition = predictNextPosition(transitionMatrix, rand);
        double weight = likelihoods[newPosition];
        setWeight(weight);
        setPosition(newPosition);

        // Resampling: Replacing particles based on their weights (likelihoods)
        double totalWeight = particles.stream().mapToDouble(Particle::getWeight).sum();
        resample(totalWeight, particles, NUM_PARTICLES);
    }

    /**
     * Resamples the particle based on its weight relative to the total weight of all particles.
     * The particle's weight is adjusted, and it may be replaced with a new particle from the current list of particles.
     *
     * @param totalWeight   the total weight of all particles
     * @param particles     the list of particles
     * @param NUM_PARTICLES the number of particles
     */
    private void resample(double totalWeight, List<Particle> particles, int NUM_PARTICLES) {
        // Resample the particle based on its weight
        setWeight(getWeight() / totalWeight);

        // Replace the particle with a new one from the current list of particles
        Particle newParticle = particles.get(0);
        double randomValue = Math.random();
        for (int i = 0; i < NUM_PARTICLES; i++) {
            if (randomValue <= particles.get(i).getWeight()) {
                newParticle = particles.get(i);
                break;
            }
        }

        position = newParticle.getPosition();
        setWeight(1.0); // Reset the weight after resampling
    }

```

![BFS Algorithm]

```java
/**
     * Helper method to find the shortest path from startNode to targetNode using BFS.
     *
     * @param env        the environment
     * @param startNode  the starting node
     * @param targetNode the target node
     * @return the next node in the shortest path
     */
    // Helper method to find the shortest path from startNode to targetNode using BFS
    private int bfsShortestPath(Environment env, int startNode, int targetNode) {
        Queue<Integer> queue = new LinkedList<>();
        queue.add(startNode);

        // Array to keep track of visited nodes during BFS
        boolean[] visited = new boolean[41];
        visited[startNode] = true;

        // Array to keep track of the parent nodes in the shortest path
        int[] parent = new int[41];
        Arrays.fill(parent, -1);

        while (!queue.isEmpty()) {
            int currentNode = queue.poll();

            // If the target node is found, reconstruct the shortest path and return the next node to move
            if (currentNode == targetNode) {
                int nextNode = targetNode;
                while (parent[nextNode] != startNode) {
                    nextNode = parent[nextNode];
                }
                return nextNode;
            }

            for (int neighbor : env.getNeighbors(currentNode)) {
                if (!visited[neighbor]) {
                    queue.add(neighbor);
                    visited[neighbor] = true;
                    parent[neighbor] = currentNode;
                }
            }
        }

        // If the target node is not reachable, return the current node
        return startNode;
    }
```

## 📊 Performance Metrics

Each agent's performance is evaluated by calculating the average number of steps taken to capture the target. The results are printed in the console at the end of each trial.

## 🧑‍💻 Running the Project

1. Clone the repository:
   ```bash
   git clone https://github.com/rpmjp/AgentPursuit.git
   ```
2. Navigate to the project directory and run Maven to build the project:
   ```bash
   mvn clean install
   ```
3. Execute the **Main.java** file to run the simulation:
   ```bash
   java -cp target/AgentPursuit-1.0-SNAPSHOT.jar Pursuit.Main
   ```

## 📝 License

This project is released under the Unlicense. You are free to use, modify, and distribute the software for any purpose.

For more information, please refer to the [LICENSE](./LICENSE).

---

Feel free to explore the project, modify agents, and experiment with new strategies to improve pursuit efficiency! If you have any questions or suggestions, feel free to reach out to me on [GitHub](https://github.com/rpmjp).

