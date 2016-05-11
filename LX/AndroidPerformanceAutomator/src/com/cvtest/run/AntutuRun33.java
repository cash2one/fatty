package com.cvtest.run;

import java.io.IOException;
import android.os.RemoteException;
import com.android.uiautomator.core.UiObject;
import com.android.uiautomator.core.UiObjectNotFoundException;
import com.cvtest.utils.Automator;

public class AntutuRun33 extends Automator {

    private final String PATH_ANTUTU = "Antutu";
    private final String PKG_ANTUTU = "com.antutu.ABenchMark";
    private final String ACT_ANTUTU = "com.antutu.ABenchMark.ABenchMarkStart";
    private final String[] TILE_ANTUTU = { 
        "Time", "Total_score", "RAM", "CPU_integer", 
        "CPU_float-point", "2D_graphics", "3D_graphics",
        "Database_IO", "SDcard_write", "SDcard_read" 
    };

    private final String STR_CLOSE = "Close";
    private final String STR_START_ANTUTU = "Start Test";
    private final String STR_TEST_AGAIN = "Test Again";
    private final String STR_DETAIL = "Detailed Scores";
    private final String STR_TOTAL_SCORE = "Total score:";
    private final String STR_RAM = "RAM:";
    private final String STR_CPU_INTEGER = "CPU integer:";
    private final String STR_CPU_FLOAT = "CPU float-point:";
    private final String STR_2D_GRAPHICS = "2D graphics:";
    private final String STR_3D_GRAPHICS = "3D graphics:";
    private final String STR_DATABASE_IO = "Database IO:";
    private final String STR_SD_WRITE = "SD card write:";
    private final String STR_SD_READ = "SD card read:";
    

    private UiObject btClose;
    private UiObject btStartTest;
    private UiObject btTestAgain;
    private UiObject btDetail;
    private UiObject btTotalScore;
    private String totalScore;

    public void testRunAntutu() throws UiObjectNotFoundException, IOException, RemoteException {

        btClose = (UiObject) getObjectByText(STR_CLOSE);
        btStartTest = (UiObject) getObjectByText(STR_START_ANTUTU);
        btTestAgain = (UiObject) getObjectByText(STR_TEST_AGAIN);
        btDetail = (UiObject) getObjectByText(STR_DETAIL);
        btTotalScore = (UiObject) getObjectByContainText(STR_TOTAL_SCORE);

        launchAppByPackage(PKG_ANTUTU, ACT_ANTUTU);

        while (true) {
            if (btClose.exists()) {
                btClose.clickAndWaitForNewWindow();
                break;
            }
            sleep(WAIT_TIME);
        }

        if (btTestAgain.exists()) {
            btTestAgain.clickAndWaitForNewWindow();
        }
        sleep(WAIT_TIME);

        if (getUiDevice().getDisplayHeight() < getUiDevice().getDisplayWidth()) {
            getUiDevice().setOrientationRight();
        }

        sleep(WAIT_TIME);

        btStartTest.clickAndWaitForNewWindow();

        while (true) {
            if (!btStartTest.exists()) {
                break;
            }
            btStartTest.clickAndWaitForNewWindow();
            sleep(WAIT_TIME);
        }

        while (true) {
            if (btTotalScore.exists()) {
                break;
            }
            sleep(WAIT_TIME);
        }

        totalScore = getObjectByBrotherContainText(STR_TOTAL_SCORE, 1).getText();

        getUiDevice().pressBack();

        if (btDetail.exists()) {
            btDetail.clickAndWaitForNewWindow();
        }

        String[] benchmarkDetail = {
            util.getCurrentTime(1),
            totalScore,
            getObjectByBrotherText(STR_RAM, 1).getText(),
            getObjectByBrotherText(STR_CPU_INTEGER, 1).getText(),
            getObjectByBrotherText(STR_CPU_FLOAT, 1).getText(),
            getObjectByBrotherText(STR_2D_GRAPHICS, 1).getText()
                .substring(getObjectByBrotherText(STR_2D_GRAPHICS, 1).getText().indexOf("]") + 1)
                .replaceAll("\\s*", ""),
            getObjectByBrotherText(STR_3D_GRAPHICS, 1).getText()
                .substring(getObjectByBrotherText(STR_3D_GRAPHICS, 1).getText().indexOf("]") + 1)
                .replaceAll("\\s*", ""),
            getObjectByBrotherText(STR_DATABASE_IO, 1).getText(),
            getObjectByBrotherText(STR_SD_WRITE, 1).getText()
                .substring(getObjectByBrotherText(STR_SD_WRITE, 1).getText().indexOf(")") + 1),
            getObjectByBrotherText(STR_SD_READ, 1).getText()
                .substring(getObjectByBrotherText(STR_SD_READ, 1).getText().indexOf(")") + 1) };

        util.writeData(benchmarkDetail, PATH_ANTUTU, TILE_ANTUTU);

        getUiDevice().setOrientationNatural();

        sleep(WAIT_TIME);

    }
}
