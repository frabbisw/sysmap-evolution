package builders;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.stage.Stage;
import libs.Toast;

import java.util.ArrayList;

public class BaseBuilder {
    Group root;
    PhongMaterial baseColor;
    String printingInfo;
    Box box;
    double baseHeight = 10;
    Stage primaryStage;

    public BaseBuilder(Group root, Stage primaryStage){
        this.root=root;
        this.primaryStage=primaryStage;
        this.baseColor=new PhongMaterial(Color.GRAY);
    }
    public void build(double x0, double x1, double y0, double y1, double z0, double z1){
        box = new Box(x1-x0,y1-y0, baseHeight);

        box.setTranslateX((x1+x0)/2);
        box.setTranslateY((x1+x0)/2);
        box.setTranslateZ(z0+baseHeight/2);

        box.setMaterial(baseColor);

        root.getChildren().add(box);
    }
    public void setPrintInfo(String printingInfo){
        this.printingInfo = printingInfo;

        box.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                Toast.makeText(primaryStage, printingInfo,5000, 100, 100);
            }
        });
    }
}
