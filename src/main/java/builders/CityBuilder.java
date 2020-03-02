package builders;

import drawing_curves.DrawEvolution;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;
import support.MetricsTool;
import types.EvolutionData;
import types.IClass;
import types.IPackage;

import javax.swing.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;

public class CityBuilder extends Application {
    private double mouseOldX, mouseOldY = 0;
    private Rotate rotateX = new Rotate(0, Rotate.X_AXIS);
    private Rotate rotateY = new Rotate(0, Rotate.Y_AXIS);
    private Rotate rotateZ = new Rotate(0, Rotate.Z_AXIS);

    Group root;
//    PerspectiveCamera camera;
//    Scene scene;
    ArrayList<Map<String, IPackage>>allMaps;
    Map<String, IPackage>combinedMap;

    ArrayList <String> projectFilePaths;
    ArrayList <String>  projectFileNames;

    ArrayList <EvolutionData> evolutionDataOfVersions;
    String superPath;
    BaseBuilder baseBuilder;
    Teleport teleport;

    ArrayList<DivisionBuilder> divisionBuilders;
    double cityWeightX, cityWeightY, cityWeightZ, cityWeightCoupling, cityWeightCohesion;
    double couplingColorScale = 1.0;

    double width = 1000, height = 800, depth = 400;
    double frameWidth = width, frameHeight = height;
    double cameraX = -450, cameraY = 600, cameraZ = -700;
    //    double cameraX = -450, cameraY = 0, cameraZ = -700;
    double cameraView = 30, dcv = 5;
    double posX = 0, posY = 0, posZ = 0;
    double corX0, corX1, corY0, corY1, corZ0, corZ1;
    double cameraAngle = 70;
    double rootAngle = 45;
    double dr = 1, dx = 10, dy = 10, dz = 10, dc=1;

    static int runningVersion = 0;
    Stage primaryStage;

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;


        prepareFiles();
        prepareEvolutionGraph();

        prepareCoordinates();

