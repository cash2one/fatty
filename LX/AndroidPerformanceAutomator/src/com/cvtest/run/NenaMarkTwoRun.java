package com.cvtest.run;

import com.android.uiautomator.core.UiObjectNotFoundException;
import com.cvtest.utils.Automator;

public class NenaMarkTwoRun extends Automator {
    private final String PATH_NENAMARK2 = "Nenamark2";
    private final String PKG_NENAMARK2 = "se.nena.nenamark2";
    private final String ACT_NENAMARK2 = "se.nena.nenamark2.NenaMark2";
    private final String STR_END_NENAMARK_ONE = "Publish";
    private final String[] TILE_NENAMARK2 = { "Time", "FPS" };

    public void testRunNenaMarkTwo() throws UiObjectNotFoundException {
        launchAppByPackage(PKG_NENAMARK2, ACT_NENAMARK2);

        getUiDevice().click(getUiDevice().getDisplayWidth() / 4, getUiDevice().getDisplayHeight() / 3);

        while (true) {
            if (getObjectByText(STR_END_NENAMARK_ONE).exists()) {
                break;
            }
            sleep(WAIT_TIME);
        }

        String[] benchmarkDetail = {
            util.getCurrentTime(1),
            getObjectByStartText(TILE_NENAMARK2[1]).getText()
                .toString().replaceAll("\\s*", "")
                .split(":")[1].replaceAll("[a-zA-Z]", "") 
        };

        util.writeData(benchmarkDetail, PATH_NENAMARK2, TILE_NENAMARK2);

        sleep(WAIT_TIME);
    }
}
