package com.appsponsor.androidtestapp;

import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.json.JSONObject;

import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;

import com.appsponsor.appsponsorsdk.net.AdRequestBuilder;
import com.appsponsor.appsponsorsdk.utils.IOUtils;
import com.voxel.sdk.VoxelSDK;

public class BidResponseTest extends ActivityInstrumentationTestCase2<MainActivity>
{
    private static final String APS_TAG = "APS." + BidResponseTest.class.getSimpleName();
    
    private static final String MNG_HOSTNAME = "appsponsor-bidder.doc03.manage.com";
    private static final String MNG_AD_PATH = "2/bid";

    // NOTE: This constant is associated with VAST ad
    private static final String APS_COUNTRY_CODE_CAMBODIA = "KHM";

    private static final String APS_AD_TYPE = "ad_type";
    private static final String APS_VAST_MEDIA_FILE = "vast_media_file";
    private static final String APS_VAST_HTML = "vast_html";
    private static final String APS_VIDEO_STATE_TRACK_75 = "vast_track_url_videoStateTrack75";
    
    // json main objects in bid response
    private static final String APS_WIN_URL = "win_url";
    private static final String APS_CLICK_URL = "click_url";

    // Interstitial Ad Zone 
    private static final String APS_ZONE_ID = "oojkVOpRoOZWCJ0Oa3C3cg";

    private MainActivity mainActivity;

    public BidResponseTest()
    {
        super(MainActivity.class);
    }

    @Override
    protected void setUp() throws Exception
    {
        super.setUp();

        this.mainActivity = getActivity();
        assertNotNull("MainActivity is null", this.mainActivity);
    }
   
    //
    // *************** APS-TC-01-02 ********************
    //      
    public void testResponseHTML()
    {
        assertTrue(this.getResponseByJsonObject("<a href=", 8, "^<a href=$", "html"));        
    }

    public void testResponseWidth()
    {     
        assertTrue(this.getResponseByCustomParam("width", "^[0-9]{1,4}$"));
    }
    
    public void testResponseHeight()
    {     
        assertTrue(this.getResponseByCustomParam("height", "^[0-9]{1,4}$"));
    }
    
    public void testResponseOrientation()
    {     
        assertTrue(this.getResponseByCustomParam("orientation", "^both|portrait|landscape$"));
    }

    public void testResponseDestinationUrl()
    {
        String regExp = "^destination_url\":\"(http:|https)$";
        assertTrue(this.getResponseByFullBody(APS_COUNTRY_CODE_CAMBODIA, "destination_url", 23, regExp));
    }
    
    public void testResponseWinUrlOs()
    {
        assertTrue(this.getResponseByJsonObject("os=", 10, "^os=Android$", APS_WIN_URL));
    }
    
    public void testResponseWinUrlOsv()
    {
        assertTrue(this.getResponseByJsonObject("osv=", 12, "^osv=[0-9]{1,2}.[0-9]{1,2}.[0-9]{1,2}[&a-zA-Z]{0,3}$", APS_WIN_URL));
    }
    
    public void testResponseWinUrlBid()
    {
        assertTrue(this.getResponseByJsonObject("_bid=", 6, "^_bid=[^&]$", APS_WIN_URL));
    }    

    public void testResponseWinUrlEcost1000()
    {
        boolean res = false;

        res = this.getResponseByJsonObject("_ecost1000=", 12, "_ecost1000=[^&]", APS_WIN_URL);

        if (!res)
            res = this.getResponseByJsonObject("_ecost=", 8, "_ecost=[^&]", APS_WIN_URL);

        assertTrue(res);
    }
    
    public void testResponseClickUrlOs()
    {
        assertTrue(this.getResponseByJsonObject("os=", 10, "^os=Android$", APS_CLICK_URL));
    }
    
    public void testResponseClickUrlOsv()
    {
        assertTrue(this.getResponseByJsonObject("osv=", 10, "^osv=[0-9]{1,2}.[0-9]{1,2}.[0-9]{1,2}[&a-zA-Z]{0,3}$", APS_CLICK_URL));
    }   
    
    public void testResponseClickUrlBid()
    {
        assertTrue(this.getResponseByJsonObject("_bid=", 6, "^_bid=[^&]$", APS_CLICK_URL));
    }
    
    public void testResponseClickUrlEcost1000()
    { 
    	boolean res = false;
    	
    	res = this.getResponseByJsonObject("ecost1000=", 11, "ecost1000=[^&]", APS_CLICK_URL);
    	
    	if(!res)
    	{
    		res = this.getResponseByJsonObject("ecost=", 7, "ecost=[^&]", APS_CLICK_URL);
    	}
    		
    	assertTrue(res);
    }

    public void testResponseAdType()
    {
        assertTrue(this.getResponseByCustomParam(APS_AD_TYPE, "^[1|2|3|4|5|6]$"));
    }

    public void testResponseExternalArgsVastMediaFile()
    {
        String regExp = "^vast_media_file\\\\\":\\\\\"http:|http$";
        assertTrue(this.getResponseByFullBody(APS_COUNTRY_CODE_CAMBODIA, APS_VAST_MEDIA_FILE, 25, regExp));
    }

    public void testResponseExternalArgsVastHtml()
    {
        String regExp = "^vast_html\\\\\":\\\\\"<!doctype html$";
        assertTrue(this.getResponseByFullBody(APS_COUNTRY_CODE_CAMBODIA, APS_VAST_HTML, 28, regExp));
    }

