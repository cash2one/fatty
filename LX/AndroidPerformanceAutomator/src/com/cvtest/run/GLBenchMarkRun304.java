package com.cvtest.run;

import android.os.RemoteException;
import com.android.uiautomator.core.UiObject;
import com.android.uiautomator.core.UiObjectNotFoundException;
import com.android.uiautomator.core.UiSelector;
import com.cvtest.utils.Automator;

public class GLBenchMarkRun304 extends Automator {
    private final String PATH_GLBENCH = "GLBench";
    private final String PKG_GLBENCH = "com.glbenchmark.glbenchmark27";
    private final String ACT_GLBENCH = "com.glbenchmark.activities.GLBenchmarkDownloaderActivity";
    private final String[] TILE_GLBENCH = {
        "Time",
        "GFXBench 2.7 T-Rex HD C24Z16 Offscreen (fps)",
        "GFXBench 2.7 T-Rex HD C24Z16 Onscreen (fps)",
        "GFXBench 2.7 T-Rex HD C24Z24MS4 Onscreen (fps)",
        "GFXBench 2.7 T-Rex HD C24Z16 Offscreen Fixed timestep (fps)",
        "GFXBench 2.7 T-Rex HD C24Z16 Onscreen Fixed timestep (fps)",
        "GFXBench 2.5 Egypt HD C24Z16 Offscreen (fps)",
        "GFXBench 2.5 Egypt HD C24Z16 Oncreen(fps)",
        "Fill rate C24Z16 Offscreen (texels/sec)",
        "Fill rate C24Z16 Oncreen (texels/sec)",
        "Triangle throughput: Textured C24Z16 Offscreen (triangle/sec)",
        "Triangle throughput: Textured C24Z16 Onscreen (triangle/sec)",
        "Triangle throughput: Textured C24Z16 Offscreen Vertex Lit (triangle/sec)",
        "Triangle throughput: Textured C24Z16 Onscreen Vertex Lit (triangle/sec)",
        "Triangle throughput: Textured C24Z16 Offscreen Fragment Lit (triangle/sec)",
        "Triangle throughput: Textured C24Z16 Onscreen Fragment Lit (triangle/sec)" 
    };

    private final String STR_DOWNLOAD_GLBENCH = "Downloading resources";
    private final String STR_DOWNLOAD_FAILED_GLBENCH = "Download failed because the resources could not be found";
    private final String STR_DOWNLOAD_RETRY_GLBENCH = "Retry Download";
    private final String STR_NEXT_GLBENCH = "Next";
    private final String STR_SKIP_GLBENCH = "Skip";
    private final String STR_PERFORMANCE_TESTS_GLBENCH = "Performance Tests";
    private final String STR_ALL_GLBENCH = "All";
    private final String STR_START_GLBENCH = "Start";
    private final String STR_RESULTS_GLBENCH = "Results";

    public void testRunGLBenchMark() throws UiObjectNotFoundException,
            RemoteException {
        launchAppByPackage(PKG_GLBENCH, ACT_GLBENCH);

        if (isExistObjectwithText("Accept")) {
            clickByText("Accept");
        }
        
        while (isExistObjectwithText(STR_DOWNLOAD_GLBENCH)) {
            sleep(WAIT_TIME * 5);
        }

        int retry = 0;
        while (isExistObjectwithText(STR_DOWNLOAD_FAILED_GLBENCH)) {
            if (retry == 5) {
                break;
            }
            clickByText(STR_DOWNLOAD_RETRY_GLBENCH);
            sleep(WAIT_TIME * 5);
            retry++;
        }
        if (isExistObjectwithText(STR_DOWNLOAD_FAILED_GLBENCH)) {
            System.out.println("Download data failed!");
            System.exit(-1);
        }

        sleep(WAIT_TIME * 3);
        if (isExistObjectwithText(STR_AGREE)) {
            clickByText(STR_AGREE);
        }
        clickByText(STR_NEXT_GLBENCH);
        clickByText(STR_SKIP_GLBENCH);
        clickByText(STR_CAPITAL_OK);
        clickByText(STR_PERFORMANCE_TESTS_GLBENCH);
        clickByText(STR_ALL_GLBENCH);
        clickByText(STR_START_GLBENCH);
        while (!isExistObjectwithText(STR_RESULTS_GLBENCH)) {
            sleep(WAIT_TIME * 20);
        }

        if (getUiDevice().getDisplayHeight() < getUiDevice().getDisplayWidth())
            getUiDevice().setOrientationRight();
        sleep(WAIT_TIME);

        UiObject uiObject = new UiObject(
                new UiSelector().className(STR_LIST_VIEW));
        String[] benchmarkDetail = new String[TILE_GLBENCH.length];
        benchmarkDetail[0] = util.getCurrentTime(1);
        for (int i = 0; i < TILE_GLBENCH.length - 1; i++) {
            if (i == 2 || i > 6) {
                benchmarkDetail[i + 1] = uiObject.getChild(new UiSelector().index(i))
                    .getChild(new UiSelector().index(0))
                    .getChild(new UiSelector().index(1))
                    .getChild(new UiSelector().index(0)).getText()
                    .replaceAll("\\D", "");
            } else {
                benchmarkDetail[i + 1] = uiObject.getChild(new UiSelector().index(i))
                    .getChild(new UiSelector().index(0))
                    .getChild(new UiSelector().index(1))
                    .getChild(new UiSelector().index(1)).getText()
                    .replaceAll("\\D", "");
            }
        }

        util.writeData(benchmarkDetail, PATH_GLBENCH, TILE_GLBENCH);
        getUiDevice().setOrientationNatural();
        sleep(WAIT_TIME);
    }
}
