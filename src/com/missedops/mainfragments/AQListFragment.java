package com.missedops.mainfragments;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.missedops.library.R;
import com.missedops.library.UserFunctions;
import com.missedops.mainswipetabs.AnswerActivity;
import com.missedops.mainswipetabs.ViewActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class AQListFragment extends Fragment {
	ListView listView;
	List<String> array = new ArrayList<String>();
	List<String> answerArray = new ArrayList<String>();
	List<String> senderArray = new ArrayList<String>();
	List<String> questionArray = new ArrayList<String>();
	String username, uid;
	int i = 0;
	View myFragmentView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		myFragmentView = inflater.inflate(R.layout.fragment_aq, container,
				false);
		Bundle b = getActivity().getIntent().getExtras();
		username = b.getString("username");
		uid = b.getString("uid");
		new getAnsweredQuestions().execute();

		return myFragmentView;
	}

	private class getAnsweredQuestions extends
			AsyncTask<String, String, JSONObject> {

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
			JSONObject json = userFunction.aQuestions(uid);
			return json;
		}

		@Override
		protected void onPostExecute(JSONObject json) {
			try {

				JSONArray jq = json.getJSONArray("Answered Questions");
				
				for (i = 0; i < jq.length(); i++) {
					array.add(jq.getJSONObject(i).getString("Question") + " ( "  + jq.getJSONObject(i).getString("FbName") + " ) ");
					questionArray.add(jq.getJSONObject(i).getString("Question"));
					// +"QID:"+ jq.getJSONObject(i).getString("QID"));
					answerArray.add(jq.getJSONObject(i).getString("answer"));
					senderArray.add(jq.getJSONObject(i).getString("FbName"));
				}

				pDialog.dismiss();
				// displaying inside list view
				listView = (ListView) myFragmentView.findViewById(R.id.answered_listview);
				ArrayAdapter<String> adapter = new ArrayAdapter<String>(
						getActivity(), R.layout.fragment_aq, R.id.answered_textview, array);
				listView.setAdapter(adapter);

				listView.setOnItemClickListener(new OnItemClickListener() {
					//
					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						// TODO Auto-generated method stub
						Intent registered = new Intent(getActivity(),
								ViewActivity.class);
						registered.putExtra("recipient_name", username );
						registered.putExtra("sender_name", senderArray.get(position)  );
						registered.putExtra("question", questionArray.get(position));
						registered.putExtra("answer", answerArray.get(position));
						startActivity(registered);
					}
				});

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			pDialog.dismiss();

		}
	}
}
