package com.nickteck.cus_prawnandcrab.Adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nickteck.cus_prawnandcrab.R;
import com.nickteck.cus_prawnandcrab.model.TestimonyDetails;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by admin on 5/21/2018.
 */

public class TestAdapter extends BaseAdapter {
    private Activity activity;
    private ArrayList<String> messages;

    public TestAdapter(Activity activity,int resource, ArrayList<String> messages) {
        this.activity = activity;
        this.messages = messages;
    }

    @Override
    public int getCount() {
        return messages != null? messages.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return messages.get(position);
    }

    @Override
    public long getItemId(int position) {
        return messages.hashCode();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        int layoutResource;
        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        convertView = null;
        if (convertView != null) {
            holder = (ViewHolder) convertView.getTag();
        } else {
            layoutResource = R.layout.right_approved_chat_bubble;
            convertView = inflater.inflate(layoutResource, parent, false);
            holder = new ViewHolder();
            holder.msg = (TextView) convertView.findViewById(R.id.txt_msg);
            holder.txtTime = (TextView) convertView.findViewById(R.id.txtTime);
            holder.txtDate = (TextView) convertView.findViewById(R.id.txtDate);
            holder.imgMsg = (ImageView) convertView.findViewById(R.id.imgMsg);
            holder.testUserName = (TextView) convertView.findViewById(R.id.testUserName);
            holder.ic_profile = (CircleImageView) convertView.findViewById(R.id.ic_profile);
            holder.layBg = (LinearLayout) convertView.findViewById(R.id.layBg);
            convertView.setTag(holder);
        }
        //set message content
        holder.msg.setText(messages.get(position));
        return convertView;
    }

    private class ViewHolder {
        private TextView msg, testUserName, txtDate, txtTime;
        ImageView imgMsg;
        CircleImageView ic_profile;
        LinearLayout layBg;


    }
}
