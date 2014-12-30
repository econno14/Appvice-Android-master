package com.missedops.library;
import java.util.ArrayList;
import java.util.List;
 


import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;
 


import android.content.Context;
public class UserFunctions {
	private JSONParser jsonParser;
    
    // Testing in localhost using wamp or xampp 
    // use http://10.0.2.2/ to connect to your localhost ie http://localhost/
    private static String loginURL = "http://lifeline-app.net:1024/lifeline_login_api/";
    private static String registerURL = "http://lifeline-app.net:1024/lifeline_login_api/";
     
    private static String login_tag = "login";
    private static String register_tag = "register";
    private static String soc_nw_tag= "social";
    private static String corr_tag= "correspondence";
    private static String ask_tag= "ask";
    private static String ans_tag= "ans";
    private static String p_tag= "pend";
    private static String r_tag= "resolved";
    private static String u_tag= "unanswered";
    private static String a_tag= "answered";
    // constructor
    public UserFunctions(){
        jsonParser = new JSONParser();
    }
     
    /**
     * function make Login Request
     * @param email
     * @param password
     * */
    public JSONObject loginUser(String email, String password){
        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", login_tag));
        params.add(new BasicNameValuePair("email", email));
        params.add(new BasicNameValuePair("password", password));
        JSONObject json = jsonParser.getJSONFromUrl(loginURL, params);
        // return json
        // Log.e("JSON", json.toString());
        return json;
    }
     
    /**
     * function make Login Request
     * @param name
     * @param email
     * @param password
     * */
    public JSONObject registerUser(String name, String email, String password){
        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", register_tag));
        params.add(new BasicNameValuePair("name", name));
        params.add(new BasicNameValuePair("email", email));
        params.add(new BasicNameValuePair("password", password));
         
        // getting JSON Object
        JSONObject json = jsonParser.getJSONFromUrl(registerURL, params);
        // return json
        return json;
    }
     
    public JSONObject askQuestion(String uid, String question){
    	List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", ask_tag));
        params.add(new BasicNameValuePair("uid", uid));
        params.add(new BasicNameValuePair("question", question));
        
        //getting JSON Object
        JSONObject json= jsonParser.getJSONFromUrl(loginURL, params);
        return json;
    }
    
    public JSONObject socInfo(String uid, String soc_ntwk_id, String displayname)
    {
    	List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", soc_nw_tag));
        params.add(new BasicNameValuePair("uid", uid));
        params.add(new BasicNameValuePair("soc_ntwk_id", soc_ntwk_id));
        params.add(new BasicNameValuePair("displayname", displayname));
        //getting JSON Object
        JSONObject json= jsonParser.getJSONFromUrl(loginURL, params);
        return json;
    }
    
    public JSONObject correspond(String qid, String contact_soc_ntwk_id, String contact_displayname)
    {
    	List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", corr_tag));
        params.add(new BasicNameValuePair("qid", qid));
        params.add(new BasicNameValuePair("contact_soc_ntwk_id", contact_soc_ntwk_id));
        params.add(new BasicNameValuePair("contact_displayname", contact_displayname));
        //getting JSON Object
        JSONObject json= jsonParser.getJSONFromUrl(loginURL, params);
        return json;
    }
    
    public JSONObject answer(String answer, String qid, String answer_from_unique_id)
    {
    	List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", ans_tag));
        params.add(new BasicNameValuePair("answer", answer));
        params.add(new BasicNameValuePair("qid", qid));
        params.add(new BasicNameValuePair("answer_from_unique_id", answer_from_unique_id));
        //getting JSON Object
        JSONObject json= jsonParser.getJSONFromUrl(loginURL, params);
        return json;
    }
    /**
     * Function get Login status
     * */
    public boolean isUserLoggedIn(Context context){
        DatabaseHandler db = new DatabaseHandler(context);
        int count = db.getRowCount();
        if(count > 0){
            // user logged in
            return true;
        }
        return false;
    }
    public JSONObject pQuestions(String uid)
    {
    	List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", p_tag));
        params.add(new BasicNameValuePair("uid", uid));
        JSONObject json= jsonParser.getJSONFromUrl(loginURL, params);
        return json;
    	
    }
    public JSONObject rQuestions(String uid)
    {
    	List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", r_tag));
        params.add(new BasicNameValuePair("uid", uid));
        JSONObject json= jsonParser.getJSONFromUrl(loginURL, params);
        return json;
    	
    }
    public JSONObject uQuestions(String uid)
    {
    	List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", u_tag));
        params.add(new BasicNameValuePair("uid", uid));
        JSONObject json= jsonParser.getJSONFromUrl(loginURL, params);
        return json;
    	
    }
    public JSONObject aQuestions(String uid)
    {
    	List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", a_tag));
        params.add(new BasicNameValuePair("uid", uid));
        JSONObject json= jsonParser.getJSONFromUrl(loginURL, params);
        return json;
    	
    }  
    /**
     * Function to logout user
     * Reset Database
     * */
    public boolean logoutUser(Context context){
        DatabaseHandler db = new DatabaseHandler(context);
        db.resetTables();
        return true;
    }
    
    public boolean clearQ(Context context){
    QuestionDatabase db= new QuestionDatabase(context);
    db.resetTables();
    return true;
    }
    
    public boolean clearSoc(Context context){
        SocialDatabase db= new SocialDatabase(context);
        db.resetTables();
        return true;
        }
    
    public boolean clearCorr(Context context){
        CorrespondenceDatabase db= new CorrespondenceDatabase(context);
        db.resetTables();
        return true;
        }
    }
     

