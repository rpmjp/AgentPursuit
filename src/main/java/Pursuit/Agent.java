package Pursuit;

abstract class Agent {
    protected int currentNode;
    protected int stepsTaken = 0;
    protected int successfulCaptures = 0;

    //Agent constructor
    public Agent(int startNode) {
        this.currentNode = startNode;
    }

    public abstract void move(Environment env, Target target);

    public abstract boolean capture(Target target);

    public void incrementStepsTaken() {
        this.stepsTaken++;
    }

    public void incrementSuccessfulCaptures() {
        this.successfulCaptures++;
    }

    public abstract int getStepsTaken(); // Changed return type to int

    public int getSuccessfulCaptures() {
        return this.successfulCaptures;
    }

    public abstract Agent reset(int startNode);
}
