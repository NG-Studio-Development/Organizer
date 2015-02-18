package com.ngstudio.organaizer.ui.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ngstudio.organaizer.R;
import com.ngstudio.organaizer.model.Enumeration;

import java.util.List;

public class EnumerationItemAdapter extends BaseArrayAdapter<Enumeration> {

    private LayoutInflater inflater;
    private int style;
    //private Context context;

    public EnumerationItemAdapter(Context context, int style, List<Enumeration> list) {
        super(context, style, list);
        this.style = style;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        GalleryHolder holder;
        Log.d("SCROLL", "Position = " + position);
        if (convertView == null) {
            convertView = inflater.inflate(style/*R.layout.item_task*/, parent, false);
            holder = holderInitialise(convertView);
            convertView.setTag(holder);
        } else {
            holder = (GalleryHolder) convertView.getTag();
        }

        Enumeration enumeration = getItem(position);
        holder.tvName.setText(enumeration.name);

        return convertView;
    }

    @Override
    public Enumeration getItem(int position) {
        return super.getItem(position);
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).getId();
    }

    private GalleryHolder holderInitialise(View view) {
        GalleryHolder holder = new GalleryHolder();
        holder.llRootItemTask = (LinearLayout) view.findViewById(R.id.llRootItemTask);
        holder.ivIcon = (ImageView) view.findViewById(R.id.ivIcon);
        holder.tvName = (TextView) view.findViewById(R.id.tvName);

        return holder;
    }

    static class GalleryHolder {

        LinearLayout llRootItemTask;
        ImageView ivIcon;
        TextView tvName;
        //View view;
    }
}