package com.n00bware.propmodder;
import com.n00bware.propmodder.R;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.File;
import java.lang.*;
import java.text.*;
import java.*;
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

	public static boolean RemountRW(){
                Log.i(TAG, "Mount /system as READ/WRITE: RemountRW");
		return helper.runRootCommand("mount -o rw,remount -t yaffs2 /dev/block/mtdblock1 /system");
	}

	public static boolean RemountROnly(){
                Log.i(TAG, "Mount /system as READ ONLY: RemountROnly");
		return helper.runRootCommand("mount -o ro,remount -t yaffs2 /dev/block/mtdblock1 /system");
	}

public void credit() {
Context mContext = getApplicationContext();
Dialog dialog = new Dialog(mContext);

dialog.setContentView(R.layout.credit);
dialog.setTitle("Custom Dialog");

TextView text = (TextView) dialog.findViewById(R.id.text);
text.setText("This is a test for our CREDIT SCREEN\nCode from:\nhttp://developer.android.com/guide/topics/ui/dialogs.html#ShowingADialog");
ImageView image = (ImageView) dialog.findViewById(R.id.image);
image.setImageResource(R.drawable.ic_launcher_settings);
}

}
