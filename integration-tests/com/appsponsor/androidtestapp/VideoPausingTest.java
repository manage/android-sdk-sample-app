package com.appsponsor.androidtestapp;

import android.app.Activity;
import android.app.Instrumentation;
import android.view.View;

import com.appsponsor.androidtestapp.basetestcase.BaseUIActionTestCase;

public class VideoPausingTest extends BaseUIActionTestCase
{
    public void testShortVideoPausing() throws InterruptedException
    {
        this.setupRewardedPopupAd(this.getMainActivity(),
                APS_INTERSTITIAL_REWARDED_ZONE_ID, "support@appsponsor.com",
                APS_SHORT_VAST_COUNTRY_CODE);

        this.showAd();

        Activity currentActivity = (Activity) this.getSolo()
                .getCurrentActivity();

        Instrumentation instrumentation = this.getInstrumentation();

        instrumentation.callActivityOnPause(currentActivity);

        this.waitForTimeInterval(APS_UI_MEDIUM_DELAY_MILLISECONDS);

        instrumentation.callActivityOnResume(currentActivity);

        this.waitForTimeInterval(APS_UI_SHORT_DELAY_MILLISECONDS);

        View closeButton = this.getCloseButton();

        assertNotNull("The close button should be", closeButton);
        assertFalse("The close button should be invisible",
                closeButton.getVisibility() == View.VISIBLE);

        this.waitForAdOnRewardedAdFinished();

        this.closeAd();
    }
}
