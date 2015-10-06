package com.ilp.ilpschedule.geo;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.ilp.ilpschedule.DatabaseHandler;
import com.ilp.ilpschedule.MenuFragment;
import com.ilp.ilpschedule.R;
import com.ilp.ilpschedule.util.AlertDialogManager;
import com.ilp.ilpschedule.util.ConnectionDetector;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class GeoPlane extends Activity implements  OnClickListener, LocationListener{
	// Google Map
	GoogleMap googleMap;
	private Button btnLoc1;
	private Button btnLoc2;
	private Button btnLoc3;
	private Button btnDirn;
	private Button btnClear;
	private ImageButton btnList;
	private LinearLayout extraLayout;
	private LinearLayout topLayout;
	private ImageView slideTxt;
	private Animation animUp;
	private Animation animDown;
	private int PROXIMITY_RADIUS_NEAR = 2000;
	private int PROXIMITY_RADIUS_FAR = 20000;
	//Google Server_API_Key
	/*private static final String GOOGLE_API_KEY = "AIzaSyD1VJVW1xj-KvWpe-kZAmEFAY7Sf2C36Ao";*/
	private static final String GOOGLE_API_KEY = "AIzaSyBianbp_fsoHniTb_56PHcHTzoDs6dvQkc";
	private List<MyPlace> locationData;
	private PlaceAdapter pAdapter;
	private LocationManager locationManager;
	private ProgressDialog progress;
	private LatLng touchedLocn;
	AlertDialogManager alert = new AlertDialogManager();
	private DatabaseHandler db;
	private String location;
	private SlidingMenu menu;
	private Location myLocation;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_geo_space);
		db = new DatabaseHandler(getApplicationContext());
		location = db.getLocation();
		//location="Chennai";
		//Toast.makeText(this,location,Toast.LENGTH_SHORT).show();
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

		ConnectionDetector cd = new ConnectionDetector(getApplicationContext());

		btnLoc1 = (Button) findViewById(R.id.btnLoc1);
		btnLoc2 = (Button) findViewById(R.id.btnLoc2);
		btnLoc3 = (Button) findViewById(R.id.btnLoc3);
		btnDirn = (Button) findViewById(R.id.btnDirn);
		btnClear = (Button) findViewById(R.id.btnClear);
		slideTxt = (ImageView) findViewById(R.id.slideTxt);
		extraLayout = (LinearLayout) findViewById(R.id.extraLayout);
		extraLayout.setVisibility(View.GONE);
		topLayout = (LinearLayout) findViewById(R.id.headerLayout);
		topLayout = (LinearLayout) findViewById(R.id.headerLayout);
		topLayout.setVisibility(View.GONE);
		Log.d("Message----->", "Getting hidden layout");
		Log.d("Message----->", "Hidden layout loaded successfully");
	    animUp = AnimationUtils.loadAnimation(this, R.drawable.anim_up);
	    animDown = AnimationUtils.loadAnimation(this, R.drawable.anim_down);


		locationData = new ArrayList<MyPlace>();
		locationData.add(new MyPlace(R.drawable.hospital, "Hospital",
				"Nearest Medical Care"));
		locationData.add(new MyPlace(R.drawable.restaurant, "Restaurant",
				"Eat and Dine"));
		locationData.add(new MyPlace(R.drawable.atm, "ATM", "ATM Locations"));
		locationData.add(new MyPlace(R.drawable.train, "Train Station", "Nearest railway stations"));
		locationData.add(new MyPlace(R.drawable.bank, "Bank", "Financial Centers"));
		locationData.add(new MyPlace(R.drawable.attraction, "Movie Theater", "Nearest Cinema Halls"));

		ListView placesList = new ListView(this);
		pAdapter = new PlaceAdapter();
		placesList.setAdapter(pAdapter);
		placesList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				String type = locationData.get(position).getHeadLine();
				extraLayout.startAnimation(animDown);
                extraLayout.setVisibility(View.GONE);
                slideTxt.setImageResource(R.drawable.up);
				searchPlaces(type.toLowerCase().replace(" ", "_"));
			}
		});
		extraLayout.addView(placesList);

		if (!isGooglePlayServicesAvailable()) {
            finish();
        }

		try {
			// Loading map
			initilizeMap();

			// Changing map type
			googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
			/*googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
			 googleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
			 googleMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
			 googleMap.setMapType(GoogleMap.MAP_TYPE_NONE);*/

			// Showing / hiding your current location
			googleMap.setMyLocationEnabled(true);

			// Enable / Disable zooming controls
			googleMap.getUiSettings().setZoomControlsEnabled(false);

			// Enable / Disable my location button
			googleMap.getUiSettings().setMyLocationButtonEnabled(true);

			// Enable / Disable Compass icon
			googleMap.getUiSettings().setCompassEnabled(true);

			// Enable / Disable Rotate gesture
			googleMap.getUiSettings().setRotateGesturesEnabled(true);

			// Enable / Disable zooming functionality
			googleMap.getUiSettings().setZoomGesturesEnabled(true);

			googleMap.setOnMyLocationChangeListener(myLocationChangeListener);


			initializeLocations();
			getMyLocation();

			if (!cd.isConnectingToInternet()) {
	            // Internet Connection is not present
				try{
					setMobileDataEnabled(GeoPlane.this, false);
				}catch(Exception e) {
		           Toast.makeText(GeoPlane.this, "Please connect to working internet connection",
		        		   Toast.LENGTH_SHORT).show();

				}
	        }

			googleMap.setOnMapClickListener(new OnMapClickListener() {

				public void onMapClick(LatLng latLng) {

					touchedLocn = latLng;
					MarkerOptions markerOptions = new MarkerOptions();
					markerOptions.position(latLng);
					googleMap.clear();
					initializeLocations();
					googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
					googleMap.addMarker(markerOptions);
					topLayout.setVisibility(View.VISIBLE);
					topLayout.bringToFront();
				}
			});

			googleMap.setOnMarkerClickListener(new OnMarkerClickListener() {

				@Override
				public boolean onMarkerClick(Marker marker) {
					touchedLocn = marker.getPosition();
					topLayout.setVisibility(View.VISIBLE);
					topLayout.bringToFront();
					return false;
				}
			});


			btnLoc1.setOnClickListener(this);
			btnLoc2.setOnClickListener(this);
			btnLoc3.setOnClickListener(this);
			btnDirn.setOnClickListener(this);
			btnClear.setOnClickListener(this);
			slideTxt.setOnClickListener(this);
			btnList.bringToFront();
			btnList.setOnClickListener(this);

		} catch (Exception e) {
			e.printStackTrace();
			finish();
		}

	}

    private void initializeLocations() {

    	googleMap.clear();
    	if(location.equals("Trivandrum"))
    	{
			//Toast.makeText(this,"welcome to tvm",Toast.LENGTH_LONG).show();
			LatLng loc1 = new LatLng(8.5525038,76.8800041);

			btnLoc1.setText("Peepul Park");
			btnLoc2.setText("CLC");
			googleMap.addMarker(new MarkerOptions()
				.position(loc1)
				.title("TCS Peepul Park")
				.icon(BitmapDescriptorFactory.fromResource(R.drawable.edu)));
			LatLng loc2 = new LatLng(8.555145, 76.880294);
			googleMap.addMarker(new MarkerOptions()
									.position(loc2)
									.title("TCS CLC Building")
									.icon(BitmapDescriptorFactory.fromResource(R.drawable.edu)));
			googleMap.moveCamera(CameraUpdateFactory.newLatLng(loc1));
            googleMap.animateCamera(CameraUpdateFactory.zoomTo(14));
    	}
    	else if(location.equals("Guwahati"))
    	{

			Toast.makeText(this,"welcome to guwahati"+location,Toast.LENGTH_LONG).show();

			LatLng loc1 = new LatLng(26.150799,91.790978);
    		btnLoc1.setText("Guwahati ILP");
    		googleMap.addMarker(new MarkerOptions()
				.position(loc1)
				.title("TCS Guwahati ILP Centre")
				.icon(BitmapDescriptorFactory.fromResource(R.drawable.edu)));
    		btnLoc2.setVisibility(View.GONE);
    		googleMap.moveCamera(CameraUpdateFactory.newLatLng(loc1));
            googleMap.animateCamera(CameraUpdateFactory.zoomTo(14));
    	}
    	else if(location.equals("Chennai"))
    	{
    		LatLng loc1 = new LatLng(13.096596, 80.165716);
    		btnLoc1.setText("Chennai ILP");
    		googleMap.addMarker(new MarkerOptions()
					.position(loc1)
					.title("TCS Chennai ILP Centre")
					.icon(BitmapDescriptorFactory.fromResource(R.drawable.edu)));
    		btnLoc2.setVisibility(View.GONE);
			btnLoc3.setVisibility(View.GONE);//no hostel in chennai
    		googleMap.moveCamera(CameraUpdateFactory.newLatLng(loc1));
            googleMap.animateCamera(CameraUpdateFactory.zoomTo(14));
    	}
    	else if(location.equals("Hyderabad"))
    	{
    		LatLng loc1 = new LatLng(17.427160, 78.331661);
    		btnLoc1.setText("Hyderabad ILP");
    		googleMap.addMarker(new MarkerOptions()
					.position(loc1)
					.title("TCS Hyderabad ILP Centre")
					.icon(BitmapDescriptorFactory.fromResource(R.drawable.edu)));
    		btnLoc2.setVisibility(View.GONE);
			btnLoc3.setVisibility(View.GONE);
    		googleMap.moveCamera(CameraUpdateFactory.newLatLng(loc1));
            googleMap.animateCamera(CameraUpdateFactory.zoomTo(14));
    	}
		else if(location.equals("Ahmedabad"))
		{

			//Toast.makeText(this,"welcome to Ahmedabad"+location,Toast.LENGTH_LONG).show();

			LatLng loc1 = new LatLng(23.190825, 72.636473);
			btnLoc1.setText("Ahmedabad ILP");
			googleMap.addMarker(new MarkerOptions()
					.position(loc1)
					.title("TCS Ahmedabad ILP Centre")
					.icon(BitmapDescriptorFactory.fromResource(R.drawable.edu)));
			btnLoc2.setVisibility(View.GONE);
			btnLoc3.setVisibility(View.GONE);
			googleMap.moveCamera(CameraUpdateFactory.newLatLng(loc1));
			googleMap.animateCamera(CameraUpdateFactory.zoomTo(14));
		}
	}

	@Override
    public void
    onLocationChanged(Location location) {
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();
        LatLng latLng = new LatLng(latitude, longitude);
        googleMap.addMarker(new MarkerOptions().position(latLng));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(18));
    }

    public Location getMyLocation()
    {
    	//Toast.makeText(GeoPlane.this, "Getting Location", Toast.LENGTH_SHORT).show();
    	googleMap.setMyLocationEnabled(true);
		locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
		Criteria criteria = new Criteria();
		String bestProvider = locationManager.getBestProvider(criteria, false);
		Log.d("Provider", bestProvider);
        locationManager.requestLocationUpdates(bestProvider, 20000, 0, this);
		Location location = locationManager.getLastKnownLocation(bestProvider);
		if (location != null) {
        //	Toast.makeText(GeoPlane.this, "Setting location", Toast.LENGTH_SHORT).show();
        	double latitude = location.getLatitude();
            double longitude = location.getLongitude();
			//Toast.makeText(GeoPlane.this, "Setting location"+latitude, Toast.LENGTH_SHORT).show();
			//Toast.makeText(this, "mapaa not null", Toast.LENGTH_SHORT);
			initilizeMap();
			/*if(googleMap==null)
				Toast.makeText(this,"map null",Toast.LENGTH_SHORT).show();
			else
				Toast.makeText(this,"map not null",Toast.LENGTH_SHORT).show();
*/
			LatLng latLng = new LatLng(latitude, longitude);
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            googleMap.animateCamera(CameraUpdateFactory.zoomTo(15));
        }
		return location;
    }

	@Override
	protected void onResume() {
		super.onResume();
		LocationManager manager = (LocationManager) getSystemService(GeoPlane.this.LOCATION_SERVICE );
		boolean statusOfGPS = manager.isProviderEnabled(LocationManager.GPS_PROVIDER);
		if(!statusOfGPS)
		{	if(canToggleGPS())
			{
				turnGPSOn();
				initilizeMap();
			}
			else{
				alert.showAlertDialogWithResp(GeoPlane.this, "GPS is OFF", "Please Enable GPS",
						false, Settings.ACTION_LOCATION_SOURCE_SETTINGS);
				return;
			}
		}

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		turnGPSOff();
	}

	/*
	 function to load map If map is not created it will create it for you
	  */
	private void initilizeMap() {
		Toast.makeText(this,"loading map",Toast.LENGTH_LONG);
		System.out.print("loadujhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhh");
		if (googleMap == null) {
			googleMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
			Toast.makeText(this,"loading map",Toast.LENGTH_SHORT);
			// check if map is created successfully or not
			if (googleMap == null) {
				Toast.makeText(getApplicationContext(),
						"Sorry! unable to create maps", Toast.LENGTH_SHORT)
						.show();
			}
		}
	}

    private boolean isGooglePlayServicesAvailable() {
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (ConnectionResult.SUCCESS == status) {
            return true;
        } else {
            GooglePlayServicesUtil.getErrorDialog(status, this, 0).show();
            return false;
        }
    }

	@Override
	public void onClick(View v) {
		topLayout.setVisibility(View.GONE);
		initializeLocations();
		int id = v.getId();
		switch(id) {
			case R.id.btnLoc1:
				if(location.equals("Trivandrum"))
				{
					//Toast.makeText(this,"hello",Toast.LENGTH_SHORT).show();

					initializeLocations();
					LatLng loc1 = new LatLng(8.5525038,76.8800041);
					initilizeMap();
					//googleMap.addCircle();
			        googleMap.moveCamera(CameraUpdateFactory.newLatLng(loc1));
			        googleMap.animateCamera(CameraUpdateFactory.zoomTo(18));
				}
				else if(location.equals("Guwahati"))
				{
					initializeLocations();
					LatLng loc1 = new LatLng(26.150799,91.790978);
					//initilizeMap();
			        googleMap.moveCamera(CameraUpdateFactory.newLatLng(loc1));
			        googleMap.animateCamera(CameraUpdateFactory.zoomTo(18));
				}
				else if(location.equals("Chennai"))
				{
					initializeLocations();
					LatLng loc1 = new LatLng(13.096596, 80.165716);
					//initilizeMap();
			        googleMap.moveCamera(CameraUpdateFactory.newLatLng(loc1));
			        googleMap.animateCamera(CameraUpdateFactory.zoomTo(15));
				}
				else if(location.equals("Hyderabad"))
		    	{
					initializeLocations();
					LatLng loc1 = new LatLng(17.427160, 78.331661);
			        googleMap.moveCamera(CameraUpdateFactory.newLatLng(loc1));
			        googleMap.animateCamera(CameraUpdateFactory.zoomTo(15));
		    	}
				else if(location.equals("Ahmedabad"))
				{
					initializeLocations();
					LatLng loc1 = new LatLng(23.190825, 72.636473);
					googleMap.moveCamera(CameraUpdateFactory.newLatLng(loc1));
					googleMap.animateCamera(CameraUpdateFactory.zoomTo(15));
				}

		        break;

			case R.id.btnLoc2:										//giving random locations 4 testing

				if(location.equals("Trivandrum"))
				{
					LatLng loc2 = new LatLng(8.555145, 76.880294);
			        googleMap.moveCamera(CameraUpdateFactory.newLatLng(loc2));
			        googleMap.animateCamera(CameraUpdateFactory.zoomTo(18));
				}
		        break;

			case R.id.btnLoc3:
				if(location.equals("Trivandrum"))
				{
					LatLng hostel1 = new LatLng(8.553064, 76.877913);
					googleMap.addMarker(new MarkerOptions()
											.position(hostel1)
											.title("Executive Hostel TCS")
											.icon(BitmapDescriptorFactory.fromResource(R.drawable.hostel)));
			        googleMap.moveCamera(CameraUpdateFactory.newLatLng(hostel1));
			        LatLng hostel2 = new LatLng(8.551389, 76.879763);
					googleMap.addMarker(new MarkerOptions()
											.position(hostel2)
											.title("Peepul Park Hostel TCS")
											.icon(BitmapDescriptorFactory.fromResource(R.drawable.hostel)));
			        googleMap.moveCamera(CameraUpdateFactory.newLatLng(hostel2));

			        googleMap.animateCamera(CameraUpdateFactory.zoomTo(15));
				}
				/*else if(location.equals("Hyderabad"))
				{
					LatLng hostel1 = new LatLng(17.427217, 72.331542);
					googleMap.addMarker(new MarkerOptions()
							.position(hostel1)
							.title("Hyderabad Hostel TCS")
							.icon(BitmapDescriptorFactory.fromResource(R.drawable.hostel)));
					googleMap.moveCamera(CameraUpdateFactory.newLatLng(hostel1));
					LatLng hostel2 = new LatLng(17.429479, 78.325770);

					googleMap.addMarker(new MarkerOptions()
							.position(hostel2)
							.title("Hyderabad  Hostel2 TCS")
							.icon(BitmapDescriptorFactory.fromResource(R.drawable.hostel)));
					googleMap.moveCamera(CameraUpdateFactory.newLatLng(hostel2));

					LatLng hostel3 = new LatLng(17.446424, 78.376290);

					googleMap.addMarker(new MarkerOptions()
							.position(hostel3)
							.title("Hyderabad  Hostel3 TCS")
							.icon(BitmapDescriptorFactory.fromResource(R.drawable.hostel)));
					googleMap.moveCamera(CameraUpdateFactory.newLatLng(hostel3));


					googleMap.animateCamera(CameraUpdateFactory.zoomTo(15));
				}*/
				else if(location.equals("Guwahati"))
				{
					LatLng hostel1 = new LatLng(8.553064, 76.877913);
					googleMap.addMarker(new MarkerOptions()
							.position(hostel1)
							.title("Hyderabad Hostel TCS")
							.icon(BitmapDescriptorFactory.fromResource(R.drawable.hostel)));
					googleMap.moveCamera(CameraUpdateFactory.newLatLng(hostel1));
					LatLng hostel2 = new LatLng(8.551389, 76.879763);
					googleMap.addMarker(new MarkerOptions()
							.position(hostel2)
							.title("Hyderabad  Hostel2 TCS")
							.icon(BitmapDescriptorFactory.fromResource(R.drawable.hostel)));
					googleMap.moveCamera(CameraUpdateFactory.newLatLng(hostel2));

					googleMap.animateCamera(CameraUpdateFactory.zoomTo(15));
				}
				else if(location.equals("Ahmedabad"))
				{
					LatLng hostel1 = new LatLng(8.553064, 76.877913);
					googleMap.addMarker(new MarkerOptions()
							.position(hostel1)
							.title("Ahmedabad Hostel1 TCS")
							.icon(BitmapDescriptorFactory.fromResource(R.drawable.hostel)));
					googleMap.moveCamera(CameraUpdateFactory.newLatLng(hostel1));
					LatLng hostel2 = new LatLng(8.551389, 76.879763);
					googleMap.addMarker(new MarkerOptions()
							.position(hostel2)
							.title("Ahmedabad  Hostel2 TCS")
							.icon(BitmapDescriptorFactory.fromResource(R.drawable.hostel)));
					googleMap.moveCamera(CameraUpdateFactory.newLatLng(hostel2));

					googleMap.animateCamera(CameraUpdateFactory.zoomTo(15));
				}


				break;

			case R.id.slideTxt:
				if(extraLayout.getVisibility()==View.GONE)
				{
					extraLayout.setVisibility(View.VISIBLE);
	                extraLayout.startAnimation(animUp);
	                slideTxt.setImageResource(R.drawable.down);
				}
				else if(extraLayout.getVisibility()==View.VISIBLE)
				{
					extraLayout.startAnimation(animDown);
	                extraLayout.setVisibility(View.GONE);
	                slideTxt.setImageResource(R.drawable.up);
				}
				break;

			case R.id.btnDirn:
				if(touchedLocn!=null){
					String lat = String.valueOf(touchedLocn.latitude);
					String lng = String.valueOf(touchedLocn.longitude);
					Uri gmmIntentUri = Uri.parse("google.navigation:q="+lat+","+lng+"&mode=d");
					Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
					mapIntent.setPackage("com.google.android.apps.maps");
					startActivity(mapIntent);
					initializeLocations();
				}
				break;

			case R.id.btnClear:
				googleMap.clear();
				initializeLocations();
				break;

			case R.id.btn_list:
				if (menu.isMenuShowing()) {
					menu.showContent();
				} else {
					menu.showMenu();
				}
				break;
		}

	}

	public void searchPlaces(String type){

		Toast.makeText(GeoPlane.this, "Searching for "+type, Toast.LENGTH_SHORT).show();
		StringBuilder googlePlacesUrl = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
		googleMap.setMyLocationEnabled(true);
		LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

		Criteria criteria = new Criteria();
		String bestProvider = locationManager.getBestProvider(criteria, true);
		Location location = locationManager.getLastKnownLocation(bestProvider);
		int PROXIMITY_RADIUS = 0;
		int resId = -1;
		if(type.equals("hospital"))
		{
			resId = R.drawable.hospital;
			PROXIMITY_RADIUS = PROXIMITY_RADIUS_FAR;
		}
		else if(type.equals("restaurant"))
		{
			resId = R.drawable.restaurant;
			PROXIMITY_RADIUS = PROXIMITY_RADIUS_NEAR;
		}
		else if(type.equals("atm"))
		{
			resId = R.drawable.atm;
			PROXIMITY_RADIUS = PROXIMITY_RADIUS_NEAR;
		}
		else if(type.equals("train_station"))
		{
			resId = R.drawable.train;
			PROXIMITY_RADIUS = PROXIMITY_RADIUS_FAR;
		}
		else if(type.equals("movie_theater"))
		{
			resId = R.drawable.attraction;
			PROXIMITY_RADIUS = PROXIMITY_RADIUS_FAR;
		}
		else if(type.equals("bank"))
		{
			resId = R.drawable.bank;
			PROXIMITY_RADIUS = PROXIMITY_RADIUS_NEAR;;
		}
		if(location!=null) {
			double latitude = location.getLatitude();
			double longitude = location.getLongitude();
			googlePlacesUrl.append("location=" + latitude + "," + longitude);
			googlePlacesUrl.append("&radius=" + PROXIMITY_RADIUS);
			googlePlacesUrl.append("&types=" + type);
			googlePlacesUrl.append("&sensor=true");
			googlePlacesUrl.append("&key=" + GOOGLE_API_KEY);

			GooglePlacesReadTask googlePlacesReadTask = new GooglePlacesReadTask();
			Object[] toPass = new Object[4];
			toPass[0] = googleMap;
			toPass[1] = googlePlacesUrl.toString();
			toPass[2] = resId;
			toPass[3] = PROXIMITY_RADIUS;
			googlePlacesReadTask.execute(toPass);
		}

	}

	private class PlaceAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return locationData.size();
		}

		@Override
		public MyPlace getItem(int position) {
			return locationData.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View view, ViewGroup parent) {
			if(view==null)
			{
				view = View.inflate(getApplicationContext(), R.layout.geo_simplerow, null);
				new ViewHolder(view);
			}
			ViewHolder holder = (ViewHolder) view.getTag();
			MyPlace item = getItem(position);
			holder.image.setImageResource(item.getResId());
			holder.head.setText(item.getHeadLine());
			holder.description.setText(item.getDescription());
			return view;

		}

		class ViewHolder{
			ImageView image;
			TextView head;
			TextView description;

			public ViewHolder(View view) {
				image = (ImageView) view.findViewById(R.id.icon);
				head = (TextView) view.findViewById(R.id.firstLine);
				description = (TextView) view.findViewById(R.id.secondLine);
				view.setTag(this);
			}
		}

	}

	@Override
	public void onProviderDisabled(String arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onProviderEnabled(String arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
		// TODO Auto-generated method stub

	}

	class GooglePlacesReadTask extends AsyncTask<Object, Integer, String> {
	    String googlePlacesData = null;
	    GoogleMap googleMap;
	    int resId;
	    int PROXIMITY_RADIUS;

	    @Override
		protected void onPreExecute() {

			progress = ProgressDialog.show(
					GeoPlane.this, "", "");
			progress.setContentView(R.layout.progress);
			progress.show();

		}
	    @Override
	    protected String doInBackground(Object... inputObj) {
	        try {
	            googleMap = (GoogleMap) inputObj[0];
	            String googlePlacesUrl = (String) inputObj[1];
	            resId = (Integer) inputObj[2];
	            PROXIMITY_RADIUS = (Integer) inputObj[3];
	            Http http = new Http();
	            googlePlacesData = http.read(googlePlacesUrl);
	        } catch (Exception e) {
	            Log.d("Google Place Read Task", e.toString());
	        }
	        return googlePlacesData;
	    }

	    @Override
	    protected void onPostExecute(String result) {
	        PlacesDisplayTask placesDisplayTask = new PlacesDisplayTask();
	        Object[] toPass = new Object[4];
	        toPass[0] = googleMap;
	        toPass[1] = result;
	        toPass[2] = resId;
	        toPass[3] = PROXIMITY_RADIUS;
	        placesDisplayTask.execute(toPass);
	        progress.dismiss();
	        if(PROXIMITY_RADIUS==2000)
	        {
	        	initializeLocations();
	        	getMyLocation();
	        	googleMap.animateCamera(CameraUpdateFactory.zoomTo(15));
	        }
	        else if(PROXIMITY_RADIUS==20000)
	        {
	        	initializeLocations();
	        	getMyLocation();
	        	googleMap.animateCamera(CameraUpdateFactory.zoomTo(10));
	        }
	    }
	}

	private void turnGPSOn(){
	    String provider = Settings.Secure.getString(getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);

	    if(!provider.contains("gps")){ //if gps is disabled
	        final Intent poke = new Intent();
	        poke.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider");
	        poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
	        poke.setData(Uri.parse("3"));
	        sendBroadcast(poke);
	    }
	}

	private void turnGPSOff(){
	    String provider = Settings.Secure.getString(getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);

	    if(provider.contains("gps")){ //if gps is enabled
	        final Intent poke = new Intent();
	        poke.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider");
	        poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
	        poke.setData(Uri.parse("3"));
	        sendBroadcast(poke);
	    }
	}

	private boolean canToggleGPS() {
	    PackageManager pacman = getPackageManager();
	    PackageInfo pacInfo = null;

	    try {
	        pacInfo = pacman.getPackageInfo("com.android.settings", PackageManager.GET_RECEIVERS);
	    } catch (NameNotFoundException e) {
	        return false; //package not found
	    }

	    if(pacInfo != null){
	        for(ActivityInfo actInfo : pacInfo.receivers){
	            //test if recevier is exported. if so, we can toggle GPS.
	            if(actInfo.name.equals("com.android.settings.widget.SettingsAppWidgetProvider") && actInfo.exported){
	                return true;
	            }
	        }
	    }

	    return false; //default
	}

	private void setMobileDataEnabled(Context context, boolean enabled) throws Exception{

	    final ConnectivityManager conman = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
	    final Class<?> conmanClass = Class.forName(conman.getClass().getName());
	    final Field iConnectivityManagerField = conmanClass.getDeclaredField("mService");
	    iConnectivityManagerField.setAccessible(true);
	    final Object iConnectivityManager = iConnectivityManagerField.get(conman);
	    final Class<?> iConnectivityManagerClass = Class.forName(iConnectivityManager.getClass().getName());
	    final Method setMobileDataEnabledMethod = iConnectivityManagerClass.getDeclaredMethod("setMobileDataEnabled", Boolean.TYPE);
	    setMobileDataEnabledMethod.setAccessible(true);

	    setMobileDataEnabledMethod.invoke(iConnectivityManager, enabled);

	}

	private GoogleMap.OnMyLocationChangeListener myLocationChangeListener = new GoogleMap.OnMyLocationChangeListener() {

		@Override
		public void onMyLocationChange(Location location) {
			myLocation = location;
		}
	};



}
