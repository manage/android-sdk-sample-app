package com.appsponsor.androidtestapp;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Pattern;

import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.json.JSONObject;

import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;

import com.appsponsor.appsponsorsdk.net.AdRequestBuilder;
import com.appsponsor.appsponsorsdk.utils.IOUtils;
import com.voxel.sdk.VoxelSDK;

public class BidRequestTest extends ActivityInstrumentationTestCase2<MainActivity>
{
    private static final String APS_TAG = "APS." + BidRequestTest.class.getSimpleName();
    
    private static final String MNG_HOSTNAME = "appsponsor-bidder.doc03.manage.com";
    private static final String MNG_AD_PATH = "2/bid";

    // NOTE: This constant is associated with Interstitial ad
    private static final String APS_COUNTRY_CODE_DOMINICA = "DMA";

    // NOTE: This constant is associated with Voxel Ad
    private static final String APS_COUNTRY_CODE_CUBA = "CUB";

    // NOTE: This constant is associated with VAST ad
    private static final String APS_COUNTRY_CODE_CAMBODIA = "KHM";

    private static final String APS_ORIENTATION = "orient";
    private static final String APS_ORIENTATION_VALUE_LANDSCAPE = "l";
    private static final String APS_ORIENTATION_VALUE_PORTRAIT = "p";

    private static final String APS_TIMEZONE = "tz";
    private static final String APS_SESSION_DEPTH = "sd";
    private static final String APS_SDK_VERSION = "sdkv";
    private static final String APS_BUNDLE_DISPLAY = "bdn";
    private static final String APS_ZONE_TYPE = "zone_type";
    private static final String APS_SUB8 = "sub8";
    private static final String APS_OS_VERSION = "osv";
    private static final String APS_BUNDLE_NAME = "bn";
    private static final String APS_OS_NAME = "os";
    private static final String APS_ANDROID_ID = "dpid";
    private static final String APS_NETWORK_LATENCY = "nl";
    private static final String APS_DEVICE_MAKE = "make";
    private static final String APS_VOXEL_VERSION = "voxel_version";
    private static final String APS_NETWORK_PROVIDER_INFO = "nw";
    private static final String APS_BUNDLE_IDENTIFIER = "bi";
    private static final String APS_LANGUAGE = "lang";
    private static final String APS_COUNTRY = "country";
    private static final String APS_HEIGHT = "height";
    private static final String APS_WIDTH = "width";
    private static final String APS_INTERSTITIAL = "instl";   
    private static final String APS_UBID = "ubid";

    // Interstitial Ad Zone 
    private static final String APS_ZONE_ID = "oojkVOpRoOZWCJ0Oa3C3cg";

    private MainActivity mainActivity;

    public BidRequestTest()
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
    // *************** APS-TC-01-01 ********************
    //     
    // JSON ad request contains tz (timezone)
    public void testRequestTzlInterstitial()
    {
        assertTrue(this.getParam(APS_COUNTRY_CODE_DOMINICA, false, APS_TIMEZONE, "^[+-][0-9]{4}$"));
    }

    public void testRequestTzVAST()
    {
        assertTrue(this.getParam(APS_COUNTRY_CODE_CAMBODIA, false, APS_TIMEZONE, "^[+-][0-9]{4}$"));
    }

    public void testRequestTzVoxel()
    {
        assertTrue(this.getParam(APS_COUNTRY_CODE_CUBA, true, APS_TIMEZONE, "^[+-][0-9]{4}$"));
    }

    // JSON ad request contains sd (Session Depth)
    public void testRequestSdInterstitial()
    {
        assertTrue(this.getParam(APS_COUNTRY_CODE_DOMINICA, false, APS_SESSION_DEPTH, "^[0-9]{1,2}$"));
    }

    public void testRequestSdVAST()
    {
        assertTrue(this.getParam(APS_COUNTRY_CODE_CAMBODIA, false, APS_SESSION_DEPTH, "^[0-9]{1,2}$"));
    }

