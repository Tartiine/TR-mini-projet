public class CarGenerator extends Thread {
    private static final int RANDOM_MULTIPLIER = 10000;
    private static final int MAX_CAR = 4;

    private final Intersection intersection;
    private final TrafficLight trafficLight;
    private final Orientation orientation;

    public CarGenerator(Intersection intersection, TrafficLight trafficLight, Orientation orientation) {
        this.intersection = intersection;
        this.trafficLight = trafficLight;
        this.orientation = orientation;
    }

    @Override
    public void run() {
        while (true) {
            if (trafficLight.getAllCars().size() <= MAX_CAR) {
                Car car = new Car(intersection, trafficLight, orientation);

                Thread carThread = new Thread(car);
                carThread.start();

                try {
                    Thread.sleep((long)(Math.random()*RANDOM_MULTIPLIER));
                } catch (InterruptedException e) {}
            }
        }
    }
}
