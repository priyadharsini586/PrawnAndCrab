package com.nickteck.cus_prawnandcrab.Adapter;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.nickteck.cus_prawnandcrab.R;
import com.nickteck.cus_prawnandcrab.additional_class.Constants;
import com.nickteck.cus_prawnandcrab.image_cache.ImageLoader;
import com.nickteck.cus_prawnandcrab.model.VipGalleryDetails;

import java.util.ArrayList;

/**
 * Created by admin on 5/28/2018.
 */

public class VipGalleryPagerAdapter extends PagerAdapter {
    private Activity activity;
    private Context context;
    private ArrayList<VipGalleryDetails> messages;
    private LayoutInflater layoutInflater;
    ImageLoader imageLoader;

    public VipGalleryPagerAdapter(Activity activity,Context context, ArrayList<VipGalleryDetails> messages) {
        this.activity = activity;
        this.context = context;
        this.messages = messages;
        this.layoutInflater = (LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        imageLoader=new ImageLoader(context.getApplicationContext());
    }

    @Override
    public int getCount() {
        return messages.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((View)object);
    }


    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = layoutInflater.inflate(R.layout.vip_page_viewer_adapter, container, false);
        ImageView imageView = (ImageView)view.findViewById(R.id.vip_image_page_adapter);
        Glide.with(context).load(Constants.VIP_GALLERY_IMAGE_URI+messages.get(position).getVip_gallery_lists().
                get(position).getImage()).into(imageView);

        container.addView(view);
        return view;

    }

    @Override
    public void destroyItem(ViewGroup container, int position,Object object) {
        ((ViewPager) container).removeView((View) object);
    }
}
