package com.cvtest.run;

import com.android.uiautomator.core.UiObjectNotFoundException;
import com.cvtest.utils.Automator;

public class NenaMarkOneRun extends Automator {

    private final String PATH_NENAMARK1 = "Nenamark1";
    private final String PKG_NENAMARK1 = "se.nena.nenamark1";
    private final String ACT_NENAMARK1 = "se.nena.nenamark1.NenaMark1";
    private final String[] TILE_NENAMARK1 = { "Time", "FPS" };
    private final String STR_START_NENAMARK_ONE = "Run";
    private final String STR_END_NENAMARK_ONE = "Publish";

    public void testRunNenaMarkOne() throws UiObjectNotFoundException {

        launchAppByPackage(PKG_NENAMARK1, ACT_NENAMARK1);

        clickByText(STR_START_NENAMARK_ONE);

        while (true) {
            if (getObjectByText(STR_END_NENAMARK_ONE).exists()) {
                break;
            }
            sleep(WAIT_TIME);
        }

        String[] benchmarkDetail = {
            util.getCurrentTime(1),
            getObjectByStartText(TILE_NENAMARK1[1]).getText()
                .toString().replaceAll("\\s*", "")
                .split(":")[1].replaceAll("[a-zA-Z]", "") 
        };

        util.writeData(benchmarkDetail, PATH_NENAMARK1, TILE_NENAMARK1);

        sleep(WAIT_TIME);
    }
}
