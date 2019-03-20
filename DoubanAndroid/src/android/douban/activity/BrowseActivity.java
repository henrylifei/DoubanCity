package android.douban.activity;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

public class BrowseActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.browse);
		final ArrayList<String> categoryList = new ArrayList<String>();
		categoryList.add("全部");
		categoryList.add("公益");
		categoryList.add("戏剧");
		categoryList.add("展览");
		categoryList.add("电影");
		categoryList.add("音乐");
		categoryList.add("聚会");
		categoryList.add("沙龙");
		categoryList.add("运动");
		categoryList.add("旅行");
		categoryList.add("其他");
		//all, commonweal, drama, exhibition, film, music, others, party, salon, sports, travel
		final ArrayList<String> categoryList2 = new ArrayList<String>();
		categoryList2.add("all");
		categoryList2.add("commonweal");
		categoryList2.add("drama");
		categoryList2.add("exhibition");
		categoryList2.add("film");
		categoryList2.add("music");
		categoryList2.add("party");
		categoryList2.add("salon");
		categoryList2.add("sports");
		categoryList2.add("travel");
		categoryList2.add("others");
		ListView list = (ListView) findViewById(R.id.category_list);	
		list.setAdapter(new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, categoryList));
		list.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				String category = categoryList2.get(arg2);
				Bundle bundle = new Bundle();
				bundle.putString("category", category);
				bundle.putInt("page", 0);
				Intent intent = new Intent(BrowseActivity.this, browseList.class);
				intent.putExtras(bundle);
				startActivity(intent);
			}
			
		});
		Button btn = (Button) findViewById(R.id.button1);
		btn.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(BrowseActivity.this, TabWidget.class);
				startActivity(intent);
			}
	    	   
	       });
	}

}
