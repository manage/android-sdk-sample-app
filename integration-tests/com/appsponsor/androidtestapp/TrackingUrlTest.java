package com.appsponsor.androidtestapp;

import android.app.Activity;

import com.appsponsor.androidtestapp.basetestcase.AdProvider;
import com.appsponsor.androidtestapp.basetestcase.BaseUINetworkTestCase;
import com.appsponsor.appsponsorsdk.activity.VideoAdActivity;
import com.robotium.solo.Condition;

public class TrackingUrlTest extends BaseUINetworkTestCase
{
    private long adStartTime;
    private long adEndTime;
    
    public void testTrackingUrlVideo()
    {
        this.performTrackingUrlTest(APS_INTERSTITIAL_ZONE_ID,
                APS_SHORT_VAST_COUNTRY_CODE, VideoAdActivity.class);
    }

    private <T extends Activity> void performTrackingUrlTest(String zoneId,
            String targetingOption, Class<T> activityClass)
    {
        this.setupRewardedPopupAd(this.getMainActivity(), zoneId,
                "support@appsponsor.com", targetingOption);

        this.showAd();

        this.getSolo().assertCurrentActivity("Not valid class for activity",
                activityClass);
        Activity currentActivity = (Activity) this.getSolo()
                .getCurrentActivity();
        
        this.setTrackingUrl(AdProvider
                .getTrackingUrlForActivity(currentActivity));

        assertNotNull(this.getTrackingUrl());

        boolean requestDone = getSolo().waitForCondition(new Condition()
        {
            @Override
            public boolean isSatisfied()
            {
                return TrackingUrlTest.this.isSentTrackingUrlRequest();
            }
        }, APS_UI_MEDIUM_DELAY_MILLISECONDS);
        long sendTrackingUrlTime = System.currentTimeMillis();
        
        assertTrue("Tracking url should be sent in request", requestDone);

        this.waitForAdOnRewardedAdFinished();
        
        long trackUrl75percentTime = (this.adEndTime - this.adStartTime) / 100 * 75;
        assertTrue(sendTrackingUrlTime >= this.adStartTime + trackUrl75percentTime && sendTrackingUrlTime < this.adEndTime);

        this.waitForTimeInterval(APS_UI_SHORT_DELAY_MILLISECONDS);
    }
    
    @Override
    public void onRewardedAdFinished()
    {
        super.onRewardedAdFinished();
        this.adEndTime = System.currentTimeMillis();
    }
    
    @Override
    public void willAppear()
    {
        super.willAppear();
        this.adStartTime = System.currentTimeMillis();
    }
}
