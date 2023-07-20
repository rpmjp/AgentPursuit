package Pursuit;

class Agent0 extends Agent {
    public Agent0(int startNode) {
        super(startNode);
    }

    @Override
    public void move(Environment env, Target target) {
        // Agent 0 does not move
    }

    @Override
    public boolean capture(Target target) {
        return currentNode == target.getCurrentNode();
    }
}