package it.tiz.voicerecognition.bean;

import android.graphics.drawable.Drawable;

import it.tiz.voicerecognition.enums.UiStatus;

public class UiBean {
	private Drawable buttonIcon;
	private UiStatus buttonStatus;
	
	public UiBean() {
		this.buttonStatus = UiStatus.OFF;
	}
	
	public Drawable getButtonIcon() {
		return buttonIcon;
	}
	
	public void setButtonIcon(Drawable buttonIcon) {
		this.buttonIcon = buttonIcon;
	}
	
	public UiStatus getButtonStatus() {
		return buttonStatus;
	}
	
	public void setButtonStatus(UiStatus buttonStatus) {
		this.buttonStatus = buttonStatus;
	}
}
