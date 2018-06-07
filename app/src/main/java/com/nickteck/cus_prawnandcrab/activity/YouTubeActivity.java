package com.nickteck.cus_prawnandcrab.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.nickteck.cus_prawnandcrab.R;
import com.nickteck.cus_prawnandcrab.additional_class.AdditionalClass;
import com.nickteck.cus_prawnandcrab.service.MyApplication;
import com.nickteck.cus_prawnandcrab.service.NetworkChangeReceiver;
import com.nickteck.cus_prawnandcrab.utils.Config;

public class YouTubeActivity extends YouTubeBaseActivity  implements NetworkChangeReceiver.ConnectivityReceiverListener{

    YouTubePlayerView youTubePlayerView;
    private YouTubePlayer youTubeView;
    private String video_url;
    boolean isNetworkConnected;
    Config config = Config.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_you_tube);

        MyApplication.getInstance().setConnectivityListener(this);
        if (AdditionalClass.isNetworkAvailable(this)) {
            isNetworkConnected = true;
            init();
        }else {
            isNetworkConnected = false;
        }





    }

    private void init() {
        Intent intent = getIntent();
         video_url = intent.getStringExtra("video_url");
      //  Toast.makeText(this, ""+video_url, Toast.LENGTH_SHORT).show();
        youTubePlayerView = (YouTubePlayerView)findViewById(R.id.youtube_layout_activity);
        youTubePlayerView.initialize(Config.DEVELOPER_KEY, onInitializedListener);


    }

    private YouTubePlayer.OnInitializedListener onInitializedListener = new YouTubePlayer.OnInitializedListener() {
        @Override
        public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
            if (!b) {
                // loadVideo() will auto play video
                // Use cueVideo() method, if you don't want to play it automatically
                youTubeView = youTubePlayer;
                youTubePlayer.cueVideo(video_url,config.getMillSec());
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

        }
    };


    private YouTubePlayer.PlayerStateChangeListener playerStateChangeListener = new YouTubePlayer.PlayerStateChangeListener() {


        @Override
        public void onLoading() {

        }

        @Override
        public void onLoaded(String s) {

        }

        @Override
        public void onAdStarted() {

        }

        @Override
        public void onVideoStarted() {

        }

        @Override
        public void onVideoEnded() {

        }

        @Override
        public void onError(YouTubePlayer.ErrorReason errorReason) {
            Log.e("ended", errorReason.toString());

        }
    };

    private YouTubePlayer.PlaybackEventListener playbackEventListener = new YouTubePlayer.PlaybackEventListener() {


        @Override
        public void onPlaying() {

        }

        @Override
        public void onPaused() {

        }

        @Override
        public void onStopped() {

        }

        @Override
        public void onBuffering(boolean b) {

        }

        @Override
        public void onSeekTo(int i) {

        }
    };

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        if (isNetworkConnected != isConnected) {
            if (isConnected) {
                Toast.makeText(this, "Network Connected", Toast.LENGTH_LONG).show();

            } else {
                AdditionalClass.showSnackBar(YouTubeActivity.this);
            }
        }
        isNetworkConnected = isConnected;

    }

    /*@Override
    public void onPause() {
        super.onPause();
        if (youTubeView != null) {
            if (youTubeView.isPlaying())
                config.setMillSec(youTubeView.getCurrentTimeMillis());
            Log.e("pause", String.valueOf(youTubeView.getCurrentTimeMillis()));
        }
    }*/
}
