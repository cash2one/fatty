package com.cvtest.run;

import com.android.uiautomator.core.UiObjectNotFoundException;
import com.cvtest.utils.Automator;

public class AndEBenchRun extends Automator {

    private final String FILE_ANDEBENCH = "AndEbenchResult";
    private final String PKG_ANDEBENCH = "com.eembc.coremark";
    private final String ACT_ANDEBENCH = "com.eembc.coremark.tabs";
    private final String[] TILE_ANDEBENCH = { "Time", "AndEMark native", "AndEMark Java" };
    private final String STR_IMAGE_BUTTON = "android.widget.ImageButton";
    private final String STR_RESULT_ANDEBENCH = "Results";
    private final String STR_END_ANDEBENCH = "Compare your Scores to other devices";

    public void testRunAndEBench() throws UiObjectNotFoundException {
        // launching app
        launchAppByPackage(PKG_ANDEBENCH, ACT_ANDEBENCH);

        sleep(WAIT_TIME * 2);

        // click ok button
        if (isExistObjectwithText(STR_CAPITAL_OK)) {
            clickByTextValue(STR_CAPITAL_OK);
            sleep(WAIT_TIME * 2);
        }

        clickByClassname(STR_IMAGE_BUTTON);

        while (true) {
            if (isExistObjectwithText(STR_CAPITAL_OK)) {
                clickByText(STR_CAPITAL_OK);
            }
            if (isExistObjectwithText(STR_END_ANDEBENCH))
                break;
            sleep(WAIT_TIME);
        }

        int count_benchmarkDetail = 1;
        String[] benchmarkDetail = new String[3];
        benchmarkDetail[0] = util.getCurrentTime(1);
        for (String key : getObjectByStartText(STR_RESULT_ANDEBENCH).getText()
                .replaceAll("\\s*", "").replaceAll("\\D", ":").split(":")) {
            if (!key.equals("")) {
                benchmarkDetail[count_benchmarkDetail] = key;
                count_benchmarkDetail++;
            }
        }

        util.writeData(benchmarkDetail, FILE_ANDEBENCH, TILE_ANDEBENCH);

        sleep(WAIT_TIME);
    }
}