    public void testRequestSdVoxel()
    {
        assertTrue(this.getParam(APS_COUNTRY_CODE_CUBA, true, APS_SESSION_DEPTH, "^[0-9]{1,2}$"));
    }

    // JSON ad request contains sdkv (SDK version)
    public void testRequestSdkvInterstitial()
    {
        assertTrue(this.getParam(APS_COUNTRY_CODE_DOMINICA, false, APS_SDK_VERSION, "^[0-9]{1,2}.[0-9]{1,2}.[0-9]{1,2}|[0-9]{1,2}.[0-9]{1,2}$"));
    }

    public void testRequestSdkvVAST()
    {
        assertTrue(this.getParam(APS_COUNTRY_CODE_CAMBODIA, false, APS_SDK_VERSION, "^[0-9]{1,2}.[0-9]{1,2}.[0-9]{1,2}|[0-9]{1,2}.[0-9]{1,2}$"));
    }

    public void testRequestSdkvVoxel()
    {
        assertTrue(this.getParam(APS_COUNTRY_CODE_CUBA, true, APS_SDK_VERSION, "^[0-9]{1,2}.[0-9]{1,2}.[0-9]{1,2}|[0-9]{1,2}.[0-9]{1,2}$"));
    }

    // JSON ad request contains instl (interstitial = 1)
    public void testRequestInstlInterstitial()
    {
        assertTrue(this.getParam(APS_COUNTRY_CODE_DOMINICA, false, APS_INTERSTITIAL, "^1$"));
    }

    public void testRequestInstlVAST()
    {
        assertTrue(this.getParam(APS_COUNTRY_CODE_CAMBODIA, false, APS_INTERSTITIAL, "^1$"));
    }

    public void testRequestInstlVoxel()
    {
        assertTrue(this.getParam(APS_COUNTRY_CODE_CUBA, true, APS_INTERSTITIAL, "^1$"));
    }

    // JSON ad request contains orient (Landscape orientation)
    public void testRequestOrientLandscapeInterstitial()
    {
        Map<String, String> appInfoPairs = new HashMap<String, String>();
        appInfoPairs.put(APS_ORIENTATION, APS_ORIENTATION_VALUE_LANDSCAPE);
        assertTrue(this.getParam(APS_COUNTRY_CODE_DOMINICA, false, APS_ORIENTATION, "^l$", appInfoPairs));
    }

    public void testRequestOrientLandscapeVoxel()
    {
        Map<String, String> appInfoPairs = new HashMap<String, String>();
        appInfoPairs.put(APS_ORIENTATION, APS_ORIENTATION_VALUE_LANDSCAPE);
        assertTrue(this.getParam(APS_COUNTRY_CODE_CUBA, true, APS_ORIENTATION, "^l$", appInfoPairs));
    }

    // JSON ad request contains orient (Portrait orientation)
    public void testRequestOrientPortraitInterstitial()
    {
        Map<String, String> appInfoPairs = new HashMap<String, String>();
        appInfoPairs.put(APS_ORIENTATION, APS_ORIENTATION_VALUE_PORTRAIT);
        assertTrue(this.getParam(APS_COUNTRY_CODE_DOMINICA, false, APS_ORIENTATION, "^p$", appInfoPairs));
    }

    public void testRequestOrientPortraitVoxel()
    {
        Map<String, String> appInfoPairs = new HashMap<String, String>();
        appInfoPairs.put(APS_ORIENTATION, APS_ORIENTATION_VALUE_PORTRAIT);
        assertTrue(this.getParam(APS_COUNTRY_CODE_CUBA, true, APS_ORIENTATION, "^p$", appInfoPairs));
    }

    // JSON ad request contains bdn (bundle display name)
    public void testRequestBdnInterstitial()
    {
        assertTrue(this.getParam(APS_COUNTRY_CODE_DOMINICA, false, APS_BUNDLE_DISPLAY, "^[0-9a-zA-Z%]{3,40}$"));
    }

