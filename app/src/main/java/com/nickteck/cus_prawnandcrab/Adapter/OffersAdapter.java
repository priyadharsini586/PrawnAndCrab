package com.nickteck.cus_prawnandcrab.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nickteck.cus_prawnandcrab.R;
import com.nickteck.cus_prawnandcrab.model.NotificationModel;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by admin on 5/18/2018.
 */

public class OffersAdapter  extends RecyclerView.Adapter<OffersAdapter.ViewHolder> {

    Context mContext;
    ArrayList<NotificationModel.Notification_list>notificationLists = new ArrayList<>();
    String TAG = OffersAdapter.class.getName();
    public OffersAdapter(Context mContext,ArrayList<NotificationModel.Notification_list>notificationLists)
    {
        this.mContext = mContext;
        this.notificationLists = notificationLists;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.offers_item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.list = notificationLists.get(position);
        holder.txtTitle.setText(holder.list.getTitle());
        holder.txtDes.setText(holder.list.getNotification());



    }

    @Override
    public int getItemCount() {

        return notificationLists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView txtTitle,txtDes;
        NotificationModel.Notification_list list;

        ViewHolder(View view) {
            super(view);

            txtTitle = (TextView) view.findViewById(R.id.txtTitle);
            txtDes = (TextView) view.findViewById(R.id.txtDes);
        }

    }
}
