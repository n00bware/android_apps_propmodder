package com.n00bware.propmodder;
import com.n00bware.propmodder.R;

import java.io.*;
import java.lang.*;
import java.text.*;
import android.*;
import android.app.*;
import android.app.Dialog;
import android.content.*;
import android.text.*;
import android.view.*;
import android.widget.*;

import android.util.Log;

public class helper {
    public static String TAG = "PropModder";

    public static boolean runRootCommand(String command) {
        Log.i(TAG, "runRootCommand started");
        Log.i(TAG, "Attempting to run: " + command);
        Process process = null;
        DataOutputStream os = null;
        try {
            Log.i(TAG, "attempt to get root");
	    process = Runtime.getRuntime().exec("su");
	    os = new DataOutputStream(process.getOutputStream());
	    os.writeBytes(command+"\n");
	    os.writeBytes("exit\n");
	    os.flush();
	    process.waitFor();
	} catch (Exception e) {
	    Log.d("*** DEBUG ***", "Unexpected error - Here is what I know: "+e.getMessage());
	return false;
	}
        finally {
            try {
	        if (os != null) os.close();
	            process.destroy();
	        } catch (Exception e) {}
	    }
        return true;
    }

    public static boolean MakeTmp() {
        Log.i(TAG, "Copying build.prop to /tmp/pm_build.prop for modding");
        return helper.runRootCommand("cp /system/build.prop /tmp/pm_build.prop");
    }

    public static boolean RemountRW() {
        Log.i(TAG, "Mount /system as READ/WRITE: RemountRW");
        return helper.runRootCommand("busybox mount -o rw,remount -t yaffs2 /dev/block/mtdblock1 /system");
    }

    public static boolean RemountROnly(){
        Log.i(TAG, "Mount /system as READ ONLY: RemountROnly");
        return helper.runRootCommand("busybox mount -o ro,remount -t yaffs2 /dev/block/mtdblock1 /system");
    }

    public static void InstallScript(){
        Log.i(TAG, "start InstallScript method");
        try {
            helper.RemountRW();

            FileWriter fstream = new FileWriter("/tmp/pm.sh");
		BufferedWriter out = new BufferedWriter(fstream);
                out.write("#!/system/bin/sh\n\n");
                out.write("BB=/system/xbin/busybox\nTMP_DIR=/tmp\nTMP_FILE=/tmp\n/pm_build.prop\nBUILDPROP=/system/build.prop\nFUNCTION=$1\nNEWVALUE=$2\n\n");
                out.write("apply()\n{\ncp -f $TMP_FILE $BUILDPROP\nrm -f $TMP_FILE\n}\n\n");
                out.write("reboot()\n{\nrm -f $TMP_FILE\n}\n\n");
                out.write("apply_reboot()\n{\ncp -f $TMP_FILE $BUILDPROP\nrm -f $TMP_FILE\nreboot\n}");
                out.write("wifi()\n{\n####debug\nsu\ntouch /tmp/pm_proof\n$BB sed -i \"/wifi.supplicant_scan_interval/ c wifi.supplicant_scan_interval = $1\" $TMP_FILE\n}\n\n");
                out.write("lcd()\n{\n$BB sed -i \"/ro.sf.lcd_density/ c ro.sf.lcd_density=$1\" $TMP_FILE\n}\n\n");
                out.write("max_events()\n{\n$BB sed -i \"/windowsmgr.max_events_per_sec/ c windowsmgr.max_events_per_sec=$1\" $TMP_FILE\n}\n\n");
                out.write("usb_mode()\n{\n$BB sed -i \"/ro.default_usb_mode/ c ro.default_usb_mode=$1\" $TMP_FILE\n}\n\n");
                out.write("ring_delay()\n{\n$BB sed -i \"/ro.telephony.call_ring.delay/ c ro.telephony.call_ring.delay=$1\" $TMP_FILE\n}\n\n");
                out.write("vm_heapsize()\n{\n$BB sed -i \"/dalvik.vm.heapsize/ c dalvik.vm.heapsize=$1\" $TMP_FILE\n}\n\n");
                out.write("mod_version()\n{\n$BB sed -i \"/ro.modversion/ c ro.modversion=$@\" $TMP_FILE\n}\n\n");
                out.write("if $BB [ ! -e /tmp/pm_build.prop ]\n  then\n    cp -f $BUILDPROP $TMP_FILE\nfi\n\n");
                out.write("if $BB [[ \"$FUNCTION\" = \"mod_version\" ]]\n  then\n    NEWVALUE=\"$(echo $@ | sed 's/mod_version//')\"\nfi\n\n");
                out.write("#Begin Debug");
                out.write("echo \"FUNCTION = $FUNCTION\"\necho \"NEWVALUE = $NEWVALUE\"\n$FUNCTION $NEWVALUE");

                out.close();
                fstream.close();
                helper.runRootCommand("chmod 0755 /tmp/pm.sh");

        }catch(Exception e) { e.printStackTrace();
           Log.e(TAG, "***DEBUG***: " + e);}
    }

    public static void ClearChokes(String CHOKE_PROP, String CHOKE_VALUE){
        Log.i(TAG, "SENDING values of CHOKE_PROP and CHOKE_VALUES to null " + CHOKE_PROP + " " + CHOKE_VALUE);
        CHOKE_PROP = " ";
        CHOKE_VALUE = " ";
        Log.i(TAG, "values of CHOKE_PROP and CHOKE_VALUES should be whitespace: " + "{" + CHOKE_PROP + "} {" + CHOKE_VALUE + "}");
    }
}
