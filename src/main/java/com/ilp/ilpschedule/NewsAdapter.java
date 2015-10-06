package com.ilp.ilpschedule;

import android.app.Activity;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;
//import com.costum.android.widget.PullAndLoadListView.OnLoadMoreListener;
//import com.costum.android.widget.PullToRefreshListView.OnRefreshListener;

public class NewsAdapter extends ArrayAdapter<NewsItem> {

	private List<NewsItem> _list;
	private final Activity _context;
	private static LayoutInflater _inflater = null;
	private OnClickListener _listener = null;
//    private OnLoadMoreListener _onLoadlistener;
//    private OnRefreshListener _onRefreshlistener;
    
    
    public NewsAdapter(Activity context, List<NewsItem> lst
			) {
		super(context, R.layout.list_row, lst);
		this._context = context;
		_list = lst;
	

		_inflater = this._context.getLayoutInflater();
	}
	public NewsAdapter(Activity context, List<NewsItem> lst,
			OnClickListener listener) {
		super(context, R.layout.list_row, lst);
		this._context = context;
		_list = lst;
		_listener = listener;

		_inflater = this._context.getLayoutInflater();
	}
//	public NewsAdapter(Activity context, List<NewsItem> lst,
//			OnLoadMoreListener listener) {
//		super(context, R.layout.list_row, lst);
//		this._context = context;
//		_list = lst;
//		_onLoadlistener = listener;
//
//		_inflater = this._context.getLayoutInflater();
//	}
//	public NewsAdapter(Activity context, List<NewsItem> lst,
//			OnRefreshListener listener) {
//		super(context, R.layout.list_row, lst);
//		this._context = context;
//		_list = lst;
//		_onRefreshlistener = listener;
//
//		_inflater = this._context.getLayoutInflater();
//	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = convertView;
		if (convertView == null) {
			view = _inflater.inflate(R.layout.list_row, null);
		}

//		Utils.setFontAllView(parent);

		NewsItem newItem = _list.get(position);
		Integer id = newItem.get_id();

		View layer1 = view.findViewById(R.id.view_layer1);
//		View layer2 = view.findViewById(R.id.view_layer2);

		TextView tvTitle = (TextView) view.findViewById(R.id.text_name);
//		TextView tvprice = (TextView) view.findViewById(R.id.text_price);
//		TextView tvDesc = (TextView) view.findViewById(R.id.text_desc);
//		ImageView iv = (ImageView) view.findViewById(R.id.image);

//		   TextViewEx txtViewEx1 = (TextViewEx) view.findViewById(R.id.text_name);
//	        txtViewEx1.setText(newItem.get_title(), true); // true: enables justification
		
		
		view.setId(id);
		tvTitle.setText(newItem.get_title());
		tvTitle.setMovementMethod(new ScrollingMovementMethod());
//		tvprice.setText(newItem.get_date());
//		tvDesc.setText(newItem.get_desc());
         
		
//		ImageLoader load= new ImageLoader(_context);
//		load.DisplayImage(newItem.get_image(), iv);
//		Bitmap bmp = Utils.GetImageFromAssets(this._context, "images/"
//				+ newItem.get_image());
//		iv.setImageBitmap(bmp);

//		detailViewListener(position, view);

		layer1.setVisibility(View.VISIBLE);		
//		layer2.setVisibility(View.INVISIBLE);

		return view;
	}

//	public void detailViewListener(int pos, View convertView) {
//		if (this._listener == null)
//			return;
//
//		ImageButton btn;
//		btn = (ImageButton) convertView.findViewById(R.id.btn_love);
//		btn.setTag(pos);
//		btn.setOnClickListener(this._listener);
//
//		btn = (ImageButton) convertView.findViewById(R.id.btn_like);
//		btn.setTag(pos);
//		btn.setOnClickListener(this._listener);
//
//		btn = (ImageButton) convertView.findViewById(R.id.btn_reload);
//		btn.setTag(pos);
//		btn.setOnClickListener(this._listener);
//
//	}

}
