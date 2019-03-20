package android.douban.activity;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.RadioGroup;
import android.widget.TabHost;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TabHost.TabSpec;

public class MyActivity extends TabActivity {

	private static final String FISRT = "first";  
    private static final String SECOND = "second";    

    private TabHost tabHost;
@Override
protected void onCreate(Bundle savedInstanceState) {
	// TODO Auto-generated method stub
	super.onCreate(savedInstanceState);
	 setContentView(R.layout.myactivity);
	 tabHost = this.getTabHost();

	         // 点击相应选项选项，跳转到相应的Activity,创建TabSpec   
	         TabSpec homeSpec = tabHost.newTabSpec(FISRT).setIndicator(FISRT)  
	                 .setContent(new Intent(this, Participated.class));  
	         TabSpec atSpec = tabHost.newTabSpec(SECOND).setIndicator(SECOND)  
	                 .setContent(new Intent(this, Interested.class));  
	   
	         // 添加TabSpec 到选项卡中   
	         tabHost.addTab(homeSpec);  
	         tabHost.addTab(atSpec);  
	   
	         RadioGroup radioGroup = (RadioGroup) this  
	                 .findViewById(R.id.rg_main_btns);  
	         
	   
	         radioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {  
	   
	             /** 
	              * 响应点击事件 
	              */  
	             public void onCheckedChanged(RadioGroup group, int checkedId) {  
	   
	                 switch (checkedId) {  
	                 case R.id.first:  
	                     tabHost.setCurrentTabByTag(FISRT);  
	                     break;  
	   
	                 case R.id.second:  
	                     tabHost.setCurrentTabByTag(SECOND);  
	                     break;  
	                 
	                 }  
	             }  
	         });  
	     }  
	

}
