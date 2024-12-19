import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Simulation extends Application {
    // ATTRIBUTES
    public static Intersection intersection = new Intersection();
    private static final double TILE_SIZE = 64;
    private final Map<Integer, ImageView> carViews = new HashMap<>();
    private Pane root;
    private ImageView verticalRedLightView;
    private ImageView verticalGreenLightView;
    private ImageView horizontalRedLightView;
    private ImageView horizontalGreenLightView;
    private ScheduledExecutorService scheduler;

    // METHODS
    @Override
    public void start(Stage primaryStage) {
        TrafficLight tl1 = intersection.getVerticalLight();
        TrafficLight tl2 = intersection.getHorizontalLight();

        CarGenerator cg1 = new CarGenerator(intersection, tl1);
        CarGenerator cg2 = new CarGenerator(intersection, tl2);

        setupUI(primaryStage);

        intersection.start();
        cg1.start();
        cg2.start();

        initScheduler();
    }

    private void setupUI(Stage primaryStage) {
        root = new Pane();
        Image groundImg = new Image("file:res/ground.jpg");
        Image roadImg = new Image("file:res/road.jpg");
        Image roadCornerImg = new Image("file:res/road2.jpg");
        Image redLightImg = new Image("file:res/red.jpg");
        Image greenLightImg = new Image("file:res/green.jpg");

        int gridSize = 10;

        // Ground tiles
        for (int y = 0; y < gridSize; y++) {
            for (int x = 0; x < gridSize; x++) {
                ImageView tile = new ImageView(groundImg);
                tile.setX(x * TILE_SIZE);
                tile.setY(y * TILE_SIZE);
                root.getChildren().add(tile);
            }
        }

        // Road tiles
        addRoadTiles(gridSize, roadImg, roadCornerImg);

        // Traffic lights
        verticalRedLightView = new ImageView(redLightImg);
        verticalRedLightView.setX((4 * TILE_SIZE) - TILE_SIZE);
        verticalRedLightView.setY((5 * TILE_SIZE) + TILE_SIZE);
        verticalRedLightView.setRotate(90);
        root.getChildren().add(verticalRedLightView);

        verticalGreenLightView = new ImageView(greenLightImg);
        verticalGreenLightView.setX((4 * TILE_SIZE) - TILE_SIZE);
        verticalGreenLightView.setY((5 * TILE_SIZE) + TILE_SIZE);
        verticalGreenLightView.setVisible(false); // Initially red
        root.getChildren().add(verticalGreenLightView);

        horizontalRedLightView = new ImageView(redLightImg);
        horizontalRedLightView.setX((5 * TILE_SIZE) + TILE_SIZE);
        horizontalRedLightView.setY((5 * TILE_SIZE) + TILE_SIZE);
        root.getChildren().add(horizontalRedLightView);

        horizontalGreenLightView = new ImageView(greenLightImg);
        horizontalGreenLightView.setX((5 * TILE_SIZE) + TILE_SIZE);
        horizontalGreenLightView.setY((5 * TILE_SIZE) + TILE_SIZE);
        horizontalGreenLightView.setVisible(false); // Initially red
        root.getChildren().add(horizontalGreenLightView);

        Scene scene = new Scene(root, TILE_SIZE * gridSize, TILE_SIZE * gridSize);
        primaryStage.setTitle("Intersection Simulation");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void addRoadTiles(int gridSize, Image roadImg, Image roadCornerImg) {
        for (int x = 0; x < gridSize; x++) {
            ImageView roadH1 = new ImageView(roadImg);
            roadH1.setX(x * TILE_SIZE);
            roadH1.setY(4 * TILE_SIZE);
            roadH1.setRotate(90);
            root.getChildren().add(roadH1);

            ImageView roadH2 = new ImageView(roadImg);
            roadH2.setX(x * TILE_SIZE);
            roadH2.setY(5 * TILE_SIZE);
            roadH2.setRotate(270);
            root.getChildren().add(roadH2);
        }

        for (int y = 0; y < gridSize; y++) {
            ImageView roadV1 = new ImageView(roadImg);
            roadV1.setX(4 * TILE_SIZE);
            roadV1.setY(y * TILE_SIZE);
            root.getChildren().add(roadV1);

            ImageView roadV2 = new ImageView(roadImg);
            roadV2.setX(5 * TILE_SIZE);
            roadV2.setY(y * TILE_SIZE);
            roadV2.setRotate(180);
            root.getChildren().add(roadV2);
        }

        addCorners(roadCornerImg);
    }

    private void addCorners(Image roadCornerImg) {
        ImageView cornerTopLeft = new ImageView(roadCornerImg);
        cornerTopLeft.setX(4 * TILE_SIZE);
        cornerTopLeft.setY(4 * TILE_SIZE);
        root.getChildren().add(cornerTopLeft);

        ImageView cornerTopRight = new ImageView(roadCornerImg);
        cornerTopRight.setX(5 * TILE_SIZE);
        cornerTopRight.setY(4 * TILE_SIZE);
        cornerTopRight.setRotate(90);
        root.getChildren().add(cornerTopRight);

        ImageView cornerBottomLeft = new ImageView(roadCornerImg);
        cornerBottomLeft.setX(4 * TILE_SIZE);
        cornerBottomLeft.setY(5 * TILE_SIZE);
        cornerBottomLeft.setRotate(270);
        root.getChildren().add(cornerBottomLeft);

        ImageView cornerBottomRight = new ImageView(roadCornerImg);
        cornerBottomRight.setX(5 * TILE_SIZE);
        cornerBottomRight.setY(5 * TILE_SIZE);
        cornerBottomRight.setRotate(180);
        root.getChildren().add(cornerBottomRight);
    }

    private void initScheduler() {
        scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleAtFixedRate(this::updateView, 0, 50, TimeUnit.MILLISECONDS);
    }

    private void updateView() {
        boolean verticalGreen = intersection.getVerticalLight().isGreen();
        boolean horizontalGreen = intersection.getHorizontalLight().isGreen();
        updateTrafficLights(verticalGreen, horizontalGreen);

        intersection.getVerticalLight().getCars().forEach(car -> {
            double newX = car.getPosition() * TILE_SIZE; 
            double newY = car.getLane() * TILE_SIZE; 
            updateCarPosition(car.getId(), newX, newY);
        });
    
        intersection.getHorizontalLight().getCars().forEach(car -> {
            double newX = car.getLane() * TILE_SIZE; 
            double newY = car.getPosition() * TILE_SIZE; 
            updateCarPosition(car.getId(), newX, newY);
        });
    
        synchronized (intersection) {
            if (!intersection.isOccupied()) {
                if (verticalGreen) {
                    moveCar(intersection.getVerticalLight());
                }
                if (horizontalGreen) {
                    moveCar(intersection.getHorizontalLight());
                }
            }
        }
    
        intersection.getCars().stream()
                .filter(Car::hasPassed)
                .forEach(car -> removeCar(car.getId()));
    }
    

    public void updateTrafficLights(boolean verticalGreen, boolean horizontalGreen) {
        Platform.runLater(() -> {
            verticalGreenLightView.setVisible(verticalGreen);
            verticalRedLightView.setVisible(!verticalGreen);

            horizontalGreenLightView.setVisible(horizontalGreen);
            horizontalRedLightView.setVisible(!horizontalGreen);
        });
    }

    public void addCar(int carId) {
        Image carImage = new Image("file:res/car.png");
        ImageView carView = new ImageView(carImage);
        carViews.put(carId, carView);
        Platform.runLater(() -> root.getChildren().add(carView));
    }

    public void updateCarPosition(int carId, double x, double y) {
        ImageView carView = carViews.get(carId);
        if (carView != null) {
            Platform.runLater(() -> {
                carView.setX(x);
                carView.setY(y);
            });
        }
    }

    public void removeCar(int carId) {
        ImageView carView = carViews.remove(carId);
        if (carView != null) {
            Platform.runLater(() -> root.getChildren().remove(carView));
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
