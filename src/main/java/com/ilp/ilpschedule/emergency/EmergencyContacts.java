package com.ilp.ilpschedule.emergency;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.baoyz.swipemenulistview.SwipeMenuListView.OnMenuItemClickListener;
import com.baoyz.swipemenulistview.SwipeMenuListView.OnSwipeListener;
import com.ilp.ilpschedule.DatabaseHandler;
import com.ilp.ilpschedule.MenuFragment;
import com.ilp.ilpschedule.R;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.List;


public class EmergencyContacts extends Activity{

	private List<Contact> mAppList;
	private AppAdapter mAdapter;
	private SwipeMenuListView mListView;
	private ProgressDialog progress;
	AsyncTask<Void, Void, Void> mRegisterTask;
	private String jsonContent;
	private DatabaseHandler db;

	private SlidingMenu menu;
	ImageButton btnList;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_emergency_contact);
		db = new DatabaseHandler(this);
		/********************************************************/
        	/* Sliding menu part starts*/
		/********************************************************/

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

		/********************************************************/
        	/* Sliding menu part ends*/
		/********************************************************/
		mListView = (SwipeMenuListView) findViewById(R.id.swipeListView);
		mAppList = db.getEmergencyContacts();
		if(mAppList.size()==0)
		{
			new LongOperation_getSchedule().execute();
		}
		mAdapter = new AppAdapter();
		mListView.setAdapter(mAdapter);
		SwipeMenuCreator creator = new SwipeMenuCreator() {

			@Override
			public void create(SwipeMenu menu) {
				// create "open" item
				SwipeMenuItem callItem = new SwipeMenuItem(
						getApplicationContext());
				// set item background
				callItem.setBackground(new ColorDrawable(Color.parseColor("#89CFF0")));
				// set item width
				callItem.setWidth(dp2px(70));
				// set item icon
				callItem.setIcon(R.drawable.call);
				// add to menu
				menu.addMenuItem(callItem);

				// create "delete" item
				SwipeMenuItem smsItem = new SwipeMenuItem(
						getApplicationContext());
				// set item background
				smsItem.setBackground(new ColorDrawable(Color.parseColor("#89CFF0")));
				// set item width
				smsItem.setWidth(dp2px(70));
				// set item icon
				smsItem.setIcon(R.drawable.sms);
				// add to menu
				menu.addMenuItem(smsItem);
			}
		};

		// set creator
		mListView.setMenuCreator(creator);

		mListView.setOnMenuItemClickListener(new OnMenuItemClickListener() {
			@Override
			public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
				switch (index) {
					case 0:
						//add call
						Intent callIntent = new Intent(Intent.ACTION_CALL);
						callIntent.setData(Uri.parse("tel:"+mAppList.get(position).getPhoneNum()));
						startActivity(callIntent);
						break;
					case 1:
						//add sms
						Intent smsIntent = new Intent(Intent.ACTION_VIEW);
						smsIntent.setData(Uri.parse("sms:"+mAppList.get(position).getPhoneNum()));
						startActivity(smsIntent);
						break;
				}
				return false;
			}
		});

		// set SwipeListener
		mListView.setOnSwipeListener(new OnSwipeListener() {

			@Override
			public void onSwipeStart(int position) {
				// swipe start
			}

			@Override
			public void onSwipeEnd(int position) {

			}
		});


		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
									long arg3) {
				mListView.smoothOpenMenu(position);

			}
		});

	}

	class AppAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return mAppList.size();
		}

		@Override
		public Contact getItem(int position) {
			return mAppList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = View.inflate(getApplicationContext(),
						R.layout.item_list_app, null);
				new ViewHolder(convertView);
			}
			ViewHolder holder = (ViewHolder) convertView.getTag();
			Contact item = getItem(position);
			holder.title.setText(item.getTitle());
			holder.contact.setText(item.getPhoneNum());
			return convertView;
		}

		class ViewHolder {
			TextView title;
			TextView contact;

			public ViewHolder(View view) {
				title = (TextView) view.findViewById(R.id.title);
				contact = (TextView) view.findViewById(R.id.contact);
				view.setTag(this);
			}
		}
	}


	private int dp2px(int dp) {
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
				getResources().getDisplayMetrics());
	}


	/****************************************************************/
	/* Async task to get json data from  internet */
	/****************************************************************/

	private class LongOperation_getSchedule extends
			AsyncTask<String, Void, String> {

		@Override
		protected void onPreExecute() {

			progress = ProgressDialog.show(
					EmergencyContacts.this, "", "");
			progress.setContentView(R.layout.progress);
			progress.show();

		}
		@Override
		protected String doInBackground(String... params) {

			// fetch data from internet

			BufferedReader reader = null;

			// Send data
			try {
				DatabaseHandler db = new DatabaseHandler(EmergencyContacts.this);
				String data = URLEncoder.encode("ilp", "UTF-8") + "="
						+ URLEncoder.encode(db.getLocation(), "UTF-8");

				URL url = new URL("http://theinspirer.in/ilpscheduleapp/getEmergencyContacts.php");

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
				jsonContent = sb.toString();

			} catch (Exception ex) {
				ex.printStackTrace();
			} finally {
				try {

					reader.close();
				}

				catch (Exception ex) {
				}
			}
			Log.d("Content========>", jsonContent);
			return null;
		}
		@Override
		protected void onPostExecute(String result) {
			mRegisterTask = null;
			Log.d("message", "On postExecute.");
			if(jsonContent!=null) {
				try {
					JSONArray jsonArray = new JSONArray(jsonContent);
					for(int i=0;i<jsonArray.length();i++)
					{
						JSONObject jsonResponse = jsonArray.getJSONObject(i);
						Log.d("RESPONSE---->", jsonResponse.toString());
						Iterator<String> keyList = jsonResponse.keys();
						Log.d("Message---->", "Parsing started");
						while(keyList.hasNext())
						{
							String title=keyList.next();
							String phoneNum = jsonResponse.getString(title);
							Contact contact = new Contact(title,phoneNum);
							Log.d("contact----------->", contact.getTitle());
							mAppList.add(contact);
						}
					}
					db.addEmergencyContacts(mAppList);
					mAdapter.notifyDataSetChanged();
					Log.d("Message---->", "Parsing done");

				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			Log.d("message", "PostExecute ends here");
			if (progress.isShowing())
				progress.dismiss();
		}

	}
	/****************************************************************/
	/* Async task ends here */
	/****************************************************************/


}