    public void testRequestBdnVAST()
    {
        assertTrue(this.getParam(APS_COUNTRY_CODE_CAMBODIA, false, APS_BUNDLE_DISPLAY, "^[0-9a-zA-Z%]{3,40}$"));
    }

    public void testRequestBdnVoxel()
    {
        assertTrue(this.getParam(APS_COUNTRY_CODE_CUBA, true, APS_BUNDLE_DISPLAY, "^[0-9a-zA-Z%]{3,40}$"));
    }

    // JSON ad request contains zone_type
    // 1: display ad (can be static image ad or vast)
    // 2: performance ad
    public void testRequestZoneTypeInterstitial()
    {
        assertTrue(this.getParam(APS_COUNTRY_CODE_DOMINICA, false, APS_ZONE_TYPE, "^1|2$"));
    }

    public void testRequestZoneTypeVAST()
    {
        assertTrue(this.getParam(APS_COUNTRY_CODE_CAMBODIA, false, APS_ZONE_TYPE, "^1|2$"));
    }

    public void testRequestZoneTypeVoxel()
    {
        assertTrue(this.getParam(APS_COUNTRY_CODE_CUBA, true, APS_ZONE_TYPE, "^1|2$"));
    }

    // JSON ad request contains sub8 (sub param)
    public void testRequestSub8Interstitial()
    {
        assertTrue(this.getParam(APS_COUNTRY_CODE_DOMINICA, false, APS_SUB8, "^1$"));
    }

    public void testRequestSub8VAST()
    {
        assertTrue(this.getParam(APS_COUNTRY_CODE_CAMBODIA, false, APS_SUB8, "^1$"));
    }

    public void testRequestSub8Voxel()
    {
        assertTrue(this.getParam(APS_COUNTRY_CODE_CUBA, true, APS_SUB8, "^1$"));
    }

    // JSON ad request contains width (the width of the ad unit)
    public void testRequestWidtInterstitial()
    {
        assertTrue(this.getParam(APS_COUNTRY_CODE_DOMINICA, false, APS_WIDTH, "^[0-9]{1,4}.[0-9]{0,6}$"));
    }

    public void testRequestWidtVAST()
    {
        assertTrue(this.getParam(APS_COUNTRY_CODE_CAMBODIA, false, APS_WIDTH, "^[0-9]{1,4}.[0-9]{0,6}$"));
    }

    public void testRequestWidtVoxel()
    {
        assertTrue(this.getParam(APS_COUNTRY_CODE_CUBA, true, APS_WIDTH, "^[0-9]{1,4}.[0-9]{0,6}$"));
    }

    // JSON ad request contains dpid
    // (Device platform-specific ID. Specified as <prefix>:<value>:)
    // and:<android_id_value> - Raw ANDROID_ID
    public void testRequestDpidInterstitial()
    {
        assertTrue(this.getParam(APS_COUNTRY_CODE_DOMINICA, false, APS_ANDROID_ID, "^and:[0-9a-z]{16}$"));
    }

    public void testRequestDpidVAST()
    {
        assertTrue(this.getParam(APS_COUNTRY_CODE_CAMBODIA, false, APS_ANDROID_ID, "^and:[0-9a-z]{16}$"));
    }

    public void testRequestDpidVoxel()
    {
        assertTrue(this.getParam(APS_COUNTRY_CODE_CUBA, true, APS_ANDROID_ID, "^and:[0-9a-z]{16}$"));
    }

    // JSON ad request contains nl
    // (network latency in milliseconds, the time that
    // sdk takes to get a small file from cdn)
    public void testRequestNlInterstitial()
    {
        assertTrue(this.getParam(APS_COUNTRY_CODE_DOMINICA, false, APS_NETWORK_LATENCY, "^[0-9]{1,6}.[0-9]{1,6}|[0-9]{1,6}$"));
    }

    public void testRequestNlVAST()
    {
        assertTrue(this.getParam(APS_COUNTRY_CODE_CAMBODIA, false, APS_NETWORK_LATENCY, "^[0-9]{1,6}.[0-9]{1,6}|[0-9]{1,6}$"));
    }

