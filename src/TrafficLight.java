import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javafx.scene.image.ImageView;

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

    public boolean tryCross(Car car) {
        lock.lock();
        try {
            return waitingCars.peek() == car;
        } finally {
            lock.unlock();
        }
    }

    public Car getNextCar() {
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

    public boolean isGreen() {
        lock.lock();
        try {
            return this.lightState == LightState.GREEN;
        } finally {
            lock.unlock();
        }
    }

    public List<Car> getAllCars() {
        lock.lock();
        try {
            return new ArrayList<>(waitingCars); 
        } finally {
            lock.unlock();
        }
    }

    
}
