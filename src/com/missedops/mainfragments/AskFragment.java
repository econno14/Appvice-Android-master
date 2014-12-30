package com.missedops.mainfragments;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import snowballstemmer.EnglishStemmer;

import com.facebook.HttpMethod;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.android.AsyncFacebookRunner;
import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.Facebook.DialogListener;
import com.facebook.android.FacebookError;
import com.facebook.model.GraphObject;
import com.missedops.library.CorrespondenceDatabase;
import com.missedops.library.QuestionDatabase;
import com.missedops.library.UserFunctions;
import com.missedops.mainswipetabs.AnswerActivity;
import com.missedops.mainswipetabs.MainActivity;
import com.missedops.library.R;
import com.parse.Parse;
import com.parse.ParseAnalytics;
import com.parse.ParseInstallation;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.PushService;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class AskFragment extends Fragment {
	EditText A_input;
	Button A_enter;

	// Prachurya
	// SocialAuthAdapter adapter;
	List<String> idOfAllFriends = new ArrayList<String>();
	List<String> nameOfAllFriends = new ArrayList<String>();
	List<String> id = new ArrayList<String>();
	List<String> name = new ArrayList<String>();
	List<String> packet = new ArrayList<String>();
	List<Integer> ids = new ArrayList<Integer>();
	List<String> info = new ArrayList<String>();
	TextView qErrorMsg;
	Facebook facebook;
	AsyncFacebookRunner mAsyncRunner;
	SharedPreferences mPrefs;
	Session session;
	String[] args;
	// QBUser user;
	int i = 0, j = 0;
	private static String KEY_SUCCESS = "success";
	private static String KEY_UID = "uid";
	private static String KEY_QUESTION = "question";
	private static String KEY_QID = "qid";
	private static String KEY_TIME = "created_at";
	private static String KEY_STATUS = "stat";
	private static String KEY_CAT = "category";
	private static String KEY_DLINE = "deadline";
	private static String KEY_PRIVACY = "privacylevel";
	private static String KEY_ERROR = "error";
	private static final String KEY_CON_SOC_NTWK_NAME = "contact_soc_ntwk_name";
	private static final String KEY_CON_SOC_NTWK_ID = "contact_soc_ntwk_id";
	private static final String KEY_CON_NAME = "contact_displayname";
	private static final String KEY_Q_SENT = "question_sent";
	private static final String KEY_Q_READ = "question_read";
	String uid, question, qid, username, newName;
	private static String APP_ID = "297841130364674";
	// newly added
	Activity activity = this.getActivity();
	private static AskFragment instance;
	
	// newly added
	private HashSet<String> set;
	private String infoWord;
	private String questionWord;
	private ArrayList<String> questionList;
	private Set<String> ignoreSet;
	private EnglishStemmer stemmer;
	private ArrayList<String> recipients;
	private List<String> questionArrayList;
	private String questionString;
	//Global variables
		String f;
	int s=0;

	// newly added
	public static AskFragment getInstance() {
		return instance;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View myFragmentView = inflater.inflate(R.layout.fragment_ask,
				container, false);

		// Prachurya
		Bundle b = getActivity().getIntent().getExtras();
		uid = b.getString("uid"); // sender's id
		username = b.getString("username");
		// adapter = new SocialAuthAdapter(new ResponseListener());
		facebook = new Facebook(APP_ID);
		mAsyncRunner = new AsyncFacebookRunner(facebook);
		qErrorMsg = (TextView) myFragmentView.findViewById(R.id.ask_error_text);
		A_input = (EditText) myFragmentView.findViewById(R.id.a_input);
		A_enter = (Button) myFragmentView.findViewById(R.id.a_enter);
		A_enter.setOnClickListener(A_enterOnClickListener);

		// newly added
		instance = this;
		initialize();

		return myFragmentView;
	}

	// Prachurya
	OnClickListener A_enterOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {		
			// newly added
			questionString = A_input.getText().toString();
			questionString = questionString.toLowerCase();
			questionString = clean(questionString);
			questionArrayList = Arrays.asList(questionString.split(" "));			
			
			new ProcessQuestion().execute();
			// adapter.authorize(getActivity(), Provider.FACEBOOK); // not
			// included
			loginToFacebook();
			// hide keyboard
			InputMethodManager imm = (InputMethodManager) getActivity()
					.getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(A_input.getWindowToken(), 0);
									
		}
	};

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
			facebook.authorize(getActivity(), new String[] { "email",
					"publish_stream", "friends_likes", "friends_interests",
					"friends_location", "friends_hometown", "friends_about_me",
					"friends_education_history", "friends_work_history" },
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
							// getProfileInformation();
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
		getFriends();

	}

	public void getFriends() {
		 String fqlQuery = "SELECT uid, name, music, movies,tv, education.school.name, work.employer.name, interests, books, sports, languages, current_location.name, hometown_location.name FROM user WHERE uid IN " +
			        "(SELECT uid2 FROM friend WHERE uid1 = me() LIMIT 5000) ";		 		 
			
			Bundle params = new Bundle();
		    params.putString("q", fqlQuery);
		    params.putString("access_token", facebook.getAccessToken());
		    session = Session.getActiveSession();
		    
		    id.clear();
		    idOfAllFriends.clear();
		    nameOfAllFriends.clear();
		    name.clear();
		    
		    
		    Request request = new Request(session, "/fql", params, HttpMethod.GET, new Request.Callback(){	  

		        @SuppressWarnings("deprecation")
		        public void onCompleted(Response response) {

		                GraphObject graphObject = response.getGraphObject();
//		                System.out.println("RESPONSE LENGTH: "  + response.toString().length() );
//		                System.out.println(response.toString());
		                
		                String res = response.toString();
		                System.out.println("RESPONSE LENGTH: "  + res.length() );
		                System.out.println(res);
		                

		                if (graphObject != null)
		                {
		                    if (graphObject.getProperty("data") != null)
		                    {
		                    	 JSONObject jO = graphObject.getInnerJSONObject(); // added
		                        try {
		                        	
//		                            String arry = graphObject.getProperty("data").toString();
//		                            System.out.println("STRING LENGTH: " + arry.length() );
//		                            JSONArray jsonNArray = new JSONArray(arry);
		                        	
		                        	JSONArray jsonNArray = jO.getJSONArray("data");
		                            
	                                Log.e("LENGTH", ""+jsonNArray.length());
	                                
		                          for (int i = 0; i < jsonNArray.length(); i++) {

		                               JSONObject jsonObject = jsonNArray.getJSONObject(i);
		                               
		                               if ( !nameOfAllFriends.contains(jsonObject.getString("name")))
		                            	   nameOfAllFriends.add(jsonObject.getString("name"));
		                               if ( !idOfAllFriends.contains(jsonObject.getString("uid")))
		                            	   idOfAllFriends.add(jsonObject.getString("uid"));
		                               
		                               System.out.println("JSON OBJECT FOR ONE FRIEND AND LENGTH: " + jsonObject.toString().length() );
		                               System.out.println(jsonObject.toString());
		                               
		                               System.out.println("INITIAL NAME: " + jsonObject.getString("name"));
		                               System.out.println("INITIAL UID: " + jsonObject.getString("uid"));
		                               
		                               JSONArray education= jsonObject.getJSONArray("education");
		                               JSONArray work= jsonObject.getJSONArray("work");//employer
		                               JSONArray languages= jsonObject.getJSONArray("languages");//name
		                               JSONArray sports= jsonObject.getJSONArray("sports");//name
		                               String movies= jsonObject.getString("movies");
		                               
		                               
		                               // facebook profile info
		                               Log.i("Entry", "uid: " + idOfAllFriends.get(i) +"value of s:"+ s+ ", name: " + nameOfAllFriends.get(i));
                            		   //+", music:<3"+music+", movies:<3"+movies+", books:<3"+books+", interests:<3"+interests+", user_location:<3"+user_location+", hometown:<3"+hometown);
		                               
		                               // movies
		                               movies = clean(movies);
		                               args=movies.split(" ");
//		                               args=movies.split(",");
		                               for(j=0;j<args.length;j++)	
		                               {
		                               info.add(args[j]);
		                               System.out.println("Movies: " + args[j]);
		                               }
		                              
		                               // education
		                               args=null;
		                               for(j=0; j<education.length();j++)
		                               {
		                            	   JSONObject edu= education.getJSONObject(j);
		                                   if(jsonObject.getString("education")!= null)
		                                    	f=edu.getString("school").substring(9, edu.getString("school").length()-2);
		                                   f = clean(f);		                                    
		                                   args=f.split(" ");
		 	                               for(int k=0;k<args.length;k++)
		 	                               {
		 	                               info.add(args[k]);
		 	                               System.out.println("Education: " + args[k]);
		 	                               }
		 	                               args=null;
		                                   
		                               }
		                               
		                               // work
		                               for(j=0; j<work.length();j++)
		                               {
		                            	   JSONObject emp= work.getJSONObject(j);
		                            	   if(jsonObject.getString("work")!=null){
		                            		   f = emp.getString("employer").substring(9, emp.getString("employer").length()-2);
		                            		   f = clean(f);
		                            		   args=f.split(" ");
		                            		   
		                            		   for(int k=0;k<args.length;k++)
		                            		   {
		                            			   info.add(args[k]);
		                            			   System.out.println("Work: " + args[k]);
		                            		   }		 	                               
		                            		   args=null;
		                            	   }
		                               }
		                               
		                               // sports
		                               for(j=0; j<sports.length();j++)
		                               {
		                            	   	JSONObject sp= sports.getJSONObject(j);
		                            	   	if(jsonObject.getString("sports")!=null)
		                            	   	{
		                            	   		f = sp.getString("sports");
		                            	   		f = clean(f);
		                            	   		args = f.split(" ");
		                            	   		
		                            	   		for ( int k=0 ; k < args.length ; k++ )
		                            	   		{
		                            	   			info.add(args[k]);
		                            	   			System.out.println("Sports: " + args[k]);
		                            	   		}
		                            	   		args = null;
		                            	   	}		                            	   			                            		
		                               }
		                               
		                               // languages
		                               for(j=0; j<languages.length();j++)
		                               {
		                            		JSONObject lang= languages.getJSONObject(j);
		                            	   	if(jsonObject.getString("languages")!=null)
		                            	   	{
		                            	   		f = lang.getString("languages");
		                            	   		f = clean(f);
		                            	   		args = f.split(" ");
		                            	   		
		                            	   		for ( int k=0 ; k < args.length ; k++ )
		                            	   		{
		                            	   			info.add(args[k]);
		                            	   			System.out.println("Languages: " + args[k]);
		                            	   		}
		                            	   		args = null;
		                            	   	}		         		                            	   		                            	   		                            	   		                            	   		                            	  		                            	   		                            	   		                            	   		         		                            	 
		                               }
		                               
		                               // music
		                               String music= jsonObject.getString("music");
		                               music = clean(music);
		                               args=music.split(" ");
		                               for(j=0;j<args.length;j++)
		                               {
		                            	   info.add(args[j]);
		                            	   System.out.println("Music: " + args[j]);
		                               }		                               
		                               args=null;
		                               
		                               // tv
		                               String tv= jsonObject.getString("tv");
		                               tv = clean(tv);
		                               args=tv.split(" ");
		                               for(j=0;j<args.length;j++)
		                               {
		                            	   info.add(args[j]);
		                            	   System.out.println("TV: " + args[j] );
		                               }		                               
		                               args=null;
		                               
		                               // Books
		                               String books= jsonObject.getString("books");
		                               books = clean(books);
		                               args=books.split(" ");
		                               for(j=0;j<args.length;j++)
		                               {
		                            	   info.add(args[j]);
		                            	   System.out.println("Books: " + args[j] );
		                               }		                            	   
		                               args=null;
		                               
		                               // Interests
		                               String interests= jsonObject.getString("interests");
		                               interests = clean(interests);
		                               args=interests.split(" ");		                               
		                               for(j=0;j<args.length;j++)
		                               {
		                            	   info.add(args[j]);
		                            	   System.out.println("Interests: " + args[j] );
		                               }
		                               args=null;
		                               
		                               // current location		                               
		                               if(jsonObject.getString("location").length()>8)
		                               {
		                            	   String user_location= jsonObject.getString("location").substring(9,jsonObject.getString("location").length()-2);
		                            	   user_location = clean(user_location);
		                            	   args=user_location.split(" ");
		                            	   for(j=0;j<args.length;j++)
		                            	   {
		                            		   info.add(args[j]);
		                            		   System.out.println("Location: " + args[j] );
		                            	   }		                        
		                            	   args=null;
		                               }
		                               
		                               // home town
		                               if(jsonObject.getString("hometown").length()>8)
		                               {
		                            	   String hometown_location= jsonObject.getString("hometown").substring(9,jsonObject.getString("hometown").length()-2);
		                            	   hometown_location = clean(hometown_location);
		                            	   args=hometown_location.split(" ");
		                            	   for(j=0;j<args.length;j++)
		                            	   {
		                            		   info.add(args[j]);
		                            		   System.out.println("Hometown: " + args[j] );
		                            	   }
		                            	   args=null;
		                               }
		                               //Log.v("HomeTown", "Home Town"); EJ test
		                               
		                               
		                               	set.clear();

										// / CHECKINGGGGGGGGGGGGGGGGGGGGGGG
										if (checkRecipient(info)) 
										{					
											if ( !name.contains(jsonObject.getString("name")))
												name.add(jsonObject.getString("name"));
											if ( !id.contains(jsonObject.getString("uid")))
												id.add(jsonObject.getString("uid"));
										}

										 //set
										 for (String t : set) {
										 if ( !set.isEmpty())
										 Log.d("SET:AFTER: " , t );
										 }

										 //recipients
										 for ( int k =0; k <
										 recipients.size(); k ++ )
										 {
										 if ( !recipients.isEmpty())
										 Log.d("RECIP:AFTER: " ,
										 recipients.get(k));
										 }		                               		                               		                               		                               		                               		                              
	                                  info.clear();
		                            }
		                          
//		                          // printing set
//		                          for(String str : set){
//		                              System.out.println(" HashSet : " + str);
//		                          }
		                          
		                          recipients.clear();
		                          info.clear();
		                          set.clear();
		                          
		                        } catch (JSONException e) {
		                            e.printStackTrace();
		                        }                                   
		                    }
		                }
		                
		                System.out.println("NAME OF ALL FRIENDS");
		                for ( int i = 0 ; i < nameOfAllFriends.size() ; i ++ )
		                {
		                	System.out.println(nameOfAllFriends.get(i));
		                }
		                
		                System.out.println();
		                System.out.println("ID OF ALL FRIENDS");
		                for ( int i = 0 ; i < idOfAllFriends.size() ; i ++ )
		                {
		                	System.out.println(idOfAllFriends.get(i));
		                }
		                
		                System.out.println();
		                System.out.println("NAME OF CHOSEN FRIENDS");
		                for ( int i = 0 ; i < name.size() ; i ++ )
		                {
		                	System.out.println(name.get(i));
		                }
		                
		                System.out.println();
		                System.out.println("ID OF CHOSEN FRIENDS");
		                for ( int i = 0 ; i < id.size() ; i ++ )
		                {
		                	System.out.println(id.get(i));
		                }
		                		                
		                // Match found
		                if ( id.size() != 0 )
		                {
		                	new Correspondence().execute();
		                }
		                
		                // no match found but there are friends
		                else if ( idOfAllFriends.size() != 0 )
		                {
		        			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
		        			alertDialogBuilder.setTitle("Recommendation Not Found ");
		        			AlertDialog alertDialog;
		        			alertDialogBuilder
		        				.setMessage("Send your question to all your Appvice friends?")
		        				.setCancelable(true)		        				
		        				.setNegativeButton("No",new DialogInterface.OnClickListener() {
		        					public void onClick(DialogInterface dialog,int num) {		        							
		        						dialog.dismiss();
		        					}
		        				  })
		        				.setPositiveButton("Yes",new DialogInterface.OnClickListener() {
		        					public void onClick(DialogInterface dialog,int num) {
		        						id = idOfAllFriends;
		        						name = nameOfAllFriends;
		        						dialog.dismiss();
		        						new Correspondence().execute();			        						
		        					}
		        				  });
		        			
		        			
		        			alertDialog = alertDialogBuilder.create();
		        			
		        			// show it
		        			alertDialog.show();		        					                			                			                	
		                	qErrorMsg.setText("");
		                }		  
		                else
		                {
		                	qErrorMsg.setText("ERROR GETTING FRIENDS");
		                }
		        }                  
		});
		Request.executeBatchAsync(request);   
		}
	
