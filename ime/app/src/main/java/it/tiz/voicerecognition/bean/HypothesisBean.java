package it.tiz.voicerecognition.bean;

public class HypothesisBean {
	private String text;
	private boolean isPartial;
	
	public HypothesisBean(String text, boolean isPartial) {
		this();
		this.text = text;
		this.isPartial = isPartial;
	}
	public HypothesisBean() {
	
	}
	
	public String getText() {
		return text;
	}
	
	public void setText(String text) {
		this.text = text;
	}
	
	public boolean isPartial() {
		return isPartial;
	}
	
	public void setPartial(boolean partial) {
		isPartial = partial;
	}
}
