package com.n00bware.propmodder;
import com.n00bware.propmodder.R;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.SystemProperties;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceCategory;
import android.preference.PreferenceScreen;
import android.provider.Settings;
import android.util.Log;

import java.io.*;
import java.lang.Object;
import java.lang.Process;

public class main extends PreferenceActivity implements Preference.OnPreferenceChangeListener {

    private static String TAG = "PropModder";

   /*
    *Strings for wifi_scan
    */
    private static final String WIFI_SCAN_PREF = "pref_wifi_scan_interval";
    private static final String WIFI_SCAN_PROP = "wifi.supplicant_scan_interval";
    private static final String WIFI_SCAN_PERSIST_PROP = "persist.wifi_scan_interval";
    private static final String WIFI_SCAN_DEFAULT = System.getProperty("wifi.supplicant_scan_interval");

   /*
    * Strings for lcd_density
    */
    private static final String LCD_DENSITY_PREF = "pref_lcd_density";
    private static final String LCD_DENSITY_PROP = "ro.sf.lcd_density";
    private static final String LCD_DENSITY_PERSIST_PROP = "persist.lcd_density";
    private static final String LCD_DENSITY_DEFAULT = System.getProperty("ro.sf.lcd_density");

   /*
    * Strings for max_events
    */
    private static final String MAX_EVENTS_PREF = "pref_max_events";
    private static final String MAX_EVENTS_PROP = "windowsmgr.max_events_per_sec";
    private static final String MAX_EVENTS_PERSIST_PROP = "persist.max_events";
    private static final String MAX_EVENTS_DEFAULT = System.getProperty("windowsmgr.max_events_per_sec");

   /*
    * Strings for usb_mode
    */
    private static final String USB_MODE_PREF = "pref_usb_mode";
    private static final String USB_MODE_PROP = "ro.default_usb_mode";
    private static final String USB_MODE_PERSIST_PROP = "persist.usb_mode";
    private static final String USB_MODE_DEFAULT = System.getProperty("ro.default_usb_mode");

   /*
    * Strings for ring_delay
    */
    private static final String RING_DELAY_PREF = "pref_ring_delay";
    private static final String RING_DELAY_PROP = "ro.telephony.call_ring.delay";
    private static final String RING_DELAY_PERSIST_PROP = "persist.call_ring.delay";
    private static final String RING_DELAY_DEFAULT = System.getProperty("ro.telephony.call_ring");

   /*
    * Strings for vm_heapsize
    */
    private static final String VM_HEAPSIZE_PREF = "pref_vm_heapsize";
    private static final String VM_HEAPSIZE_PROP = "dalvik.vm.heapsize";
    private static final String VM_HEAPSIZE_PERSIST_PROP = "persist.vm_heapsize";
    private static final String VM_HEAPSIZE_DEFAULT = System.getProperty("dalvik.vm.heapsize");

   /*
    * Strings for modversion
    *
    *private static final String MODVERSION_PREF = "pref_modversion";
    *private static final String MODVERSION_PROP = "ro.modversion";
    *private static final String MODVERSION_PERSIST_PROP = "persist.modversion";
    *private static final String MODVERSION_DEFAULT = System.getProperty("ro.modversion");
    */

    private ListPreference mWifiScanPref;
    private ListPreference mLcdDensityPref;
    private ListPreference mMaxEventsPref;
    private ListPreference mUsbModePref;
    private ListPreference mRingDelayPref;
    private ListPreference mVmHeapsizePref;
    //private ListPreference mModVersionPref;
    private AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle(R.string.general_title);
        addPreferencesFromResource(R.xml.main);

        PreferenceScreen prefSet = getPreferenceScreen();

        Log.i(TAG, "loading prefs");

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

