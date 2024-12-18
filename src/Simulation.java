public class Simulation {
    //ATTRIBUTES
    public static Intersection intersection = new Intersection(2000);

    //METHODS
    public static void main(String[] args) {
        Thread intersectionThread = new Thread(intersection);
        intersectionThread.start();

        for (int i = 0; i < 10; i++) {
            if (i % 2 == 0) {
                TrafficLight light = intersection.getVerticalLight(); // Assign vertical light
                Car car = new Car(intersection, light);

                // Add car to the light's waiting list
                light.addCar(car);

                Thread carThread = new Thread(car, "Car-V-" + i);
                carThread.start();
            } else {
                TrafficLight light = intersection.getHorizontalLight(); // Assign horizontal light
                Car car = new Car(intersection, light);

                // Add car to the light's waiting list
                light.addCar(car);

                Thread carThread = new Thread(car, "Car-H-" + i);
                carThread.start();
            }

            try {
                Thread.sleep((long)(Math.random()*3000));
            } catch (InterruptedException e) {}
        }
    }
}
