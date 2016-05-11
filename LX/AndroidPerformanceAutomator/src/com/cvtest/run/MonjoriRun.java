package com.cvtest.run;

import com.android.uiautomator.core.UiObjectNotFoundException;
import com.cvtest.utils.Automator;

public class MonjoriRun extends Automator {
    private final String PATH_MONJORI = "Monjori";
    private final String PKG_MONJORI = "de.swagner.monjori";
    private final String ACT_MONJORI = "de.swagner.monjori.MonjoriMenuActivity";
    private final String[] TILE_MONJORI = { "Time", "FPS" };
    private final String STR_START_MONJOR = "Tap to start Benchmark";
    private final String STR_END_MONJORI = "FPS";

    public void testRunMonjor() throws UiObjectNotFoundException {
        launchAppByPackage(PKG_MONJORI, ACT_MONJORI);
        clickByText(STR_START_MONJOR);
        while (true) {
            if (getObjectByStartText(STR_END_MONJORI).exists()) {
                break;
            }
            sleep(WAIT_TIME);
        }
        String[] benchmarkDetail = {
            util.getCurrentTime(1),
            getObjectByStartText(STR_END_MONJORI).getText().replaceAll("\\D", "") 
        };
        util.writeData(benchmarkDetail, PATH_MONJORI, TILE_MONJORI);
        sleep(WAIT_TIME);
    }
}
