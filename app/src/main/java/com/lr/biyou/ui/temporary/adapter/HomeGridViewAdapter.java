package com.lr.biyou.ui.temporary.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lr.biyou.R;
import com.lr.biyou.utils.tool.UtilTools;

import java.util.List;
import java.util.Map;


public class HomeGridViewAdapter extends BaseAdapter {
	private List<Map<String, Object>> list;
	private Context context;
	private int imageWidth;
	public HomeGridViewAdapter(Context context, List<Map<String, Object>> list) {
		super(); 
		this.context = context;
		this.list = list;
		imageWidth = UtilTools.getScreenWidth(context)/2-15;
	} 

	@Override
	public int getCount() { 
		if (null != list) { 
			return list.size(); 
		} else { 
			return 0; 
		} 
	} 

	@Override
	public Object getItem(int position) {
		return list.get(position); 
	} 

	@Override
	public long getItemId(int position) { 
		return position; 
	} 

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder; 
		Map<String, Object> map = list.get(position);
		if (convertView == null) { 
			convertView = LayoutInflater.from(context).inflate(R.layout.home_grid_item, null);
			viewHolder = new ViewHolder(); 
			viewHolder.image = (ImageView) convertView.findViewById(R.id.home_img);
			viewHolder.name = (TextView) convertView.findViewById(R.id.home_name);
			convertView.setTag(viewHolder); 
		} else { 
			viewHolder = (ViewHolder) convertView.getTag(); 
		}

		Glide.with(context)
				.load(map.get("url")+"")
				.into(viewHolder.image);
			viewHolder.name.setText(map.get("name")+"");
			return convertView;
		}
		
	class ViewHolder { 
		public TextView name;
		public ImageView image;
	}
} 


