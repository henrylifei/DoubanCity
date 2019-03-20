import java.io.IOException;
import java.util.ArrayList;

import com.google.gdata.client.douban.DoubanService;
import com.google.gdata.data.douban.SubjectEntry;
import com.google.gdata.data.douban.SubjectFeed;
import com.google.gdata.data.douban.UserEntry;
import com.google.gdata.util.ServiceException;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.douban.activity.DetailActivity;
import android.douban.activity.R;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;


public class browseList extends Activity {
	int t;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		String apiKey = "0644849f5b7ba80112cd8dde1272f803";
		String secret = "892c499d27d6bc41";
		DoubanService myService = new DoubanService("subApplication", apiKey,
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
		ArrayList<String> titleList = new ArrayList<String>();
		final ArrayList<SubjectEntry> entryList = new ArrayList<SubjectEntry>();
		try{
			
			UserEntry userEntry = myService.getUser(userId);
			String city = settings.getString(
					"city", "false");
			if(city.equals(null))
			city = userEntry.getLocation();
			Log.i("检验", city);
			Bundle bundle = new Bundle();
	        bundle = this.getIntent().getExtras();
	        t = bundle.getInt("page");
	        SubjectFeed feed;
			if(t!=0){
				feed = myService.findActivity(city,"all", 1+t, 5+t);
			}
			else{
				feed = myService.findActivity(city,"all", 1, 5);
			}
			
			for (SubjectEntry sf : feed.getEntries()) {
				//Log.i("检验",sf.getActivity());
				titleList.add(sf.getActivity());
				/*String id=sf.getId();
				id= id.substring(id.lastIndexOf('/')+1);
				Log.i("检验",id);
				idList.add(id);*/
				entryList.add(sf);
			}
		} catch (IOException e) {
			e.printStackTrace();
			Log.i("检验","bbb");
		} catch (ServiceException e) {
			e.printStackTrace();
			Log.i("检验","ccc");
		}
		Log.i("检验", "efg");
		setContentView(R.layout.activitylist);
		Button btn = (Button) findViewById(R.id.button1);
		btn.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(browseList.this, browseList.class);
				Bundle bundle = new Bundle();
				bundle.putInt("page", t+1);
				startActivity(intent);
			}
			
		});
		ListView list = (ListView) findViewById(R.id.activity_list);	
		list.setAdapter(new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, titleList));
		list.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				SubjectEntry entry = entryList.get(arg2);
				Intent intent = new Intent(browseList.this, DetailActivity.class);
				Bundle bundle = new Bundle();
				bundle.putString("id", entry.getId());
				bundle.putString("summary", entry.getSummary().getPlainText());
				bundle.putString("title", entry.getActivity());
				//entry.getAttributes().get
				intent.putExtras(bundle);
				startActivity(intent);
			}
			
		});

	}

}
