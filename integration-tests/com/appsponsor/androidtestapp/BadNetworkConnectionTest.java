package com.appsponsor.androidtestapp;

import java.lang.reflect.Field;

import org.mockito.Mockito;

import android.app.Activity;

import com.appsponsor.androidtestapp.basetestcase.BaseUIActionTestCase;
import com.appsponsor.appsponsorsdk.PopupAdListener;
import com.appsponsor.appsponsorsdk.RewardedAd;
import com.appsponsor.appsponsorsdk.net.LatencyCheckHandler;

public class BadNetworkConnectionTest extends BaseUIActionTestCase
{
    @Override
    public void setUp() throws Exception
    {
        super.setUp();
        System.setProperty("dexmaker.dexcache", getInstrumentation()
                .getTargetContext().getCacheDir().getPath());
    }

    public void testBadNetworkConnectionVideo() throws InterruptedException,
            IllegalArgumentException, NoSuchFieldException,
            IllegalAccessException
    {
        this.performBadNetworkConnectionTest(this.getMainActivity(),
                APS_INTERSTITIAL_REWARDED_ZONE_ID, "support@appsponsor.com",
                APS_SHORT_VAST_COUNTRY_CODE);
    }

    public void testBadNetworkConnectionVoxel() throws InterruptedException,
            IllegalArgumentException, NoSuchFieldException,
            IllegalAccessException
    {
        this.performBadNetworkConnectionTest(this.getMainActivity(),
                APS_INTERSTITIAL_REWARDED_ZONE_ID, "support@appsponsor.com",
                APS_VOXEL_COUNTRY_CODE);
    }

    private void performBadNetworkConnectionTest(Activity activity,
            String zoneId, String userId, String country)
            throws IllegalArgumentException, NoSuchFieldException,
            IllegalAccessException
    {
        this.setupRewardedPopupAd(activity, zoneId, userId, country);

        this.getPopupAd().setPopupAdListener(this.getMockPopupAdListener());
        this.setLatencyCheckHandler(this.getMockLatencyCheckHandler());

        this.getPopupAd().load();

        this.waitForAd(APS_UI_MEDIUM_DELAY_MILLISECONDS);

        this.getPopupAd().presentAd();

        this.waitForTimeInterval(APS_UI_SHORT_DELAY_MILLISECONDS);
    }

    private PopupAdListener getMockPopupAdListener()
    {
        PopupAdListener listener = Mockito.mock(PopupAdListener.class);
        Mockito.doThrow(new RuntimeException("The ad shouldn't be show"))
                .when(listener).willAppear();

        return listener;
    }

    private LatencyCheckHandler getMockLatencyCheckHandler()
    {
        LatencyCheckHandler mock = Mockito.mock(LatencyCheckHandler.class);

        Mockito.when(mock.getLatencyTime()).thenReturn(1000L);
        Mockito.when(mock.isVideoLatencyCheckOkay()).thenReturn(false);

        return mock;
    }

    private void setLatencyCheckHandler(LatencyCheckHandler handler)
            throws NoSuchFieldException, IllegalArgumentException,
            IllegalAccessException
    {
        Class<?> adClass = this.getPopupAd().getClass();

        if (this.getPopupAd() instanceof RewardedAd)
        {
            adClass = this.getPopupAd().getClass().getSuperclass();
        }

        Field field = adClass.getDeclaredField("latencyCheckHandler");
        ;
        field.setAccessible(true);
        field.set(this.getPopupAd(), handler);
    }
}
