package it.tiz.moduli.android;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.menny.android.anysoftkeyboard.R;

public class PermissionsModule {
	public static final int PERMISSION_CODE_RECORD_AUDIO = 102;
	
	private PermissionsModule(){}
	
	public static boolean checkPermission(Activity activity, String permission, int requestCode, boolean printIfAlreadyGranted){
		int sdkVersion = Build.VERSION.SDK_INT;
		if(sdkVersion <= Build.VERSION_CODES.LOLLIPOP_MR1){
			return true;
		}
		if(ContextCompat.checkSelfPermission(activity, permission) == PackageManager.PERMISSION_DENIED) {
			ActivityCompat.requestPermissions(activity, new String[]{permission}, requestCode);
			return false;
		}
		if(printIfAlreadyGranted){
			String msg = String.format("Permesso '%s' già %s.", permission, activity.getString(R.string.granted));
			AndroidModule.toastLong(activity, msg);
		}
		return true;
	}
	
	public static boolean onRequestPermissionsResult(Activity activity, int requestCode, @NonNull int[] grantResults, boolean reloadOnSuccess){
		String msg="Permesso '";
		if (requestCode == PermissionsModule.PERMISSION_CODE_RECORD_AUDIO) {
			msg += "Registra microfono";
		} else {
			msg += "sconosciuto";
		}
		msg += "' ";
		boolean isGranted = (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED);
		if(isGranted){
			msg += activity.getString(R.string.granted);
			if(reloadOnSuccess){
				activity.recreate();
			}
		} else {
			msg += activity.getString(R.string.denied);
		}
		AndroidModule.toastLong(activity, msg);
		return isGranted;
	}
	
}
