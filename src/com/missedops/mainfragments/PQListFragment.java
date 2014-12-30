package com.missedops.mainfragments;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.missedops.library.R;
import com.missedops.library.UserFunctions;
import com.missedops.mainswipetabs.AnswerActivity;

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

public class PQListFragment extends Fragment {
	String uid;
	ListView listView;
	List <String> array= new ArrayList<String>();
	int i;
	View myFragmentView;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		myFragmentView = inflater.inflate(R.layout.fragment_pq, container,
				false);	
		Bundle b = getActivity().getIntent().getExtras();
		uid = b.getString("uid");
		new getPendingQuestions().execute();
		
		return myFragmentView;
	}

	private class getPendingQuestions extends AsyncTask<String, String, JSONObject> {


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
            JSONObject json = userFunction.pQuestions(uid);
            return json;
        }

        @Override
        protected void onPostExecute(JSONObject json) {
            try {
            	
				JSONArray jq = json.getJSONArray("Pending Questions");
				//Log.d("LENGTH", "size: "+jq.getJSONObject(0).getString("Question"));
				for(i=0;i<jq.length();i++)
					array.add(jq.getJSONObject(i).getString("Question"));
							//+"QID:"+ jq.getJSONObject(i).getString("QID"));
					
				pDialog.dismiss();
				
				// displaying inside list view
				listView = (ListView) myFragmentView.findViewById(R.id.pending_listview);
				ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity() , R.layout.fragment_pq, R.id.pending_textview, array );		
				listView.setAdapter(adapter);						
			} 
            catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            
			

	}
    
}
}
