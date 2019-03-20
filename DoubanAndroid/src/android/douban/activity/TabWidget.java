package android.douban.activity;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

public class TabWidget extends TabActivity {
		private static final String FISRT = "first";  
	    private static final String SECOND = "second";  
	    private static final String THIRD = "third";    

	    private TabHost tabHost;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		 setContentView(R.layout.tabwidget);
		 tabHost = this.getTabHost();  

		         // �����Ӧѡ��ѡ���ת����Ӧ��Activity,����TabSpec   
		         TabSpec homeSpec = tabHost.newTabSpec(FISRT).setIndicator(FISRT)  
		                 .setContent(new Intent(this, ActivityShowActivity.class));  
		         TabSpec atSpec = tabHost.newTabSpec(SECOND).setIndicator(SECOND)  
		                 .setContent(new Intent(this, BrowseActivity.class));  
		         TabSpec msgSpec = tabHost.newTabSpec(THIRD).setIndicator(THIRD)  
		                 .setContent(new Intent(this, MyActivity.class));  
		   
		         // ���TabSpec ��ѡ���   
		         tabHost.addTab(homeSpec);  
		         tabHost.addTab(atSpec);  
		         tabHost.addTab(msgSpec);    
		   
		         RadioGroup radioGroup = (RadioGroup) this  
		                 .findViewById(R.id.rg_main_btns);  
		         
		   
		         radioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {  
		   
		             /** 
		              * ��Ӧ����¼� 
		              */  
		             public void onCheckedChanged(RadioGroup group, int checkedId) {  
		   
		                 switch (checkedId) {  
		                 case R.id.first:  
		                     tabHost.setCurrentTabByTag(FISRT);  
		                     break;  
		   
		                 case R.id.second:  
		                     tabHost.setCurrentTabByTag(SECOND);  
		                     break;  
		                 case R.id.third:  
		                     tabHost.setCurrentTabByTag(THIRD);  
		                     break;  
		                 
		                 }  
		             }  
		         });  
		     }  
	

}
