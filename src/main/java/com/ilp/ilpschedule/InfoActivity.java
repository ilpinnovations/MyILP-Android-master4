package com.ilp.ilpschedule;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gcm.GCMRegistrar;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InfoActivity extends Activity implements OnClickListener,OnItemSelectedListener {

	
	
	
	
//	String serverURL_adminReg =AppConstant.URL+"adminreg_json.php";
	
	 Controller aController;
	 private ProgressDialog progress;
	 
	// UI elements
		EditText txtID;
		EditText txtName; 
		EditText txtBatch;
		EditText txtEmail;
		
		// Register button
		Button btnSub;
	 
		ImageButton btnBack;
		// Spinner element
		Spinner spinner;
		public static int id;
		public static String name;
		public static String loc;
		public static String batch;
		public static String email;
		public static String imei;
		public static boolean flag=false;
		 String regId;
		// Asyntask
		AsyncTask<Void, Void, Void> mRegisterTask;
		// label to display gcm messages
		TextView lblMessage;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_info);

				//testing to b removed

		ViewGroup vg = (ViewGroup) findViewById(R.id.root);
		Utils.setFontAllView(vg);
		txtID = (EditText) findViewById(R.id.txtID);
		txtName = (EditText) findViewById(R.id.txtName);
		txtBatch = (EditText) findViewById(R.id.txtBatch);
		
		spinner = (Spinner) findViewById(R.id.spinner);
		txtEmail = (EditText) findViewById(R.id.txtEmail);
//		txtPassCnfrm = (EditText) findViewById(R.id.txtPassCnfrm);
		btnSub = (Button) findViewById(R.id.btnSubmit);
		
		lblMessage = (TextView) findViewById(R.id.lblMessage);
		
		// Spinner click listener
				spinner.setOnItemSelectedListener(this);
		
	
		final DatabaseHandler db = new DatabaseHandler(this);
		
		/*if(db.getContactsCount()>0)
		{
//			db.close();
			finish();
			Intent i=new Intent(this,ScheduleActivity.class);
			startActivity(i);
			
			
		}*/
		
		loadSpinnerData();
		
		/*twoW=(CheckBox)findViewById(R.id.twoW);
		fourW=(CheckBox)findViewById(R.id.fourW);*/
		
		
		
		
		 aController = (Controller) getApplicationContext();
