package android.douban.activity;



import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.douban.util.PreferencesUtil;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;


public class DoubanActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.main);
       /* Button button1 = (Button) findViewById(R.id.button1);
        button1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
            	 setContentView(R.layout.test);
            }
       });
        String apiKey =  "0644849f5b7ba80112cd8dde1272f803";
        String secret = "892c499d27d6bc41";
        DoubanService myService = new DoubanService("豆瓣同城", apiKey, secret);
        String url = myService.getAuthorizationUrl("null");
        myService.getAccessToken();*/
        if(!accessTokenIsValid())showAlertDialog();
       /* else if(!accessTokenIsValid()){
        	Log.i("检验", "ac");
        	Intent intent = new Intent(DoubanActivity.this, ActivityAuthSuccess.class);
        	startActivity(intent);
        }*/
        else{
        	Intent intent = new Intent(DoubanActivity.this, TabWidget.class);
        	startActivity(intent);
        }
        
    }
    private boolean oauthTokenIsValid(){
    	SharedPreferences settings = getSharedPreferences(
    			"Douban", 0);
    	String requestToken = settings.getString("oauthToken", "false");
    	if(requestToken.equals("false"))return false;
    	else return true;
    }
    private boolean accessTokenIsValid(){
    	SharedPreferences settings = getSharedPreferences(
    			"Douban", 0);
    	String requestToken = settings.getString("accessToken", "false");
    	if(requestToken.equals("false"))return false;
    	else return true;
    }
	private void showAlertDialog(){
    	final AlertDialog alert = new AlertDialog.Builder(this).create();
    	alert.setMessage("第一次登陆，请授权");
    	alert.setButton("登陆", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				alert.dismiss();
				Intent intent = new Intent(DoubanActivity.this,
						ActivityAuth.class);
				startActivity(intent);
				
			}
		});
    	alert.show();
    }
    
    protected void openWebBrowser(String url){
    	Intent i = new Intent(Intent.ACTION_VIEW);
    	i.setData(Uri.parse(url));
    	startActivity(i);
    }
    
}