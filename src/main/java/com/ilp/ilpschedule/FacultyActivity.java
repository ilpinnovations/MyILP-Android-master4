package com.ilp.ilpschedule;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

public class FacultyActivity extends Activity {
	ImageButton btnBack;
	ImageView badge;
	String serverURL_feedback =AppConstant.URL+"faculty_json.php";
	
	private ProgressDialog progress;
	
	String empid;
	TextView points,points_need;
	TextView txthead;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_faculty);
		ViewGroup vg = (ViewGroup) findViewById(R.id.root);
		Utils.setFontAllView(vg);
		btnBack = (ImageButton) findViewById(R.id.btn_list);
	badge = (ImageView) findViewById(R.id.image);
	points = (TextView) findViewById(R.id.text_name);
	points_need = (TextView) findViewById(R.id.text_desc);
	txthead = (TextView) findViewById(R.id.text_price);
	txthead.setText("Feedback");
	btnBack.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			finish();
		}
	});
	
	/*final DatabaseHandler db = new DatabaseHandler(this);
	
	if(db.getContactsCount()>0)
	{
//		db.close();
		finish();
		Intent i=new Intent(this,ScheduleActivity.class);
		startActivity(i);
			
		List<Info> getInfo=db.getAllContacts();
		if(getInfo.size()>0)
		{
		empid=String.valueOf(getInfo.get(0).getID());
//		empname=getInfo.get(0).getName();
//		emploc=getInfo.get(0).getLoc();
//		empbatch=getInfo.get(0).getBatch();
		}
	}*/
	
	Intent iget= getIntent();
	String slot=iget.getStringExtra("slot");
	String course=iget.getStringExtra("course");
	String faculty=iget.getStringExtra("faculty");
	String date=iget.getStringExtra("date");
	
	new LongOperation_feed(serverURL_feedback, slot,course,faculty,date, FacultyActivity.this).execute("");
	
	}
	
	
	  //+++++++++++++++++++++++++++++++++++++++++++++++++++
	
	 // Class with extends AsyncTask class
 private class LongOperation_feed  extends AsyncTask<String, Void,String> {
      String _url,_slot,_faculty,_date,_course;//,_rate;
      Activity __context;
//      int _vtype_4w,_empid,_vtype_2w;
      
      int count,avg_count;
    
//System.out.print(dateFormat.format(date)); //2014/08/06 15:59:48

      
 	// Required initialization
 	public LongOperation_feed(String url,String slot,String course,String faculty,String date,Activity c)
 	{
 		_url=url;
 		__context=c;
 		_slot=slot;
 		_faculty=faculty;
 		_date=date;
 		_course=course;
// 		_rate=rate;
 		
 		
 		
 	}
     private final HttpClient Client = new DefaultHttpClient();
     private String Content;
     private String Error = null;
//     private ProgressDialog Dialog = new ProgressDialog(__context);
     
     String data =""; 
//     TextView uiUpdate = (TextView) findViewById(R.id.output);
//     TextView jsonParsed = (TextView) findViewById(R.id.jsonParsed);
     int sizeData = 0;  
//     EditText serverText = (EditText) findViewById(R.id.serverText);
     
     
     protected void onPreExecute() {
         // NOTE: You can call UI Element here.
          
         //Start Progress Dialog (Message)
        
//         Dialog.setMessage("Please wait..");
//         Dialog.show();
         
//         try{
//         	// Set Request parameter
//             data +="&" + URLEncoder.encode("data", "UTF-8") + "="+serverText.getText();
//	            	
//         } catch (UnsupportedEncodingException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
   	  progress=ProgressDialog.show(FacultyActivity.this, "", "");
			 progress.setContentView(R.layout.progress);
		      progress.show();
   	  
   	  
   	  
   	  
//			} 
         
//   	  DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
//   	  Date date = new Date();
//   	  _slotdate=dateFormat.format(date);
//   	 	Toast.makeText(__context, _empdate, Toast.LENGTH_LONG).show();
   	  // GET IMEI NUMBER      
//			 TelephonyManager tManager = (TelephonyManager) getBaseContext()
//			    .getSystemService(Context.TELEPHONY_SERVICE);
//			   _empimei = tManager.getDeviceId();
   	  
   	  
			   
			   
			   
   	  
     }

     // Call after onPreExecute method
     protected String doInBackground(String... urls) {
     	
     	/************ Make Post Call To Web Server ***********/
     	BufferedReader reader=null;

	             // Send data 
	            try
	            { 
//	        		Thread.sleep(1000);
//	               // Defined URL  where to send data
	            	
//	            	_url=_url+"?empid="+_empid+"&empname="+_empname+"&empshift="+_empshift+"&empdate="+_empdate;
	            	
	            	  String data = URLEncoder.encode("faculty", "UTF-8") + "=" + URLEncoder.encode(_faculty, "UTF-8"); 
	                  data += "&" + URLEncoder.encode("course", "UTF-8") + "=" + URLEncoder.encode(_course, "UTF-8"); 
//	                  data += "&" + URLEncoder.encode("comment", "UTF-8") + "=" + URLEncoder.encode(_comment, "UTF-8");
//	                  data += "&" + URLEncoder.encode("rate", "UTF-8") + "=" + URLEncoder.encode(_rate, "UTF-8");
//	                  data += "&" + URLEncoder.encode("empid", "UTF-8") + "=" + URLEncoder.encode(_empid, "UTF-8");
//	                  data += "&" + URLEncoder.encode("empname", "UTF-8") + "=" + URLEncoder.encode(empname, "UTF-8");
//	                  data += "&" + URLEncoder.encode("emploc", "UTF-8") + "=" + URLEncoder.encode(emploc, "UTF-8");
//	                  data += "&" + URLEncoder.encode("empbatch", "UTF-8") + "=" + URLEncoder.encode(empbatch, "UTF-8");
	                  data += "&" + URLEncoder.encode("slot", "UTF-8") + "=" + URLEncoder.encode(_slot, "UTF-8");
	                  data += "&" + URLEncoder.encode("date", "UTF-8") + "=" + URLEncoder.encode(_date, "UTF-8");
//	                  
	            	
	            	
	            	
	               URL url = new URL(_url);
	               
//	                 
//	              // Send POST data request
//	   
	              URLConnection conn = url.openConnection(); 
	              conn.setDoOutput(true); 
	              OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream()); 
	              wr.write( data ); 
	              wr.flush(); 
//	          
	              // Get the server response 
	               
	              reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
	              StringBuilder sb = new StringBuilder();
	              String line = null;
	            
		            // Read Server Response
		            while((line = reader.readLine()) != null)
		                {
		                       // Append server response in string
		                       sb.append(line + "\n");
		                }
	                
	                // Append Server Response To Content String 
	               Content = sb.toString();
	            }
	            catch(Exception ex)
	            {
	            	Error = ex.getMessage();
	            }
	            finally
	            {
	                try
	                {
	     
	                    reader.close();
	                }
	   
	                catch(Exception ex) {}
	            }
     	
         /*****************************************************/
         return "";
     }
      
     @SuppressWarnings("unused")
	protected void onPostExecute(String unused) {
         // NOTE: You can call UI Element here.
          
//          Close progress dialog
   	  if(progress.isShowing())
   		  progress.dismiss();
//         Dialog.dismiss();
          
         if (Error != null) {
              
//             uiUpdate.setText("Output : "+Error);
       	  Toast.makeText(__context, "Error due to some network problem! Please connect to internet. ", Toast.LENGTH_LONG).show();
              
         } else {
           
         	// Show Response Json On Screen (activity)
//         	uiUpdate.setText( Content );
         	
          /****************** Start Parse Response JSON Data *************/
       	  
             
             
            String feed_result="";
       	  
       	  
         	String OutputData = "";
             JSONObject jsonResponse;
//             Toast.makeText(_context, "Error"+Content, Toast.LENGTH_LONG).show();
             try {
           	    if(Content!=null){
                  /****** Creates a new JSONObject with name/value mappings from the JSON string. ********/
                  jsonResponse = new JSONObject(Content);
                   Log.d("RESPONSE----", jsonResponse.toString());
                  /***** Returns the value mapped by name if it exists and is a JSONArray. ***/
                  /*******  Returns null otherwise.  *******/
                  JSONArray jsonMainNode = jsonResponse.optJSONArray("Android");
                   
                  /*********** Process each JSON Node ************/

                  int lengthJsonArr = jsonMainNode.length();  
//                  String[] mStrings={};
//                  String[] news_date_arr = new String[lengthJsonArr];
//                  String[] news_time_arr = new String[lengthJsonArr];
//                  String[] news_title_arr = new String[lengthJsonArr];
//                  String[] news_desc_arr = new String[lengthJsonArr];
//                  String[] mStrings = new String[lengthJsonArr];
//                  String[] news_bigimg_arr = new String[lengthJsonArr];
                  
                  
//                  news_date_arr = new String[lengthJsonArr];
//                 news_time_arr = new String[lengthJsonArr];
//                  news_title_arr = new String[lengthJsonArr];
//                  news_desc_arr = new String[lengthJsonArr];
//                  mStrings = new String[lengthJsonArr];
//                  news_bigimg_arr = new String[lengthJsonArr];
                  
                  
//                  ArrayList<Scores_Category> categoriesList=new ArrayList<Scores_Category>();
                  
                  for(int i=0; i < lengthJsonArr; i++) 
                  {
                      /****** Get Object for each JSON node.***********/
                      JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
                       
                      /******* Fetch node values **********/

                      count       = jsonChildNode.optInt("count");
                      avg_count       = jsonChildNode.optInt("avg_count");
//                       emp_date       = jsonChildNode.optString("emp_date").toString();
                      
//                       emp_name = jsonChildNode.optString("emp_name").toString();
//                       emp_shift = jsonChildNode.optString("emp_shift").toString();
                       feed_result = jsonChildNode.optString("faculty_result").toString();

                       

                      
                      
                 }
                  
                  
                  if(feed_result.equalsIgnoreCase("success"))
                  {
//                  formatTxt.setText("SHIFT: " +emp_shift +emp_name);
                  /*aController.showAlertDialog(FeedbackActivity.this,
							"Feedback Status:",
						"Thanks for your valuable feedback!", true,FeedbackActivity.this);*/
                	  
//                  Toast.makeText(BadgesActivity.this, "Submitted Successfully! ", Toast.LENGTH_LONG).show();
                	  if(avg_count==0)
                	  {
                		  points.setText("Total no. of feedbacks: "+count+"\n Average rating: "+"NA");
//                		  int rem=30-count;
                		  points_need.setText("SLOT: "+_slot+"     COURSE: "+_course);
                	  }
                	  else
                	  {
//                		  badge.setImageResource(R.drawable.badge1);
                		  points.setText("Total no. of feedbacks: "+count+"\n Average rating: "+avg_count);
//                		  int rem=30-count;
                		  points_need.setText("SLOT: "+_slot+"     COURSE: "+_course);
                	  }
                	  
                	/*  else if(count>=30 && count<60)
                	  {
                		  badge.setImageResource(R.drawable.badge2);
                		  points.setText("Congratulations!\nYou won the Feedback Karma Empower badge.\nYou earn "+count+" points");
                		  int rem=60-count;
                		  points_need.setText("You need "+rem+" points more to win the Feedback Karma Leader badge.");
                	  }
                	  else  if(count>=60 && count<100)
                	  {
                		  badge.setImageResource(R.drawable.badge3);
                		  points.setText("Congratulations!\nYou won the Feedback Karma Leader badge.\nYou earn "+count+" points");
                		  int rem=100-count;
                		  points_need.setText("You need "+rem+" points more to win the Feedback Karma King badge.");
                	  }
                	  else  if(count>=100)
                	  {
                		  badge.setImageResource(R.drawable.badge4);
                		  points.setText("Congratulations!\nYou won the Feedback Karma king badge.\nYou earn "+count+" points");
                	  }
                	  else 
                	  {
                		  
                		  points.setText("You earn "+count+" points");
                		  int rem=15-count;
                		  points_need.setText("You need "+rem+" points more to win the Feedback Karma Warrior badge.");
                	  }*/
 
                  }
                 /*else if(feed_result.equalsIgnoreCase("already"))
                  {
//                  formatTxt.setText("SHIFT: " +emp_shift +emp_name);
                  aController.showAlertDialog(FeedbackActivity.this,
							"Feedback Status:",
						"You have already given the feedback!", false,FeedbackActivity.this);
                  Toast.makeText(FeedbackActivity.this, "You have already given the feedback! ", Toast.LENGTH_LONG).show();
                  }*/
                  
                  else 
                  {
//               	   aController.showAlertDialog(FeedbackActivity.this,
//								"Booking Status:",
//								"Sorry for inconvience! \n PARKING FULL", false,InfoActivity.this);
	                   Toast.makeText(FacultyActivity.this, "Error in fetching details! ", Toast.LENGTH_LONG).show();
               	   
                  }
                  
              /****************** End Parse Response JSON Data *************/     
                  
                  //Show Parsed Output on screen (activity)
//                  jsonParsed.setText( OutputData );
                  
                  
                  
               // Create custom adapter for listview
//          		adapter = new LazyImageLoadAdapter(News.this, mStrings,news_date_arr,news_time_arr,news_title_arr,news_desc_arr);

          		// Set adapter to listview
//          		list.setAdapter(adapter);

                  
                  
//                -------------------------Fmard-----------------Start
//                Intent i=new Intent(News.this,News.class);
//                i.putExtra("news_date", news_date_arr);
//                i.putExtra("news_time", news_time_arr);
//                i.putExtra("news_title", news_title_arr);
//                i.putExtra("news_desc", news_desc_arr);
//                i.putExtra("img_urls", mStrings);
//                i.putExtra("bigimg_urls",news_bigimg_arr);
//                
//                
//                i.putExtra("categories", categoriesList);
//                
//                startActivity(i);
//                -------------------------Fmard--------------End---
                  
           	    }
	                   
	                   else {
	                	   Toast.makeText(__context, "Error due to some network problem! Please connect to internet. ", Toast.LENGTH_LONG).show();
					}    
                   
              } catch (JSONException e) {          
                  e.printStackTrace();
//                  Toast.makeText(_context, e.toString(), Toast.LENGTH_LONG).show();
              }

              
          }
		
     }
      
 }
	//+++++++++++++++++++++++++++++++++++++++++++++++++++
	
	
}
