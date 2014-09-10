package com.appsponsor.androidtestapp;

import com.appsponsor.androidtestapp.basetestcase.BaseUIActionTestCase;
import com.appsponsor.appsponsorsdk.activity.PopupAdActivity;
import com.appsponsor.appsponsorsdk.activity.VideoAdActivity;

public class BasicAdFlowTest extends BaseUIActionTestCase
{
    public void testBasicVideoAdFlow()
    {
        assertFalse(this.getSolo().getCurrentActivity() instanceof VideoAdActivity);
        
        this.setupRewardedPopupAd(this.getMainActivity(), 
                                  APS_INTERSTITIAL_REWARDED_ZONE_ID,
                                  "support@appsponsor.com",
                                  APS_SHORT_VAST_COUNTRY_CODE);
        
        this.getPopupAd().load();
        
        this.waitForAdDidCacheInterstitial();
        this.waitForTimeInterval(APS_UI_SHORT_DELAY_MILLISECONDS);

        this.getPopupAd().presentAd();

        this.waitForAdWillAppearr();

        this.waitForTimeInterval(APS_UI_SHORT_DELAY_MILLISECONDS);
        assertTrue(this.getSolo().getCurrentActivity() instanceof VideoAdActivity);
        this.waitForCloseButtonToAppear();

        this.closeAd();
        this.waitForAdWillDisappear();
        assertFalse(this.getSolo().getCurrentActivity() instanceof VideoAdActivity);
        
    }
    
    public void testBasicImageAdFlow()
    {
        assertFalse(this.getSolo().getCurrentActivity() instanceof PopupAdActivity);
        this.setupPopupAd(this.getMainActivity(),
                APS_INTERSTITIAL_NO_VIDEO_ZONE_ID, APS_IMAGE_COUNTRY_CODE);
        this.getPopupAd().load();
        
        this.waitForAdDidCacheInterstitial();
        this.waitForTimeInterval(APS_UI_SHORT_DELAY_MILLISECONDS);

        this.getPopupAd().presentAd();

        this.waitForAdWillAppearr();

        this.waitForTimeInterval(APS_UI_SHORT_DELAY_MILLISECONDS);
        assertTrue(this.getSolo().getCurrentActivity() instanceof PopupAdActivity);
        this.waitForCloseButtonToAppear();

        this.closeAd();
        this.waitForAdWillDisappear();
        assertFalse(this.getSolo().getCurrentActivity() instanceof PopupAdActivity);
        
    }
}
