package android.douban.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;

public class logOut extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		SharedPreferences settings = getSharedPreferences("Douban", 0);
		Editor editor = settings.edit();
		editor.clear();
		editor.commit();
		Intent intent = new Intent(logOut.this, DoubanActivity.class);
		startActivity(intent);
	}
	

}
