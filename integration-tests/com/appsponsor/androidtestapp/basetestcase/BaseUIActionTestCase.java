package com.appsponsor.androidtestapp.basetestcase;

import android.app.Activity;
import android.view.View;

import com.robotium.solo.Condition;

public class BaseUIActionTestCase extends BaseUITestCase
{
    private PopupAdListenerState listenerState;

    @Override
    public void setUp() throws Exception
    {
        super.setUp();

        this.listenerState = new PopupAdListenerState(false);
    }

    @Override
    public void willDisappear(DisappearReason reason)
    {
        this.listenerState.setAdWillDisappear(true);
    }

    @Override
    public void willAppear()
    {
        this.listenerState.setAdWillAppear(true);
    }

    @Override
    public void didCacheInterstitial()
    {
        this.listenerState.setAdDidCacheInterstitial(true);
    }

    @Override
    public void onRewardedAdFinished()
    {
        this.listenerState.setAdOnRewardedAdFinished(true);
    }
    
    @Override
    public void popoverDidFailToLoadWithError(Exception exception)
    {
        this.listenerState.setAdPopoverDidFailToLoadWithError(true);
    }

    protected void waitForAdWillDisappear()
    {
        boolean done = this.getSolo().waitForCondition(new Condition()
        {
            @Override
            public boolean isSatisfied()
            {
                return BaseUIActionTestCase.this.listenerState.isAdWillDisappear();
            }
        }, APS_UI_LONG_DELAY_MILLISECONDS);

        assertTrue("willDisappear wasn't called", done);
    }

    protected void waitForAdWillAppearr()
    {
        boolean done = this.getSolo().waitForCondition(new Condition()
        {
            @Override
            public boolean isSatisfied()
            {
                return BaseUIActionTestCase.this.listenerState.isAdWillAppear();
            }
        }, APS_UI_LONG_DELAY_MILLISECONDS);

        assertTrue("willAppearr wasn't called", done);
    }

    protected void waitForAdDidCacheInterstitial()
    {
        boolean done = this.getSolo().waitForCondition(new Condition()
        {
            @Override
            public boolean isSatisfied()
            {
                return BaseUIActionTestCase.this.listenerState.isAdDidCacheInterstitial();
            }
        }, APS_UI_LONG_DELAY_MILLISECONDS);

        assertTrue("didCacheInterstitial wasn't called", done);
    }

    protected void waitForAdOnRewardedAdFinished()
    {
        boolean done = this.getSolo().waitForCondition(new Condition()
        {
            @Override
            public boolean isSatisfied()
            {
                return BaseUIActionTestCase.this.listenerState.isAdOnRewardedAdFinished();
            }
        }, APS_UI_LONG_DELAY_MILLISECONDS);

        assertTrue("rewardedAdFinished wasn't called", done);
    }
    
    protected void waitForAdPopoverDidFailToLoadWithError()
    {
        boolean done = this.getSolo().waitForCondition(new Condition()
        {
            @Override
            public boolean isSatisfied()
            {
                return BaseUIActionTestCase.this.listenerState.isAdPopoverDidFailToLoadWithError();
            }
        }, APS_UI_MEDIUM_DELAY_MILLISECONDS);

        assertTrue("popoverDidFailToLoadWithError wasn't called", done);
    }

    protected void waitForTimeInterval(int time)
    {
        this.getSolo().waitForCondition(new Condition()
        {
            @Override
            public boolean isSatisfied()
            {
                return false;
            }
        }, time);
    }

    protected void waitForAd(int time)
    {
        boolean done = this.getSolo().waitForCondition(new Condition()
        {
            @Override
            public boolean isSatisfied()
            {
                return BaseUIActionTestCase.this.getPopupAd().isReady();
            }
        }, time);
        
        assertTrue("Banner wasn't ready", done);
    }

    protected void waitForCloseButtonToAppear()
    {
        this.waitForTimeInterval(APS_UI_SHORT_DELAY_MILLISECONDS);

        final View closeButton = this.getCloseButton();

        boolean done = this.getSolo().waitForCondition(new Condition()
        {
            @Override
            public boolean isSatisfied()
            {
                return closeButton != null
                        && closeButton.getVisibility() == View.VISIBLE;
            }
        }, APS_UI_LONG_DELAY_MILLISECONDS);
        
        assertTrue("Close button wasn't ready", done);
    }
    
    protected void showAd()
    {
        this.showAd(false);
    }

    protected void showAd(boolean isVoxel)
    {
        this.getPopupAd().load();

        if (isVoxel)
        {
            this.waitForAd(APS_UI_LONG_DELAY_MILLISECONDS);
        }
        else
        {
            this.waitForAdDidCacheInterstitial();
            this.waitForTimeInterval(APS_UI_SHORT_DELAY_MILLISECONDS);
            assertTrue("Ad should be ready", this.getPopupAd().isReady());
        }

        this.getPopupAd().presentAd();

        this.waitForAdWillAppearr();

        this.waitForTimeInterval(APS_UI_SHORT_DELAY_MILLISECONDS);
    }

    protected void closeAd()
    {
        this.waitForTimeInterval(APS_UI_SHORT_DELAY_MILLISECONDS);

        View closeButton = this.getCloseButton();

        assertNotNull("The close button should be", closeButton);
        assertTrue("The close button should be visible",
                closeButton.getVisibility() == View.VISIBLE);

        this.getSolo().clickOnView(closeButton);

        this.waitForTimeInterval(APS_UI_SHORT_DELAY_MILLISECONDS);
    }

    protected void tapOnScreen()
    {
        Activity activity = this.getSolo().getCurrentActivity();
        this.getSolo().clickOnView(activity.getCurrentFocus());
    }
}