    public void testResponseExternalArgsVastTrackUrlVideoStateTrack75()
    {
        String regExp = "^vast_track_url_videoStateTrack75\\\\\":\\\\\"http:|http$";
        assertTrue(this.getResponseByFullBody(APS_COUNTRY_CODE_CAMBODIA, APS_VIDEO_STATE_TRACK_75, 42, regExp));
    }
    
    private HttpPost createAdManager(MainActivity mainActivity, Map<String, String> appInfoPairs, String zoneId)
    {
        Map<String, String> requestParameters = AdRequestBuilder.create(zoneId, mainActivity)
                .withAdOptions(appInfoPairs)
                .withKeywords("")
                .build();
        
        String urlString = String.format("http://%s/%s", MNG_HOSTNAME, MNG_AD_PATH);
        HttpPost post = new HttpPost(urlString);
        try 
        {
            JSONObject jsonRequest = new JSONObject(requestParameters);
            StringEntity jsonContents = new StringEntity(jsonRequest.toString());
            post.setEntity(jsonContents);
        } 
        catch (UnsupportedEncodingException e) 
        {
            // NOTE: ignore exception
        }
        
        return post;
    }

    private Map<String, String> basicInfoPairsVoxel()
    {
        Map<String, String> appInfoPairs = this.basicInfoPairs();
        appInfoPairs.put("voxel", "1");
        return appInfoPairs;
    }

    private Map<String, String> basicInfoPairs()
    {
        Map<String, String> appInfoPairs = new HashMap<String, String>();
        appInfoPairs.put("sub8", "1");
        appInfoPairs.put("vast", "1");
        appInfoPairs.put("nl", String.valueOf(498));
        appInfoPairs.put("voxel_version", VoxelSDK.VERSION);
        appInfoPairs.put("zone_type", "1");
        appInfoPairs.put("nw", "2");
        appInfoPairs.put("dpid.ext", "a9af174f-1968-4eab-bf7c-79920dbb9f51");
        appInfoPairs.put("orient", "p");
        return appInfoPairs;
    }

    private String serverResponseBody(HttpPost post)
    {
        HttpURLConnection urlConnection = null;
        String result = null;

        try
        {
            urlConnection = (HttpURLConnection) post.getURI().toURL().openConnection();
            urlConnection.setConnectTimeout(1000 * 10);
            urlConnection.setDoOutput(true);
            post.getEntity().writeTo(urlConnection.getOutputStream());
            result = IOUtils.toString(urlConnection.getInputStream());
        }
        catch (Exception e)
        {
            Log.e(APS_TAG, e.getLocalizedMessage());
        }
        finally
        {
            if (urlConnection != null)
            {
                urlConnection.disconnect();
            }
        }

        return result;
    }

    private String getSubString(String fullString, String subString, int delta)
    {
        int index = fullString.indexOf(subString);
        int fullStrlength = fullString.length();
        int subStrlength = index + delta;

        if (fullStrlength < subStrlength)
        {
            subStrlength = fullStrlength - index;
        }
        
        return fullString.substring(index, subStrlength);
    }

    // It checks parameter in response body by substring and regular expression
    public boolean getResponseByJsonObject(String subStrValue, int subStrLength, String regExp, String JsonMainObjectName)
    {
        HttpPost httpPost = this.createAdManager(this.mainActivity, this.basicInfoPairs(), APS_ZONE_ID);

        String responseBody = this.serverResponseBody(httpPost);
        boolean res = false;

        try
        {
            JSONObject externalObj = new JSONObject(responseBody);
            String winUrl = externalObj.optString(JsonMainObjectName);
            String param = getSubString(winUrl, subStrValue, subStrLength);
            res = Pattern.matches(regExp, param);
        }
        catch (Exception e)
        {
            Log.e(APS_TAG, e.getLocalizedMessage());
        }

        return res;
    }

    // It checks parameter in response by response body and regular expression
    public boolean getResponseByCustomParam(String paramName, String regExp)
    {
        HttpPost httpPost = this.createAdManager(this.mainActivity, this.basicInfoPairs(), APS_ZONE_ID);

        String responseBody = this.serverResponseBody(httpPost);
        boolean res = false;

        try
        {
            JSONObject externalObj = new JSONObject(responseBody);
            String param = externalObj.optString(paramName);
            res = Pattern.matches(regExp, param);
        }
        catch (Exception e)
        {
            Log.e(APS_TAG, e.getLocalizedMessage());
        }

        return res;
    }

    // It checks parameter in response by substring and regular expression
    public boolean getResponseByFullBody(String country, String subStrValue, int subStrLength, String regExp)
    {
        if (country != null)
        {
            Map<String, String> appInfoPairs = this.basicInfoPairsVoxel();
            appInfoPairs.put("country", country);
        }

        HttpPost httpPost = this.createAdManager(this.mainActivity, this.basicInfoPairs(), APS_ZONE_ID);

        String responseBody = this.serverResponseBody(httpPost);
        boolean res = false;

        try
        {
            JSONObject externalObj = new JSONObject(responseBody);
            String adTypePar = externalObj.toString();
            String param = getSubString(adTypePar, subStrValue, subStrLength);
            res = Pattern.matches(regExp, param);
        }
        catch (Exception e)
        {
            Log.e(APS_TAG, e.getLocalizedMessage());
        }

        return res;
    }
} 