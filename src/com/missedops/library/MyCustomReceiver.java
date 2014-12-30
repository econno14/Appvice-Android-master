package com.missedops.library;

import java.util.Iterator;

import org.json.JSONException;
import org.json.JSONObject;

import com.missedops.logregswipetabs.LogRegActivity;
import com.missedops.mainfragments.AskFragment;
import com.missedops.mainfragments.RQListFragment;
import com.missedops.mainswipetabs.AnswerActivity;
import com.missedops.mainswipetabs.ViewActivity;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

public class MyCustomReceiver extends BroadcastReceiver {
	private static final String TAG = "MyCustomReceiver";
	private final String questionReceived = "QUESTION_RECEIVED";
	private final String questionAnswered = "QUESTION_ANSWERED";
	Intent intent;
	PendingIntent pi;
	String senderId, senderName, question, questionId, answer ,recipientName , recipientId;
	Context ctx;
	JSONObject json;
	String message;

	@Override
	public void onReceive(Context context, Intent intent) {
		ctx = context;
		try {
			if (intent == null) {
				Log.d(TAG, "Receiver intent null");
			} else {
				String action = intent.getAction();
				message = intent.getExtras().getString("com.parse.Data");		
				if ( message != null && !message.equals("") )
				{
					json = new JSONObject(intent.getExtras().getString("com.parse.Data"));
					if (action.equals("com.missedops.QUESTION_RECEIVED")) { // QUESTION_NOTIFICATION
						notify(context, intent, json, questionReceived);
					} else if (action.equals("com.missedops.QUESTION_ANSWERED")) {
						notify(context, intent, json, questionAnswered);					
					}
				}
			}
		} catch (JSONException e) {
			Log.d(TAG, "JSONException: " + e.getMessage());
		}
	}


	private void notify(Context ctx, Intent i, JSONObject dataObject, String messageType) throws JSONException {
		
		senderId = dataObject.getString("sender_id");
		senderName = dataObject.getString("sender_name");
		question = dataObject.getString("question");
		questionId = dataObject.getString("question_id");
		answer = dataObject.getString("answer");
		recipientName = dataObject.getString("recipient_name");
		recipientId = dataObject.getString("recipient_id");
		answer = dataObject.getString("answer");

		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(ctx);

		if (messageType.equals(questionReceived)) {
			Intent notificationIntent = new Intent(ctx, AnswerActivity.class);
			
			notificationIntent.putExtra("sender_name", senderName);
			notificationIntent.putExtra("sender_id", senderId);			
			notificationIntent.putExtra("question", question);
			notificationIntent.putExtra("question_id", questionId);
			notificationIntent.putExtra("recipient_name", recipientName);
			notificationIntent.putExtra("recipient_id", recipientId);
			notificationIntent.putExtra("answer", answer);	

			PendingIntent intent = PendingIntent.getActivity(ctx, 0,notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

			mBuilder.setContentIntent(intent);
			mBuilder.setStyle(new NotificationCompat.BigTextStyle().bigText("From: " + senderName + " ( " + question.toUpperCase() + " )"));
			mBuilder.setWhen(System.currentTimeMillis());
			mBuilder.setSmallIcon(R.drawable.appvice_3);
			mBuilder.setContentTitle("Question Received!");
//			mBuilder.setContentText("From: " + senderName + " ( " + question.toUpperCase() + " )");
			mBuilder.setLights(Color.RED, 400, 400);
			mBuilder.setAutoCancel(true);
			// mBuilder.setSound(Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + ctx.getPackageName() + "/raw/" + sound));
			mBuilder.setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_VIBRATE | Notification.DEFAULT_SOUND);			
		
		} else if (messageType.equals(questionAnswered)) {
			Intent notificationIntent = new Intent(ctx, ViewActivity.class);
			
			notificationIntent.putExtra("sender_name", senderName);
			notificationIntent.putExtra("sender_id", senderId);			
			notificationIntent.putExtra("question", question);
			notificationIntent.putExtra("question_id", questionId);
			notificationIntent.putExtra("recipient_name", recipientName);
			notificationIntent.putExtra("recipient_id", recipientId);	
			notificationIntent.putExtra("answer", answer);
			
			PendingIntent intent = PendingIntent.getActivity(ctx, 0,notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

			mBuilder.setContentIntent(intent);
			mBuilder.setStyle(new NotificationCompat.BigTextStyle().bigText("By: " + senderName	+ " ( " + question.toUpperCase() + " )"));
			mBuilder.setWhen(System.currentTimeMillis());
			mBuilder.setSmallIcon(R.drawable.appvice_3);
			mBuilder.setContentTitle("Question Answered!");
//			mBuilder.setContentText("By: " + senderName	+ " ( " + question.toUpperCase() + " )");
//			mBuilder.setContentText("BLAH BLAH");
			mBuilder.setLights(Color.RED, 400, 400);
			mBuilder.setAutoCancel(true);
			// mBuilder.setSound(Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + ctx.getPackageName() + "/raw/" + sound));
			mBuilder.setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_VIBRATE | Notification.DEFAULT_SOUND);			
		}
		
		NotificationManager mNotificationManager = (NotificationManager) ctx.getSystemService(Context.NOTIFICATION_SERVICE);
		mNotificationManager.notify(0, mBuilder.build());
	
	}
}