/*
 * .
 * Copyright Copyright (c) 2017 Hitesh Sahu(hiteshsahu.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */

package com.hiteshsahu.cool_keyboard.view.activitis;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.hiteshsahu.cool_keyboard.R;
import com.hiteshsahu.cool_keyboard.view.fragments.LicensesDialogFragment;

import java.io.File;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class AboutActivity extends AppCompatActivity {

    @BindView(R.id.app_version_code)
    TextView appVersion;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        ButterKnife.bind(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.primary_dark));
        }

        setSupportActionBar(toolbar);
        // add back arrow to toolbar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onBackPressed();
                }
            });
        }

        //App version
        appVersion.setText(getAppVersion(getApplicationContext()));
    }

    @Override
    public void onBackPressed() {
        vibrate(getApplicationContext());
        super.onBackPressed();
    }

    @OnClick(R.id.ll_more_apps)
    public void showMoreApps() {
        vibrate(getApplicationContext());
        openPlayStore(true, "Hitesh%20Sahu", AboutActivity.this);
    }

    @OnClick(R.id.ll_rate_this_app)
    public void openAppInPlayStore() {
        vibrate(getApplicationContext());
        openPlayStore(false, "", AboutActivity.this);
    }

    //About frag_settings
    @OnClick(R.id.ll_share)
    public void shareThisApp() {
        vibrate(getApplicationContext());

        try {
            PackageManager pm = getPackageManager();
            ApplicationInfo ai = pm.getApplicationInfo(getPackageName(), 0);
            File srcFile = new File(ai.publicSourceDir);
            Intent share = new Intent();
            share.setAction(Intent.ACTION_SEND);
            share.setType("application/vnd.android.package-archive");
            share.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(srcFile));
            startActivity(Intent.createChooser(share, "PersianCoders"));
        } catch (Exception e) {
            Log.e("ShareApp", e.getMessage());
        }
    }

    @OnClick(R.id.ll_licence)
    public void showLicence() {
        vibrate(getApplicationContext());
        new LicensesDialogFragment().show(
                getSupportFragmentManager().beginTransaction(), "dialog_licenses");
    }

    @OnClick(R.id.ll_mail)
    public void mailDev() {
        vibrate(getApplicationContext());
        final Intent emailIntent = new Intent(
                Intent.ACTION_SEND);
        emailIntent.setType("text/plain");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"hiteshkrsahu@gmail.com"});
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Hello There");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "Add Message here");
        emailIntent.setType("message/rfc822");

        try {
            startActivity(Intent.createChooser(emailIntent,
                    "Send email using..."));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(getApplicationContext(),
                    "No email clients installed.",
                    Toast.LENGTH_SHORT).show();
        }
    }

    private String getAppVersion(Context context) {
        try {
            PackageInfo pInfo = context.getPackageManager().getPackageInfo(
                    context.getPackageName(), PackageManager.GET_META_DATA);
            return "Version " + String.valueOf(pInfo.versionCode) + " " + pInfo.versionName;

        } catch (PackageManager.NameNotFoundException e) {
            return "Version " + "1.0.1";
        }
    }


    @OnClick(R.id.ll_call)
    public void callDeveloper() {
        vibrate(getApplicationContext());
        startActivity(new Intent(Intent.ACTION_DIAL)
                .setData(Uri.parse("tel:" + "+91 88888 13275")));
    }

    @OnClick(R.id.ll_dev)
    public void openPortfolio() {
        vibrate(getApplicationContext());
        startActivity(new Intent(
                Intent.ACTION_VIEW,
                Uri.parse("http://hiteshsahu.com/")));
    }

    @OnClick(R.id.ll_source)
    public void openGitHubo() {
        vibrate(getApplicationContext());
        startActivity(new Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://github.com/hiteshsahu/Magic-Keyboard")));
    }


    /**
     * This method checks if GooglePlay is installed or not on the device and accordingly handle
     * Intents to view for rate App or Publisher's Profile
     *
     * @param showPublisherProfile pass true if you want to open Publisher Page else pass false to open APp page
     * @param publisherID          pass Dev ID if you have passed PublisherProfile true
     */
    private void openPlayStore(boolean showPublisherProfile, String publisherID, Activity context) {

        //Error Handling
        if (publisherID == null || !publisherID.isEmpty()) {
            publisherID = "";
            //Log and continue
            Log.w("openPlayStore Method", "publisherID is invalid");
        }

        Intent openPlayStoreIntent;
        boolean isGooglePlayInstalled = false;

        if (showPublisherProfile) {
            //Open Publishers Profile on PlayStore
            openPlayStoreIntent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://play.google.com/store/apps/developer?id=" + "Hitesh Sahu"));
        } else {
            //Open this App on PlayStore
            openPlayStoreIntent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse("market://details?id=" + context.getPackageName()));
        }

        // find all applications who can handle openPlayStoreIntent
        final List<ResolveInfo> otherApps = context.getPackageManager()
                .queryIntentActivities(openPlayStoreIntent, 0);
        for (ResolveInfo otherApp : otherApps) {

            // look for Google Play application
            if (otherApp.activityInfo.applicationInfo.packageName.equals("com.android.vending")) {

                ActivityInfo otherAppActivity = otherApp.activityInfo;
                ComponentName componentName = new ComponentName(
                        otherAppActivity.applicationInfo.packageName,
                        otherAppActivity.name
                );
                // make sure it does NOT open in the stack of your activity
                openPlayStoreIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                // task reparenting if needed
                openPlayStoreIntent.addFlags(Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                // if the Google Play was already open in a search result
                //  this make sure it still go to the app page you requested
                openPlayStoreIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                // this make sure only the Google Play app is allowed to
                // intercept the intent
                openPlayStoreIntent.setComponent(componentName);
                context.startActivity(openPlayStoreIntent);
                isGooglePlayInstalled = true;
                break;

            }
        }
        // if Google Play is not Installed on the device, open web browser
        if (!isGooglePlayInstalled) {

            Intent webIntent;
            if (showPublisherProfile) {
                //Open Publishers Profile on web browser
                webIntent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("https://play.google.com/store/apps/developer?id=" + publisherID));
            } else {
                //Open this App on web browser
                webIntent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("https://play.google.com/store/apps/details?id=" + context.getPackageName()));
            }
            context.startActivity(webIntent);
        }
    }

    private void vibrate(Context context) {
        ((Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE))
                .vibrate(100);
    }

    private String getVersion(Context context) {
        try {
            PackageInfo pInfo = context.getPackageManager().getPackageInfo(
                    context.getPackageName(), PackageManager.GET_META_DATA);
            return String.valueOf(pInfo.versionCode) + " " + pInfo.versionName;

        } catch (PackageManager.NameNotFoundException e) {
            return "1.0.1";
        }
    }
}
