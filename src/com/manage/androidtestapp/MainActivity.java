package com.manage.androidtestapp;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.manage.managesdk.InterstitialAd;
import com.manage.managesdk.InterstitialAdListener;
import com.manage.managesdk.RewardedAd;

public class MainActivity extends Activity
{
    private static final String TAG_APS_DEMO_APP = "ManageDemoApp";

	private InterstitialAd interstitialAd;
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
		this.interstitialAd = new InterstitialAd(this, "oIs29VQKIa2IfaA4FWkEqw");
		this.interstitialAd.setInterstitialAdListener(this
				.getInterstitialAdListener(this.interstitialAd));
				
        // Simple Rewarded Ad setup
        this.rewardedAd = new RewardedAd(this, "82lEvN030_0zL0kShgS_hw", "support@appsponsor.com");
		this.rewardedAd.setInterstitialAdListener(this
				.getInterstitialAdListener(this.rewardedAd));
    }
    /**
     * Creates Ad listener which we are using as callback for Ad related events.
     * 
     * @param ad - ad we are going to "listen" to
     * @return new instance set up to listen provided ad 
     */
	private InterstitialAdListener getInterstitialAdListener(
			final InterstitialAd ad) {
        return new InterstitialAdListener()
        {
            @Override
            public void willDisappear(DisappearReason reason)
            {
                Log.d(TAG_APS_DEMO_APP, "interstitialAd willDisappear " + reason.name());
            }

            @Override
            public void willAppear()
            {
                Log.d(TAG_APS_DEMO_APP, "interstitialAd willAppear");
            }

            @Override
            public void popoverDidFailToLoadWithError(Exception exception)
            {
                Log.d(TAG_APS_DEMO_APP, "interstitialAd popoverDidFailToLoadWithError: ", exception);
            }

            @Override
            public void onRewardedAdFinished()
            {
                Log.d(TAG_APS_DEMO_APP, "interstitialAd onRewardedAdFinished " + ad.rewardedAdStatus());
            }

            @Override
            public void didCacheInterstitial()
            {
                Log.d(TAG_APS_DEMO_APP, "interstitialAd didCacheInterstitial");
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
                interstitialAd.load();
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
                interstitialAd.presentAd();
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
            	interstitialAd.loadAndPresentAd();
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
        this.interstitialAd.release();
        this.rewardedAd.release();
    }
}
