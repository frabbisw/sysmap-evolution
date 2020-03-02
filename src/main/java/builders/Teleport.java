package builders;

import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.stage.Stage;

import java.util.ArrayList;

public class Teleport {
    Group root;
    PhongMaterial baseColor;
    PhongMaterial evolColor;

    double baseHeight = 10;
//    double baseWidth = 10;
    Stage primaryStage;
    int numberOfVersions;
    TeleportCallBack teleportCallBack;
    EvolutionCallBack evolutionCallBack;
    ArrayList<String >versionsNames;


    public Teleport(Group root, Stage primaryStage, int numberOfVersions, TeleportCallBack teleportCallBack, EvolutionCallBack evolutionCallBack){
        this.root = root;
        this.primaryStage = primaryStage;
        this.numberOfVersions = numberOfVersions;
        this.teleportCallBack = teleportCallBack;
        this.evolutionCallBack = evolutionCallBack;

        this.baseColor=new PhongMaterial(Color.BLUE);
        this.evolColor=new PhongMaterial(Color.RED);
    }

    public void setVersionsNames(ArrayList<String> versionsNames) {
        this.versionsNames = versionsNames;
    }

    public void build(int x0, int y0, int z0, int size) {
        int v=0;
        for(v = 0; v<numberOfVersions; v++){
            Box box = new Box(size, size, baseHeight);

            box.setTranslateX(x0+size);
            box.setTranslateY(y0-size/2 - v * size * 1.5);
            box.setTranslateZ(z0+baseHeight/2);

            box.setMaterial(baseColor);

            root.getChildren().add(box);

            int finalV = v;
            box.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    teleportCallBack.action(finalV);
                }
            });
        }
        Box box = new Box(size, size, baseHeight);

        box.setTranslateX(x0+size);
        box.setTranslateY(y0-size/2 - v * size * 1.5);
        box.setTranslateZ(z0+baseHeight/2);

        box.setMaterial(evolColor);

        root.getChildren().add(box);

        int finalV = v;
        box.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                evolutionCallBack.action();
            }
        });
    }
}
