package builders;

import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.stage.Stage;
import libs.Toast;
import types.IClass;
//import java.awt.*;

public class BuildingBuilder {
    Group root;
    IClass bossClass;
    IClass myClass;
    PhongMaterial buildingColor;
    double x0, x1, y0, y1, z0, z1;
    double cityWeightX, cityWeightY, cityWeightZ, cityWeightCoupling, cityWeightCohesion;
    double loc=1, wmc=1;
    double coupling=1, lackCohesion=1;
    Stage primaryStage;
    String className;

    public BuildingBuilder(Group root, IClass bossClass) {
        this.root = root;
        this.bossClass = bossClass;
        this.className = bossClass.getClassName();
        this.buildingColor=new PhongMaterial(Color.RED);

        this.loc= bossClass.getLoc();
        this.wmc=Math.max(bossClass.getWmc(),1.0);

        this.coupling= bossClass.getLoggedCoupling();
        this.lackCohesion= bossClass.getLoggedLackCohesion();

    }
    public void setMyClass(IClass myClass){
        this.myClass = myClass;
    }
    public String getClassName() {
        return className;
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public void setBoundaries(double x0, double x1, double y0, double y1, double z0){
        this.x0=x0;
        this.x1=x1;
        this.y0=y0;
        this.y1=y1;
        this.z0=z0;
        this.z1=z0-wmc;
    }
    public int hasOwnClass() {
        if (myClass != null)
            return 1;
        return 0;
    }
    public double getLoc() {
        return loc;
    }

    public double getWmc() {
        return wmc;
    }

    public void setCityWeight(double cityWeightX, double cityWeightY, double cityWeightZ) {
        this.cityWeightX = cityWeightX;
        this.cityWeightY = cityWeightY;
        this.cityWeightZ = cityWeightZ;
    }

    public void setCityWeightCoupling(double cityWeightCoupling) {
        this.cityWeightCoupling = cityWeightCoupling;
        if(myClass == null)
            return;

        try{
            buildingColor = new PhongMaterial(Color.color(myClass.getLoggedCoupling()*cityWeightCoupling,1,1-myClass.getLoggedCoupling()*cityWeightCoupling));
        }
        catch (Exception e){
            buildingColor = new PhongMaterial(Color.color(0,0,0));
        }
    }

    public void setCityWeightCohesion(double cityWeightCohesion) {
        this.cityWeightCohesion = cityWeightCohesion;
    }

    public double getCoupling() {
        return coupling;
    }

    public double getLackCohesion() {
        return lackCohesion;
    }

    public void build(double extraX, double extraY) {
        if(myClass == null)
            return;

        Box box = new Box(myClass.getLoc() * cityWeightX * .9, myClass.getLoc() * cityWeightY * .9, myClass.getWmc()*cityWeightZ);
        box.setTranslateX(extraX + (x1 + x0) / 2.0 * cityWeightX);
        box.setTranslateY(extraY + (y1 + y0) / 2.0 * cityWeightY);
        box.setTranslateZ(z0 - (myClass.getWmc()*cityWeightZ)/2.0);
        box.setMaterial(buildingColor);

        box.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                String msg = getClassMessage();
                Toast.makeText(primaryStage,msg, 5000, 100, 100);
                System.out.println(bossClass.getPackageName()+"."+ bossClass.getClassName());
            }
        });

        root.getChildren().add(box);
    }
    public String getClassMessage(){
        String msg = "";
        msg += ("Class Name: "+ bossClass.getClassName()+"\n");
        msg += ("Package Name: "+ bossClass.getPackageName()+"\n");
        msg += ("Number of Statements: "+(int) bossClass.getLoc()+"\n");
        msg += ("Comment Percentage: "+100*round(bossClass.getLoComments()/(bossClass.getLoComments()+ bossClass.getLoc()),4)+"%\n");
        msg += ("Coupling : "+(int) bossClass.getCoupling()+"\n");
        msg += ("Lack of Cohesion: "+(int) bossClass.getLackCohesion()+"\n");
        msg += ("Weighted method count: "+(int) bossClass.getWmc()+"\n");
        msg += ("Response of Class: "+(int) bossClass.getResponse()+"\n");
        msg += ("Number of Children: "+(int) bossClass.getChildren()+"\n");
        msg += ("Level of Inheritance: "+(int) bossClass.getInheritence()+"\n");
        return msg;
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }
}
