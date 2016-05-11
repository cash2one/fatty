package com.cvtest.run;

import java.io.IOException;
import com.android.uiautomator.core.UiObjectNotFoundException;
import com.cvtest.utils.Automator;

public class QuadrantStandardRun extends Automator {

    private final String PATH_QUADRANTSTANDARD = "QuadrantStandard";
    private final String PKG_QUADRANTSTANDARD = "com.aurorasoftworks.quadrant.ui.standard";
    private final String ACT_QUADRANTSTANDARD = "com.aurorasoftworks.quadrant.ui.standard.QuadrantStandardLauncherActivity";
    private final String STR_CHECK_QUADRANTSTANDARD = "Information";
    private final String STR_RUN_QUADRANTSTANDARD = "Run full benchmark";
    private final String STR_RESULT_QUADRANTSTANDARD = "Benchmark result";
    private final String STR_ERROR_QUADRANTSTANDARD = "Error";
    private final String STR_CLOSE_QUADRANTSTANDARD = "Close";

    public void testRunQuadrantStandard() throws UiObjectNotFoundException {
        launchAppByPackage(PKG_QUADRANTSTANDARD, ACT_QUADRANTSTANDARD);
        while (!isExistObjectwithText(STR_CHECK_QUADRANTSTANDARD)) {
            sleep(WAIT_TIME);
        }
        clickByText(STR_CAPITAL_OK);
        clickByText(STR_RUN_QUADRANTSTANDARD);
        while (!isExistObjectwithText(STR_RESULT_QUADRANTSTANDARD)) {
            sleep(WAIT_TIME * 2);
        }
        clickByText(STR_YES);
        if (isExistObjectwithText(STR_ERROR_QUADRANTSTANDARD)) {
            clickByText(STR_CLOSE_QUADRANTSTANDARD);
            System.out.println("Network error!");
            System.exit(1);
        }
        sleep(WAIT_TIME * 2);
        try {
            Runtime.getRuntime().exec("screencap -p /mnt/sdcard/screen.png");
        } catch (IOException e) {
            System.out.println("Error: screencap -p /mnt/sdcard/" + util.getCurrentTime(2) + "_screen.png");
            System.exit(1);
        }

        String cmdString = "cp -R /mnt/sdcard/screen.png /mnt/sdcard/Autoperf/Performance_" 
            + PATH_QUADRANTSTANDARD + util.getCurrentTime(2) + "_screen.png";
        try {
            Runtime.getRuntime().exec(cmdString);
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        try {
            Runtime.getRuntime().exec("cp -R /mnt/sdcard/screen.png /mnt/sdcard/Autoperf/Performance_"
                + util.getCurrentTime(2) + "_screen.png");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
