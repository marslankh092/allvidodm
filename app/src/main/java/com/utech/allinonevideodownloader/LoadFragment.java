package com.utech.allinonevideodownloader;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.utech.allinonevideodownloader.extraFeatures.ExtraFeaturesFragment;
import com.utech.allinonevideodownloader.statussaver.StatusSaverMainFragment;

public class LoadFragment extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_load_fragment);


        if (getIntent() != null) {

            String name = getIntent().getStringExtra("name");
            if (name.equals("extra")) {
                switchFragment(new ExtraFeaturesFragment());

            } else if (name.equals("status")) {
                switchFragment(new StatusSaverMainFragment());

            }
        }
    }


    @Override
    public void onBackPressed() {
        startActivity(new Intent(LoadFragment.this, MainActivity.class));
        finish();
    }

    public void switchFragment(Fragment baseFragment) {

        baseFragment.setArguments(getIntent().getExtras());
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frame_loadfrag, baseFragment).commit();
    }
}