    public void testRequestNlVoxel()
    {
        assertTrue(this.getParam(APS_COUNTRY_CODE_CUBA, true, APS_NETWORK_LATENCY, "^[0-9]{1,6}.[0-9]{1,6}|[0-9]{1,6}$"));
    }

    // JSON ad request contains make (Make of the device)
    public void testRequestMakeInterstitial()
    {
        assertTrue(this.getParam(APS_COUNTRY_CODE_DOMINICA, false, APS_DEVICE_MAKE, "^[a-zA-Z0-9.\\-_]{3,30}$"));
    }

    public void testRequestMakeVAST()
    {
        assertTrue(this.getParam(APS_COUNTRY_CODE_CAMBODIA, false, APS_DEVICE_MAKE, "^[a-zA-Z0-9.\\-_]{3,30}$"));
    }

    public void testRequestMakeVoxel()
    {
        assertTrue(this.getParam(APS_COUNTRY_CODE_CUBA, true, APS_DEVICE_MAKE, "^[a-zA-Z0-9.\\-_]{3,30}$"));
    }

    // JSON ad request contains osv (OS version)
    public void testRequestOsvInterstitial()
    {
        assertTrue(this.getParam(APS_COUNTRY_CODE_DOMINICA, false, APS_OS_VERSION, "^[0-9].[0-9].[0-9]$"));
    }

    public void testRequestOsvVAST()
    {
        assertTrue(this.getParam(APS_COUNTRY_CODE_CAMBODIA, false, APS_OS_VERSION, "^[0-9].[0-9].[0-9]$"));
    }

    public void testRequestOsvVoxel()
    {
        assertTrue(this.getParam(APS_COUNTRY_CODE_CUBA, true, APS_OS_VERSION, "^[0-9].[0-9].[0-9]$"));
    }

    // JSON ad request contains bn (bundle name)
    public void testRequestBnInterstitial()
    {
        assertTrue(this.getParam(APS_COUNTRY_CODE_DOMINICA, false, APS_BUNDLE_NAME, "^[0-9a-zA-Z%]{3,40}$"));
    }

    public void testRequestBnVAST()
    {
        assertTrue(this.getParam(APS_COUNTRY_CODE_CAMBODIA, false, APS_BUNDLE_NAME, "^[0-9a-zA-Z%]{3,40}$"));
    }

    public void testRequestBnVoxel()
    {
        assertTrue(this.getParam(APS_COUNTRY_CODE_CUBA, true, APS_BUNDLE_NAME, "^[0-9a-zA-Z%]{3,40}$"));
    }

    // JSON ad request contains os (OS name (i.e. "iOS", "Android"))
    public void testRequestOsInterstitial()
    {
        assertTrue(this.getParam(APS_COUNTRY_CODE_DOMINICA, false, APS_OS_NAME, "^Android$"));
    }

    public void testRequestOsVAST()
    {
        assertTrue(this.getParam(APS_COUNTRY_CODE_CAMBODIA, false, APS_OS_NAME, "^Android$"));
    }

    public void testRequestOsVoxel()
    {
        assertTrue(this.getParam(APS_COUNTRY_CODE_CUBA, true, APS_OS_NAME, "^Android$"));
    }

    // JSON ad request contains voxel_version
    // (string value for version of voxel SDK,
    // because some old version can not handle newer games
    public void testRequestVoxelVersionVoxel()
    {
        assertTrue(this.getParam(APS_COUNTRY_CODE_CUBA, true, APS_VOXEL_VERSION, "^[0-9].[0-9].[0-9]$"));
    }

    // JSON ad request contains nw (0=unknown, 1=carrier, 2=wifi)
    public void testRequestNwInterstitial()
    {
        assertTrue(this.getParam(APS_COUNTRY_CODE_DOMINICA, false, APS_NETWORK_PROVIDER_INFO, "^0|1|2$"));
    }

