package com.missedops.mainswipetabs;

import com.missedops.library.R;
import com.missedops.logregswipetabs.LogRegActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ProgressBar;

public class SplashActivity extends Activity {
	// Splash screen timer
	public static boolean isLoggedIn = false;
	public static String uid;
	private static int SPLASH_TIME_OUT = 2000;
	private ProgressBar progressBar;
	private Intent i;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);

		progressBar = (ProgressBar) findViewById(R.id.progress_bar);
		progressBar.setVisibility(View.VISIBLE);

		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
//				if (isLoggedIn) {
//					i = new Intent(SplashActivity.this, MainActivity.class);
//				} else
					i = new Intent(SplashActivity.this, LogRegActivity.class);

				startActivity(i);
				finish();
			}
		}, SPLASH_TIME_OUT);
	}

}
