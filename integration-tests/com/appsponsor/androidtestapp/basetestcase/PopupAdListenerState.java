package com.appsponsor.androidtestapp.basetestcase;

public class PopupAdListenerState
{
    private boolean isAdWillDisappear;
    private boolean isAdWillAppear;
    private boolean isAdDidCacheInterstitial;
    private boolean isAdOnRewardedAdFinished;
    private boolean isAdPopoverDidFailToLoadWithError;

    public PopupAdListenerState(boolean initialValue)
    {
        this.setAdWillAppear(initialValue);
        this.setAdWillDisappear(initialValue);
        this.setAdDidCacheInterstitial(initialValue);
        this.setAdOnRewardedAdFinished(initialValue);
        this.setAdPopoverDidFailToLoadWithError(initialValue);
    }

    public boolean isAdWillDisappear()
    {
        return isAdWillDisappear;
    }

    public void setAdWillDisappear(boolean isAdWillDisappear)
    {
        this.isAdWillDisappear = isAdWillDisappear;
    }

    public boolean isAdWillAppear()
    {
        return isAdWillAppear;
    }

    public void setAdWillAppear(boolean isAdWillAppear)
    {
        this.isAdWillAppear = isAdWillAppear;
    }

    public boolean isAdDidCacheInterstitial()
    {
        return isAdDidCacheInterstitial;
    }

    public void setAdDidCacheInterstitial(boolean isAdDidCacheInterstitial)
    {
        this.isAdDidCacheInterstitial = isAdDidCacheInterstitial;
    }

    public boolean isAdOnRewardedAdFinished()
    {
        return isAdOnRewardedAdFinished;
    }

    public void setAdOnRewardedAdFinished(boolean isAdOnRewardedAdFinished)
    {
        this.isAdOnRewardedAdFinished = isAdOnRewardedAdFinished;
    }

    public boolean isAdPopoverDidFailToLoadWithError()
    {
        return isAdPopoverDidFailToLoadWithError;
    }

    public void setAdPopoverDidFailToLoadWithError(boolean isAdPopoverDidFailToLoadWithError)
    {
        this.isAdPopoverDidFailToLoadWithError = isAdPopoverDidFailToLoadWithError;
    }
    
    
}
