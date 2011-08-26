
package com.n00bware.propmodder;

import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public final class RootHelper {

    private static final String TAG = "PropModder";

    private static final String REMOUNT_CMD = "busybox mount -o %s,remount -t yaffs2 /dev/block/mtdblock1 /system";

    private static final String PROP_EXISTS_CMD = "grep -q %s /system/build.prop";

    private RootHelper() {
        // private constructor so nobody can instantiate this class
    }

    public static boolean runRootCommand(String command) {
        Log.d(TAG, "runRootCommand started");
        Log.d(TAG, "Attempting to run: " + command);
        Process process = null;
        DataOutputStream os = null;
        try {
            Log.d(TAG, "attempt to get root");
            process = Runtime.getRuntime().exec("su");
            os = new DataOutputStream(new BufferedOutputStream(process.getOutputStream()));
            os.writeBytes(command + "\n");
            os.writeBytes("exit\n");
            os.flush();
            return process.waitFor() == 0;
        } catch (IOException e) {
            Log.e(TAG, "IOException while flushing stream:", e);
            return false;
        } catch (InterruptedException e) {
            Log.e(TAG, "InterruptedException while executing process:", e);
            return false;
        } finally {
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    Log.e(TAG, "IOException while closing stream:", e);
                }
            }
            if (process != null) {
                process.destroy();
            }
        }
    }

    public static boolean propExists(String prop) {
        Log.d(TAG, "Checking if prop " + prop + " exists in /system/build.prop");
        return RootHelper.runRootCommand(String.format(PROP_EXISTS_CMD, prop));
    }

    public static boolean backupBuildProp() {
        Log.d(TAG, "Backing up build.prop to /system/tmp/pm_build.prop");
        return RootHelper.runRootCommand("cp /system/build.prop /system/tmp/pm_build.prop");
    }
    
    public static boolean restoreBuildProp() {
        Log.d(TAG, "Restoring build.prop from /system/tmp/pm_build.prop");
        return RootHelper.runRootCommand("cp /system/tmp/pm_build.prop /system/build.prop");
    }

    public static boolean remountRW() {
        Log.d(TAG, "Remounting /system rw");
        return RootHelper.runRootCommand(String.format(REMOUNT_CMD, "rw"));
    }

    public static boolean remountRO() {
        Log.d(TAG, "Remounting /system ro");
        return RootHelper.runRootCommand(String.format(REMOUNT_CMD, "ro"));
    }

    public static void updateShowBuild() {
        Log.d(TAG, "Setting up /system/tmp/showbuild");
        RootHelper.runRootCommand("cp /system/build.prop " + Constants.SHOWBUILD_PATH);
        RootHelper.runRootCommand("chmod 777 " + Constants.SHOWBUILD_PATH);
    }

    public static boolean killProp(String prop) {
        Log.d(TAG, String.format("User wants to disable %s", prop));
        return RootHelper.runRootCommand(prop);
    }

    public static boolean logcatAlive() {
        Log.d(TAG, "Installing script to control logcat persistance");
        RootHelper.runRootCommand(String.format("echo %s > %s", Constants.LOGCAT_ALIVE_SCRIPT, Constants.LOGCAT_ALIVE_PATH));
        //This should be find because if the chmod fails the install failed
        return RootHelper.runRootCommand("chmod 777 " + Constants.LOGCAT_ALIVE_PATH);
    }
}
