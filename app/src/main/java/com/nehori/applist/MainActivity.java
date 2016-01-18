package com.nehori.applist;

import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.content.pm.PackageManager;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.content.Intent;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private PackageManager packageMgr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ArrayList<String> appList = new ArrayList<>();
        packageMgr = getPackageManager();

        // Application on Leanback launcher
        Intent leanbackLauncherIntent = new Intent("android.intent.action.MAIN");
        leanbackLauncherIntent.addCategory(Intent.CATEGORY_LEANBACK_LAUNCHER);
        List<ResolveInfo> launchableApps = packageMgr.queryIntentActivities(leanbackLauncherIntent,
				packageMgr.GET_META_DATA | packageMgr.GET_ACTIVITIES );
          for (ResolveInfo info : launchableApps) {
            if (info.activityInfo != null) {
                String appName = info.loadLabel(packageMgr).toString();
                boolean isGame = false;
                if (info.activityInfo.applicationInfo.metaData != null) {
                    isGame = info.activityInfo.applicationInfo.metaData.getBoolean("isGame", false);
                }
                if (isGame) {
                    appName = appName + " (Game Row)";
                }
                appList.add(appName);
                Log.d(TAG, appName);
            }
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, appList);
        ListView listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(adapter);
    }
}
