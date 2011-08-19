
package com.n00bware.propmodder;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.SystemProperties;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceScreen;
import android.util.Log;

public class MainActivity extends PreferenceActivity implements
        Preference.OnPreferenceChangeListener {

    private static final String TAG = "PropModder";

    private static final String ROOT_CMD = "busybox sed -i \"/%s/ c %<s=%s\" /system/build.prop";

    private ListPreference mWifiScanPref;

    private ListPreference mLcdDensityPref;

    private ListPreference mMaxEventsPref;

    private ListPreference mRingDelayPref;

    private ListPreference mVmHeapsizePref;

    private AlertDialog mAlertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle(R.string.general_title);
        addPreferencesFromResource(R.xml.main);

        Log.d(TAG, "Loading prefs");
        PreferenceScreen prefSet = getPreferenceScreen();

        mWifiScanPref = (ListPreference) prefSet.findPreference(Constants.WIFI_SCAN_PREF);
        mWifiScanPref.setValue(SystemProperties.get(Constants.WIFI_SCAN_PERSIST_PROP,
                SystemProperties.get(Constants.WIFI_SCAN_PROP, Constants.WIFI_SCAN_DEFAULT)));
        mWifiScanPref.setOnPreferenceChangeListener(this);

        mLcdDensityPref = (ListPreference) prefSet.findPreference(Constants.LCD_DENSITY_PREF);
        mLcdDensityPref.setValue(SystemProperties.get(Constants.LCD_DENSITY_PERSIST_PROP,
                SystemProperties.get(Constants.LCD_DENSITY_PROP, Constants.LCD_DENSITY_DEFAULT)));
        mLcdDensityPref.setOnPreferenceChangeListener(this);

        mMaxEventsPref = (ListPreference) prefSet.findPreference(Constants.MAX_EVENTS_PREF);
        mMaxEventsPref.setValue(SystemProperties.get(Constants.MAX_EVENTS_PERSIST_PROP,
                SystemProperties.get(Constants.MAX_EVENTS_PROP, Constants.MAX_EVENTS_DEFAULT)));
        mMaxEventsPref.setOnPreferenceChangeListener(this);

        mRingDelayPref = (ListPreference) prefSet.findPreference(Constants.RING_DELAY_PREF);
        mRingDelayPref.setValue(SystemProperties.get(Constants.RING_DELAY_PERSIST_PROP,
                SystemProperties.get(Constants.RING_DELAY_PROP, Constants.RING_DELAY_DEFAULT)));
        mRingDelayPref.setOnPreferenceChangeListener(this);

        mVmHeapsizePref = (ListPreference) prefSet.findPreference(Constants.VM_HEAPSIZE_PREF);
        mVmHeapsizePref.setValue(SystemProperties.get(Constants.VM_HEAPSIZE_PERSIST_PROP,
                SystemProperties.get(Constants.VM_HEAPSIZE_PROP, Constants.VM_HEAPSIZE_DEFAULT)));
        mVmHeapsizePref.setOnPreferenceChangeListener(this);

        // WARN THE MASSES THIS CAN BE DANGEROUS!!!
        mAlertDialog = new AlertDialog.Builder(this).create();
        mAlertDialog.setTitle(R.string.main_warning_title);
        mAlertDialog.setMessage(getResources().getString(R.string.main_warning_summary));
        mAlertDialog.setButton(DialogInterface.BUTTON_POSITIVE,
                getResources().getString(com.android.internal.R.string.ok),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        return;
                    }
                });
        mAlertDialog.show();
    }

    public boolean onPreferenceChange(Preference preference, Object newValue) {
        if (newValue != null) {
            Log.e(TAG, "New preference selected: " + newValue);
            if (preference == mWifiScanPref) {
                return doMod(Constants.WIFI_SCAN_PERSIST_PROP, Constants.WIFI_SCAN_PROP,
                        newValue.toString());
            } else if (preference == mLcdDensityPref) {
                return doMod(Constants.LCD_DENSITY_PERSIST_PROP, Constants.LCD_DENSITY_PROP,
                        newValue.toString());
            } else if (preference == mMaxEventsPref) {
                return doMod(Constants.MAX_EVENTS_PERSIST_PROP, Constants.MAX_EVENTS_PROP,
                        newValue.toString());
            } else if (preference == mRingDelayPref) {
                return doMod(Constants.RING_DELAY_PERSIST_PROP, Constants.RING_DELAY_PROP,
                        newValue.toString());
            } else if (preference == mVmHeapsizePref) {
                return doMod(Constants.VM_HEAPSIZE_PERSIST_PROP,
                        Constants.VM_HEAPSIZE_PERSIST_PROP, newValue.toString());
            }
        }
        return false;
    }

    private boolean doMod(String persist, String key, String value) {
        SystemProperties.set(persist, value);
        Log.d(TAG, String.format("Calling script with args '%s' and '%s'", key, value));
        RootHelper.backupBuildProp();
        if (!RootHelper.remountRW()) {
            throw new RuntimeException("Could not remount /system rw");
        }
        boolean success = false;
        try {
            success = RootHelper.runRootCommand(String.format(ROOT_CMD, key, value));
            if (!success) {
                RootHelper.restoreBuildProp();
            }
        } finally {
            RootHelper.remountRO();
        }
        return success;
    }
}
