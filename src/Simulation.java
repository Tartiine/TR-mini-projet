public class Simulation {
    //ATTRIBUTES

    //METHODS
    public static void main(String[] args) {
        Intersection intersection = new Intersection(2000);

        Thread intersectionThread = new Thread(intersection);
        intersectionThread.start();

        for (int i = 0; i < 5; i++) {
            Car c = new Car(intersection, intersection.getVerticalLight());
            Thread carThread = new Thread(c, "Car-V-" + i);
            carThread.start();

            try {
                Thread.sleep((long)(Math.random()*3000));
            } catch (InterruptedException e) {}
        }

        for (int i = 0; i < 5; i++) {
            Car c = new Car(intersection, intersection.getHorizontalLight());
            Thread carThread = new Thread(c, "Car-H-" + i);
            carThread.start();

            try {
                Thread.sleep((long)(Math.random()*3000));
            } catch (InterruptedException e) {}
        }

    }



}
