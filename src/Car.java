import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

enum Orientation {
    VERTICAL, HORIZONTAL
}

class Car extends Thread {
    private final TrafficLight assignedLight;
    private final Intersection intersection;
    private final Orientation orientation;
    private BooleanProperty isCrossing = new SimpleBooleanProperty(false);
    
    public Car(Intersection intersection, TrafficLight light, Orientation orientation) {
        this.intersection = intersection;
        this.assignedLight = light;
        this.orientation = orientation;
        assignedLight.addCar(this);
    }
    
    @Override
    public void run() {
        try {
            if (assignedLight.tryCross(this)) {
                assignedLight.waitForGreen();

                intersection.acquireIntersection();
                try {
                    System.out.println("Car is crossing");
                    setIsCrossing(true);
                    assignedLight.removeCar();

                    Thread.sleep(500); 
                    
                } finally {
                    intersection.releaseIntersection();
                    setIsCrossing(false);
                }
            }
        } catch (InterruptedException e) {
        }
    }
    
    public BooleanProperty isCrossingProperty() {
        return isCrossing;
    }

    public boolean isCrossing() {
        return isCrossing.get();
    }

    private void setIsCrossing(boolean value) {
        isCrossing.set(value);
    }

    public TrafficLight getTrafficLight() {
        return assignedLight;
    }

    public Orientation getOrientation() {
        return orientation;
    }

    public boolean isVertical() {
        return orientation == Orientation.VERTICAL;
    }

}