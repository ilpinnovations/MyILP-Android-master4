package com.ilp.ilpschedule;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.ilp.ilpschedule.emergency.EmergencyContacts;
import com.ilp.ilpschedule.geo.GeoPlane;

import java.util.ArrayList;
@SuppressLint("NewApi")
public class MenuFragment extends Fragment implements OnItemClickListener {

	ListView listView;
	ArrayList<MenuItem> lstMenuItems;
	View vw_layout;
	private ProgressDialog progress;
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater,
	 * android.view.ViewGroup, android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		if (container == null) {
			// We have different layouts, and in one of them this
			// fragment's containing frame doesn't exist. The fragment
			// may still be created from its saved state, but there is
			// no reason to try to create its view hierarchy because it
			// won't be displayed. Note this is not needed -- we could
			// just run the code below, where we would create and return
			// the view hierarchy; it would just never be used.
			return null;
		}
		vw_layout = inflater.inflate(R.layout.fragment_menu, container, false);

		this.initialiseMenuItems(savedInstanceState);
		
		Utils.setFontAllView((ViewGroup) vw_layout);
		return vw_layout;
	}

	/**
	 * Step 2: Setup Menu Items
	 */
	private void initialiseMenuItems(Bundle args) {
		// get list view
		listView = (ListView) this.vw_layout.findViewById(R.id.lst_menu);

		lstMenuItems = new ArrayList<MenuItem>();

		addMenuItem(R.string.txt_menu_home, R.drawable.ic_launcher,
				NewsActivity.class, args);

		addMenuItem(R.string.txt_menu_geo, R.drawable.ic_launcher,
				NewsActivity.class, args);
		
		addMenuItem(R.string.txt_menu_badges, R.drawable.ic_launcher,
				NewsActivity.class, args);
		
		addMenuItem(R.string.txt_menu_notifications, R.drawable.ic_launcher,
				NewsActivity.class, args);
		
		addMenuItem(R.string.txt_menu_emergency, R.drawable.ic_launcher,
				EmergencyContacts.class, args);
		
		
		
	/*	addMenuItem(R.string.txt_menu_events, R.drawable.lst_stadium,
				StadiumsActivity.class, args);
		addMenuItem(R.string.txt_menu_content, R.drawable.lst_news,
				NewsActivity.class, args);
		addMenuItem(R.string.txt_menu_hmevents, R.drawable.lst_medal,
				MedalsActivity.class, args);
		addMenuItem(R.string.txt_menu_photos, R.drawable.lst_photos,
				Visit_KeralaFragmentTab1.class, args);*/
		
		
		
		
//		MedalsTallyFragmentTab1.class, args);
		
//		addMenuItem(R.string.txt_menu_speeches, R.drawable.lst_speeches,
//				PredictTeamFragmentTab1.class, args);
		
//		addMenuItem(R.string.txt_menu_videos, R.drawable.lst_videos,
//				NewsActivity_old.class, args);
//		addMenuItem(R.string.txt_menu_media, R.drawable.lst_media_release,
//				NewsActivity_old.class, args);
//		addMenuItem(R.string.txt_menu_contact, R.drawable.lst_contact_us,
//				NewsActivity_old.class, args);
//		addMenuItem(R.string.txt_menu_subscribe, R.drawable.lst_subscribe,
//				NewsActivity_old.class, args);
//		addMenuItem(R.string.txt_menu_CheckList, R.drawable.ico_like,
//				CheckListActivity.class, args);

		// Assign the items to the list
		listView.setAdapter(new MenuAdapter(getActivity(), lstMenuItems));
		// Register item click listener
		listView.setOnItemClickListener(this);
	}

	@SuppressWarnings("rawtypes")
	private void addMenuItem(int labelID, int drawableId, Class cl, Bundle args) {
		MenuItem mnu = new MenuItem(labelID, drawableId, cl, args);
		lstMenuItems.add(mnu);
	}

	public void onItemClick(AdapterView<?> adp, View listview, int position,
			long id) {

		if (adp != null && adp.getAdapter() instanceof MenuAdapter) {
			MenuAdapter menuAdp = (MenuAdapter) adp.getAdapter();
			MenuItem itm = menuAdp.getItem(position);
			Fragment newContent = itm.get_fragment();

			if (newContent == null) {

//				newContent = Fragment.instantiate(getActivity(), itm
//						.get_class().getName(), itm.get_args());
//				itm.set_fragment(newContent);
			}

//			switchFragment(newContent);

		}
		
		open(listview);
		
		Intent intent;
		
		switch (position) {
		case 0:
			intent = new Intent(getActivity(), ScheduleActivity.class);
			startActivity(intent);
			break;
		case 1:
			intent = new Intent(getActivity(), GeoPlane.class);
			startActivity(intent);

			getActivity().finish();
			break;
		case 2:
			intent = new Intent(getActivity(), BadgesActivity.class);
			
			
//			Intent intent = new Intent(this,NewsActivity.class);
//			intent.putExtra("id", position);
			intent.putExtra("id", position);
//			intent.putExtra("title", strValues[position]);
//			intent.putStringArrayListExtra("val", _lstValues);
//			intent.putIntegerArrayListExtra("id", _lstId);
			startActivity(intent);
			getActivity().finish();
			break;
		case 3:
			intent = new Intent(getActivity(), NewsActivity.class);
			
			
//			Intent intent = new Intent(this,NewsActivity.class);
//			intent.putExtra("id", position);
			intent.putExtra("id", position);
//			intent.putExtra("title", strValues[position]);
//			intent.putStringArrayListExtra("val", _lstValues);
//			intent.putIntegerArrayListExtra("id", _lstId);
			startActivity(intent);
			getActivity().finish();
			break;
		
		case 4: 
			intent = new Intent(getActivity(), EmergencyContacts.class);
			startActivity(intent);
			getActivity().finish();
		default:
			break;
		}
/*Intent intent = new Intent(getActivity(), SubActivity.class);
		
		
//		Intent intent = new Intent(this,NewsActivity.class);
//		intent.putExtra("id", position);
		intent.putExtra("id", position);
//		intent.putExtra("title", strValues[position]);
//		intent.putStringArrayListExtra("val", _lstValues);
//		intent.putIntegerArrayListExtra("id", _lstId);
		startActivity(intent);
*/		
		
		
		
	}

	/*// the meat of switching the above fragment
	private void switchFragment(Fragment fragment) {
		if (getActivity() == null)
			return;

		if (getActivity() instanceof SubActivity) {
			SubActivity mActivity = (SubActivity) getActivity();
			mActivity.switchContent(fragment);
		}
	}*/
	 public void open(View view){
//	      progress.setMessage("Loading...");
//	      progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//	      progress.setIndeterminate(true);
//	      progress.setCancelable(false);
		 progress=ProgressDialog.show(getActivity(), "", "");
		 progress.setContentView(R.layout.progress);
	      progress.show();
//			mProgressBarLoadMore.setVisibility(View.VISIBLE);
	   final int totalProgressTime = 100;

	   final Thread t = new Thread(){

	   @Override
	   public void run(){
	 
	      int jumpTime = 0;
	      while(jumpTime < totalProgressTime){
	         try {
	            sleep(200);
	            jumpTime += 5;
	            progress.setProgress(jumpTime);
	         } catch (InterruptedException e) {
	           // TODO Auto-generated catch block
	           e.printStackTrace();
	         }
finally{
	
	progress.dismiss();
//	mProgressBarLoadMore.setVisibility(View.GONE);
}
	      }

	   }
	   };
	   t.start();

	   }
}
