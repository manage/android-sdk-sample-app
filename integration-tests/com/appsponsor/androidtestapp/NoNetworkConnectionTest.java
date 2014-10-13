package com.appsponsor.androidtestapp;

import android.content.Context;
import android.net.wifi.WifiManager;

import com.appsponsor.androidtestapp.basetestcase.BaseUIActionTestCase;

public class NoNetworkConnectionTest extends BaseUIActionTestCase
{
    @Override
    public void setUp() throws Exception
    {
        super.setUp();
        WifiManager wifiManager = (WifiManager)this.getActivity().getSystemService(Context.WIFI_SERVICE);
        wifiManager.setWifiEnabled(false);
    }
    
    @Override
    protected void tearDown() throws Exception
    {
        super.tearDown();
        WifiManager wifiManager = (WifiManager)this.getActivity().getSystemService(Context.WIFI_SERVICE);
        wifiManager.setWifiEnabled(true);
    }
    
    public void testNoConnectionVideo()
    {
        this.setupRewardedPopupAd(this.getMainActivity(), APS_INTERSTITIAL_REWARDED_ZONE_ID,
                "support@appsponsor.com", APS_SHORT_VAST_COUNTRY_CODE);
        this.getPopupAd().load();
        
        this.waitForTimeInterval(APS_UI_MEDIUM_DELAY_MILLISECONDS);
        this.waitForAdPopoverDidFailToLoadWithError();
    }
    
    public void testNoConnectionImage()
    {
        this.setupPopupAd(this.getMainActivity(),
                APS_INTERSTITIAL_NO_VIDEO_ZONE_ID, APS_IMAGE_COUNTRY_CODE);
        this.getPopupAd().load();
        
        this.waitForTimeInterval(APS_UI_MEDIUM_DELAY_MILLISECONDS);
        this.waitForAdPopoverDidFailToLoadWithError();
    }
    
    public void testNoConnectionCloseButton()
    {
        this.setupRewardedPopupAd(this.getMainActivity(), APS_INTERSTITIAL_REWARDED_ZONE_ID,
                "support@appsponsor.com", APS_SHORT_VAST_COUNTRY_CODE);
        WifiManager wifiManager = (WifiManager)this.getActivity().getSystemService(Context.WIFI_SERVICE);
        wifiManager.setWifiEnabled(true);
        this.waitForTimeInterval(APS_UI_MEDIUM_DELAY_MILLISECONDS);
        this.showAd();
        wifiManager.setWifiEnabled(false);
        
        this.waitForCloseButtonToAppear();

        this.closeAd();
    }
}
