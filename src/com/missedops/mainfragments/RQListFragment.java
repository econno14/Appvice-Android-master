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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

import android.support.v4.app.Fragment;

public class RQListFragment extends Fragment {
	String uid, username, senderFBName;
	ListView listView;
	List<String> array = new ArrayList<String>();
	List<String> answerArray = new ArrayList<String>();
	List<String> AnsweredByArray = new ArrayList<String>();
	List<String> response=new ArrayList<String>();
	int i;
	View myFragmentView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		myFragmentView = inflater.inflate(R.layout.fragment_rq, container,
				false);
		Bundle b = getActivity().getIntent().getExtras();
		uid = b.getString("uid");
		username= b.getString("username");
		new getResolvedQuestions().execute();

		return myFragmentView;
	}

	private class getResolvedQuestions extends
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
			JSONObject json = userFunction.rQuestions(uid);
			return json;
		}

		@Override
		protected void onPostExecute(JSONObject json) {
			try {

				JSONArray jq = json.getJSONArray("Resolved Questions");
				senderFBName = jq.getJSONObject(0).getString("username");

				for (i = 0; i < jq.length(); i++) {
					array.add(jq.getJSONObject(i).getString("Question") + " ( "
				+ jq.getJSONObject(i).getString("FbName") + " ) ");
					answerArray.add(jq.getJSONObject(i).getString("Question"));
					AnsweredByArray.add(jq.getJSONObject(i).getString("FbName"));
					response.add(jq.getJSONObject(i).getString("Answer"));
				}

				pDialog.dismiss();

				// displaying inside list view
				listView = (ListView) myFragmentView
						.findViewById(R.id.resolved_listview);
				ArrayAdapter<String> adapter = new ArrayAdapter<String>(
						getActivity(), R.layout.fragment_rq,
						R.id.resolved_textview, array);
				listView.setAdapter(adapter);

				listView.setOnItemClickListener(new OnItemClickListener() {
					//
					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						// TODO Auto-generated method stub
						Intent registered = new Intent(getActivity(),
								ViewActivity.class);
						registered.putExtra("sender_name", senderFBName);
						registered.putExtra("recipient_name", AnsweredByArray.get(position));
						registered.putExtra("question", answerArray.get(position));
						registered.putExtra("answer", response.get(position));
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
