package com.appsponsor.androidtestapp.basetestcase;

import java.util.List;

import android.content.Context;
import android.test.ActivityInstrumentationTestCase2;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.appsponsor.androidtestapp.MainActivity;
import com.appsponsor.appsponsorsdk.PopupAd;
import com.appsponsor.appsponsorsdk.PopupAdListener;
import com.appsponsor.appsponsorsdk.RewardedAd;
import com.robotium.solo.Solo;

public class BaseUITestCase extends
        ActivityInstrumentationTestCase2<MainActivity> implements PopupAdListener
{
    protected static final int APS_UI_SHORT_DELAY_MILLISECONDS = 3000;
    protected static final int APS_UI_MEDIUM_DELAY_MILLISECONDS = 20000;
    protected static final int APS_UI_LONG_DELAY_MILLISECONDS = 99999;

    protected static final String APS_INTERSTITIAL_ZONE_ID = "oojkVOpRoOZWCJ0Oa3C3cg";
    protected static final String APS_INTERSTITIAL_NO_VIDEO_ZONE_ID = "o-A1gwTPEbn46lX51iHALw";
    protected static final String APS_INTERSTITIAL_REWARDED_ZONE_ID = "HPOws25FqCt5jwZkDNSvzw";

    protected static final String APS_VOXEL_COUNTRY_CODE = "CUB";
    protected static final String APS_SHORT_VAST_COUNTRY_CODE = "KHM";
    protected static final String APS_LONG_VAST_COUNTRY_CODE = "ARG";
    protected static final String APS_IMAGE_COUNTRY_CODE = "DMA";

    private MainActivity mainActivity;
    private PopupAd popupAd;
    private Solo solo;

    public BaseUITestCase()
    {
        super(MainActivity.class);
    }

    @Override
    protected void setUp() throws Exception
    {
        super.setUp();

        this.mainActivity = getActivity();
        assertNotNull("MainActivity is null", this.mainActivity);

        this.solo = new Solo(getInstrumentation(), this.getMainActivity());
    }
    
    @Override
    public void willDisappear(DisappearReason reason)
    {
        // Call when ad will disappear
    }

    @Override
    public void willAppear()
    {
        // Call when ad will appear
    }

    @Override
    public void didCacheInterstitial()
    {
        // Call when ad cached
    }

    @Override
    public void popoverDidFailToLoadWithError(Exception exception)
    {
        assertTrue("Received exception" + exception.getLocalizedMessage(),
                false);
    }

    @Override
    public void onRewardedAdFinished()
    {
        // Call when rewarded ad finished
    }

    public void setupPopupAd(Context context, String zoneId, String country)
    {
        this.setupAd(new PopupAd(context, zoneId), country);
    }

    public void setupRewardedPopupAd(Context context, String zoneId,
            String userId, String country)
    {
        this.setupAd(new RewardedAd(context, zoneId, userId), country);
    }

    private void setupAd(PopupAd ad, String country)
    {
        ad.setCountry(country);
        ad.setPopupAdListener(this);

        this.popupAd = ad;
    }

    public MainActivity getMainActivity()
    {
        return this.mainActivity;
    }

    public PopupAd getPopupAd()
    {
        return this.popupAd;
    }

    public Solo getSolo()
    {
        return this.solo;
    }

    protected View getCloseButton()
    {
        List<View> views = this.getSolo().getCurrentViews();
        View button = null;

        for (View view : views)
        {
            if (view instanceof ImageButton)
            {
                button = view;
                break;
            } 
            else if (view instanceof Button && button == null)
            {
                button = view;
            }
        }

        return button;
    }
}
