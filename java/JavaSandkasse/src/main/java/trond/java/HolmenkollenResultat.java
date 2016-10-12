package trond.java;

import java.time.Duration;
import java.util.Map;

/**
 * @author trond.
 */
public class HolmenkollenResultat {

    private final String startNumber;

    private final String division;

    private final Duration totalTime;

    public HolmenkollenResultat(final Map<String, String> resultMap) {
        startNumber = resultMap.get("Startnumber");
        division = resultMap.get("Class").replaceAll(" .*", "");
        final String totalTimeStr = resultMap.get("Total Time");

        if (totalTimeStr.matches("0:*"))
            totalTime = null;
        else
            totalTime = Duration.parse(totalTimeStr.replaceAll("(\\d*):(\\d*):(\\d*)", "PT$1H$2M$3S")
                                                   .replaceAll("(\\d*):(\\d*)", "PT$1M$2S"));
    }

    public String getStartNumber() {
        return startNumber;
    }

    public String getDivision() {
        return division;
    }

    public Duration getTotalTime() {
        return totalTime;
    }
}

