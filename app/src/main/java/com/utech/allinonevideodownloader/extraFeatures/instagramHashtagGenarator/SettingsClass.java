package com.utech.allinonevideodownloader.extraFeatures.instagramHashtagGenarator;

import android.app.Activity;
import android.view.View;
import android.widget.LinearLayout;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

public class SettingsClass {

    public static Boolean supportRTL = false;
    public static String publisherID = "pub-1234567890123456";
    public static String contactMail = "demo@outlook.com";
    public static String admBanner = "ca-app-pub-9243738527809292/1635777738";
    public static String Interstitial = "ca-app-pub-9243738527809292/6696532729";
    public static String privacy_policy_url = "https://www.google.com/policies/privacy/";
    public static int nbShowInterstitial = 5;
    public static int mCount = 0;

    public static void admobBannerCall(Activity activity, final LinearLayout linerlayout) {

        AdView adView = new AdView(activity);
        adView.setAdUnitId(SettingsClass.admBanner);
        adView.setAdSize(AdSize.SMART_BANNER);
        adView.loadAd(ConsentSDK.getAdRequest(activity));
        linerlayout.setVisibility(View.GONE);
        linerlayout.addView(adView);
        adView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                linerlayout.setVisibility(View.VISIBLE);
            }
        });
    }
}
