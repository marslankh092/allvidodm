package com.utech.allinonevideodownloader;

import android.app.Application;
import android.content.Context;

import com.facebook.ads.AudienceNetworkAds;
import com.utech.allinonevideodownloader.utils.LocaleHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class Appcontroller extends Application {


    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleHelper.onAttach(base, "en"));
    }

    @Override
    public void onCreate() {
        super.onCreate();
        AudienceNetworkAds.initialize(this);
        List<Locale> locales = new ArrayList<>();
        locales.add(Locale.ENGLISH);
        locales.add(new Locale("ar", "ARABIC"));
        locales.add(new Locale("ur", "URDU"));
        locales.add(new Locale("tr", "Turkish"));
        locales.add(new Locale("hi", "Hindi"));
        LocaleHelper.setLocale(getApplicationContext(), "en");

        Random random = new Random();
        int num = random.nextInt(2);
    }
}
