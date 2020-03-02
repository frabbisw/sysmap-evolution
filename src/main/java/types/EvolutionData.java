package types;

public class EvolutionData {
    double NumberOfPlots;
    double numberOfBuildings;
    double numberOfSkyscraper;
    double numberOfHeavyBuildings;
    String nameWithVersion;

    public EvolutionData(double numberOfPlots, double numberOfBuildings, double numberOfSkyscraper, double numberOfHeavyBuildings, String nameWithVersion) {
        NumberOfPlots = Math.log(numberOfPlots);
        this.numberOfBuildings =  Math.log(numberOfBuildings);
        this.numberOfSkyscraper = Math.log(numberOfSkyscraper);
        this.numberOfHeavyBuildings = Math.log(numberOfHeavyBuildings);
        this.nameWithVersion = nameWithVersion;
    }

    public void setNumberOfSkyscraper(int numberOfSkyscraper) {
        this.numberOfSkyscraper = numberOfSkyscraper;
    }

    public double getNumberOfHeavyBuildings() {
        return numberOfHeavyBuildings;
    }

    public void setNumberOfHeavyBuildings(int numberOfHeavyBuildings) {
        this.numberOfHeavyBuildings = numberOfHeavyBuildings;
    }

    public void setNameWithVersion(String nameWithVersion) {
        this.nameWithVersion = nameWithVersion;
    }

    public double getNumberOfSkyscraper() {
        return numberOfSkyscraper;
    }

    public String getNameWithVersion() {
        return nameWithVersion;
    }

    public double getNumberOfBuildings() {
        return numberOfBuildings;
    }

    public void setNumberOfBuildings(int numberOfBuildings) {
        this.numberOfBuildings = numberOfBuildings;
    }

    public double getNumberOfPlots() {
        return NumberOfPlots;
    }

    public void setNumberOfPlots(int numberOfPlots) {
        NumberOfPlots = numberOfPlots;
    }

    public double getNumberOfskyscraper() {
        return numberOfSkyscraper;
    }

    @Override
    public String toString() {
        String ret = "";
        ret += "Number Of Plots: ";
        ret += NumberOfPlots;
        ret += "\n";

        ret += "Number Of Buildings: ";
        ret += numberOfBuildings;
        ret += "\n";

        ret += "Number Of Skyscraper: ";
        ret += numberOfSkyscraper;
        ret += "\n";

        return ret;
    }
}
