package com.n00bware.propmodder;
import com.n00bware.propmodder.R;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.SystemProperties;
import android.preference.CheckBoxPreference;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceCategory;
import android.preference.PreferenceScreen;
import android.provider.Settings;
import android.util.Log;
import android.widget.*;

import java.io.*;
import java.lang.*;
import java.lang.String;

public class main extends PreferenceActivity implements Preference.OnPreferenceChangeListener {

    private static String TAG = "PropModder";
    public Object CHOKE_VALUE;
    public static String function = "";

   /*
    *Strings for wifi_scan
    */
    private static final String WIFI_SCAN_PREF = "pref_wifi_scan_interval";
    private static final String WIFI_SCAN_PROP = "wifi.supplicant_scan_interval";
    private static final String WIFI_SCAN_PERSIST_PROP = "persist.wifi_scan_interval";
    private static String WIFI_SCAN_DEFAULT = System.getProperty("wifi.supplicant_scan_interval");

   /*
    * Strings for lcd_density
    */
    private static final String LCD_DENSITY_PREF = "pref_lcd_density";
    private static final String LCD_DENSITY_PROP = "ro.sf.lcd_density";
    private static final String LCD_DENSITY_PERSIST_PROP = "persist.lcd_density";
    private static String LCD_DENSITY_DEFAULT = System.getProperty("ro.sf.lcd_density");

   /*
    * Strings for max_events
    */
    private static final String MAX_EVENTS_PREF = "pref_max_events";
    private static final String MAX_EVENTS_PROP = "windowsmgr.max_events_per_sec";
    private static final String MAX_EVENTS_PERSIST_PROP = "persist.max_events";
    private static String MAX_EVENTS_DEFAULT = System.getProperty("windowsmgr.max_events_per_sec");

   /*
    * Strings for usb_mode
    */
    private static final String USB_MODE_PREF = "pref_usb_mode";
    private static final String USB_MODE_PROP = "ro.default_usb_mode";
    private static final String USB_MODE_PERSIST_PROP = "persist.usb_mode";
    private static String USB_MODE_DEFAULT = System.getProperty("ro.default_usb_mode");

   /*
    * Strings for ring_delay
    */
    private static final String RING_DELAY_PREF = "pref_ring_delay";
    private static final String RING_DELAY_PROP = "ro.telephony.call_ring.delay";
    private static final String RING_DELAY_PERSIST_PROP = "persist.call_ring.delay";
    private static String RING_DELAY_DEFAULT = System.getProperty("ro.telephony.call_ring");

   /*
    * Strings for vm_heapsize
    */
    private static final String VM_HEAPSIZE_PREF = "pref_vm_heapsize";
    private static final String VM_HEAPSIZE_PROP = "dalvik.vm.heapsize";
    private static final String VM_HEAPSIZE_PERSIST_PROP = "persist.vm_heapsize";
    private static String VM_HEAPSIZE_DEFAULT = System.getProperty("dalvik.vm.heapsize");

   /*
    * Strings for modversion
    */
    private static final String MOD_VERSION_PREF = "pref_modversion";
    private static final String MOD_VERSION_PROP = "ro.modversion";
    private static final String MOD_VERSION_PERSIST_PROP = "persist.modversion";
    private static String MOD_VERSION_DEFAULT = System.getProperty("ro.modversion");

    private ListPreference mWifiScanPref;
    private ListPreference mLcdDensityPref;
    private ListPreference mMaxEventsPref;
    private ListPreference mUsbModePref;
    private ListPreference mRingDelayPref;
    private ListPreference mVmHeapsizePref;
    private EditTextPreference mModVersionPref;
    private AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle(R.string.general_title);
        addPreferencesFromResource(R.xml.main);

        PreferenceScreen prefSet = getPreferenceScreen();

        Log.i(TAG, "loading prefs");

        helper.RemountRW();

