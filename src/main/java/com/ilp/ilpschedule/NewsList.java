package com.ilp.ilpschedule;


import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;


public class NewsList {
	NewsItem itm;
	ArrayList<NewsItem> resultList;
	Activity _context;
	


	
	public NewsList(Activity c) {
		// TODO Auto-generated constructor stub
		this._context=c;
	}
	public  ArrayList<NewsItem> getNewsList() {
		 resultList = new ArrayList<NewsItem>();
		String serverURL_news =AppConstant.URL+"notify_json.php";
		// Use AsyncTask execute Method To Prevent ANR Problem
try {	
	LongOperation_news n=new LongOperation_news(serverURL_news,_context);
    n.execute(resultList);
    resultList=n.get();
    Log.d("Jeeeeet--result", String.valueOf(resultList.size()));
} catch (Exception e) {
	// TODO: handle exception
}
     
		
		
		


		return resultList;
	};

	
	
	
	//+++++++++++++++++++++++++++++++++++++++++++++++++++
	
		 // Class with extends AsyncTask class
	   private class LongOperation_news  extends AsyncTask<ArrayList<NewsItem>, Void,ArrayList<NewsItem>> {
	        String _url;
	        Activity __context;
	   	// Required initialization
	   	public LongOperation_news(String url,Activity c)
	   	{
	   		_url=url;
	   		__context=c;
	   		
	   	}
	       private final HttpClient Client = new DefaultHttpClient();
	       private String Content;
	       private String Error = null;

	       
	       String data =""; 

	       int sizeData = 0;  

	       
	       
	       protected void onPreExecute() {
	           // NOTE: You can call UI Element here.
	            
	           //Start Progress Dialog (Message)
	          
 
	           
	       }

	       // Call after onPreExecute method
	       protected ArrayList<NewsItem> doInBackground(ArrayList<NewsItem>... urls) {
	       	
	       	/************ Make Post Call To Web Server ***********/
	       	BufferedReader reader=null;
	  
		             // Send data 
		            try
		            { 
//		        		Thread.sleep(1000);
//		               // Defined URL  where to send data
		               URL url = new URL(_url);
//		                 
//		              // Send POST data request
//		   
		              URLConnection conn = url.openConnection(); 
//		              conn.setDoOutput(true); 
//		              OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream()); 
//		              wr.write( data ); 
//		              wr.flush(); 
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
	           return urls[0];
	       }
	        
	       @SuppressWarnings("unused")
		protected void onPostExecute(ArrayList<NewsItem> unused) {
	           // NOTE: You can call UI Element here.
	            
	           // Close progress dialog
//	           Dialog.dismiss();
	            
	           if (Error != null) {
	                
//	               uiUpdate.setText("Output : "+Error);
//	           	Toast.makeText(_context, "Error"+Error, Toast.LENGTH_LONG).show();
	                
	           } else {
	             
	           	// Show Response Json On Screen (activity)
//	           	uiUpdate.setText( Content );
	           	
	            /****************** Start Parse Response JSON Data *************/
	           	
	           	String OutputData = "";
	               JSONObject jsonResponse;
//	               Toast.makeText(_context, "Error"+Content, Toast.LENGTH_LONG).show();
	               try {
	                     
	                    /****** Creates a new JSONObject with name/value mappings from the JSON string. ********/
	                    jsonResponse = new JSONObject(Content);
	                     Log.d("RESPONSE----", jsonResponse.toString());
	                    /***** Returns the value mapped by name if it exists and is a JSONArray. ***/
	                    /*******  Returns null otherwise.  *******/
	                    JSONArray jsonMainNode = jsonResponse.optJSONArray("Android");
	                     
	                    /*********** Process each JSON Node ************/
	 
	                    int lengthJsonArr = jsonMainNode.length();  
	                    
	                    
	                    
	                    for(int i=0; i < lengthJsonArr; i++) 
	                    {
	                        /****** Get Object for each JSON node.***********/
	                        JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
	                         
	                        /******* Fetch node values **********/
	                        int news_id       = jsonChildNode.optInt("s_no");
//	                        String news_date       = jsonChildNode.optString("news_date").toString();
//	                        String news_time     = jsonChildNode.optString("news_date").toString();
	                        String news_title = jsonChildNode.optString("message").toString();
	                             NewsItem itm=new NewsItem(news_id,news_title);
	                             resultList.add(itm);
	                        //######################################################

	                        
	                        
	                   }
	                    
	                    
	                    

	                    
	                /****************** End Parse Response JSON Data *************/     
	                    

	                    
	                    
	                     
	                } catch (JSONException e) {          
	                    e.printStackTrace();
//	                    Toast.makeText(_context, e.toString(), Toast.LENGTH_LONG).show();
	                }
	 
	                
	            }
			
	       }
	        
	   }
		//+++++++++++++++++++++++++++++++++++++++++++++++++++





	
	
	
}
