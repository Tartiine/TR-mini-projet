import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Simulation extends Application {
    // ATTRIBUTES
    public static Intersection intersection = new Intersection();
    private static final double TILE_SIZE = 64;
    private Map<Car, ImageView> carMap = new HashMap<>();
    private Pane root;
    private ImageView verticalRedLightView;
    private ImageView verticalGreenLightView;
    private ImageView horizontalRedLightView;
    private ImageView horizontalGreenLightView;
    private ScheduledExecutorService scheduler;

    // METHODS
    @Override
    public void start(Stage primaryStage) {
        TrafficLight verticalTrafficLight = intersection.getVerticalLight();
        TrafficLight horizontalTrafficLight = intersection.getHorizontalLight();

        CarGenerator verticalCarGenerator = new CarGenerator(intersection, verticalTrafficLight, Orientation.VERTICAL);
        CarGenerator horizontalCarGenerator = new CarGenerator(intersection, horizontalTrafficLight, Orientation.HORIZONTAL);

        setupUI(primaryStage);

        intersection.start();
        verticalCarGenerator.start();
        horizontalCarGenerator.start();

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
        verticalGreenLightView.setRotate(90);
        verticalGreenLightView.setVisible(false);
        root.getChildren().add(verticalGreenLightView);

        horizontalRedLightView = new ImageView(redLightImg);
        horizontalRedLightView.setX((5 * TILE_SIZE) + TILE_SIZE);
        horizontalRedLightView.setY((5 * TILE_SIZE) + TILE_SIZE);
        root.getChildren().add(horizontalRedLightView);

        horizontalGreenLightView = new ImageView(greenLightImg);
        horizontalGreenLightView.setX((5 * TILE_SIZE) + TILE_SIZE);
        horizontalGreenLightView.setY((5 * TILE_SIZE) + TILE_SIZE);
        horizontalGreenLightView.setVisible(false);
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
        TrafficLight verticalLight = intersection.getVerticalLight();
        TrafficLight horizontalLight = intersection.getHorizontalLight();
        boolean verticalGreen = verticalLight.isGreen();
        boolean horizontalGreen = horizontalLight.isGreen();

        updateTrafficLights(verticalGreen, horizontalGreen);

        addCarsToView(verticalLight);
        addCarsToView(horizontalLight);
    }


    private void addCarsToView(TrafficLight trafficLight) {
        int position = 0;
        for (Car car : trafficLight.getAllCars()) {
            if (!carMap.containsKey(car)) {
                addCar(car, position);
            }
            position++;
        }
    }
    

    public void updateTrafficLights(boolean verticalGreen, boolean horizontalGreen) {
        Platform.runLater(() -> {
            verticalGreenLightView.setVisible(verticalGreen);
            verticalRedLightView.setVisible(!verticalGreen);

            horizontalGreenLightView.setVisible(horizontalGreen);
            horizontalRedLightView.setVisible(!horizontalGreen);
        });
    }

    public void addCar(Car car, int queuePosition) {
        Image carImage = new Image("file:res/car.png");
        ImageView carView = new ImageView(carImage);
        carMap.put(car, carView);
    
        double startX;
        double startY;
    
        if (car.isVertical()) {
            startX = 6* TILE_SIZE; 
            startY = 11*TILE_SIZE;   
        } else {
            startX = -TILE_SIZE;   
            startY = 5 * TILE_SIZE;
            carView.setRotate(90); 
        }

        double targetX = car.isVertical() ? 5 * TILE_SIZE : (4 - queuePosition) * TILE_SIZE;
        double targetY = car.isVertical() ? (5 + queuePosition) * TILE_SIZE : 5 * TILE_SIZE;
    
        carView.setX(startX);
        carView.setY(startY);
    
        car.isCrossingProperty().addListener((obs, wasCrossing, isNowCrossing) -> {
            if (isNowCrossing) {
                animateCarCrossing(carView, car.isVertical());
            }
        });
    
        Platform.runLater(() -> {
            root.getChildren().add(carView);
            //animateCarToQueue(carView, targetX, targetY);
        });
    }

    private void animateCarCrossing(ImageView carView, boolean isVertical) {
        TranslateTransition transition = new TranslateTransition(Duration.millis(1000), carView);
        if (isVertical) {
            transition.setByY(TILE_SIZE);
        } else {
            transition.setByX(11*TILE_SIZE);
        }

        transition.setOnFinished(event -> Platform.runLater(() -> root.getChildren().remove(carView)));

        transition.play();
    }

    private void animateCarToQueue(ImageView carView, double targetX, double targetY) {
        double currentX = carView.getX();
        double currentY = carView.getY();
        TranslateTransition transition = new TranslateTransition(Duration.millis(500), carView);
        transition.setByX(targetX - currentX);
        transition.setByY(targetY - currentY);
    
        transition.setOnFinished(event -> {
            carView.setX(targetX);
            carView.setY(targetY);
        });
    
        transition.play();
    }
    

    public void removeCar(int carId) {
        if (carId >= 0 && carId < carMap.size()) {
            ImageView carView = carMap.remove(carId);
            if (carView != null) {
                Platform.runLater(() -> root.getChildren().remove(carView));
            }
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
