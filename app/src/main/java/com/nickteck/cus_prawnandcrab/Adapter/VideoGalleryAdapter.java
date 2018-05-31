package com.nickteck.cus_prawnandcrab.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.nickteck.cus_prawnandcrab.R;
import com.nickteck.cus_prawnandcrab.activity.YouTubeActivity;
import com.nickteck.cus_prawnandcrab.additional_class.AdditionalClass;
import com.nickteck.cus_prawnandcrab.additional_class.Constants;
import com.nickteck.cus_prawnandcrab.fragment.YouTubeVideoFragment;
import com.nickteck.cus_prawnandcrab.interfaceFol.VideoIdItemListener;
import com.nickteck.cus_prawnandcrab.model.VideoGalleryList;
import com.nickteck.cus_prawnandcrab.model.VipGalleryDetails;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by admin on 5/29/2018.
 */

public class VideoGalleryAdapter extends RecyclerView.Adapter<VideoGalleryAdapter.ViewHolder> implements VideoIdItemListener{
    private Activity activity;
    private Context context;
    private ArrayList<VideoGalleryList.VideoGalleryListDetails> videoGalleryLists;
    private String videoUrl_String;
    private int videoItemClickPosition;
    private String concatUrl;
    private String getFull_url;
    private String final1;


    public VideoGalleryAdapter(Activity mactivity, ArrayList<VideoGalleryList.VideoGalleryListDetails> mvideoGalleryLists,Context mContext) {
        this.activity = mactivity;
        this.videoGalleryLists = mvideoGalleryLists;
        context = mContext;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.video_gallery_adapter, parent, false);
        return new ViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        String youtubeUrl = videoGalleryLists.get(position).getVideo();
        String[] splited = youtubeUrl.split("//",2);
        String part1 = splited[0];
        String part2 = splited[1];

        String[] finalyoutubeUrl = part2.split("/",2);
        String fina1 = finalyoutubeUrl[0];
         final1 = finalyoutubeUrl[1];

         concatUrl = "https://www.youtube.com/watch?v="+final1+"&feature="+fina1;
         getFull_url = "https://www.youtube.com/watch?v="+final1+"&feature="+fina1;
        final Uri uri = Uri.parse(concatUrl);
        concatUrl = uri.getQueryParameter("v");
        final String url = "http://img.youtube.com/vi/" + concatUrl +"/0.jpg";
        if(url != null) {
            holder.play_image_view.setVisibility(View.VISIBLE);
            Picasso.with(activity)
                    .load(url)
                    .placeholder(R.drawable.camera_icon)
                    .into(holder.thumbnail_image_view);
        }

        holder.card_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                videoUrl_String = url;
                videoItemClickPosition = position;
                Intent intent = new Intent(context, YouTubeActivity.class);
                intent.putExtra("video_url",final1);
                context.startActivity(intent);


                /*YouTubeVideoFragment youTubeVideoFragment = new YouTubeVideoFragment();
                AdditionalClass.replaceFragment(youTubeVideoFragment, Constants.YOU_TUBE_VIDEO_FRAGMENT,(AppCompatActivity)context);
                YouTubeVideoFragment.newInstance(final1,videoItemClickPosition);*/



            }
        });


    }

    @Override
    public int getItemCount() {
        return videoGalleryLists.size();
    }

    @Override
    public void videoIdItem(String url, Integer ItemClickposition) {

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout linearLayout;
        CardView card_view;
        ImageView thumbnail_image_view;
        ImageView play_image_view;

        public ViewHolder(View itemView) {
            super(itemView);

            linearLayout = (LinearLayout)itemView.findViewById(R.id.video_Linearlayout);
            card_view  = (CardView)itemView.findViewById(R.id.card_view);
            thumbnail_image_view = (ImageView)itemView.findViewById(R.id.thumbnail_image_view);
            play_image_view = (ImageView)itemView.findViewById(R.id.play_button);

        }




    }
}
