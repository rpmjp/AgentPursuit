/**
 * Represents a particle in a particle filter algorithm. Each particle has a position and weight,
 * and the class provides methods to update these properties based on transition probabilities and
 * likelihoods.
 *
 * <p>The particle filter algorithm is used in various applications, such as tracking and prediction.
 * This class encapsulates the behavior of a single particle within the filter, including prediction,
 * resampling, and weight adjustment.</p>
 *
 * <h3>Usage Example:</h3>
 * <pre>
 * {@code
 * Particle particle = new Particle(5);
 * double[] likelihoods = {...};
 * double[][] transitionMatrix = {...};
 * Random rand = new Random();
 * List<Particle> particles = {...};
 * int NUM_PARTICLES = 100;
 * particle.update(likelihoods, transitionMatrix, rand, particles, NUM_PARTICLES);
 * }
 * </pre>
 *
 * @author [Robert Jean Pierre]
 * @see Random
 * @see List
 */
package Pursuit;

import java.util.List;
import java.util.Random;

public class Particle {
    private int position;
    private double weight;

    /**
     * Constructs a new particle with the specified initial position.
     * The weight of the particle is initialized to 1.0.
     *
     * @param position the initial position of the particle
     */
    public Particle(int position) {
        this.position = position;
        this.weight = 1.0;
    }

    /**
     * Returns the current position of the particle.
     *
     * @return the position of the particle
     */
    public int getPosition() {
        return position;
    }

    /**
     * Sets the position of the particle.
     *
     * @param position the new position of the particle
     */
    public void setPosition(int position) {
        this.position = position;
    }

    /**
     * Returns the current weight of the particle.
     *
     * @return the weight of the particle
     */
    public double getWeight() {
        return weight;
    }

    /**
     * Sets the weight of the particle.
     *
     * @param weight the new weight of the particle
     */
    public void setWeight(double weight) {
        this.weight = weight;
    }

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

    /**
     * Predicts the next position of the particle using the given transition matrix and random number generator.
     * The prediction is based on the transition probabilities and the current position of the particle.
     *
     * @param transitionMatrix the transition probabilities matrix
     * @param rand             the random number generator
     * @return the predicted next position of the particle
     */
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
