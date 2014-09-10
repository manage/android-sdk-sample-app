package com.appsponsor.androidtestapp.basetestcase;

import java.util.HashSet;
import java.util.Set;

import org.jboss.netty.handler.codec.http.HttpRequest;
import org.littleshoot.proxy.HttpRequestFilter;

//NOTE: It is required to setup proxy for current network on mobile device as localhost:26571
public class BaseUINetworkTestCase extends BaseUIActionTestCase implements
        HttpRequestFilter
{
    private static final int APS_PROXY_PORT = 26571;

    private enum UrlEvent
    {
        APS_CLICK_URL, APS_WIN_URL, APS_TRACKING_URL
    }

    private Set<UrlEvent> happenedEvents;
    // NOTE: Based on https://github.com/arielnetworks/LittleProxy
    private org.littleshoot.proxy.HttpProxyServer server;

    private String clickUrl;
    private String winUrl;
    private String trackingUrl;
    private String absoluteUrl = null;

    @Override
    public void setUp() throws Exception
    {
        super.setUp();
        this.happenedEvents = new HashSet<UrlEvent>();
        this.server = new org.littleshoot.proxy.DefaultHttpProxyServer(
                APS_PROXY_PORT, this);
        this.server.start();
    }

    @Override
    public void tearDown() throws Exception
    {
        this.getSolo().finishOpenedActivities();
        this.server.stop();
    }

    public String getTrackingUrl()
    {
        return trackingUrl;
    }

    public void setTrackingUrl(String trackingUrl)
    {
        this.trackingUrl = trackingUrl;
    }

    public String getClickUrl()
    {
        return clickUrl;
    }

    public void setClickUrl(String clickUrl)
    {
        this.clickUrl = clickUrl;
    }

    public String getWinUrl()
    {
        return winUrl;
    }

    public void setWinUrl(String winUrl)
    {
        this.winUrl = winUrl;
    }

    public String getAbsoluteUrl()
    {
        return absoluteUrl;
    }

    @Override
    public void filter(HttpRequest originalRequest)
    {
        String host = originalRequest.getHeader("Host");
        String protocol = originalRequest.getProtocolVersion()
                .getProtocolName().toLowerCase();
        String absoluteUrl = protocol + "://" + host + originalRequest.getUri();

        if (this.isContains(absoluteUrl, this.getWinUrl()))
        {
            assertFalse(this.isSentWinUrlRequest());
            this.happenedEvents.add(UrlEvent.APS_WIN_URL);
        }

        if (this.isContains(absoluteUrl, this.getClickUrl()))
        {
            this.happenedEvents.add(UrlEvent.APS_CLICK_URL);
        }

        if (this.isContains(absoluteUrl, this.getTrackingUrl()))
        {
            this.happenedEvents.add(UrlEvent.APS_TRACKING_URL);
        }

        this.absoluteUrl = absoluteUrl;
    }

    private boolean isContains(String firstString, String secondString)
    {
        return secondString != null && firstString.indexOf(secondString) != -1;
    }

    public boolean isSentWinUrlRequest()
    {
        return this.happenedEvents.contains(UrlEvent.APS_WIN_URL);
    }

    public boolean isSentClickUrlRequest()
    {
        return this.happenedEvents.contains(UrlEvent.APS_CLICK_URL);
    }

    public boolean isSentTrackingUrlRequest()
    {
        return this.happenedEvents.contains(UrlEvent.APS_TRACKING_URL);
    }
}
