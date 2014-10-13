package com.appsponsor.androidtestapp;

import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.json.JSONObject;

import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;

import com.appsponsor.appsponsorsdk.net.AdRequestBuilder;
import com.appsponsor.appsponsorsdk.utils.IOUtils;
import com.voxel.sdk.VoxelSDK;

public class IdentificationByTest extends ActivityInstrumentationTestCase2<MainActivity>
{
    private static final String APS_TAG = "APS." + IdentificationByTest.class.getSimpleName();
    
    private static final String MNG_HOSTNAME = "appsponsor-bidder.doc03.manage.com";
    private static final String MNG_AD_PATH = "2/bid";

    private static final String APS_GPS_CUBA_LONGITUDE = "79.5000";
    private static final String APS_GPS_CUBA_LATITUDE = "22.0000";

    // NOTE: This constant should be updated with country code, where tests are
    // running
    private static final String APS_COUNTRY_CODE_TEST = "UKR";

    // NOTE: This constant is associated with Interstitial ad
    private static final String APS_COUNTRY_CODE_DOMINICA = "DMA";

    // NOTE: This constant is associated with Voxel Ad
    private static final String APS_COUNTRY_CODE_CUBA = "CUB";

    // NOTE: This constant is associated with VAST ad
    private static final String APS_COUNTRY_CODE_CAMBODIA = "KHM";

    private static final String APS_COUNTRY = "country";
    
    // json main objects in bid response
    private static final String APS_WIN_URL = "win_url";    

    // Interstitial Ad Zone 
    private static final String APS_ZONE_ID = "oojkVOpRoOZWCJ0Oa3C3cg";

    private MainActivity mainActivity;

    public IdentificationByTest()
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
    // *************** APS-TC-05-01 ********************
    //
    // SDK instance without hardcoded property country receives ads targeted by IP
    public void testIdentificationByCountryInterstitial()
    {
        String winUrl = this.getWinUrl(APS_COUNTRY_CODE_DOMINICA, false);
        assertNotNull(winUrl);
        assertTrue(winUrl.contains("country=" + APS_COUNTRY_CODE_DOMINICA));
    }

    public void testIdentificationByCountryVAST()
    {
        String winUrl = this.getWinUrl(APS_COUNTRY_CODE_CAMBODIA, false);
        assertNotNull(winUrl);
        assertTrue(winUrl.contains("country=" + APS_COUNTRY_CODE_CAMBODIA));
    }

    public void testIdentificationByCountryVoxel()
    {
        String winUrl = this.getWinUrl(APS_COUNTRY_CODE_CUBA, true);
        assertNotNull(winUrl);
        assertTrue(winUrl.contains("country=" + APS_COUNTRY_CODE_CUBA));
    }

    //
    // *************** APS-TC-05-02 ********************
    //
    // SDK instance with hardcoded property country receives ads for this
    // country, disregarding of actual IP
    public void testIdentificationByIP()
    {
        String winUrl = this.getWinUrl(null, false);
        assertNotNull(winUrl);
        assertTrue(winUrl.contains("country=" + APS_COUNTRY_CODE_TEST));
    }

    //
    // *************** APS-TC-05-03 ********************
    //
    // SDK instance with hardcoded property country receives ads for this
    // country, disregarding of actual GPS
    public void testIdentificationByGPS()
    {
        Map<String, String> appInfoPairs = new HashMap<String, String>();
        appInfoPairs.put("lat", APS_GPS_CUBA_LATITUDE);
        appInfoPairs.put("lon", APS_GPS_CUBA_LONGITUDE);

        String winUrl = this.getRequestWinUrl(null, true, appInfoPairs);
        assertNotNull(winUrl);
        assertTrue(winUrl.contains("country=" + APS_COUNTRY_CODE_CUBA));
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

    // It checks parameter in request by parameter name
    private String getRequestWinUrl(String country, boolean isVoxel, Map<String, String> additionalParams)
    {
        Map<String, String> appInfoPairs = null;

        if (isVoxel)
        {
            appInfoPairs = this.basicInfoPairsVoxel();
        }
        else
        {
            appInfoPairs = this.basicInfoPairs();
        }

        if (country != null)
        {
            appInfoPairs.put(APS_COUNTRY, country);
        }

        // Check addition request parameters
        if (additionalParams != null)
        {
            appInfoPairs.putAll(additionalParams);
        }

        HttpPost httpPost = this.createAdManager(this.mainActivity, appInfoPairs, APS_ZONE_ID);
        String result = this.serverResponseBody(httpPost);
        String winUrl = null;

        try
        {
            JSONObject externalObj = new JSONObject(result);
            winUrl = externalObj.optString(APS_WIN_URL);
        }
        catch (Exception e)
        {
            Log.e(APS_TAG, e.getLocalizedMessage());
        }

        return winUrl;
    }

    private String getWinUrl(String country, boolean isVoxel)
    {
        return this.getRequestWinUrl(country, isVoxel, null);
    }   
} 