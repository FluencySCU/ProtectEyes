package cn.fluencycat.protecteyes.PickImg;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;
import java.util.List;

import cn.fluencycat.protecteyes.Const.CONTEXT;
import cn.fluencycat.protecteyes.R;


public class PickAdapter extends BaseAdapter {

  private LayoutInflater layoutInflater;
  private List<String> data = new ArrayList<>();

  public PickAdapter(Context context) {
    layoutInflater = LayoutInflater.from(context);
  }

  public void setNewData(List<String> data) {
    this.data.clear();
    if (data != null) {
      this.data.addAll(data);
    }
    notifyDataSetChanged();
  }


  @Override
  public int getCount() {
    return data.size();
  }

  @Override
  public Object getItem(int position) {
    return data.get(position);
  }

  @Override
  public long getItemId(int position) {
    return position;
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    ViewHolder holder;
    if (convertView == null) {
      convertView = layoutInflater.inflate(R.layout.image_item, null, false);
      holder = new ViewHolder();
      holder.imageView = (ImageView) convertView.findViewById(R.id.image);
      convertView.setTag(holder);
    } else {
      holder = (ViewHolder) convertView.getTag();
    }
    holder.imageView.setImageResource(R.drawable.ic_album_tick_blue);
    Glide.with(CONTEXT.context).load(data.get(position)).skipMemoryCache(true)
        .diskCacheStrategy(DiskCacheStrategy.NONE)
        .dontAnimate().into(holder.imageView);
    return convertView;
  }


  class ViewHolder {
    public ImageView imageView;
  }
}
