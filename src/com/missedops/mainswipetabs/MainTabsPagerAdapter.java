package com.missedops.mainswipetabs;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.missedops.mainfragments.AQListFragment;
import com.missedops.mainfragments.AskFragment;
import com.missedops.mainfragments.MyNetworksFragment;
import com.missedops.mainfragments.PQListFragment;
import com.missedops.mainfragments.RQListFragment;
import com.missedops.mainfragments.UQListFragment;

public class MainTabsPagerAdapter extends FragmentPagerAdapter {

	public MainTabsPagerAdapter(FragmentManager fm) {
		super(fm);
	}
	
	@Override
	public Fragment getItem(int index) {
		switch (index) {
		case 0:
			return new AskFragment();
		case 1:
			return new PQListFragment();
		case 2:
			return new RQListFragment();
		case 3:
			return new UQListFragment();
		case 4:
			return new AQListFragment();
		case 5:
			return new MyNetworksFragment();
			
		}
		return null;
	}
	
	@Override
	public int getCount() {
		//get item count - equals to num of tabs
		return 6;
	}

}
