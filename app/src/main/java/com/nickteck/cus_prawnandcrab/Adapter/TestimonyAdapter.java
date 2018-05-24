package com.nickteck.cus_prawnandcrab.Adapter;

import android.app.Activity;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nickteck.cus_prawnandcrab.Db.Database;
import com.nickteck.cus_prawnandcrab.R;
import com.nickteck.cus_prawnandcrab.image_cache.ImageLoader;
import com.nickteck.cus_prawnandcrab.model.TestimonyDetails;
import com.nickteck.cus_prawnandcrab.model.UserRegisterDetails;
import com.nickteck.cus_prawnandcrab.utils.UtilsClass;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by admin on 5/21/2018.
 */

public class TestimonyAdapter extends BaseAdapter {

    private Activity activity;
    private List<TestimonyDetails> messages;
    ImageLoader imageLoader;
    Database databaseHandler;
    public TestimonyAdapter(Activity context, int resource, List<TestimonyDetails> objects) {
        this.activity = context;
        this.messages = objects;
        imageLoader=new ImageLoader(context.getApplicationContext());

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
        databaseHandler = new Database(activity);
        databaseHandler.getCustomerName();
        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        int layoutResource = 0; // determined by view type
        TestimonyDetails chatBubble = messages.get(position);
        int viewType = getItemViewType(position);



        if (chatBubble.getPhone().equals(Database.customer_phoneNo)) {
            if (chatBubble.isStatus()) {
                layoutResource = R.layout.right_approved_chat_bubble;
            }else
            {
                layoutResource = R.layout.right_unapproved_chat_bubble;
            }
            Log.e("right","right");
        } else {
            layoutResource = R.layout.left_bubble_chat;
            Log.e("left","left");
        }
        convertView = null;

        if (convertView != null) {
            holder = (ViewHolder) convertView.getTag();
        } else {
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

        Date date = null;
        SimpleDateFormat geivenDateFormat = new SimpleDateFormat("dd.MMM.yyyy HH:mm");
        String formattedDate= " ";
        try {

             date =geivenDateFormat.parse(chatBubble.getDate());
            geivenDateFormat.applyPattern("dd MMM yy/hh:mm a");
            formattedDate = geivenDateFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String[] date1 = formattedDate.split("/");
        holder.txtDate.setText(date1[0]);
        holder.txtTime.setText(date1[1]);
        //set message content
        holder.msg.setText(chatBubble.getMessage());
        holder.testUserName.setTextColor(chatBubble.getColorCode());
        holder.testUserName.setText(chatBubble.getName());
        if (chatBubble.getFrom().equals("server")) {
            if (chatBubble.getProfilePic() != null) {
                imageLoader.DisplayImage(chatBubble.getProfilePic(), holder.ic_profile, R.mipmap.ic_testimony_user_name);
            } else {
                holder.ic_profile.setImageDrawable(activity.getResources().getDrawable(R.mipmap.ic_testimony_user_name));
            }
            if (chatBubble.getTestimonyPic() != null) {
//            holder.imgMsg.setImageBitmap(chatBubble.getImageBitmap());
                holder.imgMsg.setVisibility(View.VISIBLE);
                imageLoader.DisplayImage(chatBubble.getTestimonyPic(), holder.imgMsg, R.mipmap.ic_testimony_user_name);
            } else
                holder.imgMsg.setVisibility(View.GONE);
        }else if (chatBubble.getFrom().equals("me")) {
            String userDetails = databaseHandler.getCustomerName();
            UtilsClass utilsClass = new UtilsClass();
            Bitmap bitmap = utilsClass.StringToBitMap(Database.profile_img);
            if (chatBubble.getTestimonyPic() != null) {
                holder.imgMsg.setImageBitmap(utilsClass.StringToBitMap(chatBubble.getTestimonyPic()));
            } else {
                holder.imgMsg.setVisibility(View.GONE);
            }
            if (bitmap != null) {
                holder.ic_profile.setImageBitmap(bitmap);
            }
        }


        return convertView;
    }
    @Override
    public CharSequence[] getAutofillOptions() {
        return new CharSequence[0];
    }

    private class ViewHolder {
        private TextView msg, testUserName, txtDate, txtTime;
        ImageView imgMsg;
        CircleImageView ic_profile;
        LinearLayout layBg;
    }
}
