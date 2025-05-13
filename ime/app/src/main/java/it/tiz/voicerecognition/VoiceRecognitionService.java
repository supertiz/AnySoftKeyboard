package it.tiz.voicerecognition;

import android.app.Activity;

import org.vosk.Recognizer;
import org.vosk.android.SpeechService;

import it.tiz.voicerecognition.bean.HypothesisBean;
import it.tiz.moduli.Callable;
import it.tiz.moduli.FunctionsModule;
import it.tiz.moduli.LogModule;
import it.tiz.moduli.StringsModule;
import it.tiz.moduli.android.AndroidModule;


public class VoiceRecognitionService extends AbstractVoiceRecognitionService {
	private Callable<String> callbackWrite;
	private final Activity activity;
	
	public VoiceRecognitionService(Activity activity, Callable<Void> callbackOnEndInitModel) {
		super(callbackOnEndInitModel);
		this.activity=activity;
		prepare(activity);
	}
	
	public void start(Callable<String> callbackWrite){
		LogModule.info("Starting voice recognition");
		this.callbackWrite=callbackWrite;
		recognizeMicrophone();
	}
	
	@Override
	public void onFinalResult(String hypothesis) {
		onEndProcessing(hypothesis);
	}
	
	@Override
	public void onPartialResult(String hypothesis) {
		// not used
	}
	
	@Override
	public void onResult(String hypothesis) {
		onEndProcessing(hypothesis);
	}
	
	private void onEndProcessing(String hypothesis) {
		HypothesisBean bean = getHypothesis(hypothesis);
		if(bean==null){
			return;
		}
		String text=bean.getText();
		if(StringsModule.isEmpty(text)){
			return;
		}
		if(lastHypothesis ==null){
			return;
		}
		// write text
		if(!bean.isPartial()){
			callbackWrite.call(text+ StringsModule.SPACE_STRING);
		}
		// put in cache
		lastHypothesis =bean;
	}
	
	public boolean isOn(){
		return speechService!=null;
	}
	
	private void recognizeMicrophone() {
		if (speechService != null) {
			stop();
			return;
		}
		if(model==null){
			LogModule.error("model cannot be null.");
			return;
		}
		try {
			Recognizer rec = new Recognizer(model, SAMPLE_RATE);
			speechService = new SpeechService(rec, SAMPLE_RATE);
			speechService.startListening(this);
		} catch (Exception e) {
			setErrorState(e,null);
		}
	}
	
	@Override
	protected void setErrorState(Exception exception,String message) {
		LogModule.error(FunctionsModule.fromThrowableToString(exception));
		String msg="";
		if(!StringsModule.isEmpty(message)){
			msg+=message+" ";
		}
		msg+=exception.getMessage();
		AndroidModule.toastLong(activity,msg);
	}
	
}
