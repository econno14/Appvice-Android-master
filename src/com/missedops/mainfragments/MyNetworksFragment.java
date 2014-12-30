package com.missedops.mainfragments;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.facebook.android.AsyncFacebookRunner;
import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.FacebookError;
import com.facebook.FacebookException;
import com.facebook.FacebookOperationCanceledException;
import com.facebook.HttpMethod;
import com.facebook.LoggingBehavior;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.Settings;
import com.facebook.android.AsyncFacebookRunner.RequestListener;
import com.facebook.android.Facebook.DialogListener;
import com.facebook.model.GraphObject;
import com.missedops.library.R;
import com.missedops.library.SocialDatabase;
import com.missedops.library.UserFunctions;
import com.missedops.mainswipetabs.MainActivity;
import com.missedops.mainswipetabs.SplashActivity;
import com.parse.ParseInstallation;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Switch;
import android.support.v4.app.Fragment;

public class MyNetworksFragment extends Fragment {

	// Pra
	Switch fbSwtich;
	//SocialAuthAdapter adapter;
	public String soc_ntwk_id, displayname, uid;
	TextView qErrorMsg;
	private static String KEY_SUCCESS = "success";
	private static String KEY_UID = "uid";
	private static String KEY_ERROR = "error";
	private static String KEY_SOC_NW_NAME = "soc_ntwk_name";
	private static String KEY_NID = "soc_ntwk_id";
	private static String KEY_NAME = "displayname";	
	private static String APP_ID = "297841130364674";
	private Facebook facebook;
    private AsyncFacebookRunner mAsyncRunner;
    String FILENAME = "AndroidSSO_data";
    private SharedPreferences mPrefs;
    Session session;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v;
		// Pra
		
//		if ( !SplashActivity.isLoggedIn )
//		{
			Bundle b = getActivity().getIntent().getExtras();
			uid = b.getString("uid");
//		}
		v = inflater.inflate(R.layout.fragment_my_networks, container, false);

		fbSwtich = (Switch) v.findViewById(R.id.network_switch1);
		fbSwtich.setChecked(MainActivity.isFBConnected);

