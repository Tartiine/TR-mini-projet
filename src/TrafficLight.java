public class TrafficLight {
    private final long duration;
    private State state;

    public enum State {
        RED, GREEN
    }

    public TrafficLight(State state) {
        this.state = state;
    }
}