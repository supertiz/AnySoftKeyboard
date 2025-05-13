package it.tiz.voicerecognition.enums;

public enum UiStatus {
	OFF(1),
	LOADING(2),
	ON(3);
	
	private final byte status;
	UiStatus(int status) {
		this.status= (byte) status;
	}
	
	public byte getStatus() {
		return status;
	}
}
