package com.appsponsor.androidtestapp;

import org.mockito.Mockito;

import com.appsponsor.androidtestapp.basetestcase.BaseUIActionTestCase;
import com.appsponsor.appsponsorsdk.PopupAdListener;

public class PopupAdTest extends BaseUIActionTestCase
{
    public void testVAST() throws InterruptedException
    {
        PopupAdListener listener = this.getPopupAdListenerMock();

        Mockito.doNothing().when(listener).onRewardedAdFinished();

        this.setupRewardedPopupAd(this.getMainActivity(),
                APS_INTERSTITIAL_REWARDED_ZONE_ID, "support@appsponsor.com",
                APS_SHORT_VAST_COUNTRY_CODE);
        this.getPopupAd().setPopupAdListener(listener);

        this.getPopupAd().load();

        this.waitForAd(APS_UI_MEDIUM_DELAY_MILLISECONDS);

        this.getPopupAd().presentAd();

        this.waitForTimeInterval(APS_UI_MEDIUM_DELAY_MILLISECONDS);

        this.closeAd();

        Mockito.verify(listener).onRewardedAdFinished();

        this.verifyBaseListenerCalls(listener);
    }

    public void testNoRewardedVoxel() throws InterruptedException
    {
        PopupAdListener listener = this.getPopupAdListenerMock();

        this.setupRewardedPopupAd(this.getMainActivity(),
                APS_INTERSTITIAL_NO_VIDEO_ZONE_ID, "support@appsponsor.com",
                APS_VOXEL_COUNTRY_CODE);
        this.getPopupAd().setPopupAdListener(listener);

        this.getPopupAd().load();

        this.waitForAd(APS_UI_MEDIUM_DELAY_MILLISECONDS);

        this.getPopupAd().presentAd();

        this.waitForTimeInterval(APS_UI_MEDIUM_DELAY_MILLISECONDS);

        this.closeAd();

        this.verifyBaseListenerCalls(listener);
    }

    public void testRewardedVoxel() throws InterruptedException
    {
        PopupAdListener listener = this.getPopupAdListenerMock();

        Mockito.doNothing().when(listener).onRewardedAdFinished();

        this.setupRewardedPopupAd(this.getMainActivity(),
                APS_INTERSTITIAL_REWARDED_ZONE_ID, "support@appsponsor.com",
                APS_VOXEL_COUNTRY_CODE);
        this.getPopupAd().setPopupAdListener(listener);

        this.getPopupAd().load();

        this.waitForAd(APS_UI_MEDIUM_DELAY_MILLISECONDS);

        this.getPopupAd().presentAd();

        this.waitForTimeInterval(APS_UI_MEDIUM_DELAY_MILLISECONDS);

        this.closeAd();

        Mockito.verify(listener).onRewardedAdFinished();

        this.verifyBaseListenerCalls(listener);
    }

    public void testImage() throws InterruptedException
    {
        PopupAdListener listener = this.getPopupAdListenerMock();

        this.setupPopupAd(this.getMainActivity(),
                APS_INTERSTITIAL_NO_VIDEO_ZONE_ID, APS_IMAGE_COUNTRY_CODE);
        this.getPopupAd().setPopupAdListener(listener);

        this.getPopupAd().load();

        this.waitForAd(APS_UI_SHORT_DELAY_MILLISECONDS);

        this.getPopupAd().presentAd();

        this.waitForTimeInterval(APS_UI_SHORT_DELAY_MILLISECONDS);

        this.closeAd();

        this.verifyBaseListenerCalls(listener);
    }

    private PopupAdListener getPopupAdListenerMock()
    {
        PopupAdListener mock = Mockito.mock(PopupAdListener.class);

        Mockito.doNothing().when(mock).didCacheInterstitial();
        Mockito.doNothing().when(mock).willAppear();
        Mockito.doNothing().when(mock)
                .willDisappear(DisappearReason.UserClosed);

        return mock;
    }

    private void verifyBaseListenerCalls(PopupAdListener listener)
    {
        Mockito.verify(listener).didCacheInterstitial();
        Mockito.verify(listener).willAppear();
        Mockito.verify(listener).willDisappear(DisappearReason.UserClosed);
    }
}