		// parse
		ParseInstallation installation = ParseInstallation
				.getCurrentInstallation();
		installation.put("NetId", uid);
		installation.saveInBackground();
		facebook = new Facebook(APP_ID);
        mAsyncRunner = new AsyncFacebookRunner(facebook);
		fbSwtich.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if (buttonView.isChecked()) {
					MainActivity.isFBConnected = true;
					loginToFacebook();
				} else {
					MainActivity.isFBConnected = false;
					// erase FB data
				}
			}
		});
		return v;
	}
	public void loginToFacebook() {
	    mPrefs = getActivity().getPreferences(0);
	    String access_token = mPrefs.getString("access_token", null);
	    long expires = mPrefs.getLong("access_expires", 0);
	    
	    if (access_token != null) {
	        facebook.setAccessToken(access_token);
	    }
	 
	    if (expires != 0) {
	        facebook.setAccessExpires(expires);
	    }
	 
	    if (!facebook.isSessionValid()) {
	        facebook.authorize(getActivity(),
	                new String[] { "email","publish_stream", "friends_likes", "friends_interests", "friends_location", "friends_hometown", "friends_about_me", "friends_education_history", "friends_work_history" },
	                new DialogListener() {
	 
	                    @Override
	                    public void onCancel() {
	                        // Function to handle cancel event
	                    }
	 
	                    @Override
	                    public void onComplete(Bundle values) {
	                        // Function to handle complete event
	                        // Edit Preferences and update facebook acess_token
	                        SharedPreferences.Editor editor = mPrefs.edit();
	                        editor.putString("access_token",
	                                facebook.getAccessToken());
	                        editor.putLong("access_expires",
	                                facebook.getAccessExpires());
	                        editor.commit();
	                        //getProfileInformation();       
	                    }
	
	 
	                    @Override
	                    public void onFacebookError(FacebookError fberror) {
	                        // Function to handle Facebook errors
	 
	                    }

						@Override
						public void onError(DialogError e) {
							// TODO Auto-generated method stub
							
						}

						
	 
	                });
	        
	    }
	    getProfileInformation();
	   
	}
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
	    super.onActivityResult(requestCode, resultCode, data);
	    facebook.authorizeCallback(requestCode, resultCode, data);
	    
	}
	
	public void getProfileInformation() {
		//String fqlQuery = "SELECT uid,name, music, movies FROM user WHERE uid IN " +
		        //"(SELECT uid2 FROM friend WHERE uid1 = me())";
		//String fqlQuery= "SELECT name, education.school.name, work.employer.name FROM user WHERE uid IN (SELECT uid2 FROM friend WHERE uid1 = me()) AND work AND education";
	      String fqlQuery="SELECT uid, name FROM user WHERE uid=me()";
		        Bundle params = new Bundle();
	      params.putString("q", fqlQuery);
	      params.putString("access_token", facebook.getAccessToken());
	      session = Session.getActiveSession();
	      Request request = new Request(session,
	              "/fql",                         
	              params,                         
	              HttpMethod.GET,                 
	              new Request.Callback(){  

	        @SuppressWarnings("deprecation")
	        public void onCompleted(Response response) {

	                GraphObject graphObject = response.getGraphObject();
	                System.out.println(response);

	                if (graphObject != null)
	                {
	                    if (graphObject.getProperty("data") != null)
	                    {
	                        try {
	                            String arry = graphObject.getProperty("data").toString();

	                            JSONArray jsonNArray = new JSONArray(arry);

	                          for (int i = 0; i < jsonNArray.length(); i++) {

	                               JSONObject jsonObject = jsonNArray.getJSONObject(i);

	                               displayname = jsonObject.getString("name");
	                               soc_ntwk_id = jsonObject.getString("uid");
	                              
                                Log.i("Entry", "uid: " + soc_ntwk_id + ", name: " + displayname);
                                System.out.print(displayname);
                                System.out.print(soc_ntwk_id);
                                new SocialNetwork().execute();
	                            }
	                          
	                        } catch (JSONException e) {
	                            // TODO Auto-generated catch block
	                            e.printStackTrace();
	                        }                                   
	                    }
	                }
	        }                  
	});
	Request.executeBatchAsync(request);   
	
	}
	
	private class SocialNetwork extends AsyncTask<String, String, JSONObject> {

		/**
		 * Defining Process dialog
		 **/
		private ProgressDialog pDialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(getActivity());
			pDialog.setTitle("Contacting Servers...");
			pDialog.setMessage("Synchronizing Contacts");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}

		@Override
		protected JSONObject doInBackground(String... args) {

			UserFunctions userFunction = new UserFunctions();
			Log.e("NAME",displayname);
			Log.e("UID", uid);
			Log.e("SOC_NTWK_ID", soc_ntwk_id);
			JSONObject json = userFunction.socInfo(uid, soc_ntwk_id,
					displayname);

			return json;

		}

		@Override
		protected void onPostExecute(JSONObject json) {
			/**
			 * Checks for success message.
			 **/
			try {
				if (json.getString(KEY_SUCCESS) != null) {
					// qErrorMsg.setText("");
					String res = json.getString(KEY_SUCCESS);

					String red = json.getString(KEY_ERROR);

					if (Integer.parseInt(res) == 1) {
						pDialog.setTitle("Getting Data");
						pDialog.setMessage("Loading Info");

						// qErrorMsg.setText("Successfully Recorded");

						SocialDatabase db = new SocialDatabase(getActivity());

						/**
						 * Removes all the previous data in the SQlite database
						 **/

						UserFunctions soc_nw = new UserFunctions();
						soc_nw.clearSoc(getActivity());
						db.addUser(json.getString(KEY_UID),
								json.getString(KEY_SOC_NW_NAME),
								json.getString(KEY_NID),
								json.getString(KEY_NAME));
                       
						pDialog.dismiss();

					}

					else if (Integer.parseInt(red) == 2) {
						// Make changes here...
						pDialog.dismiss();

					} else if (Integer.parseInt(red) == 3) {
						pDialog.dismiss();
						// qErrorMsg.setText("Invalid Email id");
					}

				}

				else {
					pDialog.dismiss();

					// qErrorMsg.setText("Error occured in registration");
				}

			} catch (JSONException e) {
				e.printStackTrace();

			}
		}
	}

}
