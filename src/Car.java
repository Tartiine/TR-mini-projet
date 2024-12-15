public class Car {
    //CONSTANTS
    private static final int SPEED = 10;

    //ATTRIBUTES
    private State state;
    private float positionX;
    private float positionY;

    //METHODS
    public Car() {
        this.state = State.STOPPED;
        this.positionX = 0;
        this.positionY = 0;
    }

    public Car(float positionX, float positionY) {
        this.state = State.MOVING;
        this.positionX = positionX;
        this.positionY = positionY;
    }

    public void switchState() {
        if (this.state == null) {
            throw new IllegalStateException("State cannot be null");
        }

        switch (this.state) {
            case STOPPED:
                this.state = State.MOVING;
                break;
            case MOVING:
                this.state = State.STOPPED;
                break;
            default:
                throw new IllegalStateException("Unhandled state: " + this.state);
        }
    }

    public void move(float x, float y) {
        if (this.state == State.MOVING) {
            this.positionX += SPEED * x;
            this.positionY += SPEED * y;
        }
    }

    //GETTERS & SETTERS
    public State getState() {
        return this.state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public float getPositionX() {
        return positionX;
    }

    public void setPositionX(float positionX) {
        this.positionX = positionX;
    }

    public float getPositionY() {
        return positionY;
    }

    public void setPositionY(float positionY) {
        this.positionY = positionY;
    }

    //ENUM
    enum State {
        MOVING, STOPPED
    }
}