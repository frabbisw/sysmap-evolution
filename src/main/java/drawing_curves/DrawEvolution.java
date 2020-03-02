package drawing_curves;

import org.jfree.ui.RefineryUtilities;
import types.EvolutionData;

import java.util.ArrayList;

public class DrawEvolution {
    static int flag = 0;
    public static void draw(String projectName, ArrayList<EvolutionData> evolutionDataOfVersions){
        if(flag == 1){
            return;
        }
        flag = 1;

        BarChart_AWT chart = new BarChart_AWT("Sysmap Evolution", "Evolution of "+projectName, evolutionDataOfVersions);
        chart.pack();
        RefineryUtilities.centerFrameOnScreen( chart );
        chart.setVisible( true );
    }
}
