package Pursuit;

class Agent0 extends Agent {
    private int stepsTaken = 0;
    private int successfulCaptures = 0;
    private Environment environment; // Add a class member to store the Environment instance


    public Agent0(int startNode) {
        super(startNode);
    }

    @Override
    public void move(Environment env, Target target) {
        // note Agent 0 does not move
    }

    @Override
    public boolean capture(Target target) {
        boolean captured = currentNode == target.getCurrentNode();
        if (captured) {
            // Increment successful captures
            successfulCaptures++;
        }
        return captured;
    }

    @Override
    public int getStepsTaken() {
        return stepsTaken;
    }

    public int getSuccessfulCaptures() {
        return successfulCaptures;
    }
    @Override
    public Agent0 reset(int startNode) {
        return new Agent0(startNode);
    }

}
