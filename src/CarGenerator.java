public class CarGenerator extends Thread {
    private static final int RANDOM_MULTIPLIER = 10000;

    private final Intersection intersection;
    private final TrafficLight trafficLight;

    public CarGenerator(Intersection intersection, TrafficLight trafficLight) {
        this.intersection = intersection;
        this.trafficLight = trafficLight;
    }

    @Override
    public void run() {
        while (true) {
            Car car = new Car(intersection, trafficLight);

            Thread carThread = new Thread(car);
            carThread.start();

            try {
                Thread.sleep((long)(Math.random()*RANDOM_MULTIPLIER));
            } catch (InterruptedException e) {}
        }
    }
}
