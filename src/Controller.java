import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Controller {
    private final Simulation simulation;
    private final View view;
    private ScheduledExecutorService scheduler;

    public Controller(Simulation simulation, View view) {
        this.simulation = simulation;
        this.view = view;
    }

    public void init() {
        scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleAtFixedRate(this::updateView, 0, 50, TimeUnit.MILLISECONDS);
    }

    public void updateView() {
        /* 
        TrafficLight verticalLight = simulation.intersection.getVerticalLight();
        TrafficLight horizontalLight = simulation.intersection.getHorizontalLight();

        view.updateCarPositions(verticalLight, true);  
        view.updateCarPositions(horizontalLight, false); 

        simulation.getCars().stream()
            .filter(Car::hasPassed)
            .forEach(view::removeCarView);
        */
    }

    public void startSimulation() {
        //simulation.start();
    }

    public void stopSimulation() {
        //simulation.stop();
        if (scheduler != null && !scheduler.isShutdown()) {
            scheduler.shutdown();
        }
    }
}
