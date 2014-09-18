package com.appsponsor.androidtestapp;

import android.app.Activity;

import com.appsponsor.androidtestapp.basetestcase.AdProvider;
import com.appsponsor.androidtestapp.basetestcase.BaseUINetworkTestCase;
import com.appsponsor.appsponsorsdk.activity.PopupAdActivity;
import com.appsponsor.appsponsorsdk.activity.VideoAdActivity;
import com.robotium.solo.Condition;

public class WinUrlTest extends BaseUINetworkTestCase
{
    public void testWinUrlVideo()
    {
        this.performWinUrlTest(APS_INTERSTITIAL_ZONE_ID,
                APS_SHORT_VAST_COUNTRY_CODE, VideoAdActivity.class);
    }

    public void testWinUrlInterstitial()
    {
        this.performWinUrlTest(APS_INTERSTITIAL_NO_VIDEO_ZONE_ID,
                APS_IMAGE_COUNTRY_CODE, PopupAdActivity.class);
    }

    public void testVideoDisplayRequest()
    {
        this.performWinUrlTest(APS_INTERSTITIAL_ZONE_ID,
                APS_SHORT_VAST_COUNTRY_CODE, VideoAdActivity.class);

        assertNotNull("AbsoluteUrl should be", this.getAbsoluteUrl());
        assertTrue("Request should contain _ecost", this.getAbsoluteUrl()
                .toLowerCase().contains("_ecost"));
    }

    protected <T extends Activity> void performWinUrlTest(String zoneId,
            String targetingOption, Class<T> activityClass)
    {
        this.setupRewardedPopupAd(this.getMainActivity(), zoneId,
                "support@appsponsor.com", targetingOption);

        this.getPopupAd().load();

        this.waitForAd(APS_UI_MEDIUM_DELAY_MILLISECONDS);

        this.getPopupAd().presentAd();

        this.waitForAdWillAppearr();
        
        this.setWinUrl(AdProvider.getWinUrlForActivity((Activity) this
                .getSolo().getCurrentActivity()));

        assertNotNull(this.getWinUrl());

        this.waitForTimeInterval(APS_UI_SHORT_DELAY_MILLISECONDS);

        this.getSolo().assertCurrentActivity("Not valid class for activity",
                activityClass);

        boolean requestDone = getSolo().waitForCondition(new Condition()
        {
            @Override
            public boolean isSatisfied()
            {
                return WinUrlTest.this.isSentWinUrlRequest();
            }
        }, APS_UI_MEDIUM_DELAY_MILLISECONDS);

        assertTrue("Win url should be sent in request", requestDone);
    }
}
