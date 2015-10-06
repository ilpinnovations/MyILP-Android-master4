package com.ilp.ilpschedule;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.Notification;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gcm.GCMRegistrar;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.Wearable;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.os.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ScheduleActivity extends Activity {

	private TextView Output;
	private Button changeDate;

	private int year;
	private int month;
	private int day;

	static final int DATE_PICKER_ID = 1111;

	String select_date;
	String serverURL_getSchedule = AppConstant.URL + "schedulelist_json.php";
	private ProgressDialog progress;

	Button btnGetSchedule;
	EditText txtBatch;
	ListView listView;
	ArrayList<CatItem> resultList;
	Controller aController;

	public static int id;
	public static String name;
	public static String loc;
	public static String batch;
	public static String email;
	public static String imei;
	// Asyntask
	AsyncTask<Void, Void, Void> mRegisterTask;
	private SlidingMenu menu;
	ImageButton btnList;
    

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		ViewGroup vg = (ViewGroup) findViewById(R.id.main_root);
		Utils.setFontAllView(vg);
		Output = (TextView) findViewById(R.id.Output);
		changeDate = (Button) findViewById(R.id.changeDate);
		listView = (ListView) findViewById(R.id.list_colors);

		// configure the SlidingMenu
		menu = new SlidingMenu(this);
		menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		menu.setShadowWidthRes(R.dimen.shadow_width);
		menu.setShadowDrawable(R.drawable.shadow);
		menu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
		menu.setFadeDegree(0.35f);
		menu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
		menu.setSlidingEnabled(false);
		menu.setMenu(R.layout.menu_frame);



		getFragmentManager().beginTransaction()
				.replace(R.id.menu_frame, new MenuFragment()).commit();

		btnList = (ImageButton) findViewById(R.id.btn_list);
		btnList.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (v.getId() == R.id.btn_list) {
					if (menu.isMenuShowing()) {
						menu.showContent();
					} else {
						menu.showMenu();
					}
				}
			}
		});

		resultList = new ArrayList<CatItem>();

		txtBatch = (EditText) findViewById(R.id.txtBatch);

		txtBatch.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				if (txtBatch.getText().length() > 0) {
					txtBatch.setError(null);
				}
			}
		});
