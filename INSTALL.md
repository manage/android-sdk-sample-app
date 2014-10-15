# ANDROID INSTALL GUIDE

### Overview

This guide provides integration instructions for the Manage Publisher Android SDK.  If you need support or have any questions, feel free to email us at [support@appsponsor.com](support@appsponsor.com)

Requirements and Dependencies:

* Android:
    * Android version 2.3 or greater
    * Google Play Services Library (in order to use the Google Advertising ID)
    http://developer.android.com/google/play-services/setup.html
* If you are using a Rewarded ad, you will need to set up your [Server to Server](/dashboard/publisher/guides/server_to_server/) callback.

### 1. Download and Install the SDK

The SDK for Android is available once you [sign up](https://www.appsponsor.com/user/registration). The Manage Publisher SDK includes everything you need to serve full screen interstitial, video, and playable ad units.

The SDK for Android can be downloaded here: [ManageSDK-Android-v3.1.zip](/downloads/ManageSDK-Android-v3.1.zip).

To see an example implementation, please see our [sample app](https://github.com/manage/android-sdk-sample-app).

### 2. Eclipse Integration
The Manage Android SDK contains a library project called appsponsorsdk.  

 1. Import the integration into your workspace by clicking `File > Import`, and then choosing `Existing Projects into Workspace`.

![](https://s3.amazonaws.com/cdn.manage.com/appsponsor/documentation/android/step-1.png)

2. Choose the `Select Archive File` option and then `Browse...` for the Manage-SDK archive file.

3. Select the Manage-SDK archive file, and then click `Open`.  It should automatically find the libraries.  Please include Google Play Services libraries unless previously imported.

 ![](https://s3.amazonaws.com/cdn.manage.com/appsponsor/documentation/android/step-2.png)

### 3. Adding the SDK to your project
1. Go to the Eclipse Package Explorer, right-click your project folder and select
`Properties`

 ![](https://s3.amazonaws.com/cdn.manage.com/appsponsor/documentation/android/step-3.png)
 
2. Choose `Android` from the sidebar

3. Click `Add` and then under the `Library` section select the appsponsorsdk project:

![](https://s3.amazonaws.com/cdn.manage.com/appsponsor/documentation/android/step-4.png)

You should now see the appsponsorsdk added to the project `Library` 

![](https://s3.amazonaws.com/cdn.manage.com/appsponsor/documentation/android/step-5.png)

### 4. Update your Android Manifest

Add the following permissions if your app does not already have them:

```
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
<uses-permission android:name="android.permission.READ_PHONE_STATE" />
<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
<uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />
<uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
```

Add the following meta data inside your `<application>` tag if have not already done so:

```
<meta-data android:name="com.google.android.gms.version" android:value="@integer/google_play_services_version" />
```

Declare these activities in your app manifest:

```
<activity android:name="com.appsponsor.appsponsorsdk.PopupAdActivity" android:launchMode="singleTop" android:theme="@android:style/Theme.Translucent" android:configChanges="keyboardHidden|orientation|screenSize">
</activity>

<activity android:name="com.appsponsor.appsponsorsdk.video.VideoAdActivity" android:screenOrientation="landscape" android:launchMode="singleTop" android:theme="@android:style/Theme.NoTitleBar.Fullscreen" android:configChanges="keyboardHidden|orientation|screenSize">
</activity>

```

### 5. Integrating Manage SDK 

Import the appsponsorsdk into any activity that uses it: 

```
import com.appsponsor.appsponsorsdk.PopupAd;
import com.appsponsor.appsponsorsdk.PopupAdListener;

/* If you are using rewarded ads, also import this: */
import com.appsponsor.appsponsorsdk.RewardedAd;

```

In the Activity where you want to show the ad, declare the `PopupAd` and/or `RewardedAd` instance variable:

```
public class MyActivity extends Activity {

    private PopupAd popupAd;

    /* Rewarded ad */
    private Rewarded rewardedAd;

...
}
```

Instantiate the `PopupAd` and/or `RewardedAd` during `onCreate()` method of your Activity:

```
@Override
protected void onCreate(){
    popupAd = new PopupAd(this, "YOUR ZONE ID");

    /* Rewarded ad */
    rewardedAd = new RewardedAd(this, "YOUR ZONE ID", "PUBLISHER USER ID");
}
```

#### 5.1. Improve Ad Targeting by setting non PII user data:

```
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

#### 5.2 Pre-Cached Ads 
   
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

#### 5.3 Load and Present Ad Synchronously

To show ad:
``` java
popupAd.loadAndPresentAd();

/* Rewarded ad */
rewardedAd.loadAndPresentAd();
```

Finally, when your Activity is completed, call `destroy()` in the Activity's `onDestroy()` method:

```
@Override
protected void onDestory(){
    super.onDestroy();

    popupAd.destroy();

    /* Rewarded ad */
    rewardedAd.destroy();
}
```

### 6. Optional Steps

#### 6.1 Register Listeners

Add event listeners to perform any customized callback:

```
popupAd.setPopupAdListener(new PopupAd.PopupAdListener(){
 
    //
    // These following delegates may be triggered after the load() function:
    //

    public void popoverDidFailToLoadWithError(Exception exception) {
        // Called when the ad fails to load.
    }
 
    public void didCacheInterstitial() {
        // Called when ad content is ready to display.
    }

    //
    // These following delegates will always be triggered after the presentAd() function:
    //

    public void willAppear() {
        // Called before the ad shows. For example, you can pause your app here.
    }

    public void willDisappear() {
        // Called before the ad disappears. For example, you can have resume your app here.
    }


    public void onRewardedAdFinished(){
        // Called when rewarded ad is completed
    }

  });
});
```

Example:

```
// Show alert dialog on ad completion
popupAd.setPopupAdListener(new PopupAd.IVideoAdListener() {

    public void onRewardedAdFinished(){
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Congrats!");
        builder.setMessage("You have earn 10 coins by watching this ad");
        builder.setPositiveButton("Yes",
            new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.cancel();
                }
        });
        builder.setNegativeButton("No",
            new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.cancel();
                }
        });

        AlertDialog alert = builder.create();
        alert.show();
    }
});

```