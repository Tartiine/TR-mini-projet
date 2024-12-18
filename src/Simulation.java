public class Simulation {
    //ATTRIBUTES
    public static Intersection intersection = new Intersection();

    //METHODS
    public static void main(String[] args) {
        TrafficLight tl1 = intersection.getVerticalLight();
        TrafficLight tl2 = intersection.getHorizontalLight();

        CarGenerator cg1 = new CarGenerator(intersection, tl1);
        CarGenerator cg2 = new CarGenerator(intersection, tl2);

        intersection.start();
        cg1.start();
        cg2.start();
    }
}
