package it.tiz.moduli.android.activity;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import it.tiz.moduli.android.PermissionsModule;


public abstract class AbstractActivity extends AppCompatActivity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// preliminary actions
		ActivityBean bean = beforeOnCreate();
		if(bean!=null) {
			setContentView(bean.idLayout);
		}
		// execute other stuff
		onCreate();
	}
	
	protected abstract ActivityBean beforeOnCreate();
	protected abstract void onCreate();
	
	/*
	This function is called when the user accepts or decline the permission.
	Request Code is used to check which permission called this function.
	This request code is provided when the user is prompt for permission.
	*/
	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults){
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		boolean isGranted = PermissionsModule.onRequestPermissionsResult(this, requestCode, grantResults, true);
		onRequestPermissionsResult(isGranted);
	}
	
	protected abstract void onRequestPermissionsResult(boolean isGranted);
	
}
