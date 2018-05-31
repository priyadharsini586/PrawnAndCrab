package com.nickteck.cus_prawnandcrab.Adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.nickteck.cus_prawnandcrab.R;
import com.nickteck.cus_prawnandcrab.additional_class.Constants;
import com.nickteck.cus_prawnandcrab.fragment.VipGalleryFragment;
import com.nickteck.cus_prawnandcrab.model.VideoGalleryList;
import com.nickteck.cus_prawnandcrab.model.VipGalleryDetails;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by admin on 5/28/2018.
 */

public class VipGalleryAdapter extends RecyclerView.Adapter<VipGalleryAdapter.MyViewHolder>  {

    private Activity activity;
    private Context context;
    private ArrayList<VipGalleryDetails> messages;


    public VipGalleryAdapter(Activity activity, ArrayList<VipGalleryDetails> messages,Context mContext) {
        this.activity = activity;
        this.messages = messages;
        context = mContext;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.vip_gallery_adapter, parent, false);
        return new MyViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        // loading album cover using Glide library
        Glide.with(activity).load(Constants.VIP_GALLERY_IMAGE_URI+messages.get(position).getVip_gallery_lists().
                get(position).getImage()).into(holder.card_image_view);

        holder.image_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                openDialoge(position,holder,view);

            }
        });

    }

    private void openDialoge(int position, MyViewHolder holder, View view) {

        if (context != null) {

            AlertDialog.Builder alertbox = new AlertDialog.Builder(context);
            LayoutInflater inflater = activity.getLayoutInflater();
            View alertLayout = inflater.inflate(R.layout.open_image_dialoge, null);
            ImageView imageClose = (ImageView) alertLayout.findViewById(R.id.imgClose);


            ViewPager myPager = (ViewPager)alertLayout. findViewById(R.id.notifi_viewpager);
            alertbox.setView(alertLayout);
            VipGalleryPagerAdapter vipGalleryPagerAdapter = new VipGalleryPagerAdapter(activity,context,messages);
            myPager.setAdapter(vipGalleryPagerAdapter);




            final int   dotsCount = vipGalleryPagerAdapter.getCount();
            final View[]  dots = new View[dotsCount];
            LinearLayout viewPagerCountDots = (LinearLayout) alertLayout.findViewById(R.id.viewPagerCountDots);
            for (int i=0;i<dots.length;i++) {
                dots[i] = new View(context);


                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    dots[i].setBackground(context.getDrawable(R.drawable.default_dot));
                }else
                {
                    dots[i].setBackgroundDrawable(context.getResources().getDrawable(R.drawable.default_dot));
                }
                ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(new ViewGroup.LayoutParams(30, ViewGroup.LayoutParams.WRAP_CONTENT));
                dots[i].setLayoutParams(lp);
                viewPagerCountDots.addView(dots[i]);
            }
            myPager.setCurrentItem(position);
            dots[position].setBackgroundDrawable(context.getResources().getDrawable(R.drawable.selected_dot));
            myPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                    for (int i = 0; i < dots.length; i++){
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            dots[i].setBackground(context.getDrawable(R.drawable.default_dot));
                        }else
                        {
                            dots[i].setBackgroundDrawable(context.getResources().getDrawable(R.drawable.default_dot));
                        }
                    }
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        dots[position].setBackground(context.getDrawable(R.drawable.selected_dot));
                    }else
                    {
                        dots[position].setBackgroundDrawable(context.getResources().getDrawable(R.drawable.selected_dot));
                    }
                }

                @Override
                public void onPageSelected(int position) {

                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });


            final AlertDialog alert = alertbox.create();
           // alert.getWindow().setLayout(10,15);
            alert.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation_2;
            alert.show();

            imageClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alert.dismiss();
                }
            });
        }
    }


    @Override
    public int getItemCount() {
        return messages.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        ImageView card_image_view;
        LinearLayout image_select;


        public MyViewHolder(View view) {
            super(view);

            cardView = (CardView)view.findViewById(R.id.card_view);
            card_image_view = (ImageView)view.findViewById(R.id.card_image_view);
            image_select = (LinearLayout)view.findViewById(R.id.image_select);


        }
    }


}
