package org.marcnazarian.gpxjoin;

import com.googlecode.zohhak.api.Configure;
import com.googlecode.zohhak.api.TestWith;
import com.googlecode.zohhak.api.runners.ZohhakRunner;
import org.junit.runner.RunWith;

import java.text.DecimalFormat;
import java.util.Map;
import java.util.TreeMap;

@RunWith(ZohhakRunner.class)
@Configure(separator="->")
public class GPXElevationBenchmark {

    private static final double MIN_ELEVATION_THRESHOLD_TO_TEST = 0.1;
    private static final double MAX_ELEVATION_THRESHOLD_TO_TEST = 5;
    
    private static Map<String, Integer> numberOfBestThreshold = new TreeMap<>();

    @TestWith({
            "Petit Van            -> src/test/resources/petit_van_botte.gpx      -> 1240",
            "Moucherotte          -> src/test/resources/moucherotte_et_demi.gpx  -> 1131",
            "Aigleton             -> src/test/resources/col_aigleton.gpx         -> 1076",
            "Chamrousse sauvage   -> src/test/resources/chamrousse_sauvage.gpx   ->  487",
            "La Havane            -> src/test/resources/havana.gpx               ->   50",
            "Obiou                -> src/test/resources/obiou.gpx                -> 1327",
            "4 seigneurs          -> src/test/resources/4_seigneurs.gpx          ->  999",
            "Chalance             -> src/test/resources/chalance.gpx             -> 1706",
            "Chamrousse vélo      -> src/test/resources/chamrousse_velo.gpx      -> 1535",
            "Clémencière          -> src/test/resources/clemenciere.gpx          ->  627",
            "Rochail              -> src/test/resources/rochail.gpx              -> 1926",
            "Prologue raid        -> src/test/resources/prologue_raid.gpx        -> 1484",
            "Traversée Vercors    -> src/test/resources/traversee_vercors.gpx    -> 3348",
            "Traversée Belledonne -> src/test/resources/traversee_belledonne.gpx -> 3489",
            "Intégrale Belledonne -> src/test/resources/integrale_belledonne.gpx -> 7504",
            "Pravouta             -> src/test/resources/2018-04-02_pravouta.gpx  ->  980"
    })
    public void computeElevation(String trackName, String gpxFileLocation, double expectedElevation) {
        // arrange
        DecimalFormat decimalFormat2 = new DecimalFormat("#.##");

        // act
        GPX gpx = GPXReader.read(gpxFileLocation);
        assert gpx != null;
        
        System.out.println(trackName + ": ");

        String bestThresholdForTrack = "";
        double minPercentageError = 600;
        double minDifferenceError = 8000;

        for (double thresholdForElevation = MIN_ELEVATION_THRESHOLD_TO_TEST; thresholdForElevation <= MAX_ELEVATION_THRESHOLD_TO_TEST; thresholdForElevation += 0.1) {
            double actualElevation = gpx.elevation(thresholdForElevation);

            double differenceBetweenElevations = Math.abs(actualElevation - expectedElevation);
            double percentageOfError = percentageOfError(expectedElevation, actualElevation);

            if (percentageOfError < minPercentageError) {
                minPercentageError = percentageOfError;
                minDifferenceError = differenceBetweenElevations;
                bestThresholdForTrack = decimalFormat2.format(thresholdForElevation);
            }

            //System.out.println("  threshold= " + decimalFormat2.format(thresholdForElevation) + ": altitude= " + decimalFormat2.format(actualElevation) + " | diff=" + decimalFormat2.format(differenceBetweenElevations) + " | error=" + decimalFormat2.format(percentageOfError) + " %");
        }

        System.out.println(trackName + ": best threshold = " + bestThresholdForTrack + " || diff = " + decimalFormat2.format(minDifferenceError) + " || error = " + decimalFormat2.format(minPercentageError));

        numberOfBestThreshold.put(bestThresholdForTrack, numberOfBestThreshold.getOrDefault(bestThresholdForTrack, 0) + 1);

        System.out.println(numberOfBestThreshold + System.lineSeparator());
    }

    private double percentageOfError(double expectedElevation, double actualElevation) {
        return Math.abs(((expectedElevation - actualElevation)/expectedElevation)*100);
    }
}
