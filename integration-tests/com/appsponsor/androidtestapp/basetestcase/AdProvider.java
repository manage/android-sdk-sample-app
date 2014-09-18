package com.appsponsor.androidtestapp.basetestcase;

import java.lang.reflect.Field;
import java.util.List;

import android.app.Activity;
import android.util.Log;

import com.appsponsor.appsponsorsdk.activity.VideoAdActivity;
import com.appsponsor.appsponsorsdk.model.AdActivityEntity;

public class AdProvider
{
    private static final String TAG = "APPSPONSOR.TEST." + AdProvider.class.getSimpleName();

    public static <T extends Activity> String getWinUrlForActivity(T activity)
    {
        return AdProvider.getActivityEntity(activity).getWinUrl();
    }

    public static <T extends Activity> String getTrackingUrlForActivity(T activity)
    {
        String url = null;

        if (activity instanceof VideoAdActivity)
        {
            List<String> trackUrls = (List<String>) AdProvider.getPrivateField(activity, "trackUrls");
            url = trackUrls.get(0);
        }

        return url;
    }

    public static <T extends Activity> String getClickUrlForActivity(T activity)
    {
        return AdProvider.getActivityEntity(activity).getClickUrl();
    }

    private static Object getPrivateField(Activity activity, String name)
    {
        Object privateObject = null;
        try
        {
            Field field = activity.getClass().getDeclaredField(name);
            field.setAccessible(true);
            privateObject = field.get(activity);
        }
        catch (Exception e)
        {
            Log.e(TAG, e.getMessage());
        }

        return privateObject;
    }

    private static AdActivityEntity getActivityEntity(Activity activity)
    {
        AdActivityEntity entity = (AdActivityEntity) AdProvider.getPrivateField(activity, "activityEntity");

        return entity;
    }
}
