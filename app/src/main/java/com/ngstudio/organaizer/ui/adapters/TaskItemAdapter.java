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
import com.ngstudio.organaizer.model.Task;

import java.util.List;

//import com.ngstudio.wayphoto.R;
//import com.ngstudio.wayphoto.model.PlacePhotoModel;

public class TaskItemAdapter extends BaseArrayAdapter<Task> {

    private LayoutInflater inflater;
    //private Context context;

    public TaskItemAdapter(Context context, int style, List<Task> list) {
        super(context, style, list);
        inflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        GalleryHolder holder;
        Log.d("SCROLL", "Position = " + position);
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_task, parent, false);
            holder = holderInitialise(convertView);
            convertView.setTag(holder);
        } else {
            holder = (GalleryHolder) convertView.getTag();
        }

        Task task = getItem(position);
        holder.tvName.setText(task.naming);
        if (getItem(position).isDeadLine()) {
            holder.llRootItemTask.setBackgroundColor(getContext().getResources().getColor(R.color.debugred));
        }

        return convertView;
    }

    @Override
    public Task getItem(int position) {
        return super.getItem(position);
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).getId();
    }

    private GalleryHolder holderInitialise(View view) {
        GalleryHolder holder = new GalleryHolder();
        holder.llRootItemTask = (LinearLayout ) view.findViewById(R.id.llRootItemTask);
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
