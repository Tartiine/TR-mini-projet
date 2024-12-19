import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.Map;

public class View extends Application {
    private static final double TILE_SIZE = 64;
    private final Map<Integer, ImageView> carViews = new HashMap<>();
    private Pane root;
    private ImageView verticalRedLightView;
    private ImageView verticalGreenLightView;
    private ImageView horizontalRedLightView;
    private ImageView horizontalGreenLightView;

    @Override
    public void start(Stage primaryStage) {
        Pane root = new Pane();
        Image groundImg = new Image("file:res/ground.jpg");
        Image roadImg = new Image("file:res/road.jpg");
        Image roadCornerImg = new Image("file:res/road2.jpg");
        Image redLightImg = new Image("file:res/red.jpg");
        Image greenLightImg = new Image("file:res/green.jpg");

        int gridSize = 10;
        for (int y = 0; y < gridSize; y++) {
            for (int x = 0; x < gridSize; x++) {
                ImageView tile = new ImageView(groundImg);
                tile.setX(x * TILE_SIZE);
                tile.setY(y * TILE_SIZE);
                root.getChildren().add(tile);
            }
        }
        // Routes horizontales (rangées 4 et 5)
        for (int x = 0; x < gridSize; x++) {
            // rangée 4
            ImageView roadH1 = new ImageView(roadImg);
            roadH1.setX(x * TILE_SIZE);
            roadH1.setY(4 * TILE_SIZE);
            roadH1.setRotate(90);
            root.getChildren().add(roadH1);
            // rangée 5
            ImageView roadH2 = new ImageView(roadImg);
            roadH2.setX(x * TILE_SIZE);
            roadH2.setY(5 * TILE_SIZE);
            roadH2.setRotate(270);
            root.getChildren().add(roadH2);
        }
        // Routes verticales (colonnes 4 et 5)
        for (int y = 0; y < gridSize; y++) {
            // colonne 4
            ImageView roadV1 = new ImageView(roadImg);
            roadV1.setX(4 * TILE_SIZE);
            roadV1.setY(y * TILE_SIZE);
            root.getChildren().add(roadV1);
            // colonne 5
            ImageView roadV2 = new ImageView(roadImg);
            roadV2.setRotate(180);
            roadV2.setX(5 * TILE_SIZE);
            roadV2.setY(y * TILE_SIZE);
            root.getChildren().add(roadV2);
        }
        // Coin haut-gauche de l'intersection
        ImageView cornerTopLeft = new ImageView(roadCornerImg);
        cornerTopLeft.setX(4 * TILE_SIZE);
        cornerTopLeft.setY(4 * TILE_SIZE);
        root.getChildren().add(cornerTopLeft);
        // Coin haut-droit
        ImageView cornerTopRight = new ImageView(roadCornerImg);
        cornerTopRight.setX(5 * TILE_SIZE);
        cornerTopRight.setY(4 * TILE_SIZE);
        cornerTopRight.setRotate(90);
        root.getChildren().add(cornerTopRight);
        // Coin bas-gauche
        ImageView cornerBottomLeft = new ImageView(roadCornerImg);
        cornerBottomLeft.setX(4 * TILE_SIZE);
        cornerBottomLeft.setY(5 * TILE_SIZE);
        cornerBottomLeft.setRotate(270);
        root.getChildren().add(cornerBottomLeft);
        // Coin bas-droit
        ImageView cornerBottomRight = new ImageView(roadCornerImg);
        cornerBottomRight.setX(5 * TILE_SIZE);
        cornerBottomRight.setY(5 * TILE_SIZE);
        cornerBottomRight.setRotate(180);
        root.getChildren().add(cornerBottomRight);
        ImageView redLightView = new ImageView(redLightImg);
        redLightView.setX((4 * TILE_SIZE) - TILE_SIZE);
        redLightView.setY((5 * TILE_SIZE) + (TILE_SIZE));
        redLightView.setRotate(90);
        root.getChildren().add(redLightView);
        ImageView greenLightView = new ImageView(greenLightImg);
        greenLightView.setX((5 * TILE_SIZE) + TILE_SIZE);
        greenLightView.setY((5 * TILE_SIZE) + TILE_SIZE);
        root.getChildren().add(greenLightView);

        Scene scene = new Scene(root, TILE_SIZE * gridSize, TILE_SIZE * gridSize);
        primaryStage.setTitle("Intersection Simulation");
        primaryStage.setScene(scene);
        primaryStage.show();
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
}
