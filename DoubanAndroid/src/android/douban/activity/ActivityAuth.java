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
import android.douban.util.PreferencesUtil;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

public class ActivityAuth extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		setTitle("豆瓣网");
		openWebBrowser(getRequestUrl());
	}
	
	private String getRequestUrl(){
		DoubanService myService = new DoubanService("豆瓣同城",
				"0644849f5b7ba80112cd8dde1272f803",
				"892c499d27d6bc41");
		
		String url = myService.getAuthorizationUrl("haiyang://callback");
		Log.i("检验", "url="+url);
		SharedPreferences settings = getSharedPreferences(
				"Douban", 0 );
		Editor editor = settings.edit();
		editor.putString("oauthToken", myService.getRequestToken());
		editor.putString("oauthTokenSecret", 
						myService.getRequestTokenSecret());
		editor.commit();
		Log.i("检验", myService.getRequestToken().toString());
		Log.i("检验", myService.getRequestTokenSecret().toString());
		return url;
	}
	
	protected void openWebBrowser(String url){
		
		Intent i = new Intent(Intent.ACTION_VIEW);
		i.setData(Uri.parse(url));
		startActivity(i);
	}
	

}
