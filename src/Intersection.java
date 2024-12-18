import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Intersection extends Thread {
    //ATTRIBUTES
    public static Lock intersectionLock = new ReentrantLock();
    public static TrafficLight verticalLight;
    public static TrafficLight horizontalLight;

    private final long cycleTime;

    //METHODS
    public Intersection(long cycleTime) {
        this.cycleTime = cycleTime;
        verticalLight = new TrafficLight(LightState.GREEN);
        horizontalLight = new TrafficLight(LightState.RED);

    }

    public void acquireIntersection() {
        intersectionLock.lock();
    }

    public void releaseIntersection() {
        intersectionLock.unlock();
    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(cycleTime);

                verticalLight.setLightState(LightState.RED);
                horizontalLight.setLightState(LightState.GREEN);

                Thread.sleep(cycleTime);

                verticalLight.setLightState(LightState.GREEN);
                horizontalLight.setLightState(LightState.RED);

            } catch (InterruptedException e) {
                break;
            }
        }
    }


    //GETTERS & SETTERS
    public TrafficLight getVerticalLight() {
        return verticalLight;
    }

    public TrafficLight getHorizontalLight() {
        return horizontalLight;
    }
}
