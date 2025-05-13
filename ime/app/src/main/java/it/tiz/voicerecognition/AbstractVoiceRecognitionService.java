package it.tiz.voicerecognition;

import android.app.Activity;

import com.menny.android.anysoftkeyboard.R;

import org.vosk.LibVosk;
import org.vosk.LogLevel;
import org.vosk.Model;
import org.vosk.android.RecognitionListener;
import org.vosk.android.SpeechService;
import org.vosk.android.StorageService;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.Map;

import it.tiz.voicerecognition.bean.HypothesisBean;
import it.tiz.moduli.Callable;
import it.tiz.moduli.StringsModule;
import it.tiz.moduli.android.AndroidModule;


public abstract class AbstractVoiceRecognitionService implements RecognitionListener {
	public static final float SAMPLE_RATE = 16000.0f;
	
	protected Model model;
	protected SpeechService speechService;
	
	protected HypothesisBean lastHypothesis;
	private final Callable<Void> callbackOnEndInitModel;
	
	protected AbstractVoiceRecognitionService(Callable<Void> callbackOnEndInitModel) {
		super();
		this.callbackOnEndInitModel=callbackOnEndInitModel;
		this.lastHypothesis = new HypothesisBean();
	}
	
	@Override
	public void onError(Exception e) {
		setErrorState(e);
	}
	
	@Override
	public void onTimeout() {
	}
	
	protected void pause(boolean checked){
		if (speechService == null) {
			return;
		}
		speechService.setPause(checked);
	}
	
	protected void stop(){
		if (speechService == null) {
			return;
		}
		speechService.stop();
		speechService.shutdown();
		speechService=null;
	}
	
	public void prepare(Activity activity) {
		LibVosk.setLogLevel(LogLevel.INFO);
		// check mic permission
		if (!VoiceRecognitionActivity.isRecordAudioPermissionGranted) {
			AndroidModule.toastLong(activity, R.string.need_enable_mic_permission);
			return;
		}
		initModel(activity);
	}
	
	protected void initModel(Activity activity){
		if(model!=null){
			return;
		}
		Locale locale=Locale.ITALY;
		String localeId = MessageFormat.format("{0}-{1}",locale.getLanguage(),locale.getCountry()).toLowerCase();
		String folderLang="model-"+localeId;
		StorageService.unpack(activity, folderLang, "model",
				model -> {
					AbstractVoiceRecognitionService.this.model=model;
					callbackOnEndInitModel.call(null);
				},
				exception -> setErrorState(exception,"Failed to unpack the model"));
	}
	
	protected static HypothesisBean getHypothesis(String hypothesis){
		return getHypothesis(hypothesis,true);
	}
	protected static HypothesisBean getHypothesis(String hypothesis, boolean onlyFinal){
		Map<String,Object> obj= StringsModule.fromStringToJson(hypothesis);
		// check final text
		String testo=(String) obj.get("text");
		if(!StringsModule.isEmpty(testo)){
			testo= decodeCharsItaly(testo);
			return new HypothesisBean(testo,false);
		}
		if(onlyFinal){
			return null;
		}
		// check partial text
		testo=(String) obj.get("partial");
		if(!StringsModule.isEmpty(testo)){
			testo= decodeCharsItaly(testo)+"...";
			return new HypothesisBean(testo,true);
		}
		return null;
	}
	
	private static String decodeCharsItaly(String text){
		text=text.replace("virgola",",");
		text=text.replace("punto esclamativo","!");
		text=text.replace("punto di domanda","?");
		text=text.replace("due punti",":");
		// numbers
		text=text.replace(" due "," 2 ");
		text=text.replace(" tre "," 3 ");
		text=text.replace(" quattro "," 4 ");
		text=text.replace(" cinque "," 5 ");
		text=text.replace(" sette "," 7 ");
		text=text.replace(" otto "," 8 ");
		text=text.replace(" nove "," 9 ");
		text=text.replace(" dieci "," 10 ");
		return text;
	}
	
	protected void setErrorState(Exception exception) {
		setErrorState(exception, null);
	}
	protected abstract void setErrorState(Exception exception,String message);
}
