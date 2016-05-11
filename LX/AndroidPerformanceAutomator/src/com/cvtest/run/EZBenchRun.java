package com.cvtest.run;

import com.android.uiautomator.core.UiObjectNotFoundException;
import com.cvtest.utils.Automator;

public class EZBenchRun extends Automator {
    private final String PATH_EZBENCH = "Ezbench";
    private final String PKG_EZBENCH = "net.seacow.ezbench";
    private final String ACT_EZBENCH = "net.seacow.ezbench.TitleActivity";
    private final String[] TILE_EZBENCH = {
        "Time", "Memory I/O", "File Reading", "File Writing", 
        "Encryption", "Decryption", "Hashing", "Mandelbrot Set",
        "Financial Modeling", "ezBench points" 
    };

    private final String STR_START_EZBENCH = "Run Benchmark";

    public void testRunEZBench() throws UiObjectNotFoundException {

        launchAppByPackage(PKG_EZBENCH, ACT_EZBENCH);

        clickByText(STR_START_EZBENCH);
        while (true) {
            if (!getObjectByClassName(STR_PROCESS_BAR).exists()) {
                break;
            }
            sleep(WAIT_TIME);
        }

        String[] benchmarkDetail = {
            util.getCurrentTime(1),
            getObjectByStartText(TILE_EZBENCH[1]).getText().replaceAll("\\D", ""),
            getObjectByStartText(TILE_EZBENCH[2]).getText().replaceAll("\\D", ""),
            getObjectByStartText(TILE_EZBENCH[3]).getText().replaceAll("\\D", ""),
            getObjectByStartText(TILE_EZBENCH[4]).getText().replaceAll("\\D", ""),
            getObjectByStartText(TILE_EZBENCH[5]).getText().replaceAll("\\D", ""),
            getObjectByStartText(TILE_EZBENCH[6]).getText().replaceAll("\\D", ""),
            getObjectByStartText(TILE_EZBENCH[7]).getText().replaceAll("\\D", ""),
            getObjectByStartText(TILE_EZBENCH[8]).getText().replaceAll("\\D", ""),
            getObjectByStartText(TILE_EZBENCH[9]).getText().replaceAll("\\D", "") 
        };

        util.writeData(benchmarkDetail, PATH_EZBENCH, TILE_EZBENCH);

        sleep(WAIT_TIME);
    }
}
