package com.missedops.logregswipetabs;


import org.json.JSONException;
import org.json.JSONObject;

import com.missedops.mainswipetabs.MainActivity;
import com.missedops.library.DatabaseHandler;
import com.missedops.library.R;
import com.missedops.library.UserFunctions;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LogInFragment extends Fragment {
	//SendMessage SM;
	EditText etLogEmail;
	EditText etLogPassword;
	Button bLogIn;
	TextView loginErrorMsg;
	String uid, name, email, pass;
	 // JSON Response node names
	    private static String KEY_SUCCESS = "success";
	    private static String KEY_UID = "uid";
	    private static String KEY_NAME = "name";
	    private static String KEY_EMAIL = "email";
	    private static String KEY_CREATED_AT = "created_at";
	    
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_log_in, container, false);
		etLogEmail = (EditText) rootView.findViewById(R.id.etLogEmail);
		etLogPassword = (EditText) rootView.findViewById(R.id.etLogPassword);
		loginErrorMsg = (TextView) rootView.findViewById(R.id.error_text);		
		bLogIn = (Button) rootView.findViewById(R.id.bLogIn);
		
		
		etLogPassword.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId,
                    KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                	bLogIn.performClick();
                    return true;
                }
                return false;
            }
			
        });
		
		
		bLogIn.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View v) {
				email= etLogEmail.getText().toString();
				pass= etLogPassword.getText().toString();
				if (  ( !etLogEmail.getText().toString().equals("")) && ( !etLogPassword.getText().toString().equals("")) )
	             {
					new ProcessLogin().execute();
	             }
	             else if ( ( !etLogEmail.getText().toString().equals("")) )
	             {
	                 Toast.makeText(getActivity(),
	                         "Password field empty", Toast.LENGTH_SHORT).show();
	             }
	             else if ( ( !etLogPassword.getText().toString().equals("")) )
	             {
	                 Toast.makeText(getActivity(),
	                         "Email field empty", Toast.LENGTH_SHORT).show();
	             }
	             else
	             {
	                 Toast.makeText(getActivity(),
	                         "Email and Password field are empty", Toast.LENGTH_SHORT).show();
	             }
				
				/*Intent i = new Intent(getActivity(), MainActivity.class);
				startActivity(i);*/
			}
			
		});
		
		return rootView;
	}
	
	private class ProcessLogin extends AsyncTask<String, String, JSONObject> {


        private ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(getActivity());
            pDialog.setTitle("Contacting Servers");
            pDialog.setMessage("Logging in ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... args) {

            UserFunctions userFunction = new UserFunctions();
            JSONObject json = userFunction.loginUser(email, pass);
            return json;
        }

        @Override
        protected void onPostExecute(JSONObject json) {
            try {
               if (json.getString(KEY_SUCCESS) != null) {

                    String res = json.getString(KEY_SUCCESS);

                    if(Integer.parseInt(res) == 1){
                        pDialog.setMessage("Loading User Space");
                        pDialog.setTitle("Getting Data");
                        DatabaseHandler db = new DatabaseHandler(getActivity());
                        System.out.println("db handler is well done");
                        JSONObject json_user = json.getJSONObject("user");
                        //String uid= json.getString(KEY_UID);
                        //System.out.println("json_user initialized successfully"+json_uid);
                        /**
                         * Clear all previous data in SQlite database.
                         **/
                        UserFunctions logout = new UserFunctions();
                        logout.logoutUser(getActivity());
                        System.out.println("logoutUser is well done"+ json_user.getString(KEY_CREATED_AT));
                        db.addUser(json_user.getString(KEY_NAME),json_user.getString(KEY_EMAIL),json.getString(KEY_UID),json_user.getString(KEY_CREATED_AT));
                                                                    
                        
                       /**
                        *If JSON array details are stored in SQlite it launches the User Panel.
                        **/
                        uid= json.getString(KEY_UID);
                        name= json_user.getString(KEY_NAME);
                                                
                        Intent intent= new Intent(getActivity(),MainActivity.class);
                        pDialog.dismiss();
                        intent.putExtra("uid", json.getString(KEY_UID));
                        intent.putExtra("username", json_user.getString(KEY_NAME));
                        startActivity(intent);
                        getActivity().finish();
                         /** Close Login Screen
                         **/
                    }else{

                        pDialog.dismiss();
                        loginErrorMsg.setText("Incorrect username/password");
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
       }
	
    
	}
}
