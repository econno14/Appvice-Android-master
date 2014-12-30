package com.missedops.logregswipetabs;

import com.missedops.library.R;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.FragmentTransaction;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.View;

//import com.missedops.swipetabs.FragmentView1.SendMessage;

@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
public class LogRegActivity extends FragmentActivity implements ActionBar.TabListener {
	
	private ViewPager viewPager;
	private LogRegTabsPagerAdapter mAdapter;
	private ActionBar actionBar;
	//Tab titles
	private String[] tabs = { "LOG IN", "REGISTER" };
	
	
	//newly added
	private static final String LOG_TAG = "LOG";
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_log_reg);

                		
		//Initialization
		viewPager = (ViewPager)findViewById(R.id.pager);
		actionBar = getActionBar();
		mAdapter = new LogRegTabsPagerAdapter(getSupportFragmentManager());
		
		viewPager.setAdapter(mAdapter);
		actionBar.setHomeButtonEnabled(false);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		
		//new
		final View firstCustomView = new View(this);
		firstCustomView.setBackgroundColor(Color.BLUE);
		
		//Adding Tabs
		for (String tab_name : tabs) {
			actionBar.addTab(actionBar.newTab().setText(tab_name).setTabListener(this));
		}
		
		/**
		 * on swiping the viewpager make respective tab selected
		 */
		viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int position) {
				//make respective tab selected
				actionBar.setSelectedNavigationItem(position);
			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub
				
			}
		});
	}
	
	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		//show respective fragment view
		viewPager.setCurrentItem(tab.getPosition());
	}
	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub
		
	}
	
}
