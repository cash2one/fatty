package com.cvtest.run;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import com.android.uiautomator.core.UiObjectNotFoundException;
import com.cvtest.utils.Automator;

public class Quake3Run extends Automator {
    private final String PATH_QUAKE3 = "Kwaak3";
    private final String PKG_QUAKE3 = "org.kwaak3";
    private final String ACT_QUAKE3 = "org.kwaak3.Launcher";
    private final String STR_RUN_QUAKE3 = "Run benchmark";
    private final String[] TILE_QUAKE3 = { "Time", "FPS" };

    public void testRunQuak3() throws UiObjectNotFoundException, IOException {
        Process process = null;
        BufferedReader input = null;
        String fps = null;

        Runtime.getRuntime().exec("logcat -c");
        launchAppByPackage(PKG_QUAKE3, ACT_QUAKE3);
        clickByText(STR_RUN_QUAKE3);
        sleep(WAIT_TIME * 30);

        try {
            process = Runtime.getRuntime().exec("logcat -v time -s Quake_DEBUG");
            input = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line = "";
            while ((line = input.readLine()) != null) {
                int count = 0;
                for (String key : line.split(" ")) {
                    ++count;
                    if (key.equals("frames")) {
                        fps = line.split(" ")[count + 2].replaceAll("\\s*", "").replaceAll("[a-zA-Z]", "");
                        process.destroy();
                        break;
                    }
                }
            }
            input.close();
        } catch (IOException e) {
            input.close();
        } finally {
            input.close();
        }

        String[] benchmarkDetail = { util.getCurrentTime(1), fps };
        System.out.println(fps);

        util.writeData(benchmarkDetail, PATH_QUAKE3, TILE_QUAKE3);
        sleep(WAIT_TIME);
    }
}
