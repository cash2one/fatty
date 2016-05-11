package com.cvtest.run;

import java.io.IOException;
import android.os.RemoteException;
import com.android.uiautomator.core.UiObjectNotFoundException;
import com.android.uiautomator.core.UiScrollable;
import com.android.uiautomator.core.UiSelector;
import com.cvtest.utils.Automator;

public class AntutuRun extends Automator {

    private final String PATH_ANTUTU = "Antutu";
    private final String PKG_ANTUTU = "com.antutu.ABenchMark";
    private final String ACT_ANTUTU = "com.antutu.ABenchMark.ABenchMarkStart";
    private final String[] TILE_ANTUTU = { 
        "Time", "Total_score", "Multitask",
        "Runtime", "RAM Operation", "RAM Speed", 
        "CPU integer", "CPU float-point", "2D graphics", 
        "3D graphics", "Storage I/O", "Database I/O" 
    };
    private final String[] TILE_ANTUTU_NEW = { 
        "Time", "Total_score", "Multitask",
        "Runtime", "RAM Operation", "RAM Speed",
        "CPU integer(multi-thread)", "CPU float-point(multi-thread)",
        "CPU integer(single-thread)", "CPU float-point(single-thread)",
        "2D graphics", "3D graphics", "Storage I/O", "Database I/O" 
    };

    private UiScrollable uiScrollable;
    private String[] benchmarkDetail = null;
    private String[] tmpStrings = null;

    public void testRunAntutu() throws UiObjectNotFoundException, IOException,
            RemoteException {

        // launching app by package and active name
        launchAppByPackage(PKG_ANTUTU, ACT_ANTUTU);

        // click "Try Now"
        while (true) {
            if (getObjectByText("Test").exists()) {
                break;
            } else if (getObjectByText("Try Now").exists()) {
                clickByText("Try Now");
                break;
            }
            sleep(WAIT_TIME);
        }

        sleep(WAIT_TIME);
        // click "Test"
        if (getObjectByClassNameAndText("android.widget.Button", "Test")
                .exists()) {
            getObjectByClassNameAndText("android.widget.Button", "Test")
                    .clickAndWaitForNewWindow();
        }

        // click "Test" to start
        if (getObjectByText("Test").exists()) {
            clickByText("Test");
        }

        // click "Details"
        while (true) {
            if (!getObjectByText("STOP").exists()
                    && getObjectByText("Details").exists()) {
                clickByText("Details");
                break;
            }
            sleep(WAIT_TIME);
        }

        // stop
        while (true) {
            if (getObjectByStartText("Details").exists())
                break;
            sleep(WAIT_TIME);
        }

        uiScrollable = new UiScrollable(
                new UiSelector().className(STR_SCROLL_VIEW));
        uiScrollable.setAsVerticalList();

        if (getObjectByText("Details - v5.1").exists()
                && uiScrollable.isScrollable()) {
            mutilScrollabeData();
        } else if (getObjectByText("Details - v5.1").exists()
                && !uiScrollable.isScrollable()) {
            mutilUnScrollabeData();
        } else {
            scrollabeData();
        }
    }

    private void scrollabeData() throws UiObjectNotFoundException {
        benchmarkDetail = new String[12];
        tmpStrings = new String[10];

        benchmarkDetail[0] = util.getCurrentTime(1);
        for (int i = 2; i < TILE_ANTUTU.length; i++) {
            scrollTextIntoView(uiScrollable, TILE_ANTUTU[i] + ":");
            if (i == 8 || i == 9) {
                tmpStrings[i - 2] = benchmarkDetail[i] = getObjectByBrotherText(
                        TILE_ANTUTU[i] + ":", 1)
                        .getText()
                        .substring(
                                getObjectByBrotherText(TILE_ANTUTU[i] + ":", 1)
                                        .getText().indexOf("]") + 1)
                        .replaceAll("\\s*", "");
            } else {
                tmpStrings[i - 2] = benchmarkDetail[i] = getObjectByBrotherText(
                        TILE_ANTUTU[i] + ":", 1).getText();
            }
        }
        // total score
        benchmarkDetail[1] = util.calculateTotal(tmpStrings);

        // write data
        util.writeData(benchmarkDetail, PATH_ANTUTU, TILE_ANTUTU);

        sleep(WAIT_TIME);
    }

    private void mutilUnScrollabeData() throws UiObjectNotFoundException {
        benchmarkDetail = new String[14];
        tmpStrings = new String[12];
        benchmarkDetail[0] = util.getCurrentTime(1);

        benchmarkDetail[8] = tmpStrings[6] = getObjectByResourceId(
                "com.antutu.ABenchMark:id/cpu_int_text2").getText().toString();

        benchmarkDetail[9] = tmpStrings[7] = getObjectByResourceId(
                "com.antutu.ABenchMark:id/cpu_float_text2").getText()
                .toString();
        for (int i = 2; i < TILE_ANTUTU.length; i++) {
            if (i >= 8) {
                tmpStrings[i] = benchmarkDetail[i + 2] = getObjectByBrotherText(
                        TILE_ANTUTU[i] + ":", 1)
                        .getText()
                        .substring(
                                getObjectByBrotherText(TILE_ANTUTU[i] + ":", 1)
                                        .getText().indexOf("]") + 1)
                        .replaceAll("\\s*", "");
            } else {
                tmpStrings[i - 2] = benchmarkDetail[i] = getObjectByBrotherText(
                        TILE_ANTUTU[i] + ":", 1).getText();
            }
        }
        // total score
        benchmarkDetail[1] = util.calculateTotal(tmpStrings);

        // write data
        util.writeData(benchmarkDetail, PATH_ANTUTU + "5.1", TILE_ANTUTU_NEW);

        sleep(WAIT_TIME);
    }

    private void mutilScrollabeData() throws UiObjectNotFoundException {
        benchmarkDetail = new String[14];
        tmpStrings = new String[12];
        benchmarkDetail[0] = util.getCurrentTime(1);
        scrollResourceIdIntoView(uiScrollable, "com.antutu.ABenchMark:id/cpu_int_text2");
        benchmarkDetail[8] = tmpStrings[6] = getObjectByResourceId("com.antutu.ABenchMark:id/cpu_int_text2").getText().toString();
        scrollResourceIdIntoView(uiScrollable, "com.antutu.ABenchMark:id/cpu_float_text2");
        benchmarkDetail[9] = tmpStrings[7] = getObjectByResourceId("com.antutu.ABenchMark:id/cpu_float_text2").getText().toString();

        uiScrollable.scrollToBeginning(uiScrollable.getMaxSearchSwipes());
        for (int i = 2; i < TILE_ANTUTU.length; i++) {
            scrollTextIntoView(uiScrollable, TILE_ANTUTU[i] + ":");
            if (i >= 8) {
                tmpStrings[i] = benchmarkDetail[i + 2] = getObjectByBrotherText(ILE_ANTUTU[i] + ":", 1)
                    .getText()
                    .substring(getObjectByBrotherText(TILE_ANTUTU[i] + ":", 1).getText().indexOf("]") + 1)
                    .replaceAll("\\s*", "");
            } else {
                tmpStrings[i - 2] = benchmarkDetail[i] = getObjectByBrotherText(TILE_ANTUTU[i] + ":", 1).getText();
            }
        }
        // total score
        benchmarkDetail[1] = util.calculateTotal(tmpStrings);

        // write data
        util.writeData(benchmarkDetail, PATH_ANTUTU + "5.1", TILE_ANTUTU_NEW);

        sleep(WAIT_TIME);
    }
}
