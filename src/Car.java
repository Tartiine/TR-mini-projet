public class Car {
    private final int id;
    private final long arrivalTime;
    private final Direction direction;
    private State state;

    public enum State {
        WAITING, CROSSING, FINISHED
    }

    public enum Direction {
        VERTICAL, HORIZONTAL
    }

    public Car(int id, long arrivalTime, Direction direction) {
        this.id = id;
        this.arrivalTime = arrivalTime;
        this.direction = direction;
        this.state = State.WAITING;
    }
}
