package com.appsponsor.androidtestapp;

import android.app.Activity;

import com.appsponsor.androidtestapp.basetestcase.AdProvider;
import com.appsponsor.androidtestapp.basetestcase.BaseUINetworkTestCase;
import com.appsponsor.appsponsorsdk.activity.PopupAdActivity;
import com.appsponsor.appsponsorsdk.activity.VideoAdActivity;
import com.robotium.solo.Condition;

public class ClickUrlTest extends BaseUINetworkTestCase
{
    public void testClickUrlVideo()
    {
        this.performClickUrlTest(APS_INTERSTITIAL_ZONE_ID,
                APS_SHORT_VAST_COUNTRY_CODE, VideoAdActivity.class);
    }

    public void testClickUrlInterstitial()
    {
        this.performClickUrlTest(APS_INTERSTITIAL_NO_VIDEO_ZONE_ID,
                APS_IMAGE_COUNTRY_CODE, PopupAdActivity.class);
    }
    
    public void testClickUrlWithSeveralAds()
    {
        this.setupRewardedPopupAd(this.getMainActivity(), APS_INTERSTITIAL_ZONE_ID,
                "support@appsponsor.com", APS_SHORT_VAST_COUNTRY_CODE);

        this.showAd();
        
        Activity currentActivity = (Activity) this.getSolo()
                .getCurrentActivity();
        
        String currentClickUrl = AdProvider.getClickUrlForActivity(currentActivity);
        
        this.setupRewardedPopupAd(this.getMainActivity(), APS_INTERSTITIAL_ZONE_ID,
                "support@appsponsor.com", APS_IMAGE_COUNTRY_CODE);

        this.showAd();
        
        this.waitForCloseButtonToAppear();

        this.tapOnScreen();

        this.setClickUrl(currentClickUrl);

        assertNotNull(this.getClickUrl());

        boolean requestDone = getSolo().waitForCondition(new Condition()
        {
            @Override
            public boolean isSatisfied()
            {
                return ClickUrlTest.this.isSentClickUrlRequest();
            }
        }, APS_UI_MEDIUM_DELAY_MILLISECONDS);

        assertTrue("Click url should be sent in request", requestDone);
    }

    private <T extends Activity> void performClickUrlTest(String zoneId,
            String targetingOption, Class<T> activityClass)
    {
        this.setupRewardedPopupAd(this.getMainActivity(), zoneId,
                "support@appsponsor.com", targetingOption);

        this.showAd();

        Activity currentActivity = (Activity) this.getSolo()
                .getCurrentActivity();

        this.waitForCloseButtonToAppear();

        this.tapOnScreen();

        this.setClickUrl(AdProvider.getClickUrlForActivity(currentActivity));

        assertNotNull(this.getClickUrl());

        boolean requestDone = getSolo().waitForCondition(new Condition()
        {
            @Override
            public boolean isSatisfied()
            {
                return ClickUrlTest.this.isSentClickUrlRequest();
            }
        }, APS_UI_MEDIUM_DELAY_MILLISECONDS);

        assertTrue("Click url should be sent in request", requestDone);
    }
}
