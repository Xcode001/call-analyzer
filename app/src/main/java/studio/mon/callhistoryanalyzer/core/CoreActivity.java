package studio.mon.callhistoryanalyzer.core;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;


public abstract class CoreActivity extends FragmentActivity implements Serializable {
	private static String TAG = "MonActivity";

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		FontsOverride.setDefaultFont(this, "DEFAULT", "fonts/helvetica.ttf");


	}

	public static String getCurrentTimezoneOffset() {

		TimeZone tz = TimeZone.getDefault();
		Log.i("TimeZone" , "" + tz);
		Calendar cal = GregorianCalendar.getInstance(tz);
		int offsetInMillis = tz.getOffset(cal.getTimeInMillis());

		Log.i("offsetInMillis" , "" + offsetInMillis);

		String offset = String.format("%02d:%02d", Math.abs(offsetInMillis / 3600000), Math.abs((offsetInMillis / 60000) % 60));
		offset = (offsetInMillis >= 0 ? "+" : "-") + offset;

		return offset;
	}


	public int getActionBarHeight() {
	    int actionBarHeight = 0;
	    TypedValue tv = new TypedValue();
	    if (getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true))
	    {
	       actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data,getResources().getDisplayMetrics());
	    }
	    return actionBarHeight;
	}

	public int getStatusBarHeight() {
	    int result = 0;
	    int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
	    if (resourceId > 0) {
	        result = getResources().getDimensionPixelSize(resourceId);
	    }
	    return result;
	}
	public void setStatusBarColor(View statusBar,int color){
	    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
	           Window w = getWindow();
	           w.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
	           int actionBarHeight = getActionBarHeight();
	           statusBar.getLayoutParams().height = getStatusBarHeight();
	           statusBar.setBackgroundColor(color);
	     }
	}
	public void openAppInStore(String appPackage) {
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setData(Uri.parse("market://details?id=" + appPackage));
		startActivity(intent);
	}
	public void openOtherApp(String appPackage) {
		PackageManager manager = getPackageManager();
		try {
		    Intent i = manager.getLaunchIntentForPackage(appPackage);
		    if (i == null){
		    	throw new PackageManager.NameNotFoundException();
		    }
		    i.addCategory(Intent.CATEGORY_LAUNCHER);
		    startActivity(i);
		} catch (PackageManager.NameNotFoundException e) {
			openAppInStore(appPackage);
			e.printStackTrace();
		}
	}
	public void hide_keyboard() {
	    InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
	    View view = getCurrentFocus();
	    if(view == null) {
	        view = new View(this);
	    }
	    inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
	}
	public void showKeyboard() {
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);
	}
	public void loge(final Object msg) {
		if(msg == null) {
			Log.e(TAG, "NULL");
		} else {
			Log.e(TAG, msg.toString());
		}
	}
	public void logd(final Object msg) {
		if(msg == null) {
			Log.d(TAG, "NULL");
		} else {
			Log.d(TAG, msg.toString());
		}
	}
	public void logi(final Object msg) {
		if(msg == null) {
			Log.i(TAG, "NULL");
		} else {
			Log.i(TAG, msg.toString());
		} 
	}
	public String readStringFromAsset(String path) {
		StringBuilder sb = new StringBuilder();
		BufferedReader reader = null;
		try {
		    reader = new BufferedReader(new InputStreamReader(getAssets().open(path), "UTF-8")); 
		    String mLine = reader.readLine();
		    while (mLine != null) {
		    	sb.append(mLine);
		    	mLine = reader.readLine(); 
		    }
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
		    if (reader != null) {
		         try {
		             reader.close();
		         } catch (IOException e) {
		        	 e.printStackTrace();
		         }
		    }
		}
		return sb.toString();
	}
    public String readStringFromSDCard(String path) {
    	StringBuilder sb = new StringBuilder();
		try {
			File myFile = new File(path);
			FileInputStream fIn = new FileInputStream(myFile);
			BufferedReader myReader = new BufferedReader(new InputStreamReader(fIn));
			String aDataRow = "";
			while ((aDataRow = myReader.readLine()) != null) {
				sb.append(aDataRow).append("\n");
			}
			myReader.close();
			logi(new StringBuilder().append("Done. Read SDCard [").append(path).append(sb.toString()).toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return sb.toString();
    }
    public void writeStringToSDCard(String path, String msg) {
    	File myFile = new File(path);
		try {
			myFile.createNewFile();
			FileOutputStream fOut = new FileOutputStream(myFile);
			OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);
			myOutWriter.append(msg);
			myOutWriter.close();
			fOut.close();
			logi(new StringBuilder().append("Done. Write to SDCard [").append(path).append("]: ").append(msg).toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
	
    public boolean hasCamera(CoreActivity context) {
		boolean state = false;
		if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
			return state = true;
		} 
		logi(new StringBuilder().append("Done. Check has Camera: ").append(state).toString());
		return state;
	}
	public boolean hasSDCard() {
		boolean state = false;
		String sd = Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED.equals(sd)) {
			state = true;
		}
		logi(new StringBuilder().append("Done. Check has SDCard: ").append(state).toString());
		return state;
	}
	
	public void showToastShort(final Object message) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				if(message == null) {
					Toast.makeText(CoreActivity.this, "NULL", Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(CoreActivity.this, message.toString(), Toast.LENGTH_SHORT).show();
				}
			}
		});
		logi(new StringBuilder().append("Done. Show short Toast: ").append(message).toString());
	}
	public void showToastLong(final Object message) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				if(message == null) {
					Toast.makeText(CoreActivity.this, "NULL", Toast.LENGTH_LONG).show();
				} else {
					Toast.makeText(CoreActivity.this, message.toString(), Toast.LENGTH_LONG).show();
				}
			}
		});
		logi(new StringBuilder().append("Done. Show long Toast: ").append(message).toString());
	}
	
	public String readSharedPreferences(String preferencesName, String elementName) {
		SharedPreferences pre = getSharedPreferences(preferencesName, MODE_PRIVATE);
		String s = pre.getString(elementName, "");
		logi(new StringBuilder().append("Done. Read shared preferences [ ")
				.append(preferencesName).append("]:[").append(elementName)
				.append("]: ").append(s).toString());
		return s;
	}
	public void saveSharedPreferences(String preferencesName, String elementName, String data) {
		SharedPreferences pre = getSharedPreferences(preferencesName, MODE_PRIVATE);
		SharedPreferences.Editor edit = pre.edit();
		edit.putString(elementName, data);
		edit.commit();
		logi(new StringBuilder().append("Done. Write shared preferences [ ")
				.append(preferencesName).append("]:[").append(elementName)
				.append("]: ").append(data).toString());
	}
	
	private android.support.v4.app.FragmentManager mFragmentManager = getSupportFragmentManager();
	public void removePreviousDialog() {
		FragmentTransaction ft = mFragmentManager.beginTransaction();
		Fragment prev = mFragmentManager.findFragmentByTag(TAG);
		if (prev != null) ft.remove(prev);
		ft.commit();
		logi(new StringBuilder().append("Done. Remove previous dialog. TAG = ").append(TAG).toString());
	}
	
	public void switchContent(int contentId, Fragment fragment, String tag) {
		FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
		transaction.replace(contentId, fragment);
		transaction.addToBackStack(tag);
		transaction.commitAllowingStateLoss();
	}
	public void switchContentWithAnimation(int contentId, Fragment fragment, int arg0, int arg1, String tag) {
		FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
		transaction.setCustomAnimations(arg0, arg1);
		transaction.replace(contentId, fragment);
		transaction.addToBackStack(tag);
		transaction.commitAllowingStateLoss();
	}
	public void switchContentWithAnimation(int contentId, Fragment fragment, int arg0, int arg1, int arg2, int arg3, String tag) {
		FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
		transaction.setCustomAnimations(arg0, arg1, arg2, arg3);
		transaction.replace(contentId, fragment);
		transaction.addToBackStack(tag);
		transaction.commitAllowingStateLoss();
	}
	public void switchContent(int contentId, Fragment fragment) {
		FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
		transaction.replace(contentId, fragment);
		transaction.commitAllowingStateLoss();
	}
	public void switchContentWithAnimation(int contentId, Fragment fragment, int arg0, int arg1) {
		FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
		transaction.setCustomAnimations(arg0, arg1);
		transaction.replace(contentId, fragment);
		transaction.commitAllowingStateLoss();
	}
	public void switchContentWithAnimation(int contentId, Fragment fragment, int arg0, int arg1, int arg2, int arg3) {
		FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
		transaction.setCustomAnimations(arg0, arg1, arg2, arg3);
		transaction.replace(contentId, fragment);
		transaction.commitAllowingStateLoss();
	}

	
	protected abstract void initViews();
	protected abstract void initModels();
	protected abstract void initListeners();
	protected abstract void initAnimations();

}
