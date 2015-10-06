package com.ilp.ilpschedule;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class ScheduleArrayAdapter_old extends ArrayAdapter<CatItem> {
	private final Context _context;
	private final String[] values;
	private final ArrayList<CatItem> list;
	private final Integer[] images;
	private final LayoutInflater inflater;


	
	public ScheduleArrayAdapter_old(Context context, String[] values,ArrayList<CatItem> list,
			Integer[] images) {
		super(context, R.layout.row_color_schedulelist, list);
		this._context = context;
		this.values = values;
		this.list=list;
		this.images = images;
		inflater = (LayoutInflater) this._context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public View getView(final int position, View convertView,final ViewGroup parent) {

		View view = convertView;

		if (convertView == null) {
			view = inflater.inflate(R.layout.row_color_schedulelist, parent, false);

			view.setOnClickListener(new OnClickListener() {

			    @Override
			    public void onClick(View v) {

//			         Toast.makeText(parent.getContext(), "view clicked: " + list.get(position).get_slot(), Toast.LENGTH_SHORT).show();
			         Intent i= new Intent(parent.getContext(),FeedbackActivity.class);
						i.putExtra("slot",list.get(position).get_slot());
						i.putExtra("course",list.get(position).get_course());
						i.putExtra("faculty",list.get(position).get_faculty());
						i.putExtra("date",list.get(position).get_date());
						parent.getContext().startActivity(i);
			    }
			});

		}

		
		 		
		
		
		Utils.setFontAllView(parent);
		view.setId(position);

		TextView textSlot = (TextView) view.findViewById(R.id.text_slot);
		TextView textCourse = (TextView) view.findViewById(R.id.text_course);
		TextView textFaculty = (TextView) view.findViewById(R.id.text_faculty);
		TextView textRoom = (TextView) view.findViewById(R.id.text_room);
//		ImageView imageView = (ImageView) view.findViewById(R.id.image_color);
//		textView.setText(values[position]);
		
		if(list.size()>0){
		
		textSlot.setText(list.get(position).get_slot());
		textCourse.setText(list.get(position).get_course());
		textFaculty.setText(list.get(position).get_faculty());
		textRoom.setText(list.get(position).get_room());
		textCourse.setSelected(true);
		textFaculty.setSelected(true);
		
		}
//		imageView.setImageResource(images[position]);

		return view;
	}

}
