package com.appsponsor.androidtestapp;

import com.appsponsor.androidtestapp.basetestcase.BaseUIActionTestCase;


public class NoAdDataTest extends BaseUIActionTestCase
{
    private static final String APS_NO_COUNTRY_CODE = "NOCODE";
    public void testNoAdDataVideo()
    {
        this.setupRewardedPopupAd(this.getMainActivity(), APS_INTERSTITIAL_REWARDED_ZONE_ID,
                "support@appsponsor.com", APS_NO_COUNTRY_CODE);
        this.getPopupAd().load();
        
        this.waitForTimeInterval(APS_UI_MEDIUM_DELAY_MILLISECONDS);
        this.waitForAdPopoverDidFailToLoadWithError();
    }
    
    public void testNoAdDataImage()
    {
        this.setupPopupAd(this.getMainActivity(),
                APS_INTERSTITIAL_NO_VIDEO_ZONE_ID, APS_NO_COUNTRY_CODE);
        this.getPopupAd().load();
        
        this.waitForTimeInterval(APS_UI_MEDIUM_DELAY_MILLISECONDS);
        this.waitForAdPopoverDidFailToLoadWithError();
    }
}
