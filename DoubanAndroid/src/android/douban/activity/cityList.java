package android.douban.activity;

import java.util.ArrayList;



import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class cityList extends Activity {
	private static final String[] cityList = {"北京", "上海", "广州", "武汉", "成都",
		"深圳", "杭州", "西安", "南京", "长沙", "郑州", "重庆", "天津", "福州", "温州"};
	
	private  ArrayList<String> cityName = new ArrayList<String>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.citylist);
		cityName.add("beijing");
		cityName.add("shanghai");
		cityName.add("guangzhou");
		cityName.add("wuhan");
		cityName.add("chengdu");
		cityName.add("shenzheng");
		cityName.add("hangzhou");
		cityName.add("xian");
		cityName.add("nanjing");
		cityName.add("changsha");
		cityName.add("zhengzhou");
		cityName.add("chongqing");
		cityName.add("fuzhou");
		cityName.add("wenzhou");
		ListView list = (ListView) findViewById(R.id.city_list);	
		list.setAdapter(new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, cityList));
		list.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				SharedPreferences settings = getSharedPreferences(
		    			"Douban", 0);
				Editor editor = settings.edit();
				editor.putString("city"
						, cityName.get(arg2));
				
				editor.commit();
				Intent intent = new Intent(cityList.this, TabWidget.class);
				startActivity(intent);
			}
			
		});
	}
	
	

}
