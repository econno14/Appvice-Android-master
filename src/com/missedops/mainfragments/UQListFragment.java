package com.missedops.mainfragments;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.missedops.mainswipetabs.AnswerActivity;
import com.missedops.library.R;
import com.missedops.library.UserFunctions;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.support.v4.app.Fragment;

public class UQListFragment extends Fragment {
	ListView question;
	List <String> array= new ArrayList<String>();
	List <String> qidarray= new ArrayList<String>();
	List <String> sender_name= new ArrayList<String>();
	List <String> sender_id= new ArrayList<String>();
	String username, uid;
    int i=0;
    View v;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		v = inflater.inflate(R.layout.fragment_uq, container, false);
		Bundle b = getActivity().getIntent().getExtras();
		username= b.getString("username");
		uid= b.getString("uid");
		new getUnansweredQuestions().execute();
		return v;
	}
	private class getUnansweredQuestions extends AsyncTask<String, String, JSONObject> {

        private ProgressDialog pDialog;
       
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(getActivity());
            pDialog.setTitle("Accessing Servers ...");
            pDialog.setMessage("Retrieving your data");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... args) {

            UserFunctions userFunction = new UserFunctions();
            JSONObject json = userFunction.uQuestions(uid);
            return json;
        }

        @Override
        protected void onPostExecute(JSONObject json) {
        	try
        	{
        	
				JSONArray jq = json.getJSONArray("Unanswered Questions");
				//Log.d("LENGTH", "size: "+jq.getJSONObject(0).getString("Question"));
				for(i=0;i<jq.length();i++)
					{array.add(jq.getJSONObject(i).getString("Question"));
					sender_name.add(jq.getJSONObject(i).getString("FbName"));
					sender_id.add(jq.getJSONObject(i).getString("uid"));
				 qidarray.add(jq.getJSONObject(i).getString("QID"));
					}
				pDialog.dismiss();
				question = (ListView) v.findViewById(R.id.unanswered_listview);
				ArrayAdapter<String> adapter = new ArrayAdapter<String>( getActivity() , R.layout.fragment_uq, R.id.unanswered_textview, array );
//				
				question.setAdapter(adapter);	
				question.setOnItemClickListener(new OnItemClickListener() {
		//
					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						// TODO Auto-generated method stub
						Intent registered = new Intent(getActivity(), AnswerActivity.class);
						registered.putExtra("recipient_id", uid);
						registered.putExtra("recipient_name", username);
						registered.putExtra("question", array.get(position) );
						registered.putExtra("question_id", qidarray.get(position));
						registered.putExtra("sender_name", sender_name.get(position));
						registered.putExtra("sender_id", sender_id.get(position));
						 startActivity(registered);
					}
				});				
        	}			
			catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}            			    
}
}
}
