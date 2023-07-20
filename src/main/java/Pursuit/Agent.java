package Pursuit;

abstract class Agent {
    protected int currentNode;

    public Agent(int startNode) {
        this.currentNode = startNode;
    }

    public abstract void move(Environment env, Target target);
    public abstract boolean capture(Target target);
}