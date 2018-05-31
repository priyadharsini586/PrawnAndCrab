package com.nickteck.cus_prawnandcrab.fragment;


import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;
import com.google.android.youtube.player.YouTubePlayerView;
import com.nickteck.cus_prawnandcrab.R;
import com.nickteck.cus_prawnandcrab.activity.MenuNavigationActivity;
import com.nickteck.cus_prawnandcrab.model.ItemModel;
import com.nickteck.cus_prawnandcrab.utils.Config;

/**
 * A simple {@link Fragment} subclass.
 */
public class YouTubeVideoFragment extends Fragment {

    private static final int RECOVERY_DIALOG_REQUEST = 1;
    // YouTube player view
    View view;
    private YouTubePlayer youTubeView;
    YouTubePlayerSupportFragment youTubePlayerFragment;
    Activity activity;
    String TAG = "YoutubeFragment";
    private static final String VIDEO_URL = "param1";
    private static String Url;
    private static final String ARG_PARAM2 = "param2";
    Config config = Config.getInstance();
    YouTubePlayerView youTubePlayerView;

    // TODO: Rename and change types and number of parameters
    public static YouTubeVideoFragment newInstance(String videoUrl, int clickedPosition) {
        YouTubeVideoFragment fragment = new YouTubeVideoFragment();
        Bundle args = new Bundle();
        args.putString(VIDEO_URL, videoUrl);
        args.putInt(ARG_PARAM2, clickedPosition);
        fragment.setArguments(args);
        Url = videoUrl;
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_you_tube_video, container, false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();

        /*View decorView = getActivity().getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);*/




        /*getActivity().getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                        View.SYSTEM_UI_FLAG_FULLSCREEN);*/




        setOrienatation();
        init(view);
        return view;
    }

    private void setOrienatation() {

        ItemModel itemModel = ItemModel.getInstance();
        itemModel.setFromYoutube("true");

        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

    }
    private void init(View view) {

        youTubePlayerFragment = YouTubePlayerSupportFragment.newInstance();
       // youTubePlayerView = (YouTubePlayerView)view.findViewById(R.id.youtube_layout);

        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.add(R.id.youtube_layout, youTubePlayerFragment).commit();

        youTubePlayerFragment.initialize(Config.DEVELOPER_KEY, onInitializedListener);
    }

    private YouTubePlayer.OnInitializedListener onInitializedListener = new YouTubePlayer.OnInitializedListener() {


        @Override
        public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
            if (!b) {
                // loadVideo() will auto play video
                // Use cueVideo() method, if you don't want to play it automatically
                youTubeView = youTubePlayer;
                youTubePlayer.cueVideo(Url,config.getMillSec());
                youTubePlayer.setPlayerStateChangeListener(playerStateChangeListener);
                youTubePlayer.setPlaybackEventListener(playbackEventListener);
                // Hiding player controls
                youTubePlayer.setPlayerStyle(YouTubePlayer.PlayerStyle.DEFAULT);
                //youTubePlayer.getFullscreenControlFlags();
             //   youTubePlayer.addFullscreenControlFlag(YouTubePlayer.FULLSCREEN_FLAG_ALWAYS_FULLSCREEN_IN_LANDSCAPE);
                youTubePlayer.play();

            }
        }

        @Override
        public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
            if (youTubeInitializationResult.isUserRecoverableError()) {
                youTubeInitializationResult.getErrorDialog(getActivity(), RECOVERY_DIALOG_REQUEST).show();
            } else {
                String errorMessage = String.format(
                        "Error", youTubeInitializationResult.toString());
                Toast.makeText(getActivity(), errorMessage, Toast.LENGTH_LONG).show();
            }

        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RECOVERY_DIALOG_REQUEST) {
            // Retry initialization if user performed a recovery action
        }
    }

    private YouTubePlayer.PlayerStateChangeListener playerStateChangeListener = new YouTubePlayer.PlayerStateChangeListener() {


        @Override
        public void onLoading() {
            Log.e("loading", " loading");

        }

        @Override
        public void onLoaded(String s) {
            Log.e("loaded", "loaded ");

        }

        @Override
        public void onAdStarted() {

        }

        @Override
        public void onVideoStarted() {
            Log.e("onvideoStarted", "onvideoStarted ");

        }

        @Override
        public void onVideoEnded() {
            Log.e("ended", "ended ");

        }

        @Override
        public void onError(YouTubePlayer.ErrorReason errorReason) {
            Log.e("ended", errorReason.toString());

        }
    };

    private YouTubePlayer.PlaybackEventListener playbackEventListener = new YouTubePlayer.PlaybackEventListener() {

        @Override
        public void onBuffering(boolean arg0) {
        }

        @Override
        public void onPaused() {
            Log.e("auto_pause", "automatically paused");
        }

        @Override
        public void onPlaying() {
        }

        @Override
        public void onSeekTo(int arg0) {
        }

        @Override
        public void onStopped() {
            Log.e("auto_stop", "automatically paused");
        }

    };

    @Override
    public void onPause() {
        super.onPause();
        if (youTubeView != null) {
            if (youTubeView.isPlaying())
                config.setMillSec(youTubeView.getCurrentTimeMillis());
            Log.e("pause", String.valueOf(youTubeView.getCurrentTimeMillis()));
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
