package com.ilp.ilpschedule;

import android.app.Activity;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TextView;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import java.util.ArrayList;

public class NewsActivity extends Activity implements OnClickListener,
		OnItemClickListener {// ,OnTabChangeListener
								// {//,TabHost.OnTabChangeListener
	

	private SlidingMenu menu;

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.activity_news);
		LinearLayout theLayout = (LinearLayout) findViewById(R.id.news);
		Utils.setFontAllView((ViewGroup) theLayout);
		// vw_layout = inflater.inflate(R.layout.activity_news, container,
		// false);

		this.vw_master = (View) theLayout.findViewById(R.id.master);

		this.vw_header = (View) theLayout.findViewById(R.id.main_header);

		// get list view
		// listView = (ListView) this.vw_layout.findViewById(R.id.lst_videos);
		listView = (ListView) this.vw_master.findViewById(R.id.lst_videos);
		this.vw_master.setVisibility(View.VISIBLE);

		final NewsList n = new NewsList(this);
		
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
				
		// resultList= n.getNewsList();
		lstNews = n.getNewsList();
		// new loadMoreListView().execute();
		// lstNews=lstNewsload;
		// Log.d("Jeeettt Sizeee----old", String.valueOf(lstNewsload.size()));
		// lstNews.addAll(lstNewsload);
		// Log.d("Jeeettt Sizeee----old", String.valueOf(lstNews.size()));
		// Log.d("Jeeet",lstNews.get(0).toString());
		listView.setAdapter(new NewsAdapter(this, lstNews, this));
		// ((LoadMoreListView)listView).setOnItemClickListener(this);
		// listView.setOnItemClickListener(this);

		tvPrice = (TextView) this.vw_header.findViewById(R.id.text_price);
		tvPrice.setText(_title);
		btnBack = (ImageButton) this.vw_header.findViewById(R.id.btn_list);
		btnBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (menu.isMenuShowing()) {
					menu.showContent();
				} else {
					menu.showMenu();
				}
			}
		});
	}

	// footer view
	private RelativeLayout mFooterView;
	// private TextView mLabLoadMore;
	private ProgressBar mProgressBarLoadMore;
	private LayoutInflater mInflater;
	String serverURL_news = AppConstant.URL + "news_json.php";
	NewsAdapter adapter;
	int current_page = 1;
	// ArrayList<NewsItem> resultList;

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
	// private SlidingMenu menu;
	// //++++++++++++++++++++++++++++++++++++++++
	private static final String TAG = "FragmentTabs";
	public static final String TAB_WORDS = "news";
	public static final String TAB_NUMBERS = "photos";

	// View mRoot;
	TabHost mTabHost;
	int mCurrentTab;
	// //++++++++++++++++++++++++++++++++++++++++

	// detail view
	TextView tvTitle, tvPrice, tvDesc;
	ImageView img;
	ImageButton btnBack, btnLike, btnLove, btnList;

	// animation
	private Animation mSlideInLeft;
	private Animation mSlideOutRight;
	private Animation mSlideInRight;
	private Animation mSlideOutLeft;

	boolean _isBack = true;

	CellAnimationOut cellAnimationOutListener = new CellAnimationOut();
	CellAnimationIn cellAnimationInListener = new CellAnimationIn();
	private Animation mCellSlideInRight;
	private Animation mCellSlideOutRight;

	// +++++++++++++++
	/*
	 * @Override public void onAttach(Activity activity) {
	 * super.onAttach(activity); }
	 */
	// +++++++++++++++
	String _title = "Notifications";

	/*
	 * public void switchContent(Fragment fragment) { // mContent = fragment;
	 * getFragmentManager().beginTransaction() .replace(R.id.content_frame_news,
	 * fragment).commit(); menu.showContent(); }
	 */

	/*
	 * private void initAnimation() { // animation mSlideInLeft =
	 * AnimationUtils.loadAnimation(getActivity(), R.anim.push_left_in);
	 * mSlideOutRight = AnimationUtils.loadAnimation(getActivity(),
	 * R.anim.push_right_out); mSlideInRight =
	 * AnimationUtils.loadAnimation(getActivity(), R.anim.push_right_in);
	 * mSlideOutLeft = AnimationUtils.loadAnimation(getActivity(),
	 * R.anim.push_left_out); }
	 */

	@Override
	public void onItemClick(AdapterView<?> adp, View listview, int position,
			long id) {

		// this._isBack = false;
		/*
		 * NewsItem itm1=lstNews.get(position); // Toast.makeText(getActivity(),
		 * String.valueOf(itm1.get_id()),Toast.LENGTH_LONG).show(); Fragment
		 * fragment=new FragmentTab1(getActivity(),itm1); FragmentManager
		 * fragmentManager = getFragmentManager(); FragmentTransaction
		 * ft=fragmentManager.beginTransaction();
		 * ft.replace(R.id.content_frame_news, fragment); //
		 * ft.hide(NewsActivity.this); //
		 * ft.addToBackStack(NewsActivity.class.getName());
		 * ft.addToBackStack(null); ft.commit();
		 */

		// showView(this._isBack);
		// updateTab(TAB_WORDS, R.id.tab_1);
		if (adp != null && adp.getAdapter() instanceof NewsAdapter) {
			NewsAdapter newsAdp = (NewsAdapter) adp.getAdapter();
			NewsItem itm = newsAdp.getItem(position);
			itm.set_selected(!itm.get_selected());
			// updateTab(TAB_WORDS, R.id.tab_1);
			// tvTitle.setText(itm.get_title());
			// tvPrice.setText(">" + itm.get_date());
			// tvDesc.setText(Html.fromHtml(itm.get_content()));
			// ////
			// ImageLoader load=new ImageLoader(getActivity());
			// load.DisplayImage(itm.get_bigimage(), img);
			//
			// FragmentManager fm1 = getFragmentManager();
			// fm1.beginTransaction()
			// .replace(R.id.content_frame_news, new FragmentTab1(itm),"news")
			// .commit();

			// Toast.makeText(getActivity(),
			// String.valueOf(itm.get_id()),Toast.LENGTH_LONG).show();

			// FragmentManager fm2 = getFragmentManager();

			// fm2.beginTransaction()
			// .add(new FragmentTab2(), "photos")
			// // Add this transaction to the back stack
			// .addToBackStack(null)
			// .commit();

			// FragmentTransaction fragmentTransaction = fm2.beginTransaction();
			// fragmentTransaction.replace(R.id.tab_2, new
			// FragmentTab2(getActivity(),itm), "photos");
			// fragmentTransaction.addToBackStack(null);
			// fragmentTransaction.commit();
			// fm2.beginTransaction()
			// .replace(R.id.tab_2, new FragmentTab2(getActivity(),itm),
			// "photos")
			// .commit();

			// updateTab(TAB_WORDS, R.id.tab_1);
			// Bitmap bmp = Utils.GetImageFromAssets(getActivity(), "images/"
			// + itm.get_image());
			// img.setImageBitmap(bmp);

			// Toast.makeText(getActivity(), itm.get_title() +
			// " have been clicked", Toast.LENGTH_SHORT).show();
		}
	}

	private void showView(boolean isBack) {
		try {

			if (isBack) {
				this.vw_master.setVisibility(View.VISIBLE);
				this.vw_detail.setVisibility(View.GONE);
				this.vw_detail.startAnimation(mSlideOutRight);
				this.vw_master.startAnimation(mSlideInLeft);
			} else {
				this.vw_master.setVisibility(View.GONE);
				this.vw_detail.setVisibility(View.GONE);

				this.vw_master.startAnimation(mSlideOutLeft);
				this.vw_detail.startAnimation(mSlideInRight);

			}

		} catch (Exception e) {

		}
	}

	// public void onBackPressed() {
	// if (!this._isBack) {
	// this._isBack = !this._isBack;
	// showView(this._isBack);
	// return;
	// }
	// if (menu.isMenuShowing()) {
	// menu.showContent();
	// } else {
	// super.onBackPressed();
	// }
	// }

	/*
	 * public void onBackPressed() { if (menu.isMenuShowing()) {
	 * menu.showContent(); } else { // getActivity().onBackPressed(); } }
	 */
	@Override
	public void onClick(View v) {
		// onBackPressed();

		/*
		 * if (v.getId() == R.id.btn_list) { if (menu.isMenuShowing()) {
		 * menu.showContent(); } else { menu.showMenu(); } }
		 */
		// if (v.getId() == R.id.btn_love) {
		// Integer pos = (Integer) v.getTag();
		// Toast.makeText(getActivity(),
		// "Love button clicked at position " + pos,
		// Toast.LENGTH_SHORT).show();
		// }
		//
		// if (v.getId() == R.id.btn_like) {
		// Integer pos = (Integer) v.getTag();
		// Toast.makeText(getActivity(),
		// "Like button clicked at position " + pos,
		// Toast.LENGTH_SHORT).show();
		//
		// }
		//
		// if (v.getId() == R.id.btn_reload) {
		// Integer pos = (Integer) v.getTag();
		// Toast.makeText(getActivity(),
		// "Reload button clicked at position " + pos,
		// Toast.LENGTH_SHORT).show();
		//
		// }

	}

	class MyGestureDetector extends SimpleOnGestureListener {

		int lastIndex = -1;

		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
				float velocityY) {
			try {

				if (Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF_PATH)
					return false;

				if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE
						&& Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
					// right to left swipe
				} else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE
						&& Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {

					int index = (int) listView.pointToPosition((int) e1.getX(),
							(int) e1.getY());
					int currentIndex = index
							- listView.getFirstVisiblePosition()
							- listView.getHeaderViewsCount();

					lastIndex = lastIndex - listView.getFirstVisiblePosition()
							- listView.getHeaderViewsCount();
					if (lastIndex < 0 || lastIndex >= listView.getChildCount())
						cellAnimationOutListener.setPreviousView(null);

					View _v = listView.getChildAt(currentIndex);

					View vwLayer1 = _v.findViewById(R.id.view_layer1);
					View vwLayer2 = _v.findViewById(R.id.view_layer2);

					cellAnimationOutListener.setCurrentView(_v);
					vwLayer1.startAnimation(mCellSlideOutRight);

					int height = vwLayer1.getHeight();
					int width = vwLayer1.getWidth();
					vwLayer2.setLayoutParams(new RelativeLayout.LayoutParams(
							width, height));
					vwLayer2.setVisibility(View.VISIBLE);

					lastIndex = index;

				}
			} catch (Exception e) {
				// nothing
			}
			return false;
		}

	}

	public class CellAnimationOut implements Animation.AnimationListener {
		private View previousView;
		private View currentView;

		public CellAnimationOut() {

		}

		public View getPreviousView() {
			return previousView;
		}

		public void setPreviousView(View previousView) {
			this.previousView = previousView;
		}

		public View getCurrentView() {
			return currentView;
		}

		public void setCurrentView(View currentView) {
			this.currentView = currentView;
		}

		@Override
		public void onAnimationStart(Animation arg0) {

			if (previousView != null) {
				View layer1 = previousView.findViewById(R.id.view_layer1);
				cellAnimationInListener.setCurrentView(previousView);
				layer1.startAnimation(mCellSlideInRight);
				previousView = null;

			}

			if (currentView != null) {
				View layer2 = currentView.findViewById(R.id.view_layer2);
				layer2.setVisibility(View.VISIBLE);
			}
		}

		@Override
		public void onAnimationRepeat(Animation arg0) {
		}

		@Override
		public void onAnimationEnd(Animation arg0) {

			if (currentView != null) {
				View textView = currentView.findViewById(R.id.view_layer1);
				textView.setVisibility(View.GONE);
				previousView = currentView;
			}
		}
	}

	public class CellAnimationIn implements Animation.AnimationListener {

		private View currentView;

		public CellAnimationIn() {

		}

		public View getCurrentView() {
			return currentView;
		}

		public void setCurrentView(View currentView) {
			this.currentView = currentView;
		}

		@Override
		public void onAnimationStart(Animation arg0) {

		}

		@Override
		public void onAnimationRepeat(Animation arg0) {
		}

		@Override
		public void onAnimationEnd(Animation arg0) {
			if (currentView != null) {
				View layer1 = currentView.findViewById(R.id.view_layer1);
				View layer2 = currentView.findViewById(R.id.view_layer2);

				layer1.setVisibility(View.VISIBLE);
				layer2.setVisibility(View.INVISIBLE);

			}

		}
	}

	// @Override
	// protected void onCreate(Bundle savedInstanceState) {
	// super.onCreate(savedInstanceState);
	// setContentView(R.layout.activity_news);
	// }
	//
	// @Override
	// public boolean onCreateOptionsMenu(Menu menu) {
	// // Inflate the menu; this adds items to the action bar if it is present.
	// getMenuInflater().inflate(R.menu.news, menu);
	// return true;
	// }

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		// if (id == R.id.action_settings) {
		// return true;
		// }
		return super.onOptionsItemSelected(item);
	}

	/**
	 * Async Task that send a request to url Gets new list view data Appends to
	 * list view
	 * */
	/*
	 * private class loadMoreListView extends AsyncTask<Void, Void, Void> {
	 * private final HttpClient Client = new DefaultHttpClient(); private String
	 * Content; private String Error = null;
	 * 
	 * // private ProgressDialog Dialog = new ProgressDialog(__context);
	 * 
	 * String data =""; // TextView uiUpdate = (TextView)
	 * findViewById(R.id.output); // TextView jsonParsed = (TextView)
	 * findViewById(R.id.jsonParsed); int sizeData = 0;
	 * 
	 * 
	 * @Override protected void onPreExecute() {
	 * 
	 * mProgressBarLoadMore.setVisibility(View.VISIBLE); // Showing progress
	 * dialog before sending http request // pDialog = new ProgressDialog( //
	 * AndroidListViewWithLoadMoreButtonActivity.this); //
	 * pDialog.setMessage("Please wait.."); // pDialog.setIndeterminate(true);
	 * // pDialog.setCancelable(false); // pDialog.show(); }
	 * 
	 * protected Void doInBackground(Void... unused) {
	 *//************ Make Post Call To Web Server ***********/
	/*
	 * 
	 * // Simulates a background task try { Thread.sleep(1000); } catch
	 * (InterruptedException e) { } // getActivity().runOnUiThread(new
	 * Runnable() { // public void run() { // increment current page //
	 * current_page += 1;
	 * 
	 * // Next page request // URL =
	 * "http://api.androidhive.info/list_paging/?page=" + current_page;
	 * serverURL_news = AppConstant.URL+"news_json.php//?page=" + current_page;
	 * BufferedReader reader=null; try { // Thread.sleep(1000); // // Defined
	 * URL where to send data URL url = new URL(serverURL_news); // // // Send
	 * POST data request // URLConnection conn = url.openConnection(); //
	 * conn.setDoOutput(true); // OutputStreamWriter wr = new
	 * OutputStreamWriter(conn.getOutputStream()); // wr.write( data ); //
	 * wr.flush(); // // Get the server response
	 * 
	 * reader = new BufferedReader(new
	 * InputStreamReader(conn.getInputStream())); StringBuilder sb = new
	 * StringBuilder(); String line = null;
	 * 
	 * // Read Server Response while((line = reader.readLine()) != null) { //
	 * Append server response in string sb.append(line + "\n"); }
	 * 
	 * // Append Server Response To Content String Content = sb.toString(); //
	 * Log.d("Contentttttt",Content); } catch(Exception ex) { Error =
	 * ex.getMessage(); } finally { try {
	 * 
	 * reader.close(); }
	 * 
	 * catch(Exception ex) {} }
	 *//*****************************************************/
	/*
	 * 
	 * 
	 * // xml = parser.getXmlFromUrl(URL); // getting XML // doc =
	 * parser.getDomElement(xml); // getting DOM element
	 * 
	 * // NodeList nl = doc.getElementsByTagName(KEY_ITEM); // looping through
	 * all item nodes <item> // for (int i = 0; i < nl.getLength(); i++) { // //
	 * creating new HashMap // HashMap<String, String> map = new HashMap<String,
	 * String>(); // Element e = (Element) nl.item(i); // // // adding each
	 * child node to HashMap key => value // map.put(KEY_ID, parser.getValue(e,
	 * KEY_ID)); // map.put(KEY_NAME, parser.getValue(e, KEY_NAME)); // // //
	 * adding HashList to ArrayList // menuItems.add(map); // }
	 * 
	 * // get listview current position - used to maintain scroll position //
	 * int currentPosition = lv.getFirstVisiblePosition(); // int
	 * currentPosition = listView.getFirstVisiblePosition(); // Appending new
	 * data to menuItems ArrayList // adapter = new ListViewAdapter( //
	 * AndroidListViewWithLoadMoreButtonActivity.this, // menuItems); //
	 * lv.setAdapter(adapter); // // // Setting new scroll position //
	 * lv.setSelectionFromTop(currentPosition + 1, 0); //
	 * listView.setSelectionFromTop(currentPosition + 1, 0);
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * // } // });
	 * 
	 * return (null); }
	 * 
	 * 
	 * protected void onPostExecute(Void unused) { // closing progress dialog //
	 * pDialog.dismiss(); mProgressBarLoadMore.setVisibility(View.GONE); // We
	 * need notify the adapter that the data have been changed // ((BaseAdapter)
	 * getListAdapter()).notifyDataSetChanged();
	 * 
	 * //Jeeeeeeeeeeeeeeeeeeeeeeeeeeeeeetttttttttttttttt
	 * 
	 * JSONObject jsonResponse;
	 * 
	 * try { // Log.d("RESPONSE----", Content);
	 *//******
	 * Creates a new JSONObject with name/value mappings from the JSON string.
	 ********/
	/*
	 * jsonResponse = new JSONObject(Content); // Log.d("RESPONSE----",
	 * jsonResponse.toString());
	 *//***** Returns the value mapped by name if it exists and is a JSONArray. ***/
	/*
                   *//******* Returns null otherwise. *******/
	/*
	 * JSONArray jsonMainNode = jsonResponse.optJSONArray("Android");
	 *//*********** Process each JSON Node ************/
	/*
	 * 
	 * int lengthJsonArr = jsonMainNode.length(); // String[] mStrings={}; //
	 * String[] news_date_arr = new String[lengthJsonArr]; // String[]
	 * news_time_arr = new String[lengthJsonArr]; // String[] news_title_arr =
	 * new String[lengthJsonArr]; // String[] news_desc_arr = new
	 * String[lengthJsonArr]; // String[] mStrings = new String[lengthJsonArr];
	 * // String[] news_bigimg_arr = new String[lengthJsonArr];
	 * 
	 * 
	 * // news_date_arr = new String[lengthJsonArr]; // news_time_arr = new
	 * String[lengthJsonArr]; // news_title_arr = new String[lengthJsonArr]; //
	 * news_desc_arr = new String[lengthJsonArr]; // mStrings = new
	 * String[lengthJsonArr]; // news_bigimg_arr = new String[lengthJsonArr];
	 * 
	 * 
	 * // ArrayList<Scores_Category> categoriesList=new
	 * ArrayList<Scores_Category>();
	 * 
	 * for(int i=0; i < lengthJsonArr; i++) {
	 *//****** Get Object for each JSON node. ***********/
	/*
	 * JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
	 *//******* Fetch node values **********/
	/*
	 * // String news_date = jsonChildNode.optString("news_date").toString(); //
	 * String news_time = jsonChildNode.optString("news_time").toString(); //
	 * String news_title = jsonChildNode.optString("news_title").toString(); //
	 * String news_desc = jsonChildNode.optString("news_content").toString();
	 * String news_img = jsonChildNode.optString("nimg_image").toString();
	 * String news_bigimg = jsonChildNode.optString("nimg_bigimg").toString();
	 * int news_id = jsonChildNode.optInt("news_id"); String news_date =
	 * jsonChildNode.optString("news_date").toString(); String news_time =
	 * jsonChildNode.optString("news_time").toString(); String news_title =
	 * jsonChildNode.optString("news_title").toString(); String news_desc =
	 * jsonChildNode.optString("news_desc").toString(); String news_content =
	 * jsonChildNode.optString("news_content").toString();
	 * news_img=AppConstant.URLimg+news_img;
	 * news_bigimg=AppConstant.URLimg+news_bigimg; //
	 * mStrings[i]="http://10.0.2.2/fmard/"+news_qrimg; //
	 * news_bigimg_arr[i]="http://10.0.2.2/fmard/"+news_bigimg; // OutputData +=
	 * " Name 		    : "+ news_title +" \n " // + "Number 		: "+ news_date
	 * +" \n " // + "Time 				: "+ news_time +" \n " // + "DESC 				: "+
	 * news_desc +" \n " // + "Image 				: "+ news_qrimg +" \n " // +
	 * "Image 				: "+ mStrings[i] +" \n " //
	 * +"--------------------------------------------------\n";
	 * 
	 * //Log.i("JSON parse", song_name);
	 * 
	 * //#######################################################
	 * 
	 * // JSONObject catObj = (JSONObject) jsonMainNode.get(i); //
	 * Scores_Category cat = new
	 * Scores_Category(catObj.getInt("news_id"),catObj.getString("news_title"));
	 * // categoriesList.add(cat); // public NewsItem(int id, String date,
	 * String title, String image,String bigimage, String desc,String
	 * content,Boolean selected) NewsItem itm=new
	 * NewsItem(news_id,news_date,news_title
	 * ,news_img,news_bigimg,news_desc,news_content,false); lstNews.add(itm);
	 * //###################################################### Log.d("Jeeeeet",
	 * String.valueOf(lstNews.size()));
	 * 
	 * 
	 * }
	 * 
	 * 
	 * 
	 * // Toast.makeText(News.this, news_desc_arr[0].toString(),
	 * Toast.LENGTH_LONG).show();
	 *//****************** End Parse Response JSON Data *************/
	/*
	 * 
	 * //Show Parsed Output on screen (activity) // jsonParsed.setText(
	 * OutputData );
	 * 
	 * 
	 * 
	 * // Create custom adapter for listview // adapter = new
	 * LazyImageLoadAdapter(News.this,
	 * mStrings,news_date_arr,news_time_arr,news_title_arr,news_desc_arr);
	 * 
	 * // Set adapter to listview // list.setAdapter(adapter);
	 * 
	 * 
	 * 
	 * // -------------------------Fmard-----------------Start // Intent i=new
	 * Intent(News.this,News.class); // i.putExtra("news_date", news_date_arr);
	 * // i.putExtra("news_time", news_time_arr); // i.putExtra("news_title",
	 * news_title_arr); // i.putExtra("news_desc", news_desc_arr); //
	 * i.putExtra("img_urls", mStrings); //
	 * i.putExtra("bigimg_urls",news_bigimg_arr); // // //
	 * i.putExtra("categories", categoriesList); // // startActivity(i); //
	 * -------------------------Fmard--------------End--- int currentPosition =
	 * listView.getFirstVisiblePosition(); adapter=new
	 * NewsAdapter(getActivity(),lstNews); listView.setAdapter(adapter);
	 * listView.setSelectionFromTop(currentPosition + 1, 0);
	 * 
	 * ((BaseAdapter)adapter).notifyDataSetChanged(); // Call onLoadMoreComplete
	 * when the LoadMore task, has finished ((LoadMoreListView)
	 * listView).onLoadMoreComplete(); } catch (JSONException e) {
	 * e.printStackTrace(); // Toast.makeText(News.this, e.toString(),
	 * Toast.LENGTH_LONG).show(); }
	 * 
	 * 
	 * 
	 * 
	 * 
	 * //%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
	 * 
	 * 
	 * //Jeeeeeeeeeeeeeeeeeeeeeeeetttttttttttttttttt
	 * 
	 * 
	 * 
	 * 
	 * 
	 * //%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% // NOTE: You can call UI
	 * Element here.
	 * 
	 * // Close progress dialog // Dialog.dismiss();
	 * 
	 * if (Error != null) {
	 * 
	 * // uiUpdate.setText("Output : "+Error); // Toast.makeText(News.this,
	 * "Error"+Error, Toast.LENGTH_LONG).show();
	 * 
	 * } else { } // Show Response Json On Screen (activity) //
	 * uiUpdate.setText( Content );
	 *//****************** Start Parse Response JSON Data *************/
	/*
	 * 
	 * String OutputData = "";
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * }
	 * 
	 * @Override protected void onCancelled() { // Notify the loading more
	 * operation has finished ((LoadMoreListView)
	 * listView).onLoadMoreComplete(); } }
	 */

}
