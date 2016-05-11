package com.cvtest.run;

import java.io.File;
import java.io.IOException;

import com.android.uiautomator.core.UiObjectNotFoundException;
import com.cvtest.utils.Automator;

public class ThreeDMarkRun extends Automator {

    private final String PATH_3DMARK = "3DMark";
    private final String PKG_3DMARK = "com.futuremark.dmandroid.application";
    private final String ACT_3DMARK = "com.futuremark.dmandroid.application.activity.MainActivity";
    private final String[] TILE_3DMARK = { 
        "Time", "Ice storm score", "Graphics score", "Physics score", 
        "Graphics test1(FPS)", "Graphics test2(FPS)", "Physics test(FPS)", 
        "Demo(FPS)", "[Extreme]Ice storm score", "[Extreme]Graphics score", 
        "[Extreme]Physics score", "[Extreme]Graphics test1(FPS)",
        "[Extreme]Graphics test2(FPS)", "[Extreme]Physics test(FPS)", "[Extreme]Demo(FPS)" 
    };
    private final String STR_DOWLOAD_FAIL_3DMARK = "Download failed";
    private final String STR_RESUME_DOWLOAD_3DMARK = "Resume Download";
    private final String STR_CMD_3DMARK = "chmod -R 777 /data/data/com.futuremark.dmandroid.application/";
    
    public void testRunThreeDMark(String[] args) throws UiObjectNotFoundException {
        try {
            Runtime.getRuntime().exec("su");
            Runtime.getRuntime().exec(STR_CMD_3DMARK);
            Runtime.getRuntime().exec("rm -rf /data/data/com.futuremark.dmandroid.application/files/*");
        } catch (IOException e) {
            System.out.println("Cannot get root permission!");
            System.exit(1);
        }

        getUiDevice().pressHome();
        sleep(WAIT_TIME);
        launchAppByPackage(PKG_3DMARK, ACT_3DMARK);
        while (getObjectByClassName(STR_PROCESS_BAR).exists()) {
            if (getObjectByStartText(STR_DOWLOAD_FAIL_3DMARK).exists()) {
                clickByText(STR_RESUME_DOWLOAD_3DMARK);
            }
            sleep(WAIT_TIME * 2);
        }
        sleep(WAIT_TIME * 2);

        // Run Ice Storm
        System.out.println("runing---");
        getUiDevice().click(1305, 645);
        System.out.println("runed---");
        sleep(WAIT_TIME * 360);

        // Run Ice Storm Extreme
        sleep(WAIT_TIME * 600);
        getUiDevice().pressBack();
        System.out.println("runing---");
        getUiDevice().click(1305, 785);
        System.out.println("runed---");
        sleep(WAIT_TIME * 360);
        sleep(WAIT_TIME);

        // handle the result
        String[] benchmarkDetail = new String[15];
        benchmarkDetail[0] = util.getCurrentTime(1);
        int count_benchmarkDetail = 1;
        File file = new File("/data/data/com.futuremark.dmandroid.application/files");
        File[] files = file.listFiles();
        for (int i = 0; i < files.length; i++) {
            if (!files[i].isDirectory() && !files[i].isHidden()) {
                for (String key : util.get3DMarkData(files[i].toString())) {
                    benchmarkDetail[count_benchmarkDetail] = key;
                    count_benchmarkDetail++;
                    System.out.println(key);
                }
            }
        }

        // write data
        util.writeData(benchmarkDetail, PATH_3DMARK, TILE_3DMARK);
    }
}
