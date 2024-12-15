import java.util.LinkedList;
import java.util.List;

public class TrafficLight extends Thread {
    //CONSTANTS
    private final float ORIGIN_X;
    private final float ORIGIN_Y;

    //ATTRIBUTES
    private State state;
    private List<Car> cars = new LinkedList<Car>();

    //METHODS
    public TrafficLight() {
        this.state = State.RED;
        this.ORIGIN_X = 0;
        this.ORIGIN_Y = 0;
    }

    public TrafficLight(State state, float originX, float originY) {
        this.state = state;
        this.ORIGIN_X = originX;
        this.ORIGIN_Y = originY;
    }

    public void addCar() {
        Car car = new Car(this.ORIGIN_X, this.ORIGIN_Y);
        this.cars.add(car);
    }

    public void crossCar() {
        Car crossingCar = cars.removeFirst();
        int distance = 0;

        while (distance < Intersection.LENGTH) {
            crossingCar.move(Math.signum(Math.abs(ORIGIN_X)), Math.signum(Math.abs(ORIGIN_Y)));
            distance += Intersection.LENGTH;
        }
    }

    @Override
    public void run() {
        while (true) {
            if (this.state == State.GREEN) {
                try {
                    Intersection.intersection.acquire();
                    crossCar();
                    Intersection.intersection.release();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    //GETTERS & SETTERS
    public boolean isGreen() {
        return this.state == State.GREEN;
    }

    public boolean isRed() {
        return this.state == State.RED;
    }

    public void setGreen() {
        this.state = State.GREEN;
    }

    public void setRed() {
        this.state = State.RED;
    }

    //ENUM
    enum State {
        GREEN, RED
    }
}