//		protected void getLikes(String query, final String user, final int userindex, final int s)
//		{
//			Bundle params = new Bundle();
//		      params.putString("q", query);
//		      params.putString("access_token", facebook.getAccessToken());
//		      session = Session.getActiveSession();
//		      Request request = new Request(session,
//		              "/fql",                         
//		              params,                         
//		              HttpMethod.GET,                 
//		              new Request.Callback(){  
//
//		        @SuppressWarnings("deprecation")
//		        public void onCompleted(Response response) {
//
//		                GraphObject graphObject = response.getGraphObject();
//		                System.out.println(response);
//	                    	
//		                if (graphObject != null)
//		                {
//		                    if (graphObject.getProperty("data") != null)
//		                    {
//		                        try {
//		                            String arry = graphObject.getProperty("data").toString();
//
//		                            JSONArray jsonNArray = new JSONArray(arry);
//	                                int j=s;
//		                          for (int i = 0; i < jsonNArray.length(); i++) {
//
//		                               JSONObject jsonObject = jsonNArray.getJSONObject(i);
//
//		                                String name = jsonObject.getString("name");
//		                                String page_id = jsonObject.getString("page_id");
//	                                  //info.add(name);
//		                                input[userindex][++j]=name;    
//		                          }		 
//		                          
//		                          for(i=0;i<=s;i++)
//		                          {	
//		                        	  if ( !input[userindex][i].equals("0") )
//		                        		  Log.i("SHOPPE" + i + " : ",input[userindex][i]);
//		                          }
//		                        } catch (JSONException e) {
//		                            // 
//		                            e.printStackTrace();
//		                        }                                       
//		                    }
//		                }
//
//		        }                  
//		}); 
//		Request.executeBatchAsync(request);   
//		}
	
	public String clean( String input )
	{
		input = input.replace("," , "");
		input = input.replace("!" , "");
		input = input.replace("?" , "");				
		input = input.replace("." , "");
		input = input.replace("(" , "");
		input = input.replace(")" , "");
		input = input.replace(":" , "");
		input = input.replace(";" , "");		
		
		return input;
	}

	public void initialize() {
		set = new HashSet<String>();
		stemmer = new EnglishStemmer();
		recipients = new ArrayList<String>();
		questionList = new ArrayList<String>();
		questionArrayList = new ArrayList<String>();
		ignoreSet = new HashSet<String>(Arrays.asList("a bit", "a couple",
				"a few", "a good deal", "a great deal", "a lack",
				"a little bit", "a little", "a lot", "a", "aboard", "about",
				"above", "absolutely", "according to", "achoo", "ack",
				"across", "after", "against", "agreed", "aha", "ahead of",
				"ahem", "ahh", "ahoy", "alack", "alas", "all", "along with",
				"along", "alongside", "alright", "alrighty", "although",
				"amen", "amid", "amidst", "among", "amongst", "an", "another",
				"anti", "any", "anybody", "anyhoo", "anyhow", "anyone",
				"anything", "anytime", "apart from", "argh", "around",
				"as for", "as if", "as long as", "as much as", "as per",
				"as soon as", "as though", "as to", "as well as", "as",
				"aside from", "astride", "at", "atop", "attaboy", "attagirl",
				"away from", "awful", "awww", "bah humbug", "bam", "bar",
				"barring", "because of", "because", "before", "behind",
				"behold", "below", "beneath", "beside", "besides", "between",
				"beyond", "bingo", "blah", "bless you", "boo", "both", "bravo",
				"but for", "but", "by means of", "by the time", "by", "cheers",
				"circa", "close to", "concerning", "considering",
				"contrary to", "counting", "crud", "cum", "dang", "darn",
				"depending on", "despite", "doh", "down", "drat", "due to",
				"duh", "during", "each other", "each", "eek", "eh", "either",
				"either", "enough", "even if", "even though", "everybody",
				"everyone", "everything", "except for", "except", "excepting",
				"excluding", "few", "following", "for", "forward of", "from",
				"further to", "gee whiz", "gee", "geepers", "given", "golly",
				"goodness gracious", "goodness", "gosh", "ha", "hallelujah",
				"he", "her", "hers", "herself", "hey", "hi", "him", "himself",
				"his", "hmm", "huh", "if", "in addition to", "in between",
				"in case of", "in case", "in face of", "in favor of",
				"in front of", "in lieu of", "in order that", "in spite of",
				"in view of", "in", "including", "indeed", "inside",
				"instead of", "into", "irrespective of", "it", "its", "itself",
				"jeez", "less", "lest", "like", "little", "lots", "many", "me",
				"mine", "minus", "more", "most", "much", "my gosh", "my",
				"myself", "nah", "near to", "near", "neither", "next to",
				"no one", "no", "nobody", "none", "not much", "not only",
				"nothing", "notwithstanding", "now", "of", "off",
				"on account of", "on behalf of", "on board", "on to",
				"on top of", "on", "once", "one another", "one", "only if",
				"onto", "oops", "opposite to", "opposite", "other than",
				"other", "others", "ouch", "our", "ours", "ourselves",
				"out of", "outside of", "outside", "over", "owing to", "past",
				"pending", "per", "phew", "please", "plenty", "plus",
				"preparatory to", "prior to", "pro", "provided that", "rats",
				"re", "regarding", "regardless of", "respecting", "round",
				"save for", "several", "she", "shoot", "shucks", "since",
				"so that", "some", "somebody", "someone", "something", "than",
				"thanks to", "that", "the", "their", "theirs", "them",
				"themselves", "there", "these", "they", "this", "those",
				"though", "through", "throughout", "thru", "till", "to",
				"together with", "touching", "toward", "towards", "tut",
				"uggh", "under", "underneath", "unless", "unlike", "until",
				"up against", "up to", "up until", "up", "upon", "us",
				"versus", "via", "vis-à-vis", "waa", "we", "what", "whatever",
				"when", "whenever", "where", "wherever", "whether", "which",
				"whichever", "while", "who", "whoever", "whom", "whomever",
				"whose", "with reference to", "with regard to", "with",
				"within", "without", "woah", "whoa", "woops", "worth", "wow",
				"yay", "yes", "yikes", "you", "your", "yours", "yourself",
				"yourselves"));
	}

	// Check if a friend's interests have any match with question
	public boolean checkRecipient(List<String> info) {
		// inserting facebook interests
		for (int i = 0; i < info.size(); i++) {
			if (!ignoreSet.contains(info.get(i))) {
				stemmer.setCurrent(info.get(i));
				stemmer.stem();
				infoWord = stemmer.getCurrent();
				infoWord = infoWord.toLowerCase();
				set.add(infoWord);
			}
		}

		// matching with question words
		for (int j = 0; j < questionArrayList.size(); j++) {
			if (!ignoreSet.contains(questionArrayList.get(j))) {
				stemmer.setCurrent(questionArrayList.get(j));
				stemmer.stem();
				questionWord = stemmer.getCurrent();
				questionWord = questionWord.toLowerCase();
			}

			if (set.contains(questionWord)) {
				System.out.println("MATCH FOUND FOR: " + questionWord );
				return true;
			}
		}
		return false;

	}
	
	// Breaks down the question into parts and put it inside an array
	public void analyzeQuestion ()
	{
		for ( int i = 0 ; i < questionArrayList.size(); i ++ )
		{
			System.out.println(questionArrayList.get(i));
		}
	}

	private class ProcessQuestion extends AsyncTask<String, String, JSONObject> {

		/**
		 * Defining Process dialog
		 **/
		private ProgressDialog pDialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			question = A_input.getText().toString();
			pDialog = new ProgressDialog(getActivity());
			pDialog.setTitle("Contacting Servers");
			pDialog.setMessage("Recording the Question ...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}

		@Override
		protected JSONObject doInBackground(String... args) {

			UserFunctions userFunction = new UserFunctions();
			JSONObject json = userFunction.askQuestion(uid, question);
			return json;

		}

		@Override
		protected void onPostExecute(JSONObject json) {
			/**
			 * Checks for success message.
			 **/
			try {
				if (json.getString(KEY_SUCCESS) != null) {
					qErrorMsg.setText("");
					String res = json.getString(KEY_SUCCESS);
					username = json.getString("displayname");

					String red = json.getString(KEY_ERROR);
					qid = json.getString(KEY_QID);

					if (Integer.parseInt(res) == 1) {
						pDialog.setTitle("Getting Data");
						pDialog.setMessage("Loading Info");

						qErrorMsg.setText("Successfully Recorded");

						QuestionDatabase db = new QuestionDatabase(
								getActivity());

						UserFunctions cq = new UserFunctions();
						cq.clearQ(getActivity());
						db.addUser(json.getString(KEY_UID),
								json.getString(KEY_QUESTION),
								json.getString(KEY_QID),
								json.getString(KEY_TIME),
								json.getString(KEY_STATUS),
								json.getString(KEY_CAT),
								json.getString(KEY_DLINE),
								json.getString(KEY_PRIVACY));

						pDialog.dismiss();

					}

					else if (Integer.parseInt(red) == 2) {
						pDialog.dismiss();
						qErrorMsg.setText("User already exists");
					} else if (Integer.parseInt(red) == 3) {
						pDialog.dismiss();
						qErrorMsg.setText("Invalid Email id");
					}

				}

				else {
					if ( pDialog!= null )
						pDialog.dismiss();
					qErrorMsg.setText("Error occured in registration");
				}

			} catch (JSONException e) {
				e.printStackTrace();

			}
			if ( pDialog!= null )
				pDialog.dismiss();
		}
	}

	private class Correspondence extends
			AsyncTask<String, String, List<JSONObject>> {

		/**
		 * Defining Process dialog
		 **/
		private ProgressDialog pDialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(getActivity());
			pDialog.setTitle("Appvice");
			pDialog.setMessage("Sending question ...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}

		@Override
		protected List<JSONObject> doInBackground(String... args) {

			UserFunctions userFunction = new UserFunctions();
			List<JSONObject> json = new ArrayList<JSONObject>();
			for (i = 0; i < id.size(); i++) {
				json.add(userFunction.correspond(qid, id.get(i), name.get(i)));
			}			
			return json;

		}

		@Override
		protected void onPostExecute(List<JSONObject> json) {
			int appviceFriendCount = 0;
			int notAppviceFriendCount = 0;
			/**
			 * Checks for success message.
			 **/
			try {
				for (i = 0; i < json.size(); i++) {
					if (json.get(i).getString(KEY_SUCCESS) != null) {
						// qErrorMsg.setText("");
						String res = json.get(i).getString(KEY_SUCCESS);

						String red = json.get(i).getString(KEY_ERROR);
						if (!json.get(i).getString("contact_unique_id").equals("None")) {

							// System.out.println(packet.get(j));
							ParseQuery<ParseInstallation> pushQuery = ParseInstallation
									.getQuery();
							pushQuery.whereEqualTo("NetId", json.get(i).getString("contact_unique_id"));

							JSONObject data = new JSONObject();

							data.put("action",
									"com.missedops.QUESTION_RECEIVED");
							data.put("sender_id", uid);
							data.put("sender_name", username);
							data.put("question", question);
							data.put("question_id", qid);
							data.put("recipient_id",json.get(i).getString("contact_unique_id"));
							data.put("recipient_name", name.get(i));
							data.put("answer", "NULL");

							// Send push notification to query
							ParsePush push = new ParsePush();
							push.setQuery(pushQuery); // Set our Installation
														// query
							push.setData(data);
							push.sendInBackground();
							j++;
							System.out.println("NOTIFICATION SENT TO " +name.get(i));
							System.out.println("RECIPIENT ID " +json.get(i).getString("contact_unique_id"));
							appviceFriendCount ++;
						}
						else
						{
							notAppviceFriendCount ++;
						}
						
						if (Integer.parseInt(res) == 1) {
							pDialog.setTitle("Getting Data");
							pDialog.setMessage("Loading Info");
							
							if ( appviceFriendCount!= 0)
								qErrorMsg.setText("Question sent to " + appviceFriendCount  +" Appvice users.");
							else
								qErrorMsg.setText( notAppviceFriendCount + " candidates found. But they are not Appvice users.");

							CorrespondenceDatabase db = new CorrespondenceDatabase(getActivity());

							/**
							 * Removes all the previous data in the SQlite
							 * database
							 **/

							UserFunctions cq = new UserFunctions();
							cq.clearCorr(getActivity());
							db.addCorrespondence(
									json.get(i).getString(KEY_QID), json.get(i)
											.getString(KEY_CON_SOC_NTWK_NAME),
									json.get(i).getString("contact_unique_id"),
									json.get(i).getString(KEY_CON_SOC_NTWK_ID),
									json.get(i).getString(KEY_CON_NAME), json
											.get(i).getString(KEY_Q_SENT), json
											.get(i).getString(KEY_Q_READ));
							pDialog.dismiss();

						}

						else if (Integer.parseInt(red) == 2) {
							pDialog.dismiss();
							qErrorMsg.setText("User already exists");
						} else if (Integer.parseInt(red) == 3) {
							pDialog.dismiss();
							qErrorMsg.setText("Invalid Email id");
						}

					}

					else {
						pDialog.dismiss();

						qErrorMsg.setText("Error occured in registration");
					}

				}
			} catch (JSONException e) {
				e.printStackTrace();

			}
			
			if ( pDialog!= null && pDialog.isShowing() )
				pDialog.cancel();			
			
//			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
//			alertDialogBuilder.setTitle("Question Sent");
// 
//			alertDialogBuilder
//				.setMessage("Question sent to " + json.size() + " Appvice users.")
//				.setCancelable(false)
//				.setPositiveButton("OK",new DialogInterface.OnClickListener() {
//					public void onClick(DialogInterface dialog,int id) {
//						dialog.dismiss();
//					}
//				  });
//			AlertDialog alertDialog = alertDialogBuilder.create();
//			
//			// show it
//			alertDialog.show();
			
			id.clear();
			name.clear();

			
		}
		
		
		
		
		
		
	}

}
