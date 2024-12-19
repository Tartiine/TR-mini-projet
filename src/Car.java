class Car extends Thread {
    private final TrafficLight assignedLight;
    private final Intersection intersection;
    private boolean passed = false;
    
    public Car(Intersection intersection, TrafficLight light) {
        this.intersection = intersection;
        this.assignedLight = light;
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
                    assignedLight.removeCar();

                    Thread.sleep(500); 
                    
                    passed = true;
                } finally {
                    intersection.releaseIntersection();
                }
            }
        } catch (InterruptedException e) {
        }
    }
    
    public boolean hasPassed() {
        return passed;
    }
}