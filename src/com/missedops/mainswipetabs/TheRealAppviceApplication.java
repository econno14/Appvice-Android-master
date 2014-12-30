package com.missedops.mainswipetabs;
import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseUser;
import com.parse.PushService;

import android.app.Application;
public class TheRealAppviceApplication extends Application {
	@Override
	public void onCreate() {
		super.onCreate();
		// Add your initialization code here
		Parse.initialize(this, "TZN3nXf5zWEDhkl4z2GkWEV8VacutqEmFG81yoJz", "IuWnbJIVdMQP6x5DoGJVTDWmbcgwiG2h0SWRtUHn");
		PushService.setDefaultPushCallback(this, AnswerActivity.class);
		//PushService.setDefaultPushCallback(this, AskFragment.class);
		
		ParseUser.enableAutomaticUser();
		ParseACL defaultACL = new ParseACL();
	    
		// If you would like all objects to be private by default, remove this line.
		defaultACL.setPublicReadAccess(true);
		
		ParseACL.setDefaultACL(defaultACL, true);
	}
}
