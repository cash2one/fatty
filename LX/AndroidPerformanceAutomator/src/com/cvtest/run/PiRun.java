package com.cvtest.run;

import com.android.uiautomator.core.UiObjectNotFoundException;
import com.cvtest.utils.Automator;

public class PiRun extends Automator {
    private final String PATH_PI = "Pi";
    private final String PKG_PI = "com.gg.pi";
    private final String ACT_PI = "com.gg.pi.MainActivity";
    private final String[] TILE_PI = { "Time", "Pi_10000000" };
    private final String STR_Start_PI = "10,000,000";
    private final String STR_RESTART_PI = "Recalculate";
    private final String STR_DONE_PI = "Done";

    public void testRunPi() throws UiObjectNotFoundException {
        launchAppByPackage(PKG_PI, ACT_PI);

        getObjectByBrotherText(STR_Start_PI, 1).clickAndWaitForNewWindow();

        if (isExistObjectwithText(STR_RESTART_PI)) {
            clickByText(STR_RESTART_PI);
        }

        while (true) {
            if (!getObjectByClassName(STR_PROCESS_BAR).exists()) {
                break;
            }
            sleep(WAIT_TIME);
        }

        if (isExistObjectwithText(STR_DONE_PI))
            clickByText(STR_DONE_PI);

        String[] benchmarkDetail = {
            util.getCurrentTime(1),
            getObjectByBrotherText(STR_Start_PI, 1).getText().replaceAll("[a-z]", "") 
        };
        util.writeData(benchmarkDetail, PATH_PI, TILE_PI);
        sleep(WAIT_TIME);
    }
}
