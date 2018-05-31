package com.nickteck.cus_prawnandcrab.fragment;


import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.VideoView;

import com.nickteck.cus_prawnandcrab.Adapter.VideoGalleryAdapter;
import com.nickteck.cus_prawnandcrab.Adapter.VipGalleryAdapter;
import com.nickteck.cus_prawnandcrab.R;
import com.nickteck.cus_prawnandcrab.additional_class.Constants;
import com.nickteck.cus_prawnandcrab.additional_class.HelperClass;
import com.nickteck.cus_prawnandcrab.api.ApiClient;
import com.nickteck.cus_prawnandcrab.api.ApiInterface;
import com.nickteck.cus_prawnandcrab.model.ItemModel;
import com.nickteck.cus_prawnandcrab.model.VideoGalleryList;
import com.nickteck.cus_prawnandcrab.model.VipGalleryDetails;
import com.nickteck.cus_prawnandcrab.service.MyApplication;
import com.nickteck.cus_prawnandcrab.service.NetworkChangeReceiver;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class VideoGalleryFragment extends Fragment implements NetworkChangeReceiver.ConnectivityReceiverListener {

    String youtubeUrl = "https://www.youtube.com/watch?v=21T5VngB6D8&list=PLsyeobzWxl7p-lZvWabkVJdM_UVURhUh4&index=2";
    View view;
    private ImageButton imageview_play;
    private ImageView imageView;
    private RecyclerView video_recyclerview;
    boolean netWorkConnection;
    ProgressBar progress;
    RelativeLayout mainView;
    ApiInterface apiInterface;
    ArrayList<VideoGalleryList.VideoGalleryListDetails> videoGalleryLists;
    VideoGalleryAdapter videoGalleryAdapter;


    public VideoGalleryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_video_gallery, container, false);

        init(view);

        MyApplication.getInstance().setConnectivityListener(this);
        if (HelperClass.isNetworkAvailable(getActivity())) {
            netWorkConnection = true;
            progress.setVisibility(View.VISIBLE);
            getVideoGalleryList();

        }else {
            netWorkConnection = false;
        }

      getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        return view;
    }



    private void init(View view) {
        videoGalleryLists = new ArrayList<>();
        /*imageView = (ImageView)view.findViewById(R.id.thumbnail_image_view);
        imageview_play = (ImageButton) view.findViewById(R.id.play_button);*/
        video_recyclerview = (RecyclerView)view.findViewById(R.id.video_recyclerview);
        progress = (ProgressBar) view.findViewById(R.id.progress);
        progress.setVisibility(View.GONE);

        videoGalleryAdapter = new VideoGalleryAdapter(getActivity(),videoGalleryLists,getActivity());
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 2);
        video_recyclerview.setLayoutManager(mLayoutManager);
        video_recyclerview.setAdapter(videoGalleryAdapter);

    }

    private void getVideoGalleryList() {
        if (netWorkConnection) {
            apiInterface = ApiClient.getClient().create(ApiInterface.class);
            Call<VideoGalleryList> call = apiInterface.getVideoGalleryList();
            call.enqueue(new Callback<VideoGalleryList>() {

                @Override
                public void onResponse(Call<VideoGalleryList> call, Response<VideoGalleryList> response) {
                    if (response.isSuccessful()) {
                        VideoGalleryList videoGalleryList = response.body();
                        if (videoGalleryList.getStatus_code().equals(Constants.SUCCESS)) {
                            if(videoGalleryList.getVideo_Gallery_list() != null){
                                for(int i=0; i < videoGalleryList.getVideo_Gallery_list().size(); i++)
                                {
                                    videoGalleryList.getVideo_Gallery_list().get(i).getVideo_gal_id();
                                    videoGalleryList.getVideo_Gallery_list().get(i).getTitle();
                                    videoGalleryList.getVideo_Gallery_list().get(i).getDescription();
                                    videoGalleryList.getVideo_Gallery_list().get(i).getVideo();

                                    VideoGalleryList.VideoGalleryListDetails videoGalleryListDetails = videoGalleryList.getVideo_Gallery_list().get(i);
                                    videoGalleryLists.add(videoGalleryListDetails);

                                }
                                videoGalleryAdapter.notifyDataSetChanged();
                                progress.setVisibility(View.GONE);
                            }
                        }
                    }

                }

                @Override
                public void onFailure(Call<VideoGalleryList> call, Throwable t) {
                    Toast.makeText(getActivity(), "Server error", Toast.LENGTH_SHORT).show();

                }
            });

        }
    }





    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        netWorkConnection = isConnected;
        if (mainView != null) {
            if (!isConnected)
                HelperClass.showTopSnackBar(mainView,"Network not connected");
        }

    }

    /*private void getVideoId() {
        Uri uri = Uri.parse(youtubeUrl);
        youtubeUrl = uri.getQueryParameter("v");
        getFirstFrameImage(youtubeUrl);
    }

    private void getFirstFrameImage(String youtubeUrl) {
        String url = "http://img.youtube.com/vi/" + youtubeUrl +"/0.jpg";
        if(url != null){
            imageview_play.setVisibility(View.VISIBLE);
            Picasso.with(getActivity())
                    .load(url)
                    .placeholder(R.drawable.camera_icon)
                    .into(imageView);

        }
    }*/

}
