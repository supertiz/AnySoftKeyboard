package it.tiz.voicerecognition;

import android.Manifest;

import it.tiz.moduli.Callable;
import it.tiz.moduli.android.AndroidModule;
import it.tiz.moduli.android.PermissionsModule;
import it.tiz.moduli.android.activity.AbstractActivity;
import it.tiz.moduli.android.activity.ActivityBean;

public class VoiceRecognitionActivity extends AbstractActivity {
	public static boolean isRecordAudioPermissionGranted=false;
	public static VoiceRecognitionService voiceRecognition;
	private final Callable<Void> callbackOnEndInitModel = v ->
			VoiceRecognitionUtil.callbackIconChangeStatus.call(getApplicationContext());
	
	@Override
	protected ActivityBean beforeOnCreate() {
		return null;
	}
	
	@Override
	protected void onCreate() {
		moveTaskToBack(true);
		isRecordAudioPermissionGranted= PermissionsModule.checkPermission(this, Manifest.permission.RECORD_AUDIO, PermissionsModule.PERMISSION_CODE_RECORD_AUDIO, false);
		if(isRecordAudioPermissionGranted){
			onRequestPermissionsResult(true);
		}
		AndroidModule.close(this);
	}
	
	@Override
	protected void onRequestPermissionsResult(boolean isGranted) {
		if(isGranted && voiceRecognition ==null) {
			voiceRecognition =new VoiceRecognitionService(this,callbackOnEndInitModel);
		}
	}
}
