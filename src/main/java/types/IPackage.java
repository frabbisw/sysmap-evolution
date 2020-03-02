package types;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;

public class IPackage {
    double tloc;
    String packName;
    Map<String, IClass> classMap;
//    ArrayList<IClass>iClasses;

    public IPackage(String packName){
        this.packName=packName;
        this.classMap=new TreeMap<>();
        tloc=0;
    }

    public void addiClass(IClass iClass){
        classMap.put(iClass.getClassName(), iClass);
        tloc+=iClass.getLoc();
    }

    public Map<String, IClass> getClassMap() {
        return classMap;
    }

    public double getTloc() {
        return tloc;
    }

    public String getPackName() {
        return packName;
    }

    public Collection<IClass> getiClasses() {
        return classMap.values();
    }
}