        changeCity(0);
    }

    private void changeCity(int version) {
        runningVersion=version;

        prepareRoot();

        prepareBase();
        prepareTeleports();
        prepareDivisions(runningVersion);

        buildAll();
        prepareScene();
    }

    public void drawingEvolution() {
        String [] pargs = superPath.split("/");
        DrawEvolution.draw(pargs[pargs.length-1], evolutionDataOfVersions);
    }

    private void prepareEvolutionGraph() {
        double sumHeightOfBuildings = 0;
        double sumWidthOfBuildings = 0;
        int classCount=0;

        for(IPackage iPackage : combinedMap.values()){
            for(IClass iClass : iPackage.getiClasses()){
                sumHeightOfBuildings += iClass.getWmc();
                sumWidthOfBuildings+= iClass.getLoc();
                classCount++;
            }
        }
        double avgHeightOfBuildings = sumHeightOfBuildings / classCount;
        double avgWidthOfBuildings = sumWidthOfBuildings / classCount;

        System.out.println(sumHeightOfBuildings +" "+ sumWidthOfBuildings +" "+ classCount);

        evolutionDataOfVersions = new ArrayList<>();
        projectFileNames = new ArrayList<>();
        for(int version = 0; version< projectFilePaths.size(); version++){

            Map<String, IPackage>toolMap = allMaps.get(version);
            double numberOfPlots = toolMap.values().size();
            double numberOfBuildings = 0;
            double numberOfskyscraper = 0;
            double numberOfHeavyBuildings = 0;
            double totalLoc = 0;
            double totalWmc = 0;

            for(IPackage iPackage : toolMap.values()){
                numberOfBuildings += iPackage.getiClasses().size();

                for(IClass iClass : iPackage.getiClasses()){
                    totalLoc += iClass.getLoc();
                    totalWmc += iClass.getWmc();

                    if(iClass.getWmc() > avgHeightOfBuildings*2){
                        numberOfskyscraper++;
                    }
                    if(iClass.getLoc() > avgWidthOfBuildings*2){
                        numberOfHeavyBuildings++;
                    }
                }
            }
            String nameWithVersion = projectFilePaths.get(version);
            String [] versionArgs = nameWithVersion.split("/");
            projectFileNames.add(versionArgs[versionArgs.length-1]);

            evolutionDataOfVersions.add(new EvolutionData((int)numberOfPlots, (int)numberOfBuildings, (int)numberOfskyscraper, (int)numberOfHeavyBuildings, versionArgs[versionArgs.length-1]));

            System.out.println(projectFileNames.get(runningVersion));
            System.out.println("Packages:       "+ (int)numberOfPlots);
            System.out.println("Classes:        "+ (int)numberOfBuildings);
            System.out.println("SkyScraper:     "+ (int)numberOfskyscraper);
            System.out.println("Heavy Building: "+ (int)numberOfHeavyBuildings);
            System.out.println("TLOC:           "+ (int)totalLoc);
            System.out.println("Total WMC:      "+ (int)totalWmc);
            System.out.println();
        }

        if(evolutionDataOfVersions == null){
            System.out.println("City Builder NULL");
        }
    }

    private void prepareFiles() {
        JFileChooser chooser = new JFileChooser();
        chooser.setCurrentDirectory(new File("/home/rabbi/Desktop/re/sysmap/dataset"));
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int returnVal = chooser.showOpenDialog(null);
        if(returnVal == JFileChooser.APPROVE_OPTION) {
            superPath = chooser.getSelectedFile().getAbsolutePath();
            File superFile = new File(superPath);

            projectFilePaths = new ArrayList<>();
            for(File file : superFile.listFiles()){
                if(file.isDirectory()){
                    projectFilePaths.add(file.getAbsolutePath());
                }
            }
            Collections.sort(projectFilePaths);

            allMaps=new ArrayList<>();
            combinedMap=new TreeMap<>();
            for(String fileName : projectFilePaths){
                System.out.println(fileName);
                MetricsTool tool = new MetricsTool(fileName);
                System.out.println(tool.getProjectName());

                for(IPackage iPackage : tool.getPackageMap().values()){
                    if(!combinedMap.containsKey(iPackage.getPackName())){
                        combinedMap.put(iPackage.getPackName(), new IPackage(iPackage.getPackName()));
                    }
                    IPackage mapPackage = combinedMap.get(iPackage.getPackName());
                    for(IClass iClass : iPackage.getiClasses()){
                        if(!mapPackage.getClassMap().containsKey(iClass.getClassName())){
                            mapPackage.addiClass(new IClass(iClass.getClassName(), iClass.getPackageName()));
                        }
                        IClass mapClass = mapPackage.getClassMap().get(iClass.getClassName());

                        mapClass.setWmc(Math.max(mapClass.getWmc(), iClass.getWmc()));
                        mapClass.setCoupling(Math.max(mapClass.getCoupling(), iClass.getCoupling()));
                        mapClass.setLoc(Math.max(mapClass.getLoc(), iClass.getLoc()));
                        mapClass.setLackCohesion(Math.max(mapClass.getLackCohesion(), iClass.getLackCohesion()));
                    }
                }

                allMaps.add(tool.getPackageMap());
            }
        }
    }

    private void prepareDivisions(int version) {
        divisionBuilders = new ArrayList<>();
        for (IPackage iPackage : combinedMap.values()) {
            IPackage myPackage = null;
            if(allMaps.get(version).containsKey(iPackage.getPackName())){
                myPackage = allMaps.get(version).get(iPackage.getPackName());
            }

            DivisionBuilder divisionBuilder = new DivisionBuilder(root, iPackage);
            divisionBuilder.setMyPackage(myPackage);
            divisionBuilder.setRootSize(width, height);
            divisionBuilder.setPrimaryStage(primaryStage);
            divisionBuilders.add(divisionBuilder);
        }

        double varX = 0;
        double varY = 0;
        double varZ = 0;
        double varCohesion = 0;
        double varCoupling = 0;

        for (int i = 0; i < divisionBuilders.size(); i++) {
            DivisionBuilder divisionBuilder = divisionBuilders.get(i);

            varZ = Math.max(divisionBuilder.getMaxWmcOfClass(), varZ);
            varCoupling = Math.max(divisionBuilder.getMaxCouplingOfCLass(), varCoupling);
            varCohesion = Math.max(divisionBuilder.getMaxCohesionOfCLass(), varCohesion);

            if (i % 2 == 0) {
                divisionBuilder.setBoundaries(
                        varX, (double) varX + divisionBuilder.getOptimizedLocX(),
                        0.0, (double) divisionBuilder.getOptimizedLocY(),
                        posZ);

                varX += divisionBuilder.getOptimizedLocX();
                varY = Math.max(divisionBuilder.getOptimizedLocY(), varY);
            } else {
                divisionBuilder.setBoundaries(
                        0.0, (double) divisionBuilder.getOptimizedLocX(),
                        varY, (double) varY + divisionBuilder.getOptimizedLocY(),
                        posZ);

                varY += divisionBuilder.getOptimizedLocY();
                varX = Math.max(varX, divisionBuilder.getOptimizedLocX());
            }
        }
        cityWeightX = width / varX;
        cityWeightY = height / varY;
        cityWeightZ = depth / varZ;
        cityWeightCoupling = couplingColorScale / varCoupling;
        cityWeightCohesion = couplingColorScale / varCohesion;

        for (int i = 0; i < divisionBuilders.size(); i++) {
            DivisionBuilder divisionBuilder = divisionBuilders.get(i);
            divisionBuilder.setCityWeight(cityWeightX, cityWeightY, cityWeightZ);
            divisionBuilder.setCityWeightCoupling(cityWeightCoupling);
            divisionBuilder.setCityWeightCohesion(cityWeightCohesion);

//            divisionBuilder.build(-width / 2, -height / 2);
        }

//        System.out.println(divisionBuilders.size());
//        System.out.println(varCoupling);
    }

    private void prepareRoot() {
        root = new Group();
        root.setRotationAxis(Rotate.Z_AXIS);
        root.setRotate(rootAngle);
    }

    private void prepareBase() {
        baseBuilder = new BaseBuilder(root, primaryStage);
    }

    private void prepareTeleports() {
        teleport = new Teleport(root, primaryStage, projectFilePaths.size(), new TeleportCallBack() {
            @Override
            public void action(int clickedVersion) {
                changeCity(clickedVersion);
            }
        }, new EvolutionCallBack() {
            @Override
            public void action() {
                drawingEvolution();
            }
        });

    }

    private void buildAll() {
        baseBuilder.build(corX0, corX1, corY0, corY1, corZ0, corZ1);
        baseBuilder.setPrintInfo(evolutionDataOfVersions.get(runningVersion).toString());

        teleport.build((int)corX1, (int)corY1, (int) corZ0, 50);

        int cntP=0, cntC=0;
        for (int i = 0; i < divisionBuilders.size(); i++) {
            DivisionBuilder divisionBuilder = divisionBuilders.get(i);
            divisionBuilder.build(-width / 2, -height / 2);

            cntP += divisionBuilder.hasOwnPackage();
            cntC += divisionBuilder.numberOfOwnBuilding();
        }
        System.out.println(cntP + " " + cntC);
    }
    private PerspectiveCamera prepareCamera() {
        PerspectiveCamera camera = new PerspectiveCamera(false);
        camera.setTranslateX(cameraX);
        camera.setTranslateY(cameraY);
        camera.setTranslateZ(cameraZ);

        camera.setRotationAxis(Rotate.X_AXIS);
        camera.setRotate(cameraAngle);

        camera.setFieldOfView(cameraView);

        return camera;
    }
    private void prepareScene() {
        primaryStage.setScene(new Scene(root, frameWidth, frameHeight, true));
        String [] sargs = projectFilePaths.get(runningVersion).split("/");
        primaryStage.setTitle("SYS MAP: "+projectFileNames.get(runningVersion));
        primaryStage.getScene().setCamera(prepareCamera());

        primaryStage.setResizable(false);
        primaryStage.show();

        primaryStage.getScene().setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode().equals(KeyCode.LEFT)) {
                    root.setRotate(rootAngle += dr);
                }
                else if (event.getCode().equals(KeyCode.RIGHT)) {
                    root.setRotate(rootAngle -= dr);
                }

                else if (event.getCode().equals(KeyCode.UP)) {
                    primaryStage.getScene().getCamera().setRotate(cameraAngle+=dc);
                }
                else if (event.getCode().equals(KeyCode.DOWN)) {
                    primaryStage.getScene().getCamera().setRotate(cameraAngle-=dc);
                }

                else if (event.getCode().equals(KeyCode.A)) {
                    primaryStage.getScene().getCamera().setTranslateX(cameraX-=dx);
                }
                else if (event.getCode().equals(KeyCode.D)) {
                    primaryStage.getScene().getCamera().setTranslateX(cameraX+=dx);
                }

                else if (event.getCode().equals(KeyCode.W)) {
                    primaryStage.getScene().getCamera().setTranslateY(cameraY-=dy);
                }
                else if (event.getCode().equals(KeyCode.S)) {
                    primaryStage.getScene().getCamera().setTranslateY(cameraY+=dy);
                }

                else if (event.getCode().equals(KeyCode.Q)) {
                    primaryStage.getScene().getCamera().setTranslateZ(cameraZ-=dz);
                }
                else if (event.getCode().equals(KeyCode.E)) {
                    primaryStage.getScene().getCamera().setTranslateZ(cameraZ+=dz);
                }
            }
        });
    }

    private void prepareCoordinates() {
        corX0 = posX - width / 2;
        corX1 = posX + width / 2;

        corY0 = posY - height / 2;
        corY1 = posY + height / 2;

        corZ0 = posZ;
        corZ1 = posZ - depth;

    }

    public static void main(String[] args) {
        Application.launch(args);
    }
}