    public void testRequestNwVAST()
    {
        assertTrue(this.getParam(APS_COUNTRY_CODE_CAMBODIA, false, APS_NETWORK_PROVIDER_INFO, "^0|1|2$"));
    }

    public void testRequestNwVoxel()
    {
        assertTrue(this.getParam(APS_COUNTRY_CODE_CUBA, true, APS_NETWORK_PROVIDER_INFO, "^0|1|2$"));
    }

    // JSON ad request contains bi (bundle identifier)
    public void testRequestBiInterstitial()
    {
        assertTrue(this.getParam(APS_COUNTRY_CODE_DOMINICA, false, APS_BUNDLE_IDENTIFIER, "^[0-9a-zA-Z.]{3,40}$"));
    }

    public void testRequestBiVAST()
    {
        assertTrue(this.getParam(APS_COUNTRY_CODE_CAMBODIA, false, APS_BUNDLE_IDENTIFIER, "^[0-9a-zA-Z.]{3,40}$"));
    }

    public void testRequestBiVoxel()
    {
        assertTrue(this.getParam(APS_COUNTRY_CODE_CUBA, true, APS_BUNDLE_IDENTIFIER, "^[0-9a-zA-Z.]{3,40}$"));
    }

    // JSON ad request contains lang (Browser language using alpha-2/ISO 639-1)
    public void testRequestLangInterstitial()
    {
        assertTrue(this.getParam(APS_COUNTRY_CODE_DOMINICA, false, APS_LANGUAGE, "^ru|en$"));
    }

    public void testRequestLangVAST()
    {
        assertTrue(this.getParam(APS_COUNTRY_CODE_CAMBODIA, false, APS_LANGUAGE, "^ru|en$"));
    }

    public void testRequestLangVoxel()
    {
        assertTrue(this.getParam(APS_COUNTRY_CODE_CUBA, true, APS_LANGUAGE, "^ru|en$"));
    }

    // JSON ad request contains height (The height of the ad unit)
    public void testRequestHeightInterstitial()
    {
        assertTrue(this.getParam(APS_COUNTRY_CODE_DOMINICA, false, APS_HEIGHT, "^[0-9]{1,6}.[0-9]{0,6}$"));
    }

    public void testRequestHeightVAST()
    {
        assertTrue(this.getParam(APS_COUNTRY_CODE_CAMBODIA, false, APS_HEIGHT, "^[0-9]{1,6}.[0-9]{0,6}$"));
    }

    public void testRequestHeightVoxel()
    {
        assertTrue(this.getParam(APS_COUNTRY_CODE_CUBA, true, APS_HEIGHT, "^[0-9]{1,6}.[0-9]{0,6}$"));
    }
    
    // JSON ad request contains UUID parameter
    public void testRequestUUIDInterstitial()
    {
        assertTrue(this.getParam(APS_COUNTRY_CODE_DOMINICA, false, APS_UBID, "^[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}$"));
    }

    public void testRequestUUIDtVAST()
    {
        assertTrue(this.getParam(APS_COUNTRY_CODE_CAMBODIA, false, APS_UBID, "^[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}$"));
    }

    public void testRequestUUIDVoxel()
    {
        assertTrue(this.getParam(APS_COUNTRY_CODE_CUBA, true, APS_UBID, "^[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}$"));
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
   
    private boolean getParam(String country, boolean isVoxel, String testParameter, String reg)
    {
        return this.getParam(country, isVoxel, testParameter, reg, null);
    }

    // It checks parameter in request by parameter name and regular expression
    private boolean getParam(String country, boolean isVoxel, String testParameter, String reg,
            Map<String, String> additionalParams)
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

        boolean res = false;

        try
        {
            String content = IOUtils.toString(httpPost.getEntity().getContent());
            JSONObject externalObj = new JSONObject(content);
            String param = externalObj.optString(testParameter);
            res = Pattern.matches(reg, param);
        }
        catch (Exception e)
        {
            Log.e(APS_TAG, e.getLocalizedMessage());
        }

        return res;
    }  
} 