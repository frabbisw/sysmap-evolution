package types;

public class IClass {
    String className="";
    String packageName="";
    double loc=1, nos=1, wmc=1;
    double coupling=1, lackCohesion=1;
    double loComments=1;
    double response=1;
    double children=1;
    double inheritence=1;

    public IClass(String className, String packageName){
        this.packageName = packageName;
        this.className = className;
    }

    public String getClassName() {
        return className;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setLoc(double loc) {
        this.loc = loc;
    }
    public void setNos(double loc) {
        this.nos = nos;
    }

    public void setWmc(double wmc) {
        this.wmc = wmc;
    }

    public double getLoc() {
        return loc;
    }
    public double getNos() {
        return nos;
    }
    public double getWmc() {
        return wmc;
    }

    public double getLoggedCoupling() {
        return Math.log(coupling+1);
    }

    public void setCoupling(double coupling) {
        this.coupling = coupling;
    }

    public double getLoggedLackCohesion() {
        return Math.log(lackCohesion+1);
    }

    public void setLackCohesion(double lackCohesion) {
        this.lackCohesion = lackCohesion;
    }

    public double getCoupling() {
        return coupling;
    }

    public double getLackCohesion() {
        return lackCohesion;
    }

    public double getLoComments() {
        return loComments;
    }

    public void setLoComments(double loComments) {
        this.loComments = loComments;
    }

    public double getResponse() {
        return response;
    }

    public void setResponse(double response) {
        this.response = response;
    }

    public double getChildren() {
        return children;
    }

    public void setChildren(double children) {
        this.children = children;
    }

    public double getInheritence() {
        return inheritence;
    }

    public void setInheritence(double inheritence) {
        this.inheritence = inheritence;
    }
}



