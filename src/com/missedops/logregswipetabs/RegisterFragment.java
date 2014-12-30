package com.missedops.logregswipetabs;

import com.missedops.mainswipetabs.MainActivity;

import org.json.JSONException;
import org.json.JSONObject;

import com.missedops.library.DatabaseHandler;
import com.missedops.library.R;
import com.missedops.library.UserFunctions;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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

public class RegisterFragment extends Fragment {
	EditText etFullName;
	EditText etRegEmail;
	EditText etRegPassword;
	Button bRegister;

	TextView registerErrorMsg;
	String name, pass, email;
	// JSON Response node names
	private static String KEY_SUCCESS = "success";
	private static String KEY_UID = "uid";
	private static String KEY_NAME = "name";
	private static String KEY_EMAIL = "email";
	private static String KEY_CREATED_AT = "created_at";
	private static String KEY_ERROR = "error";

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_register, container,
				false);
		etFullName = (EditText) rootView.findViewById(R.id.register_name);
		etRegPassword = (EditText) rootView.findViewById(R.id.register_password);
		etRegEmail = (EditText) rootView.findViewById(R.id.register_email);
		bRegister = (Button) rootView.findViewById(R.id.register_button);
		registerErrorMsg = (TextView) rootView.findViewById(R.id.register_error_text);
		
		etRegEmail.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId,
                    KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    bRegister.performClick();
                    return true;
                }
                return false;
            }
			
        });

		// Register Button Click event
		bRegister.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				name = etFullName.getText().toString();
				pass = etRegPassword.getText().toString();
				email = etRegEmail.getText().toString();
				if ((!etRegPassword.getText().toString().equals(""))
						&& (!etFullName.getText().toString().equals(""))
						&& (!etRegEmail.getText().toString().equals(""))) {
					if (etRegEmail.getText().toString().length() > 4) {

						new ProcessRegister().execute();

					} else {
						Toast.makeText(getActivity(),
								"Username should be minimum 5 characters",
								Toast.LENGTH_SHORT).show();
					}
				} else {
					Toast.makeText(getActivity(),
							"One or more fields are empty", Toast.LENGTH_SHORT)
							.show();
				}
			}
		});

		return rootView;
	}

	private class ProcessRegister extends AsyncTask<String, String, JSONObject>
			{

		/**
		 * Defining Process dialog
		 **/
		private ProgressDialog pDialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(getActivity());
			pDialog.setTitle("Contacting Servers");
			pDialog.setMessage("Registering ...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}

		@Override
		protected JSONObject doInBackground(String... args) {

			UserFunctions userFunction = new UserFunctions();
			JSONObject json = userFunction.registerUser(name, email, pass);

			return json;

		}

		@Override
		protected void onPostExecute(JSONObject json) {
			/**
			 * Checks for success message.
			 **/
			try {
				if (json.getString(KEY_SUCCESS) != null) {
					registerErrorMsg.setText("");
					String res = json.getString(KEY_SUCCESS);

					String red = json.getString(KEY_ERROR);

					if (Integer.parseInt(res) == 1) {
						pDialog.setTitle("Getting Data");
						pDialog.setMessage("Loading Info");

						// newly added
						Toast.makeText(getActivity(),
								"Successfully Registered", Toast.LENGTH_SHORT)
								.show();

						registerErrorMsg.setText("Successfully Registered");

						DatabaseHandler db = new DatabaseHandler(getActivity());
						JSONObject json_user = json.getJSONObject("user");

						/**
						 * Removes all the previous data in the SQlite database
						 **/

						UserFunctions logout = new UserFunctions();
						logout.logoutUser(getActivity());
						db.addUser(json_user.getString(KEY_NAME),
								json_user.getString(KEY_EMAIL),
								json.getString(KEY_UID),
								json_user.getString(KEY_CREATED_AT));

						/**
						 * Stores registered data in SQlite Database Launch
						 * Registered screen
						 **/						
						Intent registered = new Intent(getActivity(),
								MainActivity.class);
						registered.putExtra("uid", json.getString(KEY_UID));
						registered.putExtra("username",	json_user.getString(KEY_NAME));

						/**
						 * Close all views before launching Registered screen
						 **/
						registered.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						pDialog.dismiss();
						startActivity(registered);
						getActivity().finish();

					}

					else if (Integer.parseInt(red) == 2) {
						pDialog.dismiss();
						registerErrorMsg.setText("User already exists");
					} else if (Integer.parseInt(red) == 3) {
						pDialog.dismiss();
						registerErrorMsg.setText("Invalid Email id");
					}

				}

				else {
					pDialog.dismiss();

					registerErrorMsg.setText("Error occured in registration");
				}

			} catch (JSONException e) {
				e.printStackTrace();

			}
		}
		
	}
}

