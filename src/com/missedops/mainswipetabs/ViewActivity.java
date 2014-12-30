package com.missedops.mainswipetabs;

import com.missedops.library.R;

import android.app.Activity;
import android.os.Bundle;
import android.text.Html;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class ViewActivity extends Activity implements View.OnClickListener {
	private Button ok;
	private Button cancel;
	private TextView questionView, questionLabel, answerLabel, answerView;
	private String senderId, senderName, question, questionId, answer, recipientId, recipientName ;
	private SpannableString content;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Bundle b = getIntent().getExtras();
		
		senderId = b.getString("sender_id");		
		senderName = b.getString("sender_name");
		question = b.getString("question");
		questionId = b.getString("question_id");
		recipientId = b.getString("recipient_id");
		recipientName = b.getString("recipient_name");	
		answer = b.getString("answer");				

		setContentView(R.layout.activity_view);
		questionLabel = (TextView) findViewById(R.id.view_questionLabel);		
		content = new SpannableString("Question by: " + senderName );
		content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
		questionLabel.setText(content);
		
		questionView = (TextView) findViewById(R.id.view_questionView);
		questionView.setText(question);
		
		answerLabel = (TextView) findViewById(R.id.view_answerLabel);		
		content = new SpannableString("Answered by: " + recipientName );
		content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
		answerLabel.setText(content);
						
		answerView = (TextView) findViewById(R.id.view_answerView); 
		answerView.setText(answer);
				
		ok = (Button) findViewById(R.id.view_okButton);						
		ok.setOnClickListener(this);		

	}

	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		finish();
	}
}
