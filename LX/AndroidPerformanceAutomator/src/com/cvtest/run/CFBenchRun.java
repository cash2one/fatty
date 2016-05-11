package com.cvtest.run;

import android.os.RemoteException;

import com.android.uiautomator.core.UiObjectNotFoundException;
import com.android.uiautomator.core.UiScrollable;
import com.android.uiautomator.core.UiSelector;
import com.cvtest.utils.Automator;

public class CFBenchRun extends Automator {

    private final String PATH_CFBENCH = "Cfbench";
    private final String PKG_CFBENCH = "eu.chainfire.cfbench";
    private final String ACT_CFBENCH = "eu.chainfire.cfbench.MainActivity";
    private final String[] TILE_CFBENCH = { 
        "Time", "Native MIPS", "Native MSFLOPS", 
        "Native MDFLOPS", "Native MALLOCS", "Native Memory Read", 
        "Native Memory Write", "Native Disk Read", "Native Disk Write", 
        "Native Score", "Overall Score" 
    };

    private final String STR_START_CFBENCH = "Full Benchmark";

    private final String STR_BENCH_MARKING = "BenchMarking";

    public void testRunCFBench() throws UiObjectNotFoundException,
            RemoteException {
        
        // launch app by app' package name
        launchAppByPackage(PKG_CFBENCH, ACT_CFBENCH);

        if (getUiDevice().getDisplayHeight() < getUiDevice().getDisplayWidth())
            getUiDevice().setOrientationRight();
        sleep(WAIT_TIME);

        clickByText(STR_START_CFBENCH);

        while (true) {
            if (getObjectByClassName(STR_PROCESS_BAR).exists()
                    && getObjectByContainText(STR_BENCH_MARKING).exists()) {
                break;
            }
            clickByText(STR_START_CFBENCH);
        }

        sleep(WAIT_TIME * 2);

        while (true) {
            if (!getObjectByClassName(STR_PROCESS_BAR).exists() && !getObjectByContainText(STR_BENCH_MARKING).exists()) {
                break;
            }
            sleep(WAIT_TIME);
        }

        String[] benchmarkDetail = new String[11];
        benchmarkDetail[0] = util.getCurrentTime(1);

        UiScrollable uiScrollable = new UiScrollable(new UiSelector().className(STR_LIST_VIEW).scrollable(true));
        uiScrollable.setAsVerticalList();

        for (int i = 1; i <= 10; i++) {
            scrollTextIntoView(uiScrollable, TILE_CFBENCH[i]);
            benchmarkDetail[i] = getObjectByBrotherText(TILE_CFBENCH[i], 1).getText();
        }

        util.writeData(benchmarkDetail, PATH_CFBENCH, TILE_CFBENCH);

        getUiDevice().setOrientationNatural();
        sleep(WAIT_TIME);

    }
}
