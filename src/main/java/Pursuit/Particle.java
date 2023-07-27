package Pursuit;

import java.util.List;
import java.util.Random;
public class Particle {
    private int position;

    public Particle(int position) {
        this.position = position;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public void update(double[] likelihoods, double[][] transitionMatrix, Random rand, List<Particle> particles, int NUM_PARTICLES) {
        // Particle Filter update step
        int newPosition = predictNextPosition(transitionMatrix, rand);
        double weight = likelihoods[newPosition];
        setPosition(newPosition);

        // Resampling: Replacing particles based on their weights (likelihoods)
        if (rand.nextDouble() > weight) {
            setPosition(particles.get(rand.nextInt(NUM_PARTICLES)).getPosition());
        }
    }

    private int predictNextPosition(double[][] transitionMatrix, Random rand) {
        // Use the transition probabilities to predict the next position of the particle
        // In this example, we'll use the current position as the prediction
        int sumProbabilities = 0;
        for (int i = 1; i <= 40; i++) {
            sumProbabilities += transitionMatrix[position][i] * 1000;
        }
        int randomNum = rand.nextInt(sumProbabilities);
        int cumulativeProb = 0;
        for (int i = 1; i <= 40; i++) {
            cumulativeProb += transitionMatrix[position][i] * 1000;
            if (randomNum < cumulativeProb) {
                return i;
            }
        }
        return position;
    }
}
