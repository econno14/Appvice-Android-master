package com.missedops.mainswipetabs;

import com.missedops.mainswipetabs.AnswerActivity;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.missedops.library.R;
import com.missedops.library.UserFunctions;
import com.parse.ParseInstallation;
import com.parse.ParsePush;
import com.parse.ParseQuery;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class AnswerActivity extends Activity {

	String uid,question, senderId, senderName, questionId, recipientId ,recipientName;
	EditText response;
	Button respond, cancel;
	final Context context = this;
	TextView questionView, senderView;
	String newName;	
	List<Integer> ids;
	private static AnswerActivity instance;	
	private static String KEY_SUCCESS = "success";
	List<String> packet = new ArrayList<String>();

	public static AnswerActivity getInstance() {
		return instance;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_answer);
		Bundle b = getIntent().getExtras();
		senderId = b.getString("sender_id");		
		senderName = b.getString("sender_name");
		question = b.getString("question");
		questionId = b.getString("question_id");
		recipientId = b.getString("recipient_id");
		recipientName = b.getString("recipient_name");	

		questionView = (TextView) findViewById(R.id.answer_questionView);
		questionView.setText(question);		
		senderView = (TextView) findViewById(R.id.answer_SenderView);
		senderView.setText("By: " + senderName);
		respond = (Button) findViewById(R.id.answer_sendButton);
		cancel = (Button) findViewById(R.id.answer_cancelButton);
		response = (EditText) findViewById(R.id.answer_response);
		
				
		instance = this;
		ids = new ArrayList<Integer>();
		
		respond.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				sendMessageAsIntent(v);
				new CheckAns().execute();
				
				// hide keyboard
				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(response.getWindowToken(), 0);
			}

		});
		
		cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {								
				// hide keyboard
				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(response.getWindowToken(), 0);
				finish();
			}

		});
	}
	
	// newly added
	public void sendMessageAsIntent(View v)
	{
		ParseQuery<ParseInstallation> pushQuery = ParseInstallation.getQuery();
		pushQuery.whereEqualTo("NetId", senderId);		
		
	    JSONObject data = getJSONDataMessageForIntent();
	    ParsePush push = new ParsePush();
		push.setQuery(pushQuery); // Set our Installation query						
		push.setData(data);
		push.sendInBackground();
	    

	}
	
	// newly added
	private JSONObject getJSONDataMessageForIntent()
	{
	    try
	    {
	        JSONObject data = new JSONObject();	        
	        data.put("action", "com.missedops.QUESTION_ANSWERED");	        
	        data.put("sender_id", senderId);
	        data.put("sender_name", senderName);
	        data.put("question", question);	        	       
	        data.put("question_id",questionId);
	        data.put("recipient_id", recipientId );	
	        data.put("recipient_name", recipientName );
	        data.put("answer", response.getText().toString());	        	       
													        	        	       
	        return data;
	    }
	    catch(JSONException x)
	    {
	        throw new RuntimeException("Something wrong with JSON", x);
	    }
	}
	

	public class CheckAns extends AsyncTask<String, String, JSONObject> {

		private ProgressDialog pDialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(AnswerActivity.this);
			pDialog.setTitle("Accessing Servers ...");
			pDialog.setMessage("Recording Answer");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}

		@Override
		protected JSONObject doInBackground(String... args) {

			UserFunctions userFunction = new UserFunctions();
			JSONObject json = userFunction.answer( response.getText().toString(), questionId , recipientId); // could be senderId
//					response.getText().toString(), "539fee78a1af81.47586447", uid);
					
			return json;
		}

		@Override
		protected void onPostExecute(JSONObject json) {
			try {
				if (json.getString(KEY_SUCCESS) != null) {

					String res = json.getString(KEY_SUCCESS);

					if (Integer.parseInt(res) == 1) {

						//pDialog.setMessage("Thank You For Your Response XOXO <3");
						pDialog.dismiss();
						//finish();
						AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
								context);
				 
							// set title
							alertDialogBuilder.setTitle("Response Recorded");
				 
							// set dialog message
							alertDialogBuilder
								.setMessage("Thank You For Your Response!")
								.setCancelable(false)
								.setPositiveButton("OK",new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,int id) {
										// if this button is clicked, close
										// current activity
										AnswerActivity.this.finish();
										
									}
								  })
								/*.setNegativeButton("No",new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,int id) {
										// if this button is clicked, just close
										// the dialog box and do nothing
										dialog.cancel();
									}
								})*/;
				 
								// create alert dialog
								AlertDialog alertDialog = alertDialogBuilder.create();
				 
								// show it
								alertDialog.show();
							}

					} else {
						//pDialog.setMessage("You lazy ass, you are too late! Somebody beat you to it. Thanks for NOTHING! Be Ashamed of Yourself");
						pDialog.dismiss();
						AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
								context);
				 
							// set title
							alertDialogBuilder.setTitle("Response Not Recorded");
				 
							// set dialog message
							alertDialogBuilder
								.setMessage("Sorry. The question has already been answered.")
								.setCancelable(false)
								.setPositiveButton("OK",new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,int id) {
										// if this button is clicked, close
										// current activity
										AnswerActivity.this.finish();
										
									}
								  });
							AlertDialog alertDialog = alertDialogBuilder.create();
							 
							// show it
							alertDialog.show();
					}
				
			} catch (JSONException e) {
				e.printStackTrace();
			}
			pDialog.dismiss();
		}

	}

}
