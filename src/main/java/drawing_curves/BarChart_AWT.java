package drawing_curves;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;
import types.EvolutionData;

import java.io.File;
import java.util.ArrayList;

import static constants.Paths.OUTPUT_FOLDER;

public class BarChart_AWT extends ApplicationFrame {
    ArrayList<EvolutionData> evolutionDataOfVersions;

    public BarChart_AWT( String applicationTitle , String chartTitle , ArrayList<EvolutionData> evolutionDataOfVersions) {
        super( applicationTitle );
        this.evolutionDataOfVersions=evolutionDataOfVersions;

        JFreeChart barChart = ChartFactory.createBarChart(
                chartTitle,
                "Versions",
                "Logged Number Count",
                createDataset(),
                PlotOrientation.VERTICAL,
                true, true, false);

        ChartPanel chartPanel = new ChartPanel( barChart );
        chartPanel.setPreferredSize(new java.awt.Dimension( 1000 , 600 ) );
        setContentPane( chartPanel );

        try {
            int width = 1000;    /* Width of the image */
            int height = 600;   /* Height of the image */

            File BarChart = new File( OUTPUT_FOLDER+chartTitle+".png" );
            ChartUtilities.saveChartAsPNG( BarChart , barChart , width , height );
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    private CategoryDataset createDataset( ) {
        final String NUMBER_OF_PLOTS = "#Plots";
        final String NUMBER_OF_BUILDINGS = "#Buildings";
        final String NUMBER_OF_SKYSCRAPER = "#Skyscrapers";
        final String NUMBER_OF_HEAVY_BUILDINGS = "#Large Buildings";

        final DefaultCategoryDataset dataset = new DefaultCategoryDataset( );

        if(evolutionDataOfVersions == null){
            System.out.println("Bar Chart AWT NULL");
        }

        for(EvolutionData evolutionData : this.evolutionDataOfVersions){
            dataset.addValue(evolutionData.getNumberOfPlots(), NUMBER_OF_PLOTS, evolutionData.getNameWithVersion());
            dataset.addValue(evolutionData.getNumberOfBuildings(), NUMBER_OF_BUILDINGS, evolutionData.getNameWithVersion());
            dataset.addValue(evolutionData.getNumberOfSkyscraper(), NUMBER_OF_SKYSCRAPER, evolutionData.getNameWithVersion());
            dataset.addValue(evolutionData.getNumberOfHeavyBuildings(), NUMBER_OF_HEAVY_BUILDINGS, evolutionData.getNameWithVersion());
        }

        return dataset;
    }
}