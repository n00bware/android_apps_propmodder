
package com.n00bware.propmodder;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemProperties;
import android.preference.CheckBoxPreference;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceScreen;
import android.text.InputFilter;
import android.text.InputFilter.LengthFilter;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.File;

public class MainActivity extends PreferenceActivity implements
        Preference.OnPreferenceChangeListener {

    private static final String APPEND_CMD = "echo \"%s=%s\" >> /system/build.prop";
    private static final String KILL_PROP_CMD = "busybox sed -i \"/%s/D\" /system/build.prop";
    private static final String REPLACE_CMD = "busybox sed -i \"/%s/ c %<s=%s\" /system/build.prop";
    private static final String LOGCAT_CMD = "busybox sed -i \"/log/ c %s\" /system/etc/init.d/72propmodder_script";
    private static final String SDCARD_BUFFER_CMD = "busybox sed -i \"/179:0/ c echo %s > /sys/devices/virtual/bdi/179:0/read_ahead_kb\" /system/etc/init.d/72propmodder_script";
    private static final String SDCARD_BUFFER_ON_THE_FLY_CMD = "echo %s > /sys/devices/virtual/bdi/179:0/read_ahead_kb";
    private static final String TAG = "PropModder";

    private String ModPrefHolder = SystemProperties.get(Constants.MOD_VERSION_PERSIST_PROP,
                SystemProperties.get(Constants.MOD_VERSION_PROP, Constants.MOD_VERSION_DEFAULT));

    //handles for our menu hard key press
    private final int MENU_MARKET = 1;
    private final int MENU_REBOOT = 2;

    private ListPreference mWifiScanPref;
    private ListPreference mLcdDensityPref;
    private ListPreference mMaxEventsPref;
    private ListPreference mRingDelayPref;
    private ListPreference mVmHeapsizePref;
    private ListPreference mFastUpPref;
    private CheckBoxPreference mDisableBootAnimPref;
    private ListPreference mProxDelayPref;
    private CheckBoxPreference mLogcatPref;
    private EditTextPreference mModVersionPref;
    private ListPreference mSleepPref;
    private CheckBoxPreference mTcpStackPref;
    private CheckBoxPreference mJitPref;
    private CheckBoxPreference mCheckInPref;
    private ListPreference mSdcardBufferPref;
    private CheckBoxPreference m3gSpeedPref;
    private CheckBoxPreference mGpuPref;
    private AlertDialog mAlertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle(R.string.main_title_head);
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

        mFastUpPref = (ListPreference) prefSet.findPreference(Constants.FAST_UP_PREF);
        mFastUpPref.setValue(SystemProperties.get(Constants.FAST_UP_PERSIST_PROP,
                SystemProperties.get(Constants.FAST_UP_PROP, Constants.FAST_UP_DEFAULT)));
        mFastUpPref.setOnPreferenceChangeListener(this);

        mDisableBootAnimPref = (CheckBoxPreference) prefSet.findPreference(Constants.DISABLE_BOOT_ANIM_PREF);
        boolean bootAnim1 = SystemProperties.getBoolean(Constants.DISABLE_BOOT_ANIM_PROP_1, true);
        boolean bootAnim2 = SystemProperties.getBoolean(Constants.DISABLE_BOOT_ANIM_PROP_2, false);
        mDisableBootAnimPref.setChecked(SystemProperties.getBoolean(
                Constants.DISABLE_BOOT_ANIM_PERSIST_PROP, !bootAnim1 && bootAnim2));

        mProxDelayPref = (ListPreference) prefSet.findPreference(Constants.PROX_DELAY_PREF);
        mProxDelayPref.setValue(SystemProperties.get(Constants.PROX_DELAY_PERSIST_PROP,
                SystemProperties.get(Constants.PROX_DELAY_PROP, Constants.PROX_DELAY_DEFAULT)));
        mProxDelayPref.setOnPreferenceChangeListener(this);

        mLogcatPref = (CheckBoxPreference) prefSet.findPreference(Constants.LOGCAT_PREF);
        boolean rmLogging = RootHelper.runRootCommand(String.format("grep -q \"#rm -f /dev/log/main\" %s", Constants.INIT_SCRIPT_PATH));
        mLogcatPref.setChecked(rmLogging);

        mSleepPref = (ListPreference) prefSet.findPreference(Constants.SLEEP_PREF);
        mSleepPref.setValue(SystemProperties.get(Constants.SLEEP_PERSIST_PROP,
                SystemProperties.get(Constants.SLEEP_PROP, Constants.SLEEP_DEFAULT)));
        mSleepPref.setOnPreferenceChangeListener(this);

        mTcpStackPref = (CheckBoxPreference) prefSet.findPreference(Constants.TCP_STACK_PREF);
        boolean tcpstack0 = SystemProperties.getBoolean(Constants.TCP_STACK_PROP_0, false);
        boolean tcpstack1 = SystemProperties.getBoolean(Constants.TCP_STACK_PROP_1, false);
        boolean tcpstack2 = SystemProperties.getBoolean(Constants.TCP_STACK_PROP_2, false);
        boolean tcpstack3 = SystemProperties.getBoolean(Constants.TCP_STACK_PROP_3, false);
        boolean tcpstack4 = SystemProperties.getBoolean(Constants.TCP_STACK_PROP_4, false);
        mTcpStackPref.setChecked(SystemProperties.getBoolean(
                Constants.TCP_STACK_PERSIST_PROP, tcpstack0 && tcpstack1 && tcpstack2 && tcpstack3 && tcpstack4));

        mJitPref = (CheckBoxPreference) prefSet.findPreference(Constants.JIT_PREF);
        boolean jitVM = SystemProperties.getBoolean(Constants.JIT_PROP, true);
        mJitPref.setChecked(SystemProperties.getBoolean(
                Constants.LOGCAT_PERSIST_PROP, !jitVM));

        Log.d(TAG, String.format("ModPrefHoler = '%s'", ModPrefHolder)); 
        mModVersionPref = (EditTextPreference) prefSet.findPreference(Constants.MOD_VERSION_PREF);
        if (mModVersionPref != null) {
            EditText modET = mModVersionPref.getEditText();
            ModPrefHolder = mModVersionPref.getEditText().toString();
            if (modET != null){
                InputFilter lengthFilter = new InputFilter.LengthFilter(20);
                modET.setFilters(new InputFilter[]{lengthFilter});
                modET.setSingleLine(true);
            }
        }
        mModVersionPref.setOnPreferenceChangeListener(this);

        mCheckInPref = (CheckBoxPreference) prefSet.findPreference(Constants.CHECK_IN_PREF);
        boolean checkin = SystemProperties.getBoolean(Constants.CHECK_IN_PROP, true);
        mCheckInPref.setChecked(SystemProperties.getBoolean(
                Constants.CHECK_IN_PERSIST_PROP, !checkin));

        mSdcardBufferPref = (ListPreference) prefSet.findPreference(Constants.SDCARD_BUFFER_PREF);
        mSdcardBufferPref.setOnPreferenceChangeListener(this);

        m3gSpeedPref = (CheckBoxPreference) prefSet.findPreference(Constants.THREE_G_PREF);
        boolean speed3g0 = SystemProperties.getBoolean(Constants.THREE_G_PROP_0, false);
        boolean speed3g1 = SystemProperties.getBoolean(Constants.THREE_G_PROP_1, false);
        boolean speed3g2 = SystemProperties.getBoolean(Constants.THREE_G_PROP_2, false);
        boolean speed3g3 = SystemProperties.getBoolean(Constants.THREE_G_PROP_3, false);
        boolean speed3g4 = SystemProperties.getBoolean(Constants.THREE_G_PROP_4, false);
        boolean speed3g5 = SystemProperties.getBoolean(Constants.THREE_G_PROP_5, false);
        boolean speed3g6 = SystemProperties.getBoolean(Constants.THREE_G_PROP_6, false);
        boolean speed3g7 = SystemProperties.getBoolean(Constants.THREE_G_PROP_7, false);
        m3gSpeedPref.setChecked(SystemProperties.getBoolean(Constants.THREE_G_PERSIST_PROP, speed3g0 && speed3g1 && speed3g2 && speed3g3 && speed3g4 && speed3g5 && speed3g6 && speed3g7));

        mGpuPref = (CheckBoxPreference) prefSet.findPreference(Constants.GPU_PREF);
        boolean gpu = SystemProperties.getBoolean(Constants.GPU_PROP, false);
        mGpuPref.setChecked(SystemProperties.getBoolean(Constants.GPU_PERSIST_PROP, gpu));
        

        /*
         * Mount /system RW and determine if /system/tmp exists; if it doesn't
         * we make it
         */
        File tmpDir = new File("/system/tmp");
        boolean tmpDir_exists = tmpDir.exists();

        File init_d = new File("/system/etc/init.d");
        boolean init_d_exists = init_d.exists();

        File initScript = new File(Constants.INIT_SCRIPT_PATH);
        boolean initScript_exists = initScript.exists();

        if (!tmpDir_exists) {
            try {
                Log.d(TAG, "We need to make /system/tmp dir");
                RootHelper.remountRW();
                RootHelper.runRootCommand("mkdir /system/tmp");
            } finally {
                RootHelper.remountRO();
            }
        }
        if (!init_d_exists) {
            try {
                Log.d(TAG, "We need to make /system/etc/init.d/ dir");
                RootHelper.remountRW();
                RootHelper.enableInit();
            } finally {
                RootHelper.remountRO();
            }
        }
        if (!initScript_exists) {
            try {
                Log.d(TAG, String.format("init.d script not found @ '%s'", Constants.INIT_SCRIPT_PATH));
                RootHelper.remountRW();
                RootHelper.initScript();
            } finally {
                RootHelper.remountRO();
            }
        }


        // WARN THE MASSES THIS CAN BE DANGEROUS!!!
        mAlertDialog = new AlertDialog.Builder(this).create();
        mAlertDialog.setTitle(R.string.main_warning_title);
        mAlertDialog.setMessage(getResources().getString(R.string.main_warning_summary));
        mAlertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Understood, my device my problem",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        return;
                    }
                });
        mAlertDialog.show();
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "com.n00bware.propmodder.MainActivity has been paused");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "com.n00bware.propmodder.MainActivity is being resumed");
    }

    @Override
    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
        boolean value;
        if (preference == mDisableBootAnimPref) {
            value = mDisableBootAnimPref.isChecked();
            return doMod(null, Constants.DISABLE_BOOT_ANIM_PROP_1, String.valueOf(value ? 0 : 1))
                    && doMod(Constants.DISABLE_BOOT_ANIM_PERSIST_PROP,
                            Constants.DISABLE_BOOT_ANIM_PROP_2, String.valueOf(value ? 1 : 0));
        } else if (preference == mLogcatPref) {
            value = mLogcatPref.isChecked();
            return RootHelper.runRootCommand(String.format(LOGCAT_CMD, String.valueOf(value ? Constants.LOGCAT_ENABLE : Constants.LOGCAT_DISABLE)));
        } else if (preference == mTcpStackPref) {
            Log.d(TAG, "mTcpStackPref.onPreferenceTreeClick()");
            value = mTcpStackPref.isChecked();
            return doMod(null, Constants.TCP_STACK_PROP_0, String.valueOf(value ? Constants.TCP_STACK_BUFFER : Constants.DISABLE))
                    && doMod(null, Constants.TCP_STACK_PROP_1, String.valueOf(value ? Constants.TCP_STACK_BUFFER : Constants.DISABLE))
                    && doMod(null, Constants.TCP_STACK_PROP_2, String.valueOf(value ? Constants.TCP_STACK_BUFFER : Constants.DISABLE))
                    && doMod(null, Constants.TCP_STACK_PROP_3, String.valueOf(value ? Constants.TCP_STACK_BUFFER : Constants.DISABLE))
                    && doMod(Constants.TCP_STACK_PERSIST_PROP, Constants.TCP_STACK_PROP_4, String.valueOf(value ? Constants.TCP_STACK_BUFFER : Constants.DISABLE));
        } else if (preference == mJitPref) {
            Log.d(TAG, "mJitPref.onPreferenceTreeClick()");
            value = mJitPref.isChecked();
            return doMod(Constants.JIT_PERSIST_PROP, Constants.JIT_PROP, String.valueOf(value ? "int:fast" : "int:jit"));
        } else if (preference == mCheckInPref) {
            value = mCheckInPref.isChecked();
            doMod(null, Constants.CHECK_IN_PROP_HTC, String.valueOf(value ? 1 : Constants.DISABLE));
            return doMod(Constants.CHECK_IN_PERSIST_PROP, Constants.CHECK_IN_PROP, String.valueOf(value ? 1 : Constants.DISABLE));
        } else if (preference == m3gSpeedPref) {
            value = m3gSpeedPref.isChecked();
            return doMod(Constants.THREE_G_PERSIST_PROP, Constants.THREE_G_PROP_0, String.valueOf(value ? 1 : Constants.DISABLE))
            && doMod(null, Constants.THREE_G_PROP_1, String.valueOf(value ? 1 : Constants.DISABLE))
            && doMod(null, Constants.THREE_G_PROP_2, String.valueOf(value ? 2 : Constants.DISABLE))
            && doMod(null, Constants.THREE_G_PROP_3, String.valueOf(value ? 1 : Constants.DISABLE))
            && doMod(null, Constants.THREE_G_PROP_4, String.valueOf(value ? 12 : Constants.DISABLE))
            && doMod(null, Constants.THREE_G_PROP_5, String.valueOf(value ? 8 : Constants.DISABLE))
            && doMod(null, Constants.THREE_G_PROP_6, String.valueOf(value ? 1 : Constants.DISABLE))
            && doMod(null, Constants.THREE_G_PROP_7, String.valueOf(value ? 5 : Constants.DISABLE));
        } else if (preference == mGpuPref) {
            value = mGpuPref.isChecked();
            return doMod(Constants.GPU_PERSIST_PROP, Constants.GPU_PROP, String.valueOf(value ? 1 : Constants.DISABLE));
        }
        return false;
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
                return doMod(Constants.VM_HEAPSIZE_PERSIST_PROP, Constants.VM_HEAPSIZE_PROP,
                        newValue.toString());
            } else if (preference == mFastUpPref) {
                return doMod(Constants.FAST_UP_PERSIST_PROP, Constants.FAST_UP_PROP,
                        newValue.toString());
            } else if (preference == mProxDelayPref) {
                 return doMod(Constants.PROX_DELAY_PERSIST_PROP, Constants.PROX_DELAY_PROP,
                        newValue.toString());
            } else if (preference == mModVersionPref) {
                 return doMod(Constants.MOD_VERSION_PERSIST_PROP, Constants.MOD_VERSION_PROP,
                        newValue.toString());
            } else if (preference == mSdcardBufferPref) {
                 RootHelper.remountRW();
                 return RootHelper.runRootCommand(String.format(SDCARD_BUFFER_ON_THE_FLY_CMD, newValue.toString())) 
                        && RootHelper.runRootCommand(String.format(SDCARD_BUFFER_CMD, newValue.toString())) && RootHelper.remountRO();
            }
        }
        return false;
    }

    private boolean doMod(String persist, String key, String value) {
        if (persist != null) {
            SystemProperties.set(persist, value);
        }
        Log.d(TAG, String.format("Calling script with args '%s' and '%s", key, value));
        RootHelper.backupBuildProp();
        if (!RootHelper.remountRW()) {
            throw new RuntimeException("Could not remount /system rw");
        }
        boolean success = false;
        try {
            if (!RootHelper.propExists(key) && value.equals(Constants.DISABLE)) {
                Log.d(TAG, String.format("We want {%s} DISABLED however it doesn't exist so we do nothing and move on", key));
            } else if (RootHelper.propExists(key)) {
                if (value.equals(Constants.DISABLE)) {
                    Log.d(TAG, String.format("value == %s", Constants.DISABLE));
                    success = RootHelper.killProp(String.format(KILL_PROP_CMD, key));

                } else {
                    Log.d(TAG, String.format("value != %s", Constants.DISABLE));
                    success = RootHelper.runRootCommand(String.format(REPLACE_CMD, key, value));
                }

            } else {
                Log.d(TAG, "append command starting");
                success = RootHelper.runRootCommand(String.format(APPEND_CMD, key, value));
            }
            if (!success) {
                RootHelper.restoreBuildProp();
            }
        } finally {
            RootHelper.remountRO();
        }
        return success;
    }

    public boolean onCreateOptionsMenu(Menu menu){
        boolean result = super.onCreateOptionsMenu(menu);
        menu.add(0, MENU_MARKET, 0, "Please Rate PropModder").setIcon(R.drawable.market);
        menu.add(0, MENU_REBOOT, 0, "!!! REBOOT NOW !!!").setIcon(R.drawable.reboot);
        return result;
    }
 
    /* Handle the menu selection */
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case MENU_MARKET:
            Intent PropModderMarket = null;
            PropModderMarket = new Intent(Intent.ACTION_VIEW,Uri.parse("market://details?id=com.n00bware.propmodder"));
            startActivity(PropModderMarket);
            return true;
        case MENU_REBOOT:
            Toast.makeText(MainActivity.this, "REBOOTING", Toast.LENGTH_SHORT).show();
            return RootHelper.runRootCommand("reboot");
        }
        return false;
    }
}
