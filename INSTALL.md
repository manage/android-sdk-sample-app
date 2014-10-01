
# ANDROID INSTALL GUIDE


### Overview

This guide provides integration instructions for the Manage Publisher Android SDK.  If you need support or have any questions, feel free to email us at [support@appsponsor.com](support@appsponsor.com)

 Requirements and Dependencies:

* Android version 2.3 or greater
* Android Support Libraries  
    https://developer.android.com/tools/support-library/setup.html
* Google Play Services Library (in order to use the Google Advertising ID)
    http://developer.android.com/google/play-services/setup.html
* If you are using a Rewarded ad, you will need to set up your [Server to Server](https://appsponsor.com/site/page/?view=install_server2server) callback.
* If you are using Unity or Adobe AIR please reference the respective guides here: [Adobe AIR](https://appsponsor.com/site/page/?view=install_adobeair)  
[Unity](https://appsponsor.com/site/page/?view=install_unity)


###1. Download and Install the SDK
The SDK for Android is available once you [sign up](https://www.appsponsor.com/user/registration). The Manage Publisher SDK includes everything you need to serve full screen interstitial, video, and playable ad units.

For an example, please see our [sample app](https://github.com/manage/android-sdk-sample-app).

###2. Eclipse Integration
The Manage Android SDK contains a library project called appsponsorsdk.  

 1. Import the integration into your workspace by clicking `File > Import`, and then choosing `Existing Projects into Workspace`.

![](http://cdn.manage.com/appsponsor/documentation/android/step-1.png)

 2. Choose the `Select Archive File` option and then `Browse...` for the Manage-SDK archive file.
3. Select the Manage-SDK archive file, and then click `Open`.  It should automatically find the libraries.  Please include Google Play Services libraries unless previously imported.

 ![](http://cdn.manage.com/appsponsor/documentation/android/step-2.png)


###3. Adding the SDK to your project
1. Go to the Eclipse Package Explorer, right-click your project folder and select
`Properties`
 ![](http://cdn.manage.com/appsponsor/documentation/android/step-3.png)
 
2. Choose `Android` from the sidebar
3. Click `Add` and then under the `Library` section select the appsponsorsdk project:
![](http://cdn.manage.com/appsponsor/documentation/android/step-4.png)

You should now see the appsponsorsdk added to the project `Library` 

![](http://cdn.manage.com/appsponsor/documentation/android/step-5.png)

###4. Update your Android Manifest
Add the following permissions if your app does not already have them:

``` java
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
<uses-permission android:name="android.permission.READ_PHONE_STATE" />
<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
<uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />
<uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
```

Add the following meta data inside your `<application>` tag if have not already done so:

``` java
<meta-data android:name="com.google.android.gms.version" android:value="@integer/google_play_services_version" />
```

Declare these activities in your app manifest:
``` java
<activity android:name="com.appsponsor.appsponsorsdk.PopupAdActivity" android:launchMode="singleTop" android:theme="@android:style/Theme.Translucent" android:configChanges="keyboardHidden|orientation|screenSize">
</activity>
<activity android:name="com.appsponsor.appsponsorsdk.video.VideoAdActivity" android:screenOrientation="landscape" android:launchMode="singleTop" android:theme="@android:style/Theme.NoTitleBar.Fullscreen" android:configChanges="keyboardHidden|orientation|screenSize">
</activity>

```

###5. Importing AppSponsor  
Import the appsponsorsdk into any activity that uses it: 


``` java
import com.appsponsor.appsponsorsdk.PopupAd;
import com.appsponsor.appsponsorsdk.PopupAdListener;

/* If you are using rewarded ads, also import this: */
import com.appsponsor.appsponsorsdk.RewardedAd;

```

In the Activity where you want to show the ad, declare the `PopupAd` and/or `RewardedAd` instance variable:

``` java
public class MyActivity extends Activity {

    private PopupAd popupAd;

    /* Rewarded ad */
    private Rewarded rewardedAd;

...
}
```

Instantiate the `PopupAd` and/or `RewardedAd` during `onCreate()` method of your Activity:

``` java
@Override
protected void onCreate(){
    popupAd = new PopupAd(this, "YOUR ZONE ID");

    /* Rewarded ad */
    rewardedAd = new RewardedAd(this, "YOUR ZONE ID", "PUBLISHER USER ID");
}
```

###5.1. Improve Ad Targeting by setting non PII user data:

``` java
popupAd.setCity("");
popupAd.setUCity("");
popupAd.setCountry("");
popupAd.setUCountry("")
popupAd.setRegion("")
popupAd.setMetro("")
popupAd.setZip("")
popupAd.setUZip("")
popupAd.setLongitude("")
popupAd.setLatitude("")
popupAd.setGender("");
popupAd.setYob("")
popupAd.setKeywords("");

```

If you would like to pre cache your ads follow steps in section 5.2.  Otherwise, if you would like to load ads synchronously for immediate presentation of an ad follow steps in section 5.3:

####5.2 Pre-Cached Ads 
   
  Pre-cache ad:

``` java
popupAd.load();

/* Rewarded ad */
rewardedAd.load();
```

To show ad:

``` java
if (popupAd.isReady()) {
    popupAd.presentAd();   
}

/* Rewarded ad */
if (rewardedAd.isReady()) {
    rewardedAd.presentAd();   
}
```

####5.3 Load and Present Ad Synchronously

To show ad:
``` java
popupAd.loadAndPresentAd();

/* Rewarded ad */
rewardedAd.loadAndPresentAd();
```

Finally, when your Activity is completed, call `destroy()` in the Activity's `onDestroy()` method:
``` java
@Override
protected void onDestory(){
    super.onDestroy();
    popupAd.destroy();

    /* Rewarded ad */
    rewardedAd.destroy();
}
```