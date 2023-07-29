package Pursuit;

import java.util.List;
import java.util.Random;

public class Particle {
    private int position;
    private double weight;

    public Particle(int position) {
        this.position = position;
        this.weight = 1.0;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

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
