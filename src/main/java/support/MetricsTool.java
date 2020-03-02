package support;

import class_metrics.CallGraphMetrics;
import collect_classes.ClassFinder;
import collect_classes.FileExplorer;
import com.github.javaparser.ast.CompilationUnit;
import types.IClass;
import types.IPackage;

import java.io.*;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

public class MetricsTool {
    String projectPath;
//    String outputPath;
    ArrayList<ClassManager> classManagers;
    CallGraphMetrics callGraphMetrics;
    InheritenceHandler inheritenceHandler;
    Map<String , IPackage> packageMap;
    String projectName="";

    public MetricsTool(String projectPath) {
        String [] args = projectPath.split("/");
        projectName = args[args.length-1];

        classManagers = new ArrayList<>();
        callGraphMetrics = new CallGraphMetrics();

        this.projectPath = projectPath;

        ClassFinder.setProjectPath(projectPath);
        FileExplorer explorer = ClassFinder.getClassExplorer();

        for (String key : explorer.getCUMap().keySet()) {
            for (int i = 0; i < explorer.getCUMap().get(key).size(); i++) {
                CompilationUnit cu = explorer.getCUMap().get(key).get(i);
                CUManager manager = new CUManager(key, cu);

                for (ClassManager classManager : manager.getLocalClasses()) {
                    classManagers.add(classManager);
                }
            }
        }

        generateCallGraph();
        setCouplingValues();

        inheritenceHandler=new InheritenceHandler(classManagers);
        setInheritenceValues();
        preparePackageMap();
    }

    public String getProjectName() {
        return projectName;
    }

    private void preparePackageMap() {
        packageMap = new TreeMap<>();
        for(ClassManager classManager : classManagers){
            IClass iClass = new IClass(classManager.getMyClassName(), classManager.getMyPackageName());
            iClass.setCoupling(classManager.getCoupling());
            iClass.setLackCohesion(classManager.getLackOfCohesion());
            iClass.setLoc(classManager.getLineOfCodes());
            iClass.setNos(classManager.getNumberOfStatements());
            iClass.setWmc(classManager.getWeightedMethodCount());

            String packageName = classManager.getMyPackageName();
            if(packageMap.containsKey(packageName)){
                packageMap.get(packageName).addiClass(iClass);
            }
            else {
                packageMap.put(packageName, new IPackage(packageName));
            }
        }
    }

    public Map<String, IPackage> getPackageMap() {
        return packageMap;
    }

    private void setInheritenceValues() {
        for(ClassManager classManager : classManagers)
        {
            classManager.setNumberOfChildren(inheritenceHandler.getNumberOfChildren(classManager.getMyFullName()));
            classManager.setLevelOfInheritence(inheritenceHandler.getLevelOfInheritence(classManager.getMyFullName()));
        }
    }

    private void setCouplingValues() {
        Map<String, Integer> couplingValues = callGraphMetrics.getCouplingValues();

        for(ClassManager manager : classManagers)
        {
            int value;
            try{
                value = couplingValues.get(manager.getMyFullName());
            }
            catch (Exception e){
                value=0;
            }
            manager.setCoupling(value);
        }
    }

    private void generateCallGraph() {
        for (ClassManager classManager : classManagers) {
            for (MethodManager methodManager : classManager.getMethodManagers())
                callGraphMetrics.addCouplingHandler(methodManager.getCouplingHandler());
        }
        callGraphMetrics.generateCouplingClass();
        callGraphMetrics.generateMethodCallGraph();
    }
}