//		aController = (MyApp) getApplicationContext();
			// Check if Internet Connection present
			if (!aController.isConnectingToInternet()) {
				// Internet Connection is not present
				aController.showAlertDialog(InfoActivity.this,
						"Internet Connection Error",
						"Please connect to working Internet connection", false,InfoActivity.this);

						// stop executing code by return
//				finish();
			   return;
			   
			 
			 }
			
			else if(GCMRegistrar.isRegisteredOnServer(this)) {
				

					Intent i=new Intent(this,ScheduleActivity.class);
				i.putExtra("flag", flag);
					startActivity(i);
					Toast.makeText(getApplicationContext(), "Registered on Server", Toast.LENGTH_LONG).show();

				// Skips registration.
//					Toast.makeText(getApplicationContext(), "Please register yourself with ILP Schedule Server", Toast.LENGTH_LONG).show();
				finish();
				} 		
	
			
			
			/*else{
		
new LongOperation_updateslot(serverURL_adminReg,this).execute("");
			}*/
			
			
			// Click event on Register button
			btnSub.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View arg0) {  
					// Get data from EditText
					// Hiding the keyboard
					InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
					imm.hideSoftInputFromWindow(txtEmail.getWindowToken(), 0);
					
					if((txtID.getText().toString().length()==0) || (txtID.getText().toString().length()<6) )//|| (txtID.getText().toString().length()>6
					{        	
						txtID.setError( "Please Enter Valid Employee ID" );
					}
					else if(txtName.getText().toString().length()==0)
					{        	
						txtName.setError( "Please Enter Name" );
					}
				    else if(!isValidName(txtName.getText().toString()))
					{
						 txtName.setError( "Please Enter Valid Name" );
					}
					else if(txtBatch.getText().toString().length()==0  )
					{   txtBatch.setError( "Please Enter LG Name" ); 	
					
					
					}	
				    
					
					else if(txtEmail.getText().toString().length()==0 || !txtEmail.getText().toString().endsWith("@tcs.com") )
					{   txtEmail.setError( "Please Enter Email" );
					}	
				    
					else if(!isValidEmail(txtEmail.getText().toString()))
					{
						 txtEmail.setError( "Please Enter Valid Email" );
					}
					/*else  if((txtPass.getText().toString().length()==0) || (txtPass.getText().toString().length()<8))
					{        	
						txtPass.setError( "Please Enter Password of atleast 8 characters" );
					}
					else  if(!(txtPass.getText().toString()).equalsIgnoreCase(txtPassCnfrm.getText().toString()) )
					{        	
						txtPassCnfrm.setError( "Please Enter the Correct Password" );
					}*/
					/*else if(!(twoW.isChecked() || fourW.isChecked()))
					{
						 twoW.setError( "Please Select Vehicle Type" );
						 fourW.setError( "Please Select Vehicle Type" );
					}*/
					
					
					else{
					  // GET IMEI NUMBER      
					 TelephonyManager tManager = (TelephonyManager) getBaseContext()
					    .getSystemService(Context.TELEPHONY_SERVICE);
					   imei = tManager.getDeviceId();
//					  String devicePhn = tManager.getLine1Number();
//					  Toast.makeText(getApplicationContext(), devicePhn, Toast.LENGTH_LONG).show();
//					  int id=Integer.parseInt(txtID.getText().toString());
						 name = txtName.getText().toString(); 
						 id = Integer.parseInt(txtID.getText().toString());
						 batch = txtBatch.getText().toString();
						 email = txtEmail.getText().toString();
					  
					
		          /*  int chek_2W=0,chek_4W=0;		
					if(twoW.isChecked() && fourW.isChecked())
					{
						chek_2W=1;
						chek_4W=1;
					}
					else if(fourW.isChecked())
					{
						chek_4W=1;
					}
					else if (twoW.isChecked()) {
						chek_2W=1;
					}
*/					// Check if user filled the form
					if(name.trim().length() > 0 && batch.trim().length() > 0){
						
						
						
//						new LongOperation_reg(serverURL_adminReg,name,org,email,pass,InfoActivity.this).execute("");
						
//						// Hiding the keyboard
//						InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//						imm.hideSoftInputFromWindow(txtBatch.getWindowToken(), 0);
					        
					        /**
					         * CRUD Operations
					         * */
					        // Inserting Contacts
					        Log.d("Insert: ", "Inserting ..");
					        db.addContact(new Info(id, name, loc, batch));
						
					        if(db.getContactsCount()>0)
							{
					        	
					        	Toast.makeText(getApplicationContext(), "Submitted Successfully", Toast.LENGTH_LONG).show();	
//					        	db.close();
								
								
								// Launch Main Activity
								Intent i = new Intent(getApplicationContext(), ScheduleActivity.class);
								GCMRegistrar.setRegisteredOnServer(getApplicationContext(), true);
								// Registering user on our server					
								// Sending registraiton details to MainActivity_old
								i.putExtra("id", id);
								i.putExtra("name", name);
								i.putExtra("loc", loc);
								i.putExtra("batch", batch);
								i.putExtra("email", email);
								i.putExtra("imei", imei);
								flag=true;
								i.putExtra("flag", flag);
								
								startActivity(i);
								finish();
								
								
								
								
							}
						
						
						
						
						
					}else{
						
						// user doen't filled that data
						aController.showAlertDialog(InfoActivity.this, "Error!", "Please enter your details", false,InfoActivity.this);
					}
					}
				}
			});
			
			
			
			btnBack = (ImageButton) findViewById(R.id.btn_list);
//			btnLike = (ImageButton) findViewById(R.id.btnImg_Like);

			btnBack.setOnClickListener(this);
//			btnLike.setOnClickListener(this);

			
			
			
			
	}
	
	
	
	/**
	 * Function to load the spinner data from SQLite database
	 * */
	private void loadSpinnerData() {
		// database handler
//		DatabaseHandler db = new DatabaseHandler(getApplicationContext());

		// Spinner Drop down elements
		List<String> lables = new ArrayList<String>();
          
		
		lables.add("Chennai");
		/*lables.add("Guwahati");*/
		lables.add("Hyderabad");
		lables.add("Trivandrum");
		lables.add("Ahmedabad");
		
		// Creating adapter for spinner
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, lables);

		// Drop down layout style - list view with radio button
		dataAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		// attaching data adapter to spinner
		spinner.setAdapter(dataAdapter);
	}

	
	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
		// On selecting a spinner item
		 loc = parent.getItemAtPosition(position).toString();

		// Showing selected spinner item
		Toast.makeText(parent.getContext(), "You selected: " + loc,
				Toast.LENGTH_LONG).show();

	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub

	}
	
	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.btn_list) {
			finish();
		}
		/*if (v.getId() == R.id.btnImg_Like) {

		}*/

	}
	private boolean isValidEmail(String email) {
		String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
				+ "tcs.com";
 
		Pattern pattern = Pattern.compile(EMAIL_PATTERN);
		Matcher matcher = pattern.matcher(email);
		return matcher.matches();
	}
	
	private boolean isValidName(String name) {
		String NAME_PATTERN = "[A-Za-z-\\s]*";
 
		Pattern pattern = Pattern.compile(NAME_PATTERN);
		Matcher matcher = pattern.matcher(name);
		return matcher.matches();
	}
	
	
	

	  
	  
	 

	  
	  
	  
	  
	  
}
