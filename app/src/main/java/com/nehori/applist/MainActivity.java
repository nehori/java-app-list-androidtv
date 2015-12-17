package com.nehori.applist;

import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.view.Menu;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.content.Intent;


public class MainActivity extends AppCompatActivity {

    private PackageManager packageMgr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ArrayList<String> appList = new ArrayList<String>();
        packageMgr = getPackageManager();

        List<ApplicationInfo> applicationInfo = packageMgr.getInstalledApplications(PackageManager.GET_META_DATA);
        for (ApplicationInfo info : applicationInfo) {
            if ((info.flags & ApplicationInfo.FLAG_SYSTEM) == 0
                    && (info.flags & ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) == 0) {
                // Downloaded app
                appList.add(packageMgr.getApplicationLabel(info).toString());
            }
        }
        // Add leanback launchers
        Intent leanbackLauncherIntent = new Intent(Intent.ACTION_MAIN);
        leanbackLauncherIntent.addCategory(Intent.CATEGORY_LEANBACK_LAUNCHER);
        List<ResolveInfo> launchableApps =
// GET_META_DATA, GET_ACTIVITIES
                packageMgr.queryIntentActivities(leanbackLauncherIntent, PackageManager.GET_DISABLED_COMPONENTS
                        | PackageManager.GET_UNINSTALLED_PACKAGES);
        for (ResolveInfo info : launchableApps) {
            if (info.activityInfo != null && info.activityInfo.applicationInfo != null) {
                int flags = info.activityInfo.applicationInfo.flags;
                final String appName = info.loadLabel(packageMgr).toString();
                if ((flags & ApplicationInfo.FLAG_SYSTEM) != 0
                        || (flags & ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) != 0) {
                    appList.add(appName);
                }
            }
        }
        // Add Settings
        appList.add("Settings");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, appList);
        ListView listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
}
