package builders;

import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.stage.Stage;
import types.IClass;
import types.IPackage;

import java.util.ArrayList;

public class DivisionBuilder implements Comparable<DivisionBuilder> {
    Group root;
    double x0, x1, y0, y1, z0, z1;
    double packageHeight = 10;
    PhongMaterial divisionColor;
    IPackage bossPackage;
    IPackage myPackage;
    double cityWeightX, cityWeightY, cityWeightZ, cityWeightCoupling, cityWeightCohesion;
    double optimizedLocX, optimizedLocY;
    double rootHeight, rootWidth;
    double maxWmcOfClass=0, maxCouplingOfCLass=0, maxCohesionOfCLass=0;
    Stage primaryStage;
    String packageName = "";

    ArrayList<BuildingBuilder> buildingBuilders;

    public DivisionBuilder(Group root, IPackage bossPackage) {
        this.root = root;
        this.divisionColor = new PhongMaterial(Color.GREEN);
        this.bossPackage = bossPackage;
        this.packageName = bossPackage.getPackName();

        prepareBuildings();
    }
    public void setMyPackage(IPackage myPackage){
        this.myPackage = myPackage;

        for(BuildingBuilder buildingBuilder : buildingBuilders){
            IClass myClass = null;
            if(myPackage != null){
                if(myPackage.getClassMap().containsKey(buildingBuilder.getClassName())){
                    myClass = myPackage.getClassMap().get(buildingBuilder.getClassName());
                }
            }
            buildingBuilder.setMyClass(myClass);
        }
    }

    public String getPackageName() {
        return packageName;
    }
    public int numberOfOwnBuilding() {
        int cnt =0;
        for(BuildingBuilder buildingBuilder : buildingBuilders){
            cnt += buildingBuilder.hasOwnClass();
        }
        return cnt;
    }
    public int hasOwnPackage() {
        if (myPackage != null)
            return 1;
        return 0;
    }
    private void prepareBuildings() {
        buildingBuilders = new ArrayList<>();
        for (IClass iClass : bossPackage.getiClasses()) {
            BuildingBuilder buildingBuilder = new BuildingBuilder(root, iClass);
            buildingBuilders.add(buildingBuilder);
            maxWmcOfClass=Math.max(maxWmcOfClass,buildingBuilder.getWmc());
            maxCouplingOfCLass=Math.max(maxCouplingOfCLass,buildingBuilder.getCoupling());
            maxCohesionOfCLass=Math.max(maxCohesionOfCLass,buildingBuilder.getLackCohesion());
        }

        double varX = 0;
        double varY = 0;

        for (int i = 0; i < buildingBuilders.size(); i++) {
            BuildingBuilder buildingBuilder=buildingBuilders.get(i);
            if (i % 2 == 0) {
                buildingBuilder.setBoundaries(
                        varX, (double) varX + buildingBuilder.getLoc(),
                        0.0, (double) buildingBuilder.getLoc(),
                        -packageHeight);

                varX += buildingBuilder.getLoc();
                varY = Math.max(buildingBuilder.getLoc(), varY);
            } else {
                buildingBuilder.setBoundaries(
                        0.0, (double) buildingBuilder.getLoc(),
                        varY, (double) varY + buildingBuilder.getLoc(),
                        -packageHeight);

                varY += buildingBuilder.getLoc();
                varX = Math.max(varX, buildingBuilder.getLoc());
            }
        }
        setOptimizedLocX(varX);
        setOptimizedLocY(varY);
    }

    public void setBoundaries(double x0, double x1, double y0, double y1, double z0) {
        this.x0 = x0;
        this.x1 = x1;
        this.y0 = y0;
        this.y1 = y1;
        this.z0 = z0;
        this.z1 = z0 - packageHeight;
    }

    public double getOptimizedLocX() {
        return optimizedLocX;
    }
    public double getOptimizedLocY() {
        return optimizedLocY;
    }

    public double getMaxWmcOfClass() {
        return maxWmcOfClass;
    }

    public double getMaxCouplingOfCLass() {
        return maxCouplingOfCLass;
    }

    public double getMaxCohesionOfCLass() {
        return maxCohesionOfCLass;
    }

    public void setOptimizedLocX(double optimizedLocX) {
        this.optimizedLocX = optimizedLocX;
    }
    public void setOptimizedLocY(double optimizedLocY) {
        this.optimizedLocY = optimizedLocY;
    }

    public void setCityWeight(double cityWeightX, double cityWeightY, double cityWeightZ) {
        this.cityWeightX = cityWeightX;
        this.cityWeightY = cityWeightY;
        this.cityWeightZ = cityWeightZ;

        for(BuildingBuilder buildingBuilder : buildingBuilders){
            buildingBuilder.setCityWeight(cityWeightX, cityWeightY, cityWeightZ);
        }
    }

    public void setCityWeightCoupling(double cityWeightCoupling) {
        this.cityWeightCoupling = cityWeightCoupling;
        for(BuildingBuilder buildingBuilder : buildingBuilders){
            buildingBuilder.setCityWeightCoupling(cityWeightCoupling);
        }
    }

    public void setCityWeightCohesion(double cityWeightCohesion) {
        this.cityWeightCohesion = cityWeightCohesion;
        for(BuildingBuilder buildingBuilder : buildingBuilders){
            buildingBuilder.setCityWeightCohesion(cityWeightCohesion);
        }
    }

    public void setRootSize(double rootWidth, double rootHeight) {
        this.rootHeight = rootHeight;
        this.rootWidth = rootWidth;
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage= primaryStage;
        for(BuildingBuilder buildingBuilder : buildingBuilders){
            buildingBuilder.setPrimaryStage(primaryStage);
        }
    }

    public void build(double extraX, double extraY) {
        if(myPackage == null)
            return;

        Box box = new Box((x1 - x0) * cityWeightX, (y1 - y0) * cityWeightY, packageHeight);
        box.setTranslateX(extraX + (x1 + x0) / 2.0 * cityWeightX);
        box.setTranslateY(extraY + (y1 + y0) / 2.0 * cityWeightY);
        box.setTranslateZ(z0 - packageHeight/2.0);
        box.setMaterial(divisionColor);

        box.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                //banner.setMaterial(buildingColor);
                //root.getSc
                System.out.println(bossPackage.getPackName());
            }
        });

        root.getChildren().add(box);

        for(BuildingBuilder buildingBuilder : buildingBuilders){
            buildingBuilder.build(-rootWidth/2+x0*cityWeightX,-rootHeight/2+y0*cityWeightY);
        }
    }

    @Override
    public int compareTo(DivisionBuilder o) {
        if(this.getMaxWmcOfClass() > o.getMaxWmcOfClass()){
            return -1;
        }
        else
            return 1;
    }
}
