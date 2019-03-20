package android.douban.activity;

import java.io.IOException;
import java.util.ArrayList;

import com.google.gdata.client.douban.DoubanService;
import com.google.gdata.data.douban.UserEntry;
import com.google.gdata.util.ServiceException;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.douban.util.DoubanUtil;
import android.douban.util.PreferencesUtil;
import android.os.Bundle;
import android.util.Log;

public class ActivityAuthSuccess extends Activity {
	DoubanService myService;
	SharedPreferences settings;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		Log.i("ºÏ—È","ab");
		
		saveAccessToken();
		Intent intent = new Intent(ActivityAuthSuccess.this,
				TabWidget.class);
		startActivity(intent);
	}
	
	private boolean accessTokenIsValid(){
    	SharedPreferences settings = getSharedPreferences(
    			"Douban", 0);
    	String requestToken = settings.getString("accessToken", "false");
    	if(requestToken.equals("false"))return false;
    	else return true;
    }
	
	private void saveAccessToken(){
		myService  = new DoubanService("∂π∞ÍÕ¨≥«",
				"0644849f5b7ba80112cd8dde1272f803", "892c499d27d6bc41");
		Log.i("jj", myService.toString());
		//String url = myService.getAuthorizationUrl("haiyang://callback");
		//Log.i("jj", url);
		settings = getSharedPreferences(
				"Douban",0);
		String requestToken = settings.getString(
				"oauthToken", "false");
		String requestTokenSecret = settings.getString(
				"oauthTokenSecret", "false");
		Log.i("ja", requestToken);
		myService.setRequestToken(requestToken);
		Log.i("jb", requestTokenSecret);
		myService.setRequestTokenSecret(requestTokenSecret);
		Log.i("ax", myService.getRequestToken());
		ArrayList<String> list = myService.getAccessToken();
		Log.i("jc", list.toString());
		String accessToken = list.get(0);
		String accessTokenSecret = list.get(1);
		Editor editor = settings.edit();
		editor.putString("accessToken"
				, accessToken);
		editor.putString("accessTokenSecret"
						, accessTokenSecret);
		editor.commit();
		
		UserEntry userEntry;
		try {
			userEntry = myService.getAuthorizedUser();
			settings.edit().putString("userId", 
					userEntry.getUid()).commit();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//return url;
		
	}
	
	
}