        mWifiScanPref = (ListPreference) prefSet.findPreference(WIFI_SCAN_PREF);
        mWifiScanPref.setValue(SystemProperties.get(WIFI_SCAN_PERSIST_PROP,
                SystemProperties.get(WIFI_SCAN_PROP, WIFI_SCAN_DEFAULT)));
        mWifiScanPref.setOnPreferenceChangeListener(this);

        mLcdDensityPref = (ListPreference) prefSet.findPreference(LCD_DENSITY_PREF);
        mLcdDensityPref.setValue(SystemProperties.get(LCD_DENSITY_PERSIST_PROP,
                SystemProperties.get(LCD_DENSITY_PROP, LCD_DENSITY_DEFAULT)));
        mLcdDensityPref.setOnPreferenceChangeListener(this);

        mMaxEventsPref = (ListPreference) prefSet.findPreference(MAX_EVENTS_PREF);
        mMaxEventsPref.setValue(SystemProperties.get(MAX_EVENTS_PERSIST_PROP,
                SystemProperties.get(MAX_EVENTS_PROP, MAX_EVENTS_DEFAULT)));
        mMaxEventsPref.setOnPreferenceChangeListener(this);

      /* This may added again later
       *
       *mUsbModePref = (ListPreference) prefSet.findPreference(USB_MODE_PREF);
       *mUsbModePref.setValue(SystemProperties.get(USB_MODE_PERSIST_PROP,
       *        SystemProperties.get(USB_MODE_PROP, USB_MODE_DEFAULT)));
       *mUsbModePref.setOnPreferenceChangeListener(this);
       */

        mRingDelayPref = (ListPreference) prefSet.findPreference(RING_DELAY_PREF);
        mRingDelayPref.setValue(SystemProperties.get(RING_DELAY_PERSIST_PROP,
                SystemProperties.get(RING_DELAY_PROP, RING_DELAY_DEFAULT)));
        mRingDelayPref.setOnPreferenceChangeListener(this);

        mVmHeapsizePref = (ListPreference) prefSet.findPreference(VM_HEAPSIZE_PREF);
        mVmHeapsizePref.setValue(SystemProperties.get(VM_HEAPSIZE_PERSIST_PROP,
                SystemProperties.get(VM_HEAPSIZE_PROP, VM_HEAPSIZE_DEFAULT)));
        mVmHeapsizePref.setOnPreferenceChangeListener(this);

     /*
      * TODO: We don't want to use ListPreferece this should be a text box entry
      */
        mModVersionPref = (EditTextPreference) findPreference(MOD_VERSION_PREF);

        //another example not yet modded
        //EditText myEditText = (EditText)myEditTextPreference.getEditText(); myEditText.setKeyListener(DigitsKeyListener.getInstance(false,true));

        // WARN THE MASSES THIS CAN BE DANGEROUS!!!
        alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle(R.string.main_warning_title);
        alertDialog.setMessage(getResources().getString(R.string.main_warning_summary));
        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE,
                getResources().getString(com.android.internal.R.string.ok),
                new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                return;
            }
        });

        alertDialog.show();
    }

