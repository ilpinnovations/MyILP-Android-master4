package com.ilp.ilpschedule;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RatingBar;
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
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class FeedbackActivity extends Activity implements OnClickListener {

	String serverURL_feedback = AppConstant.URL + "feedback_json.php";

	Controller aController;
	private ProgressDialog progress;

	// UI elements
	EditText txtfaculty;
	EditText txtCourse;
	EditText txtComment;
	// Submit button
	Button btnSub;

	ImageButton btnBack;

	RatingBar ratingbar1;

	String empid, empname, emploc, empbatch;

	String slot, date;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_feedback);

		ViewGroup vg = (ViewGroup) findViewById(R.id.root);
		Utils.setFontAllView(vg);

		txtfaculty = (EditText) findViewById(R.id.txtFaculty);
		txtCourse = (EditText) findViewById(R.id.txtCourse);
		txtComment = (EditText) findViewById(R.id.txtComment);
		ratingbar1 = (RatingBar) findViewById(R.id.rate);

		btnSub = (Button) findViewById(R.id.btnSubmit);
		txtComment.addTextChangedListener(new TextWatcher() {

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
				if (txtComment.getText().length() > 0) {
					txtComment.setError(null);
				}
			}
		});

		Intent get = getIntent();
		txtfaculty.setText(get.getStringExtra("faculty"));
		txtCourse.setText(get.getStringExtra("course"));
		slot = get.getStringExtra("slot");
		date = get.getStringExtra("date");

		final DatabaseHandler db = new DatabaseHandler(this);

		if (db.getContactsCount() > 0) {
			// db.close();

			List<Info> getInfo = db.getAllContacts();
			if (getInfo.size() > 0) {
				empid = String.valueOf(getInfo.get(0).getID());
				empname = getInfo.get(0).getName();
				emploc = getInfo.get(0).getLoc();
				empbatch = getInfo.get(0).getBatch();
			}
		}

		aController = (Controller) getApplicationContext();

		// DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date date1 = new Date();
		String getDate1 = dateFormat.format(date1);

		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		// String dateInString = "7-Jun-2013";

		try {

			Date date2 = formatter.parse(date);

			String getDate2 = formatter.format(date2);

			if (!getDate1.equals(getDate2)) {
				aController
						.showAlertDialog(
								FeedbackActivity.this,
								"Feedback Error",
								"Sorry! You can only give the feedback for current date.",
								false, FeedbackActivity.this);

				// stop executing code by return
				// finish();
				return;

			}

		} catch (ParseException e) {
			e.printStackTrace();
		}

		// Click event on Submit button
		btnSub.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// Get data from EditText

				if (txtComment.getText().toString().length() == 0) {
					txtComment.setError("Please Enter Comment");

				}

				else {

					String faculty = txtfaculty.getText().toString();
					String course = txtCourse.getText().toString();
					String comment = txtComment.getText().toString();

					String rating = String.valueOf(ratingbar1.getRating());

					// / Check if user filled the form
					if (faculty.trim().length() > 0
							&& comment.trim().length() > 0) {

						new LongOperation_feed(serverURL_feedback, faculty,
								course, comment, rating, FeedbackActivity.this)
								.execute("");

					} else {

						// user doen't filled that data
						aController.showAlertDialog(FeedbackActivity.this,
								"Error!", "Please enter your details", false,
								FeedbackActivity.this);
					}
				}
			}
		});

		btnBack = (ImageButton) findViewById(R.id.btn_list);

		btnBack.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.btn_list) {
			finish();
		}

	}

	// +++++++++++++++++++++++++++++++++++++++++++++++++++

	// Class with extends AsyncTask class
	private class LongOperation_feed extends AsyncTask<String, Void, String> {
		String _url, _faculty, _course, _comment, _rate;
		Activity __context;

		// Required initialization
		public LongOperation_feed(String url, String faculty, String course,
				String comment, String rate, Activity c) {
			_url = url;
			__context = c;

			_faculty = faculty;
			_course = course;
			_comment = comment;
			_rate = rate;

		}

		private final HttpClient Client = new DefaultHttpClient();
		private String Content;
		private String Error = null;

		protected void onPreExecute() {
			// NOTE: You can call UI Element here.

			// Start Progress Dialog (Message)

			progress = ProgressDialog.show(FeedbackActivity.this, "", "");
			progress.setContentView(R.layout.progress);
			progress.show();

			// }

		}

		// Call after onPreExecute method
		protected String doInBackground(String... urls) {

			/************ Make Post Call To Web Server ***********/
			BufferedReader reader = null;

			// Send data
			try {

				String data = URLEncoder.encode("faculty", "UTF-8") + "="
						+ URLEncoder.encode(_faculty, "UTF-8");
				data += "&" + URLEncoder.encode("course", "UTF-8") + "="
						+ URLEncoder.encode(_course, "UTF-8");
				data += "&" + URLEncoder.encode("comment", "UTF-8") + "="
						+ URLEncoder.encode(_comment, "UTF-8");
				data += "&" + URLEncoder.encode("rate", "UTF-8") + "="
						+ URLEncoder.encode(_rate, "UTF-8");
				data += "&" + URLEncoder.encode("empid", "UTF-8") + "="
						+ URLEncoder.encode(empid, "UTF-8");
				data += "&" + URLEncoder.encode("empname", "UTF-8") + "="
						+ URLEncoder.encode(empname, "UTF-8");
				data += "&" + URLEncoder.encode("emploc", "UTF-8") + "="
						+ URLEncoder.encode(emploc, "UTF-8");
				data += "&" + URLEncoder.encode("empbatch", "UTF-8") + "="
						+ URLEncoder.encode(empbatch, "UTF-8");
				data += "&" + URLEncoder.encode("slot", "UTF-8") + "="
						+ URLEncoder.encode(slot, "UTF-8");
				data += "&" + URLEncoder.encode("date", "UTF-8") + "="
						+ URLEncoder.encode(date, "UTF-8");
				//

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
			// Dialog.dismiss();

			if (Error != null) {

				// uiUpdate.setText("Output : "+Error);
				Toast.makeText(
						__context,
						"Error due to some network problem! Please connect to internet. ",
						Toast.LENGTH_LONG).show();

			} else {

				// Show Response Json On Screen (activity)
				// uiUpdate.setText( Content );

				/****************** Start Parse Response JSON Data *************/

				String feed_result = "";

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

						for (int i = 0; i < lengthJsonArr; i++) {
							/****** Get Object for each JSON node. ***********/
							JSONObject jsonChildNode = jsonMainNode
									.getJSONObject(i);

							/******* Fetch node values **********/
							feed_result = jsonChildNode
									.optString("feed_result").toString();

						}

						if (feed_result.equalsIgnoreCase("success")) {

							aController.showAlertDialog(FeedbackActivity.this,
									"Feedback Status:",
									"Thanks for your valuable feedback!", true,
									FeedbackActivity.this);
							Toast.makeText(FeedbackActivity.this,
									"Submitted Successfully! ",
									Toast.LENGTH_LONG).show();
						} else if (feed_result.equalsIgnoreCase("already")) {

							aController.showAlertDialog(FeedbackActivity.this,
									"Feedback Status:",
									"You have already given the feedback!",
									false, FeedbackActivity.this);
							Toast.makeText(FeedbackActivity.this,
									"You have already given the feedback! ",
									Toast.LENGTH_LONG).show();
						}

						else {

							Toast.makeText(FeedbackActivity.this,
									"Not Submitted Successfully! ",
									Toast.LENGTH_LONG).show();

						}

						/****************** End Parse Response JSON Data *************/

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
