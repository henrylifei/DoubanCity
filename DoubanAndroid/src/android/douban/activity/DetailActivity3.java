package android.douban.activity;

import java.io.IOException;

import com.google.gdata.client.douban.DoubanService;
import com.google.gdata.util.ServiceException;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class DetailActivity3 extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activitydetail3);
		Bundle bundle = new Bundle();
        bundle = this.getIntent().getExtras();
        String title = bundle.getString("title");
        final String id = bundle.getString("id");
        String summary = bundle.getString("summary");
        int status = bundle.getInt("status");
        final Bundle newBundle = new Bundle();
        newBundle.putString("title", title);
        newBundle.putString("summary", summary);
        newBundle.putString("id", id);
        TextView titleView = (TextView)findViewById(R.id.title);
        titleView.setText(title);
        TextView summaryView = (TextView)findViewById(R.id.summary);
        summaryView.setText(summary);
        
        String apiKey = "0644849f5b7ba80112cd8dde1272f803";
		String secret = "892c499d27d6bc41";
		final DoubanService myService = new DoubanService("豆瓣同城", apiKey,
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
		
		
       final String status1 = "参加";
       final String status2 = "不感兴趣";
      
       Button btn1 = (Button) findViewById(R.id.button1);
       Button btn2 = (Button) findViewById(R.id.button2);
       Button btn3 = (Button) findViewById(R.id.button3);
       btn1.setText(status1);
       btn2.setText(status2);
       btn1.setOnClickListener(new OnClickListener(){

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if(status1.equals("参加")){
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
			else{
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
			Intent intent = new Intent(DetailActivity3.this, TabWidget.class);
			intent.putExtras(newBundle);
			startActivity(intent);
		}
    	   
       });
       
       btn2.setOnClickListener(new OnClickListener(){

   		@Override
   		public void onClick(View v) {
   			// TODO Auto-generated method stub
   			if(status2.equals("不感兴趣")){
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
   			Intent intent = new Intent(DetailActivity3.this, TabWidget.class);
			intent.putExtras(newBundle);
			startActivity(intent);
   		}
       	   
          });
       
       btn3.setOnClickListener(new OnClickListener(){

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent intent = new Intent(DetailActivity3.this, TabWidget.class);
			startActivity(intent);
		}
    	   
       });
	}
	

}
