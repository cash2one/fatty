package com.cvtest.run;

import java.io.File;
import java.io.IOException;
import com.android.uiautomator.core.UiObjectNotFoundException;
import com.android.uiautomator.core.UiScrollable;
import com.android.uiautomator.core.UiSelector;
import com.cvtest.utils.Automator;

public class VellamoRun extends Automator {
    private final String PATH_VELLAMO = "Vellamo";
    private final String PATH_VELLAMO_HTML5 = "/data/data/com.quicinc.vellamo/files/html5_results.html";
    private final String PATH_VELLAMO_METAL = "/data/data/com.quicinc.vellamo/files/metal_results.html";
    private final String PATH_VELLAMO_HTML5_TMP = "/mnt/sdcard/html5_results.html";
    private final String PATH_VELLAMO_METAL_TMP = "/mnt/sdcard/metal_results.html";
    private final String PKG_VELLAMO = "com.quicinc.vellamo";
    private final String ACT_VELLAMO = "com.quicinc.vellamo.VellamoActivity";

    private final String[] TILE_VELLAMO = { 
        "Time", "HTML5", "See The Sun Canvas", 
        "Pixel Blender", "Canvas Crossfader", "Aquarium Canvas", 
        "Sun Spider", "V8 Benchmark", "Surf Wax Binder",
        "DOM Node Surfer", "Reflo", "Image Scroller", "Ocean Scroller",
        "Ocean Zoomer", "WebGL Jellyfish", "Inline Video",
        "Load and Reload", "Metal", "dhrystone", "Linpack", "Branch-K",
        "Stream 5.9", "RamJam", "Storage"
    };
    private final String STR_ACCEPT_TILE_VELLAMO = "Vellamo EULA";
    private final String STR_ACCEPT_VELLAMO = "Accept";
    private final String STR_START_VELLAMO = "Run All Chapters";
    private final String STR_EXPLANATION_VELLAMO = "Benchmarks Explanation";
    private final String STR_NO = "No";
    private final String STR_END_TILE_VELLAMO = "Your device's results!";
    private final String STR_CMD_VELLAMO = "chmod -R 777 /data/data/com.quicinc.vellamo/";
    private final String REGEX_VELLAMO = "<span style=\'color:cyan;\'>([0-9]*)</span>";

    public void testRunVellamo() throws UiObjectNotFoundException {
        launchAppByPackage(PKG_VELLAMO, ACT_VELLAMO);

        if (isExistObjectwithText(STR_ACCEPT_TILE_VELLAMO)) {
            clickByText(STR_ACCEPT_VELLAMO);
            sleep(WAIT_TIME);
        }
        UiScrollable uiScrollable = new UiScrollable(new UiSelector().className(STR_V4_VIEW_PAGE).scrollable(true));
        uiScrollable.setAsHorizontalList();
        while (!isExistObjectwithText(STR_START_VELLAMO)) {
            scrollTextIntoView(uiScrollable, STR_START_VELLAMO);
        }
        clickByText(STR_START_VELLAMO);
        if (isExistObjectwithText(STR_EXPLANATION_VELLAMO) && isExistObjectwithText(STR_NO)) {
            clickByText(STR_NO);
        }

        while (!isExistObjectwithText(STR_END_TILE_VELLAMO)) {
            sleep(WAIT_TIME);
        }

        clickByText(STR_NO);

        try {
            Runtime.getRuntime().exec("su");
            Runtime.getRuntime().exec(STR_CMD_VELLAMO);
        } catch (IOException e) {
            System.out.println("Cannot get root permission!");
            System.exit(1);
        }

        File HTML5resultFile = null;
        HTML5resultFile = new File(PATH_VELLAMO_HTML5);

        File SDCARDHTML5resultFile = null;
        SDCARDHTML5resultFile = new File(PATH_VELLAMO_HTML5_TMP);

        File metalresultFile = null;
        metalresultFile = new File(PATH_VELLAMO_METAL);
        File SDCARDmetalresultFile = null;
        SDCARDmetalresultFile = new File(PATH_VELLAMO_METAL_TMP);

        try {
            util.copyFile(HTML5resultFile, SDCARDHTML5resultFile);
        } catch (IOException e) {
            System.out.println("Error to copy HTML5resultFile to sdcard!");
            System.exit(1);
        }
        try {
            util.copyFile(metalresultFile, SDCARDmetalresultFile);
        } catch (IOException e) {
            System.out.println("Error to copy metalresultFile to sdcard!");
            System.exit(1);
        }

        String[] benchmarkDetail = new String[24];
        benchmarkDetail[0] = util.getCurrentTime(1);

        int count_benchmarkDetail = 1;
        for (String key : util.getVellamoData(PATH_VELLAMO_HTML5_TMP,
                REGEX_VELLAMO, 16)) {
            benchmarkDetail[count_benchmarkDetail] = key;
            count_benchmarkDetail++;
        }
        for (String key : util.getVellamoData(PATH_VELLAMO_METAL_TMP, _VELLAMO, 7)) {
            benchmarkDetail[count_benchmarkDetail] = key;
            count_benchmarkDetail++;
        }

        util.writeData(benchmarkDetail, PATH_VELLAMO, TILE_VELLAMO);
        sleep(WAIT_TIME);
    }
}
