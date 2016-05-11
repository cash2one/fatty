package com.cvtest.run;

import com.android.uiautomator.core.UiObject;
import com.android.uiautomator.core.UiObjectNotFoundException;
import com.android.uiautomator.core.UiSelector;
import com.cvtest.utils.Automator;

public class BaseMarkESRun extends Automator {
    private final String PATH_BASEMARKES = "BaseMarkES";
    private final String PKG_BASEMARKES = "com.rightware.tdmm2v10jnifree";
    private final String ACT_BASEMARKES = "com.rightware.tdmm2v10jnifree.SplashView";
    private final String[] TILE_BASEMARKES = { "Time", "FPS" };
    private final String STR_RUN_BASEMARKES = "Run Benchmark";
    private final String STR_TILE_BASEMARKES = "Basemark ES 2.0 Taiji Free";

    public void testRunBaseMarkEs() throws UiObjectNotFoundException {
        launchAppByPackage(PKG_BASEMARKES, ACT_BASEMARKES);
        while (!isExistObjectwithText(STR_AGREE)) {
            sleep(WAIT_TIME);
        }
        clickByText(STR_AGREE);

        clickByText(STR_RUN_BASEMARKES);

        while (!isExistObjectwithText(STR_TILE_BASEMARKES)) {
            sleep(WAIT_TIME * 2);
        }
        String[] benchmarkDetail = {
            util.getCurrentTime(1),
            new UiObject(new UiSelector().className(STR_TEXT_VIEW).enabled(true).instance(1)).getText() 
        };
        util.writeData(benchmarkDetail, PATH_BASEMARKES, TILE_BASEMARKES);
        sleep(WAIT_TIME);
    }
}
