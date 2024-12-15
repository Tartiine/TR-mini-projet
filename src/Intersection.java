import java.util.concurrent.Semaphore;

public class Intersection extends Thread {
    //CONSTANTS
    public static final int LENGTH = 128;
    public static final int LIGHT_DURATION = 10000;

    //ATTRIBUTES
    public static Semaphore intersection = new Semaphore(1);

    public static TrafficLight trafficLight1;
    public static TrafficLight trafficLight2;

    //METHODS
    public Intersection() {
        trafficLight1 = new TrafficLight();
        trafficLight2 = new TrafficLight();
    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(LIGHT_DURATION);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            if (trafficLight1.isGreen()) {
                trafficLight1.setRed();
                trafficLight2.setGreen();
            } else {
                trafficLight1.setGreen();
                trafficLight2.setRed();
            }
        }
    }

    //GETTERS & SETTERS
}
