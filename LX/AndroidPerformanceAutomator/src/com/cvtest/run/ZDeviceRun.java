package com.cvtest.run;

import com.android.uiautomator.core.UiObjectNotFoundException;
import com.android.uiautomator.core.UiScrollable;
import com.android.uiautomator.core.UiSelector;
import com.cvtest.utils.Automator;

public class ZDeviceRun extends Automator {
    private final String PATH_ZDEVICE = "ZDevice";
    private final String PKG_ZDEVICE = "zausan.zdevicetest";
    private final String ACT_ZDEVICE = "zausan.zdevicetest.zdevicetest";
    private final String[] TILE_ZDEVICE = { 
        "Time", "Main camera", "Secondary camera", 
        "GPS", "Wifi", "Bluetooth", "GSM / UMTS",
        "Accelerometers", "Compass", "Radio", "Screen", 
        "Battery", "CPU", "Sound", "Vibrator", "Microphone", 
        "USB", "Audio / Video Outputs", "Android OS", 
        "Light sensor", "Proximity sensor", "Temperature sensor",
        "Pressure sensor", "Relative humidity", "Flash",
        "NFC", "Ant+", "Gyroscope", "Gravity",
        "Linear acceleration", "Rotation vector" 
    };

    public void testRunZDevice() throws UiObjectNotFoundException {
        launchAppByPackage(PKG_ZDEVICE, ACT_ZDEVICE);
        while (!isExistObjectwithText(STR_MIX_CAPITAL_OK)) {
            sleep(WAIT_TIME);
        }
        clickByText(STR_MIX_CAPITAL_OK);
        sleep(WAIT_TIME);

        String[] benchmarkDetail = new String[31];
        benchmarkDetail[0] = util.getCurrentTime(1);

        UiScrollable uiScrollable = new UiScrollable(new UiSelector().className(STR_SCROLL_VIEW).scrollable(true));

        for (int i = 1; i < TILE_ZDEVICE.length; i++) {
            while (!isExistObjectwithText(TILE_ZDEVICE[i])) {
                scrollTextIntoView(uiScrollable, TILE_ZDEVICE[i]);
            }

            if (getObjectByText(TILE_ZDEVICE[i]).isClickable()) {
                benchmarkDetail[i] = "Yes";
            } else {
                benchmarkDetail[i] = "No";
            }
        }
        util.writeData(benchmarkDetail, PATH_ZDEVICE, TILE_ZDEVICE);
        sleep(WAIT_TIME);
    }
}
