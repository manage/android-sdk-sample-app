package com.appsponsor.androidtestapp;

import java.util.concurrent.TimeUnit;

import android.content.res.Configuration;
import android.util.Log;
import android.view.View;

import com.appsponsor.androidtestapp.basetestcase.BaseUIActionTestCase;

public class PopupAdVideoTest extends BaseUIActionTestCase
{
    private long rewardedAdFinishedTimeMilliseconds = 0;

    @Override
    public void onRewardedAdFinished()
    {
        super.onRewardedAdFinished();

        PopupAdVideoTest.this.rewardedAdFinishedTimeMilliseconds = System.currentTimeMillis();
    }

    public void testShortVASTViewEnable() throws InterruptedException, ClassNotFoundException
    {
        this.setupRewardedPopupAd(this.getMainActivity(), APS_INTERSTITIAL_REWARDED_ZONE_ID,
                "support@appsponsor.com", APS_SHORT_VAST_COUNTRY_CODE);

        this.rewardedAdFinishedTimeMilliseconds = 0;

        this.showAd();

        long startTime = System.currentTimeMillis();

        this.waitForCloseButtonToAppear();

        this.waitForTimeInterval(APS_UI_SHORT_DELAY_MILLISECONDS);

        long endTimeMilliseconds = System.currentTimeMillis();
        long deltaTimeCloseButtonSeconds = TimeUnit.MILLISECONDS.toSeconds(endTimeMilliseconds
                - startTime);
        long deltaTimeVideoViewSeconds = TimeUnit.MILLISECONDS
                .toSeconds(this.rewardedAdFinishedTimeMilliseconds - startTime);

        // TODO: Revise the condition because it doesn't work sometimes
        assertTrue("Video unlock at wrong time",
                this.compareSeconds(deltaTimeCloseButtonSeconds, deltaTimeVideoViewSeconds, 3));

        this.closeAd();
    }

    public void testShortVASTDoubleTap() throws InterruptedException
    {
        this.setupRewardedPopupAd(this.getMainActivity(), APS_INTERSTITIAL_REWARDED_ZONE_ID,
                "support@appsponsor.com", APS_SHORT_VAST_COUNTRY_CODE);

        this.showAd();

        this.tapOnScreen();
        this.tapOnScreen();

        this.waitForAdOnRewardedAdFinished();

        this.closeAd();
    }

    public void testGeneralLogicVAST() throws InterruptedException
    {
        this.setupRewardedPopupAd(this.getMainActivity(), APS_INTERSTITIAL_REWARDED_ZONE_ID,
                "support@appsponsor.com", APS_SHORT_VAST_COUNTRY_CODE);
        this.showAd();

        View closeButton = this.getCloseButton();

        assertNotNull("The close button should be", closeButton);
        assertFalse("The close button should be invisible",
                closeButton.getVisibility() == View.VISIBLE);

        this.waitForAdOnRewardedAdFinished();

        this.closeAd();
    }

    public void testCloseButtonLogicWithLongVAST() throws InterruptedException
    {
        this.setupRewardedPopupAd(this.getMainActivity(), APS_INTERSTITIAL_REWARDED_ZONE_ID,
                "support@appsponsor.com", APS_LONG_VAST_COUNTRY_CODE);

        this.showAd();

        long startTimeMilliseconds = System.currentTimeMillis();

        this.waitForCloseButtonToAppear();

        long endTimeMilliseconds = System.currentTimeMillis();
        long deltaTimeSeconds = TimeUnit.MILLISECONDS.toSeconds(endTimeMilliseconds
                - startTimeMilliseconds);

        assertTrue("The close button should be  " + 5 + " seconds",
                this.compareSeconds(deltaTimeSeconds, 5, 1));

        this.closeAd();
    }

    public void testCloseButtonLogicWithShortVAST() throws InterruptedException
    {
        this.setupRewardedPopupAd(this.getMainActivity(), APS_INTERSTITIAL_REWARDED_ZONE_ID,
                "support@appsponsor.com", APS_SHORT_VAST_COUNTRY_CODE);

        this.showAd();

        long startTimeMilliseconds = System.currentTimeMillis();

        this.waitForCloseButtonToAppear();

        long endTimeMilliseconds = System.currentTimeMillis();
        long deltaTimeSeconds = TimeUnit.MILLISECONDS.toSeconds(endTimeMilliseconds
                - startTimeMilliseconds);

        assertTrue("The close button should appear less than " + 15 + " seconds",
                (deltaTimeSeconds > 0 && deltaTimeSeconds <= 15));

        this.closeAd();
    }

    public void testVideoOrientation() throws InterruptedException, ClassNotFoundException
    {
        this.setupRewardedPopupAd(this.getMainActivity(), APS_INTERSTITIAL_REWARDED_ZONE_ID,
                "support@appsponsor.com", APS_SHORT_VAST_COUNTRY_CODE);

        this.rewardedAdFinishedTimeMilliseconds = 0;

        this.showAd();

        this.waitForTimeInterval(APS_UI_SHORT_DELAY_MILLISECONDS);

        Log.d("aasdasdsa", String.valueOf(this.getMainActivity().getResources().getConfiguration().orientation));
        assertTrue("Orientation should be in landscape mode", this.getMainActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE);
    }

    protected boolean compareSeconds(long firstTime, long secondTime, long accuracy)
    {
        return (firstTime >= (secondTime - accuracy) && firstTime <= (secondTime + accuracy));
    }
}
