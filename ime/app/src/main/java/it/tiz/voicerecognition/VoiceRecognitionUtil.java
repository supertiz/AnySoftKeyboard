package it.tiz.voicerecognition;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;

import com.menny.android.anysoftkeyboard.R;

import it.tiz.moduli.Callable;
import it.tiz.moduli.android.AndroidModule;
import it.tiz.moduli.exceptions.GenericException;
import it.tiz.voicerecognition.bean.UiBean;
import it.tiz.voicerecognition.enums.UiStatus;

public class VoiceRecognitionUtil {
	public static Callable<Context> callbackIconChangeStatus = context ->
			AndroidModule.toastShort(context,R.string.click_again_to_start);
	
	private VoiceRecognitionUtil(){}
	
	public static void onHiddenWindow(UiBean uiBean){
		iconChangeStatus(uiBean,UiStatus.OFF);
		// stop recording
		if (VoiceRecognitionActivity.voiceRecognition == null) {
			return;
		}
		VoiceRecognitionActivity.voiceRecognition.stop();
		VoiceRecognitionActivity.voiceRecognition =null;
	}
	
	public static Drawable initIcon(Context context){
		return AndroidModule.fromIdToDrawable(context, R.drawable.ic_av_mic);
	}
	public static void iconChangeStatus(UiBean uiBean, UiStatus status){
		if(uiBean.getButtonIcon() ==null){
			return;
		}
		int color;
		if(status==UiStatus.OFF){
			color=Color.WHITE;
		} else if(status==UiStatus.LOADING){
			color=Color.YELLOW;
		} else if(status==UiStatus.ON){
			color=Color.RED;
		} else {
			throw new GenericException("status not accepted: "+status);
		}
		
		AndroidModule.setColorFilter(uiBean.getButtonIcon(), color);
		uiBean.setButtonStatus(status);
	}
}
