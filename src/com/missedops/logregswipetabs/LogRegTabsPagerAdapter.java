package com.missedops.logregswipetabs;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
//import android.support.v4.app.FragmentPagerAdapter;
//import android.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentPagerAdapter;

public class LogRegTabsPagerAdapter extends FragmentPagerAdapter {

	public LogRegTabsPagerAdapter(FragmentManager fm) {
		super(fm);
	}
	
	@Override
	public Fragment getItem(int index) {
		switch (index) {
		case 0:
			return new LogInFragment();
		case 1:
			return new RegisterFragment();
		}
		return null;
	}
	
	@Override
	public int getCount() {
		//get item count - equals to num of tabs
		return 2;
	}
}