/*************************checking****************************/


        /*************************************/
		btnGetSchedule = (Button) findViewById(R.id.btnGetSchedule);

		final DatabaseHandler db = new DatabaseHandler(this);
		if (db.getContactsCount() > 0) {

			List<Info> getInfo = db.getAllContacts();
			if (getInfo.size() > 0) {
				txtBatch.setText(getInfo.get(0).getBatch());
			}
		}
        Button b1 = (Button) findViewById(R.id.button);
        b1.setOnClickListener(new OnClickListener() {
            @Override

            public void onClick(View view) {

                String batch = txtBatch.getText().toString();

                new LongOperation_getSchedule(serverURL_getSchedule,
                        ScheduleActivity.this, batch, select_date)
                        .execute("");

                Notification notification = new NotificationCompat.Builder(getApplication())
                        .setSmallIcon(R.drawable.ic_launcher)
                        .setContentTitle("Hello World")
                        .setContentText("My first Android Wear notification")
                        .extend(
                                new NotificationCompat.WearableExtender().setHintShowBackgroundOnly(true))
                        .build();
                NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getApplication());
                int notificationId = 1;
                notificationManager.notify(notificationId, notification);
            }
        });

		btnGetSchedule.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				// Hiding the keyboard
				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(txtBatch.getWindowToken(), 0);

				aController = (Controller) getApplicationContext();

				// Check if Internet Connection present
				if (!aController.isConnectingToInternet()) {
					// Internet Connection is not present

					Toast.makeText(getApplicationContext(),
							"Please connect to working Internet connection",
							Toast.LENGTH_LONG).show();

					// stop executing code by return
					// finish();
					return;

				} else if (txtBatch.getText().toString().length() == 0) {
					txtBatch.setError("Please Enter LG Name");
				} else {

					resultList.clear();
					String batch = txtBatch.getText().toString();

					new LongOperation_getSchedule(serverURL_getSchedule,
							ScheduleActivity.this, batch, select_date)
							.execute("");
				}
			}
		});

		// Get current date by calender

		final Calendar c = Calendar.getInstance();
		year = c.get(Calendar.YEAR);
		month = c.get(Calendar.MONTH);
		day = c.get(Calendar.DAY_OF_MONTH);

		// Show current date

		Output.setText(new StringBuilder()
				// Month is 0 based, just add 1
				.append(day).append("-").append(month + 1).append("-")
				.append(year).append(" "));
		select_date = (new StringBuilder().append(year).append("-")
				.append(month + 1).append("-").append(day)).toString();
		// Toast.makeText(getApplicationContext(), select_date,
		// Toast.LENGTH_LONG).show();
		// Button listener to show date picker dialog

		changeDate.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				// On button click show datepicker dialog
				showDialog(DATE_PICKER_ID);

			}

		});

		// ____________________________________START__________________________

		Intent i = getIntent();
		boolean flag = i.getBooleanExtra("flag", false);

		aController = (Controller) getApplicationContext();

		if (!GCMRegistrar.isRegisteredOnServer(this) && flag == false) {

			Toast.makeText(this,"Not Registered on the server",Toast.LENGTH_SHORT).show();
			Intent i1 = new Intent(this, InfoActivity.class);

			startActivity(i1);
			// Skips registration.

			finish();
		}

		else if (flag) {

			// Getting name, email from intent

			id = i.getIntExtra("id", 0);

			name = i.getStringExtra("name");
			loc = i.getStringExtra("loc");
			batch = i.getStringExtra("batch");
			email = i.getStringExtra("email");
			imei = i.getStringExtra("imei");
			Toast.makeText(this,loc,Toast.LENGTH_SHORT).show();
			// Make sure the device has the proper dependencies.
			GCMRegistrar.checkDevice(this);

			// Make sure the manifest permissions was properly set
			GCMRegistrar.checkManifest(this);

			// lblMessage = (TextView) findViewById(R.id.lblMessage);

			// Register custom Broadcast receiver to show messages on activity
			registerReceiver(mHandleMessageReceiver, new IntentFilter(
					Config.DISPLAY_MESSAGE_ACTION));

			// Get GCM registration id
			final String regId = GCMRegistrar.getRegistrationId(this);

			// Check if regid already presents
			if (regId.equals("")) {

				// Register with GCM
				GCMRegistrar.register(this, Config.GOOGLE_SENDER_ID);

			} else {

				// Device is already registered on GCM Server
				if (GCMRegistrar.isRegisteredOnServer(this)) {

					// Skips registration.
					Toast.makeText(getApplicationContext(),
							"Already registered with ILP Schedule Server",
							Toast.LENGTH_LONG).show();

				} else {

					// Try to register again, but not in the UI thread.
					// It's also necessary to cancel the thread onDestroy(),
					// hence the use of AsyncTask instead of a raw thread.

					final Context context = this;
					mRegisterTask = new AsyncTask<Void, Void, Void>() {

						protected void onPreExecute() {
							// NOTE: You can call UI Element here.

							// Start Progress Dialog (Message)

							//
							progress = ProgressDialog.show(
									ScheduleActivity.this, "", "");
							progress.setContentView(R.layout.progress);
							progress.show();
						}

						@Override
						protected Void doInBackground(Void... params) {

							// Register on our server
							// On server creates a new user
							aController.register(context, name, email, regId,
									id, loc, batch, imei);

							return null;
						}

						@Override
						protected void onPostExecute(Void result) {
							mRegisterTask = null;
							if (progress.isShowing())
								progress.dismiss();
						}

					};

					// execute AsyncTask
					mRegisterTask.execute(null, null, null);
				}
			}

		}

		// Check if Internet Connection present
		else if (!aController.isConnectingToInternet()) {
			// Internet Connection is not present
			/*
			 * aController.showAlertDialog(ScheduleActivity.this,
			 * "Internet Connection Error",
			 * "Please connect to working Internet connection",
			 * false,ScheduleActivity.this);
			 */
			Toast.makeText(getApplicationContext(),
					"Please connect to working Internet connection",
					Toast.LENGTH_LONG).show();
			// stop executing code by return
			// finish();
			return;

		}

		else if (txtBatch.getText().toString().length() == 0) {
			txtBatch.setError("Please Enter LG Name");
		}

		else {

			resultList.clear();
			String batch = txtBatch.getText().toString();

			new LongOperation_getSchedule(serverURL_getSchedule,
					ScheduleActivity.this, batch, select_date).execute("");
		}

		// ____________________________________END__________________________

	}

	private Object getSupportFragmentManager() {
		// TODO Auto-generated method stub
		return null;
	}

	// Create a broadcast receiver to get message and show on screen
	private final BroadcastReceiver mHandleMessageReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {

			String newMessage = intent.getExtras().getString(
					Config.EXTRA_MESSAGE);

			// Waking up mobile if it is sleeping
			aController.acquireWakeLock(getApplicationContext());

			// Display message on the screen
			// lblMessage.append(newMessage + "\n");

			// Toast.makeText(getApplicationContext(), "Got Message: " +
			// newMessage, Toast.LENGTH_LONG).show();

			// Releasing wake lock
			aController.releaseWakeLock();
		}
	};

	@Override
	protected void onDestroy() {
		// Cancel AsyncTask
		if (mRegisterTask != null) {
			mRegisterTask.cancel(true);
		}
		try {
			// Unregister Broadcast Receiver
			unregisterReceiver(mHandleMessageReceiver);

			// Clear internal resources.
			GCMRegistrar.onDestroy(this);

		} catch (Exception e) {
			Log.e("UnRegister Receiver Error", "> " + e.getMessage());
		}
		super.onDestroy();
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DATE_PICKER_ID:

			// open datepicker dialog.
			// set date picker for current date
			// add pickerListener listner to date picker
			return new DatePickerDialog(this, pickerListener, year, month, day);
		}
		return null;
	}

	private DatePickerDialog.OnDateSetListener pickerListener = new DatePickerDialog.OnDateSetListener() {

		// when dialog box is closed, below method will be called.
		@Override
		public void onDateSet(DatePicker view, int selectedYear,
				int selectedMonth, int selectedDay) {

			year = selectedYear;
			month = selectedMonth;
			day = selectedDay;

			// Show selected date
			Output.setText(new StringBuilder().append(day).append("-")
					.append(month + 1).append("-").append(year).append(" "));

			select_date = (new StringBuilder().append(year).append("-")
					.append(month + 1).append("-").append(day)).toString();
			// Toast.makeText(getApplicationContext(), select_date,
			// Toast.LENGTH_LONG).show();

		}
	};

	// +++++++++++++++++++++++++++++++++++++++++++++++++++

	// Class with extends AsyncTask class
	private class LongOperation_getSchedule extends
			AsyncTask<String, Void, String> {
		String _url;
		String _batch, _date;
		Activity __context;

		String[] values;
		Integer[] images;

		// Required initialization
		public LongOperation_getSchedule(String url, Activity c, String batch,
				String date) {
			_url = url;
			__context = c;
			_batch = batch;
			_date = date;

		}

		private final HttpClient Client = new DefaultHttpClient();
		private String Content;
		private String Error = null;

		String data = "";

		int sizeData = 0;

		protected void onPreExecute() {
			// NOTE: You can call UI Element here.

			progress = ProgressDialog.show(ScheduleActivity.this, "", "");
			progress.setContentView(R.layout.progress);
			progress.show();

		}

		// Call after onPreExecute method
		protected String doInBackground(String... urls) {

			/************ Make Post Call To Web Server ***********/
			BufferedReader reader = null;

			// Send data
			try {

				data = URLEncoder.encode("batch", "UTF-8") + "="
						+ URLEncoder.encode(_batch.trim(), "UTF-8");
				data += "&" + URLEncoder.encode("date", "UTF-8") + "="
						+ URLEncoder.encode(_date, "UTF-8");

				URL url = new URL(_url);

				//
				// // Send POST data request
				//
				URLConnection conn = url.openConnection();
				conn.setDoOutput(true);
				OutputStreamWriter wr = new OutputStreamWriter(
						conn.getOutputStream());
				wr.write(data);
				wr.flush();
				//
				// Get the server response

				reader = new BufferedReader(new InputStreamReader(
						conn.getInputStream()));
				StringBuilder sb = new StringBuilder();
				String line = null;

				// Read Server Response
				while ((line = reader.readLine()) != null) {
					// Append server response in string
					sb.append(line + "\n");
				}

				// Append Server Response To Content String
				Content = sb.toString();
			} catch (Exception ex) {
				Error = ex.getMessage();
			} finally {
				try {

					reader.close();
				}

				catch (Exception ex) {
				}
			}

			/*****************************************************/
			return "";
		}

		@SuppressWarnings("unused")
		protected void onPostExecute(String unused) {
			// NOTE: You can call UI Element here.

			// Close progress dialog
			if (progress.isShowing())
				progress.dismiss();

			if (Error != null) {

				Toast.makeText(
						__context,
						"Error due to some network problem! Please connect to internet. ",
						Toast.LENGTH_LONG).show();

			} else {

				// Show Response Json On Screen (activity)

				/****************** Start Parse Response JSON Data *************/

				String result = "";

				String OutputData = "";
				JSONObject jsonResponse;
				// Toast.makeText(_context, "Error"+Content,
				// Toast.LENGTH_LONG).show();
				try {
					if (Content != null) {
						/******
						 * Creates a new JSONObject with name/value mappings
						 * from the JSON string.
						 ********/
						jsonResponse = new JSONObject(Content);
						Log.d("RESPONSE----", jsonResponse.toString());
						/*****
						 * Returns the value mapped by name if it exists and is
						 * a JSONArray.
						 ***/
						/******* Returns null otherwise. *******/
						JSONArray jsonMainNode = jsonResponse
								.optJSONArray("Android");

						/*********** Process each JSON Node ************/

						int lengthJsonArr = jsonMainNode.length();

						values = new String[lengthJsonArr];
						images = new Integer[lengthJsonArr];

						// ArrayList<Scores_Category> categoriesList=new
						// ArrayList<Scores_Category>();

						for (int i = 0; i < lengthJsonArr; i++) {
							/****** Get Object for each JSON node. ***********/
							JSONObject jsonChildNode = jsonMainNode
									.getJSONObject(i);

							/******* Fetch node values **********/

							values[i] = jsonChildNode.optString("slot")
									.toString();
							images[i] = R.drawable.ico_menu;

							String strdate = jsonChildNode.optString("date1")
									.toString();
							String batch = jsonChildNode.optString("batch")
									.toString();
							String slot = jsonChildNode.optString("slot")
									.toString();
							String course = jsonChildNode.optString("course")
									.toString();
							String faculty = jsonChildNode.optString("faculty")
									.toString();
							String room = jsonChildNode.optString("room")
									.toString();

							result = jsonChildNode.optString("result")
									.toString();

							CatItem itm = new CatItem(strdate, batch, slot,
									course, faculty, room, false);
							resultList.add(itm);

						}

						if (result.equalsIgnoreCase("success")) {

							if (resultList.size() > 0) {

								// Create Color Adapter
								ScheduleArrayAdapter adapter = new ScheduleArrayAdapter(
										ScheduleActivity.this, values,
										resultList, images);

								// Assign adapter to ListView
								listView.setAdapter(adapter);
								listView.setVisibility(View.VISIBLE);
								// listView.setOnItemClickListener(ScheduleActivity.this);
							}

						} else {
							/*
							 * aController.showAlertDialog(ScheduleActivity.this,
							 * "Voting Status:", "Voted Successfully!",
							 * true,TopicActivity.this);
							 */
							listView.setVisibility(View.GONE);
							Toast.makeText(__context, "No schedule found!",
									Toast.LENGTH_LONG).show();
						}

						/****************** End Parse Response JSON Data *************/

						// Show Parsed Output on screen (activity)
						// jsonParsed.setText( OutputData );

						// Create custom adapter for listview
						// adapter = new LazyImageLoadAdapter(News.this,
						// mStrings,news_date_arr,news_time_arr,news_title_arr,news_desc_arr);

						// Set adapter to listview
						// list.setAdapter(adapter);

						// -------------------------Fmard-----------------Start
						// Intent i=new Intent(News.this,News.class);
						// i.putExtra("news_date", news_date_arr);
						// i.putExtra("news_time", news_time_arr);
						// i.putExtra("news_title", news_title_arr);
						// i.putExtra("news_desc", news_desc_arr);
						// i.putExtra("img_urls", mStrings);
						// i.putExtra("bigimg_urls",news_bigimg_arr);
						//
						//
						// i.putExtra("categories", categoriesList);
						//
						// startActivity(i);
						// -------------------------Fmard--------------End---

					}

					else {
						Toast.makeText(
								__context,
								"Error due to some network problem! Please connect to internet. ",
								Toast.LENGTH_LONG).show();
					}

				} catch (JSONException e) {
					e.printStackTrace();
					// Toast.makeText(_context, e.toString(),
					// Toast.LENGTH_LONG).show();
				}

			}

		}

	}
	// +++++++++++++++++++++++++++++++++++++++++++++++++++

}