package com.appsponsor.androidtestapp;

import java.net.HttpURLConnection;
import java.util.LinkedList;

import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;

import com.appsponsor.androidtestapp.MainActivity;
import com.appsponsor.appsponsorsdk.AdManager;
import com.appsponsor.appsponsorsdk.utils.IOUtils;
import com.voxel.sdk.VoxelSDK;

public class MainActivityTest extends
		ActivityInstrumentationTestCase2<MainActivity> {
	
	private static final String APS_TAG = "APS." + MainActivityTest.class.getSimpleName();
	
	private static final String APS_GPS_CUBA_LONGITUDE = "79.5000";
	private static final String APS_GPS_CUBA_LATITUDE = "22.0000";

	// NOTE: This constant should be updated with country code, where tests are running
	private static final String APS_COUNTRY_CODE_TEST = "UKR";
	private static final String APS_COUNTRY_CODE_CUBA = "CUB";

	private static final String APS_WIN_URL = "win_url";

	private static final String APS_ZONE_ID = "oIs29VQKIa2IfaA4FWkEqw";
	
	
	private MainActivity mainActivity;

	public MainActivityTest() {
		super(MainActivity.class);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();

		this.mainActivity = getActivity();		
		assertNotNull("MainActivity is null", this.mainActivity);
	}

	public void testIdentificationByCountry() {
		LinkedList<BasicNameValuePair> appInfoPairs = this.basicInfoPairs();
		appInfoPairs.add(new BasicNameValuePair("country", APS_COUNTRY_CODE_TEST));
		AdManager adManager = this.createAdManager(this.mainActivity, appInfoPairs);
		
		HttpPost httpPost = adManager.serverRequest(APS_ZONE_ID);

		String result = this.serverResponseBody(httpPost);
		String winUrl = null;
		try {
			JSONObject externalObj = new JSONObject(result);
			winUrl = externalObj.optString(APS_WIN_URL);
		} catch (Exception e) {
			Log.e(APS_TAG, e.getLocalizedMessage());
		}
		
		assertNotNull(winUrl);
		assertTrue(winUrl.contains("country=" + APS_COUNTRY_CODE_TEST));
	}

	/*
	 * SDK instance with hardcoded property ‘country’ receives ads for this
	 * country, disregarding of actual IP or GPS.
	 */
	public void testIdentificationByIP() {
		AdManager adManager = this.createAdManager(this.mainActivity, this.basicInfoPairs());
		
		HttpPost httpPost = adManager.serverRequest(APS_ZONE_ID);

		String result = this.serverResponseBody(httpPost);
		String winUrl = null;
		try {
			JSONObject externalObj = new JSONObject(result);
			winUrl = externalObj.optString(APS_WIN_URL);
		} catch (Exception e) {
			Log.e(APS_TAG, e.getLocalizedMessage());
		}
		
		assertNotNull(winUrl);
		assertTrue(winUrl.contains("country=" + APS_COUNTRY_CODE_TEST));
	}

	public void testIdentificationByGPS() {
		LinkedList<BasicNameValuePair> appInfoPairs = this.basicInfoPairs();
		appInfoPairs.add(new BasicNameValuePair("lat", APS_GPS_CUBA_LATITUDE));
		appInfoPairs.add(new BasicNameValuePair("lon", APS_GPS_CUBA_LONGITUDE));
		AdManager adManager = this.createAdManager(this.mainActivity, appInfoPairs);

		HttpPost httpPost = adManager.serverRequest(APS_ZONE_ID);

		String result = this.serverResponseBody(httpPost);
		String winUrl = null;
		try {
			JSONObject externalObj = new JSONObject(result);
			winUrl = externalObj.optString(APS_WIN_URL);
		} catch (Exception e) {
			Log.e(APS_TAG, e.getLocalizedMessage());
		}
		
		assertNotNull(winUrl);
		assertTrue(winUrl.contains("country=" + APS_COUNTRY_CODE_CUBA));
	}

	private AdManager createAdManager(MainActivity mainActivity,
			LinkedList<BasicNameValuePair> appInfoPairs) {
		AdManager adManager = new AdManager(mainActivity);
		adManager.setTestMode(false);
		adManager.setAppInfoPairs(appInfoPairs);
		adManager.setKeywords("");
		adManager.setInterstitialView(true);
		return adManager;
	}

	private LinkedList<BasicNameValuePair> basicInfoPairs() {
		LinkedList<BasicNameValuePair> appInfoPairs = new LinkedList<BasicNameValuePair>();
		appInfoPairs.add(new BasicNameValuePair("sub8", "1"));
		appInfoPairs.add(new BasicNameValuePair("vast", "1"));
		appInfoPairs.add(new BasicNameValuePair("nl", String.valueOf(498)));
		appInfoPairs.add(new BasicNameValuePair("voxel_version",
				VoxelSDK.VERSION));
		appInfoPairs.add(new BasicNameValuePair("zone_type", "1"));
		appInfoPairs.add(new BasicNameValuePair("nw", "2"));
		appInfoPairs.add(new BasicNameValuePair("dpid.ext",
				"a9af174f-1968-4eab-bf7c-79920dbb9f51"));
		appInfoPairs.add(new BasicNameValuePair("orient", "p"));
		return appInfoPairs;
	}

	private String serverResponseBody(HttpPost post) {
		HttpURLConnection urlConnection = null;
		String result = null;
		try {
			urlConnection = (HttpURLConnection) post.getURI().toURL()
					.openConnection();
			urlConnection.setConnectTimeout(1000 * 10);
			urlConnection.setDoOutput(true);
			
			post.getEntity().writeTo(urlConnection.getOutputStream());

			result = IOUtils.toString(urlConnection.getInputStream());
		} catch (Exception e) {
			Log.e(APS_TAG, e.getLocalizedMessage());
		} finally {
			if (urlConnection != null) {
				urlConnection.disconnect();
			}
		}

		return result;
	}
}