import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class View extends Application {
    
    @Override
    public void start(Stage primaryStage) {
        Pane root = new Pane();

        Image groundImg = new Image("file:res/ground.jpg");
        Image roadImg = new Image("file:res/road.jpg");
        Image roadCornerImg = new Image("file:res/road2.jpg");
        Image redLightImg = new Image("file:res/red.jpg");
        Image greenLightImg = new Image("file:res/green.jpg");
        
        double tileSize = 64;
        int gridSize = 10; 

        for (int y = 0; y < gridSize; y++) {
            for (int x = 0; x < gridSize; x++) {
                ImageView tile = new ImageView(groundImg);
                tile.setX(x * tileSize);
                tile.setY(y * tileSize);
                root.getChildren().add(tile);
            }
        }

        // Routes horizontales (rangées 4 et 5)
        for (int x = 0; x < gridSize; x++) {
            // rangée 4
            ImageView roadH1 = new ImageView(roadImg);
            roadH1.setX(x * tileSize);
            roadH1.setY(4 * tileSize);
            roadH1.setRotate(90);
            root.getChildren().add(roadH1);

            // rangée 5
            ImageView roadH2 = new ImageView(roadImg);
            roadH2.setX(x * tileSize);
            roadH2.setY(5 * tileSize);
            roadH2.setRotate(270);
            root.getChildren().add(roadH2);
        }

        // Routes verticales (colonnes 4 et 5)
        for (int y = 0; y < gridSize; y++) {
            // colonne 4
            ImageView roadV1 = new ImageView(roadImg);
            roadV1.setX(4 * tileSize);
            roadV1.setY(y * tileSize);
            root.getChildren().add(roadV1);

            // colonne 5
            ImageView roadV2 = new ImageView(roadImg);
            roadV2.setRotate(180);
            roadV2.setX(5 * tileSize);
            roadV2.setY(y * tileSize);
            root.getChildren().add(roadV2);
        }

        // Coin haut-gauche de l'intersection
        ImageView cornerTopLeft = new ImageView(roadCornerImg);
        cornerTopLeft.setX(4 * tileSize);
        cornerTopLeft.setY(4 * tileSize);
        root.getChildren().add(cornerTopLeft);

        // Coin haut-droit
        ImageView cornerTopRight = new ImageView(roadCornerImg);
        cornerTopRight.setX(5 * tileSize);
        cornerTopRight.setY(4 * tileSize);
        cornerTopRight.setRotate(90);
        root.getChildren().add(cornerTopRight);

        // Coin bas-gauche
        ImageView cornerBottomLeft = new ImageView(roadCornerImg);
        cornerBottomLeft.setX(4 * tileSize);
        cornerBottomLeft.setY(5 * tileSize);
        cornerBottomLeft.setRotate(270);
        root.getChildren().add(cornerBottomLeft);

        // Coin bas-droit
        ImageView cornerBottomRight = new ImageView(roadCornerImg);
        cornerBottomRight.setX(5 * tileSize);
        cornerBottomRight.setY(5 * tileSize);
        cornerBottomRight.setRotate(180);
        root.getChildren().add(cornerBottomRight);


        ImageView redLightView = new ImageView(redLightImg);
        redLightView.setX((4 * tileSize) - tileSize); 
        redLightView.setY((5 * tileSize) + (tileSize));
        redLightView.setRotate(90);
        root.getChildren().add(redLightView);

        ImageView greenLightView = new ImageView(greenLightImg);
        greenLightView.setX((5 * tileSize) + tileSize);
        greenLightView.setY((5 * tileSize) + tileSize);
        root.getChildren().add(greenLightView);

        Scene scene = new Scene(root, tileSize * gridSize, tileSize * gridSize);
        primaryStage.setTitle("Intersection");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