/* TODO This is not right
 *   void onDialogClosed(boolean positiveResult) {
 *      mModVersionPref.getText(MOD_VERSION_PERSIST_PROP);
 *      if (MOD_VERSION_PREF != MOD_VERSION_DEFAULT) {
 *          CHOKE_VALUE = mModVersionPref.getText();
 *          function = "mod_version";
 *          Log.i(TAG, "calling script with args " + function + " and " + CHOKE_VALUE);
 *          helper.runRootCommand("/tmp/pm.sh " + function + " " + CHOKE_VALUE);
 *      }
 *  }
 */

    public boolean onPreferenceChange(Preference preference, Object newValue) {
	if (newValue != null) {
            Log.i(TAG, "new preference selected: " + newValue);
            if (preference == mWifiScanPref) {
                SystemProperties.set(WIFI_SCAN_PERSIST_PROP, (String)newValue);
                CHOKE_VALUE = newValue;
                function = "wifi";
                Log.i(TAG, "calling script with args " + function + " and " + CHOKE_VALUE);
                helper.runRootCommand("busybox sed -i \"/wifi.supplicant_scan_interval/ c wifi.supplicant_scan_interval=" + newValue + "\" /system/build.prop");
            return true;

            } if (preference == mLcdDensityPref) {
                SystemProperties.set(LCD_DENSITY_PERSIST_PROP, (String)newValue);
                CHOKE_VALUE = newValue;
                function = "lcd";
                Log.i(TAG, "calling script with args " + function + " and " + CHOKE_VALUE);
                helper.runRootCommand("busybox sed -i \"/ro.sf.lcd_density/ c ro.sf.lcd_density=" + newValue + "\" /system/build.prop");
            return true;

            } if (preference == mMaxEventsPref) {
                SystemProperties.set(MAX_EVENTS_PERSIST_PROP, (String)newValue);
                CHOKE_VALUE = newValue;
                function = "max_events";
                Log.i(TAG, "calling script with args " + function + " and " + CHOKE_VALUE);
                helper.runRootCommand("busybox sed -i \"/windowsmgr.max_events_per_sec/ c windowsmgr.max_events_per_sec=" + newValue + "\" /system/build.prop");
            return true;

            } if (preference == mUsbModePref) {
                SystemProperties.set(USB_MODE_PERSIST_PROP, (String)newValue);
                CHOKE_VALUE = newValue;
                function = "usb_mode";
                Log.i(TAG, "calling script with args " + function + " and " + CHOKE_VALUE);
                helper.runRootCommand("busybox sed -i \"/ro.default_usb_mode/ c ro.default_usb_mode=" + newValue + "\" /system/build.prop");
            return true;

            } if (preference == mRingDelayPref) {
                SystemProperties.set(RING_DELAY_PERSIST_PROP, (String)newValue);
                CHOKE_VALUE = newValue;
                function = "ring_delay";
                Log.i(TAG, "calling script with args " + function + " and " + CHOKE_VALUE);
                helper.runRootCommand("busybox sed -i \"/ro.telephony.call_ring.delay/ c ro.telephony.call_ring.delay=" + newValue + "\" /system/build.prop");
            return true;

            } if (preference == mVmHeapsizePref) {
                SystemProperties.set(VM_HEAPSIZE_PERSIST_PROP, (String)newValue);
                CHOKE_VALUE = newValue;
                function = "vm_heapsize";
                Log.i(TAG, "calling script with args " + function + " and " + CHOKE_VALUE);
                helper.runRootCommand("busybox sed -i \"/dalvik.vm.heapsize/ c dalvik.vm.heapsize=" + newValue + "\" /system/build.prop");
            return true;

/*          } if (preference == mModVersionPref) {
                SystemProperties.set(MOD_VERSION_PROP, (String)newValue);
                CHOKE_VALUE = mModVersionPref.getText();
                CHOKE_PROP = MOD_VERSION_PROP;
                Log.i(TAG, "new value {" + CHOKE_PROP +"} set to CHOKE_PROP");
                Log.i(TAG, "new value {" + CHOKE_VALUE +"} set to CHOKE_VALUE");
                Log.i(TAG, "calling method SetProp with args " + CHOKE_PROP + " and " + CHOKE_VALUE);
                helper.SetProp((String)CHOKE_PROP, (String)CHOKE_VALUE);
                Log.i(TAG, "default value is " + MOD_VERSION_DEFAULT + " but it just changed so reevaluate.");
                MOD_VERSION_DEFAULT = System.getProperty(CHOKE_PROP);
                Log.i(TAG, "new " + CHOKE_PROP + " default is {" + MOD_VERSION_DEFAULT + "}"); 
            return true; */

            }
            return true;
        }
    return false;
    }
}