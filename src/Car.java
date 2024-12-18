class Car implements Runnable {
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
            assignedLight.waitForGreen();

            intersection.acquireIntersection();
            try {
                assignedLight.removeCar();

                Thread.sleep(500); 
                
                passed = true;
            } finally {
                intersection.releaseIntersection();
            }
        } catch (InterruptedException e) {
        }
    }
    
    public boolean hasPassed() {
        return passed;
    }
}