        mUsbModePref = (ListPreference) prefSet.findPreference(USB_MODE_PREF);
        mUsbModePref.setValue(SystemProperties.get(USB_MODE_PERSIST_PROP,
                SystemProperties.get(USB_MODE_PROP, USB_MODE_DEFAULT)));
        mUsbModePref.setOnPreferenceChangeListener(this);

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
      *
      * mModVersionPref = (ListPreference) prefSet.findPreference(MODVERSION_PREF);
      * mModVersionPref.setValue(SystemProperties.get(MODVERSION_PERSIST_PROP;
      *         SystemProperties.get(MODVERSION_PROP, MODVERSION_DEFAILT)));
      * mModVersionPref.setOnPreferenceChangeListener(this);
      */

    }

    public boolean onPreferenceChange(Preference preference, Object newValue) {
        if (newValue != null) {
            if (preference == mWifiScanPref) {
                SystemProperties.set(WIFI_SCAN_PERSIST_PROP, (String)newValue);
                try {
                    Log.i(TAG, "bufferedReader about to start loading /system/build.prop");
                    Process i = Runtime.getRuntime().exec("su");
                    BufferedReader in = new BufferedReader(new FileReader("/system/build.prop"));
                    PrintWriter out = new PrintWriter(new File("/tmp/build.prop"));

                    String line;
                    String params[];

                    while ((line = in.readLine()) != null) {
                        params = line.split("="); // some devices have values in ' = ' format vs '='
                    if (params[0].equalsIgnoreCase("wifi.supplicant_scan_interval ") ||
                        params[0].equalsIgnoreCase("wifi.supplicant_scan_interval")) {
                        out.println("wifi.supplicant_scan_interval=" + newValue);
                    } else {
                        out.println(line);
                        Log.e(TAG, "println failed");
                        }
                    }

                    in.close();
                    out.flush();
                    out.close();

                    // open su shell and write commands to the OutStream for execution
                    Process p = Runtime.getRuntime().exec("su");
                    PrintWriter pw = new PrintWriter(p.getOutputStream());
                    pw.println("busybox mount -o remount,rw /system");
                    pw.println("mv /tmp/build.prop /system/build.prop");
                    pw.println("exit");
                    pw.close();

                }catch(Exception e) { e.printStackTrace(); }
                return true;
            }

            if (preference == mLcdDensityPref) {
                SystemProperties.set(LCD_DENSITY_PERSIST_PROP, (String)newValue);
                try {
                    BufferedReader in = new BufferedReader(new FileReader("/system/build.prop"));
                    PrintWriter out = new PrintWriter(new File("/tmp/build.prop"));

                    String line;
                    String params[];

                    while ((line = in.readLine()) != null) {
                        params = line.split("="); // some devices have values in ' = ' format vs '='
                    if (params[0].equalsIgnoreCase("ro.sf.lcd_density ") ||
                        params[0].equalsIgnoreCase("ro.sf.lcd_density")) {
                        out.println("ro.sf.lcd_density=" + newValue);
                    } else {
                        out.println(line);
                        }
                    }

                    in.close();
                    out.flush();
                    out.close();

                    // open su shell and write commands to the OutStream for execution
                    Process p = Runtime.getRuntime().exec("su");
                    PrintWriter pw = new PrintWriter(p.getOutputStream());
                    pw.println("busybox mount -o remount,rw /system");
                    pw.println("mv /tmp/build.prop /system/build.prop");
                    pw.println("exit");
                    pw.close();

                }catch(Exception e) { e.printStackTrace(); }
                return true;
            }

            if (preference == mMaxEventsPref) {
                SystemProperties.set(MAX_EVENTS_PERSIST_PROP, (String)newValue);
                try {
                    BufferedReader in = new BufferedReader(new FileReader("/system/build.prop"));
                    PrintWriter out = new PrintWriter(new File("/tmp/build.prop"));

                    String line;
                    String params[];

                    while ((line = in.readLine()) != null) {
                        params = line.split("="); // some devices have values in ' = ' format vs '='
                    if (params[0].equalsIgnoreCase("windowsmgr.max_events_per_sec ") ||
                        params[0].equalsIgnoreCase("windowsmgr.max_events_per_sec")) {
                        out.println("windowsmgr.max_events_per_sec=" + newValue);
                    } else {
                        out.println(line);
                        }
                    }

                    in.close();
                    out.flush();
                    out.close();

                    // open su shell and write commands to the OutStream for execution
                    Process p = Runtime.getRuntime().exec("su");
                    PrintWriter pw = new PrintWriter(p.getOutputStream());
                    pw.println("busybox mount -o remount,rw /system");
                    pw.println("mv /tmp/build.prop /system/build.prop");
                    pw.println("exit");
                    pw.close();

                }catch(Exception e) { e.printStackTrace(); }
                return true;
            }

            if (preference == mUsbModePref) {
                SystemProperties.set(USB_MODE_PERSIST_PROP, (String)newValue);
                try {
                    BufferedReader in = new BufferedReader(new FileReader("/system/build.prop"));
                    PrintWriter out = new PrintWriter(new File("/tmp/build.prop"));

                    String line;
                    String params[];

                    while ((line = in.readLine()) != null) {
                        params = line.split("="); // some devices have values in ' = ' format vs '='
                    if (params[0].equalsIgnoreCase("ro.default_usb_mode ") ||
                        params[0].equalsIgnoreCase("ro.default_usb_mode")) {
                        out.println("ro.default_usb_mode=" + newValue);
                    } else {
                        out.println(line);
                        }
                    }

                    in.close();
                    out.flush();
                    out.close();

                    // open su shell and write commands to the OutStream for execution
                    Process p = Runtime.getRuntime().exec("su");
                    PrintWriter pw = new PrintWriter(p.getOutputStream());
                    pw.println("busybox mount -o remount,rw /system");
                    pw.println("mv /tmp/build.prop /system/build.prop");
                    pw.println("exit");
                    pw.close();

                }catch(Exception e) { e.printStackTrace(); }
                return true;
            }

            if (preference == mRingDelayPref) {
                SystemProperties.set(RING_DELAY_PERSIST_PROP, (String)newValue);
                try {
                    BufferedReader in = new BufferedReader(new FileReader("/system/build.prop"));
                    PrintWriter out = new PrintWriter(new File("/tmp/build.prop"));

                    String line;
                    String params[];

                    while ((line = in.readLine()) != null) {
                        params = line.split("="); // some devices have values in ' = ' format vs '='
                    if (params[0].equalsIgnoreCase("ro.telephony.call_ring.delay ") ||
                        params[0].equalsIgnoreCase("ro.telephony.call_ring.delay")) {
                        out.println("ro.telephony.call_ring.delay=" + newValue);
                    } else {
                        out.println(line);
                        }
                    }

                    in.close();
                    out.flush();
                    out.close();

                    // open su shell and write commands to the OutStream for execution
                    Process p = Runtime.getRuntime().exec("su");
                    PrintWriter pw = new PrintWriter(p.getOutputStream());
                    pw.println("busybox mount -o remount,rw /system");
                    pw.println("mv /tmp/build.prop /system/build.prop");
                    pw.println("exit");
                    pw.close();

                }catch(Exception e) { e.printStackTrace(); }
                return true;
        }

            if (preference == mVmHeapsizePref) {
                SystemProperties.set(VM_HEAPSIZE_PERSIST_PROP, (String)newValue);
                try {
                    BufferedReader in = new BufferedReader(new FileReader("/system/build.prop"));
                    PrintWriter out = new PrintWriter(new File("/tmp/build.prop"));

                    String line;
                    String params[];

                    while ((line = in.readLine()) != null) {
                        params = line.split("="); // some devices have values in ' = ' format vs '='
                    if (params[0].equalsIgnoreCase("dalvik.vm.heapsize ") ||
                        params[0].equalsIgnoreCase("dalvik.vm.heapsize")) {
                        out.println("dalvik.vm.heapsize=" + newValue);
                    } else {
                        out.println(line);
                        }
                    }

                    in.close();
                    out.flush();
                    out.close();

                    // open su shell and write commands to the OutStream for execution
                    Process p = Runtime.getRuntime().exec("su");
                    PrintWriter pw = new PrintWriter(p.getOutputStream());
                    pw.println("busybox mount -o remount,rw /system");
                    pw.println("mv /tmp/build.prop /system/build.prop");
                    pw.println("exit");
                    pw.close();

                }catch(Exception e) { e.printStackTrace(); }
                return true;
        }

        return false;
        }
    return false;
    }
}
