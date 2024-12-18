import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

//ENUM
enum LightState {
    RED, GREEN
}

public class TrafficLight {
    //ATTRIBUTES
    private LightState lightState;
    private final Lock lock = new ReentrantLock();
    private final Condition greenCondition = lock.newCondition();
    private final Queue<Car> waitingCars = new LinkedList<>();

    //METHODS
    public TrafficLight(LightState initialState) {
        this.lightState = initialState;
    }

    public void addCar(Car car) {
        lock.lock();
        try {
            waitingCars.add(car);
        } finally {
            lock.unlock();
        }
    }

    public void waitForGreen() throws InterruptedException {
        lock.lock();
        try {
            // If it’s not green, wait until signaled
            while (lightState != LightState.GREEN) {
                greenCondition.await();
            }
        } finally {
            lock.unlock();
        }
    }

    public Car getNextCar() {
        // Not strictly necessary, but if you manage cars in order
        // you can pop from the waitingCars queue here.
        lock.lock();
        try {
            return waitingCars.peek();
        } finally {
            lock.unlock();
        }
    }

    public void removeCar() {
        lock.lock();
        try {
            waitingCars.poll();
        } finally {
            lock.unlock();
        }
    }

    //GETTERS & SETTERS
    public void setLightState(LightState newState) {
        lock.lock();
        try {
            this.lightState = newState;
            if (lightState == LightState.GREEN) {
                // Wake up all cars waiting at this light
                greenCondition.signalAll();
            }
        } finally {
            lock.unlock();
        }
    }

    public LightState getLightState() {
        lock.lock();
        try {
            return this.lightState;
        } finally {
            lock.unlock();
        }
    }
}
