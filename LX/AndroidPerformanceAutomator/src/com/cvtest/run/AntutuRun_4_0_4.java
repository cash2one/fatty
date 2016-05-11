package com.cvtest.run;

import android.os.RemoteException;
import com.android.uiautomator.core.UiObject;
import com.android.uiautomator.core.UiObjectNotFoundException;
import com.android.uiautomator.core.UiSelector;

public class AntutuRun_4_0_4 extends AntutuRun33 {

    private final String PATH_ANTUTU_4_0_4 = "Aututu4.0.4";
    private final String[] TILE_ANTUTU_4_0_4 = { 
        "Time", "Totals", "Multitask", "Dalvik", 
        "CPU integer", "CPU float-point", "RAM Operation", 
        "RAM Speed", "2D graphics", "3D graphics",
        "Storage I/O", "Database I/O" 
    };
    private final String PKG_ANTUTU = "com.antutu.ABenchMark";
    private final String ACT_ANTUTU = "com.antutu.ABenchMark.ABenchMarkStart";
    private final String STR_TEST_ANTUTU = "Test";
    private final String STR_DETAILS_ANTUTU = "Details";

    public void testRunAntutu() throws UiObjectNotFoundException {
        launchAppByPackage(PKG_ANTUTU, ACT_ANTUTU);
        while (!isExistObjectwithText(STR_TEST_ANTUTU)) {
            sleep(WAIT_TIME);
        }
        clickByText(STR_TEST_ANTUTU);
        new UiObject(new UiSelector().className(STR_BUTTON).enabled(true).instance(0)).clickAndWaitForNewWindow();
        new UiObject(new UiSelector().className(STR_BUTTON).enabled(true).instance(0)).clickAndWaitForNewWindow();

        while (!isExistObjectwithText(STR_TEST_ANTUTU)) {
            sleep(WAIT_TIME);
        }
        clickByText(STR_TEST_ANTUTU);
        clickByText(STR_DETAILS_ANTUTU);

        String[] benchmarkDetail = new String[12];
        benchmarkDetail[0] = util.getCurrentTime(1);
        benchmarkDetail[1] = new UiObject(new UiSelector().className(STR_TEXT_VIEW).enabled(true).instance(2)).getText();

        for (int i = 2; i < TILE_ANTUTU_4_0_4.length; i++) {
            if (i == 8 || i == 9) {
                benchmarkDetail[i] = getObjectByBrotherText(TILE_ANTUTU_4_0_4[i] + ":", 1).getText()
                    .substring(getObjectByBrotherText(TILE_ANTUTU_4_0_4[i] + ":", 1).getText().indexOf("]") + 1)
                    .replaceAll("\\s*", "");
            } else {
                benchmarkDetail[i] = getObjectByBrotherText(TILE_ANTUTU_4_0_4[i] + ":", 1).getText();
            }
        }
        util.writeData(benchmarkDetail, PATH_ANTUTU_4_0_4, TILE_ANTUTU_4_0_4);

        try {
            getUiDevice().setOrientationNatural();
        } catch (RemoteException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        sleep(WAIT_TIME);
    }
}
