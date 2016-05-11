package com.cvtest.utils;

import java.io.IOException;
import com.android.uiautomator.core.UiObject;
import com.android.uiautomator.core.UiObjectNotFoundException;
import com.android.uiautomator.core.UiScrollable;
import com.android.uiautomator.core.UiSelector;
import com.android.uiautomator.testrunner.UiAutomatorTestCase;

/**
 * 
 * @author ShaneStevenRain
 * 
 */
public class Automator extends UiAutomatorTestCase {
    protected final double VERSION = 1.22;
    protected final String STR_CAPITAL_OK = "OK";
    protected final String STR_MIX_CAPITAL_OK = "Ok";
    protected final String STR_CLEAR_DATA = "Clear data";
    protected final String STR_CLEAR_CACHE = "Clear cache";
    protected final String STR_CLEAR_DEFAULTS = "Clear defaults";
    protected final String STR_SETTING_PACKAGE = "com.android.settings";
    protected final String STR_UNABLE_SETTINGS = "Unable to detect Settings";
    protected final String STR_V4_VIEW_PAGE = "android.support.v4.view.ViewPager";
    protected final String STR_LIST_VIEW = "android.widget.ListView";
    protected final String STR_TEXT_VIEW = "android.widget.TextView";
    protected final String STR_SCROLL_VIEW = "android.widget.ScrollView";
    protected final String STR_BUTTON = "android.widget.Button";
    protected final String STR_PROCESS_BAR = "android.widget.ProgressBar";
    protected final String STR_AGREE = "Agree";
    protected final String STR_YES = "Yes";
    protected final int WAIT_TIME = 1000;
    protected Util util = new Util();

    protected void getCurrentVersion() {
        System.out.println("Autoperf Jar Version is " + VERSION + " .");
    }

    protected void launchAppByPackage(String appPackage, String appActivity) {
        getCurrentVersion();
        try {
            Runtime.getRuntime().exec( "am start -a ACTION=android.intent.action.MAIN -n "
                + appPackage + "/" + appActivity);
            sleep(WAIT_TIME * 2);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // click by text
    protected void clickByText(String text) throws UiObjectNotFoundException {
        UiObject uiObject = new UiObject(new UiSelector().text(text));
        uiObject.clickAndWaitForNewWindow();
    }

    // click by text value
    protected void clickByTextValue(String textValue) throws UiObjectNotFoundException {
        UiObject uiObject = new UiObject(new UiSelector().description(textValue));
        uiObject.clickAndWaitForNewWindow();
    }

    // click by class name
    protected void clickByClassname(String classname) throws UiObjectNotFoundException {
        UiObject uiObject = getObjectByClassName(classname);
        uiObject.clickAndWaitForNewWindow();
    }

    // click by class name
    protected void clickByResourceId(String id) throws UiObjectNotFoundException {
        UiObject uiObject = getObjectByResourceId(id);
        uiObject.clickAndWaitForNewWindow();
    }

    protected UiObject getObjectByClassNameAndText(String classname, String text) {
        return new UiObject(new UiSelector().className(classname).text(text));
    }

    // get object by start text
    protected UiObject getObjectByStartText(String text) throws UiObjectNotFoundException {
        return new UiObject(new UiSelector().textStartsWith(text));
    }

    // get object by class name
    protected UiObject getObjectByClassName(String classname) throws UiObjectNotFoundException {
        return new UiObject(new UiSelector().className(classname));
    }

    // get object by text
    protected UiObject getObjectByText(String text) throws UiObjectNotFoundException {
        return new UiObject(new UiSelector().text(text));
    }

    // get object by contain text
    protected UiObject getObjectByContainText(String text) throws UiObjectNotFoundException {
        return new UiObject(new UiSelector().textContains(text));
    }

    // get object by contain text
    protected UiObject getObjectByResourceId(String id) throws UiObjectNotFoundException {
        return new UiObject(new UiSelector().resourceId(id));
    }

    // get object by brother contain text
    protected UiObject getObjectByBrotherContainText(String text, int index) throws UiObjectNotFoundException {
        if (!isExistObjectwithText(text)) {
            UiScrollable appViews = new UiScrollable(new UiSelector().scrollable(true));
            scrollTextIntoView(appViews, text);
        }
        UiObject brother = getObjectByContainText(text);
        return brother.getFromParent(new UiSelector().index(index));
    }

    // get object by brother text
    protected UiObject getObjectByBrotherText(String text, int index)
            throws UiObjectNotFoundException {
        if (!isExistObjectwithText(text)) {
            UiScrollable appViews = new UiScrollable(new UiSelector().scrollable(true));
            scrollTextIntoView(appViews, text);
        }
        UiObject brother = getObjectByText(text);
        return brother.getFromParent(new UiSelector().index(index));
    }

    // judge the object whether exist
    protected Boolean isExistObjectwithText(String str) throws UiObjectNotFoundException {
        UiObject textObject = new UiObject(new UiSelector().text(str));
        if (textObject.exists()) {
            return true;
        } else {
            return false;
        }
    }

    // scroll the specifyed text into current view
    protected boolean scrollTextIntoView(UiScrollable uiscro, String text) throws UiObjectNotFoundException {
        while (true) {
            if (isExistObjectwithText(text)) {
                break;
            } else if (!uiscro.scrollForward(uiscro.getMaxSearchSwipes())) {
                if (isExistObjectwithText(text)) {
                    break;
                }
                uiscro.scrollToBeginning(uiscro.getMaxSearchSwipes());
            }
        }
        return true;
    }

    // scroll the specifyed text into current view
    protected boolean scrollResourceIdIntoView(UiScrollable uiscro, String resourceId) throws UiObjectNotFoundException {
        while (true) {
            if (getObjectByResourceId(resourceId).exists()) {
                break;
            } else if (!uiscro.scrollForward(uiscro.getMaxSearchSwipes())) {
                if (getObjectByResourceId(resourceId).exists()) {
                    break;
                }
                uiscro.scrollToBeginning(uiscro.getMaxSearchSwipes());
            }
        }
        return true;
    }
}
