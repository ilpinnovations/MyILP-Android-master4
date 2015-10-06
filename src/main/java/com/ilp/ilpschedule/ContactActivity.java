package com.ilp.ilpschedule;

import android.app.Activity;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class ContactActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_contact);
		
		LinearLayout theLayout = (LinearLayout) findViewById(R.id.contact);
		Utils.setFontAllView((ViewGroup) theLayout);
		
		this.vw_master = (View) theLayout.findViewById(R.id.master);

		this.vw_header= (View) theLayout.findViewById(R.id.main_header);
	

		// get list view
//		listView = (ListView) this.vw_layout.findViewById(R.id.lst_videos);
		listView = (ListView) this.vw_master.findViewById(R.id.lst_videos);
		this.vw_master.setVisibility(View.VISIBLE);

final NewsList n=new NewsList(this);
//resultList= n.getNewsList();
lstNews=n.getNewsList();
//			 new loadMoreListView().execute();
//lstNews=lstNewsload;
//Log.d("Jeeettt Sizeee----old", String.valueOf(lstNewsload.size()));
//lstNews.addAll(lstNewsload);
//Log.d("Jeeettt Sizeee----old", String.valueOf(lstNews.size()));
//Log.d("Jeeet",lstNews.get(0).toString());
/////////////////listView.setAdapter(new NewsAdapter(this, lstNews, this));
//((LoadMoreListView)listView).setOnItemClickListener(this);
//listView.setOnItemClickListener(this);
	
tvPrice = (TextView) this.vw_header.findViewById(R.id.text_price);
tvPrice.setText(_title);
btnBack = (ImageButton) this.vw_header.findViewById(R.id.btn_list);
	}

	



private static final int SWIPE_MIN_DISTANCE = 100;
private static final int SWIPE_MAX_OFF_PATH = 250;
private static final int SWIPE_THRESHOLD_VELOCITY = 70;
private GestureDetector gestureDetector;

ListView listView;
ArrayList<NewsItem> lstNews;
ArrayList<NewsItem> lstNewsload;
View vw_layout;

View vw_master;
View vw_detail;
View vw_header;
View vw_footer;

TextView tvTitle, tvPrice, tvDesc;
ImageButton btnBack, btnLike, btnLove,btnList;

String _title="Emergency Contacts";
}
