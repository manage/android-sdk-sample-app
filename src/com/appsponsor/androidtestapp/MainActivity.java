package com.appsponsor.androidtestapp;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.appsponsor.appsponsorsdk.PopupAd;
import com.appsponsor.appsponsorsdk.PopupAdListener;
import com.appsponsor.appsponsorsdk.RewardedAd;

public class MainActivity extends Activity
{
    private static final String TAG_APS_DEMO_APP = "AppsponsorDemoApp";

    private PopupAd popupAd;
    private RewardedAd rewardedAd;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        // Initial activity setup. Let us make it full-screen
        this.requestWindowFeature(android.view.Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.setContentView(R.layout.activity_main);

        this.setupAds();
        this.setupAdControls();
    }
    
    /**
     * Initializes ads we are going to use
     */
    private void setupAds()
    {
        // Simple Popup Ad setup
        this.popupAd = new PopupAd(MainActivity.this, "oIs29VQKIa2IfaA4FWkEqw");
        this.popupAd.setPopupAdListener(this.getPopupAdListener(this.popupAd));
        // Simple Rewarded Ad setup
        this.rewardedAd = new RewardedAd(this, "82lEvN030_0zL0kShgS_hw", "support@appsponsor.com");
        this.rewardedAd.setPopupAdListener(this.getPopupAdListener(this.rewardedAd));
    }
    /**
     * Creates Ad listener which we are using as callback for Ad related events.
     * 
     * @param ad - ad we are going to "listen" to
     * @return new instance set up to listen provided ad 
     */
    private PopupAdListener getPopupAdListener(final PopupAd ad)
    {
        return new PopupAdListener()
        {
            @Override
            public void willDisappear(DisappearReason reason)
            {
                Log.d(TAG_APS_DEMO_APP, "popupAd willDisappear " + reason.name());
            }

            @Override
            public void willAppear()
            {
                Log.d(TAG_APS_DEMO_APP, "popupAd willAppear");
            }

            @Override
            public void popoverDidFailToLoadWithError(Exception exception)
            {
                Log.d(TAG_APS_DEMO_APP, "popupAd popoverDidFailToLoadWithError: ", exception);
            }

            @Override
            public void onRewardedAdFinished()
            {
                Log.d(TAG_APS_DEMO_APP, "popupAd onRewardedAdFinished " + ad.rewardedAdStatus());
            }

            @Override
            public void didCacheInterstitial()
            {
                Log.d(TAG_APS_DEMO_APP, "popupAd didCacheInterstitial");
            }
        };
    }
    
    /**
     * Configures application button listeners
     */
    private void setupAdControls()
    {
        this.getButtonWithId(R.id.btn_load_popup).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                popupAd.load();
            }
        });
        this.getButtonWithId(R.id.btn_load_rewarded).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                rewardedAd.load();
            }
        });

        this.getButtonWithId(R.id.btn_show_popup).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                popupAd.presentAd();
            }
        });

        this.getButtonWithId(R.id.btn_show_rewarded).setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                rewardedAd.presentAd();
            }
        });
 
        this.getButtonWithId(R.id.btn_load_show_popup).setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
            	popupAd.loadAndPresentAd();
            }
        });

        this.getButtonWithId(R.id.btn_load_show_rewarded).setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                rewardedAd.loadAndPresentAd();
            }
        });
    }
    
    private Button getButtonWithId(int buttonId)
    {
        return (Button) this.findViewById(buttonId);
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        // Releasing ads
        this.popupAd.release();
        this.rewardedAd.release();
    }
}
