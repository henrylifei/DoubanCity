package android.douban.activity;

import java.io.IOException;

import com.google.gdata.client.douban.DoubanService;
import com.google.gdata.util.ServiceException;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class DetailActivity extends Activity {
	
	String apiKey = "0644849f5b7ba80112cd8dde1272f803";
	String secret = "892c499d27d6bc41";
	DoubanService myService;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activitydetail);
		Bundle bundle = new Bundle();
        bundle = this.getIntent().getExtras();
        String title = bundle.getString("title");
        final String id = bundle.getString("id");
        String summary = bundle.getString("summary");
        final int status = bundle.getInt("status");
        final Bundle newBundle = new Bundle();
        newBundle.putString("title", title);
        newBundle.putString("summary", summary);
        newBundle.putString("id", id);
        newBundle.putInt("status", status);
        TextView titleView = (TextView)findViewById(R.id.title);
        titleView.setText(title);
        TextView summaryView = (TextView)findViewById(R.id.summary);
        summaryView.setText(summary);
        
        myService = new DoubanService("豆瓣同城", apiKey,
    			secret);
        SharedPreferences settings = getSharedPreferences("Douban" ,0);
    	String requestToken = settings.getString(
    			"oauthToken", "false");
    	String requestTokenSecret = settings.getString(
    			"oauthTokenSecret", "false");
    	myService.setRequestToken(requestToken);
    	myService.setRequestTokenSecret(requestTokenSecret);
    	String accessToken = settings.getString("accessToken", "false");
    	String acessTokenSecret = settings.getString("accessTokenSecret", "false");
    	String userId = settings.getString("userId", "false");
    	myService.setAccessToken(accessToken, acessTokenSecret);
		
		
		Button btn1 = (Button) findViewById(R.id.button1);
	    Button btn2 = (Button) findViewById(R.id.button2);
	    Button btn3 = (Button) findViewById(R.id.button3);
       
       if(status == 0){
    	 btn1.setText("参加");
    	 btn2.setText("感兴趣");
       }
       else if(status == 1){
    	   btn1.setText("不参加");
      	 	btn2.setText("感兴趣");
       }
       else{
    	   btn1.setText("参加");
      	 	btn2.setText("不感兴趣");
       }
         
       btn1.setOnClickListener(new OnClickListener(){

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if(status == 1){
				try {
					myService.unparticipateActivity(id);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ServiceException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else{
				try {
					myService.participateActivity(id);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ServiceException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			Intent intent = new Intent(DetailActivity.this, TabWidget.class);
			intent.putExtras(newBundle);
			startActivity(intent);
		}
    	   
       });
       
       btn2.setOnClickListener(new OnClickListener(){

   		@Override
   		public void onClick(View v) {
   			// TODO Auto-generated method stub
   			if(status == 2){
				try {
					myService.unwishActivity(id);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ServiceException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else{
				try {
					myService.wishActivity(id);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ServiceException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			Intent intent = new Intent(DetailActivity.this, TabWidget.class);
			intent.putExtras(newBundle);
			startActivity(intent);
		}
       	   
          });
       
       btn3.setOnClickListener(new OnClickListener(){

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent intent = new Intent(DetailActivity.this, TabWidget.class);
			startActivity(intent);
		}
    	   
       });
	}
	

}
