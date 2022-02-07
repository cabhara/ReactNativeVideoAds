package com.mascotmedia.Media;

import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.net.Uri;
import android.os.Bundle;
import androidx.multidex.MultiDex;
import android.app.Service;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.uimanager.events.RCTEventEmitter;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.ExoPlayer;//
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.ext.ima.ImaAdsLoader;
import com.google.android.exoplayer2.RenderersFactory;
import com.google.android.exoplayer2.source.DefaultMediaSourceFactory;
import com.google.android.exoplayer2.source.hls.DefaultHlsExtractorFactory;
import com.google.android.exoplayer2.source.hls.HlsExtractorFactory;
import com.google.android.exoplayer2.source.MediaSourceFactory;
import com.google.android.exoplayer2.source.ads.AdsLoader;//
import com.google.android.exoplayer2.ui.PlayerNotificationManager;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

// replace with your view's import
//import com.mypackage.CustomView;

public class VideoFragment extends Fragment {

    private PlayerView playerView;
    private SimpleExoPlayer player;
    private ImaAdsLoader adsLoader;
    private String videoUrl;
    private String adTagUrl;
    private int reactNativeViewId;
    private ReactApplicationContext reactContext;

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public void setAdTagUrl(String adTagUrl) {
        this.adTagUrl = adTagUrl;
    }

    public void setReactNativeViewId(int reactNativeViewId) {
        this.reactNativeViewId = reactNativeViewId;
    }

    public void setReactContext(ReactApplicationContext reactContext) {
        this.reactContext = reactContext;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        return inflater.inflate(R.layout.video_view,parent,false);
    }

    private void releasePlayer() {
        adsLoader.setPlayer(null);
        playerView.setPlayer(null);
        if(player!=null) player.release();
        player = null;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // do any logic that should happen in an `onCreate` method
        MultiDex.install(requireContext());

        playerView = (PlayerView) view.findViewById(R.id.player_view);
        // Create an AdsLoader.
        adsLoader = new ImaAdsLoader.Builder(requireContext()).build();

    }

    private void initializePlayer() {
        // Set up the factory for media sources, passing the ads loader and ad view providers.
        DataSource.Factory dataSourceFactory =
                new DefaultDataSourceFactory(requireContext(), Util.getUserAgent(requireContext(), getString(R.string.app_name)));

        MediaSourceFactory mediaSourceFactory =
                new DefaultMediaSourceFactory(dataSourceFactory)
                        //new DefaultHlsExtractorFactory(dataSourceFactory)
                        .setAdsLoaderProvider(unusedAdTagUri -> adsLoader)
                        .setAdViewProvider(playerView);

        // Create a SimpleExoPlayer and set it as the player for content and ads.
        player = new SimpleExoPlayer.Builder(requireContext()).setMediaSourceFactory(mediaSourceFactory).build();
        playerView.setPlayer(player);
        adsLoader.setPlayer(player);

        MediaItem mediaItem;


            //for no preroll: video and maybe adTag
            // Create the MediaItem to play, specifying the content URI and ad tag URI.

            //check for valid link in RN!
            Uri contentUri = Uri.parse(videoUrl);

            Uri adTagUri = null;
            if (adTagUrl != null && !adTagUrl.equals("")) {
                adTagUri = Uri.parse(adTagUrl);
                mediaItem = new MediaItem.Builder().setUri(contentUri).setAdTagUri(adTagUri).build();
            } else{
                mediaItem = new MediaItem.Builder().setUri(contentUri).build();
            }
            // Prepare the content and ad to be played with the SimpleExoPlayer.
            player.setMediaItem(mediaItem);


        player.prepare();

        // Set PlayWhenReady. If true, content and ads will autoplay.
        player.setPlayWhenReady(true);

    }

    @Override
    public void onPause() {
        super.onPause();
        // do any logic that should happen in an `onPause` method
        // e.g.: customView.onPause();
        if (playerView != null) {
            playerView.onPause();
        }
        releasePlayer();
    }

    @Override
    public void onResume() {
        super.onResume();
        // do any logic that should happen in an `onResume` method
        // e.g.: customView.onResume();
        initializePlayer();
        if (playerView != null) {
            playerView.onResume();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // do any logic that should happen in an `onDestroy` method
        // e.g.: customView.onDestroy();
        if (playerView != null) {
            playerView.onPause();
        }
        releasePlayer();
        adsLoader.release();
    }
}

