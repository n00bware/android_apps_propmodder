
package com.n00bware.propmodder;

import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.FileWriter;

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
        try {
            RootHelper.remountRW();
            RootHelper.runRootCommand("cp -f /system/build.prop " + Constants.SHOWBUILD_PATH);
            RootHelper.runRootCommand("chmod 777 " + Constants.SHOWBUILD_PATH);
        } finally {
            RootHelper.remountRO();
        }
    }

    public static boolean killProp(String prop) {
        Log.d(TAG, String.format("User wants to disable so lets run { %s }", prop));
        return RootHelper.runRootCommand(prop);
    }

    public static boolean initScript() {
        FileWriter wAlive;
        try {
            wAlive = new FileWriter(Constants.INIT_SCRIPT_TEMP_PATH);
            //forgive me but without all the \n's the script is one line long O:-)
            wAlive.write("#\n#init.d script by PropModder\n#\n\n");
            wAlive.write("#rm -f /dev/log/main\n");
            wAlive.write("#echo 2048 > /sys/devices/virtual/bdi/179:0/read_ahead_kb");
            wAlive.flush();
            wAlive.close();
            RootHelper.runRootCommand(String.format("cp -f %s %s", Constants.INIT_SCRIPT_TEMP_PATH, Constants.INIT_SCRIPT_PATH));
            //This should be find because if the chmod fails the install failed
            return RootHelper.runRootCommand(String.format("chmod 755 %s", Constants.INIT_SCRIPT_PATH));
        } catch(Exception e) {
            Log.e(TAG, "initScript install failed: " + e);
            e.printStackTrace();
        }
        return false;
    }

    public static boolean enableInit() {
        FileWriter wAlive;
        try {
            wAlive = new FileWriter("/system/tmp/initscript");
            //forgive me but without all the \n's the script is one line long O:-)
            wAlive.write("#\n#enable init.d script by PropModder\n#\n\n");
            wAlive.write("log -p I -t boot \"Starting init.d ...\"\n");
            wAlive.write("busybox run-parts /system/etc/init.d");
            wAlive.flush();
            wAlive.close();
            RootHelper.runRootCommand("cp -f /system/tmp/initscript /system/usr/bin/init.sh");
            //This should be find because if the chmod fails the install failed
            return RootHelper.runRootCommand("chmod 755 /system/usr/bin/pm_init.sh");
        } catch(Exception e) {
            Log.e(TAG, "enableInit install failed: " + e);
            e.printStackTrace();
        }
        return false;
    }
}
