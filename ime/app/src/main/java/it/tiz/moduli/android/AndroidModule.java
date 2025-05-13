package it.tiz.moduli.android;

import android.app.Activity;
import android.content.Context;
import android.graphics.BlendMode;
import android.graphics.BlendModeColorFilter;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.widget.Toast;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;


public class AndroidModule {
	private AndroidModule(){}
	
	public static void toastLong(Context context, String text){
		toast(context, text, Toast.LENGTH_LONG);
	}
	public static void toastLong(Context context, int stringId){
		toast(context, stringId, Toast.LENGTH_LONG);
	}
	public static void toastShort(Context context, String text){
		toast(context, text, Toast.LENGTH_SHORT);
	}
	public static void toastShort(Context context, int stringId){
		toast(context, stringId, Toast.LENGTH_SHORT);
	}
	
	private static void toast(Context context, int stringId, int duration){
		toast(context,context.getString(stringId),duration);
	}
	private static void toast(Context context, String text, int duration){
		Toast.makeText(context,text,duration).show();
	}
	/*
	mipmap / drawable / attr
	*/
	public static Drawable fromIdToDrawable(Context context, int drawableId){
		return ResourcesCompat.getDrawable(context.getResources(),drawableId, context.getTheme());
	}
	
	public static void setColorFilter(@NonNull Drawable drawable, @ColorInt int color) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
			drawable.setColorFilter(new BlendModeColorFilter(color, BlendMode.SRC_ATOP));
		} else {
			drawable.setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
		}
	}
	
	/*
	activity
	*/
	public static void close(Activity activity){
		activity.finish();
	}
	
}
