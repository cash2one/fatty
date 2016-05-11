package com.cvtest.run;

import com.android.uiautomator.core.UiObjectNotFoundException;
import com.cvtest.utils.Automator;

public class NenaMarkOneRun15 extends Automator {

    private final String PATH_NENAMARK1 = "Nenamark1";
    private final String PKG_NENAMARK1 = "se.nena.nenamark1";
    private final String ACT_NENAMARK1 = "se.nena.nenamark1.NenaMarkActivity";
    private final String[] TILE_NENAMARK1 = { "Time", "FPS" };
    private final String STR_START_NENAMARK_ONE = "Run";
    private final String STR_END_NENAMARK_ONE = "Publish";
    private final String STR_BACK_NENAMARK_ONE = "Back";
    private final String STR_START_TEXT_NENAMARK_ONE = "FPS:";

    public void testRunNenaMarkOne() throws UiObjectNotFoundException {

        launchAppByPackage(PKG_NENAMARK1, ACT_NENAMARK1);

        clickByText(STR_START_NENAMARK_ONE);

        while (true) {
            if (getObjectByText(STR_END_NENAMARK_ONE).exists()) {
                break;
            }
            sleep(WAIT_TIME);
        }
        clickByText(STR_BACK_NENAMARK_ONE);

        String[] benchmarkDetail = {
            util.getCurrentTime(1),
            getObjectByStartText(STR_START_TEXT_NENAMARK_ONE).getText()
                .toString().replaceAll("\\s*", "").split(":")[1]
                .replaceAll("[a-zA-Z]", "") 
        };

        util.writeData(benchmarkDetail, PATH_NENAMARK1, TILE_NENAMARK1);

        sleep(WAIT_TIME);
    